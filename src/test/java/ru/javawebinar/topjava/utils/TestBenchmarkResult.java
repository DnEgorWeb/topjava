package ru.javawebinar.topjava.utils;

public class TestBenchmarkResult {
    private final String methodName;
    private final long executionTime;

    public TestBenchmarkResult(String methodName, long executionTime) {
        this.methodName = methodName;
        this.executionTime = executionTime;
    }

    public String getMethodName() {
        return methodName;
    }

    public long getExecutionTime() {
        return executionTime;
    }
}
