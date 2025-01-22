package org.jeecg.modules.ibf.job;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xiaowei.song
 * @version v1.0.0
 * @description TODO
 * @date 18/1/2025 上午 9:40
 */
@Slf4j
public class CreateReportRecordJob implements Job {

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

    protected static final String DICT_CODE = "short_market_id";

    public static final String[] EXCLUDE_PROPERTIES = {"id", "created_at", "updated_at", "created_by", "updated_by", "is_publish", "flag"};

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
        log.info("创建报表记录");
        String curMonth = IbfDateUtil.getCurrentMonth();
        if (StringUtils.isNotBlank(parameter)) {
            curMonth = parameter;
        }

        List<DictModel> dictModelList = commonApi.queryEnableDictItemsByCode(DICT_CODE);
        List<String> shortMarketIdList = dictModelList.stream().map(DictModel::getValue).collect(Collectors.toList());

//        每月21号 8点创建填报记录和填报数据，若存在系统数据，则填报数据为系统数据，若不存在系统数据，则填报数据为0，每月26号0点自动发布（12月时，生成待填报记录，第2天0点自动发布）有填报记录权限的人，可以手动发布，可以复制发布记录，在21-25号内，不允许发布，可以预览2个版本数据，
//
//        若手动发布时，查看是否有同期发布版本，将同期发布版本删除状态置为删除，填报记录的is_publish改为1，表示已经发布，将同期填报数据删除，填报数据is_publish改为1
//
//        已发布记录允许复制1遍（未发布的不允许复制），只有发布记录允许复制，只允许复制1遍，复制后，is_copy改为1，is_publish改为0，同时复制填报数据，填报数据is_publish改为0
//
        // 查询系统数据
        LambdaQueryWrapper<IbfMarketResourceSys> sysDataQuery =  new LambdaQueryWrapper<>();
        sysDataQuery.eq(IbfMarketResourceSys::getMonthCol, curMonth)
                .orderByDesc(IbfMarketResourceSys::getCreatedAt)
                ;
        List<IbfMarketResourceSys> sysDataList = ibfMarketResourceSysService.list(sysDataQuery);
        Date now = new Date();
        // 创建市场资源填报数据
        Map<String, IbfMarketResource> initResourceM = new HashMap<>(8);
        Map<String, IbfMarketResourceGmv> initResourceGmvM = new HashMap<>(8);
        Map<String, IbfMarketResourceFlow> initResourceFlowM = new HashMap<>(8);
        Map<String, IbfMarketFinance> initFinanceFlowM = new HashMap<>(8);
        for (String shortMarketId : shortMarketIdList) {
            IbfMarketResource resource = new IbfMarketResource();
            resource.setIsPublish(0)
                    .setFlag(0)
                    .setShortMarketId(shortMarketId)
                    .setMonthCol(curMonth)
                    .setCreateBy("system")
                    .setCreateTime(now)
            ;
            initResourceM.put(shortMarketId, resource);

            IbfMarketResourceGmv resourceGmv = new IbfMarketResourceGmv();
            resourceGmv.setMarketGmv1m(BigDecimal.valueOf(0))
                    .setIsPublish(0)
                    .setFlag(0)
                    .setShortMarketId(shortMarketId)
                    .setMonthCol(curMonth)
                    .setCreateBy("system")
                    .setCreateTime(now)
            ;
            initResourceGmvM.put(shortMarketId, resourceGmv);

            // 创建流量
            IbfMarketResourceFlow resourceFlow = new IbfMarketResourceFlow();
            resourceFlow.setForeignBuyerEntrNum1m(BigDecimal.valueOf(0))
                    .setMarketBuyerEntrNum1m(BigDecimal.valueOf(0))
                    .setCarEntrNum1m(BigDecimal.valueOf(0))
                    .setBoothOpeningRate1m(BigDecimal.valueOf(0))
                    .setIsPublish(0)
                    .setFlag(0)
                    .setShortMarketId(shortMarketId)
                    .setMonthCol(curMonth)
                    .setCreateBy("system")
                    .setCreateTime(now);

            initResourceFlowM.put(shortMarketId, resourceFlow);

            // 创建财务填报记录
            IbfMarketFinance finance = new IbfMarketFinance();
            finance.setCurPeriodIncome1m(BigDecimal.valueOf(0))
                    .setTurnoverIncomeSd(BigDecimal.valueOf(0))
                    .setTargetTurnoverIncomeSd(BigDecimal.valueOf(0))
                    .setAccumulateProfitIncomeSd(BigDecimal.valueOf(0))
                    .setTargetProfitIncomeSd(BigDecimal.valueOf(0))
                    .setIsPublish(0)
                    .setFlag(0)
                    .setMonthCol(curMonth)
                    .setShortMarketId(shortMarketId)
                    .setCreateBy("system")
                    .setCreateTime(now)
                ;
            initFinanceFlowM.put(shortMarketId, finance);
        }

        if (sysDataList != null && !sysDataList.isEmpty()) {
            sysDataList.forEach(sysData -> {
                // 创建资源填报主记录
                IbfMarketResource resource = initResourceM.get(sysData.getShortMarketId());
                // 复制系统数据，过滤ID和创建时间、创建人、更新人、更新时间属性，默认全量属性拷贝
                BeanUtils.copyProperties(sysData, resource, EXCLUDE_PROPERTIES);

                // 复制资源流量数据
                IbfMarketResourceFlow resourceFlow = initResourceFlowM.get(sysData.getShortMarketId());
                BeanUtils.copyProperties(sysData, resourceFlow, EXCLUDE_PROPERTIES);
            });
        } else {
            log.error("系统数据为空, 请检查系统数据，稍后再重试");
        }

        // 创建发版记录
        IbfReportingSummary record = new IbfReportingSummary();
        String[] curMonthDuration = IbfDateUtil.getCurrentMonthStartAndEndDate(curMonth);
        record.setYear(Integer.parseInt(curMonth.substring(0, 4)))
                // 起始时间为若为1月，则起始时间为本年1月1号-本年1月20号
                .setStatStartDate(DateUtils.parseDate(curMonthDuration[0], "yyyy-MM-dd"))
                .setStatEndDate(DateUtils.parseDate(curMonthDuration[1], "yyyy-MM-dd"))
                .setRemark(String.format("%s月份填报数据", curMonth))
                .setIsPublish(0)
                .setFlag(0)
                .setMonthCol(curMonth)
                .setCreateBy("system")
                .setCreateTime(now)
                ;

        try {
            // 先删除本月数据
            ibfMarketResourceService.remove(new LambdaQueryWrapper<IbfMarketResource>().eq(IbfMarketResource::getMonthCol, curMonth));
            ibfMarketResourceGmvService.remove(new LambdaQueryWrapper<IbfMarketResourceGmv>().eq(IbfMarketResourceGmv::getMonthCol, curMonth));
            ibfMarketResourceFlowService.remove(new LambdaQueryWrapper<IbfMarketResourceFlow>().eq(IbfMarketResourceFlow::getMonthCol, curMonth));
            ibfMarketFinanceService.remove(new LambdaQueryWrapper<IbfMarketFinance>().eq(IbfMarketFinance::getMonthCol, curMonth));
            ibfReportingSummaryService.remove(new LambdaQueryWrapper<IbfReportingSummary>().eq(IbfReportingSummary::getMonthCol, curMonth));

            // 批量保存所有数据，若存在本月数据，先清除旧数据
            // 开启事务，批量保存上面4种填报数据和发版记录对象
            ibfMarketResourceService.saveBatch(initResourceM.values());
            ibfMarketResourceGmvService.saveBatch(initResourceGmvM.values());
            ibfMarketResourceFlowService.saveBatch(initResourceFlowM.values());
            ibfMarketFinanceService.saveBatch(initFinanceFlowM.values());
            ibfReportingSummaryService.save(record);
            log.info("业财一体-创建[{}]月份数据成功", curMonth);
        } catch (Exception e) {
            log.error("业财一体-每月创建数据失败", e);
            throw e;
        }
    }
}
