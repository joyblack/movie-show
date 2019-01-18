package com.zhaoyi.movieshow;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.UUID;

public class CrawerMain {

    public void getAllMovie(){
        try {
            String url = "https://list.iqiyi.com/www/1/-------------24-[page]-1-iqiyi--.html";
            Integer page = 1;
            Integer total = 30;
            while(page <= total){
                try{

                    Document doc = Jsoup.connect(url.replace("[page]", page.toString())).get();
                    // 电影列表
                    Elements listMovieLis = doc.getElementsByClass("wrapper-piclist").first().getElementsByTag("li");
                    for (Element movieLi : listMovieLis) {
                        // 获取链接对象
                        Element movieLink = movieLi.selectFirst("a");
                        // 电影连接
                       String detailUrl = movieLink.attr("href");

                        // 进入电影播放页面，并设置电影的其他信息
                        try{
                            this.goMovieDetailPage(detailUrl);
                        }catch (Exception e){
                            System.out.println("XXXXXXXX: failed, the reson is :" + e.getMessage());
                        }

                    }
                }catch (Exception e){
                    System.out.println("xxxxx");
                }
                page ++;
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void goMovieDetailPage(String url) throws Exception {
        Document doc = Jsoup.connect(url).get();
        // 获取评分：第二个元素的一个介绍属性
        // <div is="i71-play" :page-info="{&quot;albumId&quot;:0,&quot;albumName&quot;:&quot;亡命救赎&quot;,&quot;imageUrl&quot;:&quot;//pic6.iqiyipic.com/image/20190111/3f/e3/v_120486505_m_601_m8.jpg&quot;,&quot;tvId&quot;:1483448200,&quot;vid&quot;:&quot;678f2d187a252c9bb9479e4fc3570d07&quot;,&quot;cid&quot;:1,&quot;isSource&quot;:false,&quot;contentType&quot;:1,&quot;vType&quot;:&quot;video&quot;,&quot;pType&quot;:&quot;advplay&quot;,&quot;pageNo&quot;:&quot;1&quot;,&quot;pageType&quot;:&quot;player&quot;,&quot;userId&quot;:0,&quot;pageUrl&quot;:&quot;http://www.iqiyi.com/v_19rr2srld0.html&quot;,&quot;tvName&quot;:&quot;亡命救赎&quot;,&quot;isfeizhengpian&quot;:&quot;true&quot;,&quot;categoryName&quot;:&quot;电影&quot;,&quot;categories&quot;:&quot;动作,犯罪,&quot;,&quot;downloadAllowed&quot;:1,&quot;publicLevel&quot;:&quot;PUBLIC&quot;,&quot;payMark&quot;:1,&quot;payMarkUrl&quot;:&quot;http://pic0.iqiyipic.com/common/20171106/ac/1b/vip_100000_v_601.png&quot;,&quot;vipType&quot;:[0],&quot;qiyiProduced&quot;:0,&quot;exclusive&quot;:0,&quot;tvYear&quot;:&quot;0&quot;,&quot;duration&quot;:&quot;1:24:02&quot;,&quot;wallId&quot;:0,&quot;rewardAllowed&quot;:0,&quot;commentAllowed&quot;:1,&quot;heatShowTypes&quot;:&quot;auto&quot;,&quot;videoTemplate&quot;:1,&quot;issueTime&quot;:&quot;2019-01-11&quot;}" :video-info="{&quot;displayUpDown&quot;:1,&quot;baikeUrl&quot;:&quot;https://baike.baidu.com/item/%E4%BA%A1%E5%91%BD%E6%95%91%E8%B5%8E/23111752?fr=aladdin&quot;,&quot;imageUrl&quot;:&quot;http://pic6.iqiyipic.com/image/20190111/3f/e3/v_120486505_m_601_m8.jpg&quot;,&quot;score&quot;:7.7,&quot;downloadAllowed&quot;:1,&quot;contentType&quot;:1,&quot;endTime&quot;:-1,&quot;qiyiProduced&quot;:0,&quot;startTime&quot;:-1,&quot;vip&quot;:{&quot;purchaseType&quot;:5,&quot;payMarkUrl&quot;:&quot;http://pic0.iqiyipic.com/common/20171106/ac/1b/vip_100000_v_601.png&quot;,&quot;vipType&quot;:[0],&quot;payMark&quot;:1},&quot;description&quot;:&quot;Lydia很小的时候父亲就进了监狱，她从小和母亲生活在一起，但骨子里继承了父亲Link的基因，十分叛逆。一次闯下大祸后，Lydia走投无路联系到多年未曾见面的父亲，Link已经退隐江湖很多年，在拖车里以纹身为生。为了解决女儿的困境，他鼓起勇气挺起胸膛，以父亲的身份对决Jonah一行人，用鲜血为女儿换来了重生...&quot;,&quot;userId&quot;:0,&quot;vid&quot;:&quot;678f2d187a252c9bb9479e4fc3570d07&quot;,&quot;firstPublishTime&quot;:1547191812000,&quot;videoPageStatus&quot;:1,&quot;albumId&quot;:0,&quot;channelId&quot;:1,&quot;commentAllowed&quot;:1,&quot;intellectual&quot;:{&quot;id&quot;:2311450770,&quot;deleted&quot;:0,&quot;tickets&quot;:[218931014]},&quot;recommendation&quot;:&quot;父女联手勇斗毒贩&quot;,&quot;posterUrl&quot;:&quot;http://pic5.iqiyipic.com/image/20190110/96/66/v_120486505_m_600.jpg&quot;,&quot;period&quot;:&quot;20181130&quot;,&quot;deleted&quot;:0,&quot;url&quot;:&quot;http://www.iqiyi.com/v_19rr2srld0.html&quot;,&quot;videoType&quot;:1,&quot;cast&quot;:{&quot;writers&quot;:[{&quot;id&quot;:228361305,&quot;imageUrl&quot;:&quot;http://pic0.iqiyipic.com/image/20181228/5e/f7/p_5173472_m_601_m2.jpg&quot;,&quot;userId&quot;:0,&quot;name&quot;:&quot;安德丽亚·贝尔洛夫&quot;,&quot;roleCName&quot;:&quot;编剧&quot;,&quot;roleName&quot;:&quot;writers&quot;,&quot;circleId&quot;:0,&quot;relatedMovieCount&quot;:0}],&quot;directors&quot;:[{&quot;id&quot;:216002505,&quot;imageUrl&quot;:&quot;http://pic7.iqiyipic.com/image/20181227/10/c1/p_5049653_m_601_m2.jpg&quot;,&quot;userId&quot;:0,&quot;name&quot;:&quot;让-弗朗西斯·瑞切&quot;,&quot;roleCName&quot;:&quot;导演&quot;,&quot;roleName&quot;:&quot;directors&quot;,&quot;circleId&quot;:0,&quot;relatedMovieCount&quot;:1}],&quot;mainActors&quot;:[{&quot;id&quot;:215121905,&quot;imageUrl&quot;:&quot;http://pic7.iqiyipic.com/image/20160307/d6/80/p_2002457_m_601_m1.jpg&quot;,&quot;userId&quot;:0,&quot;roles&quot;:[&quot;John Link&quot;],&quot;name&quot;:&quot;梅尔·吉布森&quot;,&quot;roleCName&quot;:&quot;主演&quot;,&quot;roleName&quot;:&quot;mainActors&quot;,&quot;circleId&quot;:0,&quot;relatedMovieCount&quot;:22},{&quot;id&quot;:223731605,&quot;imageUrl&quot;:&quot;http://pic6.iqiyipic.com/image/20181227/3d/a7/p_5127408_m_601_m1.jpg&quot;,&quot;userId&quot;:0,&quot;roles&quot;:[&quot;Lydia&quot;],&quot;name&quot;:&quot;艾琳·莫里亚蒂&quot;,&quot;roleCName&quot;:&quot;主演&quot;,&quot;roleName&quot;:&quot;mainActors&quot;,&quot;circleId&quot;:0,&quot;relatedMovieCount&quot;:1},{&quot;id&quot;:206221405,&quot;imageUrl&quot;:&quot;http://pic0.iqiyipic.com/image/20181228/4d/76/p_1056878_m_601_m1.jpg&quot;,&quot;userId&quot;:0,&quot;roles&quot;:[&quot;Preacher&quot;],&quot;name&quot;:&quot;Michael Parks&quot;,&quot;roleCName&quot;:&quot;主演&quot;,&quot;roleName&quot;:&quot;mainActors&quot;,&quot;circleId&quot;:0,&quot;relatedMovieCount&quot;:1},{&quot;id&quot;:206245105,&quot;imageUrl&quot;:&quot;http://pic9.iqiyipic.com/image/20181229/81/a2/p_1057115_m_601_m2.jpg&quot;,&quot;userId&quot;:0,&quot;roles&quot;:[&quot;Jonah&quot;],&quot;name&quot;:&quot;迭戈·鲁纳&quot;,&quot;roleCName&quot;:&quot;主演&quot;,&quot;roleName&quot;:&quot;mainActors&quot;,&quot;starWallStatus&quot;:2,&quot;circleId&quot;:213436647,&quot;relatedMovieCount&quot;:6},{&quot;id&quot;:201963605,&quot;imageUrl&quot;:&quot;http://pic6.iqiyipic.com/image/20181228/8a/bf/p_1014300_m_601_m2.jpg&quot;,&quot;userId&quot;:0,&quot;roles&quot;:[&quot;Ursula&quot;],&quot;name&quot;:&quot;伊丽莎白·霍尔姆&quot;,&quot;roleCName&quot;:&quot;主演&quot;,&quot;roleName&quot;:&quot;mainActors&quot;,&quot;circleId&quot;:0,&quot;relatedMovieCount&quot;:1}]},&quot;contentRating&quot;:3,&quot;areas&quot;:[&quot;法国&quot;],&quot;officialEpisodeId&quot;:0,&quot;topChart&quot;:0,&quot;lastPublishTime&quot;:1547191812000,&quot;episodeType&quot;:0,&quot;editorInfo&quot;:&quot;&quot;,&quot;exclusive&quot;:0,&quot;isKnowledgePayment&quot;:0,&quot;superAlbumId&quot;:0,&quot;mode1080p&quot;:1,&quot;imageUrlWebp85&quot;:&quot;http://pic6.iqiyipic.com/image/20190111/3f/e3/v_120486505_m_601_m8.webp&quot;,&quot;circle&quot;:{&quot;id&quot;:0,&quot;type&quot;:0},&quot;dynamicImageWebp&quot;:&quot;&quot;,&quot;effective&quot;:1,&quot;panorama&quot;:{&quot;zoomRate&quot;:1.0,&quot;viewAngleY&quot;:0.0,&quot;viewAngleX&quot;:0.0,&quot;videoType&quot;:1},&quot;order&quot;:1,&quot;dynamicImageGif&quot;:&quot;&quot;,&quot;name&quot;:&quot;亡命救赎&quot;,&quot;rewardAllowed&quot;:0,&quot;publicLevel&quot;:0,&quot;ipId&quot;:2311450770,&quot;tvId&quot;:1483448200,&quot;copyrightStatus&quot;:1,&quot;isPano&quot;:0,&quot;duration&quot;:5042,&quot;isLequ&quot;:true,&quot;shortTitle&quot;:&quot;亡命救赎&quot;,&quot;displayCircle&quot;:0,&quot;iresearchCrumb&quot;:{&quot;category&quot;:&quot;电影&quot;,&quot;albumName&quot;:&quot;&quot;},&quot;subtitle&quot;:&quot;&quot;,&quot;categories&quot;:[{&quot;id&quot;:27815,&quot;parentId&quot;:2739633,&quot;subName&quot;:&quot;规格&quot;,&quot;level&quot;:1,&quot;subType&quot;:7,&quot;name&quot;:&quot;院线&quot;,&quot;qipuId&quot;:2781533,&quot;url&quot;:&quot;http://list.iqiyi.com/www/1/------27815------------.html&quot;},{&quot;id&quot;:291,&quot;parentId&quot;:1800133,&quot;subName&quot;:&quot;类型&quot;,&quot;level&quot;:0,&quot;subType&quot;:2,&quot;name&quot;:&quot;犯罪&quot;,&quot;qipuId&quot;:29133,&quot;url&quot;:&quot;http://list.iqiyi.com/www/1/-291-----------------.html&quot;},{&quot;id&quot;:11,&quot;parentId&quot;:1800133,&quot;subName&quot;:&quot;类型&quot;,&quot;level&quot;:0,&quot;subType&quot;:2,&quot;name&quot;:&quot;动作&quot;,&quot;qipuId&quot;:1133,&quot;url&quot;:&quot;http://list.iqiyi.com/www/1/-11-----------------.html&quot;},{&quot;id&quot;:20004,&quot;parentId&quot;:2000033,&quot;subName&quot;:&quot;配音语种&quot;,&quot;level&quot;:0,&quot;subType&quot;:0,&quot;name&quot;:&quot;英语&quot;,&quot;qipuId&quot;:2000433,&quot;url&quot;:&quot;http://list.iqiyi.com/www/1/------------------.html&quot;}],&quot;channel&quot;:{&quot;id&quot;:1,&quot;ppsUrl&quot;:&quot;http://www.iqiyi.com/dianying/&quot;,&quot;logoImage&quot;:&quot;http://pic1.qiyipic.com/common/lego/20150119/fa99e22ecc8f4ad2ad1bb04760d28c2e.png&quot;,&quot;description&quot;:&quot;爱奇艺电影频道为您提供最新最热最经典最高清的电影大片，打造互联网观影首选平台！&quot;,&quot;name&quot;:&quot;电影&quot;,&quot;url&quot;:&quot;http://www.iqiyi.com/dianying/&quot;},&quot;imageUrlWebp55&quot;:&quot;http://m.iqiyipic.com/image/20190111/3f/e3/v_120486505_m_601_m8.webp&quot;,&quot;featureKeyword&quot;:&quot;亡命救赎&quot;}">
        Element firstDiv = doc.getElementById("iqiyi-main").children().first();
        // 将电影的json信息写入文件
        BufferedWriter writer = new BufferedWriter(new FileWriter("movies/page-info/" + UUID.randomUUID() + ".video"));
        writer.write(firstDiv.attr(":video-info"));
        writer.flush();
        writer.close();
    }
}
