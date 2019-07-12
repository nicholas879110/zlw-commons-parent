package com.zlw.excel;

public class JavaBean {

	private String id;
	private String empNo;
	private String account;
	private String company;
	private String phone;
	private String createTime;
	private Integer order;
	private Double dub; 
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	public Double getDub() {
		return dub;
	}
	public void setDub(Double dub) {
		this.dub = dub;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmpNo() {
		return empNo;
	}
	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Integer getOrder() {
		return order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}
	
	@Override
	public String toString() {
		return "JavaBean [id=" + id + ", empNo=" + empNo + ", account=" + account + ", company=" + company + ", phone=" + phone + ", createTime=" + createTime
				+ ", order=" + order + ", dub=" + dub + "]";
	}
	
	
}
