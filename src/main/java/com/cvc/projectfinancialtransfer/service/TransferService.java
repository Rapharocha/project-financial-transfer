package com.cvc.projectfinancialtransfer.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cvc.projectfinancialtransfer.model.Transfer;
import com.cvc.projectfinancialtransfer.repository.TransferRepository;

@Service
public class TransferService {

	@Autowired
	private TransferRepository repository;
	
	public Transfer created(Transfer transf) {
		transf.calculateTax(transf.getDateTransfer());
		repository.save(transf);
		return transf;
	}

	public Transfer update(Long id, Transfer transfer) {
		Transfer transferSave = repository.findById(id).get();
		BeanUtils.copyProperties(transfer, transferSave, "id");
		transferSave.calculateTax(transferSave.getDateTransfer());
		return repository.save(transferSave);
	} 
}
