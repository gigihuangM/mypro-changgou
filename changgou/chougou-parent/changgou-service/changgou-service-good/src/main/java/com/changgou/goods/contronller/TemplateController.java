package com.changgou.goods.contronller;

import com.changgou.goods.pojo.Template;
import com.changgou.goods.service.TemplateService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/template")
@CrossOrigin
public class TemplateController {
    @Autowired
    TemplateService templateService;

    @PostMapping("/search/{page}/{size}")
    public Result<PageInfo<Template>> findPage(@RequestBody Template template,
                                               @PathVariable("page")int page,
                                               @PathVariable("size")int size){
        PageInfo<Template> page1 = templateService.findPage(template, page, size);
        return new Result<PageInfo<Template>>(true, StatusCode.OK,"查询成功",page1);
    }

    @GetMapping("/search/{page}/{size}")
    public Result<PageInfo<Template>> findPage(@PathVariable("page")int page,
                                               @PathVariable("size")int size){
        PageInfo<Template> page1 = templateService.findPage(page, size);
        return new Result<PageInfo<Template>>(true, StatusCode.OK,"查询成功",page1);
    }

    @PostMapping(value = "/search")
    public Result<List<Template>> findList(@RequestBody Template template){
        List<Template> list = templateService.findList(template);
        return new Result<List<Template>>(true, StatusCode.OK,"查询成功",list);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id")long id){
        templateService.delete(id);
        return new Result(true, StatusCode.OK,"删除成功");
    }
    @PutMapping("/{id}")
    public Result update(@RequestBody Template template,@PathVariable("id")int id){
        template.setId(id);
        templateService.update(template);
        return new Result(true, StatusCode.OK,"修改成功");
    }

    @PostMapping
    public Result add(@RequestBody Template template){
        templateService.add(template);
        return new Result(true, StatusCode.OK,"增加成功");
    }
    @GetMapping("/{id}")
    public Result<Template> findById(@PathVariable("id")long id){
        Template template = templateService.findById(id);
        return new Result<Template>(true, StatusCode.OK,"查询成功",template);
    }
    @GetMapping
    public Result<List<Template>> findAll(){
        List<Template> all = templateService.findAll();
        return new Result<List<Template>>(true, StatusCode.OK,"查询成功",all);
    }
}
