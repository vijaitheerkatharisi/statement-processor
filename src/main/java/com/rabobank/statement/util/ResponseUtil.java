package com.rabobank.statement.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.rabobank.statement.api.controllers.v1.objects.Response;
import com.rabobank.statement.api.controllers.v1.objects.Status;
import com.rabobank.statement.constants.ResponseCode;
import com.rabobank.statement.service.objects.ServiceResponse;

public interface ResponseUtil {

	public static ResponseEntity<?> preperEntityForException(ResponseCode responseCode, Response response) {
		Status status = response.getStatus();
		status.setStatusCode(responseCode.getCode());
		status.setStatusDescription(responseCode.getDescrption());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	public static ResponseEntity<?> preperEntityForBadRequest(ResponseCode responseCode, Response response) {
		Status status = response.getStatus();
		status.setStatusCode(responseCode.getCode());
		status.setStatusDescription(responseCode.getDescrption());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	public static ResponseEntity<?> preperEntityForOk(ServiceResponse serviceResponse, Response response) {
		Status status = response.getStatus();
		if (serviceResponse != null) {
			status.setStatusCode(serviceResponse.getServiceResponse().getCode());
			status.setStatusDescription(serviceResponse.getServiceResponse().getDescrption());
		}
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
}
