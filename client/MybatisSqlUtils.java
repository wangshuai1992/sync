/*
 * Copyright (c) 2001-2017 GuaHao.com Corporation Limited. All rights reserved. 
 * This software is the confidential and proprietary information of GuaHao Company. 
 * ("Confidential Information"). 
 * You shall not disclose such Confidential Information and shall use it only 
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.wedoctor.health.card.cloud.sync.client.utils;

import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSession;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * TODO
 *
 * @author wangshuai
 * @version V1.0
 * @since 2017-09-08 15:24
 */
@Component
public class MybatisSqlUtils {

    @Resource
    private SqlSessionFactory sessionFactory;

    /**
     * 获取sql操作类型
     * @param id
     * @return
     */
    public String getSqlType(String id) {
        MappedStatement ms = sessionFactory.getConfiguration().getMappedStatement(id);

        return ms.getSqlCommandType().toString();
    }

    /**
     * 获取表名（INSERT UPDATE DELETE）
     * @param id
     * @param parameterObject
     * @param sqlType
     * @return
     */
    public String getTabName(String id, Object parameterObject, String sqlType) {
        MappedStatement ms = sessionFactory.getConfiguration().getMappedStatement(id);

        parameterObject = wrapCollection(parameterObject);
        String sql = ms.getBoundSql(parameterObject).getSql().toUpperCase();

        String[] arr = sql.split("\\s");
        int index = -1;
        if("INSERT".equalsIgnoreCase(sqlType)) {
            for(int i=0; i<arr.length; i++) {
                if("INTO".equals(arr[i])) {
                    index = i;
                }
            }
        }
        if("UPDATE".equalsIgnoreCase(sqlType)) {
            for(int i=0; i<arr.length; i++) {
                if("UPDATE".equals(arr[i])) {
                    index = i;
                }
            }
        }
        if("DELETE".equalsIgnoreCase(sqlType)) {
            for(int i=0; i<arr.length; i++) {
                if("FROM".equals(arr[i])) {
                    index = i;
                }
            }
        }
        if(index == -1) {
            throw new RuntimeException("未找到表名");
        }
        return arr[index+1].toLowerCase();
    }

    /**
     * mybatis mapper入参对集合参数的包装
     * @param object
     * @return
     */
    private Object wrapCollection(final Object object) {
        if (object instanceof Collection) {
            DefaultSqlSession.StrictMap<Object> map = new DefaultSqlSession.StrictMap<Object>();
            map.put("collection", object);
            if (object instanceof List) {
                map.put("list", object);
            }
            return map;
        } else if (object != null && object.getClass().isArray()) {
            DefaultSqlSession.StrictMap<Object> map = new DefaultSqlSession.StrictMap<Object>();
            map.put("array", object);
            return map;
        }
        return object;
    }

}
