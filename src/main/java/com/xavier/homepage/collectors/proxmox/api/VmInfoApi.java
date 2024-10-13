package com.xavier.homepage.collectors.proxmox.api;

import com.xavier.homepage.collectors.proxmox.entity.VirtualMachineResponse;
import com.xavier.homepage.collectors.proxmox.handler.QemuApiHandler;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vm")
public class VmInfoApi {

    @Resource
    private QemuApiHandler qemuApiHandler;

    @GetMapping("/info")
    public ResponseEntity<VirtualMachineResponse> getVmInfo() {
        try {
            return ResponseEntity.ok(qemuApiHandler.getVmInfo()) ;
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(new VirtualMachineResponse());
        }
    }
}
