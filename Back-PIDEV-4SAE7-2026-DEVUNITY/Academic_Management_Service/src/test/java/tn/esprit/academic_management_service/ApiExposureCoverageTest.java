package tn.esprit.academic_management_service;

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

    private static final String BASE_PACKAGE = "tn.esprit.academic_management_service";

    @Test
    void shouldExposeApiMappingsAcrossLearningAndCertificationsModules() {
        List<Endpoint> endpoints = discoverEndpoints(BASE_PACKAGE);
        assertFalse(endpoints.isEmpty(), "No API endpoints discovered");
        assertTrue(endpoints.size() >= 20, "Expected at least 20 endpoints, found " + endpoints.size());

        long crudLikeCount = endpoints.stream()
                .filter(e -> e.path.toLowerCase().matches(".*(add|create|update|delete|all|byid|id|enroll|assign).*")
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
                String[] classPaths = extractPaths(controllerClass.getAnnotation(RequestMapping.class));
                if (classPaths.length == 0) {
                    classPaths = new String[]{""};
                }

                for (Method method : controllerClass.getDeclaredMethods()) {
                    List<MethodMapping> methodMappings = extractMethodMappings(method);
                    for (MethodMapping mapping : methodMappings) {
                        for (String classPath : classPaths) {
                            for (String methodPath : mapping.paths) {
                                endpoints.add(new Endpoint(
                                        controllerClass.getSimpleName(),
                                        mapping.httpMethod,
                                        normalizePath(classPath, methodPath)
                                ));
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

        GetMapping get = method.getAnnotation(GetMapping.class);
        if (get != null) mappings.add(new MethodMapping("GET", pickPaths(get.value(), get.path())));

        PostMapping post = method.getAnnotation(PostMapping.class);
        if (post != null) mappings.add(new MethodMapping("POST", pickPaths(post.value(), post.path())));

        PutMapping put = method.getAnnotation(PutMapping.class);
        if (put != null) mappings.add(new MethodMapping("PUT", pickPaths(put.value(), put.path())));

        DeleteMapping delete = method.getAnnotation(DeleteMapping.class);
        if (delete != null) mappings.add(new MethodMapping("DELETE", pickPaths(delete.value(), delete.path())));

        PatchMapping patch = method.getAnnotation(PatchMapping.class);
        if (patch != null) mappings.add(new MethodMapping("PATCH", pickPaths(patch.value(), patch.path())));

        RequestMapping request = method.getAnnotation(RequestMapping.class);
        if (request != null) {
            RequestMethod[] methods = request.method();
            String[] paths = pickPaths(request.value(), request.path());
            if (methods.length == 0) {
                mappings.add(new MethodMapping("REQUEST", paths));
            } else {
                for (RequestMethod requestMethod : methods) {
                    mappings.add(new MethodMapping(requestMethod.name(), paths));
                }
            }
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
