package org.jeecg.modules.cg.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xiaowei.song
 * @version v1.0.0
 * @description TODO
 * @date 11/10/2024 上午 8:46
 */
class UniversalConnectionServiceTest {

    @Test
    void testOracleConnection() {
        UniversalConnectionService service = new UniversalConnectionService();
        String result = service.testConnection("oracle", "localhost", "1521", "boothdb_read", "1234", "BOOTHDB");
        // 测试校验返回结果是否是"JDBC connection successful!"
        assertEquals("JDBC connection successful!", result);
    }
}