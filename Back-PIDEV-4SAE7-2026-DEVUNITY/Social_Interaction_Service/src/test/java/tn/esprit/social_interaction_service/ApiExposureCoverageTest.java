package tn.esprit.social_interaction_service;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestFactory;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ApiExposureCoverageTest {

    private static final String BASE_PACKAGE = "tn.esprit.social_interaction_service";

    @Test
    void shouldExposeApiMappingsAcrossCommunicationsAndReportingModules() {
        List<Endpoint> endpoints = discoverEndpoints(BASE_PACKAGE);
        assertFalse(endpoints.isEmpty(), "No API endpoints discovered");
        assertTrue(endpoints.size() >= 15, "Expected at least 15 endpoints, found " + endpoints.size());

        long crudLikeCount = endpoints.stream()
                .filter(e -> e.path.toLowerCase().matches(".*(add|create|update|delete|all|id|message|topic|report|notification|kpi|search).*")
                        || e.httpMethod.equals("DELETE")
                        || e.httpMethod.equals("PUT")
                        || e.httpMethod.equals("POST"))
                .count();

        assertTrue(crudLikeCount > 0, "No CRUD-like API endpoints detected");
    }

    @TestFactory
    List<DynamicTest> shouldHaveValidHttpMethodAndPathForEveryExposedApi() {
        List<Endpoint> endpoints = discoverEndpoints(BASE_PACKAGE);
        List<DynamicTest> tests = new ArrayList<>();
        for (Endpoint endpoint : endpoints) {
            tests.add(DynamicTest.dynamicTest(endpoint.controller + " " + endpoint.httpMethod + " " + endpoint.path, () -> {
                assertNotNull(endpoint.httpMethod);
                assertFalse(endpoint.httpMethod.isBlank());
                assertNotNull(endpoint.path);
                assertFalse(endpoint.path.isBlank());
            }));
        }
        return tests;
    }

    private List<Endpoint> discoverEndpoints(String basePackage) {
        List<Endpoint> endpoints = new ArrayList<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(RestController.class));
        scanner.addIncludeFilter(new AnnotationTypeFilter(Controller.class));

        scanner.findCandidateComponents(basePackage).forEach(bean -> {
            try {
                Class<?> controllerClass = Class.forName(bean.getBeanClassName());
                if (!controllerClass.getName().contains("Controllers") && !controllerClass.getName().contains("controllers")) {
                    return;
                }
                String[] classPaths = extractPaths(controllerClass.getAnnotation(RequestMapping.class));
                if (classPaths.length == 0) classPaths = new String[]{""};

                for (Method method : controllerClass.getDeclaredMethods()) {
                    List<MethodMapping> methodMappings = extractMethodMappings(method);
                    for (MethodMapping mapping : methodMappings) {
                        for (String classPath : classPaths) {
                            for (String methodPath : mapping.paths) {
                                endpoints.add(new Endpoint(controllerClass.getSimpleName(), mapping.httpMethod, normalizePath(classPath, methodPath)));
                            }
                        }
                    }
                }
            } catch (ClassNotFoundException e) {
                fail("Unable to load controller class: " + bean.getBeanClassName());
            }
        });

        return endpoints;
    }

    private List<MethodMapping> extractMethodMappings(Method method) {
        List<MethodMapping> mappings = new ArrayList<>();
        if (method.isAnnotationPresent(GetMapping.class)) mappings.add(new MethodMapping("GET", pickPaths(method.getAnnotation(GetMapping.class).value(), method.getAnnotation(GetMapping.class).path())));
        if (method.isAnnotationPresent(PostMapping.class)) mappings.add(new MethodMapping("POST", pickPaths(method.getAnnotation(PostMapping.class).value(), method.getAnnotation(PostMapping.class).path())));
        if (method.isAnnotationPresent(PutMapping.class)) mappings.add(new MethodMapping("PUT", pickPaths(method.getAnnotation(PutMapping.class).value(), method.getAnnotation(PutMapping.class).path())));
        if (method.isAnnotationPresent(DeleteMapping.class)) mappings.add(new MethodMapping("DELETE", pickPaths(method.getAnnotation(DeleteMapping.class).value(), method.getAnnotation(DeleteMapping.class).path())));
        if (method.isAnnotationPresent(PatchMapping.class)) mappings.add(new MethodMapping("PATCH", pickPaths(method.getAnnotation(PatchMapping.class).value(), method.getAnnotation(PatchMapping.class).path())));
        if (method.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping rm = method.getAnnotation(RequestMapping.class);
            RequestMethod[] methods = rm.method();
            String[] paths = pickPaths(rm.value(), rm.path());
            if (methods.length == 0) mappings.add(new MethodMapping("REQUEST", paths));
            for (RequestMethod requestMethod : methods) mappings.add(new MethodMapping(requestMethod.name(), paths));
        }
        return mappings;
    }

    private String[] extractPaths(RequestMapping requestMapping) {
        if (requestMapping == null) return new String[]{""};
        return pickPaths(requestMapping.value(), requestMapping.path());
    }

    private String[] pickPaths(String[] values, String[] paths) {
        if (paths != null && paths.length > 0) return paths;
        if (values != null && values.length > 0) return values;
        return new String[]{""};
    }

    private String normalizePath(String classPath, String methodPath) {
        String joined = ("/" + classPath + "/" + methodPath).replaceAll("//+", "/");
        return joined.endsWith("/") && joined.length() > 1 ? joined.substring(0, joined.length() - 1) : joined;
    }

    private record Endpoint(String controller, String httpMethod, String path) {}

    private record MethodMapping(String httpMethod, String[] paths) {}
}
