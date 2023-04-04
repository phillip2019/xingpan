package org.jeecg.modules.cg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.jeecg.modules.cg.entity.CgShopsZf;

import java.util.List;

/**
 * @Description: 维度店铺拉链表
 * @Author: jeecg-boot
 * @Version: V1.0
 */
public interface ICgShopsZfService extends IService<CgShopsZf> {

    /**
     * 根据店铺编号查询店铺详情信息
     * @param shopIdsFE 店铺编号列表
     * @return shopZf 店铺详情列表
     **/
    List<CgShopsZf> queryListByIds(List<String> shopIdsFE);
}
