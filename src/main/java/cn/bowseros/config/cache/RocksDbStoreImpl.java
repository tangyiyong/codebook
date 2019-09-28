package cn.bowseros.config.cache;

import java.io.UnsupportedEncodingException;

import javax.annotation.Resource;

import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 *
 * Created by bowser Aug 11, 2019/12:17:34 PM
 * 
 */
@Component
@ConditionalOnProperty("db.rocksDB")
public class RocksDbStoreImpl implements DbStore {
	
   @Resource
   private RocksDB rocksDB;

   @Override
   public void put(String key, String value) {
       try {
           rocksDB.put(key.getBytes("utf-8"), value.getBytes("utf-8"));
       } catch (RocksDBException | UnsupportedEncodingException e) {
           e.printStackTrace();
       }
   }


   @Override
   public String get(String key) {
       try {
           byte[] bytes = rocksDB.get(key.getBytes("utf-8"));
           if (bytes != null) {
               return new String(bytes, "utf-8");
           }
           return null;
       } catch (Exception e) {
           e.printStackTrace();
           return null;
       }
   }

   @Override
   public void remove(String key) {
       try {
           rocksDB.delete(key.getBytes("utf-8"));
       } catch (RocksDBException | UnsupportedEncodingException e) {
           e.printStackTrace();
       }
   }

}
