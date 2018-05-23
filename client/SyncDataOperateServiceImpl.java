package com.wangshuai.health.card.cloud.sync.client.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Reference;
import com.wangshuai.health.card.cloud.common.dto.ResponseDTO;
import com.wangshuai.health.card.cloud.common.enums.ExceptionEnum;
import com.wangshuai.health.card.cloud.common.mapper.SyncableMapper;
import com.wangshuai.health.card.cloud.common.utils.SpringUtil;
import com.wangshuai.health.card.cloud.config.share.dto.MapperConfigDTO;
import com.wangshuai.health.card.cloud.config.share.service.MapperConfigService;
import com.wangshuai.health.card.cloud.sync.share.service.SyncDataOperateService;

/**
 * @Type SyncOperateServiceImpl
 * @Desc 同步系统操作各业务系统数据服务
 * @author liuhj
 * @created 2017年9月20日 下午1:53:26
 * @version 1.0.0
 */
@Component
public class SyncDataOperateServiceImpl implements SyncDataOperateService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SyncDataOperateServiceImpl.class);

    @Value("${dubbo.application.name}")
    private static String application;

    /**
     * 配置系统服务
     */
    @Reference
    private MapperConfigService mapperConfigService;

    @Override
    public ResponseDTO<String> operateSyncableData(Long id, Long relationId, String tabName, Map<String, Object> params,
            String operateType) {
        ResponseDTO<String> responseDTO = new ResponseDTO<>(ExceptionEnum.SUCCESS);
        try {
            MapperConfigDTO mapperConfigDTO = mapperConfigService.findByAppAndTabname(application, tabName).getData();
            if (null == mapperConfigDTO) {
                LOGGER.error("SyncDataOperateServiceImpl.operateSyncableData ->获取表名对应mapper失败");
                responseDTO.setDataMessage(ExceptionEnum.ERROR.getCode(), "获取表名对应mapper失败");
                return responseDTO;
            }
            String mapperClass = mapperConfigDTO.getMapperClass();
            SyncableMapper mapper = (SyncableMapper) SpringUtil.getBean(Class.forName(mapperClass));
            if (null == mapper) {
                LOGGER.error("SyncDataOperateServiceImpl.operateSyncableData ->找不到对应的mapper：{}", mapperClass);
                responseDTO.setDataMessage(ExceptionEnum.ERROR.getCode(), "找不到对应的mapper：" + mapperClass);
                return responseDTO;
            }
            int count = 0;
            Long callBackId = null;
            if ("I".equalsIgnoreCase(operateType)) {
                count = mapper.insertSyncableData(params);
                callBackId = (long) (int) params.get("id");
            }
            if ("U".equalsIgnoreCase(operateType)) {
                params.put("id", relationId);
                count = mapper.updateSyncableData(params);
                callBackId = null;
            }
            if ("D".equalsIgnoreCase(operateType)) {
                count = mapper.deleteSyncableDataById(relationId);
                callBackId = null;
            }
            if (count < 0) {
                LOGGER.error("SyncDataOperateServiceImpl.operateSyncableData ->同步数据失败！");
                responseDTO.setDataMessage(ExceptionEnum.ERROR.getCode(), "同步数据失败！");
                return responseDTO;
            }
            responseDTO.setData(String.valueOf(callBackId));
        } catch (Exception e) {
            LOGGER.error("SyncDataOperateServiceImpl.operateSyncableData ->加载mapper类失败", e);
            responseDTO.setDataMessage(ExceptionEnum.ERROR.getCode(), "加载mapper类失败");
            return responseDTO;
        }
        return responseDTO;
    }
}
