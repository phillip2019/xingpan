package org.jeecg.modules.cg.controller;

import org.jeecg.modules.cg.entity.CgDeptIndexValue;
import org.jeecg.modules.cg.service.ICgDeptIndexValueService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author xiaowei.song
 * @version v1.0.0
 * @description TODO
 * @date 2024/4/9 7:00
 */
@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
public class CgDeptIndexValueControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICgDeptIndexValueService cgDeptIndexValueService;

    @Test
    public void testAddAndUpdateIfUniqueIndexExists() throws Exception {
        // Create a sample CgDeptIndexValue object with existing unique index constraints
        CgDeptIndexValue existingCgDeptIndexValue = new CgDeptIndexValue();
        existingCgDeptIndexValue.setDeptIndexId("30");
        existingCgDeptIndexValue.setBeginDate(new Date(2024, 4, 1));
        existingCgDeptIndexValue.setEndDate(new Date(2024, 4, 7));

        // Perform a POST request to add a new CgDeptIndexValue
        mockMvc.perform(post("/cg/cgDeptIndexValue/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"deptId\":\"1625117673753038849\",\"deptIndexId\":\"30\",\"indexValue\":13,\"beginDate\":\"2024-04-01\",\"endDate\":\"2024-04-07\"}"))
                .andExpect(status().isOk());

        // Verify that the service method to save or update the CgDeptIndexValue is called
        verify(cgDeptIndexValueService).saveOrUpdate(any());
    }
}