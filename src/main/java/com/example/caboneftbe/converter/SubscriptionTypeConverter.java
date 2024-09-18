package com.example.caboneftbe.converter;

import com.example.caboneftbe.dto.RoleDto;
import com.example.caboneftbe.dto.SubscriptionTypeDto;
import com.example.caboneftbe.models.Role;
import com.example.caboneftbe.models.SubscriptionType;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface SubscriptionTypeConverter {
    SubscriptionTypeConverter INSTANCE = Mappers.getMapper(SubscriptionTypeConverter.class);

    SubscriptionTypeDto fromSubscriptionTypeToSubscriptionTypeDto(SubscriptionType subscriptionType);
}
