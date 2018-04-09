package com.business.master.service.Impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.business.master.dao.SystemLogMapper;
import com.business.master.model.SystemLog;
import com.business.master.service.SystemLogService;


@Service("systemLogService")
public class SystemLogServiceImpl implements SystemLogService {

    @Autowired
    private SystemLogMapper systemLogMapper;
    
    @Override
    public int deleteSystemLog(String id) {
        
        return systemLogMapper.deleteByPrimaryKey(id);
    }

    @Override
    
    public int insert(SystemLog record) {
        
        return systemLogMapper.insertSelective(record);
    }

    @Override
    public SystemLog selectSystemLog(String id) {
        
        return systemLogMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateSystemLog(SystemLog record) {
        
        return systemLogMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int insertTest(SystemLog record) {
        
        return systemLogMapper.insert(record);
    }

}