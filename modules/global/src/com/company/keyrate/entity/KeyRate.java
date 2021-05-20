package com.company.keyrate.entity;

import com.haulmont.chile.core.annotations.MetaClass;
import com.haulmont.chile.core.annotations.MetaProperty;
import com.haulmont.chile.core.annotations.NamePattern;
import com.haulmont.cuba.core.entity.BaseUuidEntity;

import java.time.LocalDate;

@MetaClass(name = "keyrate_KeyRate")
@NamePattern("%s %s|date,rateValue")
public class KeyRate extends BaseUuidEntity
{
    private static final long serialVersionUID = -7027469963544915537L;
    
    @MetaProperty
    private LocalDate date;
    
    @MetaProperty
    private Double rateValue;
    
    public void setDate(LocalDate date) { this.date = date; }
    
    public LocalDate getDate() { return date; }
    
    public void setRateValue(Double rateValue) { this.rateValue = rateValue; }
    
    public Double getRateValue() {
        return rateValue;
    }
    
}