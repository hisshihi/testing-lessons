package org.example;

import org.junit.platform.engine.discovery.DiscoverySelectors;
import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.TagFilter;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;

import java.io.PrintWriter;

// Собственный класс для запуска тестов
public class TestLauncher {

    public static void main(String[] args) {
        // Создания экземпляря launcher для запуска тестов
        Launcher launcher = LauncherFactory.create();
        // Создание слушателя для генерации сводки результатов тестирования
        SummaryGeneratingListener summaryGeneratingListener = new SummaryGeneratingListener();
        // Создание запросов на обнаружение тестов
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder
                .request()
                // Поиск всех тестов в указанном объекте
//                .selectors(DiscoverySelectors.selectClass(UserServiceTest.class))
                // Поиск всех тестов в указанном пакете
                .selectors(DiscoverySelectors.selectPackage("org.example.service"))
                .filters(
                        // Запуск тестов помечанные тегом login
                        TagFilter.includeTags("login")
                        // Запуск тестов не помечанных тегом login
//                        TagFilter.excludeTags("login")
                )
                .build();
        // Выполнение тестов с использованием заданного запроса и слушателя
        launcher.execute(request, summaryGeneratingListener);
        // Печать результатов в консоль
        try (PrintWriter writer = new PrintWriter(System.out)) {
            summaryGeneratingListener.getSummary().printTo(writer);
        }
    }

}
