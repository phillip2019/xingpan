package org.jeecg.modules.demo.ma.mapper;

import org.jeecg.modules.demo.ma.entity.MaPosition;
import org.jeecg.modules.demo.ma.mapper.dto.MaMarketFloorNumDTO;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author xiaowei.song
 * @version v1.0.0
 * @description TODO
 * @date 2023/1/17 13:39
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
//@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("dev")
class MaPositionMapperTest {

    @Autowired
    private MaPositionMapper positionMapper;

    @Test
    void selectMarketFloorPositionNum() {
        List<MaMarketFloorNumDTO> marketFloorNumDTOList = positionMapper.selectMarketFloorPositionNum(1L);
        assert marketFloorNumDTOList.size() == 0;
    }

    @Test
    void insertPositionReturnId() {
        // TODO 写测试
        MaPosition maPosition = new MaPosition();
        maPosition.setPositionNo("M01F101")
                .setPositionType("易拉宝")
                .setSeqNo("01")
                .setActiveId(1L)
                .setMarketName("国际商贸城一区")
                .setFloor("一楼")
                .setOwnerAccount("1243242")
                .setOwnerName("张三")
                .setQrCodeTicket("4234324")
                .setQrCodeUrl("432432")
                .setStatus(1);
        positionMapper.insertPositionReturnId(maPosition);
        assert maPosition.getId() != null;
    }
}