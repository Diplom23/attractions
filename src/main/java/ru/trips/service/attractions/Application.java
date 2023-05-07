package ru.trips.service.attractions;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Главный класс.
 */
@SpringBootApplication
@ComponentScan(basePackages = "ru.trips")
public class Application {

    /**
     * Точка входа в приложение.
     *
     * @param args аргументы
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
