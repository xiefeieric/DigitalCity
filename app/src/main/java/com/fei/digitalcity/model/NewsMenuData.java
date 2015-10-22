package com.fei.digitalcity.model;

import java.util.ArrayList;

/**
 * Created by Fei on 09/10/15.
 */
public class NewsMenuData {

    String retcode;
    ArrayList<NewsMenuItem> data;

    public String getRetcode() {
        return retcode;
    }

    public void setRetcode(String retcode) {
        this.retcode = retcode;
    }

    public ArrayList<NewsMenuItem> getData() {
        return data;
    }

    public void setData(ArrayList<NewsMenuItem> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "NewsMenuData{" +
                "retcode='" + retcode + '\'' +
                ", data=" + data +
                '}';
    }

    public class NewsMenuItem {

        String id;
        String title;
        int type;
        String url;
        ArrayList<NewsMenuItemChilden> children;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public ArrayList<NewsMenuItemChilden> getChildren() {
            return children;
        }

        public void setChildren(ArrayList<NewsMenuItemChilden> children) {
            this.children = children;
        }


        @Override
        public String toString() {
            return "NewsMenuItem{" +
                    "children=" + children +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", url='" + url + '\'' +
                    '}';
        }

        public class NewsMenuItemChilden {

            String id;
            String title;
            int type;
            String url;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            @Override
            public String toString() {
                return "NewsMenuItemChilden{" +
                        "title='" + title + '\'' +
                        ", type=" + type +
                        ", url='" + url + '\'' +
                        '}';
            }
        }

    }

}
