package com.power.service;

import com.power.domain.po.Organization;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author moka
* @description 针对表【organization】的数据库操作Service
* @createDate 2025-02-28 10:43:50
*/
public interface OrganizationService extends IService<Organization> {

    String getMainLocaleByOrgId(String orgId);
}
