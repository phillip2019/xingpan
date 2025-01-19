package org.jeecg.modules.ibf.service;

import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.ibf.entity.IbfReportingSummary;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Description: 填报发布汇总
 * @Author: jeecg-boot
 * @Date:   2025-01-17
 * @Version: V1.0
 */
public interface IIbfReportingSummaryService extends IService<IbfReportingSummary> {

    /**
     * 复制
     *
     * @param record
     * @param loginUser
     */
    void copy(IbfReportingSummary record, LoginUser loginUser);

    void removeBatch(List<IbfReportingSummary> ibfReportingSummaryList);
}
