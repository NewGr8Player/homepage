package com.xavier.homepage.collectors.proxmox.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class VirtualMachineResponse {
    private List<VirtualMachine> data;

    @Getter
    @Setter
    public static class VirtualMachine {
        private long uptime;
        private String name;
        private String status;
        private long diskread;
        private long maxmem;
        private int cpus;
        private long netin;
        private long vmid;
        private long netout;
        private long disk;
        private double cpu;
        private long maxdisk;
        private long mem;
        private String tags;
        private long diskwrite;
    }
}

