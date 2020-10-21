package vn.com.vtcc.apiExpose.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.elasticsearch.client.RestHighLevelClient;
import org.json.JSONException;
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
import vn.com.vtcc.apiExpose.entity.dto.HtmlData;
import vn.com.vtcc.apiExpose.entity.response.RestCode;
import vn.com.vtcc.apiExpose.entity.response.DataRestResponse;
import vn.com.vtcc.apiExpose.storage.elasticsearch.ESConnectorFactory;
import vn.com.vtcc.apiExpose.storage.elasticsearch.ESUtils;
import vn.com.vtcc.apiExpose.utils.HttpUtils;
import vn.com.vtcc.apiExpose.utils.JacksonMapper;

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
    public ResponseEntity<DataRestResponse<?>> queryData(@RequestBody String query) {
        JSONObject json = null;
        try {
            json = new JSONObject(query);
        } catch (JSONException e) {
            return new ResponseEntity<>(new DataRestResponse<>(RestCode.ERROR_JSON_PARSING_CODE, "can't parse string to json", null), HttpStatus.OK);
        }
        if (json.has("url") && json.get("url") != JSONObject.NULL) {
            String url = json.getString("url");
            RestHighLevelClient client = esConnectorFactory.createConnect();
            JSONObject result = null;
            ResponseEntity responseEntity = null;
            try {
                result = ESUtils.queryHtml(client, indicesRegex, url);
            } catch (IOException e) {
                e.printStackTrace();
                responseEntity =  new ResponseEntity<>(new DataRestResponse<>(RestCode.ERROR_ELASTICSEARCH_CODE, RestCode.ERROR_ELASTICSEARCH_CODE_MSG, null), HttpStatus.SERVICE_UNAVAILABLE);
            } finally {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (responseEntity == null) {
                if (result != null) {
                    try {
                        responseEntity = new ResponseEntity<>(HttpUtils.makeSuccessResponseObject(JacksonMapper.parseToObject(result.toString(), HtmlData.class)), HttpStatus.OK);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                        responseEntity =  new ResponseEntity<>(new DataRestResponse<>(RestCode.ERROR_JSON_PARSING_CODE, "can't parse json to html data", null), HttpStatus.OK);
                    }
                } else {
                    responseEntity = new ResponseEntity<>(HttpUtils.makeSuccessResponse(null), HttpStatus.OK);
                }
            }
            return responseEntity;
        } else {
            return new ResponseEntity<>(new DataRestResponse<>(RestCode.ERROR_PARAMETER_MISSING_CODE, "can't found url parameter", null), HttpStatus.OK);
        }
    }
}
