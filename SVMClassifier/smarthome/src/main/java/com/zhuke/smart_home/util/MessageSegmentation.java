package com.zhuke.smart_home.util;

import com.zhuke.smart_home.beans.SHMessage;

import java.util.Date;
import java.util.StringTokenizer;

/**
 * 控制报文分割
 * Created by ZHUKE on 2016/3/31.
 */
public class MessageSegmentation {

    /**
     * 控制报文格式：fromSdId-toSdId-statusId
     */
    public static SHMessage messageSegmentation(String messageStr) {
        StringTokenizer stringTokenizer = new StringTokenizer(messageStr, "-");
        SHMessage message = new SHMessage();
        message.setFromSdId(Long.parseLong(stringTokenizer.nextToken()));
        message.setToSdId(Long.parseLong(stringTokenizer.nextToken()));
        message.setStatusId(Long.parseLong(stringTokenizer.nextToken()));
        message.setSendDate(new Date());
        return message;
    }
}
