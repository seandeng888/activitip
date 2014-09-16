/**
 *
 */
package com.easyway.workflow.activiti.exam;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.FormService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
/**
 *
 * Activiti 5.6与Spring3.0.3整合也比较简单，其基本思想就是，通过Spring的IOC容器来管理Activiti的流程引擎
 * 实例以及相关服务，可见，主要是基于Activiti在与Spring整合上努力上，做好配置即可。这里基于前面的
 * <receiveTask>的例子来进行.
 *
 *
 * 为了测试方便，将获取服务的实现抽象出来，同时使用Spring自带的与JUnit4集成的工具（
 * AbstractTransactionalJUnit4SpringContextTests）。我们的实现类为AbstractSpringTest，
 *
 *
 * 本文采用activiti和spring整合中自动部署资源的功能配置如下：
 * <!-- 创建流程引擎配置对象 -->
 <bean id="processEngineConfiguration" class="org.activiti.spring.SpringProcessEngineConfiguration">
 <property name="dataSource" ref="dataSource" />
 <property name="transactionManager" ref="transactionManager" />
 <property name="databaseSchemaUpdate" value="true" />
 <property name="mailServerHost" value="localhost" />
 <property name="mailServerPort" value="5025" />
 <property name="jpaHandleTransaction" value="true" />
 <property name="jpaCloseEntityManager" value="true" />
 <property name="jobExecutorActivate" value="false" />
 <!-- 使用spring的自动资源加载部署方式部署 -->
 <property name="deploymentResources" value="classpath*:diagrams/*.bpmn20.xml" />

 </bean>
 * @author longgangbai
 *
 * 2011-12-18  上午12:58:31
 */
@ContextConfiguration("classpath*:application-context.xml")
public class ActivitiWithSpringTest extends  AbstractTransactionalJUnit4SpringContextTests{


    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;
    @Autowired
    private ManagementService managerService;
    @Autowired
    private IdentityService identityService;
    @Autowired
    private FormService formService;



    /**
     * 测试方法
     */
    @Test
    public void triggerMyProcess() {
        // 面试题目和答案
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("姓名", "程序员");
        variables.put("职务", "高级软件工程师");
        variables.put("语言", "Java/C#");
        variables.put("操作系统", "Window,Linux，unix，Aix");
        variables.put("工作地点","苏州高新技术软件园");

        // start process instance
        //获取创建一个实例
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("DeveloperWorkExam", variables);
        assert (pi!=null);

        List<Execution> executions = runtimeService.createExecutionQuery().list();
        assert (executions.size()==1);
        //执行开发技术知识面试业务
        Execution execution = runtimeService.createExecutionQuery().singleResult();
        runtimeService.setVariable(execution.getId(), "type", "receiveTask");
        runtimeService.signal(execution.getId());

        executions = runtimeService.createExecutionQuery().list();
        assert (executions.size()==1);
        //执行人事面试业务
        execution = executions.get(0);
        runtimeService.setVariable(execution.getId(), "oper", "录用此人....");
        runtimeService.signal(execution.getId());
    }
}