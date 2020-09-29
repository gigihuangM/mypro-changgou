package com.changgou.goods.service.impl;

import com.changgou.goods.dao.CategoryMapper;
import com.changgou.goods.dao.SpecMapper;
import com.changgou.goods.pojo.Category;
import com.changgou.goods.pojo.Spec;
import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.SpecService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class SpecServiceImpl implements SpecService {
    @Autowired
    SpecMapper specMapper;

    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 根据分类id查询规格
     * @param categoryId
     * @return
     */
    @Override
    public List<Spec> findByCategory(Integer categoryId) {
        Category category = categoryMapper.selectByPrimaryKey(categoryId);
        Spec spec=new Spec();
        spec.setTemplateId(category.getTemplateId());
        return specMapper.select(spec);
    }

    @Override
    public PageInfo<Spec> findPage(Spec spec, int page, int size) {
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构造
        Example example=createExample(spec);
        //执行搜索
        return new PageInfo<Spec>(specMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Spec> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        return new PageInfo<>(specMapper.selectAll());
    }

    @Override
    public List<Spec> findList(Spec spec) {
        Example example=createExample(spec);
        return specMapper.selectByExample(example);
    }

    public Example createExample(Spec spec){
        Example example=new Example(Template.class);
        Example.Criteria criteria = example.createCriteria();
        if(spec!=null){
            if(!StringUtils.isEmpty(spec.getId())){
                criteria.andEqualTo("id",spec.getId());
            }
            if(!StringUtils.isEmpty(spec.getName())){
                criteria.andLike("name","%"+spec.getName()+"%");
            }
            if(!StringUtils.isEmpty(spec.getOptions())){
                criteria.andEqualTo("options",spec.getOptions());
            }
            if(!StringUtils.isEmpty(spec.getSeq())){
                criteria.andEqualTo("seq",spec.getSeq());
            }
            if(!StringUtils.isEmpty(spec.getTemplateId())){
                criteria.andEqualTo("templateId",spec.getTemplateId());
            }
        }
        return example;
    }

    @Override
    public void delete(long id) {
        specMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Spec spec) {
        specMapper.updateByPrimaryKey(spec);
    }

    @Override
    public void add(Spec album) {
        specMapper.insertSelective(album);
    }

    @Override
    public Spec findById(long id) {
        return specMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Spec> findAll() {
        return specMapper.selectAll();
    }
}
