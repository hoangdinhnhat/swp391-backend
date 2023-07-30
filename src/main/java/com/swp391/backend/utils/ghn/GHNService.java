package com.swp391.backend.utils.ghn;

import com.swp391.backend.model.order.Order;
import com.swp391.backend.model.orderDetails.OrderDetails;
import com.swp391.backend.model.receiveinfo.ReceiveInfo;
import com.swp391.backend.model.shop.Shop;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class GHNService {
    private Map<String, String> config = new HashMap<String, String>() {
        {
            put("shop_id", "122248");
            put("token", "fc0ea700-c65d-11ed-ab31-3eeb4194879e");
            put("endpoint", "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/create");
        }
    };

    public String shippingOrders(Shop shop, ReceiveInfo receiveInfo, Order order, List<OrderDetails> orderDetailss, String requiredNote, String note) {
        String expectedDeliveryDate = "";
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost post = new HttpPost(config.get("endpoint"));
            post.setHeader("Content-type", "application/json");

            String phoneString = "                             {\n"
                    + "                                 \"name\":\"" + orderDetailss.get(0).getProduct().getName() + "\",\n"
                    + "                                 \"code\":\"" + orderDetailss.get(0).getProduct().getId() + "\",\n"
                    + "                                 \"quantity\": " + orderDetailss.get(0).getQuantity() + ",\n"
                    + "                                 \"price\": " + Math.round(orderDetailss.get(0).getSellPrice()) + ",\n"
                    + "                                 \"length\": 12,\n"
                    + "                                 \"width\": 12,\n"
                    + "                                 \"height\": 12,\n"
                    + "                                 \"weight\": 12,\n"
                    + "                                 \"category\": \n"
                    + "                                 {\n"
                    + "                                     \"level1\":\"" + orderDetailss.get(0).getProduct().getId() + "\"\n"
                    + "                                 }\n"
                    + "                             }\n";
            for (int i = 1; i < orderDetailss.size(); i++) {
                phoneString += "                             ,{\n"
                        + "                                 \"name\":\"" + orderDetailss.get(i).getProduct().getName() + "\",\n"
                        + "                                 \"code\":\"" + orderDetailss.get(i).getProduct().getId() + "\",\n"
                        + "                                 \"quantity\": " + orderDetailss.get(i).getQuantity() + ",\n"
                        + "                                 \"price\": " + Math.round(orderDetailss.get(i).getSellPrice() * 23000) + ",\n"
                        + "                                 \"length\": 12,\n"
                        + "                                 \"width\": 12,\n"
                        + "                                 \"height\": 12,\n"
                        + "                                 \"weight\": 12,\n"
                        + "                                 \"category\": \n"
                        + "                                 {\n"
                        + "                                     \"level1\":\"" + orderDetailss.get(i).getProduct().getId() + "\"\n"
                        + "                                 }\n"
                        + "                             }\n";
            }

            String json = "{\n"
                    + "                        \"from_name\":\"" + shop.getName() + "\",\n"
                    + "                        \"from_phone\":\"" + shop.getPhone() + "\",\n"
                    + "                        \"from_address\":\"" + shop.getShopAddress().getSpecificAddress() + "\",\n"
                    + "                        \"from_ward_code\":\"" + shop.getShopAddress().getWard().getId() + "\",\n"
                    + "                        \"from_district_id\":\"" + shop.getShopAddress().getDistrict().getId() + "\",\n"
                    + "                        \"from_province_id\":\"" + shop.getShopAddress().getProvince().getId() + "\",\n"
                    + "\"return_name\": \"" + shop.getName() + "\",\n"
                    + "                        \"return_phone\": \"" + shop.getPhone() + "\",\n"
                    + "                        \"return_address\": \"" + shop.getShopAddress().getSpecificAddress() + "\",\n"
                    + "                        \"return_ward_code\": \"" + shop.getShopAddress().getWard().getId() + "\",\n"
                    + "                        \"return_district_id\": \"" + shop.getShopAddress().getDistrict().getId() + "\",\n"
                    + "                        \"return_province_id\":\"" + shop.getShopAddress().getProvince().getId() + "\","
                    + "                        \"to_name\": \"" + receiveInfo.getFullname() + "\",\n"
                    + "                        \"to_phone\": \"" + receiveInfo.getPhone() + "\",\n"
                    + "                        \"to_address\": \"" + receiveInfo.getSpecific_address() + "\",\n"
                    + "                        \"to_ward_code\": \"" + receiveInfo.getWard().getId() + "\",\n"
                    + "                        \"to_district_id\": \"" + receiveInfo.getDistrict().getId() + "\",\n"
                    + "                        \"to_province_id\": \"" + receiveInfo.getProvince().getId() + "\",\n"
                    + "                        \"weight\": 200,\n"
                    + "                        \"length\": 20,\n"
                    + "                        \"width\": 20,\n"
                    + "                        \"height\": 50,\n"
                    + "                        \"insurance_value\": " + Math.round(order.getSellPrice() * 23000) + ",\n"
                    + "                        \"service_type_id\": 2,\n"
                    + "                        \"payment_type_id\": 2,\n"
                    + "                        \"required_note\": \"" + requiredNote + "\",\n"
                    + "                        \"note\": \"" + note + "\",\n"
                    + "                        \"cod_amount\": " + Math.round(order.getSellPrice() * 23000) + ",\n"
                    + "                        \"items\": [\n"
                    + phoneString
                    + "                             \n"
                    + "                         ]\n"
                    + "                    }";
            StringEntity entity = new StringEntity(json, "UTF-8");
            entity.setContentType(ContentType.APPLICATION_JSON.withCharset("UTF-8").getMimeType());

            post.setEntity(entity);
            post.addHeader("token", config.get("token"));
            post.addHeader("shop_id", config.get("shop_id"));

            CloseableHttpResponse res = client.execute(post);
            BufferedReader rd = new BufferedReader(new InputStreamReader(res.getEntity().getContent()));
            StringBuilder resultJsonStr = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                resultJsonStr.append(line);
            }

            JSONObject result = new JSONObject(resultJsonStr.toString());
            String code = "";
            String codeMessage = "";
            for (String key : result.keySet()) {
                if (key.equals("code")) {
                    code = result.get(key).toString();
                }
                if (key.equals("code_message")) {
                    codeMessage = result.get(key).toString();
                }
                if (key.equals("data")) {
                    String temps[] = result.get(key).toString().split(",");
                    String temp2 = temps[3];
                    String temp3 = temp2.split(":")[1];
                    String temp4 = temp3.split("T")[0];
                    expectedDeliveryDate = temp4;
                }
                System.out.format("%s = %s\n", key, result.get(key));
            }
            if (!code.equals("200")) {
                throw new Exception(codeMessage);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return expectedDeliveryDate;
    }
}
