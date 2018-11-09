package com.blockeng.repository;

import com.blockeng.entity.EntrustOrder;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

/**
 * @author qiang
 */
public interface OrderRepository<T, ID extends Serializable> extends CrudRepository<EntrustOrder, Long> {

}