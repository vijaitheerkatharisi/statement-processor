package com.rabobank.statement.parser.objects;

import java.util.List;

public interface Transactions {

	public List<Transaction> getStatements();

	public void setStatements(List<Transaction> transactions);
}
