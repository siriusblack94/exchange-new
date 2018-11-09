package com.blockeng.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

/**
 * @author qiang
 */


public interface UserLoginLogRepository<T, ID extends Serializable> extends CrudRepository<UserLoginLog, String> {

    Page<T> findByUserId(Long userId, Pageable pageable);
}
