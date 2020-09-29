package com.changgou.goods.service.impl;

import com.changgou.goods.dao.TemplateMapper;
import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.TemplateService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class TemplateImpl implements TemplateService {
    @Autowired
    TemplateMapper templateMapper;


    @Override
    public PageInfo<Template> findPage(Template template, int page, int size) {
        PageHelper.startPage(page,size);
        Example example = createExample(template);
        return new PageInfo<Template>(templateMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Template> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        return new PageInfo<>(templateMapper.selectAll());
    }

    @Override
    public List<Template> findList(Template template) {
        Example example = createExample(template);
        return templateMapper.selectByExample(example);
    }
    public Example createExample(Template template){
        Example example=new Example(Template.class);
        Example.Criteria criteria = example.createCriteria();
        if(template!=null){
            if(!StringUtils.isEmpty(template.getId())){
                criteria.andEqualTo("id",template.getId());
            }
            if(!StringUtils.isEmpty(template.getName())){
                criteria.andLike("name","%"+template.getName()+"%");
            }
            if(!StringUtils.isEmpty(template.getSpecNum())){
                criteria.andEqualTo("specNum",template.getSpecNum());
            }
            if(!StringUtils.isEmpty(template.getParaNum())){
                criteria.andEqualTo("paraNum",template.getParaNum());
            }
        }
        return example;
    }

    @Override
    public void delete(long id) {
        templateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Template template) {
       templateMapper.updateByPrimaryKeySelective(template);
    }

    @Override
    public void add(@RequestBody Template template) {
      templateMapper.insertSelective(template);
    }

    @Override
    public Template findById(long id) {
        Template template = templateMapper.selectByPrimaryKey(id);
        return template;
    }

    @Override
    public List<Template> findAll() {
        List<Template> list = templateMapper.selectAll();
        return list;
    }
}
