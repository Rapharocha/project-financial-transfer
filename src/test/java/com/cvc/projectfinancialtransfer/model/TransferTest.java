package com.cvc.projectfinancialtransfer.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.cvc.projectfinancialtransfer.exception.TransferException;
import com.cvc.projectfinancialtransfer.model.enumeration.Transaction;

public class TransferTest {

	private Transfer transfer;
	private Calendar calendar;
	
	@BeforeEach
	void setup() {
		transfer = new Transfer("172457", "189567", 100.0, Calendar.getInstance());
	}
	
	@Test
	void testCheckSameDayTrue() {
	    calendar = Calendar.getInstance();
		boolean result = transfer.checkSameDay(calendar);
		assertTrue(result);
	}
	
	@Test
	void testCheckSameDayFalse() {
		calendar = new GregorianCalendar(2022, 04, 8);
		boolean result = transfer.checkSameDay(calendar);
		assertFalse(result);
	}
	
	@Test
	void testDaysDifferenceException() {
		calendar = new GregorianCalendar(2021, 04, 7);
		assertThrows(TransferException.class, 
				() -> {transfer.dayDifferenceTransferToDayScheduling(calendar);}); 
	    
	}
	
	@Test
	void testDaysDifferenceOneYearMore() {
		calendar = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR) + 1,
				Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DATE)
				);
		assertEquals(365, transfer.dayDifferenceTransferToDayScheduling(calendar));
	}
	
	@Test
	void testTwentyDaysDifference() {
		calendar = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DATE) + 20
				);
		assertEquals(20, transfer.dayDifferenceTransferToDayScheduling(calendar));
	}
	
	@Test
	void testCheckTransactionA() {
		calendar = Calendar.getInstance();
		assertEquals(Transaction.A, transfer.checkTransaction(calendar));
	}
	
	@Test
	void testCheckTransactionB() {
		calendar = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DATE) + 10
				);
		assertEquals(Transaction.B, transfer.checkTransaction(calendar));
	}
	
	@Test
	void testCheckTransactionC() {
		calendar = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DATE) + 11
				);
		assertEquals(Transaction.C, transfer.checkTransaction(calendar));
	}
	
	@Test
	void testCalculateTaxTransactionA() {
		calendar = Calendar.getInstance();
		transfer.calculateTax(calendar);
		assertEquals(6, transfer.getTax());
	}
	
	@Test
	void testCalculateTaxTransactionB() {
		calendar = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DATE) + 10
				);
		transfer.calculateTax(calendar);
		assertEquals(120, transfer.getTax());
	}
	
	@Test
	void testCalculateTaxDaysDifferenceSmallerTwenty() {
		calendar = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DATE) + 19
				);
		transfer.calculateTax(calendar);
		assertEquals(8, transfer.getTax());
	}
	
	@Test
	void testCalculateTaxDaysDifferenceMoreTwenty() {
		calendar = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DATE) + 21
				);
		transfer.calculateTax(calendar);
		assertEquals(6, transfer.getTax());
	}
	
	@Test
	void testCalculateTaxDaysDifferenceMoreThirty() {
		calendar = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DATE) + 31
				);
		transfer.calculateTax(calendar);
		assertEquals(4, transfer.getTax());
	}
	
	@Test
	void testCalculateTaxDaysDifferenceMoreFortyAndTransferValueMoreThousand() {
		calendar = new GregorianCalendar(Calendar.getInstance().get(Calendar.YEAR),
				Calendar.getInstance().get(Calendar.MONTH),
				Calendar.getInstance().get(Calendar.DATE) + 41
				);
		transfer.setTransferValue(100.001);
		transfer.calculateTax(calendar);
		assertEquals(2.00002, transfer.getTax());
	}
}
