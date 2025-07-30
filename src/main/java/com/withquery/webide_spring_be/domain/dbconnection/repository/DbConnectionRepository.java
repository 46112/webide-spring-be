package com.withquery.webide_spring_be.domain.dbconnection.repository;

import com.withquery.webide_spring_be.domain.dbconnection.entity.DbConnection;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DbConnectionRepository extends JpaRepository<DbConnection, Long> {}