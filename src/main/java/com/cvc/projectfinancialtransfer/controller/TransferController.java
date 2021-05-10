package com.cvc.projectfinancialtransfer.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.cvc.projectfinancialtransfer.model.Transfer;
import com.cvc.projectfinancialtransfer.repository.TransferRepository;
import com.cvc.projectfinancialtransfer.service.TransferService;

@RestController
@RequestMapping("/transf")
public class TransferController {

	@Autowired
	private TransferRepository repository;
	
	@Autowired
	private TransferService service;
	
	@GetMapping
	public Page<Transfer> findAllLimitFive( @RequestParam(defaultValue = "0") String numberPage) {
		int numberPag = Integer.parseInt(numberPage);
		Pageable page = PageRequest.of(numberPag, 5);
		return repository.findAll(page);
	} 
	
	@GetMapping("/{id}")
	public ResponseEntity<?> findById(@PathVariable Long id) {
		Optional<Transfer> transfer = repository.findById(id);
		return transfer.isPresent() ? ResponseEntity.ok(transfer) : ResponseEntity.notFound().build();
	}
	
	@PostMapping
	public ResponseEntity<Transfer> created(@Valid @RequestBody Transfer transfer) {
		Transfer transfSave = service.created(transfer);
		return ResponseEntity.status(HttpStatus.CREATED).body(transfSave);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Transfer> update(@PathVariable Long id, @Valid @RequestBody Transfer transfer) {
		Transfer transferSave = service.update(id, transfer);
		return ResponseEntity.ok(transferSave);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		repository.deleteById(id);
	}
	
}
