package com.power.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.power.domain.po.BusinessBase;
import com.power.service.BusinessBaseService;
import com.power.mapper.BusinessBaseMapper;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
* @author power
* @description 针对表【business_base】的数据库操作Service实现
* @createDate 2025-02-26 01:02:07
*/
@Service
public class BusinessBaseServiceImpl extends ServiceImpl<BusinessBaseMapper, BusinessBase>
    implements BusinessBaseService{

    @Override
    public BusinessBase getOneById(String orgId, Long id) {
        LambdaQueryWrapper<BusinessBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BusinessBase::getOrgId, orgId);
        queryWrapper.eq(BusinessBase::getId, id);

        return this.getOne(queryWrapper);
    }

    @Override
    public List<BusinessBase> getListByIdList(String orgId, List<Long> idList) {
        LambdaQueryWrapper<BusinessBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BusinessBase::getOrgId, orgId);
        queryWrapper.in(BusinessBase::getId, idList);
        queryWrapper.orderByAsc(BusinessBase::getId);
        return this.list(queryWrapper);
    }
}




