package org.jeecg.modules.cg.service.impl;

import org.jeecg.modules.cg.service.ICgShopsZfService;
import org.jeecg.modules.ma.mapper.MaPositionMapper;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xiaowei.song
 * @version v1.0.0
 * @description TODO
 * @date 2023/4/4 8:53
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
class CgCgShopsZfServiceImplTest {

    @Autowired
    private ICgShopsZfService cgShopsZfService;

    @Test
    void queryListByIds() {
        cgShopsZfService.queryListByIds(Arrays.asList("6313158", "6313159"));
    }
}