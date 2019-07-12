package com.zlw.commons.utils;


public class MD5Exception extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public MD5Exception() {
    }

    public MD5Exception(Throwable t) {
        super(t);
    }

    public MD5Exception(String msg) {
        super(msg);
    }

    public MD5Exception(String msg, Throwable t) {
        super(msg, t);
    }
}
