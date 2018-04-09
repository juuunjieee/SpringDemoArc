package com.business.master.service;

import com.business.master.model.SystemLog;


public interface SystemLogService {

    int deleteSystemLog(String id);

    int insert(SystemLog record);
    
    int insertTest(SystemLog record);

    SystemLog selectSystemLog(String id);
    
    int updateSystemLog(SystemLog record);
}