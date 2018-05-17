package saju.livy.spring.controler;

import org.apache.livy.LivyClient;

import saju.livy.client.service.CacheJob;
import saju.livy.client.service.LivyHiveJob;
import saju.livy.client.service.LoadCSV;
import saju.livy.client.service.PiJob1;
import saju.livy.client.service.SaleSummary;
import saju.livy.spring.entity.CacheDetails;
import saju.livy.spring.entity.Response;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;


@RestController
@RequestMapping("api")
public class LivyControler {

    @Value("${livyUrl}")
    private String livyUrl;

    @Value("${jarPath}")
    private String jarPath;

    @Autowired
    LivyClient client;


    private static Logger logger = Logger.getLogger(LivyControler.class);

    @RequestMapping("/{pi}")
    public ResponseEntity<Object> findPi(@PathVariable("pi") Integer samples) throws IOException,URISyntaxException{
        logger.info("Before submitting spark job...");
        Double res=null;
        System.err.printf("Running PiJob with %d samples...\n", samples);
        try {
            res = client.submit(new PiJob1(samples)).get();
        }catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }

        logger.info("Pi is roughly: " + res);

        return new ResponseEntity<>(res, HttpStatus.OK);
    }

    @RequestMapping("/sales/{plan}")
    public ResponseEntity<Response> RESales(@PathVariable("plan") String plan) {
        logger.info("Before submitting Load Plan job...");
        List<Map<String,Object>> result = new ArrayList<>();
        Response resp = new Response();
        resp.setCode(200);
        resp.setMessage("Request completed successfully");
        try {
            result = client.submit(new LoadCSV(plan)).get();
            //result = client.run(new LoadCSV(plan)).get(5, TimeUnit.MINUTES);
        }catch (ExecutionException | InterruptedException 
        	//		| TimeoutException 
        e){
            e.printStackTrace();
            resp.setCode(200);
            resp.setMessage(e.getMessage());
        }
        resp.setResult(result);
        logger.info("Before returning result " + result);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping("/summary/{zip}")
    public ResponseEntity<Response> RESummary(@PathVariable("zip") String zipCd) {
        logger.info("Before submitting Summary job...");
        List<Map<String,Object>> result = new ArrayList<>();
        Response resp = new Response();
        resp.setCode(200);
        resp.setMessage("Request completed successfully");
        try {
            //result = client.submit(new LivyHiveJob(brand)).get();
            result = client.run(new SaleSummary(zipCd)).get(5, TimeUnit.MINUTES);
        }catch (ExecutionException | InterruptedException | TimeoutException e){
            e.printStackTrace();
            resp.setCode(500);
            resp.setMessage(e.getMessage());
        }
        resp.setResult(result);
        logger.info("Before returning result " + result);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }
    @RequestMapping("/sales")
    public ResponseEntity<Response> sales() {
        logger.info("Before submitting sales job...");
        List<Map<String,Object>> result = new ArrayList<>();
        Response resp = new Response();
        resp.setCode(200);
        resp.setMessage("Request completed successfully");
        try {
            result = client.run(new LivyHiveJob()).get(5, TimeUnit.MINUTES);
        }catch (ExecutionException | InterruptedException | TimeoutException e){
            e.printStackTrace();
            resp.setCode(500);
            resp.setMessage(e.getMessage());
        }
        resp.setResult(result);
        logger.info("Before returning result " + result);
        return new ResponseEntity<>(resp,HttpStatus.OK);
    }

    @RequestMapping(value="/cache",method = RequestMethod.POST)
    public ResponseEntity<String> cache(@RequestBody CacheDetails cacheDetails){
        logger.info("Before submitting cache job...");
        String result="";
        try {
            result = client.submit(new CacheJob(cacheDetails)).get();
        }catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
        logger.info("Before returning result " + result);
        return new ResponseEntity<>(result,HttpStatus.OK);

    }



    @PreDestroy
    public void shutdownHook(){
        logger.info("Inside shutdownHook...");
        client.stop(true);
    }
}
