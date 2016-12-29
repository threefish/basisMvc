package com.sgaop.basis.dao;


import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/23 0023
 * To change this template use File | Settings | File Templates.
 */
public class Condition {

    private LinkedList<Criteria> criteriaLinkedList = new LinkedList<>();

    private LinkedList<Object> valLinkedList = new LinkedList<>();


    /**
     * @param sql 字段
     * @return cnd
     */
    public Condition asc(String sql) {
        criteriaLinkedList.add(new Criteria("order", "order by " + sql + " asc"));
        return this;
    }

    /**
     * @param sql 字段
     * @return cnd
     */
    public Condition desc(String sql) {
        criteriaLinkedList.add(new Criteria("order", "order by " + sql + " desc"));
        return this;
    }


    /**
     * @param sql 字段
     * @return cnd
     */
    public Condition strs(String sql) {
        criteriaLinkedList.add(new Criteria("strs", sql));
        return this;
    }

    /**
     * @param sql 字段
     * @return cnd
     */
    public Condition strs(String sql, Object... parme) {
        criteriaLinkedList.add(new Criteria("strs", sql, parme));
        return this;
    }


    /**
     * @param colum    字段
     * @param operator 操作符
     * @param val      值
     * @return cnd
     */
    public Condition and(String colum, String operator, Object val) {
        criteriaLinkedList.add(new Criteria("and", colum, operator, val));
        return this;
    }

    /**
     * @param condition 字段
     * @return cnd
     */
    public Condition and(Condition condition) {
        int size = condition.criteriaLinkedList.size();
        if (size > 0) {
            Criteria criteria = new Criteria(true, "and");
            for (int i = 0; i < size; i++) {
                Criteria criteriatemp = condition.criteriaLinkedList.get(i);
                if (i == 0) {
                    criteriatemp.setType("");
                }
                criteria.addCriteriaLinked(criteriatemp);
            }
            this.criteriaLinkedList.add(criteria);
        }
        return this;
    }

    /**
     * @param colum    字段
     * @param operator 操作符
     * @param val      值
     * @return cnd
     */
    public Condition or(String colum, String operator, Object val) {
        criteriaLinkedList.add(new Criteria("or", colum, operator, val));
        return this;
    }


    /**
     * @param condition 字段
     * @return cnd
     */
    public Condition or(Condition condition) {
        int size = condition.criteriaLinkedList.size();
        if (size > 0) {
            Criteria criteria = new Criteria(true, "or");
            for (int i = 0; i < size; i++) {
                Criteria criteriatemp = condition.criteriaLinkedList.get(i);
                if (i == 0) {
                    criteriatemp.setType("");
                }
                criteria.addCriteriaLinked(criteriatemp);
            }
            this.criteriaLinkedList.add(criteria);
        }
        return this;
    }

    public String toSql() {
        return assembly(criteriaLinkedList);
    }

    public Object[] valToArry() {
        return valLinkedList.toArray();
    }

    private String assembly(LinkedList<Criteria> criteriaLinkedList) {
        StringBuffer sb = new StringBuffer();
        for (Criteria criteria : criteriaLinkedList) {
            if (criteria.isChild()) {
                sb.append(" " + criteria.getType());
                if (criteria.getCriteriaLinkedList().size() != 0) {
                    sb.append("(");
                    sb.append(assembly(criteria.getCriteriaLinkedList()));
                    sb.append(")");
                }
            } else if ("strs".equals(criteria.getType())) {
                sb.append(" " + criteria.getStrs());
                if (criteria.getVals() != null && criteria.getVals().length != 0) {
                    for (Object val : criteria.getVals()) {
                        valLinkedList.add(val);
                    }
                }
            } else if ("order".equals(criteria.getType())) {
                sb.append(" " + criteria.getStrs());
            } else {
                sb.append(" " + criteria.getType());
                sb.append(" " + criteria.getColum());
                sb.append(criteria.getOperator() + "?");
                valLinkedList.add(criteria.getVal());
            }
        }
        String sql = sb.toString();
        if (sql.startsWith(" and")) {
            sql = " where" + sql.substring(4, sql.length());
        }
        if (sql.startsWith(" or") && !sql.startsWith(" order")) {
            sql = " where" + sql.substring(3, sql.length());
        }
        return sql;
    }

    public static void main(String[] args) {
        Condition cnd = new Condition();
//        cnd.or("name", "=", "test");
//        cnd.and("id", "=", 2);
//        cnd.or("xx", "=", 99);
//        cnd.strs("find_in_set(?,?)", 1, 2);
//        Condition cndCom = new Condition();
//        cndCom.and("dd", "=", 123);
//        cndCom.or("mm", "=", "wq");
//        cndCom.and("mmxx", "=", "xxx");
//        cnd.and(cndCom);
        cnd.asc("mmxx1");


        System.out.println(cnd.toSql());
        for (Object object : cnd.valToArry()) {
            System.out.println("【" + object + "】");
        }
    }
}
