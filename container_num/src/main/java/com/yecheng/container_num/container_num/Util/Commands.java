package com.yecheng.container_num.container_num.Util;

public class Commands {
    
    public static String[] GetServiceNums(String service) {
        return new String[] {
            "/bin/sh", "-c", "docker ps --format \"{{.Image}}\" | grep " + service + " | wc -l",
        };
    }

    public static String[] JudgePortAvaliable(int port) {
        return new String[] {
            "/bin/sh", "-c", "netstat -anp | grep " + port + " | wc -l" 
        };
    }

    public static String[] RunContainer(String service, int port) {
        String name = service.replace('/', '_');
        return new String[] { 
            "/bin/sh",
            "-c",
            String.format("docker run -d -p %d:%d --name=%s_%d %s java -jar app.jar " +
            "--server.port=%d --spring.cloud.consul.discovery.instance-id=%s_%d ",
             port, port, name, port, service, port, name, port)
        };
    }
}
