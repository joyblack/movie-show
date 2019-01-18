package com.zhaoyi.simexample.common;

import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.BaseAnalysis;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.util.List;
import java.util.UUID;

public class Helper{

    public static boolean isWordFile(String filePath){
        String[] split = filePath.split("\\.");
        if(split[split.length -1].equals("doc") || split[split.length -1].equals("docx")){
            return true;
        }else{
            return false;
        }
    }




    public static String getWordText(String filePath) throws Exception {
        String content = "";
        FileInputStream in = new FileInputStream(filePath);
        try {
            XWPFDocument doc = new XWPFDocument(in);
            XWPFWordExtractor extractor = new XWPFWordExtractor(doc);
            content = extractor.getText();
            in.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return  content;
    }

    // Transform word file to simple, general a file that name is old.uuid
    public static String handlerFile(String url) throws Exception{
        String sparkFile = url;
        if(isWordFile(url)){
            System.out.println("=== This is a word file, need nio to handler and transfer...===");
            sparkFile = url + "." + UUID.randomUUID().toString() ;
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(sparkFile), "UTF-8"));
            writer.write(getWordText(url));
            writer.flush();
            writer.close();
        }else{
            System.out.println("=== This is a normal file... ===");
        }
        return sparkFile;
    }


    public static void test () throws Exception {
        String str = "你好，你好， fen  我很有想法，      请问你是怎么.做的" ;
        List<Term> terms = BaseAnalysis.parse(str).getTerms();
        terms.forEach(word -> {
            System.out.println("name = " + word.getName());
            System.out.println("nartureStr = " + word.getNatureStr());
            System.out.println("realName = " + word.getRealName());
            System.out.println("RealNameIfnull = " + word.getRealNameIfnull());
            System.out.println("toString = " + word.toString());
            System.out.println("offe = " + word.getOffe());
            System.out.println("Synonyms = " + word.getSynonyms());
            System.out.println("offe = " + word.score());
            System.out.println("wwww ===" + word.getNatureStr());
        });

        System.out.println(terms);

    }

}
