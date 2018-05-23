package com.wangshuai.health.card.cloud.common.mapper;

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
