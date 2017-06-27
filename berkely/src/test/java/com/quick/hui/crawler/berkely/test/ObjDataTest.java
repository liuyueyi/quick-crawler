package com.quick.hui.crawler.berkely.test;

import com.quick.hui.crawler.berkely.test.entity.ToSaveDO;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.*;
import org.junit.Test;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by yihui on 2017/6/21.
 */
public class ObjDataTest {

    private ToSaveDO build() {
        ToSaveDO toSaveDO = new ToSaveDO();

        List<String> list = Arrays.asList("hello", "world", "123");
        Map<String, Object> map = new HashMap<>();
        map.put("key", 123);
        map.put("content", "hello world");


        toSaveDO.setId(100);
        toSaveDO.setUnique(false);
        toSaveDO.setNames(list);
        toSaveDO.setMap(map);
        toSaveDO.setTitle("title");
        return toSaveDO;
    }


    @Test
    public void test() throws UnsupportedEncodingException {
        EnvironmentConfig config = new EnvironmentConfig();
        config.setAllowCreate(true);

        Environment env = new Environment(new File("log"), config);
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setAllowCreate(true);
        Database db = env.openDatabase(null, "myDB", dbConfig);


        String key = "akey";
        Long data = 1234556633L;
        DatabaseEntry keyEntry = new DatabaseEntry(key.getBytes("UTF-8"));
        DatabaseEntry dataEntry = new DatabaseEntry();


        try {
            // 先写入数据
            EntryBinding myBinding = TupleBinding.getPrimitiveBinding(Long.class);
            myBinding.objectToEntry(data, dataEntry);


            // 读取数据
            EntryBinding binding = TupleBinding.getPrimitiveBinding(Long.class);
            db.get(null, keyEntry, dataEntry, LockMode.DEFAULT);
            Long l = (Long) binding.entryToObject(dataEntry);
            System.out.println(l);
        } catch (Exception e) {
            e.printStackTrace();
        }


        Database classDB = null;
        try {
            classDB = env.openDatabase(null, "classDB", dbConfig);
            StoredClassCatalog classCatalog = new StoredClassCatalog(classDB);

            ToSaveDO toSaveDO = build();
            EntryBinding dataBinding = new SerialBinding(classCatalog, ToSaveDO.class);


            // 写入对象
            DatabaseEntry sKey = new DatabaseEntry(toSaveDO.getTitle().getBytes("UTF-8"));
            DatabaseEntry sVal = new DatabaseEntry();
            dataBinding.objectToEntry(toSaveDO, sVal);
            db.put(null, sKey, sVal);


            db.get(null, sKey, sVal, LockMode.DEFAULT);
            ToSaveDO p = (ToSaveDO) dataBinding.entryToObject(sVal);
            System.out.println(p);
        } catch (Exception e) {
            e.printStackTrace();
        }


        classDB.close();
        db.close();
        env.close();
    }
}

