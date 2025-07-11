# mingsha Jenkins Plugin Build Metrics

> Jenkins 构建指标采集与导出插件

---

## 项目简介

本插件用于采集 Jenkins Job 及其构建历史的详细指标信息，支持定时与手动采集，并以文本格式对外暴露，便于监控、分析与集成。

---

## 主要特性
- 支持采集所有 Job 的构建历史、状态、参数、结果、触发人等信息
- 指标数据可通过 REST API 导出，支持 Prometheus 等系统采集
- 支持异步定时采集与手动触发采集
- 采集窗口、命名空间等参数可全局配置
- 完善的权限校验与安全控制
- 代码结构现代化，注释规范，单元测试覆盖
- 支持 SonarQube 静态代码扫描

---

## 安装与运行

### 依赖环境
- Jenkins 2.501+
- JDK 17
- Maven 3.6+

### 本地运行（开发调试）
```sh
make run
```

### 打包
```sh
make package
```
生成的插件包位于 `target/*.hpi`。

### 安装到 Jenkins
1. 进入 Jenkins 管理后台 → 插件管理 → 高级 → 上传插件
2. 选择 `target/*.hpi` 文件上传并重启 Jenkins

---

## 配置说明
- 插件支持全局配置（路径、命名空间、采集周期、采集窗口等）
- 可在 Jenkins 系统管理 → 系统配置 → Build Metrics 配置项中设置

---

## API 说明
- 指标数据接口：`/your_plugin_url/metrics`（具体路径可在全局配置中自定义）
- 支持 GET 方式访问，返回文本格式指标数据
- 支持手动采集接口 `/your_plugin_url/collect`，需管理员权限或 API Token

---

## 单元测试
- 使用 JUnit 5 + Mockito，测试代码位于 `src/test/java/`
- 运行测试：
```sh
make test
```

---

## CI/CD
- 推荐使用 Jenkinsfile 进行自动化构建、归档、邮件通知等
- 示例流水线见 `Jenkinsfile`

---

## SonarQube 静态代码扫描
- 推荐使用 Jenkinsfile.sonar 进行 SonarQube 扫描
- 需在 Jenkins 全局工具配置中设置 jdk17 和 SonarQube 服务器

---

## 常见问题
- **插件无法加载？** 请确认 Jenkins 版本、JDK 版本与依赖环境一致
- **API 无法访问？** 检查权限配置与 Jenkins 全局安全设置
- **采集数据不全？** 检查采集窗口、命名空间等配置项

---

## 贡献方式
欢迎提交 Issue、PR 或建议！
1. Fork 本仓库
2. 新建分支进行开发
3. 提交 Pull Request

---

## 联系方式
- 作者：mingsha
- 邮箱：chenlong220192@gmail.com
- GitHub: [项目主页](https://github.com/chenlong220192/mingsha-jenkins-plugin-build-metrics)

---

> 本项目遵循 [MIT 许可证](./LICENSE)，欢迎自由使用与二次开发。
