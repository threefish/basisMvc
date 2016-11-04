package com.sgaop.basis.trans;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/1 0001
 * To change this template use File | Settings | File Templates.
 */
public interface TransAop {

    String NONE = "TRANS_NONE";

    String READ_UNCOMMITTED = "TRANS_READ_UNCOMMITTED";

    String READ_COMMITTED = "TRANS_READ_COMMITTED";

    String REPEATABLE_READ = "TRANS_REPEATABLE_READ";

    String SERIALIZABLE = "TRANS_SERIALIZABLE";

}