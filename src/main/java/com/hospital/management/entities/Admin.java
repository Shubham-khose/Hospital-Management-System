package com.hospital.management.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "admins")
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int aId;
	private String name;
	private String gender;
	private String dob;
	private String address;
	private String emailId;
	private long mobileNo;
	private String password;

	public Admin(int aId, String name, String gender, String dob, String address, String emailId, long mobileNo,
			String password) {
		super();
		this.aId = aId;
		this.name = name;
		this.gender = gender;
		this.dob = dob;
		this.address = address;
		this.emailId = emailId;
		this.mobileNo = mobileNo;
		this.password = password;
	}

	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getaId() {
		return aId;
	}

	public void setaId(int aId) {
		this.aId = aId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	@Override
	public String toString() {
		return "Admin [aId=" + aId + ", name=" + name + ", gender=" + gender + ", dob=" + dob + ", address=" + address
				+ ", emailId=" + emailId + ", mobileNo=" + mobileNo + "]";
	}

}
