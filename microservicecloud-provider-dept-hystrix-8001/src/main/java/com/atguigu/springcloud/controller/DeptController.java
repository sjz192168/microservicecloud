package com.atguigu.springcloud.controller;

import com.atguigu.springcloud.entities.Dept;
import com.atguigu.springcloud.service.DeptService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class DeptController {
    @Autowired
    private DeptService deptService;
    @RequestMapping(value="/dept/add",method = RequestMethod.POST)
    public boolean add(@RequestBody Dept dept){
        return deptService.add(dept);
    }

    /**
     * 在查询数据库没有对应的信息时，会抛出异常，会自动调用@HystrixCommand(fallbackMethod = "processHystrix_Get")
     * 标注好的指定的方法
     * @param id
     * @return
     */
    @RequestMapping(value="/dept/get/{id}",method = RequestMethod.GET)
    @HystrixCommand(fallbackMethod = "processHystrix_Get")
    public Dept add(@PathVariable("id") Long id){
        Dept dept = this.deptService.get(id);
        if (null==dept){
            throw new RuntimeException("该ID: "+id+"没有对应的信息，null--@HystrixCommand");
        }
        return dept;
    }

    public Dept processHystrix_Get(@PathVariable("id")Long id){
        return new Dept().setDeptno(id).setDname("该ID: "+id+"没有对应的信息，null--@HystrixCommand").setDb_source("no this database in mysql");

    }

    @RequestMapping(value="/dept/list",method = RequestMethod.GET)
    public List<Dept> list(){
        return deptService.list();
    }

}
