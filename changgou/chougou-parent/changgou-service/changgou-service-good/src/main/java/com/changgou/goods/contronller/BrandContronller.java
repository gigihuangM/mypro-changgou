package com.changgou.goods.contronller;

import com.changgou.goods.pojo.Brand;
import com.changgou.goods.service.BrandService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/brand")
@CrossOrigin  //跨域
public class BrandContronller {
    @Autowired
    BrandService brandService;

    @GetMapping(value = "/category/{id}")
    public Result<List<Brand>>  findBrandByCategory(@PathVariable("id")Integer id){
        List<Brand> brands = brandService.findByCategory(id);
        return new Result<List<Brand>>(true,StatusCode.OK,"查询成功",brands);
    }

    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@RequestBody Brand brand,
                                            @PathVariable("page")Integer page,
                                            @PathVariable("size")Integer size){

        PageInfo<Brand> pageInfo = brandService.findPage(brand,page,size);
        return new Result<PageInfo<Brand>>(true, StatusCode.OK,"分页查询品牌成功！",pageInfo);
    }


    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo<Brand>> findPage(@PathVariable("page")Integer page,
                                    @PathVariable("size")Integer size){

        PageInfo<Brand> pageInfo = brandService.findPage(page, size);
        return new Result<PageInfo<Brand>>(true, StatusCode.OK,"分页查询品牌成功！",pageInfo);
    }

    /**
     * 条件查询
     * @param
     * @return
     */

    @PostMapping(value = "/search")
    public Result<List<Brand>> findList(@RequestBody Brand brand){
        List<Brand> brands = brandService.findList(brand);
        return new Result<>(true, StatusCode.OK,"条件搜索查询品牌成功",brands);
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable("id")Integer id){
        brandService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功！！");
    }

    @PutMapping(value = "/{id}")
    public Result update(@PathVariable("id")Integer id,@RequestBody Brand brand){
        brand.setId(id);
        brandService.update(brand);
        return new Result(true,StatusCode.OK,"修改成功！！");
    }


    @PostMapping
    public Result add(@RequestBody Brand brand){
        //调用Service实现增加
        brandService.add(brand);
        return new Result(true, StatusCode.OK,"增加数据成功！");
    }

    @GetMapping(value = "/{id}")
    public Result<Brand> findById(@PathVariable("id") Integer id){
        Brand brand = brandService.findById(id);
        return new Result<>(true, StatusCode.OK,"根据ID查询成功",brand);
    }

    @GetMapping
    public Result<List<Brand>> findAll(){

        List<Brand> all = brandService.findAll();
        //响应结果封装
        return new Result<>(true, StatusCode.OK,"查询品牌成功",all);
    }
}
