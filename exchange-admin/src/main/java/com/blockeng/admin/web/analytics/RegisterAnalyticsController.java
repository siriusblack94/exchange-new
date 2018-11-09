package com.blockeng.admin.web.analytics;

import com.blockeng.framework.http.Response;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author qiang
 */
@RestController
@RequestMapping("/analytics")
public class RegisterAnalyticsController {

    @GetMapping("/register")
    Object register() {
        return Response.ok();
    }
}