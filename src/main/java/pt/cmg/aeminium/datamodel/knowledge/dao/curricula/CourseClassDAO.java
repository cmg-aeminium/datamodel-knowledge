/**
 * Copyright (c) 2020 Carlos Gonçalves (https://www.linkedin.com/in/carlosmogoncalves/)
 * Likely open-source, so copy at will, bugs will be yours as well.
 */
package pt.cmg.aeminium.datamodel.knowledge.dao.curricula;

import jakarta.ejb.Stateless;
import pt.cmg.aeminium.datamodel.knowledge.dao.JPACrudDAO;
import pt.cmg.aeminium.datamodel.knowledge.entities.curricula.CourseClass;

/**
 * @author Carlos Gonçalves
 */
@Stateless
public class CourseClassDAO extends JPACrudDAO<CourseClass> {

    public CourseClassDAO() {
        super(CourseClass.class);
    }

}
