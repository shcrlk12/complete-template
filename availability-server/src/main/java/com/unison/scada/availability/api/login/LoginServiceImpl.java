package com.unison.scada.availability.api.login;

import com.unison.scada.availability.api.user.Users;
import com.unison.scada.availability.api.user.UsersMapper;
import com.unison.scada.availability.api.user.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;

    @Override
    public LoginDTO.Response login(LoginDTO.Request request) throws Exception {

        Optional<Users> optionalUsers = usersRepository.findById(request.getUsername());

        Users users = optionalUsers.orElseThrow(() -> new Exception("not found user name [" + request.getUsername() + "]"));

        users.setLastLoginTime(LocalDateTime.now());
        usersRepository.save(users);

        return usersMapper.toLoginDTO(users);
    }
}
