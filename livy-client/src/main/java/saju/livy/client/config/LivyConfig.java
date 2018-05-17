package saju.livy.client.config;

import org.apache.livy.LivyClient;
import org.apache.livy.LivyClientBuilder;
import org.apache.log4j.Logger;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;



public  class LivyConfig {

    Properties properties  = loadProperties();
    
    private String portNum = properties.getProperty("server.port");
    private String jarPath = properties.getProperty("jarPath");
    private String livyUrl =properties.getProperty("livyUrl");
    

    private static Logger logger = Logger.getLogger(LivyConfig.class);
    
  
    public LivyClient client() throws URISyntaxException,IOException {
        URI uri = new URI(livyUrl);
        Map<String,String> config = new HashMap<>();
        /*config.put("spark.app.name","livy-poc");
        config.put("connection.timeout", "180s");
        config.put("spark.driver.memory", "4g");
        config.put("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        config.put("spark.executor.memory", "15g");
        config.put("spark.executor.cores","10");
        //spark.executor.cores concurrent task that and executor can run thread count
        config.put("spark.executor.instances","3");
		*/
        //config.put("spark.dynamicAllocation.enabled","true");
        //config.put("spark.dynamicAllocation.minExecutors","2");
        //config.put("spark.dynamicAllocation.maxExecutors","5");
        //config.put("spark.dynamicAllocation.initialExecutors","2");
        //config.put("spark.dynamicAllocation.schedulerBacklogTimeout","300s");
        //config.put("spark.dynamicAllocation.sustainedSchedulerBacklogTimeout","60s");
        //config.put("spark.dynamicAllocation.executorIdleTimeout","300s");


        LivyClient client = new LivyClientBuilder(false).setURI(uri).setAll(config).build();
        
        for(String s : System.getProperty("java.class.path").split(File.pathSeparator)) {
			logger.info("jar file: {}"+ s);
			if(new File(s).getName().startsWith("livy-client")) {
				try {
					client.uploadJar(new File(s)).get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			}
		}
        /*
        logger.info("Uploading to the Spark context.\n"+ jarPath);
        try {
            client.addJar(new URI(jarPath)).get();
        	//client.uploadJar(new File(jarPath)).get();
            logger.info("Upload done to the Spark context...\n"+ jarPath);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } */
        
        return client;

    }
    
    private Properties loadProperties() {
    	Properties appProps = new Properties();
    
    	try {
    		logger.info("Basedir" + System.getProperty("user.dir"));
    		InputStream in = getClass().getResourceAsStream("/application.properties"); 
			appProps.load(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return  appProps;
    	
    	
    }


}
