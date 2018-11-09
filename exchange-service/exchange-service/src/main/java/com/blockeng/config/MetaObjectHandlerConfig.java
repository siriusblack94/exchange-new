package com.blockeng.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

/**
 * @author qiang
 */
@Component
@Slf4j
public class MetaObjectHandlerConfig extends MetaObjectHandler {

    /**
     * 插入方法实体填充
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        //setFieldValByName("testDate", new Date(), metaObject);
    }

    /**
     * 更新方法实体填充
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {

    }
}
