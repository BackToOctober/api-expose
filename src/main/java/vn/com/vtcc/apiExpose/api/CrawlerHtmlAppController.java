package vn.com.vtcc.apiExpose.api;

import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.com.vtcc.apiExpose.repository.JobRequestRepository;
import vn.com.vtcc.apiExpose.storage.elasticsearch.ESConnectorFactory;
import vn.com.vtcc.apiExpose.storage.elasticsearch.ESUtils;
import vn.com.vtcc.apiExpose.utils.HttpUtils;

import java.io.IOException;

@RestController
@RequestMapping(value = "/api")
public class CrawlerHtmlAppController {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerHtmlAppController.class);

    @Value(value = "${es.indices}")
    private String indicesRegex;

    private ESConnectorFactory esConnectorFactory;

    @Autowired
    public void setEsConnectorFactory(ESConnectorFactory esConnectorFactory) {
        this.esConnectorFactory = esConnectorFactory;
    }

    @PostMapping(value = "/query-html", headers = {"Content-Type=application/json; charset=UTF-8"})
    public ResponseEntity<?> queryData(@RequestBody String query) {
        JSONObject json = new JSONObject(query);
        String url = json.getString("url");
        RestHighLevelClient client = esConnectorFactory.createConnect();
        JSONObject result = null;
        try {
            logger.info("indicesRegex = " + indicesRegex);
            result = ESUtils.queryHtml(client, indicesRegex, url);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (result != null) {
            return new ResponseEntity<>(result.toString(4), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpUtils.genErrorJson("something is failure"), HttpStatus.OK);
    }
}
