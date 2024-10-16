package com.example.cabonerfbe.converter;

import com.example.cabonerfbe.dto.SubscriptionTypeDto;
import com.example.cabonerfbe.models.SubscriptionType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SubscriptionTypeConverter {
    SubscriptionTypeConverter INSTANCE = Mappers.getMapper(SubscriptionTypeConverter.class);

    SubscriptionTypeDto fromSubscriptionTypeToSubscriptionTypeDto(SubscriptionType subscriptionType);
}
