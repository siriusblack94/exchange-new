package com.blockeng.utils;

import com.blueconic.browscap.Capabilities;
import com.blueconic.browscap.ParseException;
import com.blueconic.browscap.UserAgentParser;
import com.blueconic.browscap.UserAgentService;

import java.io.IOException;

/**
 * @author qiang
 */
public class UserAgentUtils {

    private static UserAgentParser parser;

    static {
        try {
            parser = new UserAgentService().loadParser();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static Capabilities parse(String userAgent) {
        return parser.parse(userAgent);
    }
}
