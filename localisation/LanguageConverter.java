/*
 * Copyright (c) 2024 Carlos Gonçalves (https://www.linkedin.com/in/carlosmogoncalves/)
 * Likely open-source, so copy at will, bugs will be yours as well.
 */
package pt.cmg.aeminium.knowledge.persistence.entities.localisation;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * @author Carlos Gonçalves
 */
/**
 * Converter used to manipulate the language when has contact with the database
 */
@Converter(autoApply = true)
public class LanguageConverter implements AttributeConverter<Language, String> {

    @Override
    public String convertToDatabaseColumn(Language language) {
        if (language == null) {
            return null;
        }
        return language.getName();
    }

    @Override
    public Language convertToEntityAttribute(String name) {
        return Language.fromString(name);
    }
}