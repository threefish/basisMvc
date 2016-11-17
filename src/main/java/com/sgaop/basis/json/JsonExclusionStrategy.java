package com.sgaop.basis.json;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/17 0017
 * To change this template use File | Settings | File Templates.
 */
public class JsonExclusionStrategy implements ExclusionStrategy {

    private String[] exclusions;

    public JsonExclusionStrategy(String[] exclusions) {
        this.exclusions = exclusions;
    }


    @Override
    public boolean shouldSkipField(FieldAttributes fieldAttributes) {
        for (String field : exclusions) {
            if (field.equals(fieldAttributes.getName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldSkipClass(Class<?> aClass) {
        return false;
    }
}
