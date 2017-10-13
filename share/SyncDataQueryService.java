/*
 * Copyright (c) 2001-2017 GuaHao.com Corporation Limited. All rights reserved. 
 * This software is the confidential and proprietary information of GuaHao Company. 
 * ("Confidential Information"). 
 * You shall not disclose such Confidential Information and shall use it only 
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.wedoctor.health.card.cloud.sync.share.service;

import com.wedoctor.health.card.cloud.common.dto.ResponseDTO;

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
