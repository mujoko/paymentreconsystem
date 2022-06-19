package com.paymentology.mujokoreconcile.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.paymentology.mujokoreconcile.model.UnmatchTransaction;

public interface UnmatchTransactionRepo extends JpaRepository<UnmatchTransaction, Long> {

	List<UnmatchTransaction> findByDataSourceIs(String dataSource);

	List<UnmatchTransaction> findByDataSourceIsOrderByCreatedDateAsc(String dataSource);

	List<UnmatchTransaction> findByDataSourceIsNot(String dataSource);

	List<UnmatchTransaction> findByDataSourceIsNotOrderByCreatedDateAsc(String dataSource);

}
