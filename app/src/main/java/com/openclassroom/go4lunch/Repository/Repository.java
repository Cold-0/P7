package com.openclassroom.go4lunch.Repository;

public class Repository {

    // ----------------
    // Singleton
    // ----------------
    static Repository repository;

    public static Repository getRepository() {
        if (repository == null)
            repository = new Repository();
        return repository;
    }

    // ----------------
    // Instance
    // ----------------
    RetrofitService service;

    public RetrofitService getService() {
        return service;
    }

    Repository() {
        service = RetrofitInstance.getRetrofitInstance().create(RetrofitService.class);
    }
}
