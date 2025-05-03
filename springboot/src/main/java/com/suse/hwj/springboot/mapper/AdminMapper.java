package com.suse.hwj.springboot.mapper;

import com.suse.hwj.springboot.entity.Admin;
import org.apache.ibatis.annotations.Select;

public interface AdminMapper {

    @Select("select * from `admin` where account = #{account}")
    Admin selectByAccount(String account);
}
