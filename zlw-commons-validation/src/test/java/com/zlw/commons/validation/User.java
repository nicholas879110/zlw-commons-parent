package com.zlw.commons.validation;

import com.zlw.commons.validation.annotation.Chinese;
import com.zlw.commons.validation.annotation.Email;
import com.zlw.commons.validation.annotation.Entity;
import com.zlw.commons.validation.annotation.IDCard;
import com.zlw.commons.validation.annotation.IP;
import com.zlw.commons.validation.annotation.Length;
import com.zlw.commons.validation.annotation.Letter;
import com.zlw.commons.validation.annotation.Max;
import com.zlw.commons.validation.annotation.Min;
import com.zlw.commons.validation.annotation.Mobile;
import com.zlw.commons.validation.annotation.NotEmpty;
import com.zlw.commons.validation.annotation.NotNull;
import com.zlw.commons.validation.annotation.Range;
import com.zlw.commons.validation.annotation.RegMatcher;
import com.zlw.commons.validation.annotation.Tel;
import com.zlw.commons.validation.annotation.Xss;


@Entity
public class User {
	
	@Chinese
	@NotNull(message="用户名不能为空")
	private String username;

	@NotEmpty(message="user密码不允许为空")
	private String password;
	
	@NotEmpty(message="地址不能为空")
	private String addr;
	
	@Email
	private String email;
	
	@Mobile
	private String moble;
	
	@Tel(zipCode=Tel.ZipCode.ALL)
	private String tel;
	
	@Min(value="10" , message = "年龄不能小于10")
	private Integer age;
	
	@Max(value="20" , message = "工作经验不能大于20")
	private float works;
	
	@Range(min="10.5" , max= "85.6" , message="有效成绩范围是10.5-85.6")
	private double score ;
	
	@Xss
	@Length(min=5 , max = 200 , message = "content输入值未在制定范围内（5-200）")
	private String content ;
	
	@IDCard(message = "无效的身份证号码")
	private String idcard ;
	
	@IP(message="IP 不允许空")
	private String ip;
	
	@Length(min=6,max=10)
	@Letter
	private String nickName;
	
	@RegMatcher(regex = "^[a-z]+$",message="只能输入小写字母")
	private String reg;
	
	@RegMatcher(regex = "^[0-1]{1}$",message="flush只能是0和1")
	private Long id;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public String getMoble() {
		return moble;
	}

	public void setMoble(String moble) {
		this.moble = moble;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public float getWorks() {
		return works;
	}

	public void setWorks(float works) {
		this.works = works;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password
				+ ", addr=" + addr + ", email=" + email + ", moble=" + moble
				+ ", tel=" + tel + ", age=" + age + ", works=" + works
				+ ", score=" + score + ", content=" + content + "]";
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getReg() {
		return reg;
	}

	public void setReg(String reg) {
		this.reg = reg;
	}

	
	
}
