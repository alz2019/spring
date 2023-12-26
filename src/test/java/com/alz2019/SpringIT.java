package com.alz2019;

import com.alz2019.context.ApplicationContext;
import com.alz2019.service.BookingService;
import com.alz2019.service.InvoiceService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SpringIT {
    @Test
    void lifecycleTest() {
        ApplicationContext applicationContext = new SpringApplication("com.alz2019.service").run();

        BookingService bookingService = applicationContext.getBean(BookingService.class);
        InvoiceService invoiceService = applicationContext.getBean("invoiceService", InvoiceService.class);

        assertNotNull(bookingService);
        assertNotNull(invoiceService);

        assertEquals(invoiceService, bookingService.getInvoiceService());
    }
}
