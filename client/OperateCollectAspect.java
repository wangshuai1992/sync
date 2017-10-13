/*
 * Copyright (c) 2001-2017 GuaHao.com Corporation Limited. All rights reserved. 
 * This software is the confidential and proprietary information of GuaHao Company. 
 * ("Confidential Information"). 
 * You shall not disclose such Confidential Information and shall use it only 
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.wedoctor.health.card.cloud.sync.client.aspect;

import com.wedoctor.health.card.cloud.sync.client.utils.SyncCollectUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * TODO
 *
 * @author wangshuai
 * @version V1.0
 * @since 2017-09-08 13:48
 */
@Aspect
@Component
public class OperateCollectAspect {

    @Resource
    private SyncCollectUtil syncCollectUtil;

    @Around("execution(* com.wedoctor.health.card.cloud.*.manager.*.impl.*.*(..)) || " +
            "execution(* com.wedoctor.health.card.cloud.*.biz.*.*.impl.*.*(..))")
    public Object aroundManager(ProceedingJoinPoint pjp) {
        return syncCollectUtil.doTransaction(pjp);
    }

    @After("execution(* com.wedoctor.health.card.cloud.*.dal.*.mapper.*.*(..)) || " +
            "execution(* com.wedoctor.health.card.cloud.*.biz.dal.*.mapper.*.*(..))")
    public void afterMapper(JoinPoint point) {
        syncCollectUtil.doCollect(point);
    }

}
