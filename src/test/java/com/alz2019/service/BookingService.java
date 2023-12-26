package com.alz2019.service;

import com.alz2019.annotation.Autowired;
import com.alz2019.annotation.Component;
import lombok.Getter;

@Component
@Getter
public class BookingService {
    @Autowired
    private InvoiceService invoiceService;
}
