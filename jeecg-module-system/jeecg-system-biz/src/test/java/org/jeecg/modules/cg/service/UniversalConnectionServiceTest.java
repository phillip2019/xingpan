package org.jeecg.modules.cg.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.ftp.FTPClient;

import java.sql.DatabaseMetaData;
import java.util.*;
import java.math.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

/**
 * @author xiaowei.song
 * @version v1.0.0
 * @description TODO
 * @date 11/10/2024 上午 8:46
 */
@Slf4j
@ExtendWith(MockitoExtension.class)
class UniversalConnectionServiceTest {

    @InjectMocks
    private UniversalConnectionService service;

    @Mock
    private Jedis jedis;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void checkEngineVersion_MySQL_ReturnsVersion() {
        String version = "5.7.31";
        try (Connection connection = mock(Connection.class)) {
            DatabaseMetaData metaData = mock(DatabaseMetaData.class);
            when(connection.getMetaData()).thenReturn(metaData);
            when(metaData.getDatabaseProductName()).thenReturn("MySQL");
            when(metaData.getDatabaseProductVersion()).thenReturn(version);

            String result = service.checkEngineVersion("mysql", "localhost", "3306", "root", "password");
            assertEquals(version, result);
        } catch (SQLException e) {
            fail("SQLException should not occur");
        }
    }

    @Test
    void checkEngineVersion_SQLServer_ReturnsVersion() {
        String version = "14.0.3042.14";
        try (Connection connection = mock(Connection.class)) {
            DatabaseMetaData metaData = mock(DatabaseMetaData.class);
            when(connection.getMetaData()).thenReturn(metaData);
            when(metaData.getDatabaseProductName()).thenReturn("SQL Server");
            when(metaData.getDatabaseProductVersion()).thenReturn(version);

            String result = service.checkEngineVersion("sqlserver", "localhost", "1433", "sa", "password");
            assertEquals(version, result);
        } catch (SQLException e) {
            fail("SQLException should not occur");
        }
    }

    @Test
    void checkEngineVersion_Oracle_ReturnsVersion() {
        String version = "12.1.0.2.0";
        try (Connection connection = mock(Connection.class)) {
            DatabaseMetaData metaData = mock(DatabaseMetaData.class);
            when(connection.getMetaData()).thenReturn(metaData);
            when(metaData.getDatabaseProductName()).thenReturn("Oracle");
            when(metaData.getDatabaseProductVersion()).thenReturn(version);

            String result = service.checkEngineVersion("oracle", "localhost", "1521", "user", "password");
            assertEquals(version, result);
        } catch (SQLException e) {
            fail("SQLException should not occur");
        }
    }

    @Test
    void checkEngineVersion_PostgreSQL_ReturnsVersion() {
        String version = "13.3";
        try (Connection connection = mock(Connection.class)) {
            DatabaseMetaData metaData = mock(DatabaseMetaData.class);
            when(connection.getMetaData()).thenReturn(metaData);
            when(metaData.getDatabaseProductName()).thenReturn("PostgreSQL");
            when(metaData.getDatabaseProductVersion()).thenReturn(version);

            String result = service.checkEngineVersion("postgresql", "localhost", "5432", "user", "password");
            assertEquals(version, result);
        } catch (SQLException e) {
            fail("SQLException should not occur");
        }
    }

    @Test
    void checkEngineVersion_ClickHouse_ReturnsVersion() {
        String version = "21.8.3.17";
        try (Connection connection = mock(Connection.class)) {
            DatabaseMetaData metaData = mock(DatabaseMetaData.class);
            when(connection.getMetaData()).thenReturn(metaData);
            when(metaData.getDatabaseProductName()).thenReturn("ClickHouse");
            when(metaData.getDatabaseProductVersion()).thenReturn(version);

            String result = service.checkEngineVersion("clickhouse", "localhost", "9000", "user", "password");
            assertEquals(version, result);
        } catch (SQLException e) {
            fail("SQLException should not occur");
        }
    }

    @Test
    void checkEngineVersion_Redis_ReturnsVersion() {
        String version = "7.0.5";
        String result = service.checkEngineVersion("redis", "172.18.5.17", "6379", "", "Admin@123");
        log.info("##########################");
        log.info("Actual Redis version: {}", result);
        log.info("##########################");
        assertEquals(version, result);
    }

    @Test
    void checkEngineVersion_SFTP_ReturnsVersion() {
        String version = "UNIX";
        FTPClient ftpClient = mock(FTPClient.class);
        try {
            when(ftpClient.getSystemType()).thenReturn(version);

            String result = service.checkEngineVersion("sftp", "localhost", "22", "user", "password");
            assertEquals(version, result);
        } catch (Exception e) {
            fail("Exception should not occur");
        }
    }

    @Test
    void checkEngineVersion_HTTP_ReturnsVersion() {
        String result = service.checkEngineVersion("http", "", "", "", "");
        assertEquals("1.0", result);
    }

    @Test
    void checkEngineVersion_UnsupportedType_ReturnsNull() {
        String result = service.checkEngineVersion("unsupported", "", "", "", "");
        assertNull(result);
    }

    @Test
    void testOracleConnection() {
        UniversalConnectionService service = new UniversalConnectionService();
        String result = service.testConnection("oracle", "localhost", "1521", "boothdb_read", "1234", "BOOTHDB");
        // 测试校验返回结果是否是"JDBC connection successful!"
        assertEquals("JDBC connection successful!", result);
    }

    @Test
    void testPostgresqlConnection() {
        UniversalConnectionService service = new UniversalConnectionService();
        // jdbc:postgresql://pc-uf67ahoip5xxv783d.rwlb.rds.aliyuncs.com:1521/booth_prod?useUnicode=true&characterEncoding=utf8&currentSchema=newmarket&oracleCase=true
        String result = service.testConnection("postgresql", "pc-uf67ahoip5xxv783d.rwlb.rds.aliyuncs.com", "1521", "cgbi_ro", "bi@chinagoods", "booth_prod");
        System.out.println(result);
        assertEquals("JDBC connection successful!", result);
    }
}