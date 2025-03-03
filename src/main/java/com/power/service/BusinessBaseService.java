package com.power.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.power.domain.po.BusinessBase;

import java.util.List;

/**
* @author power
* @description 针对表【business_base】的数据库操作Service
* @createDate 2025-02-26 01:02:07
*/
public interface BusinessBaseService extends IService<BusinessBase> {
    public BusinessBase getOneById(String orgId, Long id);

    public List<BusinessBase> getListByIdList(String orgId, List<Long> idList);
}
