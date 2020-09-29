package com.changgou.goods.service;


import com.changgou.goods.pojo.Spec;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface SpecService {

    List<Spec> findByCategory(Integer categoryId);
    /**
     * 多条件分页查询
     * @param
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spec> findPage(Spec spec, int page, int size);

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Spec> findPage(int page,int size);

    /**
     * 多条件方法查询
     * @param
     * @return
     */
    List<Spec> findList(Spec spec);

    /**
     * 根据id删除
     * @param id
     */
    void delete(long id);

    /**
     * 更新
     * @param
     */
    void update(Spec spec);

    /**
     * 添加操作
     * @param
     */
    void add(Spec spec);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Spec findById(long id);

    /**
     * 查询所有
     * @return
     */
    List<Spec> findAll();
}
