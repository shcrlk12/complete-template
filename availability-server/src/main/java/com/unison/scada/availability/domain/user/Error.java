package com.unison.scada.availability.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Error{
    private final int code;
    private final String message;
}
