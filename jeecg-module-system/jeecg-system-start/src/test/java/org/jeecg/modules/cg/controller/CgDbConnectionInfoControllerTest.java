package org.jeecg.modules.cg.controller;

import java.util.*;
import java.math.*;

import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.cg.entity.CgDbConnectionInfo;
import org.jeecg.modules.cg.service.ICgDbConnectionInfoService;
import org.jeecg.modules.cg.service.UniversalConnectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CgDbConnectionInfoControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ICgDbConnectionInfoService cgDbConnectionInfoService;

    @Mock
    private UniversalConnectionService connectionService;

    @InjectMocks
    private CgDbConnectionInfoController cgDbConnectionInfoController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(cgDbConnectionInfoController).build();
    }

    @Test
    public void testConnection_ValidInput_ShouldReturnOk() throws Exception {
        CgDbConnectionInfo connectionInfo = new CgDbConnectionInfo();
        connectionInfo.setId("1");
        connectionInfo.setConnectionType("MySQL");
        connectionInfo.setHost("localhost");
        connectionInfo.setPort(3306);
        connectionInfo.setLogin("root");
        connectionInfo.setPassword("password");
        connectionInfo.setSchemaName("test_db");

        when(cgDbConnectionInfoService.getById(anyString())).thenReturn(connectionInfo);
        when(connectionService.testConnection(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("Connection successful");

        mockMvc.perform(post("/cg/cgDbConnectionInfo/testConnection")
                .param("id", "1"))
                .andExpect(status().isOk());

        verify(cgDbConnectionInfoService, times(1)).getById(anyString());
        verify(connectionService, times(1)).testConnection(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testConnection_MissingFields_ShouldReturnError() throws Exception {
        CgDbConnectionInfo connectionInfo = new CgDbConnectionInfo();
        connectionInfo.setId("1");
        connectionInfo.setConnectionType("MySQL");
        connectionInfo.setHost("localhost");
        connectionInfo.setPort(3306);
        connectionInfo.setLogin("root");
        connectionInfo.setPassword(null); // Missing password
        connectionInfo.setSchemaName("test_db");

        when(cgDbConnectionInfoService.getById(anyString())).thenReturn(connectionInfo);

        mockMvc.perform(post("/cg/cgDbConnectionInfo/testConnection")
                .param("id", "1"))
                .andExpect(status().isInternalServerError());

        verify(cgDbConnectionInfoService, times(1)).getById(anyString());
        verify(connectionService, never()).testConnection(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void testConnection_NullConnectionInfo_ShouldReturnError() throws Exception {
        when(cgDbConnectionInfoService.getById(anyString())).thenReturn(null);

        mockMvc.perform(post("/cg/cgDbConnectionInfo/testConnection")
                .param("id", "1"))
                .andExpect(status().isInternalServerError());

        verify(cgDbConnectionInfoService, times(1)).getById(anyString());
        verify(connectionService, never()).testConnection(anyString(), anyString(), anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void jdbcUrl_ValidInput_ShouldReturnJdbcUrl() throws Exception {
        CgDbConnectionInfo connectionInfo = new CgDbConnectionInfo();
        connectionInfo.setId("1");
        connectionInfo.setConnectionType("MySQL");
        connectionInfo.setHost("localhost");
        connectionInfo.setPort(3306);
        connectionInfo.setLogin("root");
        connectionInfo.setPassword("password");
        connectionInfo.setSchemaName("test_db");

        when(cgDbConnectionInfoService.getById(anyString())).thenReturn(connectionInfo);
        when(connectionService.buildJdbcUrl(anyString(), anyString(), anyString(), anyString()))
                .thenReturn("jdbc:mysql://localhost:3306/test_db");

        mockMvc.perform(get("/cg/cgDbConnectionInfo/jdbcUrl")
                .param("id", "1"))
                .andExpect(status().isOk());

        verify(cgDbConnectionInfoService, times(1)).getById(anyString());
        verify(connectionService, times(1)).buildJdbcUrl(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void jdbcUrl_MissingFields_ShouldReturnError() throws Exception {
        CgDbConnectionInfo connectionInfo = new CgDbConnectionInfo();
        connectionInfo.setId("1");
        connectionInfo.setConnectionType("MySQL");
        connectionInfo.setHost("localhost");
        connectionInfo.setPort(3306);
        connectionInfo.setLogin("root");
        connectionInfo.setPassword(null); // Missing password
        connectionInfo.setSchemaName("test_db");

        when(cgDbConnectionInfoService.getById(anyString())).thenReturn(connectionInfo);

        mockMvc.perform(get("/cg/cgDbConnectionInfo/jdbcUrl")
                .param("id", "1"))
                .andExpect(status().isInternalServerError());

        verify(cgDbConnectionInfoService, times(1)).getById(anyString());
        verify(connectionService, never()).buildJdbcUrl(anyString(), anyString(), anyString(), anyString());
    }

    @Test
    public void jdbcUrl_NullConnectionInfo_ShouldReturnError() throws Exception {
        when(cgDbConnectionInfoService.getById(anyString())).thenReturn(null);

        mockMvc.perform(get("/cg/cgDbConnectionInfo/jdbcUrl")
                .param("id", "1"))
                .andExpect(status().isInternalServerError());

        verify(cgDbConnectionInfoService, times(1)).getById(anyString());
        verify(connectionService, never()).buildJdbcUrl(anyString(), anyString(), anyString(), anyString());
    }
}
