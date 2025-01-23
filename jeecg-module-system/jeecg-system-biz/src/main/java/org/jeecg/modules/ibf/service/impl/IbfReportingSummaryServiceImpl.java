package org.jeecg.modules.ibf.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.jeecg.common.system.vo.LoginUser;
import org.jeecg.modules.ibf.entity.*;
import org.jeecg.modules.ibf.mapper.IbfReportingSummaryMapper;
import org.jeecg.modules.ibf.service.*;
import org.jeecg.modules.ibf.util.IbfDateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Description: 填报发布汇总
 * @Author: jeecg-boot
 * @Date:   2025-01-17
 * @Version: V1.0
 */
@DS("ibf")
@Service
public class IbfReportingSummaryServiceImpl extends ServiceImpl<IbfReportingSummaryMapper, IbfReportingSummary> implements IIbfReportingSummaryService {

    public static final String[] EXCLUDE_PROPERTIES = {"id", "created_at", "updated_at", "created_by", "updated_by", "is_publish", "flag"};


    @Autowired
    private IIbfMarketResourceService ibfMarketResourceService;

    @Autowired
    private IIbfMarketResourceGmvService ibfMarketResourceGmvService;

    @Autowired
    private IIbfMarketResourceFlowService ibfMarketResourceFlowService;

    @Autowired
    private IIbfMarketFinanceService ibfMarketFinanceService;

    @Lazy
    @Autowired
    private IIbfReportingSummaryService ibfReportingSummaryService; // 注入自身代理对象


    @Override
    public void copy(IbfReportingSummary record, LoginUser loginUser) {
        // 复制发布记录，复制发布数据
        String monthCol = record.getMonthCol();
        String curMonth = IbfDateUtil.getCurrentMonth();
        List<IbfReportingSummary> reportingSummaryList = new ArrayList<>(2);

        // 复制资源-资源
        List<IbfMarketResource> sourceResourceList = ibfMarketResourceService.list(new LambdaQueryWrapper<IbfMarketResource>()
                .eq(IbfMarketResource::getMonthCol, monthCol)
                .eq(IbfMarketResource::getIsDeleted, 0)
                .eq(IbfMarketResource::getIsPublish, 1)
        );
        // 复制资源-GMV
        List<IbfMarketResourceGmv> sourceResourceGmvList = ibfMarketResourceGmvService.list(new LambdaQueryWrapper<IbfMarketResourceGmv>()
               .eq(IbfMarketResourceGmv::getMonthCol, monthCol)
               .eq(IbfMarketResourceGmv::getIsDeleted, 0)
               .eq(IbfMarketResourceGmv::getIsPublish, 1)
        );
        // 复制资源-流量
        List<IbfMarketResourceFlow> sourceResourceFlowList = ibfMarketResourceFlowService.list(new LambdaQueryWrapper<IbfMarketResourceFlow>()
              .eq(IbfMarketResourceFlow::getMonthCol, monthCol)
              .eq(IbfMarketResourceFlow::getIsDeleted, 0)
              .eq(IbfMarketResourceFlow::getIsPublish, 1)
        );
        // 复制财务数据
        List<IbfMarketFinance> sourceFinanceList = ibfMarketFinanceService.list(new LambdaQueryWrapper<IbfMarketFinance>()
              .eq(IbfMarketFinance::getMonthCol, monthCol)
              .eq(IbfMarketFinance::getIsDeleted, 0)
              .eq(IbfMarketFinance::getIsPublish, 1)
        );

        Date now = new Date();
        Integer flag = IbfDateUtil.calculateMonthDifference(curMonth, monthCol);
        // 复制以上各个对象，修改isPublish=0，isDeleted=0，发布记录创建人为当前用户loginUser里面的用户名
        IbfReportingSummary newRecord = new IbfReportingSummary();
        BeanUtils.copyProperties(record, newRecord, EXCLUDE_PROPERTIES);
        newRecord.setIsPublish(0)
                .setIsDeleted(0)
                .setCreateBy(loginUser.getUsername())
                .setCreateTime(now)
                .setFlag(flag)
                // 设置isCopy=1, 表示改记录是复制的，后续可以发布
                .setIsCopy(1)
                .setIsVisible(0)
                .setUpdateBy(null)
                .setUpdateTime(null)
                .setRemark(String.format("%s复制%s月数据", loginUser.getUsername(), monthCol));
        ;

        record.setIsCopy(1);
        reportingSummaryList.add(record);
        reportingSummaryList.add(newRecord);

        List<IbfMarketResource> newResourceList = new ArrayList<>(sourceResourceList.size());
        for (IbfMarketResource ibfMarketResource : sourceResourceList) {
            IbfMarketResource newResource = new IbfMarketResource();
            BeanUtils.copyProperties(ibfMarketResource, newResource, EXCLUDE_PROPERTIES);
            newResource.manualCopy(now)
            ;
            newResourceList.add(newResource);
        }

        List<IbfMarketResourceGmv> newResourceGmvList = new ArrayList<>(sourceResourceGmvList.size());
        for (IbfMarketResourceGmv ibfMarketResourceGmv : sourceResourceGmvList) {
            IbfMarketResourceGmv newResourceGmv = new IbfMarketResourceGmv();
            BeanUtils.copyProperties(ibfMarketResourceGmv, newResourceGmv, EXCLUDE_PROPERTIES);
            newResourceGmv.manualCopy(now)
            ;
            newResourceGmvList.add(newResourceGmv);
        }

        List<IbfMarketResourceFlow> newResourceFlowList = new ArrayList<>(sourceResourceFlowList.size());
        for (IbfMarketResourceFlow ibfMarketResourceFlow : sourceResourceFlowList) {
            IbfMarketResourceFlow newResourceFlow = new IbfMarketResourceFlow();
            BeanUtils.copyProperties(ibfMarketResourceFlow, newResourceFlow, EXCLUDE_PROPERTIES);
            newResourceFlow.manualCopy(now)
            ;
            newResourceFlowList.add(newResourceFlow);
        }

        List<IbfMarketFinance> newFinanceList = new ArrayList<>(sourceFinanceList.size());
        for (IbfMarketFinance ibfMarketFinance : sourceFinanceList) {
            IbfMarketFinance newFinance = new IbfMarketFinance();
            BeanUtils.copyProperties(ibfMarketFinance, newFinance, EXCLUDE_PROPERTIES);
            newFinance.manualCopy(now)
            ;
            newFinanceList.add(newFinance);
        }

        ibfMarketResourceService.saveBatch(newResourceList);
        ibfMarketResourceGmvService.saveBatch(newResourceGmvList);
        ibfMarketResourceFlowService.saveBatch(newResourceFlowList);
        ibfMarketFinanceService.saveBatch(newFinanceList);
        // 保存以上对象
        ibfReportingSummaryService.saveOrUpdateBatch(reportingSummaryList);
    }

    @Override
    public void removeBatch(List<IbfReportingSummary> ibfReportingSummaryList) {
        // 批量删除发布记录
        for (IbfReportingSummary ibfReportingSummary : ibfReportingSummaryList) {
            List<IbfReportingSummary> list = new ArrayList<>(2);
            list.add(ibfReportingSummary);
            String monthCol = ibfReportingSummary.getMonthCol();
            // 查询资源-资源记录
            List<IbfMarketResource> resourceList = ibfMarketResourceService.list(new LambdaQueryWrapper<IbfMarketResource>()
                   .eq(IbfMarketResource::getMonthCol, monthCol)
                   .eq(IbfMarketResource::getIsDeleted, 0)
                   .eq(IbfMarketResource::getIsPublish, 0)
            );

            // 查询资源-GMV记录
            List<IbfMarketResourceGmv> resourceGmvList = ibfMarketResourceGmvService.list(new LambdaQueryWrapper<IbfMarketResourceGmv>()
                  .eq(IbfMarketResourceGmv::getMonthCol, monthCol)
                  .eq(IbfMarketResourceGmv::getIsDeleted, 0)
                  .eq(IbfMarketResourceGmv::getIsPublish, 0)
            );

            // 查询资源-流量记录
            List<IbfMarketResourceFlow> resourceFlowList = ibfMarketResourceFlowService.list(new LambdaQueryWrapper<IbfMarketResourceFlow>()
                 .eq(IbfMarketResourceFlow::getMonthCol, monthCol)
                 .eq(IbfMarketResourceFlow::getIsDeleted, 0)
                 .eq(IbfMarketResourceFlow::getIsPublish, 0)
            );

            // 查询财务数据
            List<IbfMarketFinance> financeList = ibfMarketFinanceService.list(new LambdaQueryWrapper<IbfMarketFinance>()
                .eq(IbfMarketFinance::getMonthCol, monthCol)
                .eq(IbfMarketFinance::getIsDeleted, 0)
                .eq(IbfMarketFinance::getIsPublish, 0)
            );

            // 删除发布记录
            ibfReportingSummary
                    .setIsDeleted(1);

            // 查询此月份为发布状态的记录
            IbfReportingSummary publishRecord = ibfReportingSummaryService.getOne(new LambdaQueryWrapper<IbfReportingSummary>()
                    .eq(IbfReportingSummary::getMonthCol, monthCol)
                    .eq(IbfReportingSummary::getIsPublish, 1)
            );
            if (Objects.nonNull(publishRecord)) {
                publishRecord.setIsCopy(0);
                list.add(publishRecord);
            }

            // 批量删除以上记录
            if (!resourceList.isEmpty()) {
                ibfMarketResourceService.removeBatchByIds(resourceList.stream().map(IbfMarketResource::getId).collect(Collectors.toList()));
            }
            if (!resourceGmvList.isEmpty()) {
                ibfMarketResourceGmvService.removeBatchByIds(resourceGmvList.stream().map(IbfMarketResourceGmv::getId).collect(Collectors.toList()));
            }
            if (!resourceFlowList.isEmpty()) {
                ibfMarketResourceFlowService.removeBatchByIds(resourceFlowList.stream().map(IbfMarketResourceFlow::getId).collect(Collectors.toList()));
            }
            if (!financeList.isEmpty()) {
                ibfMarketFinanceService.removeBatchByIds(financeList.stream().map(IbfMarketFinance::getId).collect(Collectors.toList()));
            }
            ibfReportingSummaryService.saveOrUpdateBatch(list);
        }
    }

    /**
     * 发布
     * @param ibfReportingSummary 待发布记录
     * @param loginUser 登录用户
     **/
    @Override
    public void publish(IbfReportingSummary ibfReportingSummary, LoginUser loginUser) {
        // 发布操作，将原先已发布记录置为过期且删除，原先发布数据删除
        String monthCol = ibfReportingSummary.getMonthCol();
        List<IbfReportingSummary> prepareList = new ArrayList<>(2);
        prepareList.add(ibfReportingSummary);

        List<IbfMarketResource> prepareResourceList = new ArrayList<>(16);
        List<IbfMarketResourceGmv> prepareResourceGmvList = new ArrayList<>(16);
        List<IbfMarketResourceFlow> prepareResourceFlowList = new ArrayList<>(16);
        List<IbfMarketFinance> prepareFinanceList = new ArrayList<>(16);

        // 查询出当前已发布月份的记录
        IbfReportingSummary publishRecord = ibfReportingSummaryService.getOne(new LambdaQueryWrapper<IbfReportingSummary>()
                .eq(IbfReportingSummary::getMonthCol, monthCol)
                .eq(IbfReportingSummary::getIsPublish, 1)
                .eq(IbfReportingSummary::getIsDeleted, 0)
        );
        if (Objects.nonNull(publishRecord)) {
            publishRecord.setIsCopy(0)
                         .setIsVisible(0)
                         .setIsPublish(2)
            ;
            prepareList.add(publishRecord);
        }
        ibfReportingSummary.setIsPublish(1)
                           .setIsCopy(0)
                           .setFlag(0)
                           .setIsVisible(1)
                           ;

        // 发布数据查询出来，后续删除
        List<IbfMarketResource> publishResourceList = ibfMarketResourceService.list(new LambdaQueryWrapper<IbfMarketResource>()
                .eq(IbfMarketResource::getMonthCol, monthCol)
                .eq(IbfMarketResource::getIsDeleted, 0)
                .eq(IbfMarketResource::getIsPublish, 1)
        );

        List<IbfMarketResourceGmv> publishResourceGmvList = ibfMarketResourceGmvService.list(new LambdaQueryWrapper<IbfMarketResourceGmv>()
                .eq(IbfMarketResourceGmv::getMonthCol, monthCol)
                .eq(IbfMarketResourceGmv::getIsDeleted, 0)
                .eq(IbfMarketResourceGmv::getIsPublish, 1)
        );

        List<IbfMarketResourceFlow> publishResourceFlowList = ibfMarketResourceFlowService.list(new LambdaQueryWrapper<IbfMarketResourceFlow>()
                .eq(IbfMarketResourceFlow::getMonthCol, monthCol)
                .eq(IbfMarketResourceFlow::getIsDeleted, 0)
                .eq(IbfMarketResourceFlow::getIsPublish, 1)
        );

        List<IbfMarketFinance> publishFinanceList = ibfMarketFinanceService.list(new LambdaQueryWrapper<IbfMarketFinance>()
                .eq(IbfMarketFinance::getMonthCol, monthCol)
                .eq(IbfMarketFinance::getIsDeleted, 0)
                .eq(IbfMarketFinance::getIsPublish, 1)
        );

        // 查询待发布数据，更新发布状态
        List<IbfMarketResource> preResourceList = ibfMarketResourceService.list(new LambdaQueryWrapper<IbfMarketResource>()
                .eq(IbfMarketResource::getMonthCol, monthCol)
                .eq(IbfMarketResource::getIsDeleted, 0)
                .eq(IbfMarketResource::getIsPublish, 0)
        );
        for (IbfMarketResource ibfMarketResource : preResourceList) {
            ibfMarketResource.manualPublish()
            ;
            prepareResourceList.add(ibfMarketResource);
        }
        List<IbfMarketResourceGmv> preResourceGmvList = ibfMarketResourceGmvService.list(new LambdaQueryWrapper<IbfMarketResourceGmv>()
                .eq(IbfMarketResourceGmv::getMonthCol, monthCol)
                .eq(IbfMarketResourceGmv::getIsDeleted, 0)
                .eq(IbfMarketResourceGmv::getIsPublish, 0)
        );
        for (IbfMarketResourceGmv ibfMarketResourceGmv : preResourceGmvList) {
            ibfMarketResourceGmv.manualPublish();
            prepareResourceGmvList.add(ibfMarketResourceGmv);
        }

        List<IbfMarketResourceFlow> preResourceFlowList = ibfMarketResourceFlowService.list(new LambdaQueryWrapper<IbfMarketResourceFlow>()
                .eq(IbfMarketResourceFlow::getMonthCol, monthCol)
                .eq(IbfMarketResourceFlow::getIsDeleted, 0)
                .eq(IbfMarketResourceFlow::getIsPublish, 0)
        );

        for (IbfMarketResourceFlow ibfMarketResourceFlow : preResourceFlowList) {
            ibfMarketResourceFlow.manualPublish();
            prepareResourceFlowList.add(ibfMarketResourceFlow);
        }

        List<IbfMarketFinance> preFinanceList = ibfMarketFinanceService.list(new LambdaQueryWrapper<IbfMarketFinance>()
                .eq(IbfMarketFinance::getMonthCol, monthCol)
                .eq(IbfMarketFinance::getIsDeleted, 0)
                .eq(IbfMarketFinance::getIsPublish, 0)
        );
        for (IbfMarketFinance ibfMarketFinance : preFinanceList) {
            ibfMarketFinance.manualPublish();
            prepareFinanceList.add(ibfMarketFinance);
        }

        // 批量删除已经发布数据
        ibfMarketResourceService.removeBatchByIds(publishResourceList.stream().map(IbfMarketResource::getId).collect(Collectors.toList()));
        ibfMarketResourceGmvService.removeBatchByIds(publishResourceGmvList.stream().map(IbfMarketResourceGmv::getId).collect(Collectors.toList()));
        ibfMarketResourceFlowService.removeBatchByIds(publishResourceFlowList.stream().map(IbfMarketResourceFlow::getId).collect(Collectors.toList()));
        ibfMarketFinanceService.removeBatchByIds(publishFinanceList.stream().map(IbfMarketFinance::getId).collect(Collectors.toList()));

        // 批量更新待发布数据为发布状态
        ibfMarketResourceService.saveOrUpdateBatch(prepareResourceList);
        ibfMarketResourceGmvService.saveOrUpdateBatch(prepareResourceGmvList);
        ibfMarketResourceFlowService.saveOrUpdateBatch(prepareResourceFlowList);
        ibfMarketFinanceService.saveOrUpdateBatch(prepareFinanceList);

        ibfReportingSummaryService.saveOrUpdateBatch(prepareList);
    }
}
