/*
 * Copyright (c) 2019
 * Carlos Gonçalves (https://www.linkedin.com/in/carlosmogoncalves/)
 * All rights reserved.
 */
package pt.cmg.aeminium.knowledge.persistence.entities.localisation;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * @author Carlos Gonçalves
 */
public enum Language {

    EN_UK("en-UK"),
    PT_PT("pt-PT");

    private final String name;
    private static final Map<String, Language> map = new HashMap<>();

    public final static Language DEFAULT_LANGUAGE = Language.PT_PT;
    public final static String DEFAULT_LANGUAGE_NAME = "pt-PT";

    Language(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    /**
     * Note that Jersey uses this method to instantiate enums from String QueryParams automatically, so do not delete it
     */
    public static Language fromString(String name) {
        if (name == null) {
            return Language.DEFAULT_LANGUAGE;
        }

        return map.getOrDefault(name.toLowerCase(Locale.ROOT), Language.DEFAULT_LANGUAGE);
    }

    /**
     * ... and MicroProfile uses this one so...
     */
    public static Language of(String name) {
        return fromString(name);
    }

    static {
        Language[] values = values();
        for (Language value : values) {
            map.put(value.getName().toLowerCase(Locale.ROOT), value);
        }
    }
}
