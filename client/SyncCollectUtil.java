package com.wangshuai.health.card.cloud.sync.client.utils;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wangshuai.health.card.cloud.common.dataobject.BaseDO;
import com.wangshuai.health.card.cloud.common.mapper.SyncableMapper;
import com.wangshuai.health.card.cloud.sync.share.dto.CloudSyncDTO;
import com.wangshuai.health.card.cloud.sync.share.service.CollectSyncDataService;
import io.codis.jodis.CodisAgent;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * TODO
 *
 * @author wangshuai
 * @version V1.0
 * @since 2017-09-12 10:49
 */
@Component
public class SyncCollectUtil {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SyncCollectUtil.class);

    @Resource
    private MybatisSqlUtils mybatisSqlUtils;

    @Resource
    private CodisAgent codisAgent;

    @Value("${dubbo.application.name}")
    private String application;

    @Reference
    private CollectSyncDataService collectSyncDataService;

    public Object doTransaction(ProceedingJoinPoint pjp) {
        Object result;

        String key = application + Thread.currentThread().getName();

        try {
            result = pjp.proceed(); //continue on the intercepted method
        } catch (Throwable t) {
            LOGGER.error("manager执行出错", t);
            codisAgent.del(key);
            return null;
        }

        //manager执行成功，提交缓存中记录的cloudSyncDTO
        try {
            List<String> list = codisAgent.lrange(key, 0, -1);
            list.forEach((str) -> {
                CloudSyncDTO cloudSyncDTO = ((JSONObject)JSON.parse(str)).toJavaObject(CloudSyncDTO.class);
                collectSyncDataService.collectSyncData(cloudSyncDTO);
            });
        } finally {
            codisAgent.del(key);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public void doCollect(JoinPoint point) {
        Class mapperType = point.getSignature().getDeclaringType();

        if(mapperType.equals(SyncableMapper.class)) {
            //如果执行的是SyncableMapper接口定义的方法 则不执行切面逻辑（只对实际的业务操作进行拦截）
            System.out.println("执行的是SyncableMapper接口定义的方法 不执行切面逻辑");
            return;
        }

        Class[] classes = point.getSignature().getDeclaringType().getInterfaces();
        boolean flag = Arrays.stream(classes).anyMatch((c) -> c.equals(SyncableMapper.class));
        if(!flag) {
            //如果被拦截的mapper没有实现SyncableMapper接口，则不对其进行同步操作
            System.out.println("被拦截的mapper没有实现SyncableMapper接口，不对其进行同步操作");
            return;
        }

        String fullMethodSignature = point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
        String sqlType = mybatisSqlUtils.getSqlType(fullMethodSignature);

        if("SELECT".equalsIgnoreCase(sqlType)) {
            return;
        }

        Object[] objs = point.getArgs();
        if(objs.length != 1 || objs[0] == null) {
            return;
        }

        Object obj = objs[0];
        String tabName = mybatisSqlUtils.getTabName(fullMethodSignature, obj, sqlType);

        Map<Long, String> valueMap = new HashMap<>();
        if("INSERT".equalsIgnoreCase(sqlType)) {
            //新增
            if(obj instanceof BaseDO) {
                BaseDO baseDO = (BaseDO)obj;
                valueMap.put(baseDO.getId(), "I");
            }
            //批量新增
            if(obj instanceof List) {
                List<BaseDO> list = (List<BaseDO>)obj;
                list.forEach((baseDO) -> valueMap.put(baseDO.getId(), "I"));
            }
        }

        if("UPDATE".equalsIgnoreCase(sqlType)) {
            //更新信息
            if(obj instanceof BaseDO) {
                BaseDO baseDO = (BaseDO)obj;
                valueMap.put(baseDO.getId(), "U");
            }
            //逻辑删除
            if(obj instanceof Long) {
                Long id = (Long)obj;
                valueMap.put(id, "U");
            }
            if(obj instanceof List) {
                List<Long> list = (List<Long>)obj;
                list.forEach((id) -> valueMap.put(id, "U"));
            }
        }

        if("DELETE".equalsIgnoreCase(sqlType)) {
            if(obj instanceof Long) {
                Long id = (Long)obj;
                valueMap.put(id, "D");
            }
            if(obj instanceof List) {
                List<Long> list = (List<Long>)obj;
                list.forEach((id) -> valueMap.put(id, "D"));
            }
        }

        writeOperateDataToCache(valueMap, fullMethodSignature, tabName);
    }

    private void writeOperateDataToCache(Map<Long, String> map, String fullMethodSignature, String tabname) {
        String key = application + Thread.currentThread().getName();

        map.forEach((id, opType) -> {
            CloudSyncDTO cloudSyncDTO = new CloudSyncDTO();
            cloudSyncDTO.setSyncFlag(0);
            cloudSyncDTO.setSyncOperateApp(application);
            cloudSyncDTO.setSyncOperateId(id);
            cloudSyncDTO.setSyncOperateType(opType);
            cloudSyncDTO.setSyncOperateName(fullMethodSignature);
            cloudSyncDTO.setSyncTableName(tabname);

            String jsonStr = JSON.toJSONString(cloudSyncDTO);
            codisAgent.lpush(key, jsonStr);
        });
    }

}
