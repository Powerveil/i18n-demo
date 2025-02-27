package com.power.service.impl;

import com.power.mapper.I18nMapper;
import com.power.service.I18nService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class I18nServiceImpl implements I18nService {

    @Autowired
    private I18nMapper i18nMapper;

    @Override
    // todo 添加缓存优化，建议远程缓存，不要使用本地缓存
    public String getContent(String tableName, String orgId, Long bizId, Integer type, String locale, int disabled) {
        return i18nMapper.queryItemContent(tableName, orgId, bizId, type, locale, disabled);
    }
}
