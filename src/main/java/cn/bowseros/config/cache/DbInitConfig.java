package cn.bowseros.config.cache;

import java.io.File;
import java.io.IOException;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.impl.Iq80DBFactory;
import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * Created by bowser Aug 11, 2019/11:40:29 AM
 * 
 */
@Configuration
public class DbInitConfig {

	@Bean
	@ConditionalOnProperty("db.rocksDB")
	public RocksDB rocksDB() {
		RocksDB.loadLibrary();

		Options options = new Options().setCreateIfMissing(true);
		try {
			return RocksDB.open(options, "./rocksDB");
		} catch (RocksDBException e) {
			e.printStackTrace();
			return null;
		}
	}

	@Bean
	@ConditionalOnProperty("db.levelDB")
	public DB levelDB() throws IOException {
		org.iq80.leveldb.Options options = new org.iq80.leveldb.Options();
		options.createIfMissing(true);
		return Iq80DBFactory.factory.open(new File("./levelDB"), options);
	}

}
