package com.power.manager;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.power.common.MultiResult;
import com.power.domain.dto.*;
import com.power.domain.po.BusinessBase;
import com.power.domain.po.BusinessCat;
import com.power.domain.vo.*;
import com.power.service.BusinessBaseService;
import com.power.service.BusinessCatService;
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

    @Autowired
    private BusinessCatService businessCatService;

    public Test1Vo test1(I18Test1Dto i18Test1Dto) {
        LambdaQueryWrapper<BusinessBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BusinessBase::getId, i18Test1Dto.getId());
        queryWrapper.eq(BusinessBase::getOrgId, i18Test1Dto.getOrgId());
        BusinessBase businessBase = businessBaseService.getOne(queryWrapper);

        Test1Vo test1Vo = BeanUtil.copyProperties(businessBase, Test1Vo.class);
        test1Vo.setMyId(i18Test1Dto.getId());

        return test1Vo;
    }

    public List<Test1Vo> test2(I18Test2Dto i18Test2Dto) {
        LambdaQueryWrapper<BusinessBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BusinessBase::getId, i18Test2Dto.getIdList());
        queryWrapper.eq(BusinessBase::getOrgId, i18Test2Dto.getOrgId());
        List<BusinessBase> businessBaseList = businessBaseService.list(queryWrapper);

        List<Test1Vo> test1VoList = CollUtil.newArrayList();

        for (BusinessBase businessBase : businessBaseList) {
            Test1Vo test1Vo = BeanUtil.copyProperties(businessBase, Test1Vo.class);
            test1Vo.setMyId(businessBase.getId());

            test1VoList.add(test1Vo);
        }

        return test1VoList;
    }

    public Test1Vo test3(I18Test3Dto i18Test3Dto) {
        LambdaQueryWrapper<BusinessBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BusinessBase::getId, i18Test3Dto.getId());
        queryWrapper.eq(BusinessBase::getOrgId, i18Test3Dto.getOrgIddsadsa());
        BusinessBase businessBase = businessBaseService.getOne(queryWrapper);

        Test1Vo test1Vo = BeanUtil.copyProperties(businessBase, Test1Vo.class);
        test1Vo.setMyId(i18Test3Dto.getId());

        return test1Vo;
    }

    public List<Test1Vo> test4(I18Test4Dto i18Test4Dto) {
        LambdaQueryWrapper<BusinessBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BusinessBase::getId, i18Test4Dto.getIdList());
        queryWrapper.eq(BusinessBase::getOrgId, i18Test4Dto.getOrgIdIkun());
        List<BusinessBase> businessBaseList = businessBaseService.list(queryWrapper);

        List<Test1Vo> test1VoList = CollUtil.newArrayList();

        for (BusinessBase businessBase : businessBaseList) {
            Test1Vo test1Vo = BeanUtil.copyProperties(businessBase, Test1Vo.class);
            test1Vo.setMyId(businessBase.getId());

            test1VoList.add(test1Vo);
        }

        return test1VoList;
    }

    public List<Test2Vo> test6(I18Test4Dto i18Test4Dto) {
        LambdaQueryWrapper<BusinessBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BusinessBase::getId, i18Test4Dto.getIdList());
        queryWrapper.eq(BusinessBase::getOrgId, i18Test4Dto.getOrgIdIkun());
        List<BusinessBase> businessBaseList = businessBaseService.list(queryWrapper);

        List<Test2Vo> test2VoList = CollUtil.newArrayList();

        for (BusinessBase businessBase : businessBaseList) {
            Test2Vo test2Vo = BeanUtil.copyProperties(businessBase, Test2Vo.class);
            test2Vo.setMyId(businessBase.getId());

            test2VoList.add(test2Vo);
        }

        return test2VoList;
    }

    public MultiResult<Test2Vo> test8(I18Test8Dto i18Test8Dto) {
        int currentPage = i18Test8Dto.getCurrentPage();
        int pageSize = i18Test8Dto.getPageSize();
        Page<BusinessBase> page = new Page<>(currentPage, pageSize);

        LambdaQueryWrapper<BusinessBase> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BusinessBase::getId, i18Test8Dto.getIdList());
        queryWrapper.eq(BusinessBase::getOrgId, i18Test8Dto.getOrgIdIkun());

        Page<BusinessBase> userPage = businessBaseService.page(page, queryWrapper);

        List<BusinessBase> businessBaseList = userPage.getRecords();


        List<Test2Vo> test2VoList = CollUtil.newArrayList();

        for (BusinessBase businessBase : businessBaseList) {
            Test2Vo test2Vo = BeanUtil.copyProperties(businessBase, Test2Vo.class);
            test2Vo.setMyId(businessBase.getId());

            test2VoList.add(test2Vo);
        }

//        PageResponse.of(userPage.getRecords(), (int) userPage.getTotal(), pageSize, currentPage);

        return MultiResult.successMulti(test2VoList, userPage.getTotal(), currentPage, pageSize);
    }

    public Test3Vo test9(I18Test9Dto i18Test9Dto) {
        String orgId = i18Test9Dto.getOrgIdIkun();
        Long bbId = i18Test9Dto.getBbId();
        Long bcId = i18Test9Dto.getBcId();

        BusinessBase businessBase = businessBaseService.getOneById(orgId, bbId);
        BusinessCat businessCat = businessCatService.getOneById(orgId, bcId);


        BusinessBaseVo businessBaseVo = new BusinessBaseVo();
        businessBaseVo.setBbId(bbId);
        businessBaseVo.setDescription(businessBase.getDescription());

        BusinessCatVo businessCatVo = new BusinessCatVo();
        businessCatVo.setBcId(bcId);
        businessCatVo.setDescription(businessCat.getDescription());


        Test3Vo test3Vo = new Test3Vo();

        test3Vo.setBusinessBaseVo(businessBaseVo);
        test3Vo.setBusinessCatVo(businessCatVo);

        return test3Vo;
    }
}
