package com.dova.apimaster.executor;

import com.dova.apimaster.common.domain.RestCaseSummary;
import com.dova.apimaster.common.util.JSON;
import com.dova.apimaster.executor.ast.domain.ApiRes;
import com.dova.apimaster.executor.ast.helper.PrintUtil;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import java.io.*;
import java.util.List;

/**
 * Created by liuzhendong on 16/6/11.
 */
public class RestCaseTool {

    private static HttpAssertInjectExecutor executor = new HttpAssertInjectExecutor();

    public static void runFromStream(InputStream inputStream,boolean debug)throws Exception{
        List<ApiRes>  errorApiResList = Lists.newArrayList();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        int caseNum = 0,succNum = 0,failNum = 0;
        String line = null;
        while ((line = br.readLine()) != null){
            if(line.trim().charAt(0) == '#'){
                continue;
            }
            RestCaseSummary summary = JSON.safeRead(line, RestCaseSummary.class);
            long start = System.currentTimeMillis();
            System.out.println(String.format("PROCESSING_START restcase-%d =====>>",summary.caseId));
            System.out.println(String.format("\t%s %s",summary.method, summary.url));
            ApiRes apiRes = executor.execute(summary.asRestApi(), summary.asUnitCase(),true);
            if(debug){
                System.out.println("DEBUG==============================>>");
                if((apiRes.restApi.getPathVariables() != null && apiRes.restApi.getPathVariables().size() > 0)){
                    System.out.println(String.format("\tpathVariables:%s", JSON.uncheckedToJson(apiRes.restApi.getPathVariables())));
                }
                if(apiRes.restApi.getHeaders() != null && apiRes.restApi.getHeaders().size() > 0){
                    System.out.println(String.format("\theaders:%s", JSON.uncheckedToJson(apiRes.restApi.getHeaders())));
                }
                if(apiRes.restApi.getRequestBody() != null && apiRes.restApi.getRequestBody().size() > 0){
                    System.out.println(String.format("\treq_body:%s", JSON.uncheckedToJson(apiRes.restApi.getRequestBody())));
                }
                System.out.println(String.format("\tresponse:\n\t%s", JSON.uncheckedToJson(apiRes.response)));
                System.out.println(String.format("\tassert-remark:\n\t%s", JSON.uncheckedToJson(apiRes.assertRes.remark)));
                System.out.println("DEBUG<<==============================");
            }
            System.out.println(String.format("\tasserts:%d fails:%d errors:%d",apiRes.assertRes.asserts, apiRes.assertRes.fails, apiRes.assertRes.errors));
            long end = System.currentTimeMillis();
            System.out.println(String.format("PROCESSING_END restcase-%d cost %d ms <<=====",summary.caseId, end-start));

            caseNum++;
            if(apiRes.assertRes.errors == 0 && apiRes.assertRes.fails == 0){
                succNum++;
            }else {
                errorApiResList.add(apiRes);
                failNum++;
            }
        }
        System.out.println("\nSHOW_RESULT_START==========================================");
        for (ApiRes apiRes : errorApiResList){
            String failOrError = apiRes.assertRes.errors == 0 ? "FAIL_RES" : "ERROR_RES";
            System.out.println(String.format("%s restcase-%d start =====>>",failOrError, apiRes.unitId));
            System.out.println(String.format("\t%s %s", apiRes.restApi.getMethod(), apiRes.restApi.getUrl()));
            if((apiRes.restApi.getPathVariables() != null && apiRes.restApi.getPathVariables().size() > 0)){
                System.out.println(String.format("\tpathVariables:%s", JSON.uncheckedToJson(apiRes.restApi.getPathVariables())));
            }
            if(apiRes.restApi.getHeaders() != null && apiRes.restApi.getHeaders().size() > 0){
                System.out.println(String.format("\theaders:%s", JSON.uncheckedToJson(apiRes.restApi.getHeaders())));
            }
            if(apiRes.restApi.getRequestBody() != null && apiRes.restApi.getRequestBody().size() > 0){
                System.out.println(String.format("\treq_body:%s", JSON.uncheckedToJson(apiRes.restApi.getRequestBody())));
            }
            System.out.println(String.format("\tresponse:\n\t%s", JSON.uncheckedToJson(apiRes.response)));
            System.out.println(String.format("\tassert-remark:\n\t%s", JSON.uncheckedToJson(apiRes.assertRes.remark)));
            System.out.println(String.format("%s restcase-%d end =====>>",failOrError, apiRes.unitId));

        }
        System.out.println(String.format("SHOW_RESULT_END caseNum:%d succNum:%d failNum:%d", caseNum, succNum, failNum));


    }
    public static void main(String[] args)throws Exception{
        if(args.length == 0){
            System.out.println("args length must gt 0, but");
            System.exit(1);
        }
        if(Strings.isNullOrEmpty(args[0])){
            System.out.println("conf file name is null or empty ");
            System.exit(1);
        }
        System.out.println("args[0]:" + args[0]);
        boolean debug = false;
        if(args.length >= 2 && args[1].equals("debug")){
            System.out.println("enable debug");
            debug = true;
        }
        runFromStream(new FileInputStream(args[0]), debug);
    }

}
