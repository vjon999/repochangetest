package com.prapps.core.domain;

public enum RolesTypes {
	
	VERIFIER("ROLE_VERIFIER"),
	APPROVER("ROLE_APPROVER"),
	BANK_USER("ROLE_BANK_USER"),
	SUPER_ADMIN("ROLE_SUPER_USER");
	
	private final String value;
	
	RolesTypes(String type) {
		value = type;
	}
	
	public String toString() {
        return value;
    }
}
