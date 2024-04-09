package com.unison.scada.availability.domain.user;

import lombok.RequiredArgsConstructor;

public class JSONResponse<T, E>{
    private T data;
    private E error;
}

