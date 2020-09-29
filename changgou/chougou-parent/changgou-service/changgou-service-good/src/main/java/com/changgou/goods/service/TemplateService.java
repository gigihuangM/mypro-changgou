package com.changgou.goods.service;

import com.changgou.goods.pojo.Template;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface TemplateService {
    PageInfo<Template> findPage(Template template, int page, int size);

    PageInfo<Template> findPage(int page, int size);

    List<Template> findList(Template template);

    void delete(long id);

    void update(Template template);

    void add(Template template);

    Template findById(long id);

    List<Template> findAll();
}
