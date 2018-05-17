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


public class LoadCSV  implements Job<List<Map<String,Object>>> {

    private String plan;

    private static Logger logger = Logger.getLogger(LoadCSV.class);

    public LoadCSV(){

    }
    public LoadCSV(String planCd){
    	this.plan = planCd;
    }

    private SparkSession sparkSession;

    @Override
    public List<Map<String, Object>> call(JobContext jobContext) throws Exception {
        sparkSession = jobContext.sparkSession();
        return executeJob();
    }

    private  List<Map<String, Object>> executeJob(){
    
    	return loadCSV();
    	
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

    private  List<Map<String, Object>> loadCSV(){
        logger.info("inside fetch loadCSV....");

        List<String> stringDataset= null;
        List<Map<String,Object>> result= new ArrayList<>();
        try {
        		Dataset<Row> datasetCSV = sparkSession.read().schema(customSchema).option("header","false").option("mode", "DROPMALFORMED").csv("hdfs:////user/honey_saju/data/Sacramentorealestatetransactions.csv");
	        	datasetCSV.createOrReplaceTempView("sales");
	        	//datasetCSV.createGlobalTempView("g_resales");
	        	logger.info("---------------->Global table created");
	        	result.add(convertToJSON("{Status:Success}"));
        }catch(Exception e){
            logger.error("Exception occurred during loadCSV ",e);
            e.printStackTrace();
            result.add(convertToJSON("{Status:Failed}"));
        }
        printTblNames();
        
        return result;
       

    }
    private void printTblNames( ) {
    	SQLContext thisSQLCntx = sparkSession.sqlContext();
    	String[] tblLst = thisSQLCntx.tableNames();
    	for (String tblName : tblLst)
    		logger.info("Table Name-->" + tblName);
    }
    StructType customSchema = new StructType( new StructField[]{
            new StructField("street", DataTypes.StringType, true, Metadata.empty()),
            
            new StructField("city", DataTypes.StringType, true, Metadata.empty()),
            new StructField("zip", DataTypes.LongType, true, Metadata.empty()),
            new StructField("state", DataTypes.StringType, true, Metadata.empty()),
            new StructField("beds", DataTypes.IntegerType, true, Metadata.empty()),
            new StructField("baths", DataTypes.IntegerType, true, Metadata.empty()),
            new StructField("sq__ft", DataTypes.LongType, true, Metadata.empty()),
            new StructField("type", DataTypes.StringType, true, Metadata.empty()),
            new StructField("sale_date", DataTypes.StringType, true, Metadata.empty()),
            new StructField("price", DataTypes.LongType, true, Metadata.empty()),
            new StructField("latitude", DataTypes.DoubleType, true, Metadata.empty()),
            new StructField("longitude", DataTypes.DoubleType, true, Metadata.empty()),
            
});



}

