package com.nis.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.razorpay.RazorpayException;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;


import java.nio.file.AccessDeniedException;
import java.security.SignatureException;

@ControllerAdvice
public class ErrorAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ErrorAdvice.class);

    @Autowired
    private ParseExceptions parseExceptions;

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception){
        logger.error("ResourceNotFoundException",exception.getMessage());
//        ErrorResponse response= new ErrorResponse();
//        response.setDetails(exception.getMessage());
//        response.setTimestamp(LocalDate.now());
//        response.setMessage(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserExistException.class)
    public final ResponseEntity<Object> handleUserExistException(UserExistException exception){
        logger.error("UserExistException",exception.getMessage());
//        ErrorResponse response= new ErrorResponse();
//        response.setDetails(exception.getMessage());
//        response.setTimestamp(LocalDate.now());
//        response.setMessage(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(RecordExistException.class)
    public final ResponseEntity<Object> handleRecordExistException(RecordExistException exception){
        logger.error("RecordExistException",exception.getMessage());
//        ErrorResponse response= new ErrorResponse();
//        response.setDetails(exception.getMessage());
//        response.setTimestamp(LocalDate.now());
//        response.setMessage(exception.getMessage());
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler({ AuthenticationException.class, AccessDeniedException.class })
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        logger.error("AuthenticationException",ex);
//        ErrorResponse response= new ErrorResponse();
//        response.setDetails(ex.getMessage());
//        response.setTimestamp(LocalDate.now());
//        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        logger.error("MethodArgumentNotValidException",ex);
//        ErrorResponse response= new ErrorResponse();
//        response.setDetails(ex.getMessage());
//        response.setTimestamp(LocalDate.now());
//        response.setMessage(ex.getCause().getMessage());
        return new ResponseEntity<>(ex.getCause().getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        logger.error("MethodArgumentNotValidException",ex);
//        ErrorResponse response= new ErrorResponse();
//        response.setDetails(ex.getMessage());
//        response.setTimestamp(LocalDate.now());
//        response.setMessage(ex.getCause().getMessage());
        return new ResponseEntity<>(ex.getCause().getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public final ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException exception){
        logger.error("IllegalArgumentException",exception);
//        ErrorResponse response= new ErrorResponse();
//        response.setDetails(exception.getMessage());
//        response.setTimestamp(LocalDate.now());
//        response.setMessage(exception.getCause().getMessage());
        return new ResponseEntity<>(exception.getCause().getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public final ResponseEntity<Object> handleAllException(RuntimeException exception){
        logger.error("RuntimeException",exception);
//        ErrorResponse response= new ErrorResponse();
//        response.setDetails(exception.getMessage());
//        response.setTimestamp(LocalDate.now());
//        response.setMessage(exception.getCause().getMessage());
        return new ResponseEntity<>(exception.getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public final ResponseEntity<Object> handleSqlException(DataIntegrityViolationException exception){
        logger.error("DataIntegrityViolationException",exception);

        String response="";
        ConstraintViolationException ex=(ConstraintViolationException)exception.getCause();
        if(ex.getConstraintName().equalsIgnoreCase("uniquechecklistcategoryandservice")){
            response="Checklist already exists for the selected category and service";
        }else if(ex.getConstraintName().equalsIgnoreCase("UniqueCategoryAndService")){
            response="Service already exists for the selected category";
        }else if(ex.getConstraintName().equalsIgnoreCase("UniqueUserEmail")){
            response="User already exists with this email";
        }else if(ex.getConstraintName().equalsIgnoreCase("UniqueUserPhone")){
            response="User already exits with this phone number";
        }
        else{
            response=parseExceptions.parseDuplicateKeyException(exception.getRootCause().getMessage());
        }

         return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public final ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException exception){
        logger.error("DuplicateKeyException",exception);
//        ErrorResponse response= new ErrorResponse();
//        response.setDetails(exception.getMessage());
//        response.setTimestamp(LocalDate.now());
//        response.setMessage(parseExceptions.parseDuplicateKeyException(exception.getMessage()));
        String response=parseExceptions.parseDuplicateKeyException(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(JsonMappingException.class)
    public final ResponseEntity<Object> handleJsonMappingException(JsonMappingException exception){
        logger.error("JsonMappingException",exception);
//        ErrorResponse response= new ErrorResponse();
//        response.setDetails(exception.getMessage());
//        response.setTimestamp(LocalDate.now());
//        response.setMessage(parseExceptions.parseJsonMappingException(exception.getMessage()));
        String response=parseExceptions.parseJsonMappingException(exception.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<Object> handleMessageNotReadableException(HttpMessageNotReadableException ex) {
        logger.error("HttpMessageNotReadableException",ex);
//        ErrorResponse response= new ErrorResponse();
//        response.setDetails(ex.getMessage());
//        response.setTimestamp(LocalDate.now());
//        response.setMessage(ex.getRootCause().getMessage());
        return new ResponseEntity<>(ex.getRootCause().getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RazorpayException.class)
    public final ResponseEntity<Object> handleRazorpayException(RazorpayException ex) {
        logger.error("RazorpayException",ex);
//        ErrorResponse response= new ErrorResponse();
//        response.setDetails(ex.getMessage());
//        response.setTimestamp(LocalDate.now());
//        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SignatureException.class)
    public final ResponseEntity<Object> handleSignatureException(SignatureException ex) {
        logger.error("SignatureException",ex);
//        ErrorResponse response= new ErrorResponse();
//        response.setDetails(ex.getMessage());
//        response.setTimestamp(LocalDate.now());
//        response.setMessage(ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(LockedException.class)
    public final ResponseEntity<Object> handleLockedException(LockedException ex) {
        logger.error("LockedException",ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.LOCKED);
    }

}
