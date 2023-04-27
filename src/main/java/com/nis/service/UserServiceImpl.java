package com.nis.service;

import com.nis.entity.*;
import com.nis.exception.LockedException;
import com.nis.exception.ResourceNotFoundException;
import com.nis.exception.UserExistException;
import com.nis.model.dto.UserDTO;
import com.nis.repository.ProcessingCenterRepository;
import com.nis.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProcessingCenterRepository centerDetailRepository;

    @Autowired
    private PasswordServiceImpl passwordService;

    @Autowired
    private MailService mailService;

    @Autowired
    private TemplateService templateService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${user.fe.url}")
    private String userURL;

    @Value("${admin.fe.url}")
    private String adminURL;


    @Override
    public void createUser(UserDTO userDTO) throws Exception {

        // add check for email exists in DB
        Optional<UserDetails> usr=userRepository.findByEmail(userDTO.getEmail());
        if (usr.isPresent()) {
            throw new UserExistException("The user with this email already exists ");
        }

        // create user object
        UserDetails user = new UserDetails();
        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordService.encodePassword(userDTO.getPassword()));
        user.setPhoneNumber(userDTO.getPhone_number());
        userRepository.save(user);

    }

    @Override
    public void updatePassword(Long userId, UserDTO userDTO) throws ResourceNotFoundException {

        UserDetails userDetails = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found :" + userId));

        userDetails.setPassword(passwordService.encodePassword(userDTO.getPassword()));
        userRepository.save(userDetails);

    }

    @Override
    public void resendPassword(Long userId) throws ResourceNotFoundException {
        UserDetails userDetails = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found :" + userId));

        String password = passwordService.generateRandomPassword();

        userDetails.setPassword(passwordService.encodePassword(password));
        userDetails.setFailedAttempt(0);
        userDetails.setLocked(false);
        userDetails.setLockTime(null);
        userRepository.save(userDetails);

    }

    @Override
    public UserDetails getUserByUsernameOrEmail(String email) throws ResourceNotFoundException {

        UserDetails userDetails = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found!"));

        return userDetails;
    }

    @Override
    public void increaseFailureCount(String email) throws LockedException {
        UserDetails userDetails = null;
        try {
            userDetails = getUserByUsernameOrEmail(email);
        } catch (ResourceNotFoundException ex) {
            return;
        }

        if (userDetails != null) {
            if (!userDetails.isLocked()) {
                if (userDetails.getFailedAttempt() < 2 ) {
                    userDetails.setFailedAttempt(userDetails.getFailedAttempt()+1);
                    userRepository.save(userDetails);
                } else {
//                    userDetails.setIsLocked(true);
//                    userDetails.setLockTime(LocalDateTime.now());
//                    userRepository.save(userDetails);
//                   throw new LockedException("Your account has been locked due to 3 failed attempts.");
                }
            }else{
//                throw new LockedException("Your account has been locked due to 3 failed attempts.");
            }

        }

    }

    @Override
    public void resetFailureCount(UserDetails userDetails) throws LockedException{

//        if (userDetails.getIsLocked()) {
//            throw new LockedException("Your account has been locked due to 3 failed attempts.");
//        }

        userDetails.setFailedAttempt(0);
        userDetails.setLastLogin(LocalDateTime.now());

        userRepository.save(userDetails);

    }

    @Override
    public void updateUserDetail(Long userId, UserDTO userDTO) throws ResourceNotFoundException {

        UserDetails user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found :" + userId));

        user.setFirstname(userDTO.getFirstname());
        user.setLastname(userDTO.getLastname());
        user.setEmail(userDTO.getEmail());
        user.setPhoneNumber(userDTO.getPhone_number());
        userRepository.save(user);
    }

    @Override
    public UserDetails getUserById(Long id) throws ResourceNotFoundException {
        UserDetails userDetails= userRepository.findById(id).orElseThrow(
                ()->new ResourceNotFoundException("User not found with id:"+id)
        );

        return userDetails;
    }


    private List<UserDTO> userToDTO(List<UserDetails> users){
        List<UserDTO> userDTOList = new ArrayList<>();
        for (UserDetails user : users) {
            UserDTO userDTO = new UserDTO();
            userDTO.setUser_id(user.getUserID());
            userDTO.setFirstname(user.getFirstname());
            userDTO.setLastname(user.getLastname());
            userDTO.setEmail(user.getEmail());
            userDTO.setLast_login(user.getLastLogin());
            userDTO.setPhone_number(user.getPhoneNumber());
            userDTOList.add(userDTO);
        }

        return userDTOList;
    }

    @Override
    public void removeUser(Long userId) throws ResourceNotFoundException {
        UserDetails user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("No user founs with id: " + userId));

        userRepository.delete(user);
    }


    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserDetails> userDetails = userRepository.findByEmail(email);
        if (userDetails.isPresent()) {
            UserDetails user = userDetails.get();
            return new User(email, user.getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User Not Found!");
        }
    }

    public void addMailEntry(UserDetails user, String password) {
        Map<String, String> values = new HashMap<>();
        values.put("Name", user.getFirstname());
        values.put("Password", password);
        values.put("Admin_Url", adminURL);

        String message = templateService.getTemplate(templateService.passwordTemplate, values);

        MailDetail mailDetail = new MailDetail();
        mailDetail.setMailTo(user.getEmail());
        mailDetail.setMailSubject("Password Reset");
        mailDetail.setMailBody(message);

        mailService.createMailEntry(mailDetail);
    }
}
