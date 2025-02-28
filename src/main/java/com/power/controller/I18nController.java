package com.power.controller;


import com.power.annotation.I18n;
import com.power.common.MultiResult;
import com.power.common.Result;
import com.power.domain.dto.*;
import com.power.domain.vo.Test1Vo;
import com.power.domain.vo.Test2Vo;
import com.power.enums.InterfaceTypeEnums;
import com.power.manager.I18nManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * @ClassName I18nController
 * @Description TODO
 * @Author power
 * @Date 2025/2/26 1:36
 * @Version 1.0
 */
@RestController
@RequestMapping("/i18n-test")
public class I18nController {


    @Autowired
    private I18nManager i18nManager;


    /**
     * 单个、简单 国际化
     * @param i18Test1Dto
     * @return
     */
    @RequestMapping("/test1")
    @I18n()
    public Test1Vo test1(@RequestBody I18Test1Dto i18Test1Dto) {
        return i18nManager.test1(i18Test1Dto);
    }

    /**
     * 多个、简单 国际化
     * @param i18Test2Dto
     * @return
     */
    @RequestMapping("/test2")
    @I18n()
    public List<Test1Vo> test2(@RequestBody I18Test2Dto i18Test2Dto) {
        return i18nManager.test2(i18Test2Dto);
    }

    /**
     * 测试单个不标准 多个、简单 国际化
     * @param i18Test3Dto
     * @return
     */
    @RequestMapping("/test3")
    @I18n(orgIdField = "orgIddsadsa", localeField = "localdsadsae")
    public Test1Vo test3(@RequestBody I18Test3Dto i18Test3Dto) {
        return i18nManager.test3(i18Test3Dto);
    }

    /**
     * 测试多个不标准 多个、简单 国际化
     * @param i18Test4Dto
     * @return
     */
    @RequestMapping("/test4")
    @I18n(orgIdField = "orgIdIkun", localeField = "localeIkun")
    public List<Test1Vo> test4(@RequestBody I18Test4Dto i18Test4Dto) {
        return i18nManager.test4(i18Test4Dto);
    }


    /**
     * 测试多个不标准 多个、简单（适配自定义标准返回类） 国际化
     * @param i18Test4Dto
     * @return
     */
    @RequestMapping("/test5")
    @I18n(orgIdField = "orgIdIkun", localeField = "localeIkun")
    public Result<List<Test1Vo>> test5(@RequestBody I18Test4Dto i18Test4Dto) {
        return Result.success(i18nManager.test4(i18Test4Dto));
    }

    /**
     * 测试多个不标准 多个、简单（适配自定义标准返回类） 国际化
     * @param i18Test4Dto
     * @return
     */
    @RequestMapping("/test6")
    @I18n(orgIdField = "orgIdIkun", localeField = "localeIkun")
    public Result<List<Test2Vo>> test6(@RequestBody I18Test4Dto i18Test4Dto) {
        return Result.success(i18nManager.test6(i18Test4Dto));
    }

    /**
     * 单个、简单 国际化、兜底方案
     * @param i18Test1Dto
     * @return
     */
    @RequestMapping("/test7")
    @I18n(interfaceType = InterfaceTypeEnums.INNER)
    public Test1Vo test7(@RequestBody I18Test1Dto i18Test1Dto) {
        return i18nManager.test1(i18Test1Dto);
    }

    /**
     * 测试多个不标准 多个、简单（适配自定义标准返回类）分页查询 国际化
     * @param i18Test8Dto
     * @return
     */
    @RequestMapping("/test8")
    @I18n(orgIdField = "orgIdIkun", localeField = "localeIkun")
    public MultiResult<Test2Vo> test8(@RequestBody I18Test8Dto i18Test8Dto) {
        return i18nManager.test8(i18Test8Dto);
    }




    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
