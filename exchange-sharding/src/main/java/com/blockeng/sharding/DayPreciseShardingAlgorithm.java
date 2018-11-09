package com.blockeng.sharding;

import io.shardingsphere.core.api.algorithm.sharding.PreciseShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.PreciseShardingAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Date;

import static com.blockeng.sharding.DateUtil.*;


public class DayPreciseShardingAlgorithm implements PreciseShardingAlgorithm<Date> {

    @Autowired
    BeginDateConfig dateConfig;

    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<Date> preciseShardingValue) {

        return collection.stream().filter((name) -> {
            Date date = preciseShardingValue.getValue();
            LocalDate endDate = toLocalDate(date);
            //   LocalDate startDate = DateUtil.toLocalDate(dateConfig.getBeginDate());
            long index = until(BEGIN_DATE, endDate);
          //  preciseShardingValue.get
            return name.endsWith(preciseShardingValue.getLogicTableName() + "_" + index);
        }).findFirst().get();
    }


}
