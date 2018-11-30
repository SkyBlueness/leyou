package com.leyou.search.service;

import com.leyou.item.pojo.Brand;
import com.leyou.item.pojo.Category;
import com.leyou.item.pojo.SpecParam;
import com.leyou.search.client.CategoryClient;
import com.leyou.search.client.GoodsClient;
import com.leyou.search.client.SpecifcationClient;
import com.leyou.search.pojo.Goods;
import com.leyou.search.pojo.SearchRequest;
import com.leyou.search.pojo.SearchResult;
import com.leyou.search.repository.GoodsRepository;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.LongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service
public class IndexService {

    @Autowired
    private GoodsRepository repository;

    @Autowired
    private CategoryClient categoryClient;

    @Autowired
    private GoodsClient goodsClient;

    @Autowired
    ElasticsearchTemplate template;

    @Autowired
    private SpecifcationClient specifcationClient;

    public SearchResult getGoodsPage(SearchRequest searchRequest) {
        String key = searchRequest.getKey();
        // 判断是否有搜索条件，如果没有，直接返回null。不允许搜索全部商品
        if (StringUtils.isBlank(key)) {
            return null;
        }
        // 构建查询条件
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        MatchQueryBuilder basicQuery = QueryBuilders.matchQuery("all", key).operator(Operator.AND);
        queryBuilder.withQuery(basicQuery);
        // 通过sourceFilter设置返回的结果字段,我们只需要id、skus、subTitle
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id", "skus", "subTitle"}, null));

        // 分页
        searchWithPageAndSort(queryBuilder, searchRequest);

        // 聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brands").field("cid3"));
        queryBuilder.addAggregation(AggregationBuilders.terms("category").field("brandId"));

        // 执行查询获取结果集
        AggregatedPage<Goods> goodsPage = (AggregatedPage<Goods>) this.repository.search(queryBuilder.build());

        // 获取聚合结果集
        // 商品分类的聚合结果
        List<Category> categories =
                getCategorys(goodsPage.getAggregation("brands"));
        // 品牌的聚合结果
        List<Brand> brands = getBrands(goodsPage.getAggregation("category"));

        // 根据商品分类判断是否需要聚合
        List<Map<String, Object>> specs = new ArrayList<>();
        if (categories.size() == 1) {
            // 如果商品分类只有一个才进行聚合，并根据分类与基本查询条件聚合
            specs =getSpec(categories.get(0).getId(), basicQuery);
        }

        return new SearchResult(goodsPage.getTotalElements(), goodsPage.getTotalPages(), goodsPage.getContent(), categories, brands, specs);
    }

    // 构建基本查询条件
    private void searchWithPageAndSort(NativeSearchQueryBuilder queryBuilder, SearchRequest request) {
        // 准备分页参数
        int page = request.getPage();
        int size = request.getSize();

        // 1、分页
        queryBuilder.withPageable(PageRequest.of(page - 1, size));

    }

    /**
     * 聚合出规格参数
     *
     * @param cid
     * @param query
     * @return
     */
    private List<Map<String, Object>> getSpec(Long cid, QueryBuilder query) {
        try {
            // 不管是全局参数还是sku参数，只要是搜索参数，都根据分类id查询出来
            List<SpecParam> params = this.specifcationClient.getParam(null, cid, true);
            List<Map<String, Object>> specs = new ArrayList<>();

            NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
            queryBuilder.withQuery(query);

            // 聚合规格参数
            params.forEach(p -> {
                String key = p.getName();
                queryBuilder.addAggregation(AggregationBuilders.terms(key).field("specs." + key + ".keyword"));

            });

            // 查询
            Map<String, Aggregation> aggs = this.template.query(queryBuilder.build(),
                    SearchResponse::getAggregations).asMap();

            // 解析聚合结果
            params.forEach(param -> {
                Map<String, Object> spec = new HashMap<>();
                String key = param.getName();
                spec.put("k", key);
                StringTerms terms = (StringTerms) aggs.get(key);
                spec.put("options", terms.getBuckets().stream().map(StringTerms.Bucket::getKeyAsString));
                specs.add(spec);
            });

            return specs;
        } catch (
                Exception e)

        {
            e.printStackTrace();
            return null;
        }

    }

    private List<Brand> getBrands(Aggregation brand) {
        try {
            List<Long>ids = new ArrayList<>();
            LongTerms brandAgg = (LongTerms) brand;
            for (LongTerms.Bucket bucket:brandAgg.getBuckets()){
                ids.add(bucket.getKeyAsNumber().longValue());
            }
            List<Brand>brands = goodsClient.getBrandsByIds(ids);
            return brands;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }

    private List<Category> getCategorys(Aggregation category) {
        try {
            List<Category>categories = new ArrayList<>();
            LongTerms categoryAgg = (LongTerms) category;
            List<Long>cids = new ArrayList<>();
            for (LongTerms.Bucket bucket:categoryAgg.getBuckets()){
                cids.add(bucket.getKeyAsNumber().longValue());
            }
            List<Category> categories1 = categoryClient.findCategoryByIds(cids);
            for(int i = 0 ; i<categories1.size();i++){
                Category c = new Category();
                c.setId((categories1.get(i).getId()));
                c.setName(categories1.get(i).getName());
                categories.add(c);
            }
            return categories;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
