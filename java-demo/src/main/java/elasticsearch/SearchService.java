package elasticsearch;

import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SearchService {
    public RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http"),
                    new HttpHost("localhost", 9201, "http")));


    void searchResumes(String keyword) throws IOException {
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        queryBuilder.must().add(QueryBuilders.matchQuery("all", keyword));
        queryBuilder.filter().add(QueryBuilders.termQuery("education", "本科"));
        queryBuilder.filter().add(QueryBuilders.rangeQuery("age").gte(18).lte(20));

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        sourceBuilder.query(queryBuilder);
        sourceBuilder.from(0);
        sourceBuilder.size(5);

        System.out.println(sourceBuilder.toString());
        sourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));

        SearchRequest searchRequest = new SearchRequest();
        searchRequest.source(sourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest);
        long totalHits = searchResponse.getHits().getTotalHits();
        System.out.println(totalHits);

    }

    boolean indexDoc(String index, String type, String id) {
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("user", "kimchy");
        jsonMap.put("postDate", new Date());
        jsonMap.put("age", 18);
        jsonMap.put("message", "trying out Elasticsearch");
        IndexRequest request = new IndexRequest(index, type, id)
                .source(jsonMap);
        ActionListener<IndexResponse> listener = new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                String index = indexResponse.getIndex();
                String type = indexResponse.getType();
                String id = indexResponse.getId();
                long version = indexResponse.getVersion();
                if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                    System.out.printf("Created");
                } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                    System.out.println("Updated");
                }
            }

            @Override
            public void onFailure(Exception e) {

            }
        };
        client.indexAsync(request, listener);
        return true;
    }

    public static void main(String[] args) throws IOException {
        SearchService searchService = new SearchService();
//        searchService.indexDoc("test", "doc", "id1");
        searchService.searchResumes("kimchy");

    }
}
