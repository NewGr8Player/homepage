package com.xavier.homepage.collectors.proxmox.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.xavier.homepage.common.UnsafeRestUtil;
import com.xavier.homepage.collectors.proxmox.entity.VirtualMachineResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Comparator;

@Slf4j
@Service
public class QemuApiHandler {

    @Value("${homepage.proxmox.base-url}")
    private String proxmoxBaseUrl;
    @Value("${homepage.proxmox.username}")
    private String username;
    @Value("${homepage.proxmox.token-id}")
    private String tokenId;
    @Value("${homepage.proxmox.api-token}")
    private String apiToken;
    @Value("${homepage.proxmox.node-name}")
    private String nodeName;

    public static final String PROXMOX_API_URL = "/api2/json/nodes/<node_name>/qemu";

    public VirtualMachineResponse getVmInfo() throws JsonProcessingException {
        // 替换为你的 Proxmox 服务器 IP 和节点名称
        String apiUrl = proxmoxBaseUrl + PROXMOX_API_URL.replace("<node_name>", nodeName);

        // 调用 Proxmox API
        VirtualMachineResponse vmInfo = getProxmoxVMs(apiUrl, username, tokenId, apiToken, VirtualMachineResponse.class);
        vmInfo.getData().sort(Comparator.comparing(VirtualMachineResponse.VirtualMachine::getStatus)
                .thenComparing(VirtualMachineResponse.VirtualMachine::getName)
        );
        return vmInfo;
    }

    /**
     * 获取 Proxmox 节点下所有虚拟机信息 (API Token 认证)
     *
     * @param apiUrl   API 地址
     * @param apiToken API Token
     * @return VM信息
     */
    private static <T> T getProxmoxVMs(String apiUrl, String username, String tokenId, String apiToken, Class<T> t) {
        return getProxmoxVMsCommon(apiUrl, String.format("PVEAPIToken=%s!%s=%s", username, tokenId, apiToken), t);
    }

    /**
     * 获取 Proxmox 节点下所有虚拟机信息(Basic Authentication 认证)
     *
     * @param apiUrl   API 地址
     * @param username 用户名
     * @param password 密码
     * @return VM信息
     */
    private static <T> T getProxmoxVMs(String apiUrl, String username, String password, Class<T> t) {
        String authHeader = "Basic " + Base64.getEncoder().encodeToString((username + ":" + password).getBytes());

        return getProxmoxVMsCommon(apiUrl, authHeader, t);
    }

    /**
     * 获取 Proxmox 节点下所有虚拟机信息(通用方法)
     *
     * @param apiUrl     API 地址
     * @param authHeader 认证头
     * @return VM信息
     */
    private static <T> T getProxmoxVMsCommon(String apiUrl, String authHeader, Class<T> t) {
        RestTemplate restTemplate = UnsafeRestUtil.getRestTemplate();

        /* 创建 HTTP 头并设置认证信息 */
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authHeader);

        /* 创建请求实体 */
        HttpEntity<String> entity = new HttpEntity<>(headers);

        /* 发送 GET 请求并接收响应 */
        ResponseEntity<T> response = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, t);

        /* 检查响应状态码 */
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch VM data from Proxmox API: " + response.getStatusCode());
        }
    }
}
