package com.changgou.goods.service.impl;

import com.changgou.goods.dao.BrandMapper;
import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据分类id查询品牌集合
     * @param categoryId
     * @return
     */

    @Override
    public List<Brand> findByCategory(Integer categoryId) {
        return brandMapper.findByCategory(categoryId);
    }

    @Override
    public PageInfo<Brand> findPage(Brand brand, Integer page, Integer size) {
        PageHelper.startPage(page,size);
        //搜索的数据
        Example example=createExample(brand);
        List<Brand> brands = brandMapper.selectByExample(example);

        return new PageInfo<Brand>(brands);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public PageInfo<Brand> findPage(Integer page, Integer size) {
        PageHelper.startPage(page,size);
        //查询集合
        List<Brand> brands = brandMapper.selectAll();
        //封装pageinfo
        return new PageInfo<Brand>(brands);
    }

    /**
     * 多条件搜素
     * @param brand
     * @return
     */
    @Override
    public List<Brand> findList(Brand brand) {
        //自定义条件搜索对象
        Example example =createExample(brand);

        return brandMapper.selectByExample(example);
    }

    /**
     * 条件构建
     * @param brand
     * @return
     */
    public Example createExample(Brand brand){
        Example example =new Example(Brand.class);
        Example.Criteria criteria = example.createCriteria();  //条件构造器
        if(brand!=null){
            if(!StringUtils.isEmpty(brand.getName())){
                /**
                 * javaBean的属性名
                 */
                criteria.andLike("name","%"+brand.getName()+"%");
            }
            if(!StringUtils.isEmpty(brand.getLetter())){
                criteria.andEqualTo("letter",brand.getLetter());
            }
        }
        return example;
    }

    @Override
    public void delete(Integer id) {
        brandMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Brand brand) {
        brandMapper.updateByPrimaryKey(brand);
    }

    /**
     * 方法中有slective 会直接忽略null
     * @param brand
     */
    @Override
    public void add(Brand brand) {
        brandMapper.insertSelective(brand);
    }

    @Override
    public Brand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有的方法
     * @return
     */
    @Override
    public List<Brand> findAll() {
        return brandMapper.selectAll();
    }
}
