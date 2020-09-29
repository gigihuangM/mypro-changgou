package com.changgou.user.controller;
import com.alibaba.fastjson.JSON;
import com.changgou.user.service.UserService;
import com.changgou.user.pojo.User;
import com.github.pagehelper.Page;
import entity.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {


    @Autowired
    private UserService userService;

    @GetMapping(value = "/login")
    public Result login(String username, String password, HttpServletResponse response){
        //查询用户的信息
        User user = userService.findById(username);
        //对比密码(加密)

        if (BCrypt.checkpw(password,user.getPassword())){
            //创建用户令牌信息
            Map<String,Object> tokenMap=new HashMap<>();
            tokenMap.put("role","USER");
            tokenMap.put("success","SUCCESS");
            tokenMap.put("username",username);

            String token = JwtUtil.createJWT(UUID.randomUUID().toString(), JSON.toJSONString(tokenMap), null);
            System.out.println(token);
            //把令牌信息存入到cookie
            Cookie cookie=new Cookie("Authorization",token);
            cookie.setDomain("localhost");
            cookie.setPath("/");
            response.addCookie(cookie);
            //吧令牌作为参数给用户

            // 密码匹配成功
            return new Result(true,StatusCode.OK,"登陆成功",token);
        }
        //密码不正确 失败
        return new Result(false,StatusCode.LOGINERROR,"账号或者密码有误");
    }


    /**
     * 查询全部数据
     * @return
     */
    @PreAuthorize("hasAnyRole('admin')")
    @GetMapping
    public Result findAll(){
        List<User> userList = userService.findAll();
        return new Result(true, StatusCode.OK,"查询成功",userList) ;
    }

    /***
     * 根据ID查询数据
     * @param id
     * @return
     */
    @GetMapping({"/{id}","/load/{id}"})
    public Result findById(@PathVariable String id){
        User user = userService.findById(id);
        return new Result(true,StatusCode.OK,"查询成功",user);
    }


    /***
     * 新增数据
     * @param user
     * @return
     */
    @PostMapping
    public Result add(@RequestBody User user){
        userService.add(user);
        return new Result(true,StatusCode.OK,"添加成功");
    }


    /***
     * 修改数据
     * @param user
     * @param username
     * @return
     */
    @PutMapping(value="/{username}")
    public Result update(@RequestBody User user,@PathVariable String username){
        user.setUsername(username);
        userService.update(user);
        return new Result(true,StatusCode.OK,"修改成功");
    }


    /***
     * 根据ID删除品牌数据
     * @param username
     * @return
     */
    @DeleteMapping(value = "/{username}" )
    public Result delete(@PathVariable String username){
        userService.delete(username);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /***
     * 多条件搜索品牌数据
     * @param searchMap
     * @return
     */
    @GetMapping(value = "/search" )
    public Result findList(@RequestParam Map searchMap){
        List<User> list = userService.findList(searchMap);
        return new Result(true,StatusCode.OK,"查询成功",list);
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
        Page<User> pageList = userService.findPage(searchMap, page, size);
        PageResult pageResult=new PageResult(pageList.getTotal(),pageList.getResult());
        return new Result(true,StatusCode.OK,"查询成功",pageResult);
    }


}
