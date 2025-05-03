package com.suse.hwj.springboot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.suse.hwj.springboot.entity.Appointment;
import com.suse.hwj.springboot.entity.Order;
import com.suse.hwj.springboot.mapper.AppointmentMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentService {

    @Resource
    private AppointmentMapper appointmentMapper;

    public void add(Appointment appointment){
        appointmentMapper.add(appointment);
    }

    public List<Appointment> userAppointments(String userId){
        return appointmentMapper.userAppointments(userId);
    }

    public List<Appointment> coachAppointments(String coachId) {
        return appointmentMapper.coachAppointments(coachId);

    }

    public void cancel(Integer appointmentId){
        appointmentMapper.cancel(appointmentId);
    }

    public void delete(Integer appointmentId){
        appointmentMapper.delete(appointmentId);
    }

    public void approve(Appointment appointment) {
        appointmentMapper.approve(appointment);
    }

    public void reject(Appointment appointment) {
        appointmentMapper.reject(appointment);
    }
}
