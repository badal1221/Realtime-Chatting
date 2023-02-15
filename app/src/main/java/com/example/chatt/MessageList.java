package com.example.chatt;

public class MessageList {
    String name,mobile,lastmessage,profilepic,chatkey;
    int unseenmsg;

    public MessageList(String name, String mobile, String lastmessage,String profilepic, int unseenmsg,String chatkey) {
        this.name = name;
        this.mobile = mobile;
        this.lastmessage = lastmessage;
        this.profilepic=profilepic;
        this.unseenmsg = unseenmsg;
        this.chatkey=chatkey;
    }

    public String getChatkey() {
        return chatkey;
    }

    public void setChatkey(String chatkey) {
        this.chatkey = chatkey;
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    public int getUnseenmsg() {
        return unseenmsg;
    }

    public void setUnseenmsg(int unseenmsg) {
        this.unseenmsg = unseenmsg;
    }

    public String getProfilepic() { return profilepic; }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }
}
