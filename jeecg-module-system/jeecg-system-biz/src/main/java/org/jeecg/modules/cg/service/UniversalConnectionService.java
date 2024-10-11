package org.jeecg.modules.cg.service;

import java.sql.Connection;
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
            return "JDBC connection successful!";
        } catch (SQLException e) {
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
                return "jdbc:postgresql://" + host + ":" + port + "/" + dbName + "?connectTimeout=" + TIMEOUT;
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
}
