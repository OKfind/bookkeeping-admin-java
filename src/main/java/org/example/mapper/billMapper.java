package org.example.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.pojo.entity.Bill;

@Mapper
public interface billMapper extends MPJBaseMapper<Bill> {
}
