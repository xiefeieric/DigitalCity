package com.fei.digitalcity.model;

import java.util.ArrayList;

/**
 * Created by Fei on 11/10/15.
 */
public class ChildNewsData {

    public int retcode;
    public ItemChildNewsData data;

    @Override
    public String toString() {
        return "ChildNewsData{" +
                "data=" + data +
                '}';
    }

    public class ItemChildNewsData {
        public String more;
        public String title;
        public ArrayList<ItemChildNewsDetail> news;
        public ArrayList<ItemChildTopNews> topnews;

        @Override
        public String toString() {
            return "ItemChildNewsData{" +
                    "title='" + title + '\'' +
                    ", news=" + news +
                    ", topnews=" + topnews +
                    '}';
        }

        public class ItemChildNewsDetail {

            public String id;
            public String title;
            public String listimage;
            public String type;
            public String url;
            public String pubdate;

            @Override
            public String toString() {
                return "ItemChildNewsDetail{" +
                        "title='" + title + '\'' +
                        ", listimage='" + listimage + '\'' +
                        '}';
            }
        }


        public class ItemChildTopNews {

            public String id;
            public String title;
            public String topimage;
            public String type;
            public String url;
            public String pubdate;

//            title: "蜗居生活",
//            topimage: "http://10.0.2.2:8080/zhbj/10007/1452327318UU91.jpg",
//            type: "news",
//            url: "http://10.0.2.2:8080/zhbj/10007/724D6A55496A11726628.html"


            @Override
            public String toString() {
                return "ItemChildTopNews{" +
                        "title='" + title + '\'' +
                        ", topimage='" + topimage + '\'' +
                        '}';
            }
        }
    }

}
