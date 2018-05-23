package com.wangshuai.health.card.cloud.sync.client.aspect;

import com.wangshuai.health.card.cloud.sync.client.utils.SyncCollectUtil;
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

    @Around("execution(* com.wangshuai.health.card.cloud.*.manager.*.impl.*.*(..)) || " +
            "execution(* com.wangshuai.health.card.cloud.*.biz.*.*.impl.*.*(..))")
    public Object aroundManager(ProceedingJoinPoint pjp) {
        return syncCollectUtil.doTransaction(pjp);
    }

    @After("execution(* com.wangshuai.health.card.cloud.*.dal.*.mapper.*.*(..)) || " +
            "execution(* com.wangshuai.health.card.cloud.*.biz.dal.*.mapper.*.*(..))")
    public void afterMapper(JoinPoint point) {
        syncCollectUtil.doCollect(point);
    }

}
