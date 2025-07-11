package org.jenkinsci.plugins.build.service;

import hudson.model.Cause;
import hudson.model.ParametersAction;
import hudson.model.Result;
import hudson.model.Run;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Runs 工具类的单元测试。
 * 使用 JUnit 5 + Mockito 进行 Mock 测试，覆盖主要静态方法。
 */
class RunsTest {
    /**
     * 测试：构建进行中时，includeBuildInMetrics 应返回 false。
     */
    @Test
    void testIncludeBuildInMetrics_Building() {
        // Mock 一个正在构建中的 Run
        Run run = mock(Run.class);
        when(run.isBuilding()).thenReturn(true);
        // 断言：不应计入指标
        assertFalse(Runs.includeBuildInMetrics(run));
    }

    /**
     * 测试：构建已完成时，includeBuildInMetrics 应返回 true。
     */
    @Test
    void testIncludeBuildInMetrics_NotBuilding() {
        // Mock 一个已完成的 Run
        Run run = mock(Run.class);
        when(run.isBuilding()).thenReturn(false);
        when(run.getResult()).thenReturn(Result.SUCCESS);
        // 断言：应计入指标
        assertTrue(Runs.includeBuildInMetrics(run));
    }

    /**
     * 测试：getJobNameText 能正确提取 Job 名称。
     */
    @Test
    void testGetJobNameText() {
        // Mock Run 的显示名称
        Run run = mock(Run.class);
        when(run.getFullDisplayName()).thenReturn("test-job #12");
        // 断言：应返回 "test-job"
        assertEquals("test-job", Runs.getJobNameText(run));
    }

    /**
     * 测试：getResultText 能正确返回构建结果字符串。
     */
    @Test
    void testGetResultText() {
        // Mock Run 的结果为 FAILURE
        Run run = mock(Run.class);
        when(run.getResult()).thenReturn(Result.FAILURE);
        // 断言：应返回 "FAILURE"
        assertEquals("FAILURE", Runs.getResultText(run));
    }

    /**
     * 测试：getRunnerText 能正确获取触发人 ID。
     */
    @Test
    void testGetRunnerText() {
        // Mock Run 的触发人为 admin
        Run run = mock(Run.class);
        Cause.UserIdCause cause = mock(Cause.UserIdCause.class);
        when(run.getCause(Cause.UserIdCause.class)).thenReturn(cause);
        when(cause.getUserId()).thenReturn("admin");
        // 断言：应返回 "admin"
        assertEquals("admin", Runs.getRunnerText(run));
    }

    /**
     * 测试：getBuildParameters 能正确获取参数列表。
     */
    @Test
    void testGetBuildParameters() {
        // Mock Run 带有一个参数 key=value
        Run run = mock(Run.class);
        ParametersAction action = mock(ParametersAction.class);
        hudson.model.StringParameterValue param = new hudson.model.StringParameterValue("key", "value");
        when(action.getParameters()).thenReturn(Collections.singletonList(param));
        when(run.getActions(ParametersAction.class)).thenReturn(Arrays.asList(action));
        // 断言：参数映射正确
        Map<String, Object> params = Runs.getBuildParameters(run);
        assertEquals(1, params.size());
        assertEquals("value", params.get("key"));
    }
} 