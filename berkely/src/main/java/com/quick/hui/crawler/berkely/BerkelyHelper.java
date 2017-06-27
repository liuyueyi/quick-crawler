package com.quick.hui.crawler.berkely;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.serial.SerialBinding;
import com.sleepycat.bind.serial.StoredClassCatalog;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.je.*;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Berkely DB 辅助类
 * <p>
 * Created by yihui on 2017/6/21.
 */
public class BerkelyHelper {

    /**
     * 用于管理DB
     */
    private Environment environment;


    /**
     * 类型转换日志DB
     */
    private static final String CLASS_CATALOG = "java_class_catalog";
    private StoredClassCatalog classCatalog;
    private Database catalogDb;


    /**
     * 本地缓存的bind映射表
     */
    private Map<Class, EntryBinding> bindingMap = new ConcurrentHashMap<>();


    private Database db;


    public static BerkelyHelper newInstance(String path, String dbName) {
        return new BerkelyHelper(path, dbName);
    }


    private BerkelyHelper(String path, String dbName) {
        initEnv(path);
        initDb(dbName);
    }

    private void initEnv(String path) {
        File file = new File(path);
        if ((!file.exists() || file.isFile()) && !file.mkdir()) { // 不存在，or为文件时， 创建一个对应的目录
            throw new IllegalStateException("create dir:" + path + " error!");
        }


        EnvironmentConfig environmentConfig = new EnvironmentConfig();
        environmentConfig.setAllowCreate(true);
        environmentConfig.setTransactional(true);

        environment = new Environment(file, environmentConfig);
    }


    private void initDb(String dbName) {
        DatabaseConfig dbConfig = new DatabaseConfig();
        dbConfig.setTransactional(true);
        dbConfig.setAllowCreate(true);

        catalogDb = environment.openDatabase(null, CLASS_CATALOG, dbConfig);
        classCatalog = new StoredClassCatalog(catalogDb);


        db = environment.openDatabase(null, dbName, dbConfig);
    }


    @SuppressWarnings("unchecked")
    private <T> EntryBinding getBinding(Class<T> clz) {
        if (bindingMap.containsKey(clz)) {
            return bindingMap.get(clz);
        }


        EntryBinding binding;
        if (BasicTypeHelper.isBasicType(clz)) {
            binding = TupleBinding.getPrimitiveBinding(clz);
        } else {
            binding = new SerialBinding(classCatalog, clz);
        }

        bindingMap.put(clz, binding);
        return binding;
    }

    private DatabaseEntry getKey(String key) {
        DatabaseEntry kEntrty;
        try {
            kEntrty = new DatabaseEntry(key.getBytes("utf-8"));
        } catch (Exception e) {
            kEntrty = new DatabaseEntry(key.getBytes());
        }
        return kEntrty;
    }


    /**
     * 往 DB 中塞数据
     *
     * @param key   塞入的标识key
     * @param value 塞入的value
     * @return
     * @throws UnsupportedEncodingException
     */
    public OperationStatus put(String key, Object value) {
        DatabaseEntry kEntrty = getKey(key);
        DatabaseEntry vEntry = new DatabaseEntry();


        EntryBinding binding = getBinding(value.getClass());
        binding.objectToEntry(value, vEntry);
        return db.put(null, kEntrty, vEntry);
    }


    /**
     * 从 DB 中获取数据
     *
     * @param key      唯一标识
     * @param valueClz 返回的数据类型
     * @return
     * @throws UnsupportedEncodingException
     */
    public <T> T get(String key, Class<T> valueClz) {
        DatabaseEntry kEntrty = getKey(key);
        DatabaseEntry vEntry = new DatabaseEntry();


        OperationStatus status;
        status = db.get(null, kEntrty, vEntry, LockMode.DEFAULT);

        if (status != OperationStatus.SUCCESS) {
            return null;
        }

        EntryBinding<T> binding = getBinding(valueClz);
        return binding.entryToObject(vEntry);
    }


    /**
     * 删除数据
     *
     * @param key
     */
    public void delete(String key) {
        DatabaseEntry kEntry = getKey(key);
        db.delete(null, kEntry);
    }


    // 遍历db中所有数据的方法，因为不知道value的类型， 所以不太好处理，为了解决这个方法，可以在key里面冗余一个value类型的信息
    public void scan() {

        Cursor cursor = db.openCursor(null, CursorConfig.DEFAULT);
        DatabaseEntry kEntry = new DatabaseEntry();
        DatabaseEntry vEntry = new DatabaseEntry();

        while (cursor.getNext(kEntry, vEntry, LockMode.DEFAULT) == OperationStatus.SUCCESS) {
            // key = xxx
            String key = new String(kEntry.getData());
            // value = xxx
        }
    }


    /**
     * 结束之前，得调用一下这个方法
     */
    public void close() {
        db.close();
        classCatalog.close();
        environment.close();

        System.out.println("释放资源");
    }
}
