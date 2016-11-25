package com.sgaop.basis.dao;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/23 0023
 * To change this template use File | Settings | File Templates.
 */
public class Criteria {

    private boolean isChild = false;

    private String type;

    private String colum;

    private String operator;

    private Object val;

    private String strs;

    private Object[] vals;

    private LinkedList<Criteria> criteriaLinkedList = new LinkedList<>();

    public Criteria(boolean child, String type) {
        this.isChild = true;
        this.type = type;
    }

//    public Criteria(String strs) {
//        this.strs = strs;
//    }

    public Criteria(String type, String strs) {
        this.type = type;
        this.strs = strs;
    }

    public Criteria(String type, String strs, Object... par) {
        this.type = type;
        this.strs = strs;
        this.vals = par;
    }


    public Criteria(String type, String colum, String operator, Object val) {
        this.type = type;
        this.colum = colum;
        this.operator = operator;
        this.val = val;
    }

    public Object[] getVals() {
        return vals;
    }

    public void setVals(Object[] vals) {
        this.vals = vals;
    }


    public String getType() {
        return type;
    }

    public String getStrs() {
        return strs;
    }

    public void setStrs(String strs) {
        this.strs = strs;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getColum() {
        return colum;
    }

    public boolean isChild() {
        return isChild;
    }

    public void setChild(boolean child) {
        isChild = child;
    }

    public void setColum(String colum) {
        this.colum = colum;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }

    public LinkedList<Criteria> getCriteriaLinkedList() {
        return criteriaLinkedList;
    }

    public void addCriteriaLinked(Criteria criteria) {
        this.criteriaLinkedList.add(criteria);
    }
}

