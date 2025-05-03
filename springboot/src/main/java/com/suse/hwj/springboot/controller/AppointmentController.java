package com.suse.hwj.springboot.controller;

import com.github.pagehelper.PageInfo;
import com.suse.hwj.springboot.common.Result;
import com.suse.hwj.springboot.entity.Appointment;
import com.suse.hwj.springboot.entity.Order;
import com.suse.hwj.springboot.service.AppointmentService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Resource
    private AppointmentService appointmentService;

    @PostMapping("/add")
    public Result add(@RequestBody Appointment appointment) {
        appointmentService.add(appointment);
        return Result.success();
    }

    @GetMapping("/userAppointments")
    public Result userAppointments(@RequestParam("userId") String userId) {
        List<Appointment> appointments = appointmentService.userAppointments(userId);
        return Result.success(appointments);
    }

    @GetMapping("/coachAppointments")
    public Result coachAppointments(@RequestParam("coachId") String coachId) {
        List<Appointment> appointments = appointmentService.coachAppointments(coachId);
        return Result.success(appointments);
    }

    @PutMapping("/cancel")
    public Result cancel(@RequestBody Map<String, Integer> request) {
        Integer appointmentId = request.get("appointmentId");
        appointmentService.cancel(appointmentId);
        return Result.success();
    }
    @DeleteMapping("/delete/{appointmentId}")
    public Result delete(@PathVariable("appointmentId") Integer appointmentId) {
        appointmentService.delete(appointmentId);
        return Result.success();
    }


}
