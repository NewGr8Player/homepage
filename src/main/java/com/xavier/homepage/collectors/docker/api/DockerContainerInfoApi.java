package com.xavier.homepage.collectors.docker.api;

import com.xavier.homepage.collectors.docker.entity.ContainerInfo;
import com.xavier.homepage.collectors.docker.handler.RemoteDockerInfo;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/docker")
public class DockerContainerInfoApi {

    @Resource
    private RemoteDockerInfo remoteDockerInfo;

    @GetMapping("/containers")
    public ResponseEntity<List<ContainerInfo>> getContainerInfo() {
       try{
          return ResponseEntity.ok(remoteDockerInfo.getContainers());
       }catch (Exception e){
           return ResponseEntity.status(500).body(new ArrayList<>());
       }
    }
}
