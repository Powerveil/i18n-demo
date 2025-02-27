package com.power.service.impl;

import com.power.mapper.I18nMapper;
import com.power.service.I18nService;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class I18nServiceImpl implements I18nService {

    @Autowired
    private I18nMapper i18nMapper;

    private static final String split = "---";

    @Override
    // todo 添加缓存优化，建议远程缓存，不要使用本地缓存
    public String getContent(String tableName, String orgId, Long bizId, Integer type, String locale, int disabled) {
        return i18nMapper.queryItemContent(tableName, orgId, bizId, type, locale, disabled);
    }

    @Override
    public Map<String, String> getContent(String tableName, String orgId, List<Long> bizIdList, Integer type, String locale, int disabled) {
        List<String> strings = i18nMapper.queryItemListContent(tableName, orgId, bizIdList, type, locale, disabled, split);

        Map<String, String> collect = strings.stream()
                .collect(Collectors.toMap(item -> item.split(split)[0], item -> item.split(split)[1]));
        return collect;
    }

}
