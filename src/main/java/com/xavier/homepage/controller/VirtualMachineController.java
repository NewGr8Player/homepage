package com.xavier.homepage.controller;

import com.xavier.homepage.collectors.docker.entity.ContainerInfo;
import com.xavier.homepage.collectors.docker.handler.RemoteDockerInfo;
import com.xavier.homepage.collectors.proxmox.entity.VirtualMachineResponse;
import com.xavier.homepage.collectors.proxmox.handler.QemuApiHandler;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VirtualMachineController {

    @Resource
    private QemuApiHandler qemuApiHandler;

    @Resource
    private RemoteDockerInfo remoteDockerInfo;

    @GetMapping("/virtual-machines")
    public ModelAndView getVirtualMachines(Model model) {
        List<VirtualMachineResponse.VirtualMachine> virtualMachines = new ArrayList<>();
        try {
            virtualMachines = qemuApiHandler.getVmInfo().getData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<ContainerInfo> containers = new ArrayList<>();
        try {
            containers= remoteDockerInfo.getContainers();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Map<String,Object> modelMap = new HashMap<>();
        modelMap.put("virtualMachines", virtualMachines);
        modelMap.put("dockerContainers", containers);

        return new ModelAndView("virtualMachines", modelMap);
    }
}

