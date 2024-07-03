package pt.cmg.aeminium.knowledge.dao.knowledgeareas;

import jakarta.ejb.Stateless;
import pt.cmg.aeminium.knowledge.dao.JPACrudDAO;
import pt.cmg.aeminium.knowledge.persistence.entities.knowledgebodies.KnowledgeArea;

@Stateless
public class KnowledgeAreaDAO extends JPACrudDAO<KnowledgeArea> {

    public KnowledgeAreaDAO() {
        super(KnowledgeArea.class);
    }

}
