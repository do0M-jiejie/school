package com.suse.hwj.springboot.mapper;

import com.suse.hwj.springboot.entity.CookBook;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface CookBookMapper {

    List<CookBook> selectAll(@Param("dto") CookBook cookBook);
}
