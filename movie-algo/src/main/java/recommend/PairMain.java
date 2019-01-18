package recommend;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.util.Arrays;


public class PairMain {
    public static void main(String[] args) {

        SparkConf sparkConf = new SparkConf();
        sparkConf.setAppName("movie-recommend").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);
        JavaPairRDD<String, String> tt = sc.parallelizePairs(Arrays.asList(new Tuple2<>("movie4", "第七天堂"),new Tuple2<>("movie5", "第七天堂")));


        JavaPairRDD<String, String> stringStringJavaPairRDD = tt.sortByKey(false);
        stringStringJavaPairRDD.foreach(S -> System.out.println(S));

    }
}
