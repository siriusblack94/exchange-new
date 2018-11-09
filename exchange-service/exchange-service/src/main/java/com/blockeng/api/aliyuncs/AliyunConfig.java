package com.blockeng.api.aliyuncs;

import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "aliyun")
@Configuration
public class AliyunConfig {

    private String regionId;

    private String accessKeyId;

    private String secret;

    public IClientProfile getProfile() {
        return DefaultProfile.getProfile(regionId, accessKeyId, secret);
    }
}