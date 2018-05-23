package com.wangshuai.health.card.cloud.monitor.dal.dubbo.query;

import com.wangshuai.health.card.cloud.common.query.PageQuery;

/**
 * 
 * @author wangshuai
 * @date 2017年7月25日
 * @param <T>
 */
public class DubboApplicationQuery<T> extends PageQuery<T> {

	private static final long serialVersionUID = -1879822009002756941L;

	/**
	 * 应用名称
	 */
	private String appName;
	
	/**
	 * 应用中文名
	 */
	private String appCnName;
	
	/**
	 * 应用类型
	 */
	private String appType;
	
	/**
	 * 是否删除
	 */
	private Integer isDeleted;

	/**
	 * @return the isDeleted
	 */
	public Integer getIsDeleted() {
		return isDeleted;
	}

	/**
	 * @param isDeleted the isDeleted to set
	 */
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppCnName() {
		return appCnName;
	}

	public void setAppCnName(String appCnName) {
		this.appCnName = appCnName;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}
	
}
