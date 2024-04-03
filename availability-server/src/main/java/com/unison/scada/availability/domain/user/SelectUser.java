package com.unison.scada.availability.domain.user;

import java.util.List;

public interface SelectUser {
    List<UserDTO.Response> findByAllUser();
}
