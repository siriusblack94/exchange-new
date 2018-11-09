package com.blockeng.sharding;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class BeginDateConfig {

    @Value("${sharding.jdbc.datasource.ext.begin-date:*}")
    private String beginDate; //开始日期

    @Value("${sharding.jdbc.datasource.ext.suffix-format:*}")
    private String format; // 日期格式化

}
