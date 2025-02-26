package com.power.controller;


import com.power.annotation.I18n;
import com.power.domain.dto.I18Test1Dto;
import com.power.domain.dto.I18Test2Dto;
import com.power.domain.vo.Test1Vo;
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
@RequestMapping("/i18n")
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

    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
