package com.mooc.house.biz.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.mooc.house.biz.mapper.BlogMapper;
import com.mooc.house.common.model.Blog;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BlogService {

    @Autowired
    private BlogMapper blogMapper;

    public PageInfo<Blog> queryBlog(Blog query) {
        List<Blog> blogs = blogMapper.selectBlog(query);
        PageHelper.startPage(query.getPageNum(), query.getPageSize());
        PageInfo pageInfo = new PageInfo(blogs);
        return pageInfo;
    }

    private void populate(List<Blog> blogs) {
        if (!blogs.isEmpty()) {
            blogs.stream().forEach(item -> {
                String stripped = Jsoup.parse(item.getContent()).text();
                item.setDigest(stripped.substring(0, Math.min(stripped.length(), 40)));
                String tags = item.getTags();
                item.getTagList().addAll(Lists.newArrayList(Splitter.on(",").split(tags)));
            });
        }
    }

    public Blog queryOneBlog(Long id) {
        Blog query = new Blog();
        query.setId(id);
        List<Blog> blogs = blogMapper.selectBlog(query);
        if (!blogs.isEmpty()) {
            return blogs.get(0);
        }
        return null;
    }


}
