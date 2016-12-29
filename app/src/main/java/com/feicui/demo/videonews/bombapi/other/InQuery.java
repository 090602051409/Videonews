package com.feicui.demo.videonews.bombapi.other;

/**
 * Created by Administrator on 2016/12/26.
 */

public class InQuery {
    //// URL编码参数
//'where= {
//       "查询字段": {
//          "$inQuery": {
//                 "where": {
//                      "objectId": 用户名id
//                      },
//                      "className": "表名"
//                  }
//          }
//        }'

        private String field;//查询字段
        private String className;//表名
        private String objectId;//用户id

        public InQuery(String field, String className, String objectId) {
            this.field = field;
            this.className = className;
            this.objectId = objectId;
        }
}
