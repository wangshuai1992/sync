/*
 * Copyright (c) 2001-2017 GuaHao.com Corporation Limited. All rights reserved. 
 * This software is the confidential and proprietary information of GuaHao Company. 
 * ("Confidential Information"). 
 * You shall not disclose such Confidential Information and shall use it only 
 * in accordance with the terms of the license agreement you entered into with GuaHao.com.
 */
package com.wedoctor.health.card.cloud.sync.client.dubbo;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.ProtocolConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.alibaba.dubbo.config.ServiceConfig;
import com.wedoctor.health.card.cloud.sync.share.service.SyncDataOperateService;
import com.wedoctor.health.card.cloud.sync.share.service.SyncDataQueryService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * 各业务系统注册不同的服务实现类
 * @author wangshuai
 * @version V1.0
 * @since 2017-09-20 11:13
 */
@Component
public class DubboServiceRegister {

    @Value("${dubbo.application.name}")
    private String application;

    @Value("${dubbo.registry.address}")
    private String zkAddress;

    @Value("${dubbo.protocol.port}")
    private int dubboPort;

    /**
     * 服务实现
     */
    @Resource
    private SyncDataQueryService syncDataQueryService;

    @Resource
    private SyncDataOperateService syncDataOperateService;

    @PostConstruct
    public void regist() {

        //当前应用配置
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.setName(application);

        //连接注册中心配置
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setAddress(zkAddress);

        //服务提供者协议配置
        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("dubbo");
        protocolConfig.setPort(dubboPort);

        //服务提供者暴露服务配置
        ServiceConfig<SyncDataQueryService> dataQueryServiceConfig = new ServiceConfig<>();
        dataQueryServiceConfig.setApplication(applicationConfig);
        dataQueryServiceConfig.setRegistry(registryConfig);
        dataQueryServiceConfig.setProtocol(protocolConfig);
        dataQueryServiceConfig.setInterface(SyncDataQueryService.class);
        dataQueryServiceConfig.setRef(syncDataQueryService);
        dataQueryServiceConfig.setVersion(application);

        ServiceConfig<SyncDataOperateService> dataOperateServiceConfig = new ServiceConfig<>();
        dataOperateServiceConfig.setApplication(applicationConfig);
        dataOperateServiceConfig.setRegistry(registryConfig);
        dataOperateServiceConfig.setProtocol(protocolConfig);
        dataOperateServiceConfig.setInterface(SyncDataOperateService.class);
        dataOperateServiceConfig.setRef(syncDataOperateService);
        dataOperateServiceConfig.setVersion(application);

        //暴露及注册服务
        dataQueryServiceConfig.export();
        dataOperateServiceConfig.export();
    }
}
