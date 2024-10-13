package com.xavier.homepage.collectors.docker.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ContainerInfo {
    private String id;
    private String name;
    private String image;
    private String status;
    private List<String> publicPorts;
}
