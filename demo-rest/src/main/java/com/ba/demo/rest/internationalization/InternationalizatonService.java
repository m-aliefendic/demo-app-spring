package com.ba.demo.rest.internationalization;


import com.ba.demo.api.model.time.Language;
import com.ba.demo.service.LanguageService;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class InternationalizatonService {

    private final MessageSource messages;
    private final LanguageService languageService;


//    public String get(String key, Object... tokens){
//        return messages.getMessage(key,tokens, LocaleContextHolder.getLocale());
//    }

    //Get language by token; when token/language is not found, get by LocaleContextHolder - lang param
    public String get(String key, Object... tokens){
        try {
            return messages.getMessage(key, tokens, LocaleContextHolder.getLocale());
        }
        catch (Exception ex) {
            return messages.getMessage(key, tokens, LocaleContextHolder.getLocale());

        }

    }

    public String getByLanguageId(Integer langId, String key, Object... tokens){
        List<Language> lang = languageService.getAll().stream().filter(x ->x.getId() == langId).collect(Collectors.toList());
        if(lang != null && lang.size() > 0){
            return messages.getMessage(key,tokens, new Locale(lang.get(0).getCode()));
        }
        return messages.getMessage(key,tokens, Locale.US);
    }

    public String getEnglish(String key, Object... tokens){
        return messages.getMessage(key,tokens, Locale.ENGLISH);
    }

}

