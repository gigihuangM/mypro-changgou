package com.changgou.goods.service;

import com.changgou.goods.pojo.Album;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface AlbumService {
    /**
     * 多条件分页查询
     * @param album
     * @param page
     * @param size
     * @return
     */
    PageInfo<Album> findPage(Album album,int page,int size);

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    PageInfo<Album> findPage(int page,int size);

    /**
     * 多条件方法查询
     * @param album
     * @return
     */
    List<Album> findList(Album album);

    /**
     * 根据id删除
     * @param id
     */
    void delete(long id);

    /**
     * 更新
     * @param album
     */
    void update(Album album);

    /**
     * 添加操作
     * @param album
     */
    void add(Album album);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    Album findById(long id);

    /**
     * 查询所有
     * @return
     */
    List<Album> findAll();


}
