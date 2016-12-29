package com.feicui.demo.videonews.bombapi.result;

import java.util.List;

/**
 * Created by Administrator on 2016/12/26.
 */

public class QueryResult<Model> {
    private List<Model> results;
    public List<Model> getResults(){
        return results;
    }
}
