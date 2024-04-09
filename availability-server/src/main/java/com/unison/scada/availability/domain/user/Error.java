package com.unison.scada.availability.domain.user;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Error{
    private final int code;
    private final String message;
}

