package com.sb.main.repository;

import com.sb.main.entities.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Tasks,Long>
{
    // Method for finding tasks by user ID
    List<Tasks> findAllByUser_Uid(long uid);
}
