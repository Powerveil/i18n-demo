package com.power.service;

import com.power.domain.po.BusinessBase;
import com.power.domain.po.BusinessCat;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author moka
* @description 针对表【business_cat】的数据库操作Service
* @createDate 2025-03-03 11:06:42
*/
public interface BusinessCatService extends IService<BusinessCat> {

    public BusinessCat getOneById(String orgId, Long id);

    public List<BusinessCat> getListByIdList(String orgId, List<Long> idList);

}
