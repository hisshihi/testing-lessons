package org.example.extension;

import org.example.service.UserService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestInstancePostProcessor;

import java.lang.reflect.Field;

public class PostProcessingExtension implements TestInstancePostProcessor {

    // Будет приминяться какая-то логика в завимисоти от того, какая аннотация у класса(в данном примере)
    @Override
    public void postProcessTestInstance(Object testInstance, ExtensionContext context) throws Exception {
        System.out.println("post processing test instance");
        Field[] declaredField = testInstance.getClass().getDeclaredFields();
        for (Field field : declaredField) {
            if (field.isAnnotationPresent(Order.class)) {
                field.set(testInstance, new UserService(null));
            }
        }
    }
}
