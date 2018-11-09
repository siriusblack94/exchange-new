package com.blockeng.feign;

import com.blockeng.feign.hystrix.IP2regionServiceClientCallback;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author qiang
 */
@FeignClient(url = "http://ip.taobao.com", name = "ip-taobao", fallback = IP2regionServiceClientCallback.class)
public interface IP2regionServiceClient {

    @RequestMapping(value = "/service/getIpInfo.php", method = RequestMethod.GET)
    String getIpInfo(@RequestParam("ip") String ip);
}
