package com.blockeng.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

/**
 * @author qiang
 */
public interface SendRecordRepository<T, ID extends Serializable> extends CrudRepository<SendRecord, String> {

    Page<T> findAll(Pageable pageable);
}