package com.company.keyrate.service;

import com.company.keyrate.entity.KeyRate;
import com.haulmont.cuba.core.global.DataManager;
import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import org.springframework.stereotype.Service;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.cbr.web.DailyInfo;
import ru.cbr.web.DailyInfoSoap;
import ru.cbr.web.KeyRateXMLResponse;

import javax.inject.Inject;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

@Service(NewService.NAME)
public class NewServiceBean implements NewService
{
    @Inject
    protected DataManager dataManager;
    
    @Override
    public KeyRate keyRateOnDay(LocalDate date) throws DatatypeConfigurationException {
        DailyInfo service = new DailyInfo();
        DailyInfoSoap port = service.getDailyInfoSoap();
        List<KeyRate> keyRates;
        int daysAgo = 0;
        do {
            LocalDate date1 = date.minusDays(daysAgo);
            LocalDate date2 = date1.plusDays(1);
            GregorianCalendar calendar1 = new GregorianCalendar(date1.getYear(),
                    date1.getMonth().getValue()-1, date1.getDayOfMonth());
            GregorianCalendar calendar2 = new GregorianCalendar(date2.getYear(),
                    date2.getMonth().getValue()-1, date2.getDayOfMonth());
            DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
            XMLGregorianCalendar xmlCalendar1 = datatypeFactory.newXMLGregorianCalendar(calendar1);
            XMLGregorianCalendar xmlCalendar2 = datatypeFactory.newXMLGregorianCalendar(calendar2);
            KeyRateXMLResponse.KeyRateXMLResult result = port.keyRateXML(xmlCalendar1, xmlCalendar2);
            keyRates = parse(result);
            daysAgo++;
        } while (keyRates.isEmpty());
    
        return keyRates.get(0);
    }
    
    private List<KeyRate> parse(KeyRateXMLResponse.KeyRateXMLResult result) {
        List<KeyRate> keyRates = new ArrayList<>();
        List<Object> list = result.getContent();
        ElementNSImpl e = (ElementNSImpl) list.get(0);
        NodeList nodes = e.getChildNodes();
        
        for (int i = 0; i < nodes.getLength(); i++) {
            Node node = nodes.item(i);
            String date = node.getFirstChild().getTextContent();
            String rate = node.getLastChild().getTextContent();
            LocalDate dateTime = LocalDate.parse(date, DateTimeFormatter.ISO_ZONED_DATE_TIME);
            double rateValue = Double.parseDouble(rate);
            
            KeyRate keyRate = dataManager.create(KeyRate.class);
            keyRate.setDate(dateTime);
            keyRate.setRateValue(rateValue);
            keyRates.add(keyRate);
        }
        
        return keyRates;
    }
}