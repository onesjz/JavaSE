package com.oneso.TestClass;

public class ServiceForTests {

    private final RepositoryForTests repository;

    public ServiceForTests(RepositoryForTests repository) {
        this.repository = repository;
    }

    public String getHello() {
        return "Hello";
    }

    public int increment(int value) {
        return repository.addOne(value);
    }

    public String addNewData(String text) {
        repository.addNewData(text);
        return text;
    }
}
