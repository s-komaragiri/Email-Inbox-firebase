package com.vveginati.inclass13.beans;

import java.io.Serializable;
import java.util.Map;

public class Mails implements Serializable {
    private String senderName;
    private String detailedMessage;
    private Boolean isRead;
    private String msgkey;
    private Map<String,Object> mailTimeStamp;

    public Map<String, Object> getMailTimeStamp() {
        return mailTimeStamp;
    }

    public void setMailTimeStamp(Map<String, Object> mailTimeStamp) {
        this.mailTimeStamp = mailTimeStamp;
    }




    public String getSentkey() {
        return sentkey;
    }

    public void setSentkey(String sentkey) {
        this.sentkey = sentkey;
    }

    private String sentkey;

    public String getMsgkey() {
        return msgkey;
    }

    public void setMsgkey(String msgkey) {
        this.msgkey = msgkey;
    }

    public Mails() {
    }

    public Mails(String senderName, String detailedMessage, Boolean isRead) {
        this.senderName = senderName;
        this.detailedMessage = detailedMessage;
        this.isRead = isRead;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getDetailedMessage() {
        return detailedMessage;
    }

    public void setDetailedMessage(String detailedMessage) {
        this.detailedMessage = detailedMessage;
    }

    public Boolean getRead() {
        return isRead;
    }

    public void setRead(Boolean read) {
        isRead = read;
    }

    @Override
    public String toString() {
        return "Mails{" +
                "senderName='" + senderName + '\'' +
                ", detailedMessage='" + detailedMessage + '\'' +
                ", isRead=" + isRead +
                '}';
    }
}
