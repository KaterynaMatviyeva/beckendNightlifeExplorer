package com.nightlifeexplorer.beckend.dto;

import com.nightlifeexplorer.beckend.enums.APIStatus;


public record LoginResponseGeneric<T> (APIStatus status, T data, String error) {

}

