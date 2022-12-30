package com.github.dcysteine.nesql.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ShutdownController {
    @Autowired
    private ApplicationContext context;

    @GetMapping("/shutdown")
    public String shutDown() {
        // We need to actually shut down in a separate thread, so that we can proceed with serving
        // the shutdown page.
        new Thread(() -> SpringApplication.exit(context)).start();
        return "shutdown";
    }
}