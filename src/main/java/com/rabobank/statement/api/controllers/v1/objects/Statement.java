package com.rabobank.statement.api.controllers.v1.objects;

public class Statement {

	private Long reference;
	private String description;

	public Long getReference() {
		return reference;
	}

	public void setReference(Long reference) {
		this.reference = reference;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "Statement [reference=" + reference + ", description=" + description + "]";
	}

	
}