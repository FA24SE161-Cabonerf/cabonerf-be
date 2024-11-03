package com.example.cabonerfbe.models;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.UUID;

@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
@EntityListeners(AuditingEntityListener.class)
public class Base {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @Column
    @CreatedDate
    @JsonIgnore
    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd")
    private LocalDate createAt;
    @Column
    @LastModifiedDate
    @JsonIgnore
    @Field(type = FieldType.Date, format = {}, pattern = "uuuu-MM-dd")
    private LocalDate modifiedAt;
    @Column
    @JsonIgnore
    private boolean status = true;
}
