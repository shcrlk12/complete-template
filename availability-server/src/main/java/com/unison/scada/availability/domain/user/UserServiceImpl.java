package com.unison.scada.availability.domain.user;

import com.unison.scada.availability.global.config.security.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;
    @Override
    public boolean createUser(UserDTO.Request request) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails)principal;

        if(usersRepository.findById(request.getId()).isEmpty()){
            Users users = Users.builder()
                    .id(request.getId())
                    .pw(new BCryptPasswordEncoder().encode(request.getPassword()))
                    .role(UserRole.getUserRole(request.getRole()))
                    .name(request.getName())
                    .createdAt(LocalDateTime.now())
                    .createdBy(userDetails.getUsername())
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

    @Override
    public UserDTO.Response findByUser(String id) {

        return usersMapper.toUserDTO(usersRepository.findById(id).orElse(new Users()));
    }

    @Override
    public boolean deleteUser(String id) {

        usersRepository.deleteById(id);

        return true;
    }

}
