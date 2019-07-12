package com.zlw.excel.exception;

/**
 * excel 导入异常
 * 
 * @author fukui
 */
public class ExcelImportException extends RuntimeException {

	private static final long serialVersionUID = -6944008486353345921L;

	private String code;

	public ExcelImportException() {
	}

	public ExcelImportException(String code) {
		super(code);
		this.setCode(code);
	}

	public ExcelImportException(Throwable t) {
		super(t);
	}

	public ExcelImportException(String code, Throwable t) {
		super(t);
		this.setCode(code);
	}

	public ExcelImportException(String code, String msg) {
		super(msg);
		this.setCode(code);
	}

	public ExcelImportException(String code, String msg, Throwable t) {
		super(msg, t);
		this.setCode(code);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

}
