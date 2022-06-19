package com.paymentology.mujokoreconcile.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymentology.mujokoreconcile.model.DownstreamTransaction;

public interface DownstreamTransactionRepo extends JpaRepository<DownstreamTransaction, Long> {

}
