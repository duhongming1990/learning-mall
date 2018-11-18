package com.mooc.house.biz.mapper;

import com.mooc.house.common.model.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface BlogMapper {

  public List<Blog> selectBlog(@Param("blog")Blog query);

}
