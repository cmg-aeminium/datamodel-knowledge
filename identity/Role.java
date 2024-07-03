/*
 * Copyright (c) 2024 Carlos Gonçalves (https://www.linkedin.com/in/carlosmogoncalves/)
 * Likely open-source, so copy at will, bugs will be yours as well.
 */
package pt.cmg.aeminium.knowledge.persistence.entities.identity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.config.CacheIsolationType;

/**
 * @author Carlos Gonçalves
 */
@Entity
@Cache(type = CacheType.FULL, isolation = CacheIsolationType.SHARED, alwaysRefresh = true, coordinationType = CacheCoordinationType.SEND_NEW_OBJECTS_WITH_CHANGES)
@Table(name = "roles")
@NamedQuery(name = Role.QUERY_FIND_BY_NAMES,
    query = "SELECT r FROM Role r WHERE r.name IN :names")
public class Role implements Serializable {

    private static final long serialVersionUID = -3525005961303072729L;

    public static final String QUERY_FIND_BY_NAMES = "Role.findByNames";

    public enum Name {

        GOD,
        SCHOLAR,
        ANALYST;

        private static final Map<String, Name> map = new HashMap<>();

        static {
            Name[] values = values();
            for (Name value : values) {
                map.put(value.name().toLowerCase(Locale.ROOT), value);
            }
        }

        // fromString is recognised by JAX-RS as an enum @Param converter - DO NOT change this method name
        public static Name fromString(String name) {
            if (name == null) {
                return null;
            }

            return map.get(name.toLowerCase(Locale.ROOT));
        }
    }

    public static final String GOD = Name.GOD.toString();
    public static final String SCHOLAR = Name.SCHOLAR.toString();
    public static final String ANALYST = Name.ANALYST.toString();

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ROLES_SEQUENCE")
    @SequenceGenerator(name = "ROLES_SEQUENCE",
        sequenceName = "roles_id_seq",
        initialValue = 1,
        allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    @Enumerated(EnumType.STRING)
    private Name name;

    @Column(name = "createdat", nullable = false)
    private LocalDateTime createdAt;

    public Role() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Role other = (Role) obj;
        return Objects.equals(id, other.id) && name == other.name;
    }

}
