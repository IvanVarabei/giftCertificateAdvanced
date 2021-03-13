package com.epam.esm.config;

import com.epam.esm.GiftCertificateApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * The ServletInitializer is necessary for creating a war file
 */
public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(GiftCertificateApplication.class);
    }
}
