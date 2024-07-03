/*
 * Copyright (c) 2024 Carlos Gonçalves (https://www.linkedin.com/in/carlosmogoncalves/)
 * Likely open-source, so copy at will, bugs will be yours as well.
 */
package pt.cmg.aeminium.knowledge.dao.identity;

import java.util.List;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import org.apache.commons.lang3.StringUtils;
import pt.cmg.aeminium.knowledge.dao.JPACrudDAO;
import pt.cmg.aeminium.knowledge.persistence.entities.identity.User;
import pt.cmg.jakartautils.jpa.QueryUtils;

/**
 * @author Carlos Gonçalves
 */
@Stateless
public class UserDAO extends JPACrudDAO<User> {
    public UserDAO() {
        super(User.class);
    }

    public User findByEmail(String email) {

        if (StringUtils.isBlank(email)) {
            return null;
        }

        TypedQuery<User> query = getEntityManager().createNamedQuery(User.QUERY_FIND_BY_EMAIL, User.class);
        query.setParameter("email", email);

        List<User> resultList = QueryUtils.getResultListFromQuery(query);
        return resultList.isEmpty() ? null : resultList.getFirst();
    }
}
