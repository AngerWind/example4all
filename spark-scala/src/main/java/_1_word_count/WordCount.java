package _1_word_count;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function2;
import scala.Tuple2;

import java.util.Arrays;

/**
 * 该测试用例可以远程提交spark程序
 */
public class WordCount {

    /**
     * 没有隐式转换是真的难用, spark的java api
     * 这套api就是根本就不是原生为java设计的, 只是在scala api上面对java适配
     */
    public static void main(String[] args) {
        SparkConf conf = new SparkConf()
            .setAppName("java_wordcount")
            .setMaster("spark://hadoop102:7077")
            .setJars(new String[]{"C:\\Users\\Administrator\\Desktop\\example4all\\spark-scala\\target\\spark-scala-1.0-SNAPSHOT.jar"});
        // 设置hdfs用户
        System.setProperty("HADOOP_USER_NAME", "tiger");

        JavaSparkContext context = new JavaSparkContext(conf);

        JavaRDD<String> textFile = context.textFile("hdfs://hadoop102:8020/wordcount/data.txt");

        JavaRDD<String> flatMap = textFile.flatMap(str -> Arrays.asList(str.split(" ")).iterator());

        // 这里因为没有scala的隐式转换, 所以需要调用toPair将一个单value类型转换为一个key-value类型, 才会有后面的reduceByKey方法
        JavaPairRDD<String, Integer> tupleRDD = flatMap.mapToPair(str -> new Tuple2<>(str, 1));

        // 这里必须是JavaPairRDD才有reduceByKey这个方法, JavaRDD中没有这个方法
        JavaPairRDD<String, Integer> reduce = tupleRDD.reduceByKey(Integer::sum);

        // 因为JavaPairRDD只有sortByKey这个方法, 没有sortBy这个方法(sortBy这个方法在JavaRDD中),
        // 所以必须调换key和value的位置, 才能根据count来排序
        JavaPairRDD<Integer, String> map = reduce.mapToPair(Tuple2::swap);

        JavaPairRDD<Integer, String> sort = map.sortByKey(false);
        sort.saveAsTextFile("hdfs://hadoop102:8020/wordcount/output11111");

        context.stop();
    }
}
