package com.blockeng.api.sms.chanyoo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * sms.chanyoo.url=http://api.chanyoo.cn/gbk/interface/send_sms.aspx
 * sms.chanyoo.username=
 * sms.chanyoo.password=
 *
 * @author qiang
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "sms.chanyoo")
@Configuration
public class ChanyooConfig {

    private String url;

    private String username;

    private String password;

    public String getPassword() {
        return DigestUtils.md5Hex(password);
    }
}
