package com.example.caboneftbe.services.impl;

import com.example.caboneftbe.converter.ProcessConverter;
import com.example.caboneftbe.converter.ProcessImpactValueConverter;
import com.example.caboneftbe.dto.ProcessDto;
import com.example.caboneftbe.enums.Constants;
import com.example.caboneftbe.enums.MessageConstants;
import com.example.caboneftbe.exception.CustomExceptions;
import com.example.caboneftbe.models.ImpactMethodCategory;
import com.example.caboneftbe.models.Process;
import com.example.caboneftbe.models.ProcessImpactValue;
import com.example.caboneftbe.repositories.*;
import com.example.caboneftbe.request.CreateProcessRequest;
import com.example.caboneftbe.response.CreateProcessResponse;
import com.example.caboneftbe.services.ProcessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @Override
    public CreateProcessResponse createProcess(CreateProcessRequest request) {
        Process process = new Process();
        process.setName(request.getName());
        process.setDescription(request.getDescription());
        if(lifeCycleStageRepository.findById(request.getLifeCycleStageId()).isEmpty()){
            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, Map.of("lifeCycleStageId","Not exist"));
        }
        process.setLifeCycleStage(lifeCycleStageRepository.findById(request.getLifeCycleStageId()).get());

//        if(projectRepository.findById(request.getProjectId()).isEmpty()){
//            throw CustomExceptions.badRequest(Constants.RESPONSE_STATUS_ERROR, Map.of("projectId","Not exist"));
//        }
        process.setOverallProductFlowRequired(0);

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

        ProcessDto processDto = new ProcessDto();
        processDto = processConverter.INSTANCE.fromProcessToProcessDto(process);

        processDto.setImpactValues(processImpactValueConverter.INSTANCE.fromProcessImpactValueToProcessImpactValueDto(processImpactValues));

        return CreateProcessResponse.builder()
                .process(processDto)
                .build();
    }
}
