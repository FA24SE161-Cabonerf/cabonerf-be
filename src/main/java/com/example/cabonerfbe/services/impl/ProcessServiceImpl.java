package com.example.cabonerfbe.services.impl;

import com.example.cabonerfbe.converter.ExchangesConverter;
import com.example.cabonerfbe.converter.ProcessConverter;
import com.example.cabonerfbe.converter.ProcessImpactValueConverter;
import com.example.cabonerfbe.dto.PageList;
import com.example.cabonerfbe.enums.Constants;
import com.example.cabonerfbe.exception.CustomExceptions;
import com.example.cabonerfbe.models.Exchanges;
import com.example.cabonerfbe.models.ImpactMethodCategory;
import com.example.cabonerfbe.models.Process;
import com.example.cabonerfbe.models.ProcessImpactValue;
import com.example.cabonerfbe.repositories.*;
import com.example.cabonerfbe.request.CreateProcessRequest;
import com.example.cabonerfbe.response.CreateProcessResponse;
import com.example.cabonerfbe.services.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    @Override
    public CreateProcessResponse createProcess(CreateProcessRequest request) {
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

        for(ImpactMethodCategory impactMethodCategory : list){
            ProcessImpactValue processImpactValue = new ProcessImpactValue();
            processImpactValue.setProcess(process);
            processImpactValue.setImpactMethodCategory(impactMethodCategory);
            processImpactValue.setSystemLevel(0);
            processImpactValue.setSystemLevel(0);
            processImpactValue.setOverallImpactContribution(0);
            processImpactValue.setPreviousProcessValue(0);
            processImpactValues.add(processImpactValue);
        }

        processImpactValueRepository.saveAll(processImpactValues);

        return CreateProcessResponse.builder()
                .process(processConverter.INSTANCE.fromProcessToProcessDto(process))
                .impactValues(processImpactValueConverter.INSTANCE.fromProcessImpactValueToProcessImpactValueDto(processImpactValues))
                .build();
    }

    @Override
    public CreateProcessResponse getProcessById(long id) {
        Optional<Process> process = processRepository.findById(id);
        if(process.isEmpty()){
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Process not exist");
        }

        List<ProcessImpactValue> processImpactValues = processImpactValueRepository.findByProcessId(process.get().getId());
        List<Exchanges> exchanges = exchangesRepository.findAllByProcess(process.get().getId());

        return CreateProcessResponse.builder()
                .process(processConverter.INSTANCE.fromProcessToProcessDto(process.get()))
                .impactValues(processImpactValueConverter.INSTANCE.fromProcessImpactValueToProcessImpactValueDto(processImpactValues))
                .exchanges(exchangesConverter.INSTANCE.fromExchangesToExchangesDto(exchanges))
                .build();
    }

    @Override
    public PageList<CreateProcessResponse> getAllProcesses(int currentPage, int pageSize, long projectId) {
        if (projectId == 0) {
            if (currentPage < 1 || pageSize < 1) {
                List<CreateProcessResponse> list = getAllProcessDefault();
                PageList<CreateProcessResponse> pageList = new PageList<>();
                pageList.setCurrentPage(1);
                pageList.setTotalPage(1);
                pageList.setListResult(list);
                return pageList;
            }

            List<CreateProcessResponse> pagedList = getAllProcess(currentPage, pageSize);
            int totalRecords = pagedList.size();

            int totalPage = (int) Math.ceil((double) processRepository.findAll().size() / pageSize);

            PageList<CreateProcessResponse> pageList = new PageList<>();
            pageList.setCurrentPage(currentPage);
            pageList.setTotalPage(totalPage);
            pageList.setListResult(pagedList);

            return pageList;
        }
        if (currentPage < 1 || pageSize < 1) {
            List<CreateProcessResponse> list = getAllProcessDefaultWithProject(projectId);
            PageList<CreateProcessResponse> pageList = new PageList<>();
            pageList.setCurrentPage(1);
            pageList.setTotalPage(1);
            pageList.setListResult(list);
            return pageList;
        }

        List<CreateProcessResponse> pagedList = getAllProcessWithProject(currentPage, pageSize,projectId);
        int totalRecords = pagedList.size();

        int totalPage = (int) Math.ceil((double) processRepository.findAllByProjectId(projectId).size() / pageSize);

        PageList<CreateProcessResponse> pageList = new PageList<>();
        pageList.setCurrentPage(currentPage);
        pageList.setTotalPage(totalPage);
        pageList.setListResult(pagedList);

        return pageList;
    }

    private List<CreateProcessResponse> getAllProcessDefault() {
        List<Process> processes = processRepository.findAll();
        return buildProcessResponse(processes);
    }

    private List<CreateProcessResponse> getAllProcess(int currentPage, int pageSize) {
        Page<Process> processes = processRepository.findAll(PageRequest.of(currentPage - 1, pageSize));
        return buildProcessResponse(processes.getContent());
    }

    private List<CreateProcessResponse> getAllProcessDefaultWithProject(long projectId) {
        List<Process> processes = processRepository.findAllByProjectId(projectId);
        return buildProcessResponse(processes);
    }

    private List<CreateProcessResponse> getAllProcessWithProject(int currentPage, int pageSize, long projectId) {
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        Page<Process> processes = processRepository.findAllByProjectIdWithPage(projectId,pageable);
        return buildProcessResponse(processes.getContent());
    }

    private List<CreateProcessResponse> buildProcessResponse(List<Process> processes) {
        List<CreateProcessResponse> responses = new ArrayList<>();
        for (Process process : processes) {
            List<ProcessImpactValue> processImpactValues = processImpactValueRepository.findByProcessId(process.getId());
            List<Exchanges> exchanges = exchangesRepository.findAllByProcess(process.getId());

            CreateProcessResponse processResponse = CreateProcessResponse.builder()
                    .process(processConverter.INSTANCE.fromProcessToProcessDto(process))
                    .exchanges(exchangesConverter.INSTANCE.fromExchangesToExchangesDto(exchanges))
                    .impactValues(processImpactValueConverter.INSTANCE.fromProcessImpactValueToProcessImpactValueDto(processImpactValues))
                    .build();

            responses.add(processResponse);
        }
        return responses;
    }




}
