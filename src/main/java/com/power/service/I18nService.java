package com.power.service;

public interface I18nService {
    public String getContent(String tableName, String orgId, Long bizId, Integer type, String locale, int disabled);
}
