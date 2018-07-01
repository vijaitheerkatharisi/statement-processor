package com.rabobank.statement.api.controllers.v1.objects;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;

@ApiModel("Statement Response")
public class StatementResponse implements Response {

	@JsonProperty(value = "status")
	private StatementProcessStatus status = new StatementProcessStatus();

	@JsonProperty(value = "failure_records")
	@JsonInclude(JsonInclude.Include.NON_EMPTY)
	private List<Statement> statement = new ArrayList<>();

	public List<Statement> getStatement() {
		return statement;
	}

	public void setStatement(List<Statement> statement) {
		this.statement = statement;
	}

	@Override
	public StatementProcessStatus getStatus() {
		return status;
	}

	public void setStatus(StatementProcessStatus status) {
		this.status = status;

	}

}