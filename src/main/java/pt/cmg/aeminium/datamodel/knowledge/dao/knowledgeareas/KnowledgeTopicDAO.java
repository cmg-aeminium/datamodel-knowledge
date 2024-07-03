/**
 * Copyright (c) 2020 Carlos Gonçalves (https://www.linkedin.com/in/carlosmogoncalves/)
 * Likely open-source, so copy at will, bugs will be yours as well.
 */
package pt.cmg.aeminium.datamodel.knowledge.dao.knowledgeareas;

import jakarta.ejb.Stateless;
import pt.cmg.aeminium.datamodel.knowledge.dao.JPACrudDAO;
import pt.cmg.aeminium.datamodel.knowledge.entities.knowledgebodies.KnowledgeTopic;

/**
 * @author Carlos Gonçalves
 */
@Stateless
public class KnowledgeTopicDAO extends JPACrudDAO<KnowledgeTopic> {

    public KnowledgeTopicDAO() {
        super(KnowledgeTopic.class);
    }

}
