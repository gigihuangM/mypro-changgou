package com.changgou.goods.feign;


import com.changgou.goods.pojo.Spu;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "goods")
public interface SpuFeign {

    @GetMapping("/spu/{id}")
     Result<Spu> findSpuById(@PathVariable("id") String id);

}
