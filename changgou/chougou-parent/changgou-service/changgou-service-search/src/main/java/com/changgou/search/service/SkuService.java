package com.changgou.search.service;

import java.util.Map;

public interface SkuService {

    Map<String,Object> search(Map<String,String> searchMap) throws Exception;


    /**
     * 导入数据
     */
    void importDate();
}
