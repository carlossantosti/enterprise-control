package br.com.carlosti.cep.model;

public interface SimpleCallback<T> {
    void onResponse(T response);
    void onError(String error);
}
