package com.changgou.goods.contronller;

import com.changgou.goods.pojo.Album;
import com.changgou.goods.service.AlbumService;
import com.github.pagehelper.PageInfo;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/album")
@CrossOrigin
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    @PostMapping(value = "/search/{page}/{size}")
    public Result<PageInfo<Album>> findPage(@RequestBody Album album,
                                            @PathVariable("page")Integer page,
                                            @PathVariable("size")Integer size){
        PageInfo<Album> page1 = albumService.findPage(album, page, size);
        return new Result<PageInfo<Album>>(true,StatusCode.OK,"查询成功",page1);
    }

    @GetMapping(value = "/search/{page}/{size}")
    public Result<PageInfo<Album>> findPage(@PathVariable("page")Integer page,
                                            @PathVariable("size")Integer size){
        PageInfo<Album> page1 = albumService.findPage(page, size);
        return new Result<PageInfo<Album>>(true,StatusCode.OK,"查询成功",page1);
    }

   @PostMapping(value = "/search")
    public Result<List<Album>> findList(@RequestBody Album album){
       List<Album> list = albumService.findList(album);
       return new Result<List<Album>>(true,StatusCode.OK,"查询成功",list);
    }

    @DeleteMapping(value = "/{id}")
    public Result delete(@PathVariable("id")long id){
        albumService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    @PutMapping(value = "/{id}")
    public Result update(@PathVariable("id")long id,@RequestBody Album album){
        album.setId(id);
        albumService.update(album);
        return new Result(true,StatusCode.OK,"修改成功");
    }
    @PostMapping
    public Result add(@RequestBody Album album){
        albumService.add(album);
        return new Result(true,StatusCode.OK,"增加成功");
    }

    @GetMapping("/{id}")
    public Result<Album> findById(@PathVariable("id")long id){
        Album album = albumService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",album);
    }
    @GetMapping
    public Result<List<Album>> findAll(){
        List<Album> list = albumService.findAll();
        return new Result<List<Album>>(true,StatusCode.OK,"查询成功",list);
    }

}
