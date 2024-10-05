package org.example.extension;

import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class GlobalExtension implements BeforeAllCallback, AfterAllCallback {

    // Будет приминяться ко всем тестам до их выполнения
    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        System.out.println("Before All Callback");
    }

    // Будет приминяться ко всем тестам после их выполнения
    @Override
    public void afterAll(ExtensionContext context) throws Exception {
        System.out.println("After All Callback");
    }
}
