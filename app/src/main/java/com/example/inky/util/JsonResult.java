package com.example.inky.util;

import android.util.Log;

import java.util.List;

public class JsonResult {

    private List<DataBean> data;

    public List<DataBean> getData() {
//        Log.v("||||||||||||", "setData: "+ data);
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
        Log.v("/////////", "setData: "+ this.data);
    }

    public static class DataBean {
        /**
         * id : 1
         * title : 王羲之书法
         * content : aaaaaaaaaaaaaaaaaa
         */

        private String id;
        private String title;
        private String content;

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

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
