package com.changgou.goods.contronller;

import com.changgou.goods.pojo.Goods;
import com.changgou.goods.service.SpuService;
import com.changgou.goods.pojo.Spu;
import com.github.pagehelper.Page;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
@RestController
@CrossOrigin
@RequestMapping("/spu")
public class SpuController {


    @Autowired
    private SpuService spuService;

    @PutMapping(value = "/put/many")
    public Result putMany(@RequestBody Long[] ids){
        spuService.putMany(ids);
        return new Result(true,StatusCode.OK,"上架成功！");
    }


    @PutMapping(value = "/put/{id}")
    public Result put(@PathVariable("id")Long id){
        spuService.put(id);
        return new Result(true,StatusCode.OK,"上架成功！");
    }

    @PutMapping(value = "/pull/{id}")
    public Result pull(@PathVariable("id")Long id){
        spuService.pull(id);
        return new Result(true,StatusCode.OK,"下架成功！");
    }

    @PutMapping(value = "/audit/{id}")
    public Result audit(@PathVariable("id")Long id){
        spuService.audit(id);
        return new Result(true,StatusCode.OK,"审核通过！");
    }

    @GetMapping(value = "/goods/{id}")
    public Result<Goods> findGoodsById(@PathVariable("id")Long spuid){
        Goods goods=spuService.findGoodsById(spuid);
        return new Result<Goods>(true,StatusCode.OK,"查询Goods成功！",goods);
    }

    @PostMapping(value = "/save")
    public Result saveGoods(@RequestBody Goods goods){
        spuService.save(goods);
        return new Result(true,StatusCode.OK,"增加Goods成功！");
    }

    /**
     * 查询全部数据
     * @return
     */
    @GetMapping
    public Result findAll(){
        List<Spu> spuList = spuService.findAll();
        return new Result(true, StatusCode.OK,"查询成功1",spuList) ;
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result findById(@PathVariable String id){
        Spu spu = spuService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功2",spu);
    }


    /***
     * 新增数据
     * @param spu
     * @return
     */
    @PostMapping
    public Result add(@RequestBody Spu spu){
        spuService.add(spu);
        return new Result(true,StatusCode.OK,"添加成功");
    }


    /***
     * 修改数据
     * @param spu
     * @param id
     * @return
     */
    @PutMapping(value="/{id}")
    public Result update(@RequestBody Spu spu,@PathVariable("id")long id){
        spu.setId(id);
        spuService.update(spu);
        return new Result(true,StatusCode.OK,"修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param id
     * @return
     */
    @DeleteMapping(value = "/{id}" )
    public Result delete(@PathVariable String id){
        spuService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search" )
    public Result findList(@RequestParam Map searchMap){
        List<Spu> list = spuService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功3",list);
    }


    /***
     * 分页搜索实现
     * @param searchMap
     * @param page
     * @param size
     * @return
     */
    @GetMapping(value = "/search/{page}/{size}" )
    public Result findPage(@RequestParam Map searchMap, @PathVariable  int page, @PathVariable  int size){
        Page<Spu> pageList = spuService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功4",pageResult);
    }


}
