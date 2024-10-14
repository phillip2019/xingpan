package org.jeecg.modules.cg.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import org.jeecg.modules.cg.entity.CgDbConnectionInfo;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
public class AirflowConnectionService {

    private static final String AIRFLOW_API_URL = "http://bigdata-util-gateway-01.chinagoods.te:28080/api/v1/connections";
    private static final String USERNAME = "xingpan";
    private static final String PASSWORD = "n9VDpWbAkLRSKfq@";
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;  // 使用 Jackson 的 ObjectMapper

    public AirflowConnectionService() {
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
        // 禁止 Unicode 转义
        this.objectMapper.configure(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS, false);

    }

    public String updateAirflowConnection(CgDbConnectionInfo connectionInfo, String base64SecretKey) {
        String errorMessage = String.format("同步连接ID: %s到airflow失败, 错误为: ", connectionInfo.getConnectionId());
        // Create JSON payload
        String jsonPayload = null;
        try {
            jsonPayload = CgDbConnectionInfo.toAirflowConnectionJson(connectionInfo, base64SecretKey);
        } catch (Exception e) {
            errorMessage = String.format("同步连接ID: %s到airflow失败，将连接信息转换成JSON错误，密码解密失败，详细错误为: %s", connectionInfo.getConnectionId(), e.getMessage());
            log.error(errorMessage, e);
            return errorMessage;
        }

        try {
            // Check if the connection already exists
            if (connectionExists(connectionInfo.getConnectionId())) {
                log.info("Connection does not exist, creating a new connection.");
                return updateExistingConnection(connectionInfo.getConnectionId(), jsonPayload, errorMessage);
            }

            // Prepare HTTP request
            RequestBody body = RequestBody.create(jsonPayload, MediaType.parse("application/json"));
            Request request = new Request.Builder()
                    .url(AIRFLOW_API_URL)
                    .post(body)
                    .addHeader("Accept", "application/json")
                    .addHeader("Authorization", "Basic " + getBase64Credentials())
                    .build();

            // Execute the request
            try (Response response = httpClient.newCall(request).execute()) {
                assert response.body() != null;
                // 使用 Jackson 解析响应体
                String responseBody = objectMapper.readTree(response.body().string()).toString();
                if (response.isSuccessful()) {
                    log.info("Con ID: {}, Connection updated successfully: {}", connectionInfo.getConnectionId(), responseBody);
                    return String.format("同步连接ID: %s到airflow成功!", connectionInfo.getConnectionId());
                }
                assert response.body() != null;
                errorMessage += responseBody;
                log.error("Failed to update connection: {}", errorMessage);
            }
        } catch (IOException e) {
            errorMessage += e.getMessage();
            log.error("Error Con ID: {} updating connection: {}", connectionInfo.getConnectionId(), e.getMessage(), e);
        }
        return errorMessage;
    }

    private String updateExistingConnection(String connectionId, String jsonPayload, String errorMessage) throws IOException {
        // 准备PATCH请求
        String updateConnectionUrl = AIRFLOW_API_URL + "/" + connectionId;
        RequestBody body = RequestBody.create(jsonPayload, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(updateConnectionUrl)
                .patch(body)  // 使用PATCH来更新
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Basic " + getBase64Credentials())
                .build();

        // 执行请求
        try (Response response = httpClient.newCall(request).execute()) {
            if (response.isSuccessful()) {
                assert response.body() != null;
                // 使用 Jackson 解析响应体
                String responseBody = objectMapper.readTree(response.body().string()).toString();
                log.info("Con ID: {}, Connection updated successfully: {}", connectionId, responseBody);
                return String.format("同步连接ID: %s到airflow成功!", connectionId);
            }
            assert response.body() != null;
            log.error(errorMessage + response.body().string());
            return errorMessage;
        }
    }

    private boolean connectionExists(String connectionId) throws IOException {
        // Prepare HTTP GET request to check if the connection exists
        String checkConnectionUrl = AIRFLOW_API_URL + "/" + connectionId;
        Request request = new Request.Builder()
                .url(checkConnectionUrl)
                .addHeader("Accept", "application/json")
                .addHeader("Authorization", "Basic " + getBase64Credentials())
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            // If the response code is 200, the connection exists
            log.info("Checking Con ID: {} connection existence: {}", connectionId, response.code());
            return response.isSuccessful();
        } catch (IOException e) {
            log.error("Error checking connection existence: {}", e.getMessage(), e);
            return false; // Treat any error as the connection not existing
        }
    }

    private String getBase64Credentials() {
        return Base64.getEncoder().encodeToString((AirflowConnectionService.USERNAME + ":" + AirflowConnectionService.PASSWORD).getBytes());
    }
}