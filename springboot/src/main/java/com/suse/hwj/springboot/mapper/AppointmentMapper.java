package com.suse.hwj.springboot.mapper;


import com.suse.hwj.springboot.entity.Appointment;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface AppointmentMapper {

    void add(Appointment appointment);

    @Select("select * from appointment where user_id = #{userId}")
    List<Appointment> userAppointments(String userId);

    @Update("update appointment set status = '已取消' where appointment_id = #{appointmentId}")
    void cancel(@Param("appointmentId")Integer appointmentId);

    @Delete("delete from appointment where appointment_id = #{appointmentId}")
    void delete(@Param("appointmentId")Integer appointmentId);
}
