package saju.livy.client.service;

import org.apache.livy.Job;
import org.apache.livy.JobContext;
import org.apache.livy.shaded.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.function.ForeachFunction;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.*;
import org.apache.spark.sql.catalog.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;


public class SaleSummary  implements Job<List<Map<String,Object>>> {

    private String zipCode;

    private static Logger logger = Logger.getLogger(SaleSummary.class);


    public SaleSummary(String zipCd){
        this.zipCode=zipCd;
    }

    public SaleSummary(){

    }

    private SparkSession sparkSession;

    @Override
    public List<Map<String, Object>> call(JobContext jobContext) throws Exception {
        sparkSession = jobContext.sparkSession();
        return findSummary();
    }

   

    private Map<String,Object> convertToJSON(String tempJson){
        String[] parts = tempJson.split(",");
        Map<String,Object> jsonHash = new HashMap<String,Object>();
        //logger.info("String to Json---------->" + tempJson);
        for(int i=0;i<parts.length;i++){
            parts[i]    =   parts[i].replace("\"", "");
            parts[i]    =   parts[i].replace("{", "");
            parts[i]    =   parts[i].replace("}", "");
            String[] subparts = parts[i].split(":");
            if(subparts[0] != null && subparts[1] != null) {
            	jsonHash.put(subparts[0],subparts[1]);
            }
        }
        return jsonHash;
    }

    private  List<Map<String,Object>> findSummary(){
        logger.info("inside fetch findSummary....");

        List<String> stringDataset= null;
        List<Map<String,Object>> result= new ArrayList<>();
        try {
        	
        	printTblNames();
        	String sqlQry = String.format("SELECT sum(price) as totalSum , zip  FROM sales WHERE zip='%s' group by zip", zipCode);
        	Dataset<Row> grpDataSet = sparkSession.sql(sqlQry);

            stringDataset = grpDataSet.toJSON().collectAsList();
            logger.info("After String dataSet is populated...");
            for(String s : stringDataset){
                //Map<String,Object> map = new HashMap<>();
                //map = mapper.readValue(s, new TypeReference<Map<String, Object>>(){});
                result.add(convertToJSON(s));
            }
            logger.info("Before Returning...");

        }catch(Exception e){
            logger.error("Exception occurred during findSummary ",e);
            e.printStackTrace();
        }
        return result;
        

    }
    
    private void printTblNames( ) {
    	SQLContext thisSQLCntx = sparkSession.sqlContext();
    	String[] tblLst = thisSQLCntx.tableNames();
    	for (String tblName : tblLst)
    		logger.info("Table Name-->" + tblName);
    }

}