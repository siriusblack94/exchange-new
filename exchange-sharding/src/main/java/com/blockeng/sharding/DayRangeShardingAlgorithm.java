package com.blockeng.sharding;

import com.google.common.collect.Range;
import io.shardingsphere.core.api.algorithm.sharding.RangeShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.RangeShardingAlgorithm;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.blockeng.sharding.DateUtil.*;


public class DayRangeShardingAlgorithm implements RangeShardingAlgorithm<Date> {
    //    @Autowired
    //    BeginDateConfig dateConfig;
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Date> shardingValue) {
        Range<Date> range = shardingValue.getValueRange();
        long lowerIndex = 0;
        long upperIndex = until(BEGIN_DATE, LocalDate.now());
        if (range.hasLowerBound()) {
            try {
                lowerIndex = until(BEGIN_DATE, toLocalDate(String.valueOf(range.lowerEndpoint()), "yyyy-MM-dd"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (range.hasUpperBound()) {
            try {
                upperIndex = until(BEGIN_DATE, toLocalDate(String.valueOf(range.upperEndpoint()), "yyyy-MM-dd")) -1 ;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final long lindex = lowerIndex;
        final long uindex = upperIndex;
        try {
            List<String> result = availableTargetNames.stream().filter((name) -> {
                String[] sp = name.split("_");
                long index = Long.valueOf(sp[sp.length - 1]);
                if (index >= lindex && index <= uindex) {
                    return true;
                }
                return false;
            }).collect(Collectors.toList());
            if (result == null && result.size() == 0) {
                return Arrays.asList(availableTargetNames.stream().findFirst().get());
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
