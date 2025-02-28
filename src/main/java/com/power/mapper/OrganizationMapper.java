package com.power.mapper;

import com.power.domain.po.Organization;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
* @author moka
* @description 针对表【organization】的数据库操作Mapper
* @createDate 2025-02-28 10:43:50
* @Entity com.power.domain.po.Organization
*/
public interface OrganizationMapper extends BaseMapper<Organization> {

    String getMainLocaleByOrgId(@Param("orgId") String orgId);
}




