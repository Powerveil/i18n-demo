package com.power.manager;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.power.domain.dto.I18Test1Dto;
import com.power.domain.po.BusinessBase;
import com.power.domain.vo.Test1Vo;
import com.power.service.BusinessBaseService;
import com.power.service.InternationalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName I18nManager
 * @Description TODO
 * @Author power
 * @Date 2025/2/26 1:37
 * @Version 1.0
 */
@Component
public class I18nManager {

    @Autowired
    private BusinessBaseService businessBaseService;

    public Test1Vo test1(I18Test1Dto i18Test1Dto) {
        LambdaQueryWrapper<BusinessBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BusinessBase::getId, 1);
        queryWrapper.eq(BusinessBase::getOrgId, "ORG_01");
        BusinessBase one = businessBaseService.getOne(queryWrapper);

        Test1Vo test1Vo = BeanUtil.copyProperties(one, Test1Vo.class);

        return test1Vo;
    }
}
