/**
 *
 */
package com.easyway.workflow.activiti.exam;

import java.util.logging.Logger;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
/**
 *
 * 工作流模拟程序员面试过程情景如下：
 *     1.开发知识面试或者笔试
 *     2.人事面试
 *
 * 在spring3.0.3和activiti5.6整合时候，建议采用activiti-spring-examples中的jar文件。
 * 如果么有完整 的jar文件，可以参考{activiti_home}/setup/files/dependencies/libs.spring.runtime.txt文件
 * 。（C:\mash_activiti-5.6\setup\files\dependencies）
 *
 * 之所以要采用封装的原因，spring配置文件和activiti的配置文件分开发布部署。
 *
 * @author longgangbai
 *
 * 2011-12-18  上午01:32:17
 */
@ContextConfiguration("classpath:application-context-standalone.xml")
public abstract class AbstractSpringTest extends AbstractTransactionalJUnit4SpringContextTests {

    @SuppressWarnings("unused")
    private final Logger log = Logger.getLogger(AbstractSpringTest.class.getName());

    @SuppressWarnings("unused")
    @Autowired
    private ProcessEngine processEngine;
    @Autowired
    protected RepositoryService repositoryService;
    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;
    @Autowired
    protected HistoryService historyService;
    @Autowired
    protected ManagementService managementService;

    protected String deploymentId;

    public AbstractSpringTest() {
        super();
    }

    @Before
    public void initialize() throws Exception {
        beforeTest();
    }

    @After
    public void clean() throws Exception {
        afterTest();
    }

    protected abstract void beforeTest() throws Exception;

    protected abstract void afterTest() throws Exception;
}
