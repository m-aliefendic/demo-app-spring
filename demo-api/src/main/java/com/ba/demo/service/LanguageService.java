package com.ba.demo.service;


import com.ba.demo.api.model.time.Language;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class LanguageService {

    public List<Language> getAll()  {

        return new ArrayList<Language>(){{
            add(Language.English_US);
        }};

    }

 }