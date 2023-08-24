package org.jeecg.modules.et.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: CK中实时埋点事件
 * @Author: jeecg-boot
 * @Date:   2023-08-24
 * @Version: V1.0
 */
@Data
@TableName("s_et_event_tracking_di_local")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="s_et_event_tracking_di_local对象", description="CK中实时埋点事件")
public class EtEventLocal implements Serializable {
    private static final long serialVersionUID = 1L;

	/**唯一键*/
	@Excel(name = "唯一键", width = 15)
    @ApiModelProperty(value = "唯一键")
    private java.lang.String uk;
	/**服务器记录埋点日志时间*/
	@Excel(name = "服务器记录埋点日志时间", width = 15)
    @ApiModelProperty(value = "服务器记录埋点日志时间")
    private java.lang.String timestamp;
	/**时区*/
	@Excel(name = "时区", width = 15)
    @ApiModelProperty(value = "时区")
    private java.lang.String timeZone;
	/**时区偏移量*/
	@Excel(name = "时区偏移量", width = 15)
    @ApiModelProperty(value = "时区偏移量")
    private java.lang.String scTimezoneOffset;
	/**埋点项目名称*/
	@Excel(name = "埋点项目名称", width = 15)
    @ApiModelProperty(value = "埋点项目名称")
    private java.lang.String project;
	/**平台语言*/
	@Excel(name = "平台语言", width = 15)
    @ApiModelProperty(value = "平台语言")
    private java.lang.String platformLang;
	/**平台类型*/
	@Excel(name = "平台类型", width = 15)
    @ApiModelProperty(value = "平台类型")
    private java.lang.String platformType;
	/**用户编号*/
	@Excel(name = "用户编号", width = 15)
    @ApiModelProperty(value = "用户编号")
    private java.lang.String distinctId;
	/**属性*/
	@Excel(name = "属性", width = 15)
    @ApiModelProperty(value = "属性")
    private java.lang.String properties;
	/**用户id*/
	@Excel(name = "用户id", width = 15)
    @ApiModelProperty(value = "用户id")
    private java.lang.String loginId;
	/**是否登录id*/
	@Excel(name = "是否登录id", width = 15)
    @ApiModelProperty(value = "是否登录id")
    private java.lang.String scIsLoginId;
	/**关联原始id*/
	@Excel(name = "关联原始id", width = 15)
    @ApiModelProperty(value = "关联原始id")
    private java.lang.String scTrackSignupOriginalId;
	/**是否首日访问*/
	@Excel(name = "是否首日访问", width = 15)
    @ApiModelProperty(value = "是否首日访问")
    private java.lang.String scIsFirstDay;
	/**是否首次触发事件*/
	@Excel(name = "是否首次触发事件", width = 15)
    @ApiModelProperty(value = "是否首次触发事件")
    private java.lang.String scIsFirstTime;
	/**设备编号*/
	@Excel(name = "设备编号", width = 15)
    @ApiModelProperty(value = "设备编号")
    private java.lang.String anonymousId;
	/**行为类型*/
	@Excel(name = "行为类型", width = 15)
    @ApiModelProperty(value = "行为类型")
    private java.lang.String type;
	/**事件名*/
	@Excel(name = "事件名", width = 15)
    @ApiModelProperty(value = "事件名")
    private java.lang.String event;
	/**事件中文名*/
	@TableField(exist = false)
	@Excel(name = "事件中文名", width = 15)
    @ApiModelProperty(value = "事件中文名")
    private java.lang.String eventZhName;
    /**事件*/
    @TableField(exist = false)
    @Excel(name = "事件", width = 15)
    @ApiModelProperty(value = "事件")
    private EtEvent et;
	/**事件时长*/
	@Excel(name = "事件时长", width = 15)
    @ApiModelProperty(value = "事件时长")
    private java.lang.String scEventDuration;
	/**事件发生的实际时间戳*/
	@Excel(name = "事件发生的实际时间戳", width = 15)
    @ApiModelProperty(value = "事件发生的实际时间戳")
    @TableField(value = "time")
    private java.lang.String createTime;
	/**事件唯一标识*/
	@Excel(name = "事件唯一标识", width = 15)
    @ApiModelProperty(value = "事件唯一标识")
    private java.lang.String scTrackId;
	/**sdk发送数据时的时间*/
	@Excel(name = "sdk发送数据时的时间", width = 15)
    @ApiModelProperty(value = "sdk发送数据时的时间")
    private java.lang.String scFlushTime;
	/**服务端接收到该条事件的时间*/
	@Excel(name = "服务端接收到该条事件的时间", width = 15)
    @ApiModelProperty(value = "服务端接收到该条事件的时间")
    private java.lang.String scReceiveTime;
	/**页面名称*/
	@Excel(name = "页面名称", width = 15)
    @ApiModelProperty(value = "页面名称")
    private java.lang.String scScreenName;
	/**内容标题*/
	@Excel(name = "内容标题", width = 15)
    @ApiModelProperty(value = "内容标题")
    private java.lang.String scTitle;
	/**视区高度*/
	@Excel(name = "视区高度", width = 15)
    @ApiModelProperty(value = "视区高度")
    private java.lang.String scViewportHeight;
	/**视区距顶部的位置*/
	@Excel(name = "视区距顶部的位置", width = 15)
    @ApiModelProperty(value = "视区距顶部的位置")
    private java.lang.String scViewportPosition;
	/**视区宽度*/
	@Excel(name = "视区宽度", width = 15)
    @ApiModelProperty(value = "视区宽度")
    private java.lang.String scViewportWidth;
	/**屏幕高度*/
	@Excel(name = "屏幕高度", width = 15)
    @ApiModelProperty(value = "屏幕高度")
    private java.lang.String scScreenHeight;
	/**屏幕宽度*/
	@Excel(name = "屏幕宽度", width = 15)
    @ApiModelProperty(value = "屏幕宽度")
    private java.lang.String scScreenWidth;
	/**屏幕方向*/
	@Excel(name = "屏幕方向", width = 15)
    @ApiModelProperty(value = "屏幕方向")
    private java.lang.String scScreenOrientation;
	/**启动场景*/
	@Excel(name = "启动场景", width = 15)
    @ApiModelProperty(value = "启动场景")
    private java.lang.String scScene;
	/**分享次数*/
	@Excel(name = "分享次数", width = 15)
    @ApiModelProperty(value = "分享次数")
    private java.lang.String scShareDepth;
	/**用户编号*/
	@Excel(name = "用户编号", width = 15)
    @ApiModelProperty(value = "用户编号")
    private java.lang.String scShareDistinctId;
	/**分享路径*/
	@Excel(name = "分享路径", width = 15)
    @ApiModelProperty(value = "分享路径")
    private java.lang.String scShareUrlPath;
	/**分享时途径*/
	@Excel(name = "分享时途径", width = 15)
    @ApiModelProperty(value = "分享时途径")
    private java.lang.String scShareMethod;
	/**来源应用包名*/
	@Excel(name = "来源应用包名", width = 15)
    @ApiModelProperty(value = "来源应用包名")
    private java.lang.String scSourcePackageName;
	/**实际访问链接*/
	@Excel(name = "实际访问链接", width = 15)
    @ApiModelProperty(value = "实际访问链接")
    private java.lang.String scUrl;
	/**页面参数*/
	@Excel(name = "页面参数", width = 15)
    @ApiModelProperty(value = "页面参数")
    private java.lang.String scUrlQuery;
	/**页面路径*/
	@Excel(name = "页面路径", width = 15)
    @ApiModelProperty(value = "页面路径")
    private java.lang.String scUrlPath;
	/**埋点上报的上一个地址链接*/
	@Excel(name = "埋点上报的上一个地址链接", width = 15)
    @ApiModelProperty(value = "埋点上报的上一个地址链接")
    private java.lang.String referrer;
	/**埋点上报的上一个地址链接*/
	@Excel(name = "埋点上报的上一个地址链接", width = 15)
    @ApiModelProperty(value = "埋点上报的上一个地址链接")
    private java.lang.String scReferrer;
	/**埋点上报的上一个地址域名*/
	@Excel(name = "埋点上报的上一个地址域名", width = 15)
    @ApiModelProperty(value = "埋点上报的上一个地址域名")
    private java.lang.String scReferrerHost;
	/**埋点上报的上一个页面标题*/
	@Excel(name = "埋点上报的上一个页面标题", width = 15)
    @ApiModelProperty(value = "埋点上报的上一个页面标题")
    private java.lang.String scReferrerTitle;
	/**埋点上报环境*/
	@Excel(name = "埋点上报环境", width = 15)
    @ApiModelProperty(value = "埋点上报环境")
    private java.lang.String remark;
	/**运营商*/
	@Excel(name = "运营商", width = 15)
    @ApiModelProperty(value = "运营商")
    private java.lang.String scCarrier;
	/**网络类型*/
	@Excel(name = "网络类型", width = 15)
    @ApiModelProperty(value = "网络类型")
    private java.lang.String scNetworkType;
	/**是否wifi*/
	@Excel(name = "是否wifi", width = 15)
    @ApiModelProperty(value = "是否wifi")
    private java.lang.String scWifi;
	/**设备品牌*/
	@Excel(name = "设备品牌", width = 15)
    @ApiModelProperty(value = "设备品牌")
    private java.lang.String scBrand;
	/**设备制造商*/
	@Excel(name = "设备制造商", width = 15)
    @ApiModelProperty(value = "设备制造商")
    private java.lang.String scManufacturer;
	/**机型*/
	@Excel(name = "机型", width = 15)
    @ApiModelProperty(value = "机型")
    private java.lang.String scModel;
	/**机型操作系统*/
	@Excel(name = "机型操作系统", width = 15)
    @ApiModelProperty(value = "机型操作系统")
    private java.lang.String scOs;
	/**操作系统版本*/
	@Excel(name = "操作系统版本", width = 15)
    @ApiModelProperty(value = "操作系统版本")
    private java.lang.String scOsVersion;
	/**app编号*/
	@Excel(name = "app编号", width = 15)
    @ApiModelProperty(value = "app编号")
    private java.lang.String scAppId;
	/**app名称*/
	@Excel(name = "app名称", width = 15)
    @ApiModelProperty(value = "app名称")
    private java.lang.String scAppName;
	/**app版本*/
	@Excel(name = "app版本", width = 15)
    @ApiModelProperty(value = "app版本")
    private java.lang.String scAppVersion;
	/**浏览器用户代理ua*/
	@Excel(name = "浏览器用户代理ua", width = 15)
    @ApiModelProperty(value = "浏览器用户代理ua")
    private java.lang.String scUserAgent;
	/**浏览器*/
	@Excel(name = "浏览器", width = 15)
    @ApiModelProperty(value = "浏览器")
    private java.lang.String scBrowser;
	/**浏览器版本*/
	@Excel(name = "浏览器版本", width = 15)
    @ApiModelProperty(value = "浏览器版本")
    private java.lang.String scBrowserVersion;
	/**ua*/
	@Excel(name = "ua", width = 15)
    @ApiModelProperty(value = "ua")
    private java.lang.String userAgent;
	/**浏览器解析平台类型*/
	@Excel(name = "浏览器解析平台类型", width = 15)
    @ApiModelProperty(value = "浏览器解析平台类型")
    private java.lang.String uaPlatform;
	/**浏览器解析浏览器*/
	@Excel(name = "浏览器解析浏览器", width = 15)
    @ApiModelProperty(value = "浏览器解析浏览器")
    private java.lang.String uaBrowser;
	/**浏览器版本*/
	@Excel(name = "浏览器版本", width = 15)
    @ApiModelProperty(value = "浏览器版本")
    private java.lang.String uaVersion;
	/**浏览器语言*/
	@Excel(name = "浏览器语言", width = 15)
    @ApiModelProperty(value = "浏览器语言")
    private java.lang.String uaLanguage;
	/**ip地址*/
	@Excel(name = "ip地址", width = 15)
    @ApiModelProperty(value = "ip地址")
    private java.lang.String ip;
	/**ip对应的洲*/
	@Excel(name = "ip对应的洲", width = 15)
    @ApiModelProperty(value = "ip对应的洲")
    private java.lang.String ipContinentNames;
	/**ip对应的国家*/
	@Excel(name = "ip对应的国家", width = 15)
    @ApiModelProperty(value = "ip对应的国家")
    private java.lang.String ipCountryName;
	/**ip对应的城市*/
	@Excel(name = "ip对应的城市", width = 15)
    @ApiModelProperty(value = "ip对应的城市")
    private java.lang.String ipCityName;
	/**IP*/
	@Excel(name = "IP", width = 15)
    @ApiModelProperty(value = "IP")
    private java.lang.String ipAsn;
	/**注册国家*/
	@Excel(name = "注册国家", width = 15)
    @ApiModelProperty(value = "注册国家")
    private java.lang.String registeredCountry;
	/**自治系统号*/
	@Excel(name = "自治系统号", width = 15)
    @ApiModelProperty(value = "自治系统号")
    private java.lang.String autonomousSystemNumber;
	/**自治系统组织名*/
	@Excel(name = "自治系统组织名", width = 15)
    @ApiModelProperty(value = "自治系统组织名")
    private java.lang.String autonomousSystemOrganization;
	/**经度*/
	@Excel(name = "经度", width = 15)
    @ApiModelProperty(value = "经度")
    private java.lang.String latitude;
	/**纬度*/
	@Excel(name = "纬度", width = 15)
    @ApiModelProperty(value = "纬度")
    private java.lang.String longitude;
	/**ip运营商*/
	@Excel(name = "ip运营商", width = 15)
    @ApiModelProperty(value = "ip运营商")
    private java.lang.String scIpIsp;
	/**sdk插件版本号*/
	@Excel(name = "sdk插件版本号", width = 15)
    @ApiModelProperty(value = "sdk插件版本号")
    private java.lang.String scLibPluginVersion;
	/**广告系列来源*/
	@Excel(name = "广告系列来源", width = 15)
    @ApiModelProperty(value = "广告系列来源")
    private java.lang.String scUtmSource;
	/**广告系列媒介*/
	@Excel(name = "广告系列媒介", width = 15)
    @ApiModelProperty(value = "广告系列媒介")
    private java.lang.String scUtmMedium;
	/**广告系列名称*/
	@Excel(name = "广告系列名称", width = 15)
    @ApiModelProperty(value = "广告系列名称")
    private java.lang.String scUtmCampaign;
	/**最近一次广告系列来源*/
	@Excel(name = "最近一次广告系列来源", width = 15)
    @ApiModelProperty(value = "最近一次广告系列来源")
    private java.lang.String scLatestUtmSource;
	/**最近一次广告系列媒介*/
	@Excel(name = "最近一次广告系列媒介", width = 15)
    @ApiModelProperty(value = "最近一次广告系列媒介")
    private java.lang.String scLatestUtmMedium;
	/**最近一次广告系列名称*/
	@Excel(name = "最近一次广告系列名称", width = 15)
    @ApiModelProperty(value = "最近一次广告系列名称")
    private java.lang.String scLatestUtmCampaign;
	/**最近一次广告系列内容*/
	@Excel(name = "最近一次广告系列内容", width = 15)
    @ApiModelProperty(value = "最近一次广告系列内容")
    private java.lang.String scLatestUtmContent;
	/**广告系列内容*/
	@Excel(name = "广告系列内容", width = 15)
    @ApiModelProperty(value = "广告系列内容")
    private java.lang.String scUtmContent;
	/**最近一次广告系列关键词*/
	@Excel(name = "最近一次广告系列关键词", width = 15)
    @ApiModelProperty(value = "最近一次广告系列关键词")
    private java.lang.String scLatestUtmTerm;
	/**渠道追踪匹配模式*/
	@Excel(name = "渠道追踪匹配模式", width = 15)
    @ApiModelProperty(value = "渠道追踪匹配模式")
    private java.lang.String scUtmMatchingType;
	/**广告系列关键词*/
	@Excel(name = "广告系列关键词", width = 15)
    @ApiModelProperty(value = "广告系列关键词")
    private java.lang.String scUtmTerm;
	/**渠道匹配关键字列表*/
	@Excel(name = "渠道匹配关键字列表", width = 15)
    @ApiModelProperty(value = "渠道匹配关键字列表")
    private java.lang.String scMatchingKeyList;
	/**短链目标地址*/
	@Excel(name = "短链目标地址", width = 15)
    @ApiModelProperty(value = "短链目标地址")
    private java.lang.String scShortUrlTarget;
	/**最近一次流量来源站外类型*/
	@Excel(name = "最近一次流量来源站外类型", width = 15)
    @ApiModelProperty(value = "最近一次流量来源站外类型")
    private java.lang.String scLatestTrafficSourceType;
	/**最近一次站外搜索引擎关键词*/
	@Excel(name = "最近一次站外搜索引擎关键词", width = 15)
    @ApiModelProperty(value = "最近一次站外搜索引擎关键词")
    private java.lang.String scLatestSearchKeyword;
	/**最近一次站外地址*/
	@Excel(name = "最近一次站外地址", width = 15)
    @ApiModelProperty(value = "最近一次站外地址")
    private java.lang.String scLatestReferrer;
	/**最近一次站外域名*/
	@Excel(name = "最近一次站外域名", width = 15)
    @ApiModelProperty(value = "最近一次站外域名")
    private java.lang.String scLatestReferrerHost;
	/**最近一次落地页*/
	@Excel(name = "最近一次落地页", width = 15)
    @ApiModelProperty(value = "最近一次落地页")
    private java.lang.String scLatestLandingPage;
	/**最近一次启动场景*/
	@Excel(name = "最近一次启动场景", width = 15)
    @ApiModelProperty(value = "最近一次启动场景")
    private java.lang.String scLatestScene;
	/**最近一次分享时途径*/
	@Excel(name = "最近一次分享时途径", width = 15)
    @ApiModelProperty(value = "最近一次分享时途径")
    private java.lang.String scLatestShareMethod;
	/**爬虫名称*/
	@Excel(name = "爬虫名称", width = 15)
    @ApiModelProperty(value = "爬虫名称")
    private java.lang.String scBotName;
	/**是否机刷*/
	@Excel(name = "是否机刷", width = 15)
    @ApiModelProperty(value = "是否机刷")
    private java.lang.String isMachineBrushTraffic;
	/**时间*/
	@Excel(name = "时间", width = 15, format = "yyyy-MM-dd")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "时间")
    @TableField(value = "date_col")
    private java.util.Date dateCol;
	/**日期分区*/
	@Excel(name = "日期分区", width = 15)
    @ApiModelProperty(value = "日期分区")
    private java.lang.String ds;
}
