package com.power.mapper;

import com.power.domain.po.International;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author power
* @description 针对表【international(国际化信息表)】的数据库操作Mapper
* @createDate 2025-02-26 00:52:23
* @Entity generator.domain.International
*/
@Mapper
public interface InternationalMapper extends BaseMapper<International> {


    List<String> selectAllByLocale(@Param("orgId") String orgId);
}




