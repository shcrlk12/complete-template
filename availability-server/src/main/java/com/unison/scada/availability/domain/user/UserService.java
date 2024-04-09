package com.unison.scada.availability.domain.user;

import java.util.List;

public interface UserService {
    boolean createUser(UserDTO.Request request);

    List<UserDTO.Response> findByAllUser();

    UserDTO.Response findByUser(String id);

    boolean deleteUser(String id);
}
