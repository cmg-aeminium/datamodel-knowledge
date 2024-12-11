/**
 * Copyright (c) 2020 Carlos Gonçalves (https://www.linkedin.com/in/carlosmogoncalves/)
 * Likely open-source, so copy at will, bugs will be yours as well.
 */
package pt.cmg.aeminium.datamodel.knowledge.dao.curricula;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import pt.cmg.aeminium.datamodel.common.entities.localisation.Language;
import pt.cmg.aeminium.datamodel.knowledge.dao.JPACrudDAO;
import pt.cmg.aeminium.datamodel.knowledge.entities.curricula.Course;
import pt.cmg.jakartautils.jpa.QueryUtils;

/**
 * @author Carlos Gonçalves
 */
@Stateless
public class CourseDAO extends JPACrudDAO<Course> {

    private static final String BASE_SELECT_COURSE_QUERY = "SELECT c FROM Course c ";

    public static final String AND = "AND ";

    public CourseDAO() {
        super(Course.class);
    }

    public record CourseFilterCriteria(
        Long schoolId,
        Integer year,
        Language language,
        String name,
        String acronym,
        Long size,
        Long offset) {
    }

    /**
     * Searches for Degrees with some filtering criteria
     */
    public List<Course> findFiltered(CourseFilterCriteria filter) {

        StringBuilder selectText = new StringBuilder(BASE_SELECT_COURSE_QUERY);
        StringBuilder filterText = new StringBuilder();
        String prefix = "WHERE ";

        if (filter.schoolId != null) {
            filterText.append(prefix).append("c.school.id = :school ");
            prefix = AND;
        }

        if (filter.year != null) {
            filterText.append(prefix).append("c.year = :year ");
            prefix = AND;
        }

        if (StringUtils.isNotBlank(filter.acronym)) {
            filterText.append(prefix).append("c.acronym = :acronym ");
            prefix = AND;
        }

        if (StringUtils.isNotBlank(filter.name)) {
            if (filter.language == null || filter.language.isDefaultLanguage()) {
                filterText.append(prefix).append("c.nameTextContentId IN (SELECT t.id FROM TextContent t WHERE t.textValue = :name) ");
            } else {
                filterText.append(prefix).append("c.nameTextContentId IN (SELECT t.id FROM TranslatedText t WHERE t.textValue = :name AND t.language = :language) ");
            }

            prefix = AND;
        }

        String queryText = selectText.append(filterText).toString();
        TypedQuery<Course> query = getEntityManager().createQuery(queryText, Course.class);

        setDegreeQueryParameters(query, filter);

        if (filter.size() != null) {
            query.setMaxResults(filter.size().intValue());
        }

        if (filter.offset() != null) {
            query.setFirstResult(filter.offset().intValue());
        }

        return query.getResultList();
    }

    private void setDegreeQueryParameters(TypedQuery<Course> query, CourseFilterCriteria filter) {

        if (filter.schoolId != null) {
            query.setParameter("school", filter.schoolId);
        }

        if (filter.year != null) {
            query.setParameter("year", filter.year);
        }

        if (StringUtils.isNotBlank(filter.acronym)) {
            query.setParameter("acronym", filter.acronym);
        }

        if (StringUtils.isNotBlank(filter.name)) {
            query.setParameter("name", filter.name);
        }

        if (filter.language != null && !filter.language.isDefaultLanguage()) {
            query.setParameter("language", filter.language());
        }
    }

    public Course findByName(Language language, String name) {

        if (language == null) {
            language = Language.DEFAULT_LANGUAGE;
        }

        TypedQuery<Course> query = null;
        if (language == Language.DEFAULT_LANGUAGE) {
            query = getEntityManager().createNamedQuery(Course.QUERY_FIND_BY_NAME, Course.class);
            query.setParameter("name", name);
        } else {
            query = getEntityManager().createNamedQuery(Course.QUERY_FIND_BY_TRANSLATED_NAME, Course.class);
            query.setParameter("name", name);
            query.setParameter("language", language);
        }

        List<Course> results = QueryUtils.getResultListFromQuery(query);
        return results.isEmpty() ? null : results.getFirst();
    }

}
