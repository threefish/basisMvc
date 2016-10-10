package com.sgaop.basis.aop;

public interface TransAop {

    String NONE = "txNONE";
    String READ_UNCOMMITTED = "txREAD_UNCOMMITTED";
    String READ_COMMITTED = "txREAD_COMMITTED";
    String REPEATABLE_READ = "txREPEATABLE_READ";
    String SERIALIZABLE = "txSERIALIZABLE";
    
}