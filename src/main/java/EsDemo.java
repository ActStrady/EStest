import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;

public class EsDemo {
    // 获取client
    public Client client() throws IOException {
        TransportClient client = new PreBuiltTransportClient(Settings.EMPTY)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName
                        ("localhost"), 9300));
        return client;
    }

    // 添加数据
    public void index(String json, int i) throws IOException {
        Client client = client();
        IndexResponse indexResponse = client.prepareIndex("student", "good", i + "").setSource
                (json).get();
    }

    // 获取数据
    public GetResponse get() throws IOException {
        Client client = client();
        GetResponse getFields = client.prepareGet("student", "good", "1").setOperationThreaded
                (true).get();
        return getFields;
    }

    // 删除数据
    public void delete() throws IOException {
        Client client = client();
        client.prepareDelete("student", "good", "2").get();
    }

    @Test
    public void test() throws IOException {
        Client client = client();
       /* BulkByScrollResponse bulkByScrollResponse = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(client).filter(QueryBuilders.matchQuery("4", "4")).source("4")
                .get();
        long deleted = bulkByScrollResponse.getDeleted();
        System.out.println(deleted);*/

        SearchResponse searchResponse = client.prepareSearch("student")
                .setTypes("good")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setQuery(QueryBuilders.termQuery("user", "50"))
                .setPostFilter(QueryBuilders.rangeQuery("user").from(1).to(50))
                .setFrom(0)
                .setSize(60)
                .setExplain(true).get();
        searchResponse.getHits();
        System.out.println(searchResponse);
    }
}
