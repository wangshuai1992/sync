package com.wangshuai.health.card.cloud.sync.share.service;

import java.util.Map;

import com.wangshuai.health.card.cloud.common.dto.ResponseDTO;

/**
 * @Type SyncOperateService
 * @Desc 同步系统操作各业务系统数据服务
 * @author liuhj 
 * @created 2017年9月20日 下午1:45:55
 * @version 1.0.0
 */
public interface SyncDataOperateService {
    
    /**
     * 根据操作类型进行同步方法操作接口
     *
     * @param id
     * @param relationId 
     * @param tabName
     * @param params
     * @param operateType
     * @return
     */
    ResponseDTO<String> operateSyncableData(Long id, Long relationId, String tabName, Map<String, Object> params, String operateType);

}
