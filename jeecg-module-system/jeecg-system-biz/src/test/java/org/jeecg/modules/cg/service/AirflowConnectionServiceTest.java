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
}