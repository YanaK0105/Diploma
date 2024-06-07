package org.example.service;

import lombok.AllArgsConstructor;
import org.example.dto.UserDto;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.repository.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    //указываем,что репозиторием будем пользоваться не напрямую.определим как константу
    private final UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    //возвращаем по указанному id определенного пользователя
    public User findById(long id) {
        Optional<User> user = userRepository.findById(id);
        return user.get();
    }

    public UserDto saveUser(User user) {
        try {
            user.setRole(Role.USER);
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            var newUser = userRepository.save(user);
            return UserDto.builder()
                    .error(false)
                    .name(newUser.getName())
                    .role(newUser.getRole())
                    .email(newUser.getEmail())
                    .build();
        }
        catch (DataIntegrityViolationException e){
            if(Objects.requireNonNull(e.getRootCause()).getMessage()
                    .contains("email")) {
                return UserDto.builder()
                        .error(true)
                        .email(user.getEmail())
                        .errorMessage("Такая почта уже зарегистрирована")
                        .build();
            }
            else if(Objects.requireNonNull(e.getRootCause()).getMessage()
                        .contains("name")) {
                    return UserDto.builder()
                            .error(true)
                            .name(user.getName())
                            .errorMessage("Пользователь с таким именем уже существует")
                            .build();
            }
            else{
                return UserDto.builder()
                        .error(true)
                        .errorMessage(e.getRootCause().getMessage())
                        .build();
            }
        }
    }

    public User findByEmail(String email){
        if(userRepository.findByEmail(email).isPresent())
            return userRepository.findByEmail(email).get();
        throw new UsernameNotFoundException("Could not find such user.");
    }
    public UserDto updateUser(Long id, User user){
        user.setId(id);
        var oldUser = userRepository.getById(id);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(oldUser.getRole());
        try {
            var result = userRepository.save(user);
           return UserDto.builder()
                   .error(false)
                   .email(result.getEmail())
                   .role(result.getRole())
                   .name(result.getName())
                   .errorMessage(null)
                   .build();
        }
        catch (DataIntegrityViolationException e){
            if(Objects.requireNonNull(e.getRootCause()).getMessage()
                    .contains("email")) {
                return UserDto.builder()
                        .error(true)
                        .email(user.getEmail())
                        .errorMessage("Такая почта уже зарегистрирована")
                        .build();
            }
            else if(Objects.requireNonNull(e.getRootCause()).getMessage()
                    .contains("name")) {
                return UserDto.builder()
                        .error(true)
                        .name(user.getName())
                        .errorMessage("Пользователь с таким именем уже существует")
                        .build();
            }
            else{
                return UserDto.builder()
                        .error(true)
                        .errorMessage(e.getRootCause().getMessage())
                        .build();
            }
        }
    }

    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    //отображение всех пользователей
    public List<User> findAll() {
        return userRepository.findAll();
    }

}

