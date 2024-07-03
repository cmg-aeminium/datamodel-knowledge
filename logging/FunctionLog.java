/*
 * Copyright (c) 2024 Carlos Gonçalves (https://www.linkedin.com/in/carlosmogoncalves/)
 * Likely open-source, so copy at will, bugs will be yours as well.
 */
package pt.cmg.aeminium.knowledge.persistence.entities.logging;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Objects;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * @author Carlos Gonçalves
 */
@Entity
@Table(name = "functionlogs")
@NamedNativeQuery(name = FunctionLog.QUERY_FIND_LATEST_FOR_FUNCTION,
    query = "SELECT t.* FROM FunctionLog t WHERE functionName = ? ORDER BY id DESC LIMIT 1",
    resultClass = FunctionLog.class)
public class FunctionLog implements Serializable {

    private static final long serialVersionUID = -1223413257209541328L;

    public static final String QUERY_FIND_LATEST_FOR_FUNCTION = "FunctionLog.findLatestForFunction";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FUNCTIONLOG_SEQ")
    @SequenceGenerator(name = "FUNCTIONLOG_SEQ",
        sequenceName = "functionlogs_id_seq",
        initialValue = 1,
        allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "functionname")
    private String functionName;

    @Column(name = "startedat")
    private LocalDateTime startedAt;

    @Column(name = "finishedat")
    private LocalDateTime finishedAt;

    @Column(name = "executiontimeinmillis")
    private Long executionTimeInMillis;

    @Column(name = "wassuccess")
    private boolean wasSuccess;

    @Column(name = "exception")
    private String exception;

    // Maybe this is a bit too much information?
    @Column(name = "stacktrace")
    private String stackTrace;

    public FunctionLog() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFunctionName() {
        return functionName;
    }

    public void setFunctionName(String functionName) {
        this.functionName = functionName;
    }

    public LocalDateTime getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public LocalDateTime getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(LocalDateTime finishedAt) {
        this.finishedAt = finishedAt;
    }

    public Long getExecutionTimeInMillis() {
        return executionTimeInMillis;
    }

    public void setExecutionTimeInMillis(Long executionTimeInMillis) {
        this.executionTimeInMillis = executionTimeInMillis;
    }

    public boolean wasSuccess() {
        return wasSuccess;
    }

    public void setWasSuccess(boolean wasSuccess) {
        this.wasSuccess = wasSuccess;
    }

    public String getException() {
        return exception;
    }

    public void setExceptionInfo(Exception e) {
        this.exception = e.getMessage();
        setStackTrace(e);
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public void setStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        this.stackTrace = sw.toString();
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
        FunctionLog other = (FunctionLog) obj;
        return Objects.equals(id, other.id);
    }

    @Override
    public String toString() {
        return "FunctionLog [id=%s, functionName=%s, executionTimeInMillis=%s, wasSuccess=%s]".formatted(id, functionName, executionTimeInMillis, wasSuccess);
    }

}
