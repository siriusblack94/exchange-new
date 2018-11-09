package com.blockeng.api.sms.smsbao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * sms.smsbao.url=https://api.smsbao.com/sms
 * sms.smsbao.u=
 * sms.smsbao.p=
 *
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "sms.smsbao")
@Configuration
public class SmsBaoConfig {

    private String url;

    private String u;

    public String getP() {
        return DigestUtils.md5Hex(p);
    }

    private String p;
}
