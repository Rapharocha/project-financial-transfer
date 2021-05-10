package com.cvc.projectfinancialtransfer.exception.handler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.cvc.projectfinancialtransfer.exception.TransferException;





@ControllerAdvice
public class ExceptionHandle extends ResponseEntityExceptionHandler{

	@Autowired
	private MessageSource messageSource;
	
	@ExceptionHandler({TransferException.class})
	public ResponseEntity<Object> handleTransferException(TransferException ex) {
		String messageUser = ex.getDescription();
		String messageDev = ex.getDescription();
		
		List<Error> errors = Arrays.asList(new Error(messageUser,  messageDev));
		return ResponseEntity.badRequest().body(errors);
	}
	
	@ExceptionHandler({EmptyResultDataAccessException.class})
	public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
		String messageUser = "resource not found";
		String messageDev = ex.toString();
		
		List<Error> errors = Arrays.asList(new Error(messageUser, messageDev));
		return handleExceptionInternal(ex, errors , new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({MethodArgumentTypeMismatchException.class})
	public ResponseEntity<Object> handleNoSuchElementException(MethodArgumentTypeMismatchException ex, WebRequest request) {
		String messageUser = "resource not found";
		String messageDev = ex.toString();
		
		List<Error> errors = Arrays.asList(new Error(messageUser, messageDev));
		return handleExceptionInternal(ex, errors , new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	
	@ExceptionHandler({NoSuchElementException.class})
	public ResponseEntity<Object> handleNoSuchElementException(NoSuchElementException ex, WebRequest request) {
		String messageUser = "resource not found";
		String messageDev = ex.toString();
		
		List<Error> errors = Arrays.asList(new Error(messageUser, messageDev));
		return handleExceptionInternal(ex, errors , new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String messageUser = "Some argument may be invalid.";
		String messageDev = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
		
		List<Error> errors = Arrays.asList(new Error(messageUser, messageDev));
		
		return handleExceptionInternal(ex, errors , headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<Error> errors = createdListError(ex.getBindingResult());
		
		return handleExceptionInternal(ex, errors , headers, status, request);
	}
	
	private List<Error> createdListError(BindingResult bindingResult){
		List<Error> errors = new ArrayList<>();
		
		for(FieldError fieldError : bindingResult.getFieldErrors()) {
		String messageUser = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
		String messageDev = fieldError.toString() ;
		errors.add(new Error(messageUser, messageDev));
		}
		return errors;
	}
	
	
	public static class Error {
		private String messageUser;
		private String messageDev;
		
		public Error(String messageUser, String messageDev) {
			this.messageUser = messageUser;
			this.messageDev = messageDev;
		}

		public String getMessageUser() {
			return messageUser;
		}

		public String getMessageDev() {
			return messageDev;
		}

		
	}
}
