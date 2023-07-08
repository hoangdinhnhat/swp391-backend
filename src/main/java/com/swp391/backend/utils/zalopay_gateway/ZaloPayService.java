package com.swp391.backend.utils.zalopay_gateway;

import com.google.gson.Gson;
import com.swp391.backend.utils.cryptography.HMACUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ZaloPayService {

    private static ZaloPayService instance;
    private Map<String, String> config = new HashMap<String, String>() {
        {
            put("app_id", "2554");
            put("key1", "sdngKKJmqEMzvh5QQcdD2A9XBSKUNaYn");
            put("key2", "trMrHtvjo6myautxDUiAcYsVtaeQ8nhf");
            put("endpoint", "https://sb-openapi.zalopay.vn/v2/create");
        }
    };
    private RestTemplate restTemplate;
    private Gson gson;

    private ZaloPayService() {
        restTemplate = new RestTemplate();
        gson = new Gson();
    }

    public static ZaloPayService gI() {
        if (instance == null) {
            instance = new ZaloPayService();
        }
        return instance;
    }

    public String getCurrentTimeString(String format) {
        Calendar cal = new GregorianCalendar(TimeZone.getTimeZone("GMT+7"));
        SimpleDateFormat fmt = new SimpleDateFormat(format);
        fmt.setCalendar(cal);
        return fmt.format(cal.getTimeInMillis());
    }

    public Map<String, Object> createOrderParms(int total, String path) {
        Random rand = new Random();
        final int random_id = rand.nextInt(1000000);
        Map<String, Object> order = new HashMap<String, Object>() {
            {
                put("app_id", config.get("app_id"));
                put("app_trans_id", getCurrentTimeString("yyMMdd") + "_" + random_id); // translation missing: vi.docs.shared.sample_code.comments.app_trans_id
                put("app_time", System.currentTimeMillis()); // miliseconds
                put("app_user", "user123");
                put("amount", total);
                put("description", "Bird Trading Platform #" + random_id);
                put("bank_code", "zalopayapp");
                put("item", "[{\"id\":\"knb\",\"name\":\"hoangdinhnhat\",\"price\":50000,\"quantity\":1}]");
                put("embed_data", "{\"redirecturl\": \"http://localhost:8080/api/v1/" + path + "\",\"promotioninfo\":\"\",\"merchantinfo\":\"embeddata123\"}");
                put("callback_url", "http://localhost:8080/api/v1/users/payment/finish");
            }
        };

        String data = order.get("app_id") + "|" + order.get("app_trans_id") + "|" + order.get("app_user") + "|" + order.get("amount")
                + "|" + order.get("app_time") + "|" + order.get("embed_data") + "|" + order.get("item");
        order.put("mac", HMACUtil.HMacHexStringEncode(HMACUtil.HMACSHA256, config.get("key1"), data));
        return order;
    }

    public String createGatewayUrl(int total, String path) throws Exception {
        var order = createOrderParms(total, path);

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost post = new HttpPost(config.get("endpoint"));

        List<NameValuePair> params = new ArrayList<>();
        for (Map.Entry<String, Object> e : order.entrySet()) {
            params.add(new BasicNameValuePair(e.getKey(), e.getValue().toString()));
        }

        post.setEntity(new UrlEncodedFormEntity(params));

        CloseableHttpResponse res = client.execute(post);
        BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
        StringBuilder resultJsonStr = new StringBuilder();
        String line;

        while ((line = rd.readLine()) != null) {
            resultJsonStr.append(line);
        }

        ZaloPayResponse response = gson.fromJson(resultJsonStr.toString(), ZaloPayResponse.class);

        return response.getOrder_url();
    }

    public boolean checkCallback(HttpServletRequest request) throws Exception {
        boolean result = true;
        String key2 = "trMrHtvjo6myautxDUiAcYsVtaeQ8nhf";
        String mac = HMACUtil.HMACSHA256;
        String checksumData = request.getParameter("appid") + "|" + request.getParameter("apptransid") + "|" + request.getParameter("pmcid") + "|" + request.getParameter("bankcode") + "|"
                + request.getParameter("amount") + "|" + request.getParameter("discountamount") + "|" + request.getParameter("status");
        String checksum = HMACUtil.HMacHexStringEncode(mac, key2, checksumData);

        if (!checksum.equals(request.getParameter("checksum"))) {
            result = false;
        } else {
            GetStatusOrder getStatusOfOrder1 = new GetStatusOrder();
            Status status = getStatusOfOrder1.get(request.getParameter("apptransid"));
            if (status.getReturn_code() != 1) {
                result = false;
            }
        }

        return result;
    }
}
