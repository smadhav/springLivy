package saju.livy.client.service;

import org.apache.livy.Job;
import org.apache.livy.JobContext;

import saju.livy.client.entity.request.Analysis;
import saju.livy.client.entity.request.AnalysisMeasure;

import org.apache.spark.sql.SparkSession;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AnalysisJob implements Job<List<Map<String,Object>>> {

    private Analysis analysis;

    private static Logger logger = Logger.getLogger(AnalysisJob.class);

    private SparkSession sparkSession;

    public AnalysisJob(Analysis analysis){
        this.analysis=analysis;
    }

    @Override
    public List<Map<String, Object>> call(JobContext jobContext) throws Exception {
        sparkSession = jobContext.sparkSession();
        return executeJob();
    }

    private List<Map<String,Object>> executeJob(){
        logger.info("inside analysis for ...." + analysis);
        List<String> stringDataset= null;
        List<Map<String,Object>> result= new ArrayList<>();
        for(AnalysisMeasure measure : analysis.getMeasures()){

        }
        return null;
    }

    private Map<String,Object> convertToJSON(String tempJson){
        String[] parts = tempJson.split(",");
        Map<String,Object> jsonHash = new HashMap<String,Object>();
        for(int i=0;i<parts.length;i++){
            parts[i]    =   parts[i].replace("\"", "");
            parts[i]    =   parts[i].replace("{", "");
            parts[i]    =   parts[i].replace("}", "");
            String[] subparts = parts[i].split(":");
            jsonHash.put(subparts[0],subparts[1]);
        }
        return jsonHash;
    }
}
