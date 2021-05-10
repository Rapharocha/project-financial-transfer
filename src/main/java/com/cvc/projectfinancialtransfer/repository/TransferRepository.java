package com.cvc.projectfinancialtransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cvc.projectfinancialtransfer.model.Transfer;


public interface TransferRepository extends JpaRepository<Transfer, Long>{

	public Transfer findBySourceAccount(String sourceAccount);
	public Transfer findByTargetAccount(String targetAccount);
}
