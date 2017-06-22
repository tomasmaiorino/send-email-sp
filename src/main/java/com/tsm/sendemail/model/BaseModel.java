package com.tsm.sendemail.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

public class BaseModel {

    @CreatedDate
    @Getter
    private LocalDateTime created;

    @LastModifiedDate
    @Getter
    private LocalDateTime lastUpdated;

    @JsonIgnore
    public boolean isNew() {
        return created == null;
    }
}
