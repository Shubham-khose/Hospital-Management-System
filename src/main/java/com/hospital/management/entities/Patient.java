package com.hospital.management.entities;

import java.io.ObjectInputFilter.Status;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@Entity
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int pId;
    @NotBlank(message="Name field is required!!")
	@Size(min=2, max=20,message="min 2 and max 20 characters are allowed")
    private String name;
    private String dob;
    private String gender;
    private String bloodGroup;
    @Column(unique = true, nullable = false)
    private String emailId;
    private String address;
    private long mobileNo;
    @Temporal(TemporalType.DATE)
    private Date admittedDate;

    @Temporal(TemporalType.DATE)
    private Date dischargedDate;

    @Enumerated(EnumType.STRING) // ✅ Stores as 'ACTIVE' instead of index (0,1)
    @Column(length = 20) 
    private Status status;

    private String password;
    
    @OneToMany(mappedBy = "patient", cascade = CascadeType.ALL)
    private List<Appointment> appointments;

    @OneToOne(mappedBy = "patient", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Prescription prescriptions;  // One patient -> Many prescriptions
	/* fot iterating table heading to webpage we create this list */
	/*---------------------------------------------------------*/
    
	

	
	public Patient() {
		super();
		// TODO Auto-generated constructor stub
	}
	

	public Patient(int pId,
			@NotBlank(message = "Name field is required!!") @Size(min = 2, max = 20, message = "min 2 and max 20 characters are allowed") String name,
			String dob, String gender, String bloodGroup, String emailId, String address, long mobileNo,
			Date admittedDate, Date dischargedDate, Status status, String password, List<Appointment> appointments,
			Prescription prescriptions) {
		super();
		this.pId = pId;
		this.name = name;
		this.dob = dob;
		this.gender = gender;
		this.bloodGroup = bloodGroup;
		this.emailId = emailId;
		this.address = address;
		this.mobileNo = mobileNo;
		this.admittedDate = admittedDate;
		this.dischargedDate = dischargedDate;
		this.status = status;
		this.password = password;
		this.appointments = appointments;
		this.prescriptions = prescriptions;
	}



	public Date getAdmittedDate() {
		return admittedDate;
	}



	public void setAdmittedDate(Date admittedDate) {
		this.admittedDate = admittedDate;
	}



	public Date getDischargedDate() {
		return dischargedDate;
	}



	public void setDischargedDate(Date dischargedDate) {
		this.dischargedDate = dischargedDate;
	}



	public Status getStatus() {
		return status;
	}



	public void setStatus(Status status) {
		this.status = status;
	}



	public Prescription getPrescriptions() {
		return prescriptions;
	}



	public void setPrescriptions(Prescription prescriptions) {
		this.prescriptions = prescriptions;
	}



	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public int getpId() {
		return pId;
	}

	public void setpId(int pId) {
		this.pId = pId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getMobileNo() {
		return mobileNo;
	}

	public void setMobileNo(long mobileNo) {
		this.mobileNo = mobileNo;
	}

	public List<Appointment> getAppointments() {
		return appointments;
	}

	public void setAppointments(List<Appointment> appointments) {
		this.appointments = appointments;
	}

	@Override
	public String toString() {
		return "Patient [pId=" + pId + ", name=" + name + ", dob=" + dob + ", gender=" + gender + ", bloodGroup="
				+ bloodGroup + ", emailId=" + emailId + ", address=" + address + ", mobileNo=" + mobileNo
				+ ", admittedDate=" + admittedDate + ", dischargedDate=" + dischargedDate + ", status=" + status
				+ ", password=" + password + ", appointments=" + appointments + ", prescriptions=" + prescriptions
				+ "]";
	}

	public Patient orElse(Object object) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public enum Status {
	    ACTIVE,  // ✅ Defined correctly
	    DISCHARGED;
	}


 
}