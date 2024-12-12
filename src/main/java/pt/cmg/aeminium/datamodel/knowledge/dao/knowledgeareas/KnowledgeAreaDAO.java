package pt.cmg.aeminium.datamodel.knowledge.dao.knowledgeareas;

import java.util.List;
import org.apache.commons.lang3.StringUtils;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;
import pt.cmg.aeminium.datamodel.common.entities.localisation.Language;
import pt.cmg.aeminium.datamodel.knowledge.dao.JPACrudDAO;
import pt.cmg.aeminium.datamodel.knowledge.entities.knowledgebodies.KnowledgeArea;

@Stateless
public class KnowledgeAreaDAO extends JPACrudDAO<KnowledgeArea> {

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

        StringBuilder selectText = new StringBuilder("SELECT ka FROM KnowledgeArea ka ");
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

        String queryText = selectText.append(filterText).toString();
        TypedQuery<KnowledgeArea> query = getEntityManager().createQuery(queryText, KnowledgeArea.class);

        if (filter.knowledgeBodyId != null) {
            query.setParameter("knowledgeBody", filter.knowledgeBodyId);
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
