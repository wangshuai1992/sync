package com.wangshuai.health.card.cloud.sync.client.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.wangshuai.health.card.cloud.common.dto.ResponseDTO;
import com.wangshuai.health.card.cloud.common.enums.ExceptionEnum;
import com.wangshuai.health.card.cloud.common.mapper.SyncableMapper;
import com.wangshuai.health.card.cloud.common.utils.SpringUtil;
import com.wangshuai.health.card.cloud.config.share.dto.MapperConfigDTO;
import com.wangshuai.health.card.cloud.config.share.service.MapperConfigService;
import com.wangshuai.health.card.cloud.sync.share.service.SyncDataQueryService;

/**
 * TODO
 *
 * @author wangshuai
 * @version V1.0
 * @since 2017-09-14 13:53
 */
@Component
public class SyncDataQueryServiceImpl implements SyncDataQueryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncDataQueryServiceImpl.class);
    
    @Value("${dubbo.application.name}")
    private static String application;

    @Reference
    private MapperConfigService mapperConfigService;

    public ResponseDTO<String> querySyncableData(Long id, String tabName) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>(ExceptionEnum.SUCCESS);
        try {
            MapperConfigDTO mapperConfigDTO = mapperConfigService.findByAppAndTabname(application, tabName).getData();
            if(null == mapperConfigDTO) {
                LOGGER.error("获取表名对应mapper失败");
                responseDTO.setDataMessage(ExceptionEnum.ERROR.getCode(), "获取表名对应mapper失败");
                return responseDTO;
            }
            
            String mapperClass = mapperConfigDTO.getMapperClass();
            SyncableMapper mapper = (SyncableMapper) SpringUtil.getBean(Class.forName(mapperClass));
            if(null == mapper) {
                LOGGER.error("找不到对应的mapper：{}", mapperClass);
                responseDTO.setDataMessage(ExceptionEnum.ERROR.getCode(), "找不到对应的mapper：" + mapperClass);
                return responseDTO;
            }

            Map resultMap = mapper.findSyncableDataById(id);
            if (resultMap == null) {
                LOGGER.error("查询数据失败");
                responseDTO.setDataMessage(ExceptionEnum.ERROR.getCode(), "查询数据失败");
                return responseDTO;
            }
            responseDTO.setData(JSON.toJSONString(resultMap));
        } catch (ClassNotFoundException e) {
            LOGGER.error("加载mapper类失败", e);
            responseDTO.setDataMessage(ExceptionEnum.ERROR.getCode(), "加载mapper类失败");
            return responseDTO;
        }
        return responseDTO;
    }
}
