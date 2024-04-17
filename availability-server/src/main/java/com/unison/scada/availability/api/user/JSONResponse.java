package com.unison.scada.availability.api.user;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class JSONResponse<T, E>{
    private T data;
    private E error;
}

