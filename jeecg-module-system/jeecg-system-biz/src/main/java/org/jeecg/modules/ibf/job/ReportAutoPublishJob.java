package org.jeecg.modules.ibf.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.api.CommonAPI;
import org.jeecg.common.system.vo.DictModel;
import org.jeecg.common.util.DateUtils;
import org.jeecg.modules.ibf.entity.*;
import org.jeecg.modules.ibf.service.*;
import org.jeecg.modules.ibf.util.IbfDateUtil;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiaowei.song
 * @version v1.0.0
 * @description 大屏自动发布任务
 * 每月26号8点自动发布
 * 将当月的填报记录发布，发布后，将当月发布记录is_publish改为1，将当月填报数据is_publish改为1
 * 将上月的发布记录is_publish改为2（下线)，将上月填报数据is_publish改为2（下线），将之前发布记录flag非0的修改成0，将之前填报数据flag非0的修改成0
 * @date 18/1/2025 上午 9:40
 */
@Slf4j
public class ReportAutoPublishJob implements Job {

    @Autowired
    private IIbfReportingSummaryService ibfReportingSummaryService;

    @Autowired
    private IIbfMarketResourceService ibfMarketResourceService;

    @Autowired
    private IIbfMarketResourceFlowService ibfMarketResourceFlowService;

    @Autowired
    private IIbfMarketResourceGmvService ibfMarketResourceGmvService;

    @Autowired
    private IIbfMarketFinanceService ibfMarketFinanceService;

    @Autowired
    private IIbfMarketFlowService ibfMarketFlowService;

    @Autowired
    private IIbfMarketResourceSysService ibfMarketResourceSysService;

    @Lazy
    @Autowired
    private CommonAPI commonApi;

    /**
     * 若参数变量名修改 QuartzJobController中也需对应修改
     */
    private String parameter;

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    @SneakyThrows
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        // 每月21号0时创建报表记录
        log.info("开始自动发布大屏");
        String curMonth = IbfDateUtil.getCurrentMonth();
        if (StringUtils.isNotBlank(parameter)) {
            curMonth = parameter;
        }
        String lastMonth = IbfDateUtil.getLastMonth(curMonth);

//      将当月的填报记录发布，发布后，将当月发布记录is_publish改为1，将当月填报数据is_publish改为1
//      将上月的发布记录is_publish改为2（下线)，将上月填报数据is_publish改为2（下线），将之前发布记录flag非0的修改成0，将之前填报数据flag非0的修改成0
        IbfReportingSummary ibfReportingSummary = ibfReportingSummaryService.getOne(new LambdaQueryWrapper<IbfReportingSummary>().eq(IbfReportingSummary::getMonthCol, curMonth));
        if (ibfReportingSummary == null) {
            log.warn("业财一体-每月创建数据失败，本月数据不存在");
            return;
        }
        try {
            List<IbfReportingSummary> ibfReportingSummaryList = new ArrayList<>(2);
            List<IbfMarketResource> ibfMarketResourceList = new ArrayList<>(16);
            List<IbfMarketResourceGmv> ibfMarketResourceGmvList = new ArrayList<>(16);
            List<IbfMarketResourceFlow> ibfMarketResourceFlowList = new ArrayList<>(16);
            List<IbfMarketFinance> ibfMarketFinanceList = new ArrayList<>(16);

            ibfReportingSummary.setIsPublish(1)
                    .setUpdateBy("system")
            ;
            ibfReportingSummaryList.add(ibfReportingSummary);

            List<IbfMarketResource> curIbfMarketResourceList = ibfMarketResourceService.list(new LambdaQueryWrapper<IbfMarketResource>().eq(IbfMarketResource::getMonthCol, curMonth));
            for (IbfMarketResource ibfMarketResource : curIbfMarketResourceList) {
                ibfMarketResource.setIsPublish(1)
                                .setUpdateBy("system")
                        ;
                ibfMarketResourceList.add(ibfMarketResource);
            }

            // 资源GMV更新
            List<IbfMarketResourceGmv> curIbfMarketResourceGmvList = ibfMarketResourceGmvService.list(new LambdaQueryWrapper<IbfMarketResourceGmv>().eq(IbfMarketResourceGmv::getMonthCol, curMonth));
            for (IbfMarketResourceGmv ibfMarketResourceGmv : curIbfMarketResourceGmvList) {
                ibfMarketResourceGmv.setIsPublish(1)
                        .setUpdateBy("system");
                ibfMarketResourceGmvList.add(ibfMarketResourceGmv);
            }

            // 资源流量更新
            List<IbfMarketResourceFlow> curIbfMarketResourceFlowList = ibfMarketResourceFlowService.list(new LambdaQueryWrapper<IbfMarketResourceFlow>().eq(IbfMarketResourceFlow::getMonthCol, curMonth));
            for (IbfMarketResourceFlow ibfMarketResourceFlow : curIbfMarketResourceFlowList) {
                ibfMarketResourceFlow.setIsPublish(1).setUpdateBy("system");
                ibfMarketResourceFlowList.add(ibfMarketResourceFlow);
            }
            // 财务更新
            List<IbfMarketFinance> curIbfMarketFinanceList = ibfMarketFinanceService.list(new LambdaQueryWrapper<IbfMarketFinance>().eq(IbfMarketFinance::getMonthCol, curMonth));
            for (IbfMarketFinance ibfMarketFinance : curIbfMarketFinanceList) {
                ibfMarketFinance.setIsPublish(1);
                ibfMarketFinanceList.add(ibfMarketFinance);
            }

            // 将上月的发布记录is_publish改为2（下线)，将上月填报数据is_publish改为2（下线），将之前发布记录flag非0的修改成0，将之前填报数据flag非0的修改成0
            IbfReportingSummary lastMonthIbfReportingSummary = ibfReportingSummaryService.getOne(new LambdaQueryWrapper<IbfReportingSummary>().eq(IbfReportingSummary::getMonthCol, lastMonth).eq(IbfReportingSummary::getIsPublish, 1));
            if (lastMonthIbfReportingSummary != null) {
                lastMonthIbfReportingSummary.setIsPublish(2).setUpdateBy("system");
                ibfReportingSummaryList.add(lastMonthIbfReportingSummary);
            }

            List<IbfMarketResource> lastMonthIbfMarketResourceList = ibfMarketResourceService.list(new LambdaQueryWrapper<IbfMarketResource>().eq(IbfMarketResource::getMonthCol, lastMonth).eq(IbfMarketResource::getIsPublish, 1));
            for (IbfMarketResource ibfMarketResource : lastMonthIbfMarketResourceList) {
                ibfMarketResource.setIsPublish(2).setUpdateBy("system");
                ibfMarketResourceList.add(ibfMarketResource);
            }

            // 资源GMV更新
            List<IbfMarketResourceGmv> lastMonthIbfMarketResourceGmvList = ibfMarketResourceGmvService.list(new LambdaQueryWrapper<IbfMarketResourceGmv>().eq(IbfMarketResourceGmv::getMonthCol, lastMonth).eq(IbfMarketResourceGmv::getIsPublish, 1));
            for (IbfMarketResourceGmv ibfMarketResourceGmv : lastMonthIbfMarketResourceGmvList) {
                ibfMarketResourceGmv.setIsPublish(2).setUpdateBy("system");
                ibfMarketResourceGmvList.add(ibfMarketResourceGmv);
            }

            List<IbfMarketResourceFlow> lastMonthIbfMarketResourceFlowList = ibfMarketResourceFlowService.list(new LambdaQueryWrapper<IbfMarketResourceFlow>().eq(IbfMarketResourceFlow::getMonthCol, lastMonth).eq(IbfMarketResourceFlow::getIsPublish, 1));
            for (IbfMarketResourceFlow ibfMarketResourceFlow : lastMonthIbfMarketResourceFlowList) {
                ibfMarketResourceFlow.setIsPublish(2).setUpdateBy("system");
                ibfMarketResourceFlowList.add(ibfMarketResourceFlow);
            }

            List<IbfMarketFinance> lastMonthIbfMarketFinanceList = ibfMarketFinanceService.list(new LambdaQueryWrapper<IbfMarketFinance>().eq(IbfMarketFinance::getMonthCol, lastMonth).eq(IbfMarketFinance::getIsPublish, 1));
            for (IbfMarketFinance ibfMarketFinance : lastMonthIbfMarketFinanceList) {
                ibfMarketFinance.setIsPublish(2).setUpdateBy("system");
                ibfMarketFinanceList.add(ibfMarketFinance);
            }

            // 将上月的发布flag为非0的，发布状态为0的，将之前填报数据flag非0的修改成0，且将状态置为删除状态，此记录自动删除
            List<IbfReportingSummary> lastMonthIbfReportingSummaryListFlag = ibfReportingSummaryService.list(new LambdaQueryWrapper<IbfReportingSummary>().eq(IbfReportingSummary::getMonthCol, lastMonth).eq(IbfReportingSummary::getIsPublish, 0).ne(IbfReportingSummary::getFlag, 0));
            for (IbfReportingSummary reportingSummary : lastMonthIbfReportingSummaryListFlag) {
                reportingSummary.setFlag(0);
                reportingSummary.setIsDeleted(1).setUpdateBy("system");
                ibfReportingSummaryList.add(reportingSummary);
            }

            // 将之前填报数据flag非0的修改成0，且将状态置为删除状态，此记录自动删除
            List<IbfMarketResource> lastMonthIbfMarketResourceListFlag = ibfMarketResourceService.list(new LambdaQueryWrapper<IbfMarketResource>().eq(IbfMarketResource::getMonthCol, lastMonth).ne(IbfMarketResource::getFlag, 0));
            for (IbfMarketResource ibfMarketResource : lastMonthIbfMarketResourceListFlag) {
                ibfMarketResource.setFlag(0);
                ibfMarketResource.setIsDeleted(1).setUpdateBy("system");
                ibfMarketResourceList.add(ibfMarketResource);
            }
            // 资源GMV更新
            List<IbfMarketResourceGmv> lastMonthIbfMarketResourceGmvListFlag = ibfMarketResourceGmvService.list(new LambdaQueryWrapper<IbfMarketResourceGmv>().eq(IbfMarketResourceGmv::getMonthCol, lastMonth).ne(IbfMarketResourceGmv::getFlag, 0));
            for (IbfMarketResourceGmv ibfMarketResourceGmv : lastMonthIbfMarketResourceGmvListFlag) {
                ibfMarketResourceGmv.setFlag(0);
                ibfMarketResourceGmv.setIsDeleted(1).setUpdateBy("system");
                ibfMarketResourceGmvList.add(ibfMarketResourceGmv);
            }
            List<IbfMarketResourceFlow> lastMonthIbfMarketResourceFlowListFlag = ibfMarketResourceFlowService.list(new LambdaQueryWrapper<IbfMarketResourceFlow>().eq(IbfMarketResourceFlow::getMonthCol, lastMonth).ne(IbfMarketResourceFlow::getFlag, 0));
            for (IbfMarketResourceFlow ibfMarketResourceFlow : lastMonthIbfMarketResourceFlowListFlag) {
                ibfMarketResourceFlow.setFlag(0);
                ibfMarketResourceFlow.setIsDeleted(1).setUpdateBy("system");
                ibfMarketResourceFlowList.add(ibfMarketResourceFlow);
            }
            // 财务填报记录更新
            List<IbfMarketFinance> lastMonthIbfMarketFinanceListFlag = ibfMarketFinanceService.list(new LambdaQueryWrapper<IbfMarketFinance>().eq(IbfMarketFinance::getMonthCol, lastMonth).ne(IbfMarketFinance::getFlag, 0));
            for (IbfMarketFinance ibfMarketFinance : lastMonthIbfMarketFinanceListFlag) {
                ibfMarketFinance.setFlag(0);
                ibfMarketFinance.setIsDeleted(1).setUpdateBy("system");
                ibfMarketFinanceList.add(ibfMarketFinance);
            }

            // 批量更新记录
            ibfReportingSummaryService.saveOrUpdateBatch(ibfReportingSummaryList);
            ibfMarketResourceService.saveOrUpdateBatch(ibfMarketResourceList);
            ibfMarketResourceGmvService.saveOrUpdateBatch(ibfMarketResourceGmvList);
            ibfMarketResourceFlowService.saveOrUpdateBatch(ibfMarketResourceFlowList);
            ibfMarketFinanceService.saveOrUpdateBatch(ibfMarketFinanceList);
        } catch (Exception e) {
            log.error("业财一体【{}】大屏发布失败， 异常为: ", curMonth, e);
            throw e;
        }
        log.info("业财一体【{}】大屏发布成功", curMonth);
    }
}
