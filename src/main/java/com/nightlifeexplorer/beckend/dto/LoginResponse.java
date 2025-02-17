package com.nightlifeexplorer.beckend.dto;

import com.nightlifeexplorer.beckend.enums.APIStatus;
import lombok.AllArgsConstructor;
import lombok.Data;


public record LoginResponse<T> (APIStatus status, T data, String error) {

}

