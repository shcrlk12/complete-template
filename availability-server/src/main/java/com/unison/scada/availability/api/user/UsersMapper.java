package com.unison.scada.availability.api.user;

import com.unison.scada.availability.api.login.LoginDTO;
import com.unison.scada.availability.global.DateTimeUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.time.temporal.ChronoField.MILLI_OF_DAY;

@Component
public class UsersMapper {

    public LoginDTO.Response toLoginDTO(Users users){
        return Optional.ofNullable(users)
                .map(u -> new LoginDTO.Response(u.getId(), u.getName(), u.getRole().name()))
                .orElse(new LoginDTO.Response("", "", ""));
    }

    public UserDTO.Response toUserDTO(Users users){
        return Optional.ofNullable(users)
                .map(u -> new UserDTO.Response(u.getId(), u.getName(), u.getRole().name() , u.getLastLoginTime() == null ? "-" : DateTimeUtil.formatToYearMonthDayHourMinuteSecond(u.getLastLoginTime())))
                .orElse(new UserDTO.Response("", "", "", "-"));
    }
    public List<UserDTO.Response> toUserDTO(List<Users> users){
        List<UserDTO.Response> result = new ArrayList<>();

        for(Users user : users){
            result.add(toUserDTO(user));
        }
        return result;
    }
}
