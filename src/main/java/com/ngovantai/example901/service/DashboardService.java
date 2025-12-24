package com.ngovantai.example901.service;

import java.util.Map;

public interface DashboardService {
    Map<String, Object> getDashboardStats();

    Map<String, Object> getRevenue(String startDate, String endDate);
}