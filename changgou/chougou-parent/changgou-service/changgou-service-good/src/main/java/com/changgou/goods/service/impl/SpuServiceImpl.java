package com.changgou.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.goods.dao.*;
import com.changgou.goods.pojo.*;
import com.changgou.goods.service.SpuService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import entity.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class SpuServiceImpl implements SpuService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    SkuMapper skuMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    BrandMapper brandMapper;

    @Autowired
    SpecMapper specMapper;

    @Autowired
    IdWorker idWorker;

    @Override
    public void putMany(Long[] ids) {
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andIn("id", Arrays.asList(ids));

        criteria.andEqualTo("isDelete","0");

        criteria.andEqualTo("status","1");

        Spu spu=new Spu();
        spu.setIsMarketable("1");
        spuMapper.updateByExampleSelective(spu,example);
    }

    @Override
    public void put(Long spuid) {
        Spu spu = spuMapper.selectByPrimaryKey(spuid);
        if(spu.getIsDelete().equals("1")){
            throw  new RuntimeException("此商品已删除");
        }if(spu.getStatus().equals("1")){
            throw  new RuntimeException("未审核的商品不能！！");
        }
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKey(spu);
    }

    @Override
    public void pull(Long spuid) {
        Spu spu = spuMapper.selectByPrimaryKey(spuid);
        if(spu.getIsDelete().equalsIgnoreCase("1")){
            throw  new RuntimeException("不能对该商品进行下架");
        }

        spu.setIsMarketable("0");
        spuMapper.updateByPrimaryKey(spu);
    }

    @Override
    public void audit(Long spuid) {
        Spu spu = spuMapper.selectByPrimaryKey(spuid);
        if(spu.getIsDelete().equalsIgnoreCase("1")){
            throw  new RuntimeException("不能对该商品进行审核");
        }
        spu.setStatus("1");
        spu.setIsMarketable("1");
        spuMapper.updateByPrimaryKey(spu);
    }

    @Override
    public Goods findGoodsById(long id) {
        Spu spu = spuMapper.selectByPrimaryKey(id);
        Sku sku=new Sku();
        sku.setSpuId(id);
        List<Sku> skuList = skuMapper.select(sku);
        Goods goods=new Goods();
        goods.setSpu(spu);
        goods.setSkuList(skuList);
        return goods;
    }

    @Override
    public void save(Goods goods) {
        Spu spu = goods.getSpu();

        if(StringUtils.isEmpty(spu.getId())){
            spu.setId(idWorker.nextId());
            spuMapper.insertSelective(spu);
        }else{
            spuMapper.updateByPrimaryKey(spu);
            Sku sku=new Sku();
            sku.setSpuId(spu.getId());
            skuMapper.delete(sku);
        }


        Category category = categoryMapper.selectByPrimaryKey(spu.getCategory3Id());

        Brand brand = brandMapper.selectByPrimaryKey(spu.getBrandId());

        Date date=new Date();
        List<Sku> skuList = goods.getSkuList();

        for (Sku sku : skuList) {
            sku.setId(idWorker.nextId());
            String name=spu.getName();
            if(StringUtils.isEmpty(sku.getSpec())){
                sku.setSpec("{}");
            }

            Map<String,String> map = JSON.parseObject(sku.getSpec(), Map.class);
            for (Map.Entry<String, String> entry : map.entrySet()) {
                name=" "+entry.getValue();
            }
            sku.setName(name);
            sku.setCreateTime(date);
            sku.setUpdateTime(date);
            sku.setSpuId(spu.getId());
            sku.setCategoryId(spu.getCategory3Id());
            sku.setCategoryName(category.getName());
            sku.setBrandName(brand.getName());
            skuMapper.insertSelective(sku);
        }
    }

    /**
     * 查询全部列表
     * @return
     */
    @Override
    public List<Spu> findAll() {
        return spuMapper.selectAll();
    }

    /**
     * 根据ID查询
     * @param id
     * @return
     */
    @Override
    public Spu findById(String id){
        return  spuMapper.selectByPrimaryKey(id);
    }


    /**
     * 增加
     * @param spu
     */
    @Override
    public void add(Spu spu){
        spuMapper.insert(spu);
    }


    /**
     * 修改
     * @param spu
     */
    @Override
    public void update(Spu spu){
        spuMapper.updateByPrimaryKey(spu);
    }

    /**
     * 删除
     * @param id
     */
    @Override
    public void delete(String id){
        spuMapper.deleteByPrimaryKey(id);
    }


    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @Override
    public List<Spu> findList(Map<String, Object> searchMap){
        Example example = createExample(searchMap);
        return spuMapper.selectByExample(example);
    }

    /**
     * 分页查询
     * @param page
     * @param size
     * @return
     */
    @Override
    public Page<Spu> findPage(int page, int size){
        PageHelper.startPage(page,size);
        return (Page<Spu>)spuMapper.selectAll();
    }

    /**
     * 条件+分页查询
     * @param searchMap 查询条件
     * @param page 页码
     * @param size 页大小
     * @return 分页结果
     */
    @Override
    public Page<Spu> findPage(Map<String,Object> searchMap, int page, int size){
        PageHelper.startPage(page,size);
        Example example = createExample(searchMap);
        return (Page<Spu>)spuMapper.selectByExample(example);
    }

    /**
     * 构建查询对象
     * @param searchMap
     * @return
     */
    private Example createExample(Map<String, Object> searchMap){
        Example example=new Example(Spu.class);
        Example.Criteria criteria = example.createCriteria();
        if(searchMap!=null){
            // 主键
            if(searchMap.get("id")!=null && !"".equals(searchMap.get("id"))){
                criteria.andEqualTo("id",searchMap.get("id"));
           	}
            // 货号
            if(searchMap.get("sn")!=null && !"".equals(searchMap.get("sn"))){
                criteria.andEqualTo("sn",searchMap.get("sn"));
           	}
            // SPU名
            if(searchMap.get("name")!=null && !"".equals(searchMap.get("name"))){
                criteria.andLike("name","%"+searchMap.get("name")+"%");
           	}
            // 副标题
            if(searchMap.get("caption")!=null && !"".equals(searchMap.get("caption"))){
                criteria.andLike("caption","%"+searchMap.get("caption")+"%");
           	}
            // 图片
            if(searchMap.get("image")!=null && !"".equals(searchMap.get("image"))){
                criteria.andLike("image","%"+searchMap.get("image")+"%");
           	}
            // 图片列表
            if(searchMap.get("images")!=null && !"".equals(searchMap.get("images"))){
                criteria.andLike("images","%"+searchMap.get("images")+"%");
           	}
            // 售后服务
            if(searchMap.get("saleService")!=null && !"".equals(searchMap.get("saleService"))){
                criteria.andLike("saleService","%"+searchMap.get("saleService")+"%");
           	}
            // 介绍
            if(searchMap.get("introduction")!=null && !"".equals(searchMap.get("introduction"))){
                criteria.andLike("introduction","%"+searchMap.get("introduction")+"%");
           	}
            // 规格列表
            if(searchMap.get("specItems")!=null && !"".equals(searchMap.get("specItems"))){
                criteria.andLike("specItems","%"+searchMap.get("specItems")+"%");
           	}
            // 参数列表
            if(searchMap.get("paraItems")!=null && !"".equals(searchMap.get("paraItems"))){
                criteria.andLike("paraItems","%"+searchMap.get("paraItems")+"%");
           	}
            // 是否上架
            if(searchMap.get("isMarketable")!=null && !"".equals(searchMap.get("isMarketable"))){
                criteria.andEqualTo("isMarketable",searchMap.get("isMarketable"));
           	}
            // 是否启用规格
            if(searchMap.get("isEnableSpec")!=null && !"".equals(searchMap.get("isEnableSpec"))){
                criteria.andEqualTo("isEnableSpec", searchMap.get("isEnableSpec"));
           	}
            // 是否删除
            if(searchMap.get("isDelete")!=null && !"".equals(searchMap.get("isDelete"))){
                criteria.andEqualTo("isDelete",searchMap.get("isDelete"));
           	}
            // 审核状态
            if(searchMap.get("status")!=null && !"".equals(searchMap.get("status"))){
                criteria.andEqualTo("status",searchMap.get("status"));
           	}

            // 品牌ID
            if(searchMap.get("brandId")!=null ){
                criteria.andEqualTo("brandId",searchMap.get("brandId"));
            }
            // 一级分类
            if(searchMap.get("category1Id")!=null ){
                criteria.andEqualTo("category1Id",searchMap.get("category1Id"));
            }
            // 二级分类
            if(searchMap.get("category2Id")!=null ){
                criteria.andEqualTo("category2Id",searchMap.get("category2Id"));
            }
            // 三级分类
            if(searchMap.get("category3Id")!=null ){
                criteria.andEqualTo("category3Id",searchMap.get("category3Id"));
            }
            // 模板ID
            if(searchMap.get("templateId")!=null ){
                criteria.andEqualTo("templateId",searchMap.get("templateId"));
            }
            // 运费模板id
            if(searchMap.get("freightId")!=null ){
                criteria.andEqualTo("freightId",searchMap.get("freightId"));
            }
            // 销量
            if(searchMap.get("saleNum")!=null ){
                criteria.andEqualTo("saleNum",searchMap.get("saleNum"));
            }
            // 评论数
            if(searchMap.get("commentNum")!=null ){
                criteria.andEqualTo("commentNum",searchMap.get("commentNum"));
            }

        }
        return example;
    }

}
