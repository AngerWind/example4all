package test;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.table.api.bridge.java.internal.StreamTableEnvironmentImpl;
import org.apache.flink.table.catalog.*;
import org.apache.flink.table.catalog.exceptions.*;
import org.apache.flink.table.catalog.stats.CatalogColumnStatistics;
import org.apache.flink.table.catalog.stats.CatalogTableStatistics;
import org.apache.flink.table.expressions.Expression;
import org.junit.jupiter.api.Test;

import java.util.*;

public class Test1 {


    public static void main(String[] args) {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        StreamTableEnvironmentImpl tableEnvironment = (StreamTableEnvironmentImpl)StreamTableEnvironment.create(env, EnvironmentSettings.newInstance().build());

        // 创建catalog, 这样会使用GenericInMemoryCatalog来创建catalog, 默认数据都保存在内存中, 一旦tableEnvironment关闭, 数据就会丢失
        // 你可以实现其他的Catalog, 这样你创建出来的数据库, table, udf, view等都会保存在你实现的Catalog中, 进行持久化
        tableEnvironment.executeSql("CREATE CATALOG my_catalog WITH (" +
            "'type' = 'generic_in_memory'" +
            ")");
        // 这样也可以创建一个catalog
        tableEnvironment.getCatalogManager().registerCatalog("catalog1", new GenericInMemoryCatalog("catalog1"));

        String createDatabaseSql = "CREATE DATABASE my_catalog.my_db";
        tableEnvironment.executeSql(createDatabaseSql);

        tableEnvironment.useCatalog("my_catalog");
        tableEnvironment.useDatabase("my_db");

        CatalogManager catalogManager = tableEnvironment.getCatalogManager();
        Set<String> strings = catalogManager.listCatalogs();
        HashSet<String> catalogNames = new HashSet<>(strings);
        for (String catalogName : catalogNames) {
            Optional<Catalog> catalogOptional = catalogManager.getCatalog(catalogName);
            if (catalogOptional.isPresent()) {
                Catalog catalog = catalogOptional.get();
                catalogManager.unregisterCatalog(catalogName, true);
                catalogManager.registerCatalog(catalogName, catalog);
                System.out.println("remove " + catalogName);
            }
        }

    }

    @Test
    public void test() {
        List<String> internalList = new ArrayList<>();
        internalList.add("a");
        internalList.add("b");

        Collection<String> view = Collections.unmodifiableCollection(internalList);

        for (String item : view) {
            // internalList.remove(item);  // 虽然你操作的是 internalList，但 view 也感知到了
            System.out.println("remove " + item);
        }

    }
}
