package com.abahrami.template;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

@SpringBootApplication
@RestController
@RequestMapping("/demo")
public class TemplateApplication {

    @GetMapping("/test")
    public Map<String, String> test() throws Exception {
        return Map.of(
                "Host Address", InetAddress.getLocalHost().getHostAddress(),
                "Host Name", InetAddress.getLocalHost().getHostName(),
                "time", Calendar.getInstance().toInstant().toString()
        );
    }

    @GetMapping("/addLoad/{min}")
    public String addLoad(@PathVariable("min") int min) {
        long start = System.currentTimeMillis();
        ExecutorService es = Executors.newFixedThreadPool(100);
        IntStream.rangeClosed(0, 100)
                .forEach(x -> es.submit(() -> {
                    for (; ; ) {
                        if (System.currentTimeMillis() - start > min * 60 * 1000) break;
                    }
                    return 0;
                }));

        return "Load started " + Calendar.getInstance().toInstant().toString();
    }

    public static void main(final String[] args) {
        SpringApplication.run(TemplateApplication.class, args);
    }
}
