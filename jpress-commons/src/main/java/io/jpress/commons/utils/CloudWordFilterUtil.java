package io.jpress.commons.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.ClientException;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.green.model.v20180509.TextScanRequest;
import com.aliyuncs.http.FormatType;
import com.aliyuncs.http.HttpResponse;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.tms.v20200713.TmsClient;
import com.tencentcloudapi.tms.v20200713.models.TextModerationRequest;
import com.tencentcloudapi.tms.v20200713.models.TextModerationResponse;
import io.jboot.utils.HttpUtil;
import io.jboot.utils.StrUtil;
import io.jpress.JPressOptions;

import java.nio.charset.StandardCharsets;
import java.util.*;

public class CloudWordFilterUtil {



    /**
     * Alibaba Cloud text content detection
     * @param accessKeyId
     * @param secret
     * @param content
     */
    public static boolean aliyunTextScan(String accessKeyId,String secret,String regionId,String content) {

        IClientProfile profile = DefaultProfile
                .getProfile(regionId, accessKeyId, secret);
        DefaultProfile
                .addEndpoint(regionId, "Green", "green.cn-shanghai.aliyuncs.com");
        IAcsClient client = new DefaultAcsClient(profile);
        TextScanRequest textScanRequest = new TextScanRequest();
        textScanRequest.setAcceptFormat(FormatType.JSON); // Specify the API to return format.
        textScanRequest.setHttpContentType(FormatType.JSON);
        textScanRequest.setMethod(com.aliyuncs.http.MethodType.POST); // Specify the request method.
        textScanRequest.setEncoding("UTF-8");
        textScanRequest.setRegionId(regionId);
        List<Map<String, Object>> tasks = new ArrayList<Map<String, Object>>();
        Map<String, Object> task1 = new LinkedHashMap<String, Object>();
        task1.put("dataId", UUID.randomUUID().toString());
        /**
         * The text to be tested does not exceed 10,000 characters.
         */
        task1.put("content", content);
        tasks.add(task1);
        JSONObject data = new JSONObject();

        /**
         * Test the scene.For text garbage detection, please pass Antispam.
         **/
        data.put("scenes", Arrays.asList("antispam"));
        data.put("tasks", tasks);

        textScanRequest.setHttpContent(data.toJSONString().getBytes(StandardCharsets.UTF_8), "UTF-8", FormatType.JSON);
        // Be sure to set timeout.
        textScanRequest.setConnectTimeout(3000);
        textScanRequest.setReadTimeout(6000);
        try {
            HttpResponse httpResponse = client.doAction(textScanRequest);
            if(httpResponse.isSuccess()){
                JSONObject scrResponse = JSON.parseObject(new String(httpResponse.getHttpContent(), "UTF-8"));

                if (200 == scrResponse.getInteger("code")) {
                    JSONArray taskResults = scrResponse.getJSONArray("data");

                    for (Object taskResult : taskResults) {

                        if(200 == ((JSONObject)taskResult).getInteger("code")){
                            JSONArray sceneResults = ((JSONObject)taskResult).getJSONArray("results");

                            for (Object sceneResult : sceneResults) {
                                String scene = ((JSONObject)sceneResult).getString("scene");
                                String suggestion = ((JSONObject)sceneResult).getString("suggestion");
                                // Related according to Scene and SuggeTion.
                                // SUGGGESTION indicates unpopular garbage for PASS.SUGGESTION shows the garbage for Block, and you can check the garbage classification through the Label field.

                                if(("block").equals(suggestion)){
                                    return true;
                                }
                            }
                        }
                    }
                }
            }

        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }



    /**
     * Xiaohuaer AI text content detection
     * @param appCode
     * @param content
     */
    public static boolean xiaohuaeraiTextScan(String appCode , String content){
        String host = "https://textfilter.xiaohuaerai.com";
        String path = "/textfilter";
        String method = "POST";
        String appcode = appCode;
        Map<String, String> headers = new HashMap<String, String>();
        //Finally, the format in the header (the English space in the middle) is Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //According to the requirements of the API, define the corresponding Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, String> querys = new HashMap<String, String>();
        Map<String, Object> bodys = new HashMap<String, Object>();
        bodys.put("src", content);
        bodys.put("type", "detail");
        //strict: strict，easy: easy，detail: details


        try {
            /**
             * The important tips are as follows:
             * HttpUtils Invite
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
             * download
             *
             * Please refer to the corresponding dependence
             * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
             */
//            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
//            System.out.println(response.toString());
//            获取response的body
//            System.out.println(EntityUtils.toString(response.getEntity()));

            String response = HttpUtil.httpPost(host + path, bodys, headers, null);
            if(StrUtil.isNotBlank(response)){
                JSONObject jsonObject = JSONObject.parseObject(response);

                if(200 == jsonObject.getInteger("status") && ("block").equals(jsonObject.getString("msg"))){
                    //Illegal text
                    return true;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Taiyue semantic text content detection
     * @param appCode
     * @param content
     */

    public static boolean ultrapowerTextScan(String appCode , String content){
        String host = "http://monitoring.market.alicloudapi.com";
        String path = "/neirongjiance";
        String method = "POST";
        String appcode = appCode;
        Map<String, String> headers = new HashMap<String, String>();
        //Finally, the format in the header (the English space in the middle) is Authorization:APPCODE 83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        //According to the requirements of the API, define the corresponding Content-Type
        headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        Map<String, Object> bodys = new HashMap<String, Object>();
        bodys.put("in", content);


        try {

            String response = HttpUtil.httpPost(host + path, bodys, headers, null);
            if(StrUtil.isNotBlank(response)){
                JSONObject jsonObject = JSONObject.parseObject(response);

                if (200 == jsonObject.getInteger("status") && jsonObject.getBoolean("success")) {
                    JSONObject data = JSON.parseObject(jsonObject.getString("data"));
//                    System.out.println("data--->"+data);

                    JSONArray out = data.getJSONArray("out");

                    for (Object result : out) {

                        String politics = ((JSONObject)result).getString("Political sensitivity monitoring");
                        String politicsStr = politics.substring(politics.indexOf("[") + 1, politics.lastIndexOf("]"));
                        if(StrUtil.isNotBlank(politicsStr)){
                            return true;
                        }
                        String contraband = ((JSONObject)result).getString("Surveillance monitoring");
                        String contrabandStr = contraband.substring(contraband.indexOf("[") + 1, contraband.lastIndexOf("]"));
                        if(StrUtil.isNotBlank(contrabandStr)){
                            return true;
                        }
                        String flood = ((JSONObject)result).getString("Malicious irrigation monitoring");//false

                        String porn = ((JSONObject)result).getString("Pornographic monitoring");
                        String pornStr = porn.substring(porn.indexOf("[") + 1, porn.lastIndexOf("]"));
                        if(StrUtil.isNotBlank(pornStr)){
                            return true;
                        }
                        String abuse = ((JSONObject)result).getString("Abuse monitoring");
                        String abuseStr = abuse.substring(abuse.indexOf("[") + 1, abuse.lastIndexOf("]"));
                        if(StrUtil.isNotBlank(abuseStr)){
                            return true;
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Tencent Cloud text content is safe
     * @param content
     * @return
     */
    public static boolean  qcloudTextScan(String accessKeyId,String secret,String region,String content){
        try{

            // Examination of an authentication object, entering the Tencent Cloud account Secretid, Secretkey, here you need to pay attention to the keys confidentiality
            // The key can go https://console.cloud.tencent.com/cam/capi Website acquisition
            Credential cred = new Credential(accessKeyId, secret);

            // Examination of an HTTP option, optional, no special needs can skip
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("tms.tencentcloudapi.com");

            // Examination of a client option, optional, no special needs can skip
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            // Instance to request the client object of the product, clientprofile is optional
            TmsClient client = new TmsClient(cred, region, clientProfile);

            // Examine a request object, each interface will correspond to a request object
            TextModerationRequest req = new TextModerationRequest();

            String encodeToString =null;
            if(StrUtil.isNotBlank(content)){
                //Content indicates the text of the object to be detected. The text needs to be encoded in UTF-8 format.
                encodeToString = Base64.getEncoder().encodeToString(content.getBytes(StandardCharsets.UTF_8));
            }
            req.setContent(encodeToString);

            // The returned resp is an instance of TextModeRESPONSE, which corresponds to the request object
            TextModerationResponse resp = client.TextModeration(req);

            // Output json format string back package
//            System.out.println(TextModerationResponse.toJsonString(resp));

            if(resp != null && ("Block").equals(resp.getSuggestion())){
                return true;
            }
        } catch (TencentCloudSDKException e) {
            e.toString();
        }
        return false;
    }

    /**
     * Determine whether the text content is safe
     * @param content
     * @return
     */
    public static boolean isIllegalWords(String content){
        //Open the cloud filtration function
        String serviceEnable = JPressOptions.get("text_filter_service_enable");

        //Cloud filter service provider
        String service = JPressOptions.get("text_filter_service");

        String appId = JPressOptions.get("text_filter_appid");
        String appSecret = JPressOptions.get("text_filter_appsecret");
        String regionId = JPressOptions.get("text_filter_regionid");
        String appCode = JPressOptions.get("text_filter_appcode");

        if(StrUtil.isNotBlank(serviceEnable)){
            Boolean enable = Boolean.valueOf(serviceEnable);
            if(!enable){
                return false;
            }
        }


        if(StrUtil.isBlank(content)){
            return false;
        }


        if(("aliyun").equals(service)){//Ali Cloud
            return aliyunTextScan(appId, appSecret,regionId, content);

        }
        else if(("qcloud").equals(service)){//Tencent Cloud
            return qcloudTextScan(appId,appSecret,regionId,content);

        }
        else if(("xiaohuaerai").equals(service)){//Xiaohuaer AI
            return xiaohuaeraiTextScan(appCode,content);

        }
        else if(("ultrapower").equals(service)){//Taiyue Language Volunteer Factory
            return ultrapowerTextScan(appCode,content);

        }

        return false;
    }


}
