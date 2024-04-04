package com.unison.scada.availability.domain.user;

import com.unison.scada.availability.global.config.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserCreate, SelectUser{
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    @Override
    public boolean createUser(UserDTO.Request request) {
        if(usersRepository.findById(request.getId()).isEmpty()){
            Users users = Users.builder()
                    .id(request.getId())
                    .pw(request.getPassword())
                    .role(UserRole.getUserRole(request.getRole()))
                    .name(request.getName())
                    .createdAt(LocalDateTime.now())
                    .createdBy("jeong won")
                    .build();
            usersRepository.save(users);

            return true;
        }
        return false;
    }

    @Override
    public List<UserDTO.Response> findByAllUser() {
        List<Users> usersList = usersRepository.findAll();

        return usersMapper.toUserDTO(usersList);
    }
}
