package com.rabobank.statement.service.objects;

import java.util.List;

import com.rabobank.statement.constants.ResponseCode;
import com.rabobank.statement.parser.objects.Statement;

public class StatementServiceResponse {
	
	private ResponseCode serviceResponse;
	private List<Statement> statements;
	public ResponseCode getServiceResponse() {
		return serviceResponse;
	}
	public void setServiceResponse(ResponseCode serviceResponse) {
		this.serviceResponse = serviceResponse;
	}
	public List<Statement> getStatements() {
		return statements;
	}
	public void setStatements(List<Statement> statements) {
		this.statements = statements;
	}
	
}
