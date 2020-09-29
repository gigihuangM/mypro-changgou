package com.changgou.goods.service.impl;


import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.service.CategoryService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 根据分类的父节点ID查询所有子节点的
     * @param pid
     * @return
     */
    @Override
    public List<Category> findByParentId(Integer pid) {
        Category category=new Category();
        category.setParentId(pid);
        return categoryMapper.select(category);
    }

    @Override
    public PageInfo<Category> findPage(Category category, int page, int size) {
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构造
        Example example=createExample(category);
        //执行搜索
        return new PageInfo<Category>(categoryMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Category> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        return new PageInfo<>(categoryMapper.selectAll());
    }

    @Override
    public List<Category> findList(Category category) {
        Example example=createExample(category);
        return categoryMapper.selectByExample(example);
    }
    public Example createExample(Category category){
        Example example=new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        if(category!=null){
            if(!StringUtils.isEmpty(category.getId())){
                criteria.andEqualTo("id",category.getId());
            }
            if(!StringUtils.isEmpty(category.getName())){
                criteria.andLike("name","%"+category.getName()+"%");
            }
            if(!StringUtils.isEmpty(category.getGoodsNum())){
                criteria.andEqualTo("goodsNum",category.getGoodsNum());
            }
            if(!StringUtils.isEmpty(category.getIsMenu())){
                criteria.andEqualTo("isMenu",category.getIsMenu());
            }
            if(!StringUtils.isEmpty(category.getIsShow())){
                criteria.andEqualTo("isShow",category.getIsShow());
            }
            if(!StringUtils.isEmpty(category.getSeq())){
                criteria.andEqualTo("seq",category.getSeq());
            }
            if(!StringUtils.isEmpty(category.getParentId())){
                criteria.andEqualTo("parentId",category.getParentId());
            }
            if(!StringUtils.isEmpty(category.getTemplateId())){
                criteria.andEqualTo("templateId",category.getTemplateId());
            }
        }
        return example;
    }

    @Override
    public void delete(long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Category category) {
        categoryMapper.updateByPrimaryKey(category);
    }

    @Override
    public void add(Category category) {
        categoryMapper.insertSelective(category);
    }

    @Override
    public Category findById(long id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Category> findAll() {
        return categoryMapper.selectAll();
    }
}
