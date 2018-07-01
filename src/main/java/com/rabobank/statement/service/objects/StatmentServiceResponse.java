package com.rabobank.statement.service.objects;

import java.util.List;

import com.rabobank.statement.constants.ResponseCode;
import com.rabobank.statement.parser.objects.Transaction;

public class StatmentServiceResponse implements ServiceResponse {

	private ResponseCode serviceResponse;
	private List<Transaction> transactions;

	@Override
	public ResponseCode getServiceResponse() {
		return serviceResponse;
	}

	public void setServiceResponse(ResponseCode serviceResponse) {
		this.serviceResponse = serviceResponse;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}

}
