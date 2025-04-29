package com.suse.hwj.springboot.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.suse.hwj.springboot.entity.CookBook;
import com.suse.hwj.springboot.mapper.CookBookMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CookBookService {

    @Resource
    private CookBookMapper cookBookMapper;

    public List<CookBook> selectAll(CookBook cookBook) {
        return cookBookMapper.selectAll(cookBook);
    }

    public PageInfo<CookBook> selectPage(CookBook cookBook, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<CookBook> list = cookBookMapper.selectAll(cookBook);
        return PageInfo.of(list);
    }
}
