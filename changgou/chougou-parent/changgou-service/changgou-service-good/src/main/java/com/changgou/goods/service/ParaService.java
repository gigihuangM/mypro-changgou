package com.changgou.goods.service;

import com.changgou.goods.pojo.Para;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface ParaService {

    List<Para> findByCategory(Integer categoryid);
    /**
     * 多条件分页查询
     * @param
     * @param page
     * @param size
     * @return
     */
    PageInfo<Para> findPage(Para para, int page, int size);

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Para> findPage(int page,int size);

    /**
     * 多条件方法查询
     * @param
     * @return
     */
    List<Para> findList(Para para);

    /**
     * 根据id删除
     * @param id
     */
    void delete(long id);

    /**
     * 更新
     * @param
     */
    void update(Para para);

    /**
     * 添加操作
     * @param
     */
    void add(Para para);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Para findById(long id);

    /**
     * 查询所有
     * @return
     */
    List<Para> findAll();

}
