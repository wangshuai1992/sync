package com.wedoctor.health.card.cloud.common.mapper;/*
 * Copyright (c) 2001-2017 GuaHao.com Corporation Limited. All rights reserved. 
 * This software is the confidential and proprietary information of GuaHao Company. 
 * ("Confidential Information"). 
 * You shall not disclose such Confidential Information and shall use it only 
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */

import java.util.Map;

/**
 * TODO
 *
 * @author wangshuai
 * @version V1.0
 * @since 2017-09-14 16:31
 */
public interface SyncableMapper {

    Map findSyncableDataById(Long id);

    int insertSyncableData(Map paramMap);

    int updateSyncableData(Map paramMap);

    int deleteSyncableDataById(Long id);

}
