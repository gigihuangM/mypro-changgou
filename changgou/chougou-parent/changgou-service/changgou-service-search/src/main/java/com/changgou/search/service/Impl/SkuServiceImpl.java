package com.changgou.search.service.Impl;

import com.alibaba.fastjson.JSON;
import com.changgou.search.dao.SkuEsMapper;
import com.changgou.goods.feign.SkuFeign;
import com.changgou.goods.pojo.Sku;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SkuService;
import entity.Result;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;

import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

@Service
public class SkuServiceImpl implements SkuService {

    @Autowired
    private SkuFeign skuFeign;

    @Autowired
    private SkuEsMapper skuEsMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 多条件搜索
     * @param searchMap
     * @return
     */
    @Override
    public Map<String, Object> search(Map<String, String> searchMap){

        //搜索条件构建对象，用于封装各种搜索条件
        NativeSearchQueryBuilder nativeSearchQueryBuilder = buildBasicQuery(searchMap);
        Map<String, Object> resultMap = searchList(nativeSearchQueryBuilder);

//        if(searchMap==null || StringUtils.isEmpty(searchMap.get("category"))){
//            List<String> categoryList = searchCategoryList(nativeSearchQueryBuilder);
//            resultMap.put("categoryList",categoryList);
//        }
//        if(searchMap==null || StringUtils.isEmpty(searchMap.get("brand"))) {
//            List<String> brandList = searchBrandList(nativeSearchQueryBuilder);
//            resultMap.put("brandList", brandList);
//        }
//        Map<String, Set<String>> specList = searchSpecList(nativeSearchQueryBuilder);
//        resultMap.put("specList",specList);
        Map<String, Object> groupMap = searchGroupList(nativeSearchQueryBuilder, searchMap);
        resultMap.putAll(groupMap);
        return resultMap;
    }

    private NativeSearchQueryBuilder buildBasicQuery(Map<String, String> searchMap) {
        NativeSearchQueryBuilder nativeSearchQueryBuilder=new NativeSearchQueryBuilder();

        BoolQueryBuilder boolQueryBuilder=QueryBuilders.boolQuery();

        if(searchMap!=null && searchMap.size()>0){
            String keywords = searchMap.get("keywords");
            if(!StringUtils.isEmpty(keywords)){
//                nativeSearchQueryBuilder.withQuery();
                boolQueryBuilder.must(QueryBuilders.queryStringQuery(keywords).field("name"));
            }
            //输了分类
            if(!StringUtils.isEmpty(searchMap.get("category"))){
//                nativeSearchQueryBuilder.withQuery();
                boolQueryBuilder.must(QueryBuilders.termQuery("categoryName",searchMap.get("category")));
            }
            //输了品牌
            if(!StringUtils.isEmpty(searchMap.get("brand"))){
//                nativeSearchQueryBuilder.withQuery();
                boolQueryBuilder.must(QueryBuilders.termQuery("brandName",searchMap.get("brand")));
            }
            //规格过滤
            for (Map.Entry<String, String> entry : searchMap.entrySet()) {
                String key = entry.getKey();
                if(key.startsWith("spec_")){
                    String value = entry.getValue();
                    boolQueryBuilder.must(QueryBuilders.termQuery("specMap."+key.substring(5),value));
                }
            }

            String price=searchMap.get("price");
            if(!StringUtils.isEmpty(price)){
                price=price.replace("元","").replace("以上","");
                String[] prices=price.split("-");
                if(prices!=null && prices.length>0){
                    boolQueryBuilder.must(QueryBuilders.rangeQuery("price").gt(Integer.parseInt(prices[0])));
                    if(prices.length==2){
                        boolQueryBuilder.must(QueryBuilders.rangeQuery("price").lte(Integer.parseInt(prices[1])));
                    }
                }
            }

            String sortField=searchMap.get("sortField"); //指定排序的域
            String sortRule=searchMap.get("sortRule"); //指定排序的规则
            if(!StringUtils.isEmpty(sortField)&& !StringUtils.isEmpty(sortRule)){
                nativeSearchQueryBuilder.withSort(new FieldSortBuilder(sortField).order(SortOrder.valueOf(sortRule)));
            }
        }



        //分页，用户如果不传分页参数 默认第一页
        Integer pageNum=coverterPage(searchMap);
        Integer size=10;

        nativeSearchQueryBuilder.withPageable(PageRequest.of(pageNum-1,size));

        //填充
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        return nativeSearchQueryBuilder;
    }

    public Integer coverterPage(Map<String,String> searchMap){
        if(searchMap!=null){
            String pageNum = searchMap.get("pageNum");
            try {
                return Integer.parseInt(pageNum);
            }catch (NumberFormatException e){

            }
        }
        return 1;
    }


    /**
     * 集合搜索
     * @param nativeSearchQueryBuilder
     * @return
     */

    private Map<String, Object> searchList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {

        HighlightBuilder.Field field=new HighlightBuilder.Field("name");
        //前缀
        field.preTags("<em style=\"color:red;\">");
        //后缀
        field.postTags("</em>");
        //碎片  关键词的长度
        field.fragmentOffset(100);

        //高亮显示
        nativeSearchQueryBuilder.withHighlightFields(field);

        //执行搜索.响应结果
//        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class);
        AggregatedPage<SkuInfo> page = elasticsearchTemplate.queryForPage(
                nativeSearchQueryBuilder.build(),
                SkuInfo.class ,
               new SearchResultMapper(){
                   @Override
                   public <T> AggregatedPage<T> mapResults(SearchResponse reponse, Class<T> aClass, Pageable pageable) {
                       List<T> list=new ArrayList<T>();
                       for (SearchHit hit : reponse.getHits()) {
                           //分析结果数据集，获取非高亮部分的数据
                           SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(),SkuInfo.class);
                           //分析结果数据集，获取高亮数据 只有某个域的高亮数据
                           HighlightField highlightField = hit.getHighlightFields().get("name");
                           if(highlightField!=null && highlightField.getFragments()!=null){
                               //费高亮数据指定的替换成高亮数据
                               Text[] fragments=highlightField.getFragments();
                               StringBuffer buffer=new StringBuffer();
                               for (Text fragment : fragments) {
                                   buffer.append(fragment.toString());
                               }
                               skuInfo.setName(buffer.toString());
                           }
                           list.add((T)skuInfo);
                           //将数据返回
                       }
                       return new AggregatedPageImpl<T>(list,pageable,reponse.getHits().getTotalHits());
                   }
               });

        //分组查询 分类集合
        //添加一个聚合操作




        //总记录数
        long totalElements = page.getTotalElements();
        //总页数
        int totalPages = page.getTotalPages();
        //分析数据

        //获取数据结果集
        List<SkuInfo> contents = page.getContent();

        //封装一个Map存储所有数据，并返回
        Map<String,Object> resultMap=new HashMap<>();

        resultMap.put("rows",contents);
        resultMap.put("total",totalElements);
        resultMap.put("totalPages",totalPages);


        NativeSearchQuery query=nativeSearchQueryBuilder.build();
        Pageable pageable = query.getPageable();
        int pageSize = pageable.getPageSize();
        int pageNumber = pageable.getPageNumber();
        resultMap.put("pageSize",pageSize);
        resultMap.put("pageNumber",pageNumber);
        return resultMap;
    }

    private Map<String,Object> searchGroupList(NativeSearchQueryBuilder nativeSearchQueryBuilder,Map<String,String> searchMap) {
        if(searchMap==null || StringUtils.isEmpty(searchMap.get("category"))) {
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
        }
        if(searchMap==null || StringUtils.isEmpty(searchMap.get("brand"))) {
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName"));
        }

            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword"));

        AggregatedPage<SkuInfo> aggregatedPage= elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(),SkuInfo.class);

        //定义一个Map，存储所有分组结果
        Map<String,Object> groupResult=new HashMap<>();
        //获取数据  获取的是聚合
        if(searchMap==null || StringUtils.isEmpty(searchMap.get("category"))) {
            StringTerms categoryTerms = aggregatedPage.getAggregations().get("skuCategory");
            List<String> categoryList = getGroupList(categoryTerms);
            groupResult.put("categoryList",categoryList);
        }
        if(searchMap==null || StringUtils.isEmpty(searchMap.get("brand"))) {
            StringTerms brandTerms = aggregatedPage.getAggregations().get("skuBrand");
            List<String> brandList = getGroupList(brandTerms);
            groupResult.put("brandList",brandList);
        }
        StringTerms specTerms = aggregatedPage.getAggregations().get("skuSpec");
        List<String> specList = getGroupList(specTerms);
        Map<String, Set<String>> specMap = putAllSpec(specList);
        groupResult.put("specList",specMap);


        return groupResult;
    }

    public List<String> getGroupList(StringTerms stringTerms){
        List<String> groupList=new ArrayList<>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            String feildName = bucket.getKeyAsString();//是其中的一个分类名字
            groupList.add(feildName);
        }
        return groupList;
    }

    /**
     * 品牌分类
     * @param nativeSearchQueryBuilder
     * @return
     */

    private List<String> searchBrandList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuBrand").field("brandName"));
        AggregatedPage<SkuInfo> aggregatedPage= elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(),SkuInfo.class);


        //获取数据  获取的是聚合
        StringTerms stringTerms = aggregatedPage.getAggregations().get("skuBrand");
        List<String> brandList=new ArrayList<>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();//是其中的一个分类名字
            brandList.add(keyAsString);
        }
        return brandList;
    }

    /**
     * 分类分组查询
     * @param nativeSearchQueryBuilder
     * @return
     */
    private List<String> searchCategoryList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuCategory").field("categoryName"));
        AggregatedPage<SkuInfo> aggregatedPage= elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(),SkuInfo.class);


        //获取数据  获取的是聚合
        StringTerms stringTerms = aggregatedPage.getAggregations().get("skuCategory");
        List<String> categoryList=new ArrayList<>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            String keyAsString = bucket.getKeyAsString();//是其中的一个分类名字
            categoryList.add(keyAsString);
        }
        return categoryList;
    }

    /**
     * 规格
     * @param nativeSearchQueryBuilder
     * @return
     */
    private Map<String, Set<String>> searchSpecList(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("skuSpec").field("spec.keyword").size(1000));
        AggregatedPage<SkuInfo> aggregatedPage= elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(),SkuInfo.class);


        //获取数据  获取的是聚合
        StringTerms stringTerms = aggregatedPage.getAggregations().get("skuSpec");
        List<String> specList=new ArrayList<>();
        for (StringTerms.Bucket bucket : stringTerms.getBuckets()) {
            String specName = bucket.getKeyAsString();//是其中的一个分类名字
            specList.add(specName);
        }
        Map<String, Set<String>> allSpec = putAllSpec(specList);
        return allSpec;
    }

    /**
     * 规格汇总
     * @param specList
     * @return
     */
    private Map<String, Set<String>> putAllSpec(List<String> specList) {
        Map<String, Set<String>> allSpec=new HashMap<>();
        for (String spec : specList) {
            Map<String,String> specMap = JSON.parseObject(spec, Map.class);
            for (Map.Entry<String, String> entry : specMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Set<String> specSet = allSpec.get(key);
                if(specSet==null){
                 specSet=new HashSet<>();
                }
                specSet.add(value);
                allSpec.put(key,specSet);
            }
        }
        return allSpec;
    }


    @Override
    public void importDate() {
        //Fegin调用，查询List<Sku>
        Result<List<Sku>> skuResult = skuFeign.findAll();

        List<SkuInfo> skuInfoList= JSON.parseArray(JSON.toJSONString(skuResult.getData()),SkuInfo.class);

        for (SkuInfo skuInfo : skuInfoList) {
            //获取spec
            Map<String,Object> specMap=JSON.parseObject(skuInfo.getSpec(),Map.class);
            //需要生成动态的域
            skuInfo.setSpecMap(specMap);
        }

        skuEsMapper.saveAll(skuInfoList);

    }
}
