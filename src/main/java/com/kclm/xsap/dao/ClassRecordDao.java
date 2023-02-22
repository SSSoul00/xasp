package com.kclm.xsap.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.kclm.xsap.entity.ClassRecordEntity;
import com.kclm.xsap.vo.ClassRecordVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ClassRecordDao extends BaseMapper<ClassRecordEntity> {
    List<ClassRecordVo> getAllById(Long id);
}
