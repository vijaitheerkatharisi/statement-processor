package com.rabobank.statement.service.impls;


import java.io.File;
import java.util.List;
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
import com.rabobank.statement.parser.objects.Statement;
import com.rabobank.statement.service.StatementService;
import com.rabobank.statement.service.objects.StatementServiceResponse;
@Service
public class StatementProcessService implements StatementService {

	private static final Logger LOG=LoggerFactory.getLogger(StatementProcessService.class);
	
	@Autowired
	ParseContext parseContext;
	
	@Qualifier("csvStatementParser")
	@Autowired
	CSVStatementParser csvStatementParser;
	
	@Qualifier("xmlStatementParser")
	@Autowired
	XMLStatementParser xmlStatementParser;
	
	@Override
	public StatementServiceResponse processCSVStatement(File file) {
		List<Statement> statements;
		StatementServiceResponse statementServiceResponse=new StatementServiceResponse();
		try {
			parseContext.setParser(csvStatementParser);
			
			LOG.info("parsing file");
			statements=collectFailureRecords(parseContext.paresFile(file));
			LOG.info("reterieved statementsfile");
			statementServiceResponse.setStatements(statements);
			statementServiceResponse.setServiceResponse(ResponseCodeDescription.SUCCESS);
			if(statements.isEmpty())
			statementServiceResponse.setServiceResponse(ResponseCodeDescription.NO_DATE_FOUND);
			
		} catch (StatementParserException exception) {
			LOG.error("statement parsing error", exception);
			statementServiceResponse.setServiceResponse(ResponseCodeDescription.ERROR);
		} 
		return statementServiceResponse;
	}
	
	@Override
	public StatementServiceResponse processXMLStatement(File file) {
		List<Statement> statements;
		StatementServiceResponse statementServiceResponse=new StatementServiceResponse();
		try {
			parseContext.setParser(xmlStatementParser);
			
			LOG.info("parsing file");
			statements=collectFailureRecords(parseContext.paresFile(file));
			
			LOG.info("reterieved statementsfile");
			statementServiceResponse.setStatements(statements);
			statementServiceResponse.setServiceResponse(ResponseCodeDescription.SUCCESS);
			if(statements.isEmpty())
			statementServiceResponse.setServiceResponse(ResponseCodeDescription.NO_DATE_FOUND);
			
		} catch (StatementParserException exception) {
			LOG.error("statement parsing error", exception);
			statementServiceResponse.setServiceResponse(ResponseCodeDescription.ERROR);
		} 
		return statementServiceResponse;
	}
	
	private List<Statement> collectFailureRecords(List<Statement> statements) {
		List<Statement> failureRecords = validateMutation(statements);
		failureRecords.addAll(validateReference(statements));
		return failureRecords;
	}

	private List<Statement> validateReference(List<Statement> statements) {
		List<Statement> duplicate = statements.stream()
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())).entrySet().stream() // perform group by count
				.filter(e -> e.getValue() > 1L).map(e -> e.getKey()).collect(Collectors.toList());// using take only those element whose count is greater than 1 and using map take only value
		return statements.stream().map(stmt -> {
			for (Statement duplicateStmt : duplicate) {
				if (duplicateStmt.getReference().equals(stmt.getReference())) {
					return stmt;
				}
			}
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}
	private List<Statement> validateMutation(List<Statement> statements){
		return statements.stream().filter(statement->!isValid(statement)).collect(Collectors.toList());
	}

	private boolean isValid(Statement statement){
		return Math.round(statement.getEndBalance()-statement.getStartBalance())==Math.round(statement.getMutation());
	}
}
