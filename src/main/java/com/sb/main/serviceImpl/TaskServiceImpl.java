package com.sb.main.serviceImpl;

import com.sb.main.DTO.TaskDto;
import com.sb.main.entities.Tasks;
import com.sb.main.entities.Users;
import com.sb.main.exception.ApiException;
import com.sb.main.exception.TaskNotFoundException;
import com.sb.main.exception.UserNotFoundException;
import com.sb.main.repository.TaskRepository;
import com.sb.main.repository.UserRepository;
import com.sb.main.service.TaskService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskServiceImpl implements TaskService
{
    private UserRepository userRepository;
    private TaskRepository taskRepository;
    private ModelMapper modelMapper;

    public TaskServiceImpl(UserRepository userRepository, TaskRepository taskRepository, ModelMapper modelMapper)
    {
        this.userRepository = userRepository;
        this.taskRepository = taskRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public TaskDto saveTaskByUserId(long uid, TaskDto taskDto)
    {
        Users user = userRepository.findById(uid) // get user by id
                                   .orElseThrow(() -> new UserNotFoundException(String.format("USER with ID %d is NOT FOUND",uid)));

        Tasks task = modelMapper.map(taskDto,Tasks.class); // dto to entity
        task.setUser(user); // set user in task

        Tasks savedTask = taskRepository.save(task);   // save task after setting user in task entity

        return modelMapper.map(savedTask,TaskDto.class); // entity to dto
    }

    @Override
    public List<TaskDto> getAllTasksByUserId(long uid)
    {
        Users user = userRepository.findById(uid) // get user by id
                                   .orElseThrow(() -> new UserNotFoundException(String.format("USER with ID %d is NOT FOUND", uid)));

        List<Tasks> tasks = taskRepository.findAllByUser_Uid(uid); // get All tasks of a user by uid (findxxxxUser_uid) foreign key
        return tasks.stream() //entity to dto
                    .map(task -> modelMapper.map(task,TaskDto.class))
                    .collect(Collectors.toList());
    }

    @Override
    public TaskDto getTaskByUserId(long uid, long tid)
    {
        Users user = userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException(String.format("USER with ID %d is NOT FOUND", uid)));

        Tasks task = taskRepository.findById(tid)
                .orElseThrow(() -> new TaskNotFoundException(String.format("TASK with id %d is NOT FOUND", tid)));

        if(user.getUid() != task.getUser().getUid())
        {
          throw  new ApiException(String.format("TASK tid = %d is not belongs to USER uid = %d",tid,uid));
        }

        return modelMapper.map(task,TaskDto.class);
    }

    @Override
    public void deleteTaskByUserId(long uid, long tid)
    {
        Users user = userRepository.findById(uid)
                .orElseThrow(() -> new UserNotFoundException(String.format("USER with ID %d is NOT FOUND", uid)));

        Tasks task = taskRepository.findById(tid)
                .orElseThrow(()-> new TaskNotFoundException(String.format("TASK with id %d is NOT FOUND", tid)));

        if (user.getUid() != task.getUser().getUid())
        {
            throw new ApiException(String.format("TASK tid = %d is not belongs to USER uid = %d",tid,uid));
        }
        taskRepository.delete(task); // delete task
    }
}
