package com.rabobank.statement.service.impls;


import java.io.File;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.rabobank.statement.constants.ResponseCodeDescription;
import com.rabobank.statement.parser.exception.StatementParserException;
import com.rabobank.statement.parser.impls.CSVStatementParser;
import com.rabobank.statement.parser.impls.ParseContext;
import com.rabobank.statement.parser.impls.XMLStatementParser;
import com.rabobank.statement.parser.objects.Transaction;
import com.rabobank.statement.service.StatementService;
import com.rabobank.statement.service.objects.StatmentServiceResponse;
@Service
public class StatementProcessService implements StatementService {

	private static final Logger LOG = LoggerFactory.getLogger(StatementProcessService.class);

	@Autowired
	ParseContext parseContext;

	@Qualifier("csvStatementParser")
	@Autowired
	CSVStatementParser csvStatementParser;

	@Qualifier("xmlStatementParser")
	@Autowired
	XMLStatementParser xmlStatementParser;

	@Override
	public StatmentServiceResponse processCSVStatement(File file) {
		List<Transaction> transactions;
		StatmentServiceResponse statmentServiceResponse = new StatmentServiceResponse();
		try {
			parseContext.setParser(csvStatementParser);

			LOG.info("parsing file");
			transactions = collectFailureRecords(parseContext.parseFile(file));
			LOG.info("reterieved statementsfile");

			statmentServiceResponse.setTransactions(transactions);
			statmentServiceResponse.setServiceResponse(ResponseCodeDescription.SUCCESS);

			if (transactions.isEmpty())
				statmentServiceResponse.setServiceResponse(ResponseCodeDescription.NO_DATE_FOUND);

		} catch (StatementParserException exception) {
			LOG.error("statement parsing error", exception);
			statmentServiceResponse.setServiceResponse(ResponseCodeDescription.ERROR);
		}
		return statmentServiceResponse;
	}

	@Override
	public StatmentServiceResponse processXMLStatement(File file) {
		List<Transaction> transactions;
		StatmentServiceResponse statmentServiceResponse = new StatmentServiceResponse();
		try {
			parseContext.setParser(xmlStatementParser);

			LOG.info("parsing file");
			transactions = collectFailureRecords(parseContext.parseFile(file));

			LOG.debug("reterieved statementsfile-> {}", transactions);
			statmentServiceResponse.setTransactions(transactions);
			statmentServiceResponse.setServiceResponse(ResponseCodeDescription.SUCCESS);

			if (transactions.isEmpty())
				statmentServiceResponse.setServiceResponse(ResponseCodeDescription.NO_DATE_FOUND);

		} catch (StatementParserException exception) {
			LOG.error("statement parsing error", exception);
			statmentServiceResponse.setServiceResponse(ResponseCodeDescription.ERROR);
		}
		return statmentServiceResponse;
	}

	private List<Transaction> collectFailureRecords(List<Transaction> transactions) {
		List<Transaction> failureRecords = validateMutation(transactions);
		failureRecords.addAll(filterDuplicateReference(transactions));
		return failureRecords;
	}

	/**
	 *  Filters the duplicate transaction reference number
	 * @param transactions
	 * @return
	 */
	private List<Transaction> filterDuplicateReference(List<Transaction> transactions) {
		List<Transaction> duplicate = transactions.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream() // perform group by count
				.filter(e -> e.getValue() > 1L).map(Entry<Transaction, Long>::getKey).collect(Collectors.toList());// using take only those element whose count is greater than 1 and using map take only value
		return transactions.stream().map(stmt -> {
			for (Transaction duplicateStmt : duplicate) {
				if (duplicateStmt.getReference().equals(stmt.getReference())) {
					return stmt;
				}
			}
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());
	
	}

	/**
	 * Filters the transaction with mutation failure records
	 * @param transactions
	 * @return
	 */
	private List<Transaction> validateMutation(List<Transaction> transactions) {
		return transactions.stream().filter(transaction -> !isValid(transaction)).collect(Collectors.toList());
	}

	private boolean isValid(Transaction transaction) {
		return Math.round(transaction.getEndBalance() - transaction.getStartBalance()) == Math.round(transaction.getMutation());
	}
}
