package com.yecheng.ykv.Service;

import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yecheng.ykv.Data.CodeMsg;
import com.yecheng.ykv.Data.GetValueResponse;
import com.yecheng.ykv.Data.Kv;
import com.yecheng.ykv.Exception.MyException;
import com.yecheng.ykv.Mybatis.Mapper.KvMapper;
import com.yecheng.ykv.Mybatis.SqlSessionFactory.SqlSessionBuilder;
import com.yecheng.ykv.Redis.JedisBuilder;
import com.yecheng.ykv.Redis.Key.MapKey;

import lombok.val;
import redis.clients.jedis.Jedis;

@Service
public class KvService {
    Logger logger = org.slf4j.LoggerFactory.getLogger(KvService.class);

    @Autowired
    private SqlSessionBuilder sqlSessionBuilder;

    @Autowired
    private JedisBuilder jedisBuilder;

    public CodeMsg setKv(Kv kvData, HttpServletResponse response) {
        final int MIN_CACHE_LENGTH = 450;
        kvData.setHash(kvData.getDbkey().hashCode());
        int vhash = (kvData.getValue().length() > MIN_CACHE_LENGTH)? kvData.getValue().hashCode() : 0;

        SqlSession sqlSession = null;
        Jedis jedis = null;
        try {
            sqlSession = sqlSessionBuilder.getSqlSession();
            //查看是否已存在
            KvMapper mapper = sqlSession.getMapper(KvMapper.class);
            Kv dbData = mapper.getKv(kvData.getHash());
            
            if (dbData == null) {
                logger.info("key:{} is new, begin to insert", kvData.getDbkey());
                mapper.saveKv(kvData);
            } else {
                //处理 keyhash相同但 key 不同的情况
                if (!dbData.getDbkey().equals(kvData.getDbkey())) {
                    return CodeMsg.KeyShouldChange;
                }

                logger.info("key:{} has already,begin to update", kvData.getDbkey());
                kvData.setId(dbData.getId());
                mapper.updateKv(kvData);
            }
            
            jedis = jedisBuilder.getJedis();
            //如果value长度大于450则保存进缓存
            if (kvData.getValue().length() > MIN_CACHE_LENGTH) {
                jedis.getSet(MapKey.map.getPrefix() + kvData.getDbkey(), vhash + "");
            } else if(dbData != null && dbData.getValue().length() > MIN_CACHE_LENGTH) {
                jedis.del(MapKey.map.getPrefix() + kvData.getDbkey());
            }
        

            sqlSession.commit();
        } catch (Exception e) {
            logger.error("update kv err:{}", e.getMessage());
            return CodeMsg.InternError;
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }

            if (jedis != null) {
                jedis.close();
            }
        }
        
        logger.info("save kv success");
        return CodeMsg.SuccessWithData(vhash + "");
    }

    public CodeMsg getValue(String key, Integer hash) {
        logger.info("begin get value, key is:{}, value hash is:{} ", key, hash);
        
        if (hash == null || hash == 0) {
            logger.info("hash is null");
            return getKvFromDB(key);
        } else {
            Jedis jedis = null;
            String redisValue = null;
            try {
                jedis = jedisBuilder.getJedis();
                redisValue = jedis.get(MapKey.map.getPrefix() + key);
            } finally {
                if (jedis != null) {
                    jedis.close();
                }
            }

            if (redisValue == null) {
                logger.info("redis not contain key:{}", MapKey.map.getPrefix() + key);
                return getKvFromDB(key);
            }

            if(Integer.valueOf(redisValue).equals(hash)) {
                logger.info("request hash is equal with redis hash");
                return CodeMsg.NOTMODIFY;
            }
             
            return getKvFromDB(key);
        }
    }

    private CodeMsg<GetValueResponse> getKvFromDB(String key) {
        int khash = key.hashCode();

        SqlSession sqlSession = null;
        Kv kv = null;
        try { 
            sqlSession = sqlSessionBuilder.getSqlSession();
            KvMapper mapper = sqlSession.getMapper(KvMapper.class);
            kv = mapper.getKv(khash);
        } catch (Exception e) {
            logger.error("get kv from data err:{}", e.getMessage());
            throw new MyException(CodeMsg.InternError);
        } finally {
            if (sqlSession != null) {
                sqlSession.close();
            }
        }

        if (kv == null) {
            logger.error("kv not exist, key is:{}", key);
            throw new MyException(CodeMsg.KVNotEXist);
        }
        
        GetValueResponse resp = new GetValueResponse();
        resp.setValue(kv.getValue());
        resp.setHash(kv.getValue().hashCode());
        logger.info("get kv success from db, value:{}", kv.getValue());
        
        return CodeMsg.SuccessWithData(resp);
    }
}
