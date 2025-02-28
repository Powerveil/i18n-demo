package com.power.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface I18nMapper {
    String queryItemContent(@Param("tableName") String tableName,
                            @Param("orgId") String orgId,
                            @Param("bizId") Long bizId,
                            @Param("type") Integer type,
                            @Param("locale") String locale,
                            @Param("disabled") int disabled);

    List<String> queryItemListContentByBizIdList(@Param("tableName") String tableName,
                                                 @Param("orgId") String orgId,
                                                 @Param("bizIdList") List<Long> bizIdList,
                                                 @Param("type") Integer type,
                                                 @Param("locale") String locale,
                                                 @Param("disabled") int disabled,
                                                 @Param("split") String split);

    List<String> queryItemListContentByLocaleList(@Param("tableName") String tableName,
                                                  @Param("orgId") String orgId,
                                                  @Param("bizId") Long bizId,
                                                  @Param("type") Integer type,
                                                  @Param("localeList") List<String> localeList,
                                                  @Param("disabled") int disabled,
                                                  @Param("split") String split);

    List<String> queryItemListContentByBizIdListAndLocaleList(@Param("tableName") String tableName,
                                                              @Param("orgId") String orgId,
                                                              @Param("bizIdList") List<Long> bizIdList,
                                                              @Param("type") Integer type,
                                                              @Param("localeList") List<String> localeList,
                                                              @Param("disabled") int disabled,
                                                              @Param("split") String split);
}
