# 升级文档 (2026.04.09)

## 版本信息
- **版本号**: 1.1.0
- **升级日期**: 2026-04-09
- **升级内容**: 依赖版本升级到最新

## 升级的依赖

### 日志
| 依赖项 | 旧版本 | 新版本 | 说明 |
|--------|--------|--------|------|
| SLF4J | 2.0.17 | 2.0.18 | 日志门面框架 |

### JSON 处理
| 依赖项 | 旧版本 | 新版本 | 说明 |
|--------|--------|--------|------|
| Fastjson2 | 2.0.56 | 2.0.58 | 高性能 JSON 库 |

### Maven 插件
| 依赖项 | 旧版本 | 新版本 | 说明 |
|--------|--------|--------|------|
| maven-compiler-plugin | 3.13.0 | 3.14.0 | Java 编译器插件 |

## 升级说明

### 为什么升级
1. **安全修复**: 新版本包含安全漏洞修复
2. **性能优化**: 提升运行时性能和稳定性
3. **兼容性**: 保持与 Jenkins 生态系统的兼容

### 测试验证
- [ ] 项目编译通过
- [ ] 单元测试通过
- [ ] Jenkins 集成测试通过

## 升级步骤

```bash
# 1. 更新代码
git pull origin develop

# 2. 清理并编译
mvn clean compile -DskipTests

# 3. 运行测试
mvn test

# 4. 打包 HPI 插件
mvn package
```

## 注意事项

1. Jenkins Plugin 要求使用 Java 17
2. 插件版本需要与 Jenkins 版本兼容
3. 当前 BOM 版本: 6237.v4da_61a_4a_19e5 (bom-2.555.x)

## 回滚方案

如需回滚，执行以下命令：

```bash
git revert <commit-hash>
```

## 相关链接

- [GitHub 仓库](https://github.com/chenlong220192/mingsha-jenkins-plugin-build-metrics)
- [Jenkins Plugin 文档](https://jenkins.io/doc/developer/plugin-development/)
- [问题反馈](https://github.com/chenlong220192/mingsha-jenkins-plugin-build-metrics/issues)
