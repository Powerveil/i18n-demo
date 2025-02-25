package com.power.controller;


import com.power.domain.dto.I18Test1Dto;
import com.power.domain.vo.Test1Vo;
import com.power.manager.I18nManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @RequestMapping("/test1")
    public Test1Vo test1(I18Test1Dto i18Test1Dto) {
        return i18nManager.test1(i18Test1Dto);
    }

    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
