package com.rabobank.statement.parser.objects;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * XML Statement object
 * 
 * @author vijai
 *
 */
@XmlRootElement(name = "records")
public class XMLTransactions implements Transactions {

	List<Transaction> transactions;

	@XmlElement(name = "record")
	public List<Transaction> getStatements() {
		return transactions;
	}

	public void setStatements(List<Transaction> transactions) {
		this.transactions = transactions;
	}

}
