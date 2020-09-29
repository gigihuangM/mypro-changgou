package com.changgou.goods.service.impl;

import com.changgou.goods.dao.AlbumMapper;
import com.changgou.goods.pojo.Album;
import com.changgou.goods.service.AlbumService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import entity.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
@Service
public class AlbumServiceImpl implements AlbumService {

    @Autowired
   AlbumMapper albumMapper;

    @Override
    public PageInfo<Album> findPage(Album album, int page, int size) {
        //分页
        PageHelper.startPage(page,size);
        //搜索条件构造
        Example example=createExample(album);
        //执行搜索
        return new PageInfo<Album>(albumMapper.selectByExample(example));
    }

    @Override
    public PageInfo<Album> findPage(int page, int size) {
        PageHelper.startPage(page,size);
        return new PageInfo<>(albumMapper.selectAll());
    }

    @Override
    public List<Album> findList(Album album) {
        Example example=createExample(album);
        return albumMapper.selectByExample(example);
    }

    public Example createExample(Album album){
        Example example=new Example(Album.class);
        Example.Criteria criteria = example.createCriteria();
        if(album!=null){
            if(!StringUtils.isEmpty(album.getId())){
                criteria.andEqualTo("id",album.getId());
            }
            if(!StringUtils.isEmpty(album.getTitle())){
                criteria.andLike("title","%"+album.getTitle());
            }
            if(!StringUtils.isEmpty(album.getImage())){
                criteria.andEqualTo("image",album.getImage());
            }
            if(!StringUtils.isEmpty(album.getImageItems())) {
                criteria.andEqualTo("imagesItems", album.getImageItems());
            }
        }
        return example;
    }

    @Override
    public void delete(long id) {
     albumMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Album album) {
     albumMapper.updateByPrimaryKey(album);
    }

    @Override
    public void add(Album album) {
    albumMapper.insertSelective(album);
    }

    @Override
    public Album findById(long id) {
        return albumMapper.selectByPrimaryKey(id);
    }

    @Override
    public List<Album> findAll() {
        return albumMapper.selectAll();
    }

}
