package com.yecheng.container_num.container_num.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.yecheng.container_num.container_num.Data.CodeMsg;
import com.yecheng.container_num.container_num.Util.Commands;
import com.yecheng.container_num.container_num.Util.ExcInst;

@Service
public class AdjustContainerNumsService {
    Logger logger = LoggerFactory.getLogger(AdjustContainerNumsService.class);
    
    public CodeMsg judge(String service ,int nums) {
        //查询当前有多少个实例
        CodeMsg msg = ExcInst.runShell(Commands.GetServiceNums(service));
        int oldNums = Integer.parseInt(msg.getMsg());

        if (oldNums == nums) {
            return CodeMsg.DoNotNeedAdjust;

        } else if (oldNums < nums) { 
            //如果扩容则查询可用端口，启动新容器
            for(int i = 0; i < nums - oldNums; ++ i) {
                int port = getAvailablePort();
                if (port == -1) {
                    return CodeMsg.CanNotFindAvaliablePort;
                }

                CodeMsg runContainer = ExcInst.runShell(Commands.RunContainer(service, port));
                
            }

        } else {
            //如果缩容则查询当前容器并关闭

        }
        return CodeMsg.Success;
        //删除旧的配置文件，生成新的nginx配置文件并重新加载nginx，
    }

    private int getAvailablePort() {
        //默认查询5次，超过则报错
        for(int i = 0; i < 5; ++ i) {
            int port = 1024 + (int)(Math.random() * (65535 - 1024 + 1));  
            CodeMsg msg = ExcInst.runShell( Commands.JudgePortAvaliable(port));

            int result = Integer.parseInt(msg.getMsg());
            if (result == 0) {
                logger.info("get avaliable port:{}", result);
                return port;
            }
        }
        
        logger.info("can not find avaliable port");
        return -1;
    }
    
    
}
