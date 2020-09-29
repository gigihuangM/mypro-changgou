package com.changgou.goods.service;

import com.changgou.goods.pojo.Category;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CategoryService {

    List<Category> findByParentId(Integer pid);
    /**
     * 多条件分页查询
     * @param
     * @param page
     * @param size
     * @return
     */
    PageInfo<Category> findPage(Category category, int page, int size);

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Category> findPage(int page,int size);

    List<Category> findList(Category category);

    /**
     * 根据id删除
     * @param id
     */
    void delete(long id);


    void update(Category category);

    void add(Category category);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Category findById(long id);

    /**
     * 查询所有
     * @return
     */
    List<Category> findAll();

}
