package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.*;
import com.example.cabonerfbe.dto.*;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.enums.MessageConstants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.*;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CalculateProjectRequest;
import com.example.cabonerfbe.request.CreateProjectRequest;
import com.example.cabonerfbe.request.UpdateProjectDetailRequest;
import com.example.cabonerfbe.response.CreateProjectResponse;
import com.example.cabonerfbe.response.GetAllProjectResponse;
import com.example.cabonerfbe.response.GetImpactForAllProjectResponse;
import com.example.cabonerfbe.response.ProjectCalculationResponse;
import com.example.cabonerfbe.services.ProjectService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    private static final int PAGE_INDEX_ADJUSTMENT = 1;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private ProjectImpactValueRepository projectImpactValueRepository;
    @Autowired
    private ImpactMethodCategoryRepository impactMethodCategoryRepository;
    @Autowired
    private ProjectConverter projectConverter;
    @Autowired
    private ProjectImpactValueConverter projectImpactValueConverter;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrganizationRepository oRepository;
    @Autowired
    private LifeCycleImpactAssessmentMethodRepository methodRepository;
    @Autowired
    private LifeCycleImpactAssessmentMethodConverter methodConverter;
    @Autowired
    private ImpactCategoryConverter categoryConverter;
    @Autowired
    private UnitConverter unitConverter;
    @Autowired
    private ProcessRepository processRepository;
    @Autowired
    private ProcessConverter processConverter;
    @Autowired
    private ProcessImpactValueRepository processImpactValueRepository;
    @Autowired
    private ExchangesRepository exchangesRepository;
    @Autowired
    private ExchangesConverter exchangesConverter;
    @Autowired
    private ConnectorRepository connectorRepository;
    @Autowired
    private ConnectorConverter connectorConverter;
    @Autowired
    private ProcessServiceImpl processService;
    @Autowired
    private EmissionSubstanceConverter emissionSubstanceConverter;
    @Autowired
    private ProcessImpactValueServiceImpl processImpactValueService;
    @Autowired
    private CarbonIntensityRepository ciRepository;
    @Autowired
    private CarbonIntensityConverter ciConverter;
    @Autowired
    private ImpactCategoryRepository icRepository;
    @Autowired
    private UserOrganizationRepository uoRepository;

//    private final ExecutorService executorService = Executors.newFixedThreadPool(17);

    @Override
    public List<Project> getProjectListByMethodId(UUID id) {
//        return projectRepository.getProjectLevelDetail(id);
        return null;
    }

    @Override
    public ProjectCalculationResponse calculateProject(CalculateProjectRequest request) {
        UUID projectId = request.getProjectId();
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_PROJECT_FOUND, Collections.EMPTY_LIST));
        var contributionBreakdown = processImpactValueService.computeSystemLevelOfProject(projectId);
        var response = projectConverter.fromGetProjectDtoToCalculateResponse(getProject(project));
        response.setContributionBreakdown(contributionBreakdown);
        return response;

    }

    @Override
    public CreateProjectResponse createProject(UUID userId, CreateProjectRequest request) {

        Users user = userRepository.findByIdWithStatus(userId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.USER_NOT_FOUND, Collections.EMPTY_LIST)
        );

        Organization organization = oRepository.findById(request.getOrganizationId()).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_ORGANIZATION_FOUND, Collections.EMPTY_LIST)
        );

        LifeCycleImpactAssessmentMethod method = methodRepository.findByIdAndStatus(request.getMethodId(), Constants.STATUS_TRUE).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_IMPACT_METHOD_FOUND, Collections.EMPTY_LIST)
        );

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setLocation(request.getLocation());
        project.setUser(user);
        project.setFavorite(false);
        project.setOrganization(organization);
        project.setLifeCycleImpactAssessmentMethod(method);

        project = projectRepository.save(project);

        return CreateProjectResponse.builder()
                .projectId(project.getId())
                .build();
    }

    @Override
    public GetAllProjectResponse getAllProject(int pageCurrent, int pageSize, UUID userId, UUID methodId, UUID organizationId) {

        Pageable pageable = PageRequest.of(pageCurrent - PAGE_INDEX_ADJUSTMENT, pageSize);

        UserOrganization uo = uoRepository.findByUserAndOrganization(organizationId,userId)
                .orElseThrow(() -> CustomExceptions.unauthorized(MessageConstants.USER_NOT_BELONG_TO_ORGANIZATION));

        Page<Project> projects;
        if (methodId == null) {
            projects = projectRepository.findAll(userId, organizationId, pageable);
        } else {
            projects = projectRepository.sortByMethod(userId, organizationId, methodId, pageable);
        }

        if (projects.isEmpty()) {
            GetAllProjectResponse response = new GetAllProjectResponse();
            response.setPageCurrent(0);
            response.setPageSize(0);
            response.setTotalPage(0);
            response.setProjects(Collections.EMPTY_LIST);
            return response;
        }

        int totalPage = projects.getTotalPages();
        if (pageCurrent > totalPage) {
            throw CustomExceptions.validator(Constants.RESPONSE_STATUS_ERROR, Map.of("currentPage", MessageConstants.CURRENT_PAGE_EXCEED_TOTAL_PAGES));
        }


        List<ProjectDto> list = new ArrayList<>();
        for (Project project : projects) {
            ProjectDto projectDto = projectConverter.toDto(project);

            projectDto.setImpacts(converterProject(projectImpactValueRepository.findAllByProjectId(project.getId())));

            list.add(projectDto);
        }

        GetAllProjectResponse response = new GetAllProjectResponse();
        response.setPageCurrent(pageCurrent);
        response.setPageSize(pageSize);
        response.setTotalPage(totalPage);
        response.setProjects(list);
        return response;
    }

    @Override
    public GetProjectByIdDto getById(UUID id, UUID userId) {
        Project project = projectRepository.findById(id).orElseThrow(
                () -> CustomExceptions.notFound(MessageConstants.NO_PROJECT_FOUND, Collections.EMPTY_LIST)
        );

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.USER_NOT_FOUND));

        UserOrganization uo = uoRepository.findByUserAndOrganization(project.getOrganization().getId(),userId)
                .orElseThrow(() -> CustomExceptions.unauthorized(MessageConstants.USER_NOT_BELONG_TO_ORGANIZATION));
        return getProject(project);
    }

    @NotNull
    public GetProjectByIdDto getProject(Project project) {
        GetProjectByIdDto dto = new GetProjectByIdDto();

        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setLocation(project.getLocation());
        dto.setMethod(methodConverter.fromMethodToMethodDto(project.getLifeCycleImpactAssessmentMethod()));
        dto.setImpacts(converterProject(projectImpactValueRepository.findAllByProjectId(project.getId())));
        dto.setProcesses(processService.getAllProcessesByProjectId(project.getId()));
        dto.setConnectors(connectorConverter.fromListConnectorToConnectorDto(connectorRepository.findAllByProject(project.getId())));
        return dto;
    }

    @Override
    public List<CarbonIntensityDto> getIntensity(UUID projectId) {
        Project p = projectRepository.findById(projectId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_PROJECT_FOUND, Collections.EMPTY_LIST));
        ProjectImpactValue value = processImpactValueRepository.findCO2(projectId);
        List<CarbonIntensity> ci = ciRepository.findAll();

        ci.forEach(c ->
                c.setValue(c.getValue().multiply(value.getValue()).setScale(2, RoundingMode.HALF_UP))
        );


        return ci.stream().map(ciConverter::toDto).collect(Collectors.toList());
    }

    @Override
    public int countAllProject() {
        return projectRepository.findAllByStatus();
    }

    @Override
    public List<GetImpactForAllProjectResponse> countImpactInDashboard() {
        List<ImpactCategory> ic = icRepository.findAllByStatus(true);
        return ic.stream()
                .map(category -> {
                    return new GetImpactForAllProjectResponse(category.getName(), projectImpactValueRepository.getSumImpact(category.getId()));
                }).collect(Collectors.toList());
    }

    @Override
    public UpdateProjectDto updateFavorite(UUID projectId) {
        Project p = projectRepository.findById(projectId)
                .orElseThrow(() -> CustomExceptions.notFound(MessageConstants.NO_PROJECT_FOUND));

        p.setFavorite(!p.getFavorite());

        return projectConverter.fromDetailToDto(projectRepository.save(p));
    }

    @Override
    public UpdateProjectDto updateDetail(UUID id, UpdateProjectDetailRequest request) {
        // Validate that at least one field is updated
        boolean isAnyFieldUpdated = !isNullOrEmpty(request.getName())
                || !isNullOrEmpty(request.getDescription())
                || !isNullOrEmpty(request.getLocation());

        if (!isAnyFieldUpdated) {
            throw CustomExceptions.badRequest("Update at least 1 field", Collections.EMPTY_LIST);
        }

        // Find the project by ID
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> CustomExceptions.badRequest(MessageConstants.NO_PROJECT_FOUND, Collections.EMPTY_LIST));

        // Update fields if provided
        Optional.ofNullable(request.getName()).filter(name -> !name.isEmpty()).ifPresent(project::setName);
        Optional.ofNullable(request.getDescription()).filter(desc -> !desc.isEmpty()).ifPresent(project::setDescription);
        Optional.ofNullable(request.getLocation()).filter(loc -> !loc.isEmpty()).ifPresent(project::setLocation);

        return projectConverter.fromDetailToDto(projectRepository.save(project));
    }

    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }


    @Override
    public List<Project> deleteProject(UUID id) {
        Optional<Project> project = projectRepository.findById(id);
        if (project.isEmpty()) {
            throw CustomExceptions.badRequest(MessageConstants.NO_PROJECT_FOUND, Collections.EMPTY_LIST);
        }

        project.get().setStatus(false);
        projectRepository.save(project.get());
        return new ArrayList<>();
    }

    @Transactional
    @Override
    public GetProjectByIdDto changeProjectMethod(UUID projectId, UUID methodId) {
        Project project = projectRepository.findById(projectId).orElseThrow(
                () -> CustomExceptions.badRequest(MessageConstants.NO_PROJECT_FOUND, Collections.EMPTY_LIST)
        );
        if (!methodId.equals(project.getLifeCycleImpactAssessmentMethod().getId())) {
            LifeCycleImpactAssessmentMethod method = methodRepository.findByIdAndStatus(methodId, Constants.STATUS_TRUE).orElseThrow(
                    () -> CustomExceptions.badRequest(MessageConstants.NO_IMPACT_METHOD_FOUND, Collections.EMPTY_LIST)
            );
            project.setLifeCycleImpactAssessmentMethod(method);
            processImpactValueService.computeProcessImpactValueOfProject(projectRepository.save(project));
        }
        return getProject(project);
    }

    @Override
    public ResponseEntity<Resource> exportProject(UUID projectId) {
        Project p = projectRepository.findById(projectId)
                .orElseThrow(() -> CustomExceptions.badRequest(MessageConstants.NO_PROJECT_FOUND, Collections.EMPTY_LIST));


        byte[] file = createFile(p);
        ByteArrayResource resource = new ByteArrayResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + p.getName() + ".xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(file.length)
                .body(resource);
    }

    public List<ProjectImpactDto> converterProject(List<ProjectImpactValue> list) {
        return list.stream()
                .map(x -> {
                    ProjectImpactDto p = new ProjectImpactDto();
                    p.setId(x.getId());
                    p.setValue(x.getValue());
                    p.setMethod(methodConverter.fromMethodToMethodDto(
                            x.getImpactMethodCategory().getLifeCycleImpactAssessmentMethod()));
                    p.setImpactCategory(categoryConverter.fromProjectToImpactCategoryDto(
                            x.getImpactMethodCategory().getImpactCategory()));
                    return p;
                })
                .collect(Collectors.toList());
    }

    private byte[] createFile(Project p) {
//        GetProjectByIdDto data = this.getProject(p);
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet guide = workbook.createSheet("Guide");
            XSSFSheet lcaOverview = workbook.createSheet("LCA Overview");
            XSSFSheet lciResults = workbook.createSheet("LCI Results");
            XSSFSheet lciaResults = workbook.createSheet("LCIA Results");

            CellStyle boldStyle = createStyle(workbook);

            createGuideSheet(guide, workbook, p, boldStyle);
            createLcaOverviewSheet(lcaOverview, workbook, p, boldStyle);
            createLciResultsSheet(lciResults, workbook, p, boldStyle);
            createLciaResultsSheet(lciaResults, workbook, p, boldStyle);
            // Write the workbook to a byte array
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            return outputStream.toByteArray();
        } catch (IOException e) {
            return null;
        }
    }

    private void createGuideSheet(XSSFSheet guide, XSSFWorkbook workbook, Project p, CellStyle boldStyle) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Các tiêu đề cần in đậm
        Set<String> boldHeaders = new HashSet<>(Arrays.asList(
                "Exported Cabonerf LCA Model Results",
                "What is included in this workbook?",
                "LCA Overview",
                "LCI Results",
                "LCIA Results"
        ));

        // Dữ liệu của tab Guide
        Object[][] guideData = {
                {"Exported Cabonerf LCA Model Results"},
                {"Date Created", p.getCreatedAt().format(formatter)},
                {"Created Using:", "Cabonerf"},
                {},
                {"What is included in this workbook?"},
                {"This workbook provides a static summary of a Life Cycle Assessment (LCA) model produced in CarbonGraph."},
                {"It is structured to provide you with an overview of the environmental impacts associated with a product or process LCA."},
                {"For a dynamic version of this model, please open the LCA in the CarbonGraph platform."},
                {"Included below is a guide to navigating through the different tabs of the workbook."},
                {},
                {"LCA Overview"},
                {"Purpose: This tab provides a high-level summary of the LCA activity that was modeled, including details on the scope, objectives, and system boundaries."},
                {"How to Use: Review this section first to gain context on the LCA model's background, the product or process under assessment, and any specific goals of the assessment."},
                {},
                {"LCI Results"},
                {"Purpose: LCI (Life Cycle Inventory) Results tab contains detailed data on the net flows and exchanges (e.g., energy use, material inputs, emissions) calculated for the entire LCA."},
                {"How to Use: Explore this tab for insight into the material exchanges that form the basis of the LCA. These exchanges represent the net inputs and outputs across the complete system boundaries of the LCA."},
                {},
                {"LCIA Results"},
                {"Purpose: The LCIA (Life Cycle Impact Assessment) Results tab provides a summary of the net environmental impacts calculated for the entire LCA."},
                {"How to Use: The LCIA represents an alternative view of the LCI results where the amounts of material exchanges are scaled into impact categories (e.g., global warming potential, water usage, eutrophication) based on characterization factors for these impacts."}
        };

        // Viết dữ liệu vào sheet Guide
        int rowCount = 1;
        for (Object[] rowData : guideData) {
            Row row = guide.createRow(rowCount++);
            int columnCount = 1;
            for (Object field : rowData) {
                Cell cell = row.createCell(columnCount++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                    // Áp dụng style in đậm nếu thuộc danh sách tiêu đề cần in đậm
                    if (boldHeaders.contains(field)) {
                        cell.setCellStyle(boldStyle);
                    }
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof Double) {
                    cell.setCellValue((Double) field);
                }
            }
        }

        // Điều chỉnh độ rộng cột cho dễ đọc
        for (int i = 0; i < 5; i++) {
            guide.autoSizeColumn(i);
        }
    }

    private void createLcaOverviewSheet(XSSFSheet lcaOverview, XSSFWorkbook workbook, Project p, CellStyle boldStyle) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Các tiêu đề cần in đậm
        Set<String> boldHeaders = new HashSet<>(Arrays.asList(
                "LCA Process Overview - Cabonerf Excel Export",
                "Field",
                "Value"
        ));

        // Dữ liệu của tab Guide
        Object[][] guideData = {
                {"LCA Process Overview - Cabonerf Excel Export"},
                {},
                {"Field", "Value"},
                {"Project ID", p.getId()},
                {"Name", p.getName()},
                {"Description", p.getDescription()},
                {"Last change", p.getModifiedAt().format(formatter)},
                {"Location", p.getLocation()}
        };

        // Viết dữ liệu vào sheet Guide
        int rowCount = 1;
        for (Object[] rowData : guideData) {
            Row row = lcaOverview.createRow(rowCount++);
            int columnCount = 1;
            for (Object field : rowData) {
                Cell cell = row.createCell(columnCount++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                    // Áp dụng style in đậm nếu thuộc danh sách tiêu đề cần in đậm
                    if (boldHeaders.contains(field)) {
                        cell.setCellStyle(boldStyle);
                    }
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof Double) {
                    cell.setCellValue((Double) field);
                }
            }
        }

        // Điều chỉnh độ rộng cột cho dễ đọc
        for (int i = 0; i < 5; i++) {
            lcaOverview.autoSizeColumn(i);
        }
    }

    private void createLciResultsSheet(XSSFSheet lciResults, XSSFWorkbook workbook, Project p, CellStyle boldStyle) {
        // Lấy danh sách exchanges
        List<Exchanges> exchanges = exchangesRepository.findElementaryExchangeByProject(p.getId());
        List<ExchangesDto> data = aggregateExchangesWithFields(exchanges);

        // Tiêu đề cần in đậm
        Set<String> boldHeaders = new HashSet<>(Arrays.asList(
                "LCA Process Exchange Summary - Cabonerf Excel Export",
                "Amount",
                "Unit",
                "Input",
                "Name",
                "Type",
                "Compartment"
        ));

        // Ghi dữ liệu tiêu đề vào Excel
        Object[][] headerData = {
                {"LCA Process Exchange Summary - Cabonerf Excel Export"},
                {},
                {"Amount", "Unit", "Input", "Name", "Type", "Compartment"}
        };

        int rowCount = 1; // Khởi tạo vị trí hàng đầu tiên
        for (Object[] rowData : headerData) {
            Row row = lciResults.createRow(rowCount++);
            int columnCount = 1;
            for (Object field : rowData) {
                Cell cell = row.createCell(columnCount++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                    // Kiểm tra và áp dụng in đậm nếu là tiêu đề
                    if (boldHeaders.contains(field)) {
                        cell.setCellStyle(boldStyle);
                    }
                }
            }
        }

        // Ghi dữ liệu từ danh sách ExchangesDto
        for (ExchangesDto dto : data) {
            Row row = lciResults.createRow(rowCount++);
            int columnCount = 1;

            // Điền dữ liệu từ DTO vào các cột
            row.createCell(columnCount++).setCellValue(dto.getValue().doubleValue()); // Amount
            row.createCell(columnCount++).setCellValue(dto.getUnit().getName()); // Unit
            row.createCell(columnCount++).setCellValue(dto.isInput() ? "TRUE" : "FALSE"); // Input
            row.createCell(columnCount++).setCellValue(dto.getName()); // Name
            row.createCell(columnCount++).setCellValue(dto.getExchangesType().getName().toUpperCase()); // Type
            row.createCell(columnCount++).setCellValue(
                    dto.getEmissionSubstance().getEmissionCompartment().getName() // Compartment
            );
        }

        // Điều chỉnh độ rộng cột cho tất cả các cột
        for (int i = 0; i < 6; i++) {
            lciResults.autoSizeColumn(i);
        }
    }

    private void createLciaResultsSheet(XSSFSheet lciaResults, XSSFWorkbook workbook, Project p, CellStyle boldStyle) {

        List<ProjectImpactValue> data = projectImpactValueRepository.findAllByProjectId(p.getId());

        Set<String> boldHeaders = new HashSet<>(Arrays.asList(
                "LCA Process Impact Summary - Cabonerf Excel Export",
                "Name",
                "Amount",
                "Unit",
                "Method",
                "Description"
        ));

        // Dữ liệu của tab Guide
        Object[][] guideData = {
                {"LCA Process Impact Summary - Cabonerf Excel Export"},
                {},
                {"Name", "Amount", "Unit", "Method", "Description"},

        };

        // Viết dữ liệu vào sheet Guide
        int rowCount = 1;
        for (Object[] rowData : guideData) {
            Row row = lciaResults.createRow(rowCount++);
            int columnCount = 1;
            for (Object field : rowData) {
                Cell cell = row.createCell(columnCount++);
                if (field instanceof String) {
                    cell.setCellValue((String) field);
                    // Áp dụng style in đậm nếu thuộc danh sách tiêu đề cần in đậm
                    if (boldHeaders.contains(field)) {
                        cell.setCellStyle(boldStyle);
                    }
                } else if (field instanceof Integer) {
                    cell.setCellValue((Integer) field);
                } else if (field instanceof Double) {
                    cell.setCellValue((Double) field);
                }
            }
        }

        for (ProjectImpactValue x : data) {
            Row row = lciaResults.createRow(rowCount++);
            int columnCount = 1;

            // Điền dữ liệu từ DTO vào các cột
            row.createCell(columnCount++).setCellValue(x.getImpactMethodCategory().getImpactCategory().getName());
            row.createCell(columnCount++).setCellValue(x.getValue().setScale(2, RoundingMode.HALF_UP).toString());
            row.createCell(columnCount++).setCellValue(x.getImpactMethodCategory().getImpactCategory().getMidpointImpactCategory().getUnit().getName());
            row.createCell(columnCount++).setCellValue(x.getImpactMethodCategory().getLifeCycleImpactAssessmentMethod().getName() + "(" + x.getImpactMethodCategory().getLifeCycleImpactAssessmentMethod().getPerspective().getAbbr() + ")");
            row.createCell(columnCount++).setCellValue(x.getImpactMethodCategory().getImpactCategory().getMidpointImpactCategory().getName());
        }

        // Điều chỉnh độ rộng cột cho dễ đọc
        for (int i = 0; i < 6; i++) {
            lciaResults.autoSizeColumn(i);
        }
    }

    private CellStyle createStyle(XSSFWorkbook workbook) {
        CellStyle boldStyle = workbook.createCellStyle();
        XSSFFont boldFont = workbook.createFont();
        boldFont.setBold(true);
        boldStyle.setFont(boldFont);
        return boldStyle;
    }

    public List<ExchangesDto> aggregateExchangesWithFields(List<Exchanges> data) {
        // Map để lưu các nhóm cộng dồn
        Map<String, ExchangesDto> aggregatedMap = new HashMap<>();

        for (Exchanges exchange : data) {
            if (exchange.getEmissionSubstance() != null) {
                // Tạo key duy nhất dựa trên emissionSubstance ID và input
                String key = exchange.getEmissionSubstance().getId().toString() + "_" + exchange.isInput();

                // Nếu đã tồn tại trong Map, cộng dồn value
                if (aggregatedMap.containsKey(key)) {
                    ExchangesDto existingSummary = aggregatedMap.get(key);
                    existingSummary.setValue(existingSummary.getValue().add(exchange.getValue()));
                } else {
                    // Nếu chưa tồn tại, tạo mới một ExchangeSummary và thêm vào Map
                    ExchangesDto newExchange = new ExchangesDto();
                    newExchange.setName(exchange.getName());
                    newExchange.setValue(exchange.getValue());
                    newExchange.setExchangesType(new ExchangesTypeDto(exchange.getExchangesType().getId(), exchange.getExchangesType().getName()));
                    newExchange.setEmissionSubstance(emissionSubstanceConverter.modelToDto(exchange.getEmissionSubstance()));
                    newExchange.setUnit(unitConverter.fromUnitToUnitDto(exchange.getUnit()));
                    newExchange.setInput(exchange.isInput());

                    aggregatedMap.put(key, newExchange);
                }
            }
        }
        return new ArrayList<>(aggregatedMap.values());
    }
}
