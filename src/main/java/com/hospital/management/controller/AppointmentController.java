package com.hospital.management.controller;

import java.util.Date;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hospital.management.entities.Appointment;
import com.hospital.management.entities.Appointment.AppointmentStatus;
import com.hospital.management.entities.Patient;
import com.hospital.management.entities.Patient.Status;
import com.hospital.management.repository.AppointmentRepository;
import com.hospital.management.services.AppointmentService;

@Controller
@RequestMapping("/appointment")
public class AppointmentController {

	@Autowired
	private AppointmentRepository appointmentRepository;

	@Autowired
	private AppointmentService appointmentService;

	@PostMapping("/appointmentstatus-cancel")
	public ResponseEntity<?> cancelAppointment(@RequestBody Map<String, Long> request) {
		long appointmentId = request.get("appointmentId");

		Optional<Appointment> optionalAppointment = appointmentRepository.findById(appointmentId);
		System.out.println("appointment-->"+optionalAppointment);
		if (optionalAppointment.isPresent()) {
			AppointmentStatus appointmentStatus = AppointmentStatus.CANCELLED;

			appointmentService.cancelAppointment(appointmentId, appointmentStatus);

			return ResponseEntity.ok(Map.of("success", true));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("success", false));
		}

	}
}
