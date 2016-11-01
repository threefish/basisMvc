package com.sgaop.basis.trans;

/**
 * Created by IntelliJ IDEA.
 * User: 306955302@qq.com
 * Date: 2016/11/1 0001
 * To change this template use File | Settings | File Templates.
 */
public interface TransAop {

    String NONE = "txNONE";

    String READ_UNCOMMITTED = "txREAD_UNCOMMITTED";

    String READ_COMMITTED = "txREAD_COMMITTED";

    String REPEATABLE_READ = "txREPEATABLE_READ";

    String SERIALIZABLE = "txSERIALIZABLE";

}