package com.yecheng.yredis.Util.Prefix;

public abstract class abstractPrefix implements prefix{

    private final int  expireTime;
    private final String  pre;

    protected abstractPrefix(String pre){
        this.pre = pre;
        this.expireTime = 0;
    }

    protected abstractPrefix(String pre,int expireTime){
        this.pre = pre;
        this.expireTime = expireTime;
    }

    @Override
    public int getExpiredTime() {
        return expireTime;  
    }    
    
    @Override
    public String getPrefix() {
        return pre;
    }
}
