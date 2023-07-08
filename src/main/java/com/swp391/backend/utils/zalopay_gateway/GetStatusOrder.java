package com.swp391.backend.utils.zalopay_gateway;

import com.swp391.backend.utils.cryptography.HMACUtil;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetStatusOrder {

    private Map<String, String> config = new HashMap<String, String>() {
        {
            put("app_id", "2554");
            put("key1", "sdngKKJmqEMzvh5QQcdD2A9XBSKUNaYn");
            put("key2", "trMrHtvjo6myautxDUiAcYsVtaeQ8nhf");
            put("endpoint", "https://sb-openapi.zalopay.vn/v2/query");
        }
    };

    public Status get(String app_trans_id) throws Exception {
        Status status = new Status();
        String data = config.get("app_id") + "|" + app_trans_id + "|" + config.get("key1"); // appid|app_trans_id|key1
        String mac = HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, config.get("key1"), data);

        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("app_id", config.get("app_id")));
        params.add(new BasicNameValuePair("app_trans_id", app_trans_id));
        params.add(new BasicNameValuePair("mac", mac));

        URIBuilder uri = new URIBuilder(config.get("endpoint"));
        uri.addParameters(params);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(uri.build());
        post.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse res = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }

        JSONObject result = new JSONObject(resultJsonStr.toString());
        for (String key : result.keySet()) {
            if (key.equals("return_code")) {
                status.setReturn_code(Integer.parseInt(result.get(key).toString()));
            }
            if (key.equals("return_message")) {
                status.setReturn_message(result.get(key).toString());
            }
            if (key.equals("sub_return_message")) {
                status.setSub_return_message(result.get(key).toString());
            }
            if (key.equals("is_processing")) {
                status.setIs_processing(Boolean.parseBoolean(result.get(key).toString()));
            }
            if (key.equals("amount")) {
                status.setAmount(Long.parseLong(result.get(key).toString()));
            }
            if (key.equals("zp_trans_id")) {
                status.setZp_trans_id(Long.parseLong(result.get(key).toString()));
            }
        }
        return status;
    }
}