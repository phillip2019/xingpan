package org.jeecg.modules.ibf.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.aspect.annotation.Dict;
import org.jeecg.common.system.base.entity.JeecgEntity;
import org.jeecg.common.util.DateUtil;
import org.jeecg.modules.ibf.util.IbfDateUtil;
import org.jeecgframework.poi.excel.annotation.Excel;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import static org.jeecg.modules.ibf.IbfConst.SYSTEM_USER;

@Slf4j
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class IbfCommonEntity extends JeecgEntity implements Serializable {

    /**市场*/
    @Dict(dicCode = "finance_short_market_id")
    @Excel(name = "市场", width = 4, dicCode = "finance_short_market_id", type = 1)
    @ApiModelProperty(value = "市场ID")
    private String shortMarketId;

    /**所属年月 yyyy-MM*/
    @Excel(name = "月份", width = 8, importConvert = true, type = 1)
    @ApiModelProperty(value = "所属年月")
    private String monthCol;

    /**是否发布*/
    @Dict(dicCode = "is_publish")
    @Excel(name = "状态", width = 8, importConvert = true, type = 1, dicCode = "is_publish")
    @ApiModelProperty(value = "是否发布")
    private Integer isPublish;

    /**
     * 当前月份标记
     * 0: 当前月份
     * 1: 前1个月
     * */
    @ApiModelProperty(value = "当前月份标记")
    private Integer flag;

    /**
     * 当前版本,是否可见
     * 0: 不可见
     * 1: 可见
     * */
    @Dict(dicCode = "is_visible")
    @ApiModelProperty(value = "是否可见")
    private Integer isVisible;

    /**是否删除*/
    @Dict(dicCode = "is_deleted")
    @ApiModelProperty(value = "是否删除")
    private Integer isDeleted;

    public void convertsetMonthCol(String text) {
        this.monthCol = DateUtil.convertMonthCol(text);
    }

    /**
     * 特殊处理方法，某些参数需要转移，除特定的值
     * 将数据库中的值转换成VO
     **/
    public void customDB2VO() {
    }

    /**
     * 特殊处理方法，某些参数需要转移，乘特定的值
     * 将VO转换成DB值
     **/
    public void customVO2DB() {
    }

    /***
     * 初始化实体对象
     * is_public: 0
     * flag: 0
     * is_visible: 0
     * is_deleted: 0
     */
    public IbfCommonEntity init(Date date) {
        String curMonth = IbfDateUtil.getCurrentMonth();
        Integer flag = IbfDateUtil.calculateMonthDifference(curMonth, this.monthCol);
        this.isPublish = 0;
        this.flag = Integer.valueOf(1).equals(flag) ? 1 : 0;
        this.isVisible = 1;
        this.isDeleted = 0;
        this.setCreateBy(SYSTEM_USER);
        this.setCreateTime(date);
        return this;
    }

    /***
     * 自动发布
     * 已经到达自动发布时间，本月发布记录将自动发布
     * is_public: 1
     * flag: 0
     * is_visible: 1
     * is_deleted: 0
     */
    public IbfCommonEntity autoPublish() {
        this.isPublish = 1;
        this.flag = 0;
        this.isVisible = 1;
        this.setUpdateBy(SYSTEM_USER);
        return this;
    }

    /***
     * 自动下线
     * 已经到达自动发布时间，上月发布将自动下线
     * is_public: 1
     * flag: 0
     * is_visible: 0
     * is_deleted: 0
     */
    public IbfCommonEntity autoOffline() {
        this.isPublish = 1;
        this.flag = 0;
        this.isVisible = 0;
        this.setUpdateBy(SYSTEM_USER);
        return this;
    }

    /***
     * 自动过期失效
     * 已经到达自动发布时间，但是上月复制的发布，还未发布，则此发布和数据将自动过期失效
     * is_public: 3
     * flag: 0
     * is_visible: 0
     * is_deleted: 1
     */
    public IbfCommonEntity autExpire() {
        this.isPublish = 3;
        this.flag = 0;
        this.isVisible = 0;
        this.isDeleted = 1;
        this.setUpdateBy(SYSTEM_USER);
        return this;
    }

    /***
     * 手动复制
     * 拷贝对象，新创建对象
     * is_public: 0
     * is_visible: 1
     */
    public IbfCommonEntity manualCopy(Date date) {
        String curMonth = IbfDateUtil.getCurrentMonth();
        Integer flag = IbfDateUtil.calculateMonthDifference(curMonth, this.monthCol);
        this.isPublish = 0;
        this.flag = flag;
        this.isVisible = 1;
        this.setCreateTime(date);
        return this;
    }

    /***
     * 手动发布
     * 发布对象，原对象
     * is_public: 1
     * flag: 0
     * is_visible: 1
     */
    public IbfCommonEntity manualPublish() {
        this.isPublish = 1;
        this.flag = 0;
        this.isVisible = 1;
        return this;
    }


    public void convertsetIsPublish(Integer text) {
        this.isPublish = text;
    }
}
