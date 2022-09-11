package com.yecheng.Service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitCodeService {
    private Logger logger = LoggerFactory.getLogger(GitCodeService.class);
    
    private String gitUrl;
    private String projectName;

    public GitCodeService(String gitUrl, String projectName) {
        this.gitUrl = gitUrl;
    }

    public boolean generateGitToMirror() {
        String[] codeAddr = projectName.split(".");
        if (codeAddr == null || codeAddr.length != 2) {
            logger.error("projectName must be depository.file !, but not be:{}", projectName);
            return false;
        }

        String depositoryName = codeAddr[0];
        String fileName = codeAddr[1];
        logger.info("begin generate code into mirror, depositoryName is:{}, fileName is:{}", depositoryName, fileName);
        
        String DockerFile = " \" FROM openjdk:8-jdk-alpine \n ARG JAR_FILE=target/*.jar\n COPY ${JAR_FILE} app.jar\n" +
        "ENTRYPOINT [ \"java\" , \"-jar\" , \"/app.jar\"] \" ";
        String[] commands = new String[] {
            "cd /Users/bytedance" ,
            "mkdir " + "gitcode_" + fileName,
            "cd gitcode_" + fileName,
            "git clone " + gitUrl,
            "cd " + "./"+ depositoryName + "/" + fileName,
            "\"./mvnw\" " + "package -f " + "\"pom.xml\"",
            "echo " + DockerFile + ">>" +"Dockerfile",
            "docker build -t " + depositoryName + "/" + fileName + " .",
            "cd ../../..",
            "rm -rf " + "gitcode_" + fileName
        };
        
        boolean success = false;
        for (String command : commands) {
            logger.info(command);
            success = CommandService.doCommand(command);
            if (!success) {
                logger.error("err happen when exec command:{}, exit", command);
                break;
            }
        }

        return success;
    }
}
