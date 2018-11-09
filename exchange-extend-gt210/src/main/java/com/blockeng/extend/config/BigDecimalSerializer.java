package com.blockeng.extend.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigDecimalSerializer extends StdSerializer<BigDecimal> {

    protected BigDecimalSerializer(Class<BigDecimal> t) {
        super(t);
    }

    protected BigDecimalSerializer(JavaType type) {
        super(type);
    }

    protected BigDecimalSerializer(Class<?> t, boolean dummy) {
        super(t, dummy);
    }

    protected BigDecimalSerializer(StdSerializer<?> src) {
        super(src);
    }


    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {

        DecimalFormat decimalFormat = new DecimalFormat("################.########");
        gen.writeString(decimalFormat.format(value));

    }

    public static void main(String[] args) {
        BigDecimal value = new BigDecimal("0.000000000010010000");
        DecimalFormat decimalFormat = new DecimalFormat("################.################");
        System.out.println(decimalFormat.format(value));
    }

}
