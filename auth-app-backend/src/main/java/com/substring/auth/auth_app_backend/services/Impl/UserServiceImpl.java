package com.substring.auth.auth_app_backend.services.Impl;

import com.substring.auth.auth_app_backend.dtos.UserDto;
import com.substring.auth.auth_app_backend.entities.Provider;
import com.substring.auth.auth_app_backend.entities.User;
import com.substring.auth.auth_app_backend.exceptions.ResourceNotFoundException;
import com.substring.auth.auth_app_backend.repositories.UserRepository;
import com.substring.auth.auth_app_backend.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.substring.auth.auth_app_backend.helper.UserHelper.parseUUID;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UserDto createUser(UserDto userDto) {

        if (userDto.getEmail() == null || userDto.getEmail().isBlank()) {
            throw new IllegalArgumentException("Email is required");
        }

        if (userRepository.existsByEmail(userDto.getEmail())) {
            throw new IllegalArgumentException("User with given email already exist");
        }

        User user = modelMapper.map(userDto, User.class);
        user.setProvider(userDto.getProvider() != null ? userDto.getProvider() : Provider.LOCAL);

        //  User savedUser = userRepository.save(user);
        User savedUser = userRepository.saveAndFlush(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with given email id"));


        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public UserDto updateUser(UserDto userDto, String userId) {
        UUID uid  = parseUUID(userId);
        User existingUser = userRepository
                .findById(uid)
                .orElseThrow(()->new ResourceNotFoundException("User with provided uuid does not exist"));
        if(existingUser.getName()!= null) existingUser.setName(userDto.getName());
        if(existingUser.getImage()!= null) existingUser.setImage(userDto.getImage());
        if(existingUser.getProvider()!= null) existingUser.setProvider(userDto.getProvider());
        // TODO: Change password update logic
        if(existingUser.getPassword()!= null) existingUser.setPassword(userDto.getPassword());
        existingUser.setEnabled(userDto.isEnabled());
        User updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser,UserDto.class);
    }

    @Override
    public void deleteUser(String userId) {
        UUID uid = parseUUID(userId);
        User user = userRepository.findById(uid).orElseThrow(()->new ResourceNotFoundException("User with provided uuid does not exist"));
        userRepository.delete(user);
    }

    @Override
    public UserDto getUserById(String userId) {
        UUID uid = parseUUID(userId);
        User user = userRepository.findById(uid).orElseThrow(()->new ResourceNotFoundException("User with provided uuid does not exist"));
        return modelMapper.map(user,UserDto.class);
    }

    @Override
    public Iterable<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }
}
