package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.*;
import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.dto.ProcessDetailDto;
import com.example.cabonerfbe.dto.ProcessDto;
import com.example.cabonerfbe.dto.ProcessImpactValueDto;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Exchanges;
import com.example.cabonerfbe.models.ImpactMethodCategory;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.ProcessImpactValue;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.request.GetAllProcessRequest;
import com.example.cabonerfbe.request.UpdateProcessRequest;
import com.example.cabonerfbe.response.CreateProcessResponse;
import com.example.cabonerfbe.services.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProcessServiceImpl implements ProcessService {

    @Autowired
    ProcessRepository processRepository;
    @Autowired
    ProcessImpactValueRepository processImpactValueRepository;
    @Autowired
    LifeCycleStageRepository lifeCycleStageRepository;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    ImpactMethodCategoryRepository impactMethodCategoryRepository;
    @Autowired
    ProcessConverter processConverter;
    @Autowired
    ProcessImpactValueConverter processImpactValueConverter;
    @Autowired
    private ExchangesRepository exchangesRepository;
    @Autowired
    private ExchangesConverter exchangesConverter;
    @Autowired
    private LifeCycleImpactAssessmentMethodRepository methodRepository;
    @Autowired
    private LifeCycleImpactAssessmentMethodConverter methodConverter;
    @Autowired
    private ImpactCategoryConverter categoryConverter;
    @Autowired
    private UnitConverter unitConverter;
    @Override
    public ProcessDetailDto createProcess(CreateProcessRequest request) {
        Process process = new Process();
        process.setName(request.getName());
        process.setDescription(request.getDescription());
        if(lifeCycleStageRepository.findById(request.getLifeCycleStageId()).isEmpty()){
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Life cycle stage not exist");
        }
        process.setLifeCycleStage(lifeCycleStageRepository.findById(request.getLifeCycleStageId()).get());

        if(projectRepository.findById(request.getProjectId()).isEmpty()){
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, Map.of("projectId","Not exist"));
        }
        process.setProject(projectRepository.findById(request.getProjectId()).get());
        process.setOverAllProductFlowRequired(0);

        process = processRepository.save(process);

        List<ImpactMethodCategory> list = impactMethodCategoryRepository.findAll();
        List<ProcessImpactValue> processImpactValues = new ArrayList<>();

        return processConverter.INSTANCE.fromProcessDetailToProcessDto(process);
    }

    @Override
    public ProcessDto getProcessById(UUID id) {
        Optional<Process> process = processRepository.findById(id);
        if(process.isEmpty()){
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Process not exist");
        }

        ProcessDto dto = processConverter.fromProcessToProcessDto(process.get());

        dto.setImpacts(converterProcess(processImpactValueRepository.findByProcessId(process.get().getId())));
        dto.setExchanges(exchangesConverter.fromExchangesToExchangesDto(exchangesRepository.findAllByProcess(process.get().getId())));

        return dto;
    }

    @Override
    public List<CreateProcessResponse> getAllProcesses(GetAllProcessRequest request) {
        return null;
    }

    @Override
    public CreateProcessResponse updateProcess(UUID id, UpdateProcessRequest request) {
        return null;
    }


    private List<ProcessImpactValueDto> converterProcess(List<ProcessImpactValue> list){
        List<ProcessImpactValueDto> result = new ArrayList<>();
        for(ProcessImpactValue x: list){
            ProcessImpactValueDto p = new ProcessImpactValueDto();
            p.setId(x.getId());
            p.setSystemLevel(x.getSystemLevel());
            p.setUnitLevel(x.getUnitLevel());
            p.setOverallImpactContribution(x.getOverallImpactContribution());
            p.setMethod(methodConverter.fromMethodToMethodDto(x.getImpactMethodCategory().getLifeCycleImpactAssessmentMethod()));
            p.setImpactCategory(categoryConverter.fromProjectToImpactCategoryDto(x.getImpactMethodCategory().getImpactCategory()));
            p.setUnit(unitConverter.fromProjectToUnitResponse(x.getImpactMethodCategory().getImpactCategory().getMidpointImpactCategory().getUnit()));
            result.add(p);
        }
        return result;
    }
}
