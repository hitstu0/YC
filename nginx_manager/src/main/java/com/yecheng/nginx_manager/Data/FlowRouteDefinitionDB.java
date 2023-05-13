package com.yecheng.nginx_manager.Data;

import lombok.Data;

@Data
public class FlowRouteDefinitionDB {
    private int id;
    private String host;
    private String service;
    private int serviceHash;
}
