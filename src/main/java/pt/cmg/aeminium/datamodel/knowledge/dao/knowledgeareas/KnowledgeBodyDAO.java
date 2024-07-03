/**
 * Copyright (c) 2024 Carlos Gonçalves (https://www.linkedin.com/in/carlosmogoncalves/)
 * Likely open-source, so copy at will, bugs will be yours as well.
 */
package pt.cmg.aeminium.datamodel.knowledge.dao.knowledgeareas;

import jakarta.ejb.Stateless;
import pt.cmg.aeminium.datamodel.knowledge.dao.JPACrudDAO;
import pt.cmg.aeminium.datamodel.knowledge.entities.knowledgebodies.KnowledgeBody;

@Stateless
public class KnowledgeBodyDAO extends JPACrudDAO<KnowledgeBody> {

    public KnowledgeBodyDAO() {
        super(KnowledgeBody.class);
    }

}
