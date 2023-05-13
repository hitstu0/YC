package com.yecheng.nginx_manager.Data;

import lombok.Data;

@Data
public class RouteDefinitionDBData {
    private int id;
    private String path;
    private String serviceName;
    private int serviceHash;
    private int pathHash;
}
