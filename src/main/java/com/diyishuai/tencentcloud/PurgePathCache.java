package com.diyishuai.tencentcloud;


import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;

import com.tencentcloudapi.cdn.v20180606.CdnClient;

import com.tencentcloudapi.cdn.v20180606.models.PurgePathCacheRequest;
import com.tencentcloudapi.cdn.v20180606.models.PurgePathCacheResponse;
/**
 * @author: Bruce
 * @date: 2020-03-28
 * @description:
 */
public class PurgePathCache {
    public static void main(String [] args) {
        if (args.length < 4){
            throw new RuntimeException("参数缺少");
        }
        String sId = args[0];
        String sKey = args[1];
        String region = args[2];
        String paths = "";
        for (int i = 3; i < args.length; i++) {
            paths += "\""+args[i]+"\"";
            if (i < args.length-1){
                paths += ",";
            }
        }
        System.out.println("paths : " + paths);
        System.out.println("region : " + region);
        try{

            Credential cred = new Credential(sId, sKey);

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("cdn.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            CdnClient client = new CdnClient(cred, region, clientProfile);

            String params = "{\"Paths\":["+paths+"],\"FlushType\":\"delete\"}";
            PurgePathCacheRequest req = PurgePathCacheRequest.fromJsonString(params, PurgePathCacheRequest.class);

            PurgePathCacheResponse resp = client.PurgePathCache(req);

            System.out.println(PurgePathCacheRequest.toJsonString(resp));
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        }

    }

}