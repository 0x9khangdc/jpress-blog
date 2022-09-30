package io.jpress.commons;

import nl.basjes.parse.useragent.UserAgentAnalyzer;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class UserAgentUtil {

    private static UserAgentAnalyzer uaa = UserAgentAnalyzer
            .newBuilder()
            .build();


    public static Map<String, String> getUserAgentMap(HttpServletRequest request) {
        Map<String, String> requestHeaders = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames != null && headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            requestHeaders.put(name, request.getHeader(name));
        }
        return uaa.parse(requestHeaders).toMap();
    }


    /**
     * Browser name
     *
     * @return
     */
    public static String getBrowserName(Map<String, String> userAgent) {
        return userAgent != null ? userAgent.get("AgentName") : null;
    }


    /**
     * Get the browser version
     *
     * @return
     */
    public static String getBrowserVersion(Map<String, String> userAgent) {
        return userAgent != null ? userAgent.get("AgentVersion") : null;
    }

    /**
     * Get the operating system name
     *
     * @return
     */
    public static String getOsName(Map<String, String> userAgent) {
        return userAgent != null ? userAgent.get("OperatingSystemName") : null;
    }


    /**
     * Get the name of the device
     *
     * @return
     */
    public static String getDeviceName(Map<String, String> userAgent) {
        return userAgent != null ? userAgent.get("DeviceName") : null;
    }

    /**
     * Get equipment manufacturer
     *
     * @return
     */
    public static String getDeviceBrand(Map<String, String> userAgent) {
        return userAgent != null ? userAgent.get("DeviceBrand") : null;
    }


    /**
     * Get the network type
     *
     * @return
     */
    public static String getNetworkType(Map<String, String> userAgent) {
        return userAgent != null ? userAgent.get("NetworkType") : null;
    }
}
