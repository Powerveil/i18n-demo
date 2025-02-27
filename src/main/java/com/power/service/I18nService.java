package com.power.service;

import java.util.List;
import java.util.Map;

public interface I18nService {
    public String getContent(String tableName, String orgId, Long bizId, Integer type, String locale, int disabled);

    /**
     *
     * @param tableName
     * @param orgId
     * @param bizIdList
     * @param type
     * @param locale
     * @param disabled
     * @return key:CONCAT(org_id, biz_id, type, locale, disabled) value:content
     */
    public Map<String, String> getContent(String tableName, String orgId, List<Long> bizIdList, Integer type, String locale, int disabled);
}
