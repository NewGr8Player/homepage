package com.xavier.homepage.common;

import org.springframework.web.client.RestTemplate;

import javax.net.ssl.*;
import java.security.cert.X509Certificate;

public class UnsafeRestUtil {

    /**
     * 创建一个忽略 SSL 证书验证的 RestTemplate
     *
     * @return RestTemplate
     * @throws Exception 异常
     */
    public static RestTemplate getRestTemplate() {
        // 创建一个不验证任何 SSL 证书的 TrustManager
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }
                }
        };

        // 安装全局信任的 SSL context 以禁用证书验证
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // 创建不验证主机名的 HostnameVerifier
            HostnameVerifier allHostsValid = (hostname, session) -> true;

            // 设置 SSL factory 到 HttpClient
            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回忽略 SSL 认证的 RestTemplate
        return new RestTemplate();
    }
}
