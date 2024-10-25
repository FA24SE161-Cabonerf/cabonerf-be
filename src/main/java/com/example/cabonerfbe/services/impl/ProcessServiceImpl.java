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
import com.example.cabonerfbe.request.GetAllProcessRequest;
import com.example.cabonerfbe.request.UpdateProcessRequest;
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
    @Autowired
    private LifeCycleImpactAssessmentMethodRepository lifeCycleImpactAssessmentMethodRepository;
    @Override
    public CreateProcessResponse createProcess(CreateProcessRequest request) {
        Process process = new Process();
        if(processRepository.findByName(request.getName(),request.getProjectId()).isPresent()){
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, "Project name already exist in project");
        }
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
                .process(processConverter.INSTANCE.fromProcessDetailToProcessDto(process))
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
                .process(processConverter.INSTANCE.fromProcessDetailToProcessDto(process.get()))
                .build();
    }

    @Override
    public List<CreateProcessResponse> getAllProcesses(GetAllProcessRequest request) {
        if(projectRepository.findById(request.getProjectId()).isEmpty()){
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Project not exist");
        }

        if(lifeCycleImpactAssessmentMethodRepository.findById(request.getMethodId()).isEmpty()){
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR, "Life Cycle Impact Assessment Method not exist");
        }

        List<Process> processes = processRepository.findAll(request.getProjectId());

        return buildProcessResponse(processes,request.getMethodId());
    }

    @Override
    public CreateProcessResponse updateProcess(long id, UpdateProcessRequest request) {
        if(processRepository.findById(id).isEmpty()){
            throw CustomExceptions.notFound(Constants.RESPONSE_STATUS_ERROR,"Process not exist");
        }
        Process p = processRepository.findById(id).get();
        if(!request.getName().isEmpty()){
            p.setName(request.getName());
        }
        if(!request.getDescription().isEmpty()){
            p.setName(request.getName());
        }
        if(request.getLifeCycleStageId() != 0){
            p.setLifeCycleStage(lifeCycleStageRepository.findById(request.getLifeCycleStageId()).get());
        }
        p = processRepository.save(p);
        List<ProcessImpactValue> processImpactValues = processImpactValueRepository.findByProcessId(p.getId());
        List<Exchanges> exchanges = exchangesRepository.findAllByProcess(p.getId());

        return CreateProcessResponse.builder()
                .process(processConverter.INSTANCE.fromProcessDetailToProcessDto(p))
                .build();
    }

    private List<CreateProcessResponse> buildProcessResponse(List<Process> processes, long methodId) {
        List<CreateProcessResponse> responses = new ArrayList<>();
        for (Process process : processes) {
            List<ProcessImpactValue> processImpactValues = processImpactValueRepository.findByProcessAndMethod(process.getId(),methodId);
            List<Exchanges> exchanges = exchangesRepository.findAllByProcess(process.getId());

            CreateProcessResponse processResponse = CreateProcessResponse.builder()
                    .process(processConverter.INSTANCE.fromProcessDetailToProcessDto(process))
                    .build();

            responses.add(processResponse);
        }
        return responses;
    }




}
