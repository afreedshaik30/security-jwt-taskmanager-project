package com.sb.main.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_tasks")
public class Tasks
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tid;

    @Column(name="taskname", nullable = false)
    private String taskname;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "uid")
    private Users user;

    public long getTid() {
        return tid;
    }

    public void setTid(long tid) {
        this.tid = tid;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
