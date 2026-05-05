package com.example.minor_project1.repositories;

import com.example.minor_project1.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

@Repository
public class StudentCacheRepository {

    //TODO: Make this cache repository a generic, currently it only handles the Student objects.

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    private static final String STUDENT_KEY_PREFIX = "std::";
    private static final Long STUDENT_KEY_EXPIRY = 3600l;

    public void add(Student student){
        Integer studentId = student.getId();
        String key = this.getKey(studentId);
        this.redisTemplate.opsForValue().set(key, student, STUDENT_KEY_EXPIRY, TimeUnit.SECONDS);

    }

    public Student get(Integer studentId){
        String key = this.getKey(studentId);
        return (Student) this.redisTemplate.opsForValue().get(key);
    }

    private String getKey(Integer studentId){
        return STUDENT_KEY_PREFIX + studentId;
    }
}
