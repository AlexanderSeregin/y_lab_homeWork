package website.ylab.learningplatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import website.ylab.learningplatform.starter.audit.annotation.EnableAudit;

@SpringBootApplication
@ComponentScan(basePackages = "website.ylab.learningplatform")
@EnableAspectJAutoProxy
@EnableAudit
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        System.out.println("Personal Finance Tracker application started");
    }
}