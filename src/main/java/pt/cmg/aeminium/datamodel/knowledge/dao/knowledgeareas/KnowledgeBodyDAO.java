/**
 * Copyright (c) 2024 Carlos Gonçalves (https://www.linkedin.com/in/carlosmogoncalves/)
 * Likely open-source, so copy at will, bugs will be yours as well.
 */
package pt.cmg.aeminium.datamodel.knowledge.dao.knowledgeareas;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import pt.cmg.aeminium.datamodel.common.entities.localisation.Language;
import pt.cmg.aeminium.datamodel.knowledge.dao.JPACrudDAO;
import pt.cmg.aeminium.datamodel.knowledge.entities.knowledgebodies.KnowledgeBody;
import pt.cmg.jakartautils.jpa.QueryUtils;

@Stateless
public class KnowledgeBodyDAO extends JPACrudDAO<KnowledgeBody> {

    private static final String BASE_SELECT_BODY_QUERY = "SELECT b FROM KnowledgeBody b ";
    private static final String BASE_COUNT_BODY_QUERY = "SELECT COUNT(b) FROM KnowledgeBody b ";

    private static final String WHERE = "WHERE ";
    private static final String AND = "AND ";

    public KnowledgeBodyDAO() {
        super(KnowledgeBody.class);
    }

    public record KnowledgeBodyFilterCriteria(
        Integer year,
        String name,
        Language language,
        Long createdById,
        Long size,
        Long offset) {

    }

    public List<KnowledgeBody> findFiltered(KnowledgeBodyFilterCriteria filter) {

        String queryText = filterQueryBuilder(BASE_SELECT_BODY_QUERY, filter);

        TypedQuery<KnowledgeBody> query = getEntityManager().createQuery(queryText, KnowledgeBody.class);

        query = setFilterParameters(query, filter);

        if (filter.size() != null) {
            query.setMaxResults(filter.size().intValue());
        }

        if (filter.offset() != null) {
            query.setFirstResult(filter.offset().intValue());
        }

        return QueryUtils.getResultListFromQuery(query);
    }

    public int countFiltered(KnowledgeBodyFilterCriteria filter) {

        String queryText = filterQueryBuilder(BASE_COUNT_BODY_QUERY, filter);

        TypedQuery<Integer> query = getEntityManager().createQuery(queryText, Integer.class);

        query = setFilterParameters(query, filter);

        return QueryUtils.getIntResultFromQuery(query);
    }

    private String filterQueryBuilder(String baseSelectQuery, KnowledgeBodyFilterCriteria filter) {

        StringBuilder selectText = new StringBuilder(baseSelectQuery);
        StringBuilder filterText = new StringBuilder();
        String prefix = WHERE;

        if (filter.year != null) {
            filterText.append(prefix).append("b.year = :year ");
            prefix = AND;
        }

        if (filter.createdById != null) {
            filterText.append(prefix).append("b.createdBy.id = :createdBy ");
            prefix = AND;
        }

        if (StringUtils.isNotBlank(filter.name)) {
            if (filter.language == null || filter.language.isDefaultLanguage()) {
                filterText.append(prefix).append("b.nameTextContentId IN (SELECT t.id FROM TextContent t WHERE t.textValue = :name) ");
            } else {
                filterText.append(prefix).append("b.nameTextContentId IN (SELECT t.id FROM TranslatedText t WHERE t.textValue = :name AND t.language = :language) ");
            }

            prefix = AND;
        }

        return selectText.append(filterText).toString();
    }

    private <T> TypedQuery<T> setFilterParameters(TypedQuery<T> query, KnowledgeBodyFilterCriteria filter) {

        if (filter.year != null) {
            query.setParameter("year", filter.year);
        }

        if (filter.createdById != null) {
            query.setParameter("createdBy", filter.createdById);
        }

        if (StringUtils.isNotBlank(filter.name)) {
            query.setParameter("name", filter.name);

            if (filter.language != null && !filter.language.isDefaultLanguage()) {
                query.setParameter("language", filter.language());
            }
        }

        return query;

    }

}
