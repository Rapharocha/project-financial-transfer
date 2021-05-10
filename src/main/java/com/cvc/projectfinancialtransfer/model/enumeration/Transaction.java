package com.cvc.projectfinancialtransfer.model.enumeration;

public enum Transaction {
	A("Transaction A"),
	B("Transaction B"),
	C("Transaction C"),
	Invalida("Transaction Inválida");
	
	private String type;
	
	Transaction(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
	
	
}
