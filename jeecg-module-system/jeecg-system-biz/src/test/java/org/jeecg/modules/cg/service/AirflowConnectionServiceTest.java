package org.jeecg.modules.cg.service;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.cg.entity.CgDbConnectionInfo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class AirflowConnectionServiceTest {

    @Test
    void updateAirflowSuccessConnectionSuccess() {
        AirflowConnectionService airflowConnectionService = new AirflowConnectionService();
        // 生成测试代码
        CgDbConnectionInfo connectionInfo = new CgDbConnectionInfo();
        connectionInfo.setConnectionId("test")
                .setConnectionType("mysql")
                .setHost("localhost")
                .setPort(3306)
                .setLogin("root2")
                .setPassword("AWONXT1EdbanaLn4sFhjmg==")
                .setSchemaName("test")
                .setDescription("test")
                .setConnectionTimeout(10)
                .setQueryTimeout(10);
        String result = airflowConnectionService.updateAirflowConnection(connectionInfo, "DK7VB0ca7ewZlhqQ2O+Kq756+suJBxfJ7pQ38bss+rc=");
        // 判断返回值是否为"同步连接ID: test到airflow成功!"
        assertEquals("同步连接ID: test到airflow成功!", result);
    }

    @Test
    void updateAirflowSuccessConnectionFail() {
        AirflowConnectionService airflowConnectionService = new AirflowConnectionService();
        // 生成测试代码
        CgDbConnectionInfo connectionInfo = new CgDbConnectionInfo();
        connectionInfo.setConnectionId("test")
                .setConnectionType("mysql")
                .setHost("localhost")
                .setPort(3306)
                .setLogin("root")
                .setPassword("AWONXT1EdbanaLn4sFhjmg=")
                .setSchemaName("test")
                .setDescription("test")
                .setConnectionTimeout(10)
                .setQueryTimeout(10);
        String result = airflowConnectionService.updateAirflowConnection(connectionInfo, "DK7VB0ca7ewZlhqQ2O+Kq756+suJBxfJ7pQ38bss+rc=");
        // 判断返回值是否包含"密码解密失败"
        assertTrue(result.contains("密码解密失败"));
    }

//    jdbc:mysql://172.16.3.251:3306/foreigner-credit?connectTimeout=30000
    @Test
    void updateAirflowSuccessConnectionBadRequest() {
        AirflowConnectionService airflowConnectionService = new AirflowConnectionService();
        // 生成测试代码
        CgDbConnectionInfo connectionInfo = new CgDbConnectionInfo();
        connectionInfo.setConnectionId("mysql_pro_foreigner-credit")
                .setConnectionType("mysql")
                .setHost("172.16.3.251")
                .setPort(3306)
                .setLogin("foreigner_credit")
                .setPassword("tcJAlbVmtwTJ6ydh3NXgtA==")
                .setSchemaName("foreigner-credit")
                .setDescription("国际商贸城，义信购入场日志库")
                .setConnectionTimeout(10)
                .setQueryTimeout(10);
        String result = airflowConnectionService.updateAirflowConnection(connectionInfo, "DK7VB0ca7ewZlhqQ2O+Kq756+suJBxfJ7pQ38bss+rc=");
        // 判断返回值是否包含"密码解密失败"
        assertEquals("同步连接ID: mysql_pro_foreigner-credit到airflow成功!", result);
    }
}