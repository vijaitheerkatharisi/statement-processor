package com.rabobank.statement.service;

import java.io.File;

import com.rabobank.statement.service.objects.StatmentServiceResponse;

public interface StatementService {

	public StatmentServiceResponse processCSVStatement(File file);

	public StatmentServiceResponse processXMLStatement(File file);

}
