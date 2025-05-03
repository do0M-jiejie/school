package com.suse.hwj.springboot.service;

import com.suse.hwj.springboot.entity.Appointment;
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

    public void cancel(Integer appointmentId){
        appointmentMapper.cancel(appointmentId);
    }

    public void delete(Integer appointmentId){
        appointmentMapper.delete(appointmentId);
    }
}
