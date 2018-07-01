package com.rabobank.statement.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rabobank.statement.api.controllers.v1.objects.Response;
import com.rabobank.statement.api.controllers.v1.objects.Status;
import com.rabobank.statement.constants.ResponseCode;

public interface ResponseUtil {
	

	
	public static ResponseEntity<?> preperEntityForOk(ResponseCode responseCode,Response response){
		Status status=response.getStatus();
		status.setStatusCode(responseCode.getCode());
		status.setStatusDescription(responseCode.getDescrption());
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	public static ResponseEntity<?> preperEntityForException(ResponseCode responseCode,Response response){
		Status status=response.getStatus();
		status.setStatusCode(responseCode.getCode());
		status.setStatusDescription(responseCode.getDescrption());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}
	public static ResponseEntity<?> preperEntityForBadRequest(ResponseCode responseCode,Response response){
		Status status=response.getStatus();
		status.setStatusCode(responseCode.getCode());
		status.setStatusDescription(responseCode.getDescrption());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
