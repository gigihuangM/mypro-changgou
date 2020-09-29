package com.changgou.goods.feign;


import com.changgou.goods.pojo.Category;
import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "goods")
public interface CategoryFeign {

    @GetMapping("/category/{id}")
    Result<Category> findById(@PathVariable("id") Integer id);
}
