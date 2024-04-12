package com.unison.scada.availability.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@Builder
public class JSONResponse<T, E>{
    private T data;
    private E error;
}

