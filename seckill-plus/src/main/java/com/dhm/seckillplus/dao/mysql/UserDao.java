package com.dhm.seckillplus.dao.mysql;

import com.dhm.seckillplus.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;



@Mapper
public interface UserDao {

    @Select("select * from user where id = #{id}")
    User getById(@Param("id") Long id	);

    @Insert("insert into user(id, name)values(#{id}, #{name})")
    int insert(User user);

}
