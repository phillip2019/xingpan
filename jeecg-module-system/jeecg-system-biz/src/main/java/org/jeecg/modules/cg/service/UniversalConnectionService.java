package org.jeecg.modules.cg.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.jeecg.modules.cg.entity.CgDbConnectionInfo;
import redis.clients.jedis.Jedis;

import org.apache.commons.net.ftp.FTPClient;

import org.springframework.stereotype.Service;

@Slf4j
@Service
public class UniversalConnectionService {

    private static final int TIMEOUT = 30000; // 连接超时为 3 秒

    /**
     * 通用的连接验证方法
     * @param dbType 数据库类型 (mysql, sqlserver, oracle, clickhouse, redis, sftp)
     * @param host 服务器地址
     * @param port 端口号
     * @param username 用户名
     * @param password 密码
     * @param dbName 数据库名称 (仅适用于关系型数据库)
     * @return 连接结果
     */
    public String testConnection(String dbType, String host, String port, String username, String password, String dbName) {
        switch (dbType.toLowerCase()) {
            case "mysql":
            case "sqlserver":
            case "oracle":
            case "postgresql":
            case "clickhouse":
                String url = buildJdbcUrl(dbType, host, port, dbName);
                return testJdbcConnection(url, username, password);
            case "redis":
                return testRedisConnection(host, Integer.parseInt(port), password);
            case "sftp":
                return testSFTPConnection(host, Integer.parseInt(port), username, password);
            default:
                return "Unsupported database type: " + dbType;
        }
    }

    // JDBC 连接测试
    private String testJdbcConnection(String url, String username, String password) {
        Properties props = new Properties();
        props.setProperty("user", username);
        props.setProperty("password", password);
        // oracle特殊处理，设置超时时间
        if (StringUtils.contains(url, "oracle")) {
            props.setProperty("oracle.jdbc.ReadTimeout", String.valueOf(TIMEOUT));
        }
        try (Connection connection = DriverManager.getConnection(url, props)) {
            // 连接成功后检测数据库版本
            DatabaseMetaData metaData = connection.getMetaData();
            String dbProductName = metaData.getDatabaseProductName();
            String dbProductVersion = metaData.getDatabaseProductVersion();
            log.info("Connected to {} version: {}", dbProductName, dbProductVersion);
            return "JDBC connection successful!  Database: " + dbProductName + ", Version: " + dbProductVersion;
        } catch (SQLException e) {
            log.error("Error connecting url: {}, user: {} to database: {}", url, username, e.getMessage(), e);
            return "JDBC connection failed: " + e.getMessage();
        }
    }

    public String buildJdbcUrl(String dbType, String host, String port, String dbName) {
        switch (dbType.toLowerCase()) {
            case "mysql":
                return "jdbc:mysql://" + host + ":" + port + "/" + dbName + "?connectTimeout=" + TIMEOUT;
            case "sqlserver":
                return "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + dbName + ";loginTimeout=" + (TIMEOUT / 1000);
            case "oracle":
//                return "jdbc:oracle:thin:@" + host + ":" + port + ":orcl" + ";oracle.net.CONNECT_TIMEOUT=" + TIMEOUT;
                return "jdbc:oracle:thin:@" + host + ":" + port + ":orcl";
            case "clickhouse":
                return "jdbc:clickhouse://" + host + ":" + port + "/" + dbName + "?socket_timeout=" + TIMEOUT + "&connect_timeout=" + TIMEOUT;
            case "postgresql":
                return "jdbc:postgresql://" + host + ":" + port + "/" + dbName + "?connectTimeout=" + TIMEOUT + "&useUnicode=true&characterEncoding=utf8&currentSchema=public";
            default:
                throw new IllegalArgumentException("Unsupported database type: " + dbType);
        }
    }

    // Redis 连接测试
    private String testRedisConnection(String host, int port, String password) {
        try (Jedis jedis = new Jedis(host, port, TIMEOUT)) {
            if (password != null && !password.isEmpty()) {
                jedis.auth(password);
            }
            jedis.ping();
            return "Redis connection successful!";
        } catch (Exception e) {
            return "Redis connection failed: " + e.getMessage();
        }
    }

    // 3. SFTP 连接测试
    private String testSFTPConnection(String host, int port, String username, String password) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.setConnectTimeout(TIMEOUT);
            ftpClient.connect(host, port);
            boolean login = ftpClient.login(username, password);
            if (login) {
                ftpClient.logout();
                return "SFTP connection successful!";
            } else {
                return "SFTP connection failed: Invalid credentials.";
            }
        } catch (Exception e) {
            return "SFTP connection failed: " + e.getMessage();
        } finally {
            try {
                ftpClient.disconnect();
            } catch (Exception e) {
                // 忽略关闭连接时的异常
            }
        }
    }

    // 新增的引擎版本检测方法
    public String checkEngineVersion(String dbType, String host, String port, String username, String password) {
        // 假设针对不同的引擎有不同的检测方式
        switch (dbType.toLowerCase()) {
            // 检测jdbc类型数据库
            case "mysql":
            case "sqlserver":
            case "oracle":
            case "postgresql":
            case "clickhouse":
                String url = buildJdbcUrl(dbType, host, port, "");
                String result = testJdbcConnection(url, username, password);
                // 若检测状态为可用，则解析result中的版本信息
                // 返回内容为JDBC connection successful!  Database: " + dbProductName + ", Version: " + dbProductVersion，提取版本信息
                if (result.startsWith("JDBC connection successful!")) {
                    // 提取返回信息中的版本信息
                    String ver = StringUtils.substringBetween(result, ", Version: ", "");
                    log.info("Version: {}", ver);
                    log.info("Database: {}", StringUtils.substringBetween(result, "Database: ", ", Version: "));
                    return ver;
                }
                return null;
            case "redis":
                log.info("Redis version check: {}", testRedisConnection(host, Integer.parseInt(port), password));
                return checkRedisVersion(host, Integer.parseInt(port), password);
            case "sftp":
                return checkSFTPVersion(host, Integer.parseInt(port), username, password);
            case "http":
                return checkHttpVersion();
            default:
                log.error("Unsupported database type: {}", dbType);
                return null;
        }
    }

    // Redis 版本检测 (假设 Redis 命令 info 返回版本信息)
    private String checkRedisVersion(String host, int port, String password) {
        try (Jedis jedis = new Jedis(host, port, TIMEOUT)) {
            if (password != null && !password.isEmpty()) {
                jedis.auth(password);
            }
            String info = jedis.info();
            String ver = StringUtils.substringBetween(info, "redis_version:", "\n");
            // 使用 StringUtils.normalizeSpace 规范化空白字符
            ver = org.apache.commons.lang3.StringUtils.normalizeSpace(ver);
            log.info("##########################################");
            log.info("Redis version: {}", ver);
            log.info("##########################################");
            // 提取 Redis 版本信息
            return ver;
        } catch (Exception e) {
            log.error("Redis version check failed: ", e);
            return null;
        }
    }

    // SFTP 版本检测
    private String checkSFTPVersion(String host, int port, String username, String password) {
        FTPClient ftpClient = new FTPClient();
        try {
            ftpClient.connect(host, port);
            ftpClient.login(username, password);
            String systemType = ftpClient.getSystemType();
            ftpClient.logout();
            return systemType;
        } catch (Exception e) {
            log.error("SFTP version check failed: ", e);
            return null;
        } finally {
            try {
                ftpClient.disconnect();
            } catch (Exception e) {
                // 忽略关闭连接时的异常
            }
        }
    }

    private String checkHttpVersion() {
        return "1.0";
    }
}
