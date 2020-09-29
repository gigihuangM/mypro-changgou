package com.changgou.goods.service;

import com.changgou.goods.pojo.Brand;
import com.github.pagehelper.PageInfo;
import io.swagger.models.auth.In;

import java.util.List;

public interface BrandService {

    List<Brand> findByCategory(Integer categoryId);
     /**
     * 条件分页
     * @param page
     * @param size
     * @return
     */

    PageInfo<Brand> findPage(Brand brand,Integer page,Integer size);



    /**
     * 分页操作
     * @param page
     * @param size
     * @return
     */

    PageInfo<Brand> findPage(Integer page,Integer size);


    List<Brand> findList(Brand brand);

    void delete(Integer id);

    void update(Brand brand);

    void add(Brand brand);

    Brand findById(Integer id);
    /**
     * 查询所有
     */
    List<Brand> findAll();
}
