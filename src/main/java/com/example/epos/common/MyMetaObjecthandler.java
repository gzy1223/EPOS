package com.example.epos.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class MyMetaObjecthandler implements MetaObjectHandler {
    /**
      * @Author: GZY
      * @Description: automatically insert the string into the database when creating the data
      * @Date: 25/01/2023
      * @Param metaObject:
      * @return: void
      **/

    @Override
    public void insertFill(MetaObject metaObject)
    {
        log.info("automatic insert");
        log.info(metaObject.toString());
        metaObject.setValue("createTime", LocalDateTime.now());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createUser", BaseContext.getCurrentId());
        metaObject.setValue("updateUser",   BaseContext.getCurrentId() );
    }
    /**
      * @Author: GZY
      * @Description: automatically insert the string when updating the data
      * @Date: 25/01/2023
      * @Param metaObject:
      * @return: void
      **/

    @Override
    public void updateFill(MetaObject metaObject)
    {
        log.info("automatatic update");
        log.info(metaObject.toString());
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getCurrentId() );
    }

}
