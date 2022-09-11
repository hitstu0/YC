package com.yecheng.ykv.Mybatis.Mapper;

import com.yecheng.ykv.Data.Kv;

public interface KvMapper {
    void saveKv(Kv kvData);
    Kv getKv(Integer hash);
    void updateKv(Kv kvData);
}
