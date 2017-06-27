package com.quick.hui.crawler.berkely.test;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.*;
import org.junit.Test;

import java.io.File;

/**
 * Created by yihui on 2017/6/21.
 */
public class SimpleDataTest {
    private Environment env;
    private Database db;
    private String key = "akey";
    private Long data = 1234556633L;

    public void setUp() throws Exception {
        EnvironmentConfig envConfig = new EnvironmentConfig();
        envConfig.setAllowCreate(true);
        env = new Environment(new File("log"), envConfig);
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        db = env.openDatabase(null, "myDB", dbConfig);
        DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("UTF-8"));
        DatabaseEntry dataEntry = new DatabaseEntry();

        EntryBinding myBinding = TupleBinding.getPrimitiveBinding(Long.class);
        myBinding.objectToEntry(data, dataEntry);
        db.put(null, keyEntry, dataEntry);
    }


    public void testGet() throws Exception {
        DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("UTF-8"));
        DatabaseEntry dataEntry = new DatabaseEntry();
        EntryBinding binding = TupleBinding.getPrimitiveBinding(Long.class);
        db.get(null, keyEntry, dataEntry, LockMode.DEFAULT);
        Long l = (Long) binding.entryToObject(dataEntry);
    }


    public void tearDown() throws Exception {
        db.close();
        env.truncateDatabase(null, "myDB", false);
        env.close();
    }


    @Test
    public void main() throws Exception {
        SimpleDataTest simpleDataTest = new SimpleDataTest();
        simpleDataTest.setUp();
        simpleDataTest.testGet();
    }
}
