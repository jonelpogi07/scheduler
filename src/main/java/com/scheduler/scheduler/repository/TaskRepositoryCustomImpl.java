package com.scheduler.scheduler.repository;

import com.scheduler.scheduler.dto.TaskDto;
import com.scheduler.scheduler.exceptionEnums.ExceptionEnums;
import com.scheduler.scheduler.exceptions.TaskExceptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

@Repository
public class TaskRepositoryCustomImpl implements TaskRepositoryCustom {

    @Autowired
    private EntityManager entityManager;

    @Override
    public TaskDto findTaskByNameAndStatus(String name, int status) {
        String sql = "SELECT * FROM task WHERE name = :name AND status = :status";

        Query query = entityManager.createNativeQuery(sql, "TaskDtoMapping");
        query.setParameter("name", name);
        query.setParameter("status", status);
        Object result;

        try{
            result = query.getSingleResult();
        } catch (NoResultException e){
            throw new TaskExceptions(ExceptionEnums.ErrorCode.TASK_CANNOT_FIND.getMessage());
        }

        return (TaskDto) result;
    }
}