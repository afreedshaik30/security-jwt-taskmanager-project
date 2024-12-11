package com.sb.main.service;

import com.sb.main.DTO.TaskDto;

import java.util.List;

public interface TaskService
{
  TaskDto saveTaskByUserId(long uid, TaskDto taskDto);

  List<TaskDto> getAllTasksByUserId(long uid);

  TaskDto getTaskByUserId(long uid, long tid);

  void deleteTaskByUserId(long uid, long tid);
}
