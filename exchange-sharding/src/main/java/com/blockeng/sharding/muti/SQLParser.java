package com.blockeng.sharding.muti;

/**
 * SQL解析工具
 */
public class SQLParser {

    final static String FROM1 = "FROM";
    final static String FROM2 = "from";
    final static String INTO1 = "INTO";
    final static String INTO2 = "into";
    final static String UPDATE1 = "UPDATE";
    final static String UPDATE2 = "update";

    /**
     * 解析sql类型
     */
    public String parserType(String sql) {
        String type = sql.trim().toUpperCase().substring(0, 6);
        switch (type) {
            case "SELECT":
            case "INSERT":
            case "UPDATE":
            case "DELETE":
                return type;
            default:
                return "OTHER";
        }
    }

    /**
     * 解析sql对应的表名
     */
    public String parserTableName(String sql) {
        String type = parserType(sql);
        switch (type) {
            case "SELECT":
            case "DELETE":
                return findAfterFROM(sql).trim().replace("`", "");
            case "UPDATE":
                return findAfterUPDATE(sql).trim().replace("`", "");
            case "INSERT":
                return findAfterINTO(sql).trim().replace("`", "");
        }
        return null;
    }

    /**
     * 获取字符串在大段落内的位置
     */
    private int firstIndexOfTwoFrom(String index1, String index2, String src, int from) {
        int i1 = src.indexOf(index1, from);
        int i2 = src.indexOf(index2, from);
        if (i1 > -1 && i2 > -1) {
            return i1 < i2 ? i1 : i2;
        }
        return i1 > i2 ? i1 : i2;
    }

    /**
     * 找到FROM传后面的一个单词
     */
    private String findAfterFROM(String sql) {
        int i = firstIndexOfTwoFrom(FROM1, FROM2, sql, 0);
        if (i < 0)
            return null;
        else {
            String a = sql.substring(i + 4).trim();
            if (a.startsWith("("))
                return findAfterFROM(a);
            else
                return a.indexOf(" ") > 0 ? a.substring(0, a.indexOf(" ")) : a;
        }
    }

    /**
     * 找到INTO传后面的一个单词
     */
    private String findAfterINTO(String sql) {
        String a = sql.substring(firstIndexOfTwoFrom(INTO1, INTO2, sql, 0) + 4).trim();
        return a.substring(0, a.indexOf(" "));
    }

    /**
     * 找到UPDATE传后面的一个单词
     */
    private String findAfterUPDATE(String sql) {
        String a = sql.substring(firstIndexOfTwoFrom(UPDATE1, UPDATE2, sql, 0) + 6).trim();
        return a.substring(0, a.indexOf(" "));
    }
}
