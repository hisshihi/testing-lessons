package org.example.paramresolver;

import org.example.service.UserService;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;

public class UserServiceParamResolver implements ParameterResolver {

    // Метод проверяет, можем ли мы заинжектить объект
    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType() == UserService.class;
    }

    // Создаёт новый объект
    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        // Кеширование объектов, чтобы каждый раз их не создавать
        ExtensionContext.Store store = extensionContext.getStore(ExtensionContext.Namespace.create(extensionContext.getTestMethod()));
        return store.getOrComputeIfAbsent(extensionContext.getTestMethod(), it  -> new UserService());
    }
}
