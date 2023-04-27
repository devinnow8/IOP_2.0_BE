package com.nis.service;

import com.nis.entity.UserDetails;
import com.nis.exception.LockedException;
import com.nis.exception.ResourceNotFoundException;
import com.nis.model.Role;
import com.nis.model.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {

    void createUser(UserDTO userDTO) throws Exception;

    void updatePassword(Long userId,UserDTO userDTO) throws ResourceNotFoundException;

    void resendPassword(Long userId) throws ResourceNotFoundException;
    UserDetails getUserByUsernameOrEmail(String username) throws Exception;

    void increaseFailureCount(String email) throws LockedException;
    void resetFailureCount(UserDetails userDetails) throws LockedException;
    void updateUserDetail(Long userId,UserDTO userDTO) throws ResourceNotFoundException;

    UserDetails getUserById(Long id) throws ResourceNotFoundException;


    void removeUser(Long userId) throws ResourceNotFoundException;



}
