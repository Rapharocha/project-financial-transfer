package com.cvc.projectfinancialtransfer.controller;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.given;
import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import com.cvc.projectfinancialtransfer.model.Transfer;
import com.cvc.projectfinancialtransfer.model.enumeration.Transaction;
import com.cvc.projectfinancialtransfer.repository.TransferRepository;
import com.cvc.projectfinancialtransfer.service.TransferService;

import io.restassured.http.ContentType;

@WebMvcTest
public class TransferControllerTest {

	@Autowired
	private TransferController controller;
	
	@MockBean
	private TransferRepository repository;
	
	@MockBean
	private TransferService service;
	
	@BeforeEach
	public void setup() {
		standaloneSetup(this.controller);
	}
	
	@Test
	public void testFindAllLimitFiveReturnSucess() {
		Pageable pageable = PageRequest.of(1, 5);
		Transfer t1 = new Transfer(1L, "123456", "234567", 200.0, 2.0, Calendar.getInstance(), Calendar.getInstance(),Transaction.A);
		Transfer t2 = new Transfer(2L, "134856", "234567", 200.0, 2.0, Calendar.getInstance(), Calendar.getInstance(),Transaction.A);
		List<Transfer> transferList = new ArrayList<>();
		transferList.add(t1);
		transferList.add(t2);
		Page<Transfer> pages = new PageImpl<Transfer>(transferList, pageable, transferList.size());
		
		when(this.repository.findAll(pages.getPageable()))
			.thenReturn(pages);
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get("/transf")
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void testCreatedReturnStatus201() {

		String json = "{\"sourceAccount\":\"123456\","
		+ "\"targetAccount\":\"234567\","
		+ "\"transferValue\":\"200.0\","
		+ "\"dateTransfer\":\"2021-05-10\""
		+ "}";

		given()
			.body(json)
			.contentType(ContentType.JSON)
		.when()
			.post("/transf")
		.then()
		.	statusCode(HttpStatus.CREATED.value());
	}
	
	@Test
	public void testCreatedReturnStatus400() {

		String json = "{\"sourceAccount\":\"123456\","
		+ "\"targetAccount\":\"234567\","
		+ "\"transferValue\":\"200.0\""
		+ "}";

		given()
			.body(json)
			.contentType(ContentType.JSON)
		.when()
			.post("/transf")
		.then()
		.	statusCode(HttpStatus.BAD_REQUEST.value());
	}
	
	@Test
	public void testUpdateReturnSucess() {

		String json = "{\"sourceAccount\":\"123456\","
				+ "\"targetAccount\":\"234567\","
				+ "\"transferValue\":\"200.0\","
				+ "\"dateTransfer\":\"2021-05-10\""
				+ "}";

		given()
			.body(json)
			.contentType(ContentType.JSON)
		.when()
			.put("/transf/{id}", 1L)
		.then()
		.	statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void testDeleteReturnStatus204() {
		given()
			.contentType(ContentType.JSON)
		.when()
			.delete("/transf/{id}", 1L)
		.then()
			.statusCode(HttpStatus.NO_CONTENT.value());
	}
	
	
	@Test
	public void testFindByIdReturnSucess() {
		Optional<Transfer> transf = Optional.of(new Transfer(1L, "123456", "234567", 200.0, 2.0, Calendar.getInstance(), Calendar.getInstance(),Transaction.A));
		when(this.repository.findById(1L))
			.thenReturn(transf);
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get("/transf/{id}", 1L)
		.then()
			.statusCode(HttpStatus.OK.value());
	}
	
	@Test
	public void testFindByIdReturnNotFound() {
		Optional<Transfer> transf = Optional.ofNullable(null);
		when(this.repository.findById(2L))
			.thenReturn(transf);
		
		given()
			.accept(ContentType.JSON)
		.when()
			.get("/transf/{id}", 2L)
		.then()
			.statusCode(HttpStatus.NOT_FOUND.value());
	}
	
}
