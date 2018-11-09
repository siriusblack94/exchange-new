package com.blockeng.web;


import com.blockeng.service.ConfigService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/config")
@Slf4j
@Api(value = "配置信息", description = "配置信息")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    @ApiIgnore
    @GetMapping("/getConfig")
    public Object getConfig(@RequestParam(name = "type", required = true) String type, @RequestParam(name = "code", required = true) String code) {
        return configService.queryByTypeAndCode(type, code);
    }
}
