package com.power.i18ndemo;

import com.power.domain.po.International;
import com.power.enums.I18nTableEnums;
import com.power.mapper.I18nMapper;
import com.power.mapper.InternationalMapper;
import com.power.service.BusinessBaseService;
import com.power.service.InternationalService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class I18nDemoApplicationTests {

    @Autowired
    private InternationalService internationalService;

    @Autowired
    private BusinessBaseService businessBaseService;

    @Autowired
    private InternationalMapper internationalMapper;

    @Autowired
    private I18nMapper i18nMapper;

    @Test
    void contextLoads() {
//        System.out.println(internationalService.list());
//        System.out.println(businessBaseService.list());


        for (International international : internationalService.list()) {
            System.out.println(international);
        }
    }


    @Test
    void test01() {
//        International international = internationalMapper.selectById(1L);
//        System.out.println(international);
        String content = i18nMapper.queryItemContent(I18nTableEnums.INTERNATIONAL.getTableName(), "ORG_01", 1001L, 1, "en-US", 0);

        System.out.println("==================================");
        System.out.println(content);

//        List<String> zhCn = internationalMapper.selectAllByLocale("ORG_01");
//        for (String s : zhCn) {
//            System.out.println(s);
//        }
    }

}
