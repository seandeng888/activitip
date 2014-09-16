/**
 *
 */
package com.easyway.workflow.activiti.exam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.runtime.ProcessInstance;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
/**
 * 我把Activiti 5.6默认工程中有关JPA的部分配置删除了，其实通过这个就可以初始化Activiti引擎实例。
 * 为了测试方便，将获取服务的实现抽象出来，同时使用Spring自带的与JUnit4集成的工具（
 * AbstractTransactionalJUnit4SpringContextTests）。
 *
 * 将classpath:activiti-context.xml在测试的时候进行加载，这样，在测试的子类中，只需要将其他的相
 * 关Spring配置单独加载即可，业务配置与流程配置分开，便于维护。 
 * @author longgangbai
 *
 * 2011-12-18  上午01:37:14
 */
@ContextConfiguration("classpath*:application-context-standalone.xml")
public class ActivitiWithSpringStandaloneTest extends AbstractSpringTest {

    @Override
    protected void beforeTest() throws Exception {
        Deployment deployment = repositoryService
                .createDeployment()
                .addClasspathResource(
                        "diagrams/SprintActiviti56.bpmn20.xml")
                .deploy();
        deploymentId = deployment.getId();
    }

    @Override
    protected void afterTest() throws Exception {
        repositoryService.deleteDeployment(deploymentId, true);
    }

    @Test
    public void triggerMyProcess() {
        // prepare data packet
        Map<String, Object> variables = new HashMap<String, Object>();
        variables.put("姓名", "程序员");
        variables.put("职务", "高级软件工程师");
        variables.put("语言", "Java/C#");
        variables.put("操作系统", "Window,Linux，unix，Aix");
        variables.put("工作地点","苏州高新技术软件园");

        // start process instance
        ProcessInstance pi = runtimeService.startProcessInstanceByKey("DeveloperWorkExam", variables);
        assert (pi!=null);

        List<Execution> executions = runtimeService.createExecutionQuery().list();
        assert (executions.size()==1);

        Execution execution = runtimeService.createExecutionQuery().singleResult();
        runtimeService.setVariable(execution.getId(), "type", "receiveTask");
        runtimeService.signal(execution.getId());

        executions = runtimeService.createExecutionQuery().list();
        assert (executions.size()==1);

        execution = executions.get(0);
        runtimeService.setVariable(execution.getId(), "oper", "录用此人....");
        runtimeService.signal(execution.getId());
    }
}