package com.cvc.projectfinancialtransfer.model;

import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.cvc.projectfinancialtransfer.exception.TransferException;
import com.cvc.projectfinancialtransfer.model.enumeration.Transaction;
import com.cvc.projectfinancialtransfer.model.fixtures.Fixtures;

@Entity
public class Transfer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "field [sourceAccount] cannot be null")
	@Pattern(regexp = "^[0-9]*$", message = "Field [sourceAccount] have to be numeric!")
	@Size(min = 6, max = 6, message = "the size of the [sourceAccount] field must be 6")
	private String sourceAccount;
	
	@NotNull(message = "field [targetAccount] cannot be null")
	@Pattern(regexp = "^[0-9]*$", message = "Field [targetAccount] have to be numeric!")
	@Size(min = 6, max = 6, message = "the size of the [targetAccount] field must be 6")
	private String targetAccount;
	
	@NotNull(message = "field [transferValue] cannot be null")
	@DecimalMin(value = "0.01", message = "field [transferValue] cannot be less than 0.01")
	@DecimalMax(value = "999999", message = "field [transferValue] cannot be greater than 999.999,00")
	private Double transferValue;
	
	private Double tax;
	
	@NotNull(message = "field [dateTransfer] cannot be null")
	private Calendar dateTransfer;
	
	private Calendar dateScheduling = Calendar.getInstance();
	
	private Transaction typeTransaction;
	
	public Transfer() {}
	
	public Transfer(String sourceAccount, String targetAccount, Double transferValue, Calendar dateTransfer) {
		this.sourceAccount = sourceAccount;
		this.targetAccount = targetAccount;
		this.transferValue = transferValue;
		this.dateTransfer = dateTransfer;
	}
	
	
	public Transfer(Long id,
			 String sourceAccount, 
			 String targetAccount,
			 Double transferValue,
			 Double tax,
			 Calendar dateTransfer,
			 Calendar dateScheduling,
			 Transaction typeTransaction) {

		this.id = id;
		this.sourceAccount = sourceAccount;
		this.targetAccount = targetAccount;
		this.transferValue = transferValue;
		this.tax = tax;
		this.dateTransfer = dateTransfer;
		this.dateScheduling = dateScheduling;
		this.typeTransaction = typeTransaction;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSourceAccount() {
		return sourceAccount;
	}

	public void setSourceAccount(String sourceAccount) {
		this.sourceAccount = sourceAccount;
	}

	public String getTargetAccount() {
		return targetAccount;
	}

	public void setTargetAccount(String targetAccount) {
		this.targetAccount = targetAccount;
	}

	public Double getTransferValue() {
		return transferValue;
	}

	public void setTransferValue(Double transferValue) {
		this.transferValue = transferValue;
	}

	public Double getTax() {
		return tax;
	}

	public void setTax(Double tax) {
		this.tax = tax;
	}

	public Calendar getDateTransfer() {
		return dateTransfer;
	}

	public void setDateTransfer(Calendar dateTransfer) {
		this.dateTransfer = dateTransfer;
	}

	public Calendar getDateScheduling() {
		return dateScheduling;
	}

	public void setDateScheduling(Calendar dateScheduling) {
		this.dateScheduling = dateScheduling;
	}
	
	public Transaction getTypeTransaction() {
		return typeTransaction;
	}

	public void setTypeTransaction(Transaction typeTransaction) {
		this.typeTransaction = typeTransaction;
	}
	
	public boolean checkSameDay(Calendar dateTransfer) {
		if(dateTransfer.get(Calendar.DAY_OF_YEAR) == this.dateScheduling.get(Calendar.DAY_OF_YEAR) 
				&& dateTransfer.get(Calendar.YEAR) == this.dateScheduling.get(Calendar.YEAR)) {
			return true;
		}
		return false;
	}
	
	public int dayDifferenceTransferToDayScheduling(Calendar dateTransfer) {
		Fixtures.daysDifference = dateTransfer.get(Calendar.DAY_OF_YEAR) - this.dateScheduling.get(Calendar.DAY_OF_YEAR);
		Fixtures.yearsDifference = dateTransfer.get(Calendar.YEAR) - this.dateScheduling.get(Calendar.YEAR);
		if(Fixtures.yearsDifference > 0) {
			Fixtures.daysDifference += Fixtures.yearsDifference * 365;
		} else if (Fixtures.yearsDifference < 0) {
			Fixtures.daysDifference -= Fixtures.yearsDifference * 365;
		}
		if(Fixtures.daysDifference < 0) {
			throw new TransferException("Transfer Date cannot be earlier than the Scheduling date !!");
		}
		return Fixtures.daysDifference;
	}
		
	public Transaction checkTransaction(Calendar dateTransfer) {
		
		if(checkSameDay(dateTransfer)) {
			return Transaction.A;
			
		} else if (dateTransfer.after(this.dateScheduling)) {

			Fixtures.daysDifference = dayDifferenceTransferToDayScheduling(dateTransfer);
			
			if(Fixtures.daysDifference <= 10) {
				return Transaction.B;
				
			} else if (Fixtures.daysDifference > 10) {
				return Transaction.C;
				
			}	
		}
		
		return Transaction.Invalida;
	}
	
	public void calculateTax(Calendar dateTransfer) {
		
		Fixtures.daysDifference = dayDifferenceTransferToDayScheduling(dateTransfer);
		this.typeTransaction = checkTransaction(dateTransfer);
		
		if(typeTransaction == Transaction.A) {
			this.tax = 3.00 + (this.transferValue * 0.03);
		} else if (typeTransaction == Transaction.B) {
			
			this.tax = 12.00 * Fixtures.daysDifference;
		} else if (typeTransaction == Transaction.C) {
			if(Fixtures.daysDifference < 20) {
				this.tax = this.transferValue * 0.08;
			} else if (Fixtures.daysDifference > 20 && Fixtures.daysDifference <= 30) {
				
				this.tax = this.transferValue * 0.06;
				
			} else if (Fixtures.daysDifference > 30 && Fixtures.daysDifference <= 40) {
				
				this.tax = this.transferValue * 0.04;
				
			} else if(Fixtures.daysDifference > 40 && transferValue > 100.000){

				this.tax = this.transferValue * 0.02;
			}
		} 
		
	}	
	
}
