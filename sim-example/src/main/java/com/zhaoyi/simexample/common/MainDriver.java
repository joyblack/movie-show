package com.zhaoyi.simexample.common;

import org.ansj.splitWord.analysis.BaseAnalysis;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import scala.Tuple2;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class MainDriver {

    private static String sunrunLibrary = "sunrun/library/";

    private static String sunrunDoc = "sunrun/doc/";

    private static String tmpLearnLibrary = "output/";

    public static void learnData(String[] args) throws Exception {
        String inputFile = args[1];
        JavaSparkContext sc = getSparkContext();

        String sparkFile = Helper.handlerFile(inputFile);

        // 1.load file
        JavaPairRDD<String, String> contentRDD = sc.wholeTextFiles(sparkFile);

        // 2.analysis data and filter data: null，sig，number(m)
        JavaRDD<String> filterWords = contentRDD.flatMap(s -> BaseAnalysis.parse(s._2).getTerms().iterator())
                .filter(t -> t.getName() != null
                && !t.getName().trim().isEmpty()
                && !t.getNatureStr().equals("w")
                && !t.getNatureStr().equals("m")).map(t -> t.getName());

        // 3.map(word,1)
        JavaPairRDD<String, Integer> wordsCount = filterWords.mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((ctx, cty) -> ctx + cty);
        // debug 3
        System.out.println("=== debug3: print the words count===");
        System.out.println(wordsCount.collect());

        // 4.show ordered words.
        System.out.println("=== debug4: print the Top 10 words and appear count===");
        List<Tuple2<String, Integer>> top10Words = wordsCount.takeOrdered(10, new MyTupleComparator());
        System.out.println(top10Words);

        // 5.tmp store result
        String newUUID = UUID.randomUUID().toString();
        String nowOutPath = tmpLearnLibrary + newUUID;
        System.out.println("=== debug5: store the result file to tmp library.===");
        wordsCount.map(s -> s._1 + "\t" + s._2).saveAsTextFile(nowOutPath);
        System.out.println("success...");


        // 6.move to library file.
        System.out.println("=== debug6: move file to learn library vector.===");
        Files.move(Paths.get(nowOutPath + "/" + "part-00000"), Paths.get(sunrunLibrary +  newUUID));
        System.out.println("success...");

        // 7.move learn file to library doc.
        System.out.println("=== debug7: move learn file to library doc.===");
        Files.copy(Paths.get(inputFile), Paths.get(sunrunDoc + newUUID));
        System.out.println("success...");

        // 8.print success
        System.out.println("###########################################################");
        System.out.println("##################### Success Learn and Store in Library index:" +  newUUID  + " #################");
        System.out.println("###########################################################");
    }

    public static void checkData(String[] args) throws Exception {
        // common setting.
        String inputFile = args[1];
        JavaSparkContext sc = getSparkContext();
        String sparkFile = Helper.handlerFile(inputFile);
        // debug 1
        JavaPairRDD<String, String> checkRDD = sc.wholeTextFiles(sparkFile);

        // 2.analysis data and filter data: null，sig，number(m)
        JavaRDD<String> filterWords = checkRDD.flatMap(s -> BaseAnalysis.parse(s._2).getTerms().iterator())
                .filter(t -> t.getName() != null
                        && !t.getName().trim().isEmpty()
                        && !t.getNatureStr().equals("w")
                        && !t.getNatureStr().equals("m")).map(t -> t.getName());
        // debug 2
        System.out.println("=== 2. print the filter words ===");
        System.out.println(filterWords.collect());

        // 3.map(word,1)
        JavaPairRDD<String, Integer> wordsCount = filterWords.mapToPair(word -> new Tuple2<>(word, 1))
                .reduceByKey((ctx, cty) -> ctx + cty);

        // 4.show ordered words.
        System.out.println("=== 4. print the Top 10 words and appear count ===");
        List<Tuple2<String, Integer>> top10Words = wordsCount.takeOrdered(10, new MyTupleComparator());
        System.out.println(top10Words);

        // ======= load out SunRun page library =======
        // 1.load
        JavaPairRDD<String, String> libraryRDD = sc.wholeTextFiles(sunrunLibrary);
        // 2.get all page path.
        List<String> libraryFilePaths = libraryRDD.map(l -> l._1).collect();
        System.out.println("=== 1.Now out library file list: ===");
        System.out.println(libraryFilePaths);

        HashMap<String, Double> result = new HashMap<>();
        // 3.handler each library file.
        for(String libraryFilePath:libraryFilePaths){
            String fileName = libraryFilePath;
            JavaRDD<String> nowPageRDD = sc.textFile(fileName);
            JavaPairRDD<String, Integer> libraryWordsRDD = nowPageRDD.mapToPair(line -> {
                String[] tokens = line.split("\t");
                // (word,count)
                return new Tuple2<>(tokens[0], Integer.valueOf(tokens[1]));
            });
            // show the every library info.
            System.out.println("=== The library word vector is ===");
            System.out.println(libraryWordsRDD.collect());

            // join
            JavaPairRDD<String, Tuple2<Integer, Integer>> joinResult = wordsCount.join(libraryWordsRDD);

            // show now check file join the library data info.
            System.out.println("=== The join result: ====");
            System.out.println(joinResult.collect());

            // union
            System.out.println("=== The union result ====");
            JavaPairRDD<String, Integer> unionResult = wordsCount.union(libraryWordsRDD);
            System.out.println(unionResult.collect());

            // intersection count
            long intersectionCount = joinResult.count();
            System.out.println("=== The intersection count is: " + intersectionCount + "====");

            // union count
            long unionCount = unionResult.count() - intersectionCount;
            System.out.println("=== The union count is: " + unionCount + "====");

            // jaccard
            double jaccardSimilarity = intersectionCount / (unionCount * 1.0);

            // collect
            result.put(libraryFilePath,jaccardSimilarity);
        }
        // 8.print jaccard
        DecimalFormat decimalFormat = new DecimalFormat("0.00%");
        System.out.println("##################################### ALL document with this file Jacaard Similarity list ######################################");
        result.entrySet().forEach(entry -> System.out.println("### " + entry.getKey() + " : " + decimalFormat.format(entry.getValue()) + " ###"));
        System.out.println("###########################################################");
    }

    public static JavaSparkContext getSparkContext(){
        SparkConf conf = new SparkConf();
        conf.setMaster("local").setAppName("PageCheck").set("spark.testing.memory", "2147480000");
        return new JavaSparkContext(conf);
    }


    public static void main(String[] args) throws Exception {
        int requireParameterLength = 2;
        if(args.length < requireParameterLength){
            System.out.println("Error: Please enter at least 2 parameter: opt path [option]");
            System.exit(-1);
        }
        switch (args[0]){
            case "learn": learnData(args);break;
            case "check": checkData(args);break;
            default:System.out.println("Error: This is not a illegal opt name:" + args[0]);
        }
    }
}