package com.ali.speech.boot;


import com.fast.dev.acenter.annotation.EnableApplicationClient;
import com.fast.dev.core.boot.ApplicationBootSuper;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.ComponentScan;


@EnableApplicationClient
@ComponentScan("com.ali.speech.core")
public class App extends ApplicationBootSuper {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }
}

