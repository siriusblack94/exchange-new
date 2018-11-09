package com.blockeng.boss.repository;

import com.blockeng.boss.dto.DealOrder;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;

/**
 * @author qiang
 */
public interface DealOrderRepository<T, ID extends Serializable> extends CrudRepository<DealOrder, Long> {
}
