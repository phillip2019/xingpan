package org.jeecg.modules.cg.service;

import org.jeecg.modules.cg.entity.CgDbConnectionInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Collection;

/**
 * @Description: CG数据库连接信息
 * @Author: jeecg-boot
 * @Date:   2024-09-26
 * @Version: V1.0
 */
public interface ICgDbConnectionInfoService extends IService<CgDbConnectionInfo> {

    /**
     * 显示真实密码
     * @param password 原始密码
     * @return 解密密码
     */
    String showRealPassword(String password);
}
