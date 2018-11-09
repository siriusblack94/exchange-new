package com.blockeng.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.MongoCollectionUtils;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author qiang
 */

@Repository
@Component
public interface UserLoginLogRepository<T, ID extends Serializable> extends CrudRepository<UserLoginLog, String> {

    @Query("{ 'login_time_long' :{ $gt : ?0 , $lt : ?1 } }")
    Page<UserLoginLog> findByQueryWithExpression(long isoStartTime, long isoEndTime, Pageable pageable);


    // int findCountByQueryWithExpression(Date parse, Date parse1);
}
