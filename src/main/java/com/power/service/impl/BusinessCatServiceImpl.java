package com.power.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.power.domain.po.BusinessCat;
import com.power.domain.po.BusinessCat;
import com.power.service.BusinessCatService;
import com.power.mapper.BusinessCatMapper;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author moka
* @description 针对表【business_cat】的数据库操作Service实现
* @createDate 2025-03-03 11:06:42
*/
@Service
public class BusinessCatServiceImpl extends ServiceImpl<BusinessCatMapper, BusinessCat>
    implements BusinessCatService{

    @Override
    public BusinessCat getOneById(String orgId, Long id) {
        LambdaQueryWrapper<BusinessCat> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BusinessCat::getOrgId, orgId);
        queryWrapper.eq(BusinessCat::getId, id);

        return this.getOne(queryWrapper);
    }

    @Override
    public List<BusinessCat> getListByIdList(String orgId, List<Long> idList) {
        LambdaQueryWrapper<BusinessCat> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BusinessCat::getOrgId, orgId);
        queryWrapper.in(BusinessCat::getId, idList);
        queryWrapper.orderByAsc(BusinessCat::getId);
        return this.list(queryWrapper);
    }
}




