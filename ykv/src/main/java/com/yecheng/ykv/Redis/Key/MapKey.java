package com.yecheng.ykv.Redis.Key;

import com.yecheng.yredis.Util.Prefix.abstractPrefix;

public class MapKey extends abstractPrefix{
    

    protected MapKey(String pre, int expireTime) {
        super(pre, expireTime);
    }
    
    public static MapKey map = new MapKey("kvmap", 0);
}
