package com.company.keyrate.service;

import com.company.keyrate.entity.KeyRate;

import java.time.LocalDate;

public interface NewService
{
    String NAME = "keyrate_NewService";
    
    KeyRate keyRateOnDay(LocalDate date) throws Exception;
}