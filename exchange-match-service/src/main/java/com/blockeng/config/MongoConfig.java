package com.blockeng.config;

import org.bson.types.Decimal128;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.lang.NonNull;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author qiang
 */
@Configuration
public class
MongoConfig {

    @Bean
    public MongoCustomConversions mongoCustomConversions() {
        List<Converter> converters = new ArrayList<>();
        converters.add(new Converter<BigDecimal, Decimal128>() {
            @Override
            public Decimal128 convert(@NonNull BigDecimal source) {
                return new Decimal128(source);
            }
        });
        converters.add(new Converter<Decimal128, BigDecimal>() {
            @Override
            public BigDecimal convert(@NonNull Decimal128 source) {
                return source.bigDecimalValue();
            }
        });
        return new MongoCustomConversions(converters);
    }
}
