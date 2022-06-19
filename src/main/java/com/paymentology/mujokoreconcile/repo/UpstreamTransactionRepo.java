package com.paymentology.mujokoreconcile.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymentology.mujokoreconcile.model.UpstreamTransaction;

public interface UpstreamTransactionRepo extends JpaRepository<UpstreamTransaction, Long> {

}
