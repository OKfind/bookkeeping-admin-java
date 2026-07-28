package org.example.mapper;

import com.github.yulichang.base.MPJBaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.pojo.entity.User;

@Mapper
public interface userMapper extends MPJBaseMapper<User> {
}
