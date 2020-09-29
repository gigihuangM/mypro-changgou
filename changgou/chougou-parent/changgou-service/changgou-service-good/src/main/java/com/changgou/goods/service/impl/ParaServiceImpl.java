package com.changgou.goods.service.impl;

import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.ParaMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Para;
import com.changgou.goods.service.CategoryService;
import com.changgou.goods.service.ParaService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class ParaServiceImpl implements ParaService {

    @Autowired
    ParaMapper paraMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Override
    public List<Para> findByCategory(Integer categoryid) {
        Category category = categoryMapper.selectByPrimaryKey(categoryid);
        Para para=new Para();
        para.setTemplateId(category.getTemplateId());
        return paraMapper.select(para);

    }

    @Override
    public PageInfo<Para> findPage(Para para, int page, int size) {
        PageHelper.startPage(page,size);
        //查询集合
        Example example=createExample(para);
        return new PageInfo<Para>(paraMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Para> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        return new PageInfo<>(paraMapper.selectAll());
    }

    @Override
    public List<Para> findList(Para para) {
        Example example=createExample(para);
        return paraMapper.selectByExample(example);
    }
    public Example createExample(Para para){
        Example example =new Example(Para.class);
        Example.Criteria criteria = example.createCriteria();  //条件构造器
        if(para!=null){
            if(!StringUtils.isEmpty(para.getName())){
                criteria.andLike("name","%"+para.getName()+"%");
            }
            if(!StringUtils.isEmpty(para.getOptions())){
                criteria.andEqualTo("options",para.getOptions());
            }
            if(!StringUtils.isEmpty(para.getSeq())){
                criteria.andEqualTo("seq",para.getSeq());
            }
            if(!StringUtils.isEmpty(para.getTemplateId())){
                criteria.andEqualTo("templateId",para.getTemplateId());
            }
        }
        return example;
    }

    @Override
    public void delete(long id) {
        paraMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Para para) {
        paraMapper.updateByPrimaryKey(para);
    }

    @Override
    public void add(Para para) {
        paraMapper.insertSelective(para);
    }

    @Override
    public Para findById(long id) {
        return paraMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Para> findAll() {
        return paraMapper.selectAll();
    }
}
