package com.changgou.search.controller;


import com.changgou.search.feign.SkuFeign;
import com.changgou.search.pojo.SkuInfo;
import entity.Page;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping(value = "/search")
public class SkuController {

    @Autowired
    private SkuFeign skuFeign;
    /**
     *实现搜索调用
     */
    @GetMapping(value = "/list")
    public String search(@RequestParam(required = false) Map<String,String> searchMap, Model model)throws Exception{
        Map<String,Object> resultMap = skuFeign.search(searchMap);
        model.addAttribute("result",resultMap);

        Page<SkuInfo> pageInfo=new Page<SkuInfo>(
              Long.parseLong(resultMap.get("total").toString()),
                Integer.parseInt(resultMap.get("pageNumber").toString())+1 ,
                Integer.parseInt(resultMap.get("pageSize").toString())
        );
        model.addAttribute("pageInfo",pageInfo);

        model.addAttribute("searchMap",searchMap);
        String[] url=getUrl(searchMap);
        model.addAttribute("url",url[0]);
        model.addAttribute("sorturl",url[1]);
        return "search";
    }

    /**
     * 获取用户每次请求的地址
     * 页面需要在这次请求上面添加额外的搜索条件
     */

    public String[] getUrl(Map<String,String> searchMap){
        String url ="/search/list";
        String sortUrl="/search/list";
        if(searchMap!=null && searchMap.size()>0){
            url+="?";
            sortUrl+="?";
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                String key = entry.getKey();

                if(key.equalsIgnoreCase("pageNum")){
                    continue;
                }
                String value = entry.getValue();
                url+=key+"="+value+"&";

                if(key.equalsIgnoreCase("sortField") || key.equalsIgnoreCase("sortRule")){
                    continue;
                }
                sortUrl+=key+"="+value+"&";
            }
            url=url.substring(0,url.length()-1);
            sortUrl=sortUrl.substring(0,sortUrl.length()-1);

        }
        return new String[]{url,sortUrl} ;
    }

}
