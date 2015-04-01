package com.gatech.objectsanddesign.shoppingwithfriends;

interface Consumer<T> {
    void preconsume();
    void consume(T t);
}
