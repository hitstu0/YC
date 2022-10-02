package com.yecheng.nginx_manager.Data;

import lombok.Data;

@Data
public class UpstreamData {
    private String ip;
    private String port;
    private String weight;
}
