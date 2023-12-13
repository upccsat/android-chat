package com.ntu.treatment.modle;


public class ChatMessage {
    private String content;
    private String time;
    private String fromUserName;
    private String fromUsereImage;
    private int isMeSend;//0是对方发送 1是自己发送
    private int isRead;//是否已读（0未读 1已读）

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getIsMeSend() {
        return isMeSend;
    }

    public void setIsMeSend(int isMeSend) {
        this.isMeSend = isMeSend;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }
    public void setFromUserName(String fromUsereName){this.fromUserName=fromUsereName;}
    public void setFromUsereImage(String fromUsereImage){this.fromUsereImage=fromUsereImage;}
    public String getFromUserName(){return this.fromUserName;}
    public String getFromUsereImage(){return this.fromUsereImage;}
}
