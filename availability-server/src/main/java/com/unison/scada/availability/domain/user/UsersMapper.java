package com.unison.scada.availability.domain.user;

import com.unison.scada.availability.domain.login.LoginDTO;
import com.unison.scada.availability.global.config.security.UserRole;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class UsersMapper {

    public LoginDTO.Response toLoginDTO(Users users){
        return Optional.ofNullable(users)
                .map(u -> new LoginDTO.Response(u.getId(), u.getName(), u.getRole().name()))
                .orElse(new LoginDTO.Response("", "", ""));
    }

    public UserDTO.Response toUserDTO(Users users){
        return Optional.ofNullable(users)
                .map(u -> new UserDTO.Response(u.getId(), u.getName(), u.getRole().name() , LocalTime.now()))
                .orElse(new UserDTO.Response("", "", "", LocalTime.now()));
    }
    public List<UserDTO.Response> toUserDTO(List<Users> users){
        List<UserDTO.Response> result = new ArrayList<>();

        for(Users user : users){
            result.add(toUserDTO(user));
        }
        return result;
    }
}
