package com.blockeng.service.impl;

import com.blockeng.api.IdCardApi;
import com.blockeng.service.VerifyIdCardService;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author qiang
 */
@Service
public class VerifyIdCardServiceImpl implements VerifyIdCardService {

    @Override
    public boolean verifyIdCard(String realName, String cardNo) {
        try {
            return IdCardApi.verify(realName, cardNo);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
