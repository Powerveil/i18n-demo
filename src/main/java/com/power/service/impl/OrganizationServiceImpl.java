package com.power.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.power.domain.po.Organization;
import com.power.service.OrganizationService;
import com.power.mapper.OrganizationMapper;
import org.springframework.stereotype.Service;


/**
* @author moka
* @description 针对表【organization】的数据库操作Service实现
* @createDate 2025-02-28 10:43:50
*/
@Service
public class OrganizationServiceImpl extends ServiceImpl<OrganizationMapper, Organization>
    implements OrganizationService {

    @Override
    public String getMainLocaleByOrgId(String orgId) {
        return baseMapper.getMainLocaleByOrgId(orgId);
    }

}




