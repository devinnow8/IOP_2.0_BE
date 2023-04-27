package com.nis.controller;

import com.nis.entity.UserDetails;
import com.nis.exception.LockedException;
import com.nis.model.LoginRequest;
import com.nis.model.LoginResponse;
import com.nis.model.dto.UserDTO;
import com.nis.service.UserService;
import com.nis.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/nis/user/")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest request) throws Exception{
            return signIn(request);
    }

    @PostMapping("/sign-up")
    public ResponseEntity<?> registerUser(@RequestBody UserDTO userDTO) throws Exception {

        userService.createUser(userDTO);
        LoginRequest request = new LoginRequest();
        request.setUsername_email(userDTO.getEmail());
        request.setPassword(userDTO.getPassword());
        return signIn(request);

    }



    @PutMapping("/{userId}/reset-password")
    public ResponseEntity<?> resetUserPassword(@PathVariable Long userId, @RequestBody UserDTO userDTO) throws Exception {

        userService.updatePassword(userId, userDTO);
        return new ResponseEntity<>("Record updated", HttpStatus.OK);

    }

    public ResponseEntity<?> signIn(LoginRequest request) throws Exception{
        Authentication authentication=null;
        try {
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getUsername_email(), request.getPassword()));
        }catch (BadCredentialsException ex){

            try{
                userService.increaseFailureCount(request.getUsername_email());
            }catch (LockedException exception){

                throw exception;
            }

            throw  ex;
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = userService.getUserByUsernameOrEmail(request.getUsername_email());

        userService.resetFailureCount(userDetails);

        final String jwt = jwtTokenUtil.generateToken(authentication);

        LoginResponse response = new LoginResponse();
        response.setUser_id(userDetails.getUserID());
        response.setJwt(jwt);
        response.setEmail(userDetails.getEmail());
        response.setName(userDetails.getFirstname());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
