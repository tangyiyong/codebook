package cn.bowseros.config.cache;

/**
 *
 * Created by bowser Aug 11, 2019/12:15:14 PM
 * 
 */
public interface DbStore {

	/**
	 * 数据库key value
	 *
	 * @param key   key
	 * @param value value
	 */
	void put(String key, String value);

	/**
	 * get By Key
	 *
	 * @param key key
	 * @return value
	 */
	String get(String key);

	/**
	 * remove by key
	 *
	 * @param key key
	 */
	void remove(String key);
}
