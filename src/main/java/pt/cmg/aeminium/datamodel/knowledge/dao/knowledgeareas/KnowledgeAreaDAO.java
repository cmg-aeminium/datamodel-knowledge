package pt.cmg.aeminium.datamodel.knowledge.dao.knowledgeareas;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import pt.cmg.aeminium.datamodel.common.entities.localisation.Language;
import pt.cmg.aeminium.datamodel.knowledge.dao.JPACrudDAO;
import pt.cmg.aeminium.datamodel.knowledge.entities.knowledgebodies.KnowledgeArea;
import pt.cmg.jakartautils.jpa.QueryUtils;

@Stateless
public class KnowledgeAreaDAO extends JPACrudDAO<KnowledgeArea> {

    private static final String BASE_SELECT_KA_QUERY = "SELECT ka FROM KnowledgeArea ka ";
    private static final String BASE_COUNT_KA_QUERY = "SELECT COUNT(ka) FROM KnowledgeArea ka ";

    private static final String WHERE = "WHERE ";
    private static final String AND = "AND ";

    public KnowledgeAreaDAO() {
        super(KnowledgeArea.class);
    }

    public record KnowledgeAreaFilterCriteria(
        String name,
        Language language,
        Long knowledgeBodyId,
        Long size,
        Long offset) {
    }

    public List<KnowledgeArea> findFiltered(KnowledgeAreaFilterCriteria filter) {

        String queryText = filterQueryBuilder(BASE_SELECT_KA_QUERY, filter);
        TypedQuery<KnowledgeArea> query = getEntityManager().createQuery(queryText, KnowledgeArea.class);

        query = setFilterParameters(query, filter);

        if (filter.size() != null) {
            query.setMaxResults(filter.size().intValue());
        }

        if (filter.offset() != null) {
            query.setFirstResult(filter.offset().intValue());
        }

        return QueryUtils.getResultListFromQuery(query);
    }

    public int countFiltered(KnowledgeAreaFilterCriteria filter) {

        String queryText = filterQueryBuilder(BASE_COUNT_KA_QUERY, filter);

        TypedQuery<Integer> query = getEntityManager().createQuery(queryText, Integer.class);

        query = setFilterParameters(query, filter);

        return QueryUtils.getIntResultFromQuery(query);
    }

    private String filterQueryBuilder(String baseSelectQuery, KnowledgeAreaFilterCriteria filter) {

        StringBuilder selectText = new StringBuilder(baseSelectQuery);
        StringBuilder filterText = new StringBuilder();
        String prefix = WHERE;

        if (filter.knowledgeBodyId != null) {
            filterText.append(prefix).append("ka.knowledgeBody.id = :knowledgeBody ");
            prefix = AND;
        }

        if (StringUtils.isNotBlank(filter.name)) {
            if (filter.language == null || filter.language.isDefaultLanguage()) {
                filterText.append(prefix).append("ka.nameTextContentId IN (SELECT t.id FROM TextContent t WHERE t.textValue = :name) ");
            } else {
                filterText.append(prefix).append("ka.nameTextContentId IN (SELECT t.id FROM TranslatedText t WHERE t.textValue = :name AND t.language = :language) ");
            }

            prefix = AND;
        }

        return selectText.append(filterText).toString();
    }

    private <T> TypedQuery<T> setFilterParameters(TypedQuery<T> query, KnowledgeAreaFilterCriteria filter) {

        if (filter.knowledgeBodyId != null) {
            query.setParameter("knowledgeBody", filter.knowledgeBodyId);
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
