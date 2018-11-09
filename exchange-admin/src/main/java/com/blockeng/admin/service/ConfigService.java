package com.blockeng.admin.service;

import com.baomidou.mybatisplus.service.IService;
import com.blockeng.admin.entity.Config;
import com.blockeng.framework.constants.Constant;

/**
 * <p>
 * 平台配置信息 服务类
 * </p>
 *
 * @author qiang
 * @since 2018-05-13
 */
public interface ConfigService extends IService<Config>, Constant {

    /**
     * 查询系统配置
     *
     * @param type 类型
     * @param code 代码
     * @return
     */
    Config queryBuyCodeAndType(String type, String code);
}