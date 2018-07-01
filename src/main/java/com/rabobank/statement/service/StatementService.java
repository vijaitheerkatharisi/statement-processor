package com.rabobank.statement.service;

import java.io.File;

import com.rabobank.statement.service.objects.StatementServiceResponse;


public interface StatementService {
	
	public StatementServiceResponse processCSVStatement(File file);
	public StatementServiceResponse processXMLStatement(File file);

}
