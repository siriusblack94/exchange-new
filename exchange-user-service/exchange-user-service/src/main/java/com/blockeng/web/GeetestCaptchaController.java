package com.blockeng.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blockeng.framework.geetest.GeetestLib;
import com.blockeng.framework.http.Response;
import com.blockeng.framework.utils.IpUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author qiang
 */
@RestController
@RequestMapping("/gt")
@Api(value = "极验验证", tags = "极验验证")
public class GeetestCaptchaController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private GeetestLib gtSdk;

    @GetMapping("/register")
    @ApiOperation(value = "验证预处理", httpMethod = "GET")
    Object register() {
        //自定义userid
        String userid = "qiang";
        String ip = IpUtil.getIpAddr(request);

        //自定义参数,可选择添加
        HashMap<String, String> param = new HashMap<String, String>();
        param.put("user_id", userid); //网站用户id
        param.put("client_type", "web"); //web:电脑上的浏览器；h5:手机上的浏览器，包括移动应用内完全内置的web_view；native：通过原生SDK植入APP应用的方式
        param.put("ip_address", ip); //传输用户请求验证时所携带的IP

        //进行验证预处理
        int gtServerStatus = gtSdk.preProcess(param);
        JSONObject result = JSON.parseObject(gtSdk.getResponseStr());
        result.put("ip", ip);
        return Response.ok(result);
    }
}
