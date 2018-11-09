package com.blockeng.api;

import com.blockeng.api.sms.amdxw.AmdxwConfig;
import com.blockeng.api.sms.amdxw.AmdxwSmsApi;
import com.blockeng.api.sms.chanyoo.ChanyooConfig;
import com.blockeng.api.sms.chanyoo.ChanyooSmsApi;
import com.blockeng.api.sms.cl253.Cl253Config;
import com.blockeng.api.sms.cl253.Cl253SmsApi;
import com.blockeng.api.sms.emay.EmayConfig;
import com.blockeng.api.sms.emay.EmaySmsApi;
import com.blockeng.api.sms.ihuyi.IhuyiConfig;
import com.blockeng.api.sms.ihuyi.IhuyiSmsApi;
import com.blockeng.api.sms.jiuzhouyun.JiuZhouYunConfig;
import com.blockeng.api.sms.jiuzhouyun.JiuZhouYunSmsApi;
import com.blockeng.api.sms.luosimao.LuosimaoConfig;
import com.blockeng.api.sms.luosimao.LuosimaoSmsApi;
import com.blockeng.api.sms.m5c.M5cConfig;
import com.blockeng.api.sms.m5c.M5cSmsApi;
import com.blockeng.api.sms.panfeng.PanfengConfig;
import com.blockeng.api.sms.panfeng.PanfengSmsApi;
import com.blockeng.api.sms.smsbao.SmsBaoConfig;
import com.blockeng.api.sms.smsbao.SmsBaoSmsApi;
import com.blockeng.api.sms.smschinese.SmsChineseApi;
import com.blockeng.api.sms.smschinese.SmsChineseConfig;
import com.blockeng.api.sms.yunpian.YunpianConfig;
import com.blockeng.api.sms.yunpian.YunpianSmsApi;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

/**
 * @author qiang
 */
@Component
public class SmsApi {

    @Autowired
    public void setAmdxwConfig(AmdxwConfig amdxwConfig) {
        SmsApi.amdxwConfig = amdxwConfig;
    }

    @Autowired
    public void setPanfengConfig(PanfengConfig panfengConfig) {
        SmsApi.panfengConfig = panfengConfig;
    }

    @Autowired
    public void setM5cConfig(M5cConfig m5cConfig) {
        SmsApi.m5cConfig = m5cConfig;
    }

    @Autowired
    public void setChanyooConfig(ChanyooConfig chanyooConfig) {
        SmsApi.chanyooConfig = chanyooConfig;
    }

    @Autowired
    public void setEmayConfig(EmayConfig emayConfig) {
        SmsApi.emayConfig = emayConfig;
    }

    @Autowired
    public void setJiuZhouYunConfig(JiuZhouYunConfig jiuZhouYunConfig) {
        SmsApi.jiuZhouYunConfig = jiuZhouYunConfig;
    }

    @Autowired
    public void setSmsChineseConfig(SmsChineseConfig smsChineseConfig) {
        SmsApi.smsChineseConfig = smsChineseConfig;
    }

    @Autowired
    public void setSmsBaoConfig(SmsBaoConfig smsBaoConfig) {
        SmsApi.smsBaoConfig = smsBaoConfig;
    }

    @Autowired
    public void setYunpianConfig(YunpianConfig yunpianConfig) {
        SmsApi.yunpianConfig = yunpianConfig;
    }

    @Autowired
    public void setLuosimaoConfig(LuosimaoConfig luosimaoConfig) {
        SmsApi.luosimaoConfig = luosimaoConfig;
    }

    @Autowired
    public void setCl253Config(Cl253Config cl253Config) {
        SmsApi.cl253Config = cl253Config;
    }

    @Autowired
    public void setIhuyiConfig(IhuyiConfig ihuyiConfig) {
        SmsApi.ihuyiConfig = ihuyiConfig;
    }

    private static AmdxwConfig amdxwConfig;

    private static PanfengConfig panfengConfig;

    private static M5cConfig m5cConfig;

    private static ChanyooConfig chanyooConfig;

    private static EmayConfig emayConfig;

    private static JiuZhouYunConfig jiuZhouYunConfig;

    private static SmsChineseConfig smsChineseConfig;

    private static SmsBaoConfig smsBaoConfig;

    private static YunpianConfig yunpianConfig;

    private static LuosimaoConfig luosimaoConfig;

    private static Cl253Config cl253Config;

    private static IhuyiConfig ihuyiConfig;

    /**
     * 发送接口
     *
     * @param countryCode
     * @param phone
     * @param text
     * @return
     * @throws IOException
     */
    public static String sendTo(String countryCode, String phone, String text) {
        if (!Optional.ofNullable(luosimaoConfig).isPresent() && !Strings.isNullOrEmpty(luosimaoConfig.getUrl())) {
            LuosimaoSmsApi.setLuosimaoConfig(luosimaoConfig);
            return LuosimaoSmsApi.sendTo(phone, text);
        } else if (!Optional.ofNullable(amdxwConfig).isPresent() && !Strings.isNullOrEmpty(amdxwConfig.getUrl())) {
            AmdxwSmsApi.setAmdxwConfig(amdxwConfig);
            return AmdxwSmsApi.sendTo(phone, text);
        } else if (Optional.ofNullable(panfengConfig).isPresent() && !Strings.isNullOrEmpty(panfengConfig.getUrl())) {
            PanfengSmsApi.setPanfengConfig(panfengConfig);
            return PanfengSmsApi.sendTo(phone, text);
        } else if (Optional.ofNullable(m5cConfig).isPresent() && !Strings.isNullOrEmpty(m5cConfig.getUrl())) {
            M5cSmsApi.setM5cConfig(m5cConfig);
            return M5cSmsApi.sendTo(phone, text);
        } else if (Optional.ofNullable(chanyooConfig).isPresent() && !Strings.isNullOrEmpty(chanyooConfig.getUrl())) {
            ChanyooSmsApi.setChanyooConfig(chanyooConfig);
            return ChanyooSmsApi.sendTo(phone, text);
        } else if (Optional.ofNullable(emayConfig).isPresent() && !Strings.isNullOrEmpty(emayConfig.getUrl())) {
            EmaySmsApi.setEmayConfig(emayConfig);
            return EmaySmsApi.sendTo(phone, text);
        } else if (Optional.ofNullable(jiuZhouYunConfig).isPresent() && !Strings.isNullOrEmpty(jiuZhouYunConfig.getUrl())) {
            JiuZhouYunSmsApi.setJiuZhouYunConfig(jiuZhouYunConfig);
            return JiuZhouYunSmsApi.sendTo(phone, text);
        } else if (Optional.ofNullable(smsChineseConfig).isPresent() && !Strings.isNullOrEmpty(smsChineseConfig.getUrl())) {
            SmsChineseApi.setSmsChineseConfig(smsChineseConfig);
            return SmsChineseApi.sendTo(phone, text);
        } else if (Optional.ofNullable(yunpianConfig).isPresent() && !Strings.isNullOrEmpty(yunpianConfig.getUrl())) {
            YunpianSmsApi.setYunpianConfig(yunpianConfig);
            return YunpianSmsApi.sendTo(phone, text);
        } else if (Optional.ofNullable(smsBaoConfig).isPresent() && !Strings.isNullOrEmpty(smsBaoConfig.getUrl())) {
            SmsBaoSmsApi.setSmsBaoConfig(smsBaoConfig);
            return SmsBaoSmsApi.sendTo(phone, text);
        } else if (Optional.ofNullable(ihuyiConfig).isPresent() && !Strings.isNullOrEmpty(ihuyiConfig.getUrl())) {
            IhuyiSmsApi.setIhuyiConfig(ihuyiConfig);
            return IhuyiSmsApi.sendTo(phone, text);
        } else if (Optional.ofNullable(cl253Config).isPresent() && !Strings.isNullOrEmpty(cl253Config.getUrl())) {
            Cl253SmsApi.setCl253Config(cl253Config);
            if (Strings.isNullOrEmpty(cl253Config.getApp()) || cl253Config.getApp().equalsIgnoreCase("cl_international_sms")) {
                phone = countryCode + phone;
                phone = phone.replace("+", "");
            }
            return Cl253SmsApi.sendTo(phone, text);
        }
        return null;
    }
}