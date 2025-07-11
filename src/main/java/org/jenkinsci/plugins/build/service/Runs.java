package org.jenkinsci.plugins.build.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import hudson.model.*;

/**
 * Jenkins 构建历史工具类。
 * <p>
 * 提供对 Run（构建记录）相关的常用操作方法。
 * </p>
 * @author mingsha
 * @date 2025-07-10
 */
public class Runs {

    /**
     * 判断该构建是否应计入指标统计。
     * <p>
     * 当前逻辑：只要构建不在进行中（isBuilding() 为 false）就计入统计。
     * 可以根据需要扩展更复杂的过滤条件。
     * </p>
     * @param build 构建记录
     * @return 是否计入统计
     */
    public static boolean includeBuildInMetrics(Run build) {
        boolean include = false;
        if (!build.isBuilding()) {
            // 构建已完成，计入统计
            include = true;
            // 获取构建结果（可用于后续扩展过滤条件）
            Result result = build.getResult();
        }
        return include;
    }

    /**
     * 获取构建对应的 Job 名称。
     * <p>
     * 从构建的完整显示名称中提取 Job 名称部分，
     * 去掉构建号等信息。
     * </p>
     * @param run 构建记录
     * @return Job 名称，如果获取失败返回 null
     */
    public static String getJobNameText(Run run) {
        if (run != null) {
            String fullDisplayName = run.getFullDisplayName();
            if (fullDisplayName != null) {
                return fullDisplayName.split("#")[0].trim();
            }
        }
        return null;
    }

    /**
     * 获取构建结果文本。
     * <p>
     * 将构建结果枚举转换为字符串表示。
     * </p>
     * @param run 构建记录
     * @return 结果字符串，如 "SUCCESS"、"FAILURE" 等
     */
    public static String getResultText(Run run) {
        if (run != null) {
            Result result = run.getResult();
            if (result != null) {
                return result.toString();
            }
        }
        return null;
    }

    /**
     * 获取触发该构建的用户ID。
     * <p>
     * 从构建原因中提取用户ID信息。
     * 目前只支持 UserIdCause 类型的触发原因。
     * </p>
     * @param run 构建记录
     * @return 用户ID，如果无法获取返回 null
     */
    public static String getRunnerText(Run run) {
        if (run != null) {
            Cause.UserIdCause userIdCause = (Cause.UserIdCause) run.getCause(Cause.UserIdCause.class);
            if (userIdCause != null) {
                return userIdCause.getUserId();
            }
        }
        return null;
    }

    /**
     * 获取构建参数列表。
     * <p>
     * 从构建的 ParametersAction 中提取所有参数，
     * 返回参数名到参数值的映射。
     * </p>
     * @param build 构建记录
     * @return 参数名到参数值的映射，如果没有参数返回空 Map
     */
    public static Map<String, Object> getBuildParameters(Run build) {
        List<ParametersAction> actions = build.getActions(ParametersAction.class);
        Map<String, Object> answer = new HashMap<>();
        for (ParametersAction action : actions) {
            List<ParameterValue> parameters = action.getParameters();
            if (parameters != null) {
                for (ParameterValue parameter : parameters) {
                    String name = parameter.getName();
                    Object value = parameter.getValue();
                    answer.put(name, value);
                }
            }
        }
        return answer;
    }
}
