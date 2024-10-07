package org.jeecg.modules.cg.service.impl;

import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.util.PasswordUtil;
import org.jeecg.modules.cg.entity.CgDbConnectionInfo;
import org.jeecg.modules.cg.mapper.CgDbConnectionInfoMapper;
import org.jeecg.modules.cg.service.ICgDbConnectionInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import static org.jeecg.common.util.PasswordUtil.aes256Decrypt;
import static org.jeecg.common.util.PasswordUtil.aes256Encrypt;

/**
 * @Description: CG数据库连接信息
 * @Author: jeecg-boot
 * @Date:   2024-09-26
 * @Version: V1.0
 */
@Slf4j
@Service
public class CgDbConnectionInfoServiceImpl extends ServiceImpl<CgDbConnectionInfoMapper, CgDbConnectionInfo> implements ICgDbConnectionInfoService {


    @Value("${cg.dbConnectionSecretKey}")
    private String base64SecretKey;
    @Override
    public boolean save(CgDbConnectionInfo entity) {
        // 获得密钥密码
        try {
            entity.setPassword(aes256Encrypt(entity.getPassword(), base64SecretKey));
        } catch (Exception e) {
            log.error("加密密码失败, 错误消息为: ", e);
            throw new RuntimeException(e);
        }
        return super.save(entity);
    }

    @Override
    public boolean updateById(CgDbConnectionInfo entity) {
        // 先查询数据库中老记录，比对两个密码是否一致，若不一致，则加密原始密码
        CgDbConnectionInfo oldCgDbConnectionInfo = baseMapper.selectById(entity.getId());
        if (!oldCgDbConnectionInfo.getPassword().equals(entity.getPassword())) {
            try {
                entity.setPassword(aes256Encrypt(entity.getPassword(), base64SecretKey));
            } catch (Exception e) {
                log.error("加密密码失败, 错误消息为: ", e);
                throw new RuntimeException(e);
            }
        }
        return super.updateById(entity);
    }

    @Override
    public String showRealPassword(String password) {
        try {
            return PasswordUtil.aes256Decrypt(password, base64SecretKey);
        } catch (Exception e) {
            log.error("加密密码失败, 错误消息为: ", e);
            throw new RuntimeException(e);
        }
    }
}
