package com.blockeng.extend.web;

import com.blockeng.extend.util.DESUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: sirius
 * @Date: 2018/10/31 17:29
 * @Description:
 */
@RestController
public class TestController {
    @Autowired
    DESUtil desUtil;
    @RequestMapping(value = "/encrypt")
    public Object encrypt(String data){
//        String url="https://nbnb.bxx.com/admin/entrustOrder/cancelOrder/1";
//        RestClient.setRestTemplate(restTemplate);
//        return  RestClient.postJson(url,null);
        ;
        return desUtil.encrypt(data)   ;
    }

    @RequestMapping(value = "/decrypt")
    public Object decrypt(String data){
//        String url="https://nbnb.bxx.com/admin/entrustOrder/cancelOrder/1";
//        RestClient.setRestTemplate(restTemplate);
//        return  RestClient.postJson(url,null);
        ;

        return desUtil.decrypt(data)   ;
    }

}
