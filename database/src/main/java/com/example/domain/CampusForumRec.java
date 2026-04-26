package com.example.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CampusForumRec {

    private String recordId;          // 记录id，主键
    private String pgId;             // 学号，外键
    private Date upTime;             // 上线时间
    private Date postTime;           // 发帖时间
    private String postTopic;        // 发帖主题
    private String postModel;        // 发帖模块
    private String postContent;      // 发帖内容（TEXT类型）
    private Date replyTime;          // 回帖时间
    private String replyPostTopic;   // 所回帖主题
    private String replyPostModel;   // 所回帖模块
    private String replyPostContent; // 所回帖内容（TEXT类型）
    private String replyContent;     // 回复内容（TEXT类型）
}