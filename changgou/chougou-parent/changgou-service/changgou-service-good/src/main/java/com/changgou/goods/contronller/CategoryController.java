package com.changgou.goods.contronller;


import com.changgou.goods.pojo.Category;
import com.changgou.goods.service.CategoryService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/category")
@CrossOrigin
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping(value = "/list/{pid}")
    public Result<List<Category>> findByParent(@PathVariable("pid")Integer pid){
        List<Category> categories = categoryService.findByParentId(pid);
        return new Result<List<Category>>(true,StatusCode.OK,"查询子节点成功",categories);
    }

    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo<Category>> findPage(@RequestBody Category category,
                                               @PathVariable("page")Integer page,
                                               @PathVariable("size")Integer size){
        PageInfo<Category> page1 = categoryService.findPage(category, page, size);
        return new Result<PageInfo<Category>>(true, StatusCode.OK,"查询成功",page1);
    }

    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo<Category>> findPage(@PathVariable("page")Integer page,
                                            @PathVariable("size")Integer size){
        PageInfo<Category> page1 = categoryService.findPage(page, size);
        return new Result<PageInfo<Category>>(true,StatusCode.OK,"查询成功",page1);
    }

    @PostMapping(value = "/search")
    public Result<List<Category>> findList(@RequestBody Category category){
        List<Category> list = categoryService.findList(category);
        return new Result<List<Category>>(true,StatusCode.OK,"查询成功",list);
    }

    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id")long id){
        categoryService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    @PutMapping(value = "/{id}")
    public Result update(@PathVariable("id")int id,@RequestBody Category category){
        category.setId(id);
        categoryService.update(category);
        return new Result(true,StatusCode.OK,"修改成功");
    }
    @PostMapping
    public Result add(@RequestBody Category category){
        categoryService.add(category);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    @GetMapping("/{id}")
    public Result<Category> findById(@PathVariable("id")long id){
        Category category = categoryService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",category);
    }
    @GetMapping
    public Result<List<Category>> findAll(){
        List<Category> list = categoryService.findAll();
        return new Result<List<Category>>(true,StatusCode.OK,"查询成功",list);
    }

}
