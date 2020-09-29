package com.changgou.goods.feign;

import com.changgou.goods.pojo.Sku;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
@FeignClient(value = "goods")
@RequestMapping("/sku")
public interface SkuFeign {

    @GetMapping
    Result<List<Sku>> findAll();


    @GetMapping("/sku/spu/{spuId}")
     List<Sku> findSkuListBySpuId(@PathVariable("spuId") String spuId);

    @GetMapping("/{id}")
     Result<Sku> findById(@PathVariable("id") Long id);
}
