package vn.com.vtcc.apiExpose.storage.elasticsearch;

import org.apache.derby.iapi.store.raw.log.LogFactory;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.admin.indices.alias.Alias;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public class ESUtils implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(ESUtils.class);

    public static void putData(RestHighLevelClient client, String index, String data) throws IOException {
        IndexRequest request = new IndexRequest(index);
        request.source(data, XContentType.JSON);
        request.timeout(TimeValue.timeValueSeconds(2));
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
    }

    public static void putData(RestHighLevelClient client, String index, String docIndex, String data) throws IOException {
        IndexRequest request = new IndexRequest(index);
        request.source(data, XContentType.JSON);
        request.index(docIndex);
        request.timeout(TimeValue.timeValueSeconds(2));
        IndexResponse indexResponse = client.index(request, RequestOptions.DEFAULT);
    }

    public static boolean deleteIndex(RestHighLevelClient client, String index) throws IOException {
        DeleteIndexRequest request = new DeleteIndexRequest(index);
        request.timeout(TimeValue.timeValueMinutes(2));
        request.masterNodeTimeout(TimeValue.timeValueMinutes(1));
        AcknowledgedResponse deleteIndexResponse = client.indices().delete(request, RequestOptions.DEFAULT);
        return true;
    }

    public static boolean existsIndex(RestHighLevelClient client, String index) throws IOException {
        GetIndexRequest request = new GetIndexRequest(index);
        request.local(false)
                .humanReadable(true)
                .includeDefaults(false);
        boolean result = client.indices().exists(request, RequestOptions.DEFAULT);
        return result;
    }

    public static SearchResponse queryData(RestHighLevelClient client, SearchRequest searchRequest) throws IOException {
        return client.search(searchRequest, RequestOptions.DEFAULT);
    }

    public static JSONObject queryHtml(RestHighLevelClient client, String indicesRegex,  String url) throws IOException {
        TermQueryBuilder termQueryBuilder = new TermQueryBuilder("url", url);
        SearchSourceBuilder searchSourceBuilder = SearchSourceBuilder.searchSource()
                .sort("created_time", SortOrder.DESC).query(termQueryBuilder);
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(indicesRegex).source(searchSourceBuilder);
        logger.info(" >> query : " + searchRequest);
        SearchResponse searchResponse = queryData(client, searchRequest);
        Iterator<SearchHit> ite  = searchResponse.getHits().iterator();
        while (ite.hasNext()) {
            return new JSONObject(ite.next().getSourceAsString());
        }
        return null;
    }

    public static class ESIndexBuilder {

        private final Logger logger = LoggerFactory.getLogger(ESIndexBuilder.class);
        private String esIndex;
        private int indexNumberOfShards = 1;
        private int indexNumberOfReplicas = 1;
        private Map<String, Object> mapping;
        private final String alias = null;
        private RestHighLevelClient client;

        public static ESIndexBuilder getBuilder() {
            ESIndexBuilder builder = new ESIndexBuilder();
            return builder;
        }

        public ESIndexBuilder setIndex(String esIndex) {
            this.esIndex = esIndex;
            return this;
        }

        public ESIndexBuilder setIndexNumberOfShards(int indexNumberOfShards) {
            this.indexNumberOfShards = indexNumberOfShards;
            return this;
        }

        public ESIndexBuilder setIndexNumberOfReplicas(int indexNumberOfReplicas) {
            this.indexNumberOfReplicas = indexNumberOfReplicas;
            return this;
        }

        public ESIndexBuilder setMapping(Map<String, Object> mapping) {
            this.mapping = mapping;
            return this;
        }

        public ESIndexBuilder setClient(RestHighLevelClient client) {
            this.client = client;
            return this;
        }

        public boolean create() throws IOException {
            CreateIndexRequest request = new CreateIndexRequest(this.esIndex);
            request.settings(Settings.builder()
                    .put("index.number_of_shards", this.indexNumberOfShards)
                    .put("index.number_of_replicas", this.indexNumberOfReplicas)
            );
            if (this.mapping != null) {
                request.mapping(this.mapping);
            }
            if (this.alias != null) {
                request.alias(new Alias(this.alias));
            }

            // [WARN]:TODO: acknowledged and shardsAcknowledged checking were ignored
            logger.warn("acknowledged and shardsAcknowledged checking were ignored");
            CreateIndexResponse createIndexResponse = this.client.indices().create(request, RequestOptions.DEFAULT);
            return true;
        }
    }

}
