package com.changgou.order.service.impl;

import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.feign.SpuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.goods.pojo.Spu;
import com.changgou.order.pojo.OrderItem;
import com.changgou.order.service.CartService;
import entity.Result;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    SkuFeign skuFeign;

    @Autowired
    SpuFeign spuFeign;

    @Override
    public void add(Integer num, Long id, String username) {

        if(num<=0){
            redisTemplate.boundHashOps("Cart_"+username).delete(id);

            Long size = redisTemplate.boundHashOps("Cart_" + username).size();
            if(size==null || size<=0){
                redisTemplate.delete("Cart_" + username);
            }
              return ;
        }


        Result<Sku> skuResult = skuFeign.findById(id);
        Sku sku = skuResult.getData();
        Result<Spu> spuResult = spuFeign.findSpuById(sku.getSpuId().toString());
        Spu spu = spuResult.getData();

        OrderItem orderItem = createOrderItem(num, id, sku, spu);

        redisTemplate.boundHashOps("Cart_"+username).put(id,orderItem);

    }

    @Override
    public List<OrderItem> list(String username) {
        return redisTemplate.boundHashOps("Cart_"+username).values();
    }

    private OrderItem createOrderItem(Integer num, Long id, Sku sku, Spu spu) {
        OrderItem orderItem=new OrderItem();
        orderItem.setCategoryId1(spu.getCategory1Id());
        orderItem.setCategoryId2(spu.getCategory2Id());
        orderItem.setCategoryId3(spu.getCategory3Id());
        orderItem.setSpuId(spu.getId().toString());
        orderItem.setSkuId(id.toString());
        orderItem.setName(sku.getName());
        orderItem.setPrice(sku.getPrice());
        orderItem.setNum(num);
        orderItem.setMoney(num*sku.getPrice());
        orderItem.setImage(spu.getImage());
        return orderItem;
    }
}
