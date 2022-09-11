package com.yecheng;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yecheng.Service.GitCodeService;
import com.yecheng.Service.GitUrlService;

/**
 * Hello world!
 *
 */
public class App {
    private static Logger logger = LoggerFactory.getLogger(App.class);

    public static void main( String[] args ){
        if(args == null || args.length == 0) {
            logger.error("must input a project name");
        }
        
        //获取gitURl
        String projectName = "YC.ykv";

        logger.info("begin to generate project:{} into mirror", projectName);
        GitUrlService gitUrlService = new GitUrlService(projectName);
        String gitUrl = gitUrlService.getGitURL();
        if (gitUrl == null || gitUrl.length() == 0) {
            logger.error("get git url err");
            return;
        }

        logger.info("get git url is:{}", gitUrl);

        //将代码clone到仓库
        GitCodeService gitCodeService = new GitCodeService(gitUrl, projectName);
        boolean success = gitCodeService.generateGitToMirror();
        if (!success) {
            logger.error("err when generate git to mirror");
            return;
        }
        logger.info("generater git to mirror success");
    }
}
