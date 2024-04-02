package com.unison.scada.availability.domain.login;

import com.unison.scada.availability.domain.user.Users;
import com.unison.scada.availability.domain.user.UsersMapper;
import com.unison.scada.availability.domain.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    @Override
    public LoginDTO.Response login(LoginDTO.Request request) {

        Users users = usersRepository.findById(request.getUsername()).orElse(new Users());

        return usersMapper.toLoginDTO(users);
    }
}
