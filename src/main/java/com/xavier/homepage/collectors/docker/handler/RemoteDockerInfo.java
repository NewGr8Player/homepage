package com.xavier.homepage.collectors.docker.handler;

import com.github.dockerjava.api.DockerClient;
import com.xavier.homepage.collectors.docker.entity.ContainerInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RemoteDockerInfo {

    @Resource
    private DockerClient dockerClient;

    /**
     * 获取docker容器列表
     *
     * @return 容器列表
     */
    public List<ContainerInfo> getContainers() throws Exception {
        return dockerClient.listContainersCmd().withShowAll(true).exec().stream()
                .map(e -> {
                    ContainerInfo containerInfo = new ContainerInfo();
                    containerInfo.setId(e.getId());
                    containerInfo.setName(e.getNames()[0].replace("/", ""));
                    containerInfo.setImage(e.getImage());
                    containerInfo.setStatus(e.getStatus());
                    containerInfo.setPublicPorts(Stream.of(e.getPorts())
                            .filter(it -> Objects.nonNull(it.getPublicPort()))
                            .map(it -> String.format("%s/%s", it.getPublicPort(), it.getType()))
                            .distinct()
                            .collect(Collectors.toList())
                    );
                    return containerInfo;
                })
                .sorted(Comparator.comparing(ContainerInfo::getName))
                .toList();
    }
}


