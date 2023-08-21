package org.jeecg.modules.et.mapper;

import java.util.List;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.*;
import org.jeecg.modules.et.entity.EtClient;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.jeecg.modules.et.entity.EtPlatformSiteCode;

/**
 * @Description: et_client
 * @Author: jeecg-boot
 * @Date:   2023-07-26
 * @Version: V1.0
 */
public interface EtClientMapper extends BaseMapper<EtClient> {
    /**
     * 根据 entity 条件，查询全部记录（并翻页）
     *
     * @param page         分页查询条件（可以为 RowBounds.DEFAULT）
     * @param queryWrapper 实体对象封装操作类（可以为 null）
     */
    IPage<EtClient> selectPage(Page<EtClient> page, @Param(Constants.WRAPPER) Wrapper<EtClient> queryWrapper);
}
