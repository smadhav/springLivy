package saju.livy.client;

import saju.livy.client.service.PiJob1;
import saju.livy.client.service.SaleSummary;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.livy.LivyClient;
import org.apache.log4j.Logger;

import saju.livy.client.config.LivyConfig;
import saju.livy.client.service.LoadCSV;;

public class Application {
	private static Logger logger = Logger.getLogger(Application.class);
    public static void main(String[] args){
    	LivyClient lvClient = null;
    	LivyConfig lvyConf = new LivyConfig();
    	try {
			lvClient = lvyConf.client();
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	logger.info("Before submitting spark job...");
        Integer samples =5;
        Double res = 0.0;
        System.err.printf("Running PiJob with %d samples...\n", samples);
        try {
            res = lvClient.submit(new PiJob1(samples)).get();
        }catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }

        logger.info("Pi is roughly: " + res);
    	
        List<Map<String,Object>> result = new ArrayList<>();
        try {
			result = lvClient.submit(new LoadCSV("1002")).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        logger.info("After  submitting spark job LoadCSV...");
        result.forEach(mapItem -> mapItem.forEach((k, v) -> System.out.println((k + ":" + v))));
        
        //get Summary
        logger.info("Before submitting spark job.Summary..");
        try {
			result = lvClient.run(new LoadCSV("1001")).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        result.forEach(mapItem -> mapItem.forEach((k, v) -> System.out.println((k + ":" + v))));
       
        
      //get Summary
        logger.info("Before submitting spark job.Summary..");
        try {
			result = lvClient.run(new SaleSummary("95842")).get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        result.forEach(mapItem -> mapItem.forEach((k, v) -> System.out.println((k + ":" + v))));
       
        //List<Map<String,Object>> result= new ArrayList<>();
    }
    
    
   
}
