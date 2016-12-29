package com.sgaop.basis.dao.entity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sgaop.basis.json.TimestampTypeAdapter;
import com.sgaop.basis.util.ClassTool;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/10 0010
 * To change this template use File | Settings | File Templates.
 */
public class Record implements Map<String, Object>, Serializable {

    private Map<String, Object> map = new LinkedHashMap();

    protected static int DEFAULT_INT = -1;

    public Object remove(String name) {
        return this.map.remove(name.toLowerCase());
    }

    public int getColumnCount() {
        return this.map.size();
    }

    public Set<String> getColumnNames() {
        return this.map.keySet();
    }

    public int getInt(String name) {
        try {
            String val = this.getString(name);
            return val == null ? DEFAULT_INT : Integer.parseInt(val);
        } catch (Exception e) {
            return DEFAULT_INT;
        }
    }

    public String getString(String name) {
        Object val = this.get(name);
        return null == val ? null : val.toString();
    }

    public double getDouble(String name) {
        String val = this.getString(name);
        return val == null ? DEFAULT_INT : Double.parseDouble(val);
    }

    public double getFloat(String name) {
        String val = this.getString(name);
        return val == null ? DEFAULT_INT : Float.parseFloat(val);
    }

    public boolean getBoolean(String name) {
        String val = this.getString(name);
        return val == null ? false : Boolean.parseBoolean(val);
    }


    public long getLong(String name) {
        String val = this.getString(name);
        return val == null ? DEFAULT_INT : Long.parseLong(val);
    }

    public Timestamp getTimestamp(String name) {
        Object val = this.get(name);
        Timestamp dateVal = null;
        try {
            dateVal = (Timestamp) ClassTool.ParamCast(Timestamp.class, val);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateVal;
    }

    public String toJson() {
        GsonBuilder gb = new GsonBuilder();
        gb.setDateFormat("yyyy-MM-dd hh:mm:ss");
        gb.registerTypeAdapter(Timestamp.class, new TimestampTypeAdapter()).create();
        Gson gson = gb.create();
        return gson.toJson(this.map);
    }

    public String toString() {
        return toJson();
    }

    public void clear() {
        this.map.clear();
    }

    public boolean containsKey(Object key) {
        return this.map.containsKey(key.toString().toLowerCase());
    }

    public boolean containsValue(Object value) {
        return this.map.containsValue(value);
    }

    public Set<Entry<String, Object>> entrySet() {
        return this.map.entrySet();
    }

    public boolean equals(Object out) {
        return this.map.equals(out);
    }

    public Object get(Object name) {
        return null == name ? null : this.map.get(name.toString().toLowerCase());
    }

    public int hashCode() {
        return this.map.hashCode();
    }

    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    public Set<String> keySet() {
        return this.map.keySet();
    }

    public Object put(String name, Object value) {
        return this.map.put(name.toLowerCase(), value);
    }

    public void putAll(Map<? extends String, ? extends Object> out) {
        Iterator var2 = out.entrySet().iterator();
        while (var2.hasNext()) {
            Entry entry = (Entry) var2.next();
            this.map.put(((String) entry.getKey()).toLowerCase(), entry.getValue());
        }

    }

    public Object remove(Object key) {
        return this.map.remove(key.toString().toLowerCase());
    }

    public int size() {
        return this.map.size();
    }

    public Collection<Object> values() {
        return this.map.values();
    }

}
