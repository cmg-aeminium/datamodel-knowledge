/*
 * Copyright (c) 2019
 * Carlos Gonçalves (https://www.linkedin.com/in/carlosmogoncalves/)
 * All rights reserved.
 */
package pt.cmg.aeminium.knowledge.persistence.entities.knowledgebodies;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.config.CacheIsolationType;

@Entity
@Table(name = "knowledgeareas")
@Cache(type = CacheType.FULL, isolation = CacheIsolationType.SHARED, alwaysRefresh = true, coordinationType = CacheCoordinationType.SEND_NEW_OBJECTS_WITH_CHANGES)
@NamedQuery(name = KnowledgeArea.QUERY_FIND_ALL, query = "SELECT ka FROM KnowledgeArea ka")
public class KnowledgeArea implements Serializable {

    private static final long serialVersionUID = 4341439068096536870L;

    public static final String QUERY_FIND_ALL = "KnowledgeArea.findAll";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "KNOWLEDGEAREA_SEQUENCE")
    @SequenceGenerator(name = "KNOWLEDGEAREA_SEQUENCE",
        sequenceName = "knowledgeareas_id_seq",
        initialValue = 1,
        allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "image", nullable = true)
    private String image;

    @Column(name = "name")
    private Long nameTextContentId;

    @Column(name = "description")
    private Long descriptionContentId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "knowledgebody", referencedColumnName = "id")
    private KnowledgeBody knowledgeBody;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "knowledgeArea", fetch = FetchType.LAZY)
    private List<KnowledgeTopic> knowledgeTopics;

    @Column(name = "createdat")
    private LocalDateTime createdAt;

    public KnowledgeArea() {
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Long getNameTextContentId() {
        return nameTextContentId;
    }

    public void setNameTextContentId(Long nameTextContentId) {
        this.nameTextContentId = nameTextContentId;
    }

    public Long getDescriptionContentId() {
        return descriptionContentId;
    }

    public void setDescriptionContentId(Long descriptionContentId) {
        this.descriptionContentId = descriptionContentId;
    }

    public KnowledgeBody getBodyOfKnowledge() {
        return knowledgeBody;
    }

    public void setBodyOfKnowledge(KnowledgeBody bodyOfKnowledge) {
        this.knowledgeBody = bodyOfKnowledge;
    }

    public List<KnowledgeTopic> getKnowledgeTopics() {
        return knowledgeTopics;
    }

    public void setKnowledgeTopics(List<KnowledgeTopic> knowledgeTopics) {
        this.knowledgeTopics = knowledgeTopics;
    }

    public void addTopic(KnowledgeTopic knowledgeTopic) {
        this.knowledgeTopics.add(knowledgeTopic);
    }

    public void removeTopic(KnowledgeTopic knowledgeTopic) {
        this.knowledgeTopics.remove(knowledgeTopic);
    }

    public KnowledgeBody getKnowledgeBody() {
        return knowledgeBody;
    }

    public void setKnowledgeBody(KnowledgeBody knowledgeBody) {
        this.knowledgeBody = knowledgeBody;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        KnowledgeArea other = (KnowledgeArea) obj;
        return Objects.equals(id, other.id);
    }

}
