package com.unison.scada.availability.domain.user;

import com.unison.scada.availability.domain.login.LoginDTO;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UsersMapper {

    public LoginDTO.Response toLoginDTO(Users users){
        return Optional.ofNullable(users)
                .map(u -> new LoginDTO.Response(u.getId(), u.getName()))
                .orElse(new LoginDTO.Response("", ""));
    }
}
