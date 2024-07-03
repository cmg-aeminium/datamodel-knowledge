/*
 * Copyright (c) 2024 Carlos Gonçalves (https://www.linkedin.com/in/carlosmogoncalves/)
 * Likely open-source, so copy at will, bugs will be yours as well.
 */
package pt.cmg.aeminium.knowledge.dao.identity;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import pt.cmg.aeminium.knowledge.dao.JPACrudDAO;
import pt.cmg.aeminium.knowledge.persistence.entities.identity.Role;
import pt.cmg.jakartautils.jpa.QueryUtils;

/**
 * @author Carlos Gonçalves
 */
@Stateless
public class RoleDAO extends JPACrudDAO<Role> {

    public RoleDAO() {
        super(Role.class);
    }

    public List<Role> findByNames(Collection<Role.Name> names) {

        if (names == null || names.isEmpty()) {
            return Collections.emptyList();
        }

        TypedQuery<Role> query = getEntityManager().createNamedQuery(Role.QUERY_FIND_BY_NAMES, Role.class);
        query.setParameter("names", names);

        return QueryUtils.getResultListFromQuery(query);
    }
}
