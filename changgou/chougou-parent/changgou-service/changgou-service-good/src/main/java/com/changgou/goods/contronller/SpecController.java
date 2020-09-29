package com.changgou.goods.contronller;


import com.changgou.goods.pojo.Spec;
import com.changgou.goods.service.SpecService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/spec")
@CrossOrigin  //跨域
public class SpecController {
    @Autowired
    private SpecService specService;

    /**
     * 根据分类id查询模板集合信息
     */

    @GetMapping(value = "/category/{id}")
    public Result<List<Spec>> findByCategoryId(@PathVariable("id")Integer id){
        List<Spec> specs = specService.findByCategory(id);
        return new Result<List<Spec>>(true, StatusCode.OK,"查询规格集合成功",specs);
    }

    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo<Spec>> findPage(@RequestBody Spec spec,
                                           @PathVariable("page")Integer page,
                                           @PathVariable("size")Integer size){
        PageInfo<Spec> page1 = specService.findPage(spec, page, size);
        return new Result<PageInfo<Spec>>(true, StatusCode.OK,"查询成功",page1);
    }

    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo<Spec>> findPage(@PathVariable("page")Integer page,
                                            @PathVariable("size")Integer size){
        PageInfo<Spec> page1 = specService.findPage(page, size);
        return new Result<PageInfo<Spec>>(true,StatusCode.OK,"查询成功",page1);
    }

    @PostMapping(value = "/search")
    public Result<List<Spec>> findList(@RequestBody Spec spec){
        List<Spec> list = specService.findList(spec);
        return new Result<List<Spec>>(true,StatusCode.OK,"查询成功",list);
    }

    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id")long id){
        specService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    @PutMapping(value = "/{id}")
    public Result update(@PathVariable("id")int id,@RequestBody Spec spec){
        spec.setId(id);
        specService.update(spec);
        return new Result(true,StatusCode.OK,"修改成功");
    }
    @PostMapping
    public Result add(@RequestBody Spec album){
        specService.add(album);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    @GetMapping("/{id}")
    public Result<Spec> findById(@PathVariable("id")long id){
        Spec album = specService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",album);
    }
    @GetMapping
    public Result<List<Spec>> findAll(){
        List<Spec> list = specService.findAll();
        return new Result<List<Spec>>(true,StatusCode.OK,"查询成功",list);
    }

}
