package cn.bowseros.config.cache;

import javax.annotation.Resource;

import org.iq80.leveldb.DB;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 *
 * Created by bowser Aug 11, 2019/12:15:42 PM
 * 
 */
@Component
@ConditionalOnProperty("db.levelDB")
public class LevelDbStoreImpl implements DbStore {
	@Resource
	private DB db;

	@Override
	public void put(String key, String value) {
		db.put(bytes(key), bytes(value));
	}

	@Override
	public String get(String key) {
		return asString(db.get(bytes(key)));
	}

	@Override
	public void remove(String key) {
		db.delete(bytes(key));
	}
	
	private String asString(byte[] bs) {
		return new String(bs);
	}

	private byte[] bytes(String key) {
		return key.getBytes();
	}
}