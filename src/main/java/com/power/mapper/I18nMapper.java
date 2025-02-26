package com.power.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface I18nMapper {
    String queryItemContent(@Param("tableName") String tableName,
                            @Param("orgId") String orgId,
                            @Param("bizId") Long bizId,
                            @Param("type") Integer type,
                            @Param("locale") String locale,
                            @Param("disabled") int disabled);
}
