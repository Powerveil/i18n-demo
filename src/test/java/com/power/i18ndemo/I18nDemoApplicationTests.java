package com.power.i18ndemo;

import com.power.service.BusinessBaseService;
import com.power.service.InternationalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class I18nDemoApplicationTests {

    @Autowired
    private InternationalService internationalService;

    @Autowired
    private BusinessBaseService businessBaseService;

    @Test
    void contextLoads() {
//        System.out.println(internationalService.list());
        System.out.println(businessBaseService.list());
    }

}
