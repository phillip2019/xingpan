package org.jeecg.modules.mkt.util;

import lombok.extern.slf4j.Slf4j;
import org.jeecg.modules.mkt.entity.MktChannelLink;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Slf4j
public class UrlUtils {

    public static String genAdvUrl(String sourceUrl, MktChannelLink record, boolean isPc) {
        if (sourceUrl == null || sourceUrl.isEmpty()) {
            return "";
        }

        // 校验源地址是否是https或http开头，若不是，则添加上https或http开头
        if (!sourceUrl.startsWith("https://") && !sourceUrl.startsWith("http://")) {
            sourceUrl = "https://" + sourceUrl;
        }

        try {
            URL url = new URL(sourceUrl);
            String query = url.getQuery();
            StringBuilder queryParams = new StringBuilder();

            if (query != null && !query.isEmpty()) {
                queryParams.append(query);
                if (!query.endsWith("&")) {
                    queryParams.append("&");
                }
            }

            queryParams.append("utm_campaign=").append(encode(record.getUtmCampaign()));
            queryParams.append("&utm_source=").append(encode(record.getUtmSource()));
            queryParams.append("&utm_medium=").append(encode(record.getUtmMedium()));
            queryParams.append("&utm_term=").append(encode(record.getUtmTerm()));
            queryParams.append("&utm_content=").append(encode(record.getUtmContent()));

            URL newUrl = new URL(url.getProtocol(), url.getHost(), url.getPort(), url.getPath() + "?" + queryParams);
            return newUrl.toString();
        } catch (MalformedURLException e) {
            log.error("Invalid URL: " + sourceUrl, e);
            return "";
        }
    }

    private static String encode(String value) {
        if (value == null) {
            return "";
        }
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException e) {
            log.error("Encoding error", e);
            return value;
        }
    }

    public static void main(String[] args) {
        String sourceUrl = "https://www.baidu.com?name=123";
        MktChannelLink record = new MktChannelLink();
        record.setUtmCampaign("campaign");
        record.setUtmSource("source");
        record.setUtmMedium("medium");
        record.setUtmTerm("term");
        record.setUtmContent("content");
        record.setPcTargetUrl(sourceUrl);
        System.out.println(genAdvUrl(sourceUrl, record, true));
    }
}

