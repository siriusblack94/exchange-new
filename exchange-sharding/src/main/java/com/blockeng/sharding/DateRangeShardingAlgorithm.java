package com.blockeng.sharding;

import com.google.common.collect.Range;
import io.shardingsphere.core.api.algorithm.sharding.RangeShardingValue;
import io.shardingsphere.core.api.algorithm.sharding.standard.RangeShardingAlgorithm;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static com.blockeng.sharding.DateUtil.*;


public class DateRangeShardingAlgorithm implements RangeShardingAlgorithm<Date> {
    //    @Autowired
    //    BeginDateConfig dateConfig;
    @Override
    public Collection<String> doSharding(Collection<String> availableTargetNames, RangeShardingValue<Date> shardingValue) {

        Range<Date> range = shardingValue.getValueRange();
        LocalDate lowerDate = BEGIN_DATE;
        LocalDate upperDate = LocalDate.now();

        if (range.hasLowerBound()) {
            try {
                lowerDate = toLocalDate(String.valueOf(range.lowerEndpoint()), "yyyy-MM-dd");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (range.hasUpperBound()) {
            try {
                String endTime = String.valueOf(range.upperEndpoint());
                if (endTime.indexOf("23:59:59") > -1) {
                    endTime = endTime.replace("23:59:59", "").trim();
                    endTime = DateUtil.toDateString(DateUtil.toLocalDate(endTime, "yyyy-MM-dd").plusDays(1), "yyyy-MM-dd");
                }
                upperDate = toLocalDate(endTime, "yyyy-MM-dd");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        final LocalDate lDate = lowerDate;
        final LocalDate uDate = upperDate;
        try {
            List<String> result = availableTargetNames.stream().filter((name) -> {
                String[] sp = name.split("_");
                String date = sp[sp.length - 1];
                if (toLocalDate(date, TABLE_NAME_FORMAT).isEqual(lDate))
                    return true;
                if (toLocalDate(date, TABLE_NAME_FORMAT).isAfter(lDate) && toLocalDate(date, TABLE_NAME_FORMAT).isBefore(uDate))
                    return true;
                return false;
            }).collect(Collectors.toList());

            if (result == null || result.size() == 0) {
                LinkedList list = new LinkedList(availableTargetNames);
                return Arrays.asList((String) new LinkedList(availableTargetNames).getLast());
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}
