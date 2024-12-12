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
import pt.cmg.aeminium.datamodel.knowledge.entities.curricula.CourseClass;

/**
 * @author Carlos Gonçalves
 */
@Stateless
public class CourseClassDAO extends JPACrudDAO<CourseClass> {

    private static final String WHERE = "WHERE ";
    private static final String AND = "AND ";

    public CourseClassDAO() {
        super(CourseClass.class);
    }

    public record CourseClassFilterCriteria(
        Integer year,
        Integer semester,
        Boolean isOptional,
        String name,
        Language language,
        Long courseId,
        Long schoolId,
        Long size,
        Long offset) {
    }

    public List<CourseClass> findFiltered(CourseClassFilterCriteria filter) {

        StringBuilder selectText = new StringBuilder("SELECT cc FROM CourseClass cc ");
        StringBuilder filterText = new StringBuilder();
        String prefix = WHERE;

        if (filter.year != null) {
            filterText.append(prefix).append("cc.year = :year ");
            prefix = AND;
        }

        if (filter.semester != null) {
            filterText.append(prefix).append("cc.semester = :semester ");
            prefix = AND;
        }

        if (filter.isOptional != null) {
            filterText.append(prefix).append("cc.isOptional = :isOptional ");
            prefix = AND;
        }

        if (filter.courseId != null) {
            filterText.append(prefix).append("cc.course.id = :course ");
            prefix = AND;
        }

        if (filter.schoolId != null) {
            filterText.append(prefix).append("cc.course.school.id = :school ");
            prefix = AND;
        }

        if (StringUtils.isNotBlank(filter.name)) {
            if (filter.language == null || filter.language.isDefaultLanguage()) {
                filterText.append(prefix).append("cc.nameTextContentId IN (SELECT t.id FROM TextContent t WHERE t.textValue = :name) ");
            } else {
                filterText.append(prefix).append("cc.nameTextContentId IN (SELECT t.id FROM TranslatedText t WHERE t.textValue = :name AND t.language = :language) ");
            }

            prefix = AND;
        }

        String queryText = selectText.append(filterText).toString();
        TypedQuery<CourseClass> query = getEntityManager().createQuery(queryText, CourseClass.class);

        if (filter.year != null) {
            query.setParameter("year", filter.year);
        }

        if (filter.semester != null) {
            query.setParameter("semester", filter.semester);
        }

        if (filter.isOptional != null) {
            query.setParameter("isOptional", filter.isOptional);
        }

        if (filter.courseId != null) {
            query.setParameter("course", filter.courseId);
        }

        if (filter.schoolId != null) {
            query.setParameter("school", filter.schoolId);
        }

        if (StringUtils.isNotBlank(filter.name)) {
            query.setParameter("name", filter.name);

            if (filter.language != null && !filter.language.isDefaultLanguage()) {
                query.setParameter("language", filter.language());
            }
        }

        if (filter.size() != null) {
            query.setMaxResults(filter.size().intValue());
        }

        if (filter.offset() != null) {
            query.setFirstResult(filter.offset().intValue());
        }

        return query.getResultList();
    }

}
