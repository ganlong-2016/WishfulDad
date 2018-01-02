package com.drkj.wishfuldad.bean;

import java.util.List;

/**
 * Created by ganlong on 2017/12/29.
 */

public class ChatResultBean {

    /**
     * status : 1
     * message : 发送成功
     * data : [{"id":73,"fromid":2,"fromname":"18628102767","fromavatar":"/upload/timg2.jpg","toid":1,"content":"测试aa11","timeline":1514448997,"type":"friend","needsend":0}]
     */

    private int status;
    private String message;
    private List<DataBean> data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 73
         * fromid : 2
         * fromname : 18628102767
         * fromavatar : /upload/timg2.jpg
         * toid : 1
         * content : 测试aa11
         * timeline : 1514448997
         * type : friend
         * needsend : 0
         */

        private int id;
        private int fromid;
        private String fromname;
        private String fromavatar;
        private int toid;
        private String content;
        private long timeline;
        private String type;
        private int needsend;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getFromid() {
            return fromid;
        }

        public void setFromid(int fromid) {
            this.fromid = fromid;
        }

        public String getFromname() {
            return fromname;
        }

        public void setFromname(String fromname) {
            this.fromname = fromname;
        }

        public String getFromavatar() {
            return fromavatar;
        }

        public void setFromavatar(String fromavatar) {
            this.fromavatar = fromavatar;
        }

        public int getToid() {
            return toid;
        }

        public void setToid(int toid) {
            this.toid = toid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getTimeline() {
            return timeline;
        }

        public void setTimeline(long timeline) {
            this.timeline = timeline;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getNeedsend() {
            return needsend;
        }

        public void setNeedsend(int needsend) {
            this.needsend = needsend;
        }
    }
}
