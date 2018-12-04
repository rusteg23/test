package com.example.myapplication;

public interface BaseView<T extends BasePresenter> {

    void setPresenter(T presenter);

}
