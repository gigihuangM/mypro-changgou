package com.changgou.goods.contronller;

import com.changgou.goods.pojo.Para;

import com.changgou.goods.service.ParaService;

import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/para")
@CrossOrigin  //跨域
public class ParaController {
    @Autowired
    ParaService paraService;

    @GetMapping(value = "/category/{id}")
    public Result<List<Para>> findByCategoryId(@PathVariable("id")Integer id){
        List<Para> paraList = paraService.findByCategory(id);
        return new Result<List<Para>>(true,StatusCode.OK,"查询参数集合成功",paraList);

    }


    @PostMapping("/search/{page}/{size}")
    public Result<PageInfo<Para>> findPage(@RequestBody Para para,
                                               @PathVariable("page")int page,
                                               @PathVariable("size")int size){
        PageInfo<Para> page1 = paraService.findPage(para, page, size);
        return new Result<PageInfo<Para>>(true, StatusCode.OK,"查询成功",page1);
    }

    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo<Para>> findPage(@PathVariable("page")int page,
                                               @PathVariable("size")int size){
        PageInfo<Para> page1 = paraService.findPage(page, size);
        return new Result<PageInfo<Para>>(true, StatusCode.OK,"查询成功",page1);
    }

    @PostMapping(value = "/search")
    public Result<List<Para>> findList(@RequestBody Para template){
        List<Para> list = paraService.findList(template);
        return new Result<List<Para>>(true, StatusCode.OK,"查询成功",list);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id")long id){
        paraService.delete(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }
    @PutMapping("/{id}")
    public Result update(@RequestBody Para template,@PathVariable("id")int id){
        template.setId(id);
        paraService.update(template);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    @PostMapping
    public Result add(@RequestBody Para template){
        paraService.add(template);
        return new Result(true, StatusCode.OK,"增加成功");
    }
    @GetMapping("/{id}")
    public Result<Para> findById(@PathVariable("id")long id){
        Para template = paraService.findById(id);
        return new Result<Para>(true, StatusCode.OK,"查询成功",template);
    }
    @GetMapping
    public Result<List<Para>> findAll(){
        List<Para> all = paraService.findAll();
        return new Result<List<Para>>(true, StatusCode.OK,"查询成功",all);
    }
}
