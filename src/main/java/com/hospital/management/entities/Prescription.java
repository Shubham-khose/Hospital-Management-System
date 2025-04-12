package com.hospital.management.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "prescriptions")
public class Prescription {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long preId;  // Assuming each patient has one prescription (can be modified )
	
//	@OneToOne(mappedBy = "prescription")
//	@JoinColumn(name="dId")
//	private Doctor doctor;
	@OneToOne
	@JoinColumn(name = "dId")
	private Doctor doctor;

	
	@OneToOne
	@JoinColumn(name="pId")
	private Patient patient;
	
	private String treatment;
	private String medicine;
	private String remark;
	private String advice;
	public Prescription() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Prescription(long preId, Doctor doctor, Patient patient, String treatment, String medicine, String remark,
			String advice) {
		super();
		this.preId = preId;
		this.doctor = doctor;
		this.patient = patient;
		this.treatment = treatment;
		this.medicine = medicine;
		this.remark = remark;
		this.advice = advice;
	}
	
	public long getPreId() {
		return preId;
	}

	public void setPreId(long preId) {
		this.preId = preId;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}

	
	public Doctor getDoctor() {
		return doctor;
	}
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}
	public Patient getPatient() {
		return patient;
	}
	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	public String getMedicine() {
		return medicine;
	}
	public void setMedicine(String medicine) {
		this.medicine = medicine;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getAdvice() {
		return advice;
	}
	public void setAdvice(String advice) {
		this.advice = advice;
	}

	@Override
	public String toString() {
		return "Prescription [preId=" + preId + ", patientId=" + (patient != null ? patient.getpId() : "null") +  // Only print patient ID
		           ", doctorId=" + (doctor != null ? doctor.getdId() : "null")  + ", treatment="
				+ treatment + ", medicine=" + medicine + ", remark=" + remark + ", advice=" + advice + "]";
	}
	
	
	
}