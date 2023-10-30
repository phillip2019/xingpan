package org.jeecg.modules.et.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang.StringUtils;
import org.jeecg.common.util.JacksonBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;


/**
 * @author xiaowei.song
 */
public class EventTracking implements Serializable {
    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(EventTracking.class);

    private String timeZone;
    private String scTimezoneOffset;
    private String project;
    private String platformLang;
    private String platformType;
    private String distinctId;
    private String scDeviceId;
    private String deviceId;
    private String properties;
    private String loginId;
    private String scIsLoginId;
    private String scTrackSignupOriginalId;
    private String scIsFirstDay;
    private String scIsFirstTime;
    private String anonymousId;
    private String type;
    private String event;
    private String scEventDuration;
    private Long time;
    private Long scTrackId;
    private Long scFlushTime;
    private Long scReceiveTime;
    private String scScreenName;
    private String scTitle;
    private String scViewportHeight;
    private String scViewportPosition;
    private String scViewportWidth;
    private String scScreenHeight;
    private String scScreenWidth;
    private String scScreenOrientation;
    private String scScene;
    private String scShareDepth;
    private String scShareDistinctId;
    private String scShareUrlPath;
    private String scShareMethod;
    private String scSourcePackageName;
    private String scUrl;
    private String scUrlQuery;
    private String scUrlPath;
    private String referrer;
    private String scReferrer;
    private String scReferrerHost;
    private String scReferrerTitle;
    private String remark;
    private String scCarrier;
    private String scNetworkType;
    private String scWifi;
    private String scBrand;
    private String scManufacturer;
    private String scModel;
    private String scOsVersion;
    private String scOs;
    private String scAppVersion;
    private String scAppId;
    private String scAppName;
    private String scAppState;
    private String appCrashedReason;
    private String scUserAgent;
    private String scBrowser;
    private String scBrowserVersion;
    private String userAgent;
    private String uaPlatform;
    private String uaBrowser;
    private String uaVersion;
    private String uaLanguage;
    private String ip;
    private String ipIsGood;
    private String ipContinentNames;
    private String ipCountryName;
    private String ipCityName;
    private String ipCity;
    private String ipAsn;
    private String ipTraits;
    private String registeredCountry;
    private String autonomousSystemNumber;
    private String autonomousSystemOrganization;
    private String latitude;
    private String longitude;
    private String scLatitude;
    private String scLongitude;
    private String scIpIsp;
    private String scLib;
    private String scLibPluginVersion;
    private String scUtmSource;
    private String scUtmMedium;
    private String scUtmCampaign;
    private String scUtmContent;
    private String scLatestUtmSource;
    private String scLatestUtmMedium;
    private String scLatestUtmCampaign;
    private String scLatestUtmContent;
    private String scLatestUtmTerm;
    private String scUtmMatchingType;
    private String scUtmTerm;
    private String scMatchedKey;
    private String scMatchingKeyList;
    private String scShortUrlKey;
    private String scShortUrlTarget;
    private String scLatestTrafficSourceType;
    private String scLatestSearchKeyword;
    private String scLatestReferrer;
    private String scLatestReferrerHost;
    private String scLatestLandingPage;
    private String scLatestScene;
    private String scLatestShareMethod;
    private String scIosInstallSource;
    private String scChannelDeviceInfo;
    private String scIosInstallDisableCallback;
    private String scIsChannelCallbackEvent;
    private String scChannelExtraInformation;
    private String scItemJoin;
    private String scBotName;
    private String scIsValid;
    private String allJson;

    /**
     * 字符串解析eventTracking实体
     * @param message 消息 内容
     * @return EventTracking 实例
     **/
    public static EventTracking of(String message) {
        EventTracking et = new EventTracking();
        JsonNode messageJn = null;
        try {
            messageJn = JacksonBuilder.MAPPER.readTree(message);
        } catch (JsonProcessingException e) {
            logger.error("反序列化埋点数据失败， 原始内容为： {}", message);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            System.exit(-1);
        }
        JsonNode dataJn = messageJn.path("data");
        String allJsonStr = null;
        try {
            allJsonStr = JacksonBuilder.MAPPER.writeValueAsString(dataJn);
        } catch (JsonProcessingException e) {
            logger.error("序列化埋点data属性数据失败， 原始内容为： {}", dataJn);
        }
        JsonNode ipCityJn = dataJn.path("ip_city");
        JsonNode locationJn = ipCityJn.path("location");
        JsonNode dataDecodeJn = dataJn.path("data_decode");
        JsonNode propertiesJn = dataDecodeJn.path("properties");
        String propertiesStr = null;
        String ipCity = null;
        String ipAsn = null;
        String ipTraits = null;
        String anonymousId = dataDecodeJn.path("anonymous_id").asText();
        try {
            propertiesStr = JacksonBuilder.MAPPER.writeValueAsString(propertiesJn);
        } catch (JsonProcessingException e) {
            logger.error("序列化埋点属性数据失败， 原始内容为： {}", propertiesJn);
        }

        try {
            ipCity = JacksonBuilder.MAPPER.writeValueAsString(ipCityJn);
        } catch (JsonProcessingException e) {
            logger.error("序列化埋点ip归属地数据失败， 原始内容为： {}", ipCityJn);
        }

        try {
            ipAsn = JacksonBuilder.MAPPER.writeValueAsString(dataJn.path("ip_asn"));
        } catch (JsonProcessingException e) {
            logger.error("序列化埋点ip asn数据失败， 原始内容为： {}", dataJn.path("ip_asn"));
        }

        try {
            ipTraits = JacksonBuilder.MAPPER.writeValueAsString(ipCityJn.path("traits"));
        } catch (JsonProcessingException e) {
            logger.error("序列化埋点ip注册数据失败， 原始内容为： {}", ipCityJn.path("traits"));
        }

        et.setTimeZone(locationJn.path("time_zone").asText())
                .setScTimezoneOffset(propertiesJn.path("$timezone_offset").asText())
                .setProject(dataJn.path("project").asText())
                .setPlatformLang(propertiesJn.path("platform_lang").asText())
                .setPlatformType(propertiesJn.path("platform_type").asText())
                .setDistinctId(dataDecodeJn.path("distinct_id").asText())
                .setScDeviceId(propertiesJn.path("$device_id").asText())
                .setDeviceId(propertiesJn.path("device_id").asText())
                .setProperties(propertiesStr)
                .setLoginId(dataDecodeJn.path("login_id").asText())
                .setScIsLoginId(propertiesJn.path("$is_login_id").asText())
                .setScTrackSignupOriginalId(propertiesJn.path("$track_signup_original_id").asText())
                .setScIsFirstDay(propertiesJn.path("$is_first_day").asText())
                .setScIsFirstTime(propertiesJn.path("$is_first_time").asText())
                .setAnonymousId(anonymousId)
                .setType(dataDecodeJn.path("type").asText())
                .setEvent(StringUtils.trim(dataDecodeJn.path("event").asText()))
                .setScEventDuration(propertiesJn.path("event_duration").asText())
                .setTime(dataDecodeJn.path("time").asLong())
                .setScTrackId(dataDecodeJn.path("_track_id").asLong())
                .setScFlushTime(dataDecodeJn.path("_flush_time").asLong())
                .setScReceiveTime(propertiesJn.path("$receive_time").asLong())
                .setScScreenName(propertiesJn.path("$screen_name").asText())
                .setScTitle(propertiesJn.path("$title").asText())
                .setScViewportHeight(propertiesJn.path("$viewport_height").asText())
                .setScViewportPosition(propertiesJn.path("$viewport_position").asText())
                .setScViewportWidth(propertiesJn.path("$viewport_width").asText())
                .setScScreenHeight(propertiesJn.path("$screen_height").asText())
                .setScScreenWidth(propertiesJn.path("$screen_width").asText())
                .setScScreenOrientation(propertiesJn.path("$screen_orientation").asText())
                .setScScene(propertiesJn.path("$scene").asText())
                .setScShareDepth(propertiesJn.path("$share_depth").asText())
                .setScShareDistinctId(propertiesJn.path("$share_distinct_id").asText())
                .setScShareUrlPath(propertiesJn.path("$share_url_path").asText())
                .setScShareMethod(propertiesJn.path("$share_method").asText())
                .setScSourcePackageName(propertiesJn.path("$source_package_name").asText())
                .setScUrl(propertiesJn.path("$url").asText())
                .setScUrlQuery(propertiesJn.path("$url_query").asText())
                .setScUrlPath(propertiesJn.path("$url_path").asText())
                .setReferrer(dataJn.path("referrer").asText())
                .setScReferrer(propertiesJn.path("referrer").asText())
                .setScReferrer(propertiesJn.path("$referrer").asText())
                .setScReferrerHost(propertiesJn.path("$referrer_host").asText())
                .setScReferrerTitle(propertiesJn.path("$referrer_title").asText())
                .setRemark(dataJn.path("remark").asText())
                .setScCarrier(propertiesJn.path("$carrier").asText())
                .setScNetworkType(propertiesJn.path("$network_type").asText())
                .setScWifi(propertiesJn.path("$wifi").asText())
                .setScBrand(propertiesJn.path("$brand").asText())
                .setScManufacturer(propertiesJn.path("$manufacturer").asText())
                .setScModel(propertiesJn.path("$model").asText())
                .setScOsVersion(propertiesJn.path("$os_version").asText())
                .setScOs(propertiesJn.path("$os").asText())
                .setScAppVersion(propertiesJn.path("$app_version").asText())
                .setScAppId(propertiesJn.path("$app_id").asText())
                .setScAppName(propertiesJn.path("$app_name").asText())
                .setScAppState(propertiesJn.path("$app_state").asText())
                .setAppCrashedReason(propertiesJn.path("app_crashed_reason").asText())
                .setScUserAgent(propertiesJn.path("$user_agent").asText())
                .setScBrowser(propertiesJn.path("$browser").asText())
                .setScBrowserVersion(propertiesJn.path("$browser_version").asText())
                .setUserAgent(dataJn.path("User_Agent").asText())
                .setUaPlatform(dataJn.path("ua_platform").asText())
                .setUaBrowser(dataJn.path("ua_browser").asText())
                .setUaVersion(dataJn.path("ua_version").asText())
                .setUaLanguage(dataJn.path("ua_language").asText())
                .setIp(dataJn.path("ip").asText())
                .setIpIsGood(dataJn.path("ip_is_good").asText())
                .setIpContinentNames(ipCityJn.at("/continent/names/zh-CN").asText())
                .setIpCountryName(ipCityJn.at("/country/names/zh-CN").asText())
                .setIpCityName(ipCityJn.at("/city/names/zh-CN").asText())
                .setIpCity(ipCity)
                .setIpAsn(ipAsn)
                .setIpTraits(ipTraits)
                .setRegisteredCountry(ipCityJn.at("/registered_country/names/zh-CN").asText())
                .setAutonomousSystemNumber(dataJn.at("/ip_asn/autonomous_system_number").asText())
                .setAutonomousSystemOrganization(dataJn.at("/ip_asn/autonomous_system_organization").asText())
                .setLatitude(locationJn.path("latitude").asText())
                .setLongitude(locationJn.path("longitude").asText())
                .setScLatitude(propertiesJn.path("$latitude").asText())
                .setScLongitude(propertiesJn.path("$longitude").asText())
                .setScIpIsp(propertiesJn.path("$ip_isp").asText())
                .setScLib(propertiesJn.path("$lib").asText())
                .setScLibPluginVersion(propertiesJn.path("$lib_plugin_version").asText())
                .setScUtmSource(propertiesJn.path("$utm_source").asText())
                .setScUtmMedium(propertiesJn.path("$utm_medium").asText())
                .setScUtmCampaign(propertiesJn.path("$utm_campaign").asText())
                .setScUtmContent(propertiesJn.path("$utm_content").asText())
                .setScLatestUtmSource(propertiesJn.path("$latest_utm_source").asText())
                .setScLatestUtmMedium(propertiesJn.path("$latest_utm_medium").asText())
                .setScLatestUtmCampaign(propertiesJn.path("$latest_utm_campaign").asText())
                .setScLatestUtmContent(propertiesJn.path("$latest_utm_content").asText())
                .setScLatestUtmTerm(propertiesJn.path("$latest_utm_term").asText())
                .setScUtmMatchingType(propertiesJn.path("$utm_matching_type").asText())
                .setScUtmTerm(propertiesJn.path("$utm_term").asText())
                .setScMatchedKey(propertiesJn.path("$matched_key").asText())
                .setScMatchingKeyList(propertiesJn.path("$matching_key_list").asText())
                .setScShortUrlKey(propertiesJn.path("$short_url_key").asText())
                .setScShortUrlTarget(propertiesJn.path("$short_url_target").asText())
                .setScLatestTrafficSourceType(propertiesJn.path("$latest_traffic_source_type").asText())
                .setScLatestSearchKeyword(propertiesJn.path("$latest_search_keyword").asText())
                .setScLatestReferrer(propertiesJn.path("$latest_referrer").asText())
                .setScLatestReferrerHost(propertiesJn.path("$latest_referrer_host").asText())
                .setScLatestLandingPage(propertiesJn.path("$latest_landing_page").asText())
                .setScLatestScene(propertiesJn.path("$latest_scene").asText())
                .setScLatestShareMethod(propertiesJn.path("$latest_share_method").asText())
                .setScIosInstallSource(propertiesJn.path("$ios_install_source").asText())
                .setScChannelDeviceInfo(propertiesJn.path("$channel_device_info").asText())
                .setScIosInstallDisableCallback(propertiesJn.path("$ios_install_disable_callback").asText())
                .setScIsChannelCallbackEvent(propertiesJn.path("$is_channel_callback_event").asText())
                .setScChannelExtraInformation(propertiesJn.path("$channel_extra_information").asText())
                .setScItemJoin(propertiesJn.path("$item_join").asText())
                .setScBotName(propertiesJn.path("$bot_name").asText())
                .setScIsValid(propertiesJn.path("$is_valid").asText())
                .setAllJson(allJsonStr)
        ;
        return et;
    }

    /**
     * 将etChinagoods转换成chinagoods实体内容
     **/
    public EtChinagoods toChinagoods() {
        EtChinagoods etChinagoods = new EtChinagoods();
        etChinagoods.setTrackId(this.getScTrackId())
                .setDistinctId(this.getDistinctId())
                .setLib(this.getScLib())
                .setType(this.getType())
                .setReferrer(this.getReferrer())
                .setEvent(this.getEvent())
                .setAllJson(this.getAllJson())
                .setHost(this.getScReferrerHost())
                .setUserAgent(this.getUserAgent())
                .setUaPlatform(this.getUaPlatform())
                .setUaBrowser(this.getUaBrowser())
                .setUaVersion(this.getUaVersion())
                .setUaLanguage(this.getUaLanguage())
                .setIp(this.getIp())
                .setIpCity(this.getIpCity())
                .setUrl(this.getScUrl())
                .setRemark(this.getRemark())
                .setCreatedAt(String.valueOf(this.getTime()))
                .setProject(this.getProject())
                .setPlatformType(this.getPlatformType())
                .setAnonymousId(this.getAnonymousId())
        ;
        return etChinagoods;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public EventTracking setTimeZone(String timeZone) {
        this.timeZone = timeZone;
        return this;
    }

    public String getScTimezoneOffset() {
        return scTimezoneOffset;
    }

    public EventTracking setScTimezoneOffset(String scTimezoneOffset) {
        this.scTimezoneOffset = scTimezoneOffset;
        return this;
    }

    public String getProject() {
        return project;
    }

    public EventTracking setProject(String project) {
        this.project = project;
        return this;
    }

    public String getPlatformLang() {
        return platformLang;
    }

    public EventTracking setPlatformLang(String platformLang) {
        this.platformLang = platformLang;
        return this;
    }

    public String getPlatformType() {
        return platformType;
    }

    public EventTracking setPlatformType(String platformType) {
        this.platformType = platformType;
        return this;
    }

    public String getDistinctId() {
        return distinctId;
    }

    public EventTracking setDistinctId(String distinctId) {
        this.distinctId = distinctId;
        return this;
    }

    public String getScDeviceId() {
        return scDeviceId;
    }

    public EventTracking setScDeviceId(String scDeviceId) {
        this.scDeviceId = scDeviceId;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public EventTracking setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getProperties() {
        return properties;
    }

    public EventTracking setProperties(String properties) {
        this.properties = properties;
        return this;
    }

    public String getLoginId() {
        return loginId;
    }

    public EventTracking setLoginId(String loginId) {
        this.loginId = loginId;
        return this;
    }

    public String getScIsLoginId() {
        return scIsLoginId;
    }

    public EventTracking setScIsLoginId(String scIsLoginId) {
        this.scIsLoginId = scIsLoginId;
        return this;
    }

    public String getScLib() {
        return scLib;
    }

    public EventTracking setScLib(String scLib) {
        this.scLib = scLib;
        return this;
    }

    public String getScTrackSignupOriginalId() {
        return scTrackSignupOriginalId;
    }

    public EventTracking setScTrackSignupOriginalId(String scTrackSignupOriginalId) {
        this.scTrackSignupOriginalId = scTrackSignupOriginalId;
        return this;
    }

    public String getScIsFirstDay() {
        return scIsFirstDay;
    }

    public EventTracking setScIsFirstDay(String scIsFirstDay) {
        this.scIsFirstDay = scIsFirstDay;
        return this;
    }

    public String getScIsFirstTime() {
        return scIsFirstTime;
    }

    public EventTracking setScIsFirstTime(String scIsFirstTime) {
        this.scIsFirstTime = scIsFirstTime;
        return this;
    }

    public String getAnonymousId() {
        return anonymousId;
    }

    public EventTracking setAnonymousId(String anonymousId) {
        this.anonymousId = anonymousId;
        return this;
    }

    public String getType() {
        return type;
    }

    public EventTracking setType(String type) {
        this.type = type;
        return this;
    }

    public String getEvent() {
        return event;
    }

    public EventTracking setEvent(String event) {
        this.event = event;
        return this;
    }

    public String getScEventDuration() {
        return scEventDuration;
    }

    public EventTracking setScEventDuration(String scEventDuration) {
        this.scEventDuration = scEventDuration;
        return this;
    }

    public Long getTime() {
        return time;
    }

    public EventTracking setTime(Long time) {
        this.time = time;
        return this;
    }

    public Long getScTrackId() {
        return scTrackId;
    }

    public EventTracking setScTrackId(Long scTrackId) {
        this.scTrackId = scTrackId;
        return this;
    }

    public Long getScFlushTime() {
        return scFlushTime;
    }

    public EventTracking setScFlushTime(Long scFlushTime) {
        this.scFlushTime = scFlushTime;
        return this;
    }

    public Long getScReceiveTime() {
        return scReceiveTime;
    }

    public EventTracking setScReceiveTime(Long scReceiveTime) {
        this.scReceiveTime = scReceiveTime;
        return this;
    }

    public String getScScreenName() {
        return scScreenName;
    }

    public EventTracking setScScreenName(String scScreenName) {
        this.scScreenName = scScreenName;
        return this;
    }

    public String getScTitle() {
        return scTitle;
    }

    public EventTracking setScTitle(String scTitle) {
        this.scTitle = scTitle;
        return this;
    }

    public String getScViewportHeight() {
        return scViewportHeight;
    }

    public EventTracking setScViewportHeight(String scViewportHeight) {
        this.scViewportHeight = scViewportHeight;
        return this;
    }

    public String getScViewportPosition() {
        return scViewportPosition;
    }

    public EventTracking setScViewportPosition(String scViewportPosition) {
        this.scViewportPosition = scViewportPosition;
        return this;
    }

    public String getScViewportWidth() {
        return scViewportWidth;
    }

    public EventTracking setScViewportWidth(String scViewportWidth) {
        this.scViewportWidth = scViewportWidth;
        return this;
    }

    public String getScScreenHeight() {
        return scScreenHeight;
    }

    public EventTracking setScScreenHeight(String scScreenHeight) {
        this.scScreenHeight = scScreenHeight;
        return this;
    }

    public String getScScreenWidth() {
        return scScreenWidth;
    }

    public EventTracking setScScreenWidth(String scScreenWidth) {
        this.scScreenWidth = scScreenWidth;
        return this;
    }

    public String getScScreenOrientation() {
        return scScreenOrientation;
    }

    public EventTracking setScScreenOrientation(String scScreenOrientation) {
        this.scScreenOrientation = scScreenOrientation;
        return this;
    }

    public String getScScene() {
        return scScene;
    }

    public EventTracking setScScene(String scScene) {
        this.scScene = scScene;
        return this;
    }

    public String getScShareDepth() {
        return scShareDepth;
    }

    public EventTracking setScShareDepth(String scShareDepth) {
        this.scShareDepth = scShareDepth;
        return this;
    }

    public String getScShareDistinctId() {
        return scShareDistinctId;
    }

    public EventTracking setScShareDistinctId(String scShareDistinctId) {
        this.scShareDistinctId = scShareDistinctId;
        return this;
    }

    public String getScShareUrlPath() {
        return scShareUrlPath;
    }

    public EventTracking setScShareUrlPath(String scShareUrlPath) {
        this.scShareUrlPath = scShareUrlPath;
        return this;
    }

    public String getScShareMethod() {
        return scShareMethod;
    }

    public EventTracking setScShareMethod(String scShareMethod) {
        this.scShareMethod = scShareMethod;
        return this;
    }

    public String getScSourcePackageName() {
        return scSourcePackageName;
    }

    public EventTracking setScSourcePackageName(String scSourcePackageName) {
        this.scSourcePackageName = scSourcePackageName;
        return this;
    }

    public String getScUrl() {
        return scUrl;
    }

    public EventTracking setScUrl(String scUrl) {
        this.scUrl = scUrl;
        return this;
    }

    public String getScUrlQuery() {
        return scUrlQuery;
    }

    public EventTracking setScUrlQuery(String scUrlQuery) {
        this.scUrlQuery = scUrlQuery;
        return this;
    }

    public String getScUrlPath() {
        return scUrlPath;
    }

    public EventTracking setScUrlPath(String scUrlPath) {
        this.scUrlPath = scUrlPath;
        return this;
    }

    public String getReferrer() {
        return referrer;
    }

    public EventTracking setReferrer(String referrer) {
        this.referrer = referrer;
        return this;
    }

    public String getScReferrer() {
        return scReferrer;
    }

    public EventTracking setScReferrer(String scReferrer) {
        this.scReferrer = scReferrer;
        return this;
    }

    public String getScReferrerHost() {
        return scReferrerHost;
    }

    public EventTracking setScReferrerHost(String scReferrerHost) {
        this.scReferrerHost = scReferrerHost;
        return this;
    }

    public String getScReferrerTitle() {
        return scReferrerTitle;
    }

    public EventTracking setScReferrerTitle(String scReferrerTitle) {
        this.scReferrerTitle = scReferrerTitle;
        return this;
    }

    public String getRemark() {
        return remark;
    }

    public EventTracking setRemark(String remark) {
        this.remark = remark;
        return this;
    }

    public String getScCarrier() {
        return scCarrier;
    }

    public EventTracking setScCarrier(String scCarrier) {
        this.scCarrier = scCarrier;
        return this;
    }

    public String getScNetworkType() {
        return scNetworkType;
    }

    public EventTracking setScNetworkType(String scNetworkType) {
        this.scNetworkType = scNetworkType;
        return this;
    }

    public String getScWifi() {
        return scWifi;
    }

    public EventTracking setScWifi(String scWifi) {
        this.scWifi = scWifi;
        return this;
    }

    public String getScBrand() {
        return scBrand;
    }

    public EventTracking setScBrand(String scBrand) {
        this.scBrand = scBrand;
        return this;
    }

    public String getScManufacturer() {
        return scManufacturer;
    }

    public EventTracking setScManufacturer(String scManufacturer) {
        this.scManufacturer = scManufacturer;
        return this;
    }

    public String getScModel() {
        return scModel;
    }

    public EventTracking setScModel(String scModel) {
        this.scModel = scModel;
        return this;
    }

    public String getScOsVersion() {
        return scOsVersion;
    }

    public EventTracking setScOsVersion(String scOsVersion) {
        this.scOsVersion = scOsVersion;
        return this;
    }

    public String getScOs() {
        return scOs;
    }

    public EventTracking setScOs(String scOs) {
        this.scOs = scOs;
        return this;
    }

    public String getScAppVersion() {
        return scAppVersion;
    }

    public EventTracking setScAppVersion(String scAppVersion) {
        this.scAppVersion = scAppVersion;
        return this;
    }

    public String getScAppId() {
        return scAppId;
    }

    public EventTracking setScAppId(String scAppId) {
        this.scAppId = scAppId;
        return this;
    }

    public String getScAppName() {
        return scAppName;
    }

    public EventTracking setScAppName(String scAppName) {
        this.scAppName = scAppName;
        return this;
    }

    public String getScAppState() {
        return scAppState;
    }

    public EventTracking setScAppState(String scAppState) {
        this.scAppState = scAppState;
        return this;
    }

    public String getAppCrashedReason() {
        return appCrashedReason;
    }

    public EventTracking setAppCrashedReason(String appCrashedReason) {
        this.appCrashedReason = appCrashedReason;
        return this;
    }

    public String getScUserAgent() {
        return scUserAgent;
    }

    public EventTracking setScUserAgent(String scUserAgent) {
        this.scUserAgent = scUserAgent;
        return this;
    }

    public String getScBrowser() {
        return scBrowser;
    }

    public EventTracking setScBrowser(String scBrowser) {
        this.scBrowser = scBrowser;
        return this;
    }

    public String getScBrowserVersion() {
        return scBrowserVersion;
    }

    public EventTracking setScBrowserVersion(String scBrowserVersion) {
        this.scBrowserVersion = scBrowserVersion;
        return this;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public EventTracking setUserAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public String getUaPlatform() {
        return uaPlatform;
    }

    public EventTracking setUaPlatform(String uaPlatform) {
        this.uaPlatform = uaPlatform;
        return this;
    }

    public String getUaBrowser() {
        return uaBrowser;
    }

    public EventTracking setUaBrowser(String uaBrowser) {
        this.uaBrowser = uaBrowser;
        return this;
    }

    public String getUaVersion() {
        return uaVersion;
    }

    public EventTracking setUaVersion(String uaVersion) {
        this.uaVersion = uaVersion;
        return this;
    }

    public String getUaLanguage() {
        return uaLanguage;
    }

    public EventTracking setUaLanguage(String uaLanguage) {
        this.uaLanguage = uaLanguage;
        return this;
    }

    public String getIp() {
        return ip;
    }

    public EventTracking setIp(String ip) {
        this.ip = ip;
        return this;
    }

    public String getIpIsGood() {
        return ipIsGood;
    }

    public EventTracking setIpIsGood(String ipIsGood) {
        this.ipIsGood = ipIsGood;
        return this;
    }

    public String getIpContinentNames() {
        return ipContinentNames;
    }

    public EventTracking setIpContinentNames(String ipContinentNames) {
        this.ipContinentNames = ipContinentNames;
        return this;
    }

    public String getIpCountryName() {
        return ipCountryName;
    }

    public EventTracking setIpCountryName(String ipCountryName) {
        this.ipCountryName = ipCountryName;
        return this;
    }

    public String getIpCityName() {
        return ipCityName;
    }

    public EventTracking setIpCityName(String ipCityName) {
        this.ipCityName = ipCityName;
        return this;
    }

    public String getIpCity() {
        return ipCity;
    }

    public EventTracking setIpCity(String ipCity) {
        this.ipCity = ipCity;
        return this;
    }

    public String getIpAsn() {
        return ipAsn;
    }

    public EventTracking setIpAsn(String ipAsn) {
        this.ipAsn = ipAsn;
        return this;
    }

    public String getIpTraits() {
        return ipTraits;
    }

    public EventTracking setIpTraits(String ipTraits) {
        this.ipTraits = ipTraits;
        return this;
    }

    public String getRegisteredCountry() {
        return registeredCountry;
    }

    public EventTracking setRegisteredCountry(String registeredCountry) {
        this.registeredCountry = registeredCountry;
        return this;
    }

    public String getAutonomousSystemNumber() {
        return autonomousSystemNumber;
    }

    public EventTracking setAutonomousSystemNumber(String autonomousSystemNumber) {
        this.autonomousSystemNumber = autonomousSystemNumber;
        return this;
    }

    public String getAutonomousSystemOrganization() {
        return autonomousSystemOrganization;
    }

    public EventTracking setAutonomousSystemOrganization(String autonomousSystemOrganization) {
        this.autonomousSystemOrganization = autonomousSystemOrganization;
        return this;
    }

    public String getLatitude() {
        return latitude;
    }

    public EventTracking setLatitude(String latitude) {
        this.latitude = latitude;
        return this;
    }

    public String getLongitude() {
        return longitude;
    }

    public EventTracking setLongitude(String longitude) {
        this.longitude = longitude;
        return this;
    }

    public String getScLatitude() {
        return scLatitude;
    }

    public EventTracking setScLatitude(String scLatitude) {
        this.scLatitude = scLatitude;
        return this;
    }

    public String getScLongitude() {
        return scLongitude;
    }

    public EventTracking setScLongitude(String scLongitude) {
        this.scLongitude = scLongitude;
        return this;
    }

    public String getScIpIsp() {
        return scIpIsp;
    }

    public EventTracking setScIpIsp(String scIpIsp) {
        this.scIpIsp = scIpIsp;
        return this;
    }

    public String getScLibPluginVersion() {
        return scLibPluginVersion;
    }

    public EventTracking setScLibPluginVersion(String scLibPluginVersion) {
        this.scLibPluginVersion = scLibPluginVersion;
        return this;
    }

    public String getScUtmSource() {
        return scUtmSource;
    }

    public EventTracking setScUtmSource(String scUtmSource) {
        this.scUtmSource = scUtmSource;
        return this;
    }

    public String getScUtmMedium() {
        return scUtmMedium;
    }

    public EventTracking setScUtmMedium(String scUtmMedium) {
        this.scUtmMedium = scUtmMedium;
        return this;
    }

    public String getScUtmCampaign() {
        return scUtmCampaign;
    }

    public EventTracking setScUtmCampaign(String scUtmCampaign) {
        this.scUtmCampaign = scUtmCampaign;
        return this;
    }

    public String getScUtmContent() {
        return scUtmContent;
    }

    public EventTracking setScUtmContent(String scUtmContent) {
        this.scUtmContent = scUtmContent;
        return this;
    }

    public String getScLatestUtmSource() {
        return scLatestUtmSource;
    }

    public EventTracking setScLatestUtmSource(String scLatestUtmSource) {
        this.scLatestUtmSource = scLatestUtmSource;
        return this;
    }

    public String getScLatestUtmMedium() {
        return scLatestUtmMedium;
    }

    public EventTracking setScLatestUtmMedium(String scLatestUtmMedium) {
        this.scLatestUtmMedium = scLatestUtmMedium;
        return this;
    }

    public String getScLatestUtmCampaign() {
        return scLatestUtmCampaign;
    }

    public EventTracking setScLatestUtmCampaign(String scLatestUtmCampaign) {
        this.scLatestUtmCampaign = scLatestUtmCampaign;
        return this;
    }

    public String getScLatestUtmContent() {
        return scLatestUtmContent;
    }

    public EventTracking setScLatestUtmContent(String scLatestUtmContent) {
        this.scLatestUtmContent = scLatestUtmContent;
        return this;
    }

    public String getScLatestUtmTerm() {
        return scLatestUtmTerm;
    }

    public EventTracking setScLatestUtmTerm(String scLatestUtmTerm) {
        this.scLatestUtmTerm = scLatestUtmTerm;
        return this;
    }

    public String getScUtmMatchingType() {
        return scUtmMatchingType;
    }

    public EventTracking setScUtmMatchingType(String scUtmMatchingType) {
        this.scUtmMatchingType = scUtmMatchingType;
        return this;
    }

    public String getScUtmTerm() {
        return scUtmTerm;
    }

    public EventTracking setScUtmTerm(String scUtmTerm) {
        this.scUtmTerm = scUtmTerm;
        return this;
    }

    public String getScMatchedKey() {
        return scMatchedKey;
    }

    public EventTracking setScMatchedKey(String scMatchedKey) {
        this.scMatchedKey = scMatchedKey;
        return this;
    }

    public String getScMatchingKeyList() {
        return scMatchingKeyList;
    }

    public EventTracking setScMatchingKeyList(String scMatchingKeyList) {
        this.scMatchingKeyList = scMatchingKeyList;
        return this;
    }

    public String getScShortUrlKey() {
        return scShortUrlKey;
    }

    public EventTracking setScShortUrlKey(String scShortUrlKey) {
        this.scShortUrlKey = scShortUrlKey;
        return this;
    }

    public String getScShortUrlTarget() {
        return scShortUrlTarget;
    }

    public EventTracking setScShortUrlTarget(String scShortUrlTarget) {
        this.scShortUrlTarget = scShortUrlTarget;
        return this;
    }

    public String getScLatestTrafficSourceType() {
        return scLatestTrafficSourceType;
    }

    public EventTracking setScLatestTrafficSourceType(String scLatestTrafficSourceType) {
        this.scLatestTrafficSourceType = scLatestTrafficSourceType;
        return this;
    }

    public String getScLatestSearchKeyword() {
        return scLatestSearchKeyword;
    }

    public EventTracking setScLatestSearchKeyword(String scLatestSearchKeyword) {
        this.scLatestSearchKeyword = scLatestSearchKeyword;
        return this;
    }

    public String getScLatestReferrer() {
        return scLatestReferrer;
    }

    public EventTracking setScLatestReferrer(String scLatestReferrer) {
        this.scLatestReferrer = scLatestReferrer;
        return this;
    }

    public String getScLatestReferrerHost() {
        return scLatestReferrerHost;
    }

    public EventTracking setScLatestReferrerHost(String scLatestReferrerHost) {
        this.scLatestReferrerHost = scLatestReferrerHost;
        return this;
    }

    public String getScLatestLandingPage() {
        return scLatestLandingPage;
    }

    public EventTracking setScLatestLandingPage(String scLatestLandingPage) {
        this.scLatestLandingPage = scLatestLandingPage;
        return this;
    }

    public String getScLatestScene() {
        return scLatestScene;
    }

    public EventTracking setScLatestScene(String scLatestScene) {
        this.scLatestScene = scLatestScene;
        return this;
    }

    public String getScLatestShareMethod() {
        return scLatestShareMethod;
    }

    public EventTracking setScLatestShareMethod(String scLatestShareMethod) {
        this.scLatestShareMethod = scLatestShareMethod;
        return this;
    }

    public String getScIosInstallSource() {
        return scIosInstallSource;
    }

    public EventTracking setScIosInstallSource(String scIosInstallSource) {
        this.scIosInstallSource = scIosInstallSource;
        return this;
    }

    public String getScChannelDeviceInfo() {
        return scChannelDeviceInfo;
    }

    public EventTracking setScChannelDeviceInfo(String scChannelDeviceInfo) {
        this.scChannelDeviceInfo = scChannelDeviceInfo;
        return this;
    }

    public String getScIosInstallDisableCallback() {
        return scIosInstallDisableCallback;
    }

    public EventTracking setScIosInstallDisableCallback(String scIosInstallDisableCallback) {
        this.scIosInstallDisableCallback = scIosInstallDisableCallback;
        return this;
    }

    public String getScIsChannelCallbackEvent() {
        return scIsChannelCallbackEvent;
    }

    public EventTracking setScIsChannelCallbackEvent(String scIsChannelCallbackEvent) {
        this.scIsChannelCallbackEvent = scIsChannelCallbackEvent;
        return this;
    }

    public String getScChannelExtraInformation() {
        return scChannelExtraInformation;
    }

    public EventTracking setScChannelExtraInformation(String scChannelExtraInformation) {
        this.scChannelExtraInformation = scChannelExtraInformation;
        return this;
    }

    public String getScItemJoin() {
        return scItemJoin;
    }

    public EventTracking setScItemJoin(String scItemJoin) {
        this.scItemJoin = scItemJoin;
        return this;
    }

    public String getScBotName() {
        return scBotName;
    }

    public EventTracking setScBotName(String scBotName) {
        this.scBotName = scBotName;
        return this;
    }

    public String getScIsValid() {
        return scIsValid;
    }

    public EventTracking setScIsValid(String scIsValid) {
        this.scIsValid = scIsValid;
        return this;
    }

    public String getAllJson() {
        return allJson;
    }

    public EventTracking setAllJson(String allJson) {
        this.allJson = allJson;
        return this;
    }

    /**
     * Java Bean 必须实现的方法，信息通过字符串进行拼接
     **/
    public String convertToCsv() {
        StringBuilder sb = new StringBuilder("(");
        Field[] fields = this.getClass().getDeclaredFields();
        boolean firstField = true;
        for (Field field : fields) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(this);
                if(value == null) {
                    value = "";
                }
                // 目前只有两种类型符合要求，其它类型过滤
                if (field.getType().equals(String.class) ||
                        field.getType().equals(Long.class)) {
                    if(firstField) {
                        firstField = false;
                    } else {
                        sb.append(" ,");
                    }
                    if (field.getType().equals(String.class)) {
                        sb.append("'").append(String.valueOf(value)).append("'");
                    } else if (field.getType().equals(Long.class)) {
                        sb.append(value);
                    }
                }
                field.setAccessible(false);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                logger.error("违法的参数异常或者违法的访问异常: ", e);
            }
        }
        sb.append(" )");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventTracking)) {
            return false;
        }
        EventTracking that = (EventTracking) o;
        return  Objects.equals(getTimeZone(), that.getTimeZone()) &&
                Objects.equals(getScTimezoneOffset(), that.getScTimezoneOffset()) &&
                Objects.equals(getProject(), that.getProject()) &&
                Objects.equals(getPlatformLang(), that.getPlatformLang()) &&
                Objects.equals(getPlatformType(), that.getPlatformType()) &&
                Objects.equals(getDistinctId(), that.getDistinctId()) &&
                Objects.equals(getScDeviceId(), that.getScDeviceId()) &&
                Objects.equals(getDeviceId(), that.getDeviceId()) &&
                Objects.equals(getProperties(), that.getProperties()) &&
                Objects.equals(getLoginId(), that.getLoginId()) &&
                Objects.equals(getScIsLoginId(), that.getScIsLoginId()) &&
                Objects.equals(getScTrackSignupOriginalId(), that.getScTrackSignupOriginalId()) &&
                Objects.equals(getScIsFirstDay(), that.getScIsFirstDay()) &&
                Objects.equals(getScIsFirstTime(), that.getScIsFirstTime()) &&
                Objects.equals(getAnonymousId(), that.getAnonymousId()) &&
                Objects.equals(getType(), that.getType()) &&
                Objects.equals(getEvent(), that.getEvent()) &&
                Objects.equals(getScEventDuration(), that.getScEventDuration()) &&
                Objects.equals(getTime(), that.getTime()) &&
                Objects.equals(getScTrackId(), that.getScTrackId()) &&
                Objects.equals(getScFlushTime(), that.getScFlushTime()) &&
                Objects.equals(getScReceiveTime(), that.getScReceiveTime()) &&
                Objects.equals(getScScreenName(), that.getScScreenName()) &&
                Objects.equals(getScTitle(), that.getScTitle()) &&
                Objects.equals(getScViewportHeight(), that.getScViewportHeight()) &&
                Objects.equals(getScViewportPosition(), that.getScViewportPosition()) &&
                Objects.equals(getScViewportWidth(), that.getScViewportWidth()) &&
                Objects.equals(getScScreenHeight(), that.getScScreenHeight()) &&
                Objects.equals(getScScreenWidth(), that.getScScreenWidth()) &&
                Objects.equals(getScScreenOrientation(), that.getScScreenOrientation()) &&
                Objects.equals(getScScene(), that.getScScene()) &&
                Objects.equals(getScShareDepth(), that.getScShareDepth()) &&
                Objects.equals(getScShareDistinctId(), that.getScShareDistinctId()) &&
                Objects.equals(getScShareUrlPath(), that.getScShareUrlPath()) &&
                Objects.equals(getScShareMethod(), that.getScShareMethod()) &&
                Objects.equals(getScSourcePackageName(), that.getScSourcePackageName()) &&
                Objects.equals(getScUrl(), that.getScUrl()) &&
                Objects.equals(getScUrlQuery(), that.getScUrlQuery()) &&
                Objects.equals(getScUrlPath(), that.getScUrlPath()) &&
                Objects.equals(getReferrer(), that.getReferrer()) &&
                Objects.equals(getScReferrer(), that.getScReferrer()) &&
                Objects.equals(getScReferrerHost(), that.getScReferrerHost()) &&
                Objects.equals(getScReferrerTitle(), that.getScReferrerTitle()) &&
                Objects.equals(getRemark(), that.getRemark()) &&
                Objects.equals(getScCarrier(), that.getScCarrier()) &&
                Objects.equals(getScNetworkType(), that.getScNetworkType()) &&
                Objects.equals(getScWifi(), that.getScWifi()) &&
                Objects.equals(getScBrand(), that.getScBrand()) &&
                Objects.equals(getScManufacturer(), that.getScManufacturer()) &&
                Objects.equals(getScModel(), that.getScModel()) &&
                Objects.equals(getScOsVersion(), that.getScOsVersion()) &&
                Objects.equals(getScOs(), that.getScOs()) &&
                Objects.equals(getScAppVersion(), that.getScAppVersion()) &&
                Objects.equals(getScAppId(), that.getScAppId()) &&
                Objects.equals(getScAppName(), that.getScAppName()) &&
                Objects.equals(getScAppState(), that.getScAppState()) &&
                Objects.equals(getAppCrashedReason(), that.getAppCrashedReason()) &&
                Objects.equals(getScUserAgent(), that.getScUserAgent()) &&
                Objects.equals(getScBrowser(), that.getScBrowser()) &&
                Objects.equals(getScBrowserVersion(), that.getScBrowserVersion()) &&
                Objects.equals(getUserAgent(), that.getUserAgent()) &&
                Objects.equals(getUaPlatform(), that.getUaPlatform()) &&
                Objects.equals(getUaBrowser(), that.getUaBrowser()) &&
                Objects.equals(getUaVersion(), that.getUaVersion()) &&
                Objects.equals(getUaLanguage(), that.getUaLanguage()) &&
                Objects.equals(getIp(), that.getIp()) &&
                Objects.equals(getIpIsGood(), that.getIpIsGood()) &&
                Objects.equals(getIpContinentNames(), that.getIpContinentNames()) &&
                Objects.equals(getIpCountryName(), that.getIpCountryName()) &&
                Objects.equals(getIpCityName(), that.getIpCityName()) &&
                Objects.equals(getIpCity(), that.getIpCity()) &&
                Objects.equals(getIpAsn(), that.getIpAsn()) &&
                Objects.equals(getIpTraits(), that.getIpTraits()) &&
                Objects.equals(getRegisteredCountry(), that.getRegisteredCountry()) &&
                Objects.equals(getAutonomousSystemNumber(), that.getAutonomousSystemNumber()) &&
                Objects.equals(getAutonomousSystemOrganization(), that.getAutonomousSystemOrganization()) &&
                Objects.equals(getLatitude(), that.getLatitude()) &&
                Objects.equals(getLongitude(), that.getLongitude()) &&
                Objects.equals(getScLatitude(), that.getScLatitude()) &&
                Objects.equals(getScLongitude(), that.getScLongitude()) &&
                Objects.equals(getScIpIsp(), that.getScIpIsp()) &&
                Objects.equals(getScLibPluginVersion(), that.getScLibPluginVersion()) &&
                Objects.equals(getScUtmSource(), that.getScUtmSource()) &&
                Objects.equals(getScUtmMedium(), that.getScUtmMedium()) &&
                Objects.equals(getScUtmCampaign(), that.getScUtmCampaign()) &&
                Objects.equals(getScUtmContent(), that.getScUtmContent()) &&
                Objects.equals(getScLatestUtmSource(), that.getScLatestUtmSource()) &&
                Objects.equals(getScLatestUtmMedium(), that.getScLatestUtmMedium()) &&
                Objects.equals(getScLatestUtmCampaign(), that.getScLatestUtmCampaign()) &&
                Objects.equals(getScLatestUtmContent(), that.getScLatestUtmContent()) &&
                Objects.equals(getScLatestUtmTerm(), that.getScLatestUtmTerm()) &&
                Objects.equals(getScUtmMatchingType(), that.getScUtmMatchingType()) &&
                Objects.equals(getScUtmTerm(), that.getScUtmTerm()) &&
                Objects.equals(getScMatchedKey(), that.getScMatchedKey()) &&
                Objects.equals(getScMatchingKeyList(), that.getScMatchingKeyList()) &&
                Objects.equals(getScShortUrlKey(), that.getScShortUrlKey()) &&
                Objects.equals(getScShortUrlTarget(), that.getScShortUrlTarget()) &&
                Objects.equals(getScLatestTrafficSourceType(), that.getScLatestTrafficSourceType()) &&
                Objects.equals(getScLatestSearchKeyword(), that.getScLatestSearchKeyword()) &&
                Objects.equals(getScLatestReferrer(), that.getScLatestReferrer()) &&
                Objects.equals(getScLatestReferrerHost(), that.getScLatestReferrerHost()) &&
                Objects.equals(getScLatestLandingPage(), that.getScLatestLandingPage()) &&
                Objects.equals(getScLatestScene(), that.getScLatestScene()) &&
                Objects.equals(getScLatestShareMethod(), that.getScLatestShareMethod()) &&
                Objects.equals(getScIosInstallSource(), that.getScIosInstallSource()) &&
                Objects.equals(getScChannelDeviceInfo(), that.getScChannelDeviceInfo()) &&
                Objects.equals(getScIosInstallDisableCallback(), that.getScIosInstallDisableCallback()) &&
                Objects.equals(getScIsChannelCallbackEvent(), that.getScIsChannelCallbackEvent()) &&
                Objects.equals(getScChannelExtraInformation(), that.getScChannelExtraInformation()) &&
                Objects.equals(getScItemJoin(), that.getScItemJoin()) &&
                Objects.equals(getScBotName(), that.getScBotName()) &&
                Objects.equals(getScIsValid(), that.getScIsValid());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTimeZone(), getScTimezoneOffset(), getProject(), getPlatformLang(), getPlatformType(), getDistinctId(), getScDeviceId(), getDeviceId(), getProperties(), getLoginId(), getScIsLoginId(), getScTrackSignupOriginalId(), getScIsFirstDay(), getScIsFirstTime(), getAnonymousId(), getType(), getEvent(), getScEventDuration(), getTime(), getScTrackId(), getScFlushTime(), getScReceiveTime(), getScScreenName(), getScTitle(), getScViewportHeight(), getScViewportPosition(), getScViewportWidth(), getScScreenHeight(), getScScreenWidth(), getScScreenOrientation(), getScScene(), getScShareDepth(), getScShareDistinctId(), getScShareUrlPath(), getScShareMethod(), getScSourcePackageName(), getScUrl(), getScUrlQuery(), getScUrlPath(), getReferrer(), getScReferrer(), getScReferrerHost(), getScReferrerTitle(), getRemark(), getScCarrier(), getScNetworkType(), getScWifi(), getScBrand(), getScManufacturer(), getScModel(), getScOsVersion(), getScOs(), getScAppVersion(), getScAppId(), getScAppName(), getScAppState(), getAppCrashedReason(), getScUserAgent(), getScBrowser(), getScBrowserVersion(), getUserAgent(), getUaPlatform(), getUaBrowser(), getUaVersion(), getUaLanguage(), getIp(), getIpIsGood(), getIpContinentNames(), getIpCountryName(), getIpCityName(), getIpCity(), getIpAsn(), getIpTraits(), getRegisteredCountry(), getAutonomousSystemNumber(), getAutonomousSystemOrganization(), getLatitude(), getLongitude(), getScLatitude(), getScLongitude(), getScIpIsp(), getScLibPluginVersion(), getScUtmSource(), getScUtmMedium(), getScUtmCampaign(), getScUtmContent(), getScLatestUtmSource(), getScLatestUtmMedium(), getScLatestUtmCampaign(), getScLatestUtmContent(), getScLatestUtmTerm(), getScUtmMatchingType(), getScUtmTerm(), getScMatchedKey(), getScMatchingKeyList(), getScShortUrlKey(), getScShortUrlTarget(), getScLatestTrafficSourceType(), getScLatestSearchKeyword(), getScLatestReferrer(), getScLatestReferrerHost(), getScLatestLandingPage(), getScLatestScene(), getScLatestShareMethod(), getScIosInstallSource(), getScChannelDeviceInfo(), getScIosInstallDisableCallback(), getScIsChannelCallbackEvent(), getScChannelExtraInformation(), getScItemJoin(), getScBotName(), getScIsValid());
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("EventTracking{");
        sb.append(", timeZone='").append(timeZone).append('\'');
        sb.append(", scTimezoneOffset='").append(scTimezoneOffset).append('\'');
        sb.append(", project='").append(project).append('\'');
        sb.append(", platformLang='").append(platformLang).append('\'');
        sb.append(", platformType='").append(platformType).append('\'');
        sb.append(", distinctId='").append(distinctId).append('\'');
        sb.append(", scDeviceId='").append(scDeviceId).append('\'');
        sb.append(", deviceId='").append(deviceId).append('\'');
        sb.append(", properties='").append(properties).append('\'');
        sb.append(", loginId='").append(loginId).append('\'');
        sb.append(", scIsLoginId='").append(scIsLoginId).append('\'');
        sb.append(", scTrackSignupOriginalId='").append(scTrackSignupOriginalId).append('\'');
        sb.append(", scIsFirstDay='").append(scIsFirstDay).append('\'');
        sb.append(", scIsFirstTime='").append(scIsFirstTime).append('\'');
        sb.append(", anonymousId='").append(anonymousId).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append(", event='").append(event).append('\'');
        sb.append(", scEventDuration='").append(scEventDuration).append('\'');
        sb.append(", time=").append(time);
        sb.append(", scTrackId=").append(scTrackId);
        sb.append(", scFlushTime=").append(scFlushTime);
        sb.append(", scReceiveTime=").append(scReceiveTime);
        sb.append(", scScreenName='").append(scScreenName).append('\'');
        sb.append(", scTitle='").append(scTitle).append('\'');
        sb.append(", scViewportHeight='").append(scViewportHeight).append('\'');
        sb.append(", scViewportPosition='").append(scViewportPosition).append('\'');
        sb.append(", scViewportWidth='").append(scViewportWidth).append('\'');
        sb.append(", scScreenHeight='").append(scScreenHeight).append('\'');
        sb.append(", scScreenWidth='").append(scScreenWidth).append('\'');
        sb.append(", scScreenOrientation='").append(scScreenOrientation).append('\'');
        sb.append(", scScene='").append(scScene).append('\'');
        sb.append(", scShareDepth='").append(scShareDepth).append('\'');
        sb.append(", scShareDistinctId='").append(scShareDistinctId).append('\'');
        sb.append(", scShareUrlPath='").append(scShareUrlPath).append('\'');
        sb.append(", scShareMethod='").append(scShareMethod).append('\'');
        sb.append(", scSourcePackageName='").append(scSourcePackageName).append('\'');
        sb.append(", scUrl='").append(scUrl).append('\'');
        sb.append(", scUrlQuery='").append(scUrlQuery).append('\'');
        sb.append(", scUrlPath='").append(scUrlPath).append('\'');
        sb.append(", referrer='").append(referrer).append('\'');
        sb.append(", scReferrer='").append(scReferrer).append('\'');
        sb.append(", scReferrerHost='").append(scReferrerHost).append('\'');
        sb.append(", scReferrerTitle='").append(scReferrerTitle).append('\'');
        sb.append(", remark='").append(remark).append('\'');
        sb.append(", scCarrier='").append(scCarrier).append('\'');
        sb.append(", scNetworkType='").append(scNetworkType).append('\'');
        sb.append(", scWifi='").append(scWifi).append('\'');
        sb.append(", scBrand='").append(scBrand).append('\'');
        sb.append(", scManufacturer='").append(scManufacturer).append('\'');
        sb.append(", scModel='").append(scModel).append('\'');
        sb.append(", scOsVersion='").append(scOsVersion).append('\'');
        sb.append(", scOs='").append(scOs).append('\'');
        sb.append(", scAppVersion='").append(scAppVersion).append('\'');
        sb.append(", scAppId='").append(scAppId).append('\'');
        sb.append(", scAppName='").append(scAppName).append('\'');
        sb.append(", scAppState='").append(scAppState).append('\'');
        sb.append(", appCrashedReason='").append(appCrashedReason).append('\'');
        sb.append(", scUserAgent='").append(scUserAgent).append('\'');
        sb.append(", scBrowser='").append(scBrowser).append('\'');
        sb.append(", scBrowserVersion='").append(scBrowserVersion).append('\'');
        sb.append(", userAgent='").append(userAgent).append('\'');
        sb.append(", uaPlatform='").append(uaPlatform).append('\'');
        sb.append(", uaBrowser='").append(uaBrowser).append('\'');
        sb.append(", uaVersion='").append(uaVersion).append('\'');
        sb.append(", uaLanguage='").append(uaLanguage).append('\'');
        sb.append(", ip='").append(ip).append('\'');
        sb.append(", ipIsGood='").append(ipIsGood).append('\'');
        sb.append(", ipContinentNames='").append(ipContinentNames).append('\'');
        sb.append(", ipCountryName='").append(ipCountryName).append('\'');
        sb.append(", ipCityName='").append(ipCityName).append('\'');
        sb.append(", ipCity='").append(ipCity).append('\'');
        sb.append(", ipAsn='").append(ipAsn).append('\'');
        sb.append(", ipTraits='").append(ipTraits).append('\'');
        sb.append(", registeredCountry='").append(registeredCountry).append('\'');
        sb.append(", autonomousSystemNumber='").append(autonomousSystemNumber).append('\'');
        sb.append(", autonomousSystemOrganization='").append(autonomousSystemOrganization).append('\'');
        sb.append(", latitude='").append(latitude).append('\'');
        sb.append(", longitude='").append(longitude).append('\'');
        sb.append(", scLatitude='").append(scLatitude).append('\'');
        sb.append(", scLongitude='").append(scLongitude).append('\'');
        sb.append(", scIpIsp='").append(scIpIsp).append('\'');
        sb.append(", scLibPluginVersion='").append(scLibPluginVersion).append('\'');
        sb.append(", scUtmSource='").append(scUtmSource).append('\'');
        sb.append(", scUtmMedium='").append(scUtmMedium).append('\'');
        sb.append(", scUtmCampaign='").append(scUtmCampaign).append('\'');
        sb.append(", scUtmContent='").append(scUtmContent).append('\'');
        sb.append(", scLatestUtmSource='").append(scLatestUtmSource).append('\'');
        sb.append(", scLatestUtmMedium='").append(scLatestUtmMedium).append('\'');
        sb.append(", scLatestUtmCampaign='").append(scLatestUtmCampaign).append('\'');
        sb.append(", scLatestUtmContent='").append(scLatestUtmContent).append('\'');
        sb.append(", scLatestUtmTerm='").append(scLatestUtmTerm).append('\'');
        sb.append(", scUtmMatchingType='").append(scUtmMatchingType).append('\'');
        sb.append(", scUtmTerm='").append(scUtmTerm).append('\'');
        sb.append(", scMatchedKey='").append(scMatchedKey).append('\'');
        sb.append(", scMatchingKeyList='").append(scMatchingKeyList).append('\'');
        sb.append(", scShortUrlKey='").append(scShortUrlKey).append('\'');
        sb.append(", scShortUrlTarget='").append(scShortUrlTarget).append('\'');
        sb.append(", scLatestTrafficSourceType='").append(scLatestTrafficSourceType).append('\'');
        sb.append(", scLatestSearchKeyword='").append(scLatestSearchKeyword).append('\'');
        sb.append(", scLatestReferrer='").append(scLatestReferrer).append('\'');
        sb.append(", scLatestReferrerHost='").append(scLatestReferrerHost).append('\'');
        sb.append(", scLatestLandingPage='").append(scLatestLandingPage).append('\'');
        sb.append(", scLatestScene='").append(scLatestScene).append('\'');
        sb.append(", scLatestShareMethod='").append(scLatestShareMethod).append('\'');
        sb.append(", scIosInstallSource='").append(scIosInstallSource).append('\'');
        sb.append(", scChannelDeviceInfo='").append(scChannelDeviceInfo).append('\'');
        sb.append(", scIosInstallDisableCallback='").append(scIosInstallDisableCallback).append('\'');
        sb.append(", scIsChannelCallbackEvent='").append(scIsChannelCallbackEvent).append('\'');
        sb.append(", scChannelExtraInformation='").append(scChannelExtraInformation).append('\'');
        sb.append(", scItemJoin='").append(scItemJoin).append('\'');
        sb.append(", scBotName='").append(scBotName).append('\'');
        sb.append(", scIsValid='").append(scIsValid).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
