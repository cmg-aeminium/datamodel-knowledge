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

@Stateless
public class KnowledgeBodyDAO extends JPACrudDAO<KnowledgeBody> {

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

        StringBuilder selectText = new StringBuilder("SELECT b FROM KnowledgeBody b ");
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

        String queryText = selectText.append(filterText).toString();
        TypedQuery<KnowledgeBody> query = getEntityManager().createQuery(queryText, KnowledgeBody.class);

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

        if (filter.size() != null) {
            query.setMaxResults(filter.size().intValue());
        }

        if (filter.offset() != null) {
            query.setFirstResult(filter.offset().intValue());
        }

        return query.getResultList();
    }

}
