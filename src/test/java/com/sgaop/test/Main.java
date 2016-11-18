package com.sgaop.test;

import com.google.gson.Gson;
import com.sgaop.basis.json.JsonFormat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/17 0017
 * To change this template use File | Settings | File Templates.
 */
public class Main {


    public static void main(String[] args) {
        String json="{locked:'dsad|sas|d'}";
        Gson gson=new Gson();
        JsonFormat map=gson.fromJson(json, JsonFormat.class);
        System.out.println(map.getLocked());
    }
}
