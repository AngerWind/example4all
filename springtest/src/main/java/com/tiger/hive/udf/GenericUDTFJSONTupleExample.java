package com.tiger.hive.udf;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTFJSONTuple;
import org.apache.hadoop.hive.serde.serdeConstants;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.StringObjectInspector;
import org.apache.hadoop.io.Text;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser.Feature;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 应该是用在show functions的时候, 显示函数的作用
@Description(name = "json_tuple",
        value = "_FUNC_(jsonStr, p1, p2, ..., pn) - like get_json_object, but it takes multiple names and return a tuple. " +
                "All the input parameters and output column types are string.")
public class GenericUDTFJSONTupleExample extends GenericUDTF {

    private static final Logger LOG = LoggerFactory.getLogger(GenericUDTFJSONTuple.class.getName());

    private static final JsonFactory JSON_FACTORY = new JsonFactory();
    static {
        // Allows for unescaped ASCII control characters in JSON values
        JSON_FACTORY.enable(Feature.ALLOW_UNQUOTED_CONTROL_CHARS);
        // Enabled to accept quoting of all character backslash qooting mechanism
        JSON_FACTORY.enable(Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER);
    }
    private static final ObjectMapper MAPPER = new ObjectMapper(JSON_FACTORY);
    private static final JavaType MAP_TYPE = TypeFactory.fromClass(Map.class);

    // 用于保存返回的列的个数
    int numCols;
    // 用于保存传入json_tuple函数的所有json path
    String[] paths;
    // 用于保存返回的json value
    private transient Text[] retCols;
    //object pool of non-null Text, avoid creating objects all the time
    // Text对象池, 只在调用initialize的时候初始化一次, 然后针对每一行数据调用process的时候retCols都使用这里面的Text对象保存需要返回的结果
    private transient Text[] cols;
    // 用以表示一个空行, 在解析json出错的时候使用
    private transient Object[] nullCols;
    private transient ObjectInspector[] inputOIs; // input ObjectInspectors
    boolean pathParsed = false;
    boolean seenErrors = false;

    //An LRU cache using a linked hash map
    static class HashCache<K, V> extends LinkedHashMap<K, V> {

        private static final int CACHE_SIZE = 16;
        private static final int INIT_SIZE = 32;
        private static final float LOAD_FACTOR = 0.6f;

        HashCache() {
            super(INIT_SIZE, LOAD_FACTOR);
        }

        private static final long serialVersionUID = 1;

        @Override
        protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
            return size() > CACHE_SIZE;
        }

    }

    private transient Map<String, Object> jsonObjectCache;

    @Override
    public void close() throws HiveException {
    }

    /**
     * 对传入json_tuple的入参进行校验
     */
    @Override
    public StructObjectInspector initialize(ObjectInspector[] args)
            throws UDFArgumentException {

        inputOIs = args;
        // 返回的列数为传入参数个数-1
        numCols = args.length - 1;
        jsonObjectCache = new HashCache<>();

        // 传入的参数个数进行校验
        if (numCols < 1) {
            throw new UDFArgumentException("json_tuple() takes at least two arguments: " +
                    "the json string and a path expression");
        }

        // 对传入的参数的类型进行校验
        for (int i = 0; i < args.length; ++i) {
            // category表示类型的基础分类: PRIMITIVE, LIST, MAP, STRUCT, UNION;
            if (args[i].getCategory() != ObjectInspector.Category.PRIMITIVE ||
                    // type name表示类型的具体名称
                    !args[i].getTypeName().equals(serdeConstants.STRING_TYPE_NAME)) {
                throw new UDFArgumentException("json_tuple()'s arguments have to be string type");
            }
        }

        seenErrors = false;
        pathParsed = false;
        paths = new String[numCols];
        cols = new Text[numCols];
        retCols = new Text[numCols];
        nullCols = new Object[numCols];

        for (int i = 0; i < numCols; ++i) {
            cols[i] = new Text();
            retCols[i] = cols[i];
            nullCols[i] = null;
        }

        // construct output object inspector
        ArrayList<String> fieldNames = new ArrayList<String>(numCols);
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<ObjectInspector>(numCols);
        for (int i = 0; i < numCols; ++i) {
            // column name can be anything since it will be named by UDTF as clause
            fieldNames.add("c" + i);
            // all returned type will be Text
            fieldOIs.add(PrimitiveObjectInspectorFactory.writableStringObjectInspector);
        }
        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldNames, fieldOIs);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void process(Object[] o) throws HiveException {

        if (o[0] == null) {
            // 如果传入的json为null, 返回一个所有列都是null的行
            forward(nullCols);
            return;
        }
        // get the path expression for the 1st row only
        if (!pathParsed) {
            for (int i = 0;i < numCols; ++i) {
                // 将object类型转换为具体的类型最好使用Inspector中提供的方法
                // 获取所有的json path
                paths[i] = ((StringObjectInspector) inputOIs[i+1]).getPrimitiveJavaObject(o[i+1]);
            }
            pathParsed = true;
        }

        // 获取json字符串
        String jsonStr = ((StringObjectInspector) inputOIs[0]).getPrimitiveJavaObject(o[0]);
        // 如果传入的json为null, 返回一个所有列都是null的行
        if (jsonStr == null) {
            forward(nullCols);
            return;
        }
        try {
            // 从缓存中获取
            Object jsonObj = jsonObjectCache.get(jsonStr);
            if (jsonObj == null) {
                try {
                    // 将json字符串转换为map
                    jsonObj = MAPPER.readValue(jsonStr, MAP_TYPE);
                } catch (Exception e) {
                    reportInvalidJson(jsonStr);
                    forward(nullCols);
                    return;
                }
                // 将解析后的map对象放入缓存中
                jsonObjectCache.put(jsonStr, jsonObj);
            }

            if (!(jsonObj instanceof Map)) {
                reportInvalidJson(jsonStr);
                forward(nullCols);
                return;
            }

            for (int i = 0; i < numCols; ++i) {
                // 使用cols中的Text对象而不是新建一个, 因为这个函数对于每一行数据都要调用一次
                if (retCols[i] == null) {
                    retCols[i] = cols[i]; // use the object pool rather than creating a new object
                }
                Object extractObject = ((Map<String, Object>)jsonObj).get(paths[i]);
                if (extractObject instanceof Map || extractObject instanceof List) {
                    // 将map和list转换为string
                    retCols[i].set(MAPPER.writeValueAsString(extractObject));
                } else if (extractObject != null) {
                    retCols[i].set(extractObject.toString());
                } else {
                    retCols[i] = null;
                }
            }
            // 将数据写出去
            forward(retCols);
            return;
        } catch (Throwable e) {
            LOG.error("JSON parsing/evaluation exception" + e);
            forward(nullCols);
        }
    }

    @Override
    public String toString() {
        return "json_tuple";
    }

    private void reportInvalidJson(String jsonStr) {
        if (!seenErrors) {
            LOG.error("The input is not a valid JSON string: " + jsonStr +
                    ". Skipping such error messages in the future.");
            seenErrors = true;
        }
    }
}
