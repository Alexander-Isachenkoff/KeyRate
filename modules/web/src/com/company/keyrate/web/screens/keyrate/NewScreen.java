package com.company.keyrate.web.screens.keyrate;

import com.company.keyrate.entity.KeyRate;
import com.company.keyrate.service.NewService;
import com.haulmont.cuba.core.global.DataManager;
import com.haulmont.cuba.gui.components.HasValue;
import com.haulmont.cuba.gui.model.InstanceContainer;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@UiController("keyrate_")
@UiDescriptor("new-screen.xml")
public class NewScreen extends Screen
{
    private static final Logger log = LoggerFactory.getLogger(NewScreen.class);
    @Inject
    private InstanceContainer<KeyRate> keyRateDc;
    @Inject
    private NewService newService;
    
    @Subscribe("datePicker")
    public void onDatePickerValueChange(HasValue.ValueChangeEvent<Date> event) {
        LocalDate date = event.getValue().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        KeyRate keyRate;
        try {
            keyRate = newService.keyRateOnDay(date);
        } catch (Exception e) {
            log.error("Error", e);
            return;
        }
        keyRateDc.setItem(keyRate);
    }
}

