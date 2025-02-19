package controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

import dao.DAO;

/// ApplicationRunner, runs the SpringBoot application
/// Author(s): Jamie Mizelle
/// Date: 11/13/2024
@SpringBootApplication
@ComponentScan(basePackages = {"dao", "models", "controllers", "service"})
public class ApplicationRunner  extends SpringBootServletInitializer
{
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) 
    {
        return application.sources(ApplicationRunner.class);
    }


    public static void main(String[] args) 
    {
      SpringApplication.run(ApplicationRunner.class, args);
      
    }

}