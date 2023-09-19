package com.ba.demo.api.model.time;

import lombok.Getter;

@Getter
public class Language {
    public static final Language English = new Language(1, "English (Standard)", "en");
    public static final Language English_US = new Language(1, "English", "en_us");
    public static final Language Italian = new Language(5, "Italian", "it");
    public static final Language German = new Language(6, "German", "de");


    private final Integer id;
    private final String name;
    private final String code;

    public Language(Integer id, String name, String code){
        this.id = id;
        this.name = name;
        this.code = code;
    }
}
