package com.zlw.commons.validation;

import com.zlw.commons.validation.annotation.Entity;
import com.zlw.commons.validation.annotation.NotNull;
import com.zlw.commons.validation.annotation.Numeric;
import com.zlw.commons.validation.annotation.RegMatcher;
import com.zlw.commons.validation.enums.NumericEnum;

@Entity
public class User2 {

	@Numeric
	private String numeric;
	
	@Numeric(type= NumericEnum.NEGATIVE ,message="请输入整数" )
	private int integer;
	
	@Numeric(type=NumericEnum.FLOAT ,message="请输入Float" )
	private float floa;
	
	
	@NotNull(message = "color not null")
	private String color;
	
	@NotNull(message="用户名不能为空")
	private String username;
	
	@RegMatcher(regex = "[0,9]",message="只能输入0,9两个数字")
	private Integer reg;
	
	@RegMatcher(regex = "^[0-1]{1}$",message="flush只能是0和1")
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getReg() {
		return reg;
	}

	public void setReg(Integer reg) {
		this.reg = reg;
	}

	public String getNumeric() {
		return numeric;
	}

	public void setNumeric(String numeric) {
		this.numeric = numeric;
	}

	public int getInteger() {
		return integer;
	}

	public void setInteger(int integer) {
		this.integer = integer;
	}
	
	
	
}
