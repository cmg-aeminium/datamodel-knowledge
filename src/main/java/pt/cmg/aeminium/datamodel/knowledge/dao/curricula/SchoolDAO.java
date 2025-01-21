/*
 * Copyright (c) 2024 Carlos Gonçalves (https://www.linkedin.com/in/carlosmogoncalves/)
 * Likely open-source, so copy at will, bugs will be yours as well.
 */
package pt.cmg.aeminium.datamodel.knowledge.dao.curricula;

import java.util.List;
import java.util.Set;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import pt.cmg.aeminium.datamodel.common.entities.localisation.Language;
import pt.cmg.aeminium.datamodel.knowledge.dao.JPACrudDAO;
import pt.cmg.aeminium.datamodel.knowledge.entities.curricula.School;
import pt.cmg.jakartautils.jpa.QueryUtils;

/**
 * @author Carlos Gonçalves
 */
@Stateless
public class SchoolDAO extends JPACrudDAO<School> {

    public SchoolDAO() {
        super(School.class);
    }

    public record SchoolFilter(Set<Long> countryIds, Long size, Long offset) {
        public boolean hasActiveFilter() {
            return countryIds != null && !countryIds.isEmpty();
        }
    };

    public School findByName(Language language, String name) {

        if (language == null) {
            language = Language.DEFAULT_LANGUAGE;
        }

        TypedQuery<School> query = null;
        if (language == Language.DEFAULT_LANGUAGE) {
            query = getEntityManager().createNamedQuery(School.QUERY_FIND_BY_NAME, School.class);
            query.setParameter("name", name);
        } else {
            query = getEntityManager().createNamedQuery(School.QUERY_FIND_BY_TRANSLATED_NAME, School.class);
            query.setParameter("name", name);
            query.setParameter("language", language);
        }

        List<School> results = QueryUtils.getResultListFromQuery(query);
        return results.isEmpty() ? null : results.getFirst();
    }

    public List<School> findByFiltered(SchoolFilter schoolFilter) {

        StringBuilder select = new StringBuilder("SELECT s FROM School s ");
        StringBuilder filter = new StringBuilder("");

        if (!schoolFilter.countryIds().isEmpty()) {
            filter.append("WHERE ");
            filter.append("s.country.id IN :countryIds ");
        }

        String statement = select.append(filter).toString();
        TypedQuery<School> query = getEntityManager().createQuery(statement, School.class);

        if (!schoolFilter.countryIds().isEmpty()) {
            query.setParameter("countryIds", schoolFilter.countryIds());
        }

        if (schoolFilter.size() != null) {
            query.setMaxResults(schoolFilter.size().intValue());
        }

        if (schoolFilter.offset() != null) {
            query.setFirstResult(schoolFilter.offset().intValue());
        }

        return QueryUtils.getResultListFromQuery(query);
    }

    public int countFiltered(SchoolFilter schoolFilter) {

        StringBuilder select = new StringBuilder("SELECT COUNT(s) FROM School s ");
        StringBuilder filter = new StringBuilder("");

        if (!schoolFilter.countryIds().isEmpty()) {
            filter.append("WHERE ");
            filter.append("s.country.id IN :countryIds ");
        }

        String statement = select.append(filter).toString();
        TypedQuery<Integer> query = getEntityManager().createQuery(statement, Integer.class);

        if (!schoolFilter.countryIds().isEmpty()) {
            query.setParameter("countryIds", schoolFilter.countryIds());
        }

        return QueryUtils.getIntResultFromQuery(query);
    }

}
