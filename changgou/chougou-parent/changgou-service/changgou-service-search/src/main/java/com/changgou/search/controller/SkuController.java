package com.changgou.search.controller;

import com.changgou.search.service.SkuService;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/search")
@CrossOrigin
public class SkuController {

    @Autowired
    private SkuService skuService;

    /**
     * 搜索
     */

    @GetMapping
    public Map search(@RequestParam(required = false) Map<String,String> searchMap)throws Exception{
        return skuService.search(searchMap);
    }

    /**
     * 数据导入
     * @return
     */
    @GetMapping(value = "/import")

    public Result importDate(){
        skuService.importDate();
        return new Result(true, StatusCode.OK,"执行操作成功");
    }

}
