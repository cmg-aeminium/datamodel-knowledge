package pt.cmg.aeminium.datamodel.knowledge.dao.knowledgeareas;

import jakarta.ejb.Stateless;
import pt.cmg.aeminium.datamodel.knowledge.entities.knowledgebodies.KnowledgeArea;

@Stateless
public class KnowledgeAreaDAO extends pt.cmg.aeminium.datamodel.knowledge.dao.JPACrudDAO<KnowledgeArea> {

    public KnowledgeAreaDAO() {
        super(KnowledgeArea.class);
    }

}
