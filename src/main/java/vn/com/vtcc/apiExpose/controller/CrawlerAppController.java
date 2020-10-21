package vn.com.vtcc.apiExpose.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.gson.JsonObject;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.com.vtcc.apiExpose.app.JobState;
import vn.com.vtcc.apiExpose.entity.dto.ArticleList;
import vn.com.vtcc.apiExpose.entity.dto.ArticleMetadata;
import vn.com.vtcc.apiExpose.entity.dto.JobChecking;
import vn.com.vtcc.apiExpose.entity.model.JobRequest;
import vn.com.vtcc.apiExpose.entity.response.DataRestResponse;
import vn.com.vtcc.apiExpose.entity.response.RestCode;
import vn.com.vtcc.apiExpose.repository.JobRequestRepository;
import vn.com.vtcc.apiExpose.utils.FileUtils;
import vn.com.vtcc.apiExpose.utils.HttpUtils;
import vn.com.vtcc.apiExpose.utils.JacksonMapper;
import vn.com.vtcc.apiExpose.utils.transform.QueryParsing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping(value = "/api")
public class CrawlerAppController {

    private static final Logger logger = LoggerFactory.getLogger(CrawlerAppController.class);

    private static final String METADATA_FOLDER = "config/metadata/";
    private static final String OUTPUT_RESULT_FOLDER = "output/result";

    private JobRequestRepository jobRequestRepository;

    @Autowired
    public void setJobRequestRepository(JobRequestRepository jobRequestRepository) {
        this.jobRequestRepository = jobRequestRepository;
    }

    @RequestMapping(value = "/")
    public String index() {
        return "Fuck you bug!, get out of my code";
    }

    @RequestMapping(value = "/list-article")
    public ResponseEntity<?> listArticle() {
        List<String> listFileName = FileUtils.listFiles(METADATA_FOLDER);
        List<String> listArticle = listFileName.stream().map(fileName -> {
            try {
                return FileUtils.readJsonFile(fileName).getString("article");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list_article", new JSONArray(listArticle));
        try {
            return new ResponseEntity<>(HttpUtils.makeSuccessResponseObject(JacksonMapper.parseToObject(jsonObject.toString(), ArticleList.class)), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new DataRestResponse<>(RestCode.ERROR_CODE, "can't parse json to article data", null), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/metadata-article/{article}")
    public ResponseEntity<?> getMetadataArticle(@PathVariable(value = "article") String article) {
        String filePath = Paths.get(METADATA_FOLDER, article + ".json").toString();
        try {
            JSONObject jsonObject = FileUtils.readJsonFile(filePath);
            return new ResponseEntity<>(HttpUtils.makeSuccessResponseObject(JacksonMapper.parseToObject(jsonObject.toString(4), ArticleMetadata.class)), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpUtils.makeErrorResponse(), HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping(value = "/check-status/{requestId}")
    public ResponseEntity<?> checkStatus(@PathVariable(value = "requestId") String requestId) {
        Optional<JobRequest> result = jobRequestRepository.findById(requestId);
        if (!result.isPresent()) {
            return new ResponseEntity<>(new DataRestResponse<>(RestCode.ERROR_JOB_ID_NOT_FOUND_CODE, RestCode.ERROR_JOB_ID_NOT_FOUND_CODE_MSG, null), HttpStatus.OK);
        }
        JobChecking jobChecking = new JobChecking();
        jobChecking.setId(result.get().getId());
        jobChecking.setJobState(result.get().getJobState());
        if (result.get().getFiles() != null) {
            jobChecking.setFiles(Arrays.asList(result.get().getFiles().split(",")));
        } else {
            jobChecking.setFiles(new ArrayList<>());
        }
        return new ResponseEntity<>(HttpUtils.makeSuccessResponseObject(jobChecking), HttpStatus.OK);
    }

    @PostMapping(value = "/query-data", headers = {"Content-Type=application/json; charset=UTF-8"})
    public ResponseEntity<?> queryData(@RequestBody String query) {
        if (QueryParsing.validate(query, METADATA_FOLDER)) {
            long timeStamp = System.currentTimeMillis();
            JSONObject json = new JSONObject(query);
            JobRequest job = new JobRequest(query, QueryParsing.parse(json.getJSONObject("query").toString(), METADATA_FOLDER), JobState.WAITING(), timeStamp, timeStamp);
            this.jobRequestRepository.save(job);
            String path = Paths.get(OUTPUT_RESULT_FOLDER, job.getId()).toString();
            return new ResponseEntity<>(HttpUtils.makeSuccessResponse("query is ok"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new DataRestResponse<>(RestCode.ERROR_QUERY_NOT_VALID_CODE, RestCode.ERROR_QUERY_NOT_VALID_CODE_MSG, null), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/download/{requestId}/{fileName}")
    public ResponseEntity<?> downloadFile(@PathVariable(value = "requestId") String requestId,
                                          @PathVariable(value = "fileName") String fileName) {
        Path path = Paths.get(OUTPUT_RESULT_FOLDER, requestId, fileName);
        File file = new File(path.toString());

        if (!file.exists()) {
            return new ResponseEntity<>(new DataRestResponse<>(RestCode.ERROR_FILE_NOT_FOUND_CODE, RestCode.ERROR_JOB_ID_NOT_FOUND_CODE_MSG, null), HttpStatus.OK);
        }

        ByteArrayResource resource = null;
        try {
            resource = new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new DataRestResponse<>(RestCode.ERROR_CODE, RestCode.ERROR_CODE_MSG, null), HttpStatus.SERVICE_UNAVAILABLE);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
