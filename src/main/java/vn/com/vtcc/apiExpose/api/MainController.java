package vn.com.vtcc.apiExpose.api;

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
import vn.com.vtcc.apiExpose.entity.JobRequest;
import vn.com.vtcc.apiExpose.repository.JobRequestRepository;
import vn.com.vtcc.apiExpose.utils.FileUtils;
import vn.com.vtcc.apiExpose.utils.HttpUtils;
import vn.com.vtcc.apiExpose.utils.transform.QueryParsing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api")
public class MainController {

    private static final Logger logger = LoggerFactory.getLogger(MainController.class);

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
                return FileUtils.readJsonFile(fileName).get("article").toString();
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("list_article", new JSONArray(listArticle));
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.OK);
    }

    @GetMapping(value = "/metadata-article/{article}")
    public ResponseEntity<?> getMetadataArticle(@PathVariable(value = "article") String article) {
        String filePath = Paths.get(METADATA_FOLDER, article + ".json").toString();
        try {
            JSONObject jsonObject = FileUtils.readJsonFile(filePath);
            return new ResponseEntity<>(jsonObject.get("metadata").toString(), HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpUtils.genErrorJson("not found").toString(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/check-status/{requestId}")
    public ResponseEntity<?> checkStatus(@PathVariable(value = "requestId") String requestId) {
        Optional<JobRequest> result = jobRequestRepository.findById(requestId);
        if (!result.isPresent()) {
            return new ResponseEntity<>(HttpUtils.genErrorJson("not found").toString(), HttpStatus.OK);
        }
        return new ResponseEntity<>(result.get(), HttpStatus.OK);
    }

    @PostMapping(value = "/query-data", headers = {"Content-Type=application/json; charset=UTF-8"})
    public ResponseEntity<?> queryData(@RequestBody String query) {
        if (QueryParsing.validate(query, METADATA_FOLDER)) {
            long timeStamp = System.currentTimeMillis();
            JobRequest job = new JobRequest(query, JobState.WAITING(), timeStamp, timeStamp);
            this.jobRequestRepository.save(job);
            String path = Paths.get(OUTPUT_RESULT_FOLDER, job.getId()).toString();
            return new ResponseEntity<>(HttpUtils.genSuccessJson(path).toString(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpUtils.genErrorJson("query is not invalid").toString(), HttpStatus.OK);
        }
    }

    @GetMapping(value = "/download/{requestId}")
    public ResponseEntity<?> downloadFile(@PathVariable(value = "requestId") String requestId) {
        Path path = Paths.get(OUTPUT_RESULT_FOLDER, requestId);
        File file = new File(path.toString());

        if (!file.exists()) {
            return new ResponseEntity<>(HttpUtils.genErrorJson("file not found").toString(), HttpStatus.OK);
        }

        ByteArrayResource resource = null;
        try {
            resource = new ByteArrayResource(Files.readAllBytes(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + requestId);
        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

}
