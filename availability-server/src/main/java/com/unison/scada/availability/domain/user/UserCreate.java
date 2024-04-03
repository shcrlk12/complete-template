package com.unison.scada.availability.domain.user;

public interface UserCreate {
    boolean createUser(UserDTO.Request request);
}
