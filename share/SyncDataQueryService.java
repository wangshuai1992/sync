package com.wangshuai.health.card.cloud.sync.share.service;

import com.wangshuai.health.card.cloud.common.dto.ResponseDTO;

/**
 * 同步系统查询各业务系统数据服务
 *
 * @author wangshuai
 * @version V1.0
 * @since 2017-09-14 15:32
 */
public interface SyncDataQueryService {

    /**
     * 查询数据接口
     *
     * @param tabName
     * @param id
     * @return
     */
    ResponseDTO<String> querySyncableData(Long id, String tabName);

}
