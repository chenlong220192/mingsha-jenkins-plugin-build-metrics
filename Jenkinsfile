pipeline {
    agent { label 'linux' }
    environment {
        // 邮件通知相关
        EMAIL_RECIPIENTS = "your@email.com" // 直接填写收件人，多个用逗号分隔
        EMAIL_SUBJECT_SUCCESS = "✅ Jenkins插件构建成功"
        EMAIL_SUBJECT_FAILURE = "❌ Jenkins插件构建失败"
        EMAIL_BODY_SUCCESS = """
            <h2 style='color:green;'>✅ Jenkins插件构建成功</h2>
            <p>🎉 恭喜，插件已成功构建并归档！</p>
            <ul>
                <li><b>项目名称：</b> mingsha-jenkins-plugin-build-metrics</li>
                <li><b>构建编号：</b> ${BUILD_NUMBER}</li>
                <li><b>分支：</b> ${BRANCH_NAME}</li>
                <li><b>触发人：</b> ${BUILD_USER}</li>
                <li><b>构建时间：</b> ${BUILD_TIMESTAMP}</li>
            </ul>
            <p>📦 <a href='${BUILD_URL}artifact/target/'>点击下载最新制品包</a></p>
            <p>🔗 <a href='${BUILD_URL}'>查看本次构建详情</a></p>
            <hr/>
            <p style='color:#888;'>本邮件由 Jenkins 自动发送，请勿回复。</p>
        """
        EMAIL_BODY_FAILURE = """
            <h2 style='color:red;'>❌ Jenkins插件构建失败</h2>
            <p>😢 很遗憾，插件构建未通过，请及时关注并处理。</p>
            <ul>
                <li><b>项目名称：</b> mingsha-jenkins-plugin-build-metrics</li>
                <li><b>构建编号：</b> ${BUILD_NUMBER}</li>
                <li><b>分支：</b> ${BRANCH_NAME}</li>
                <li><b>触发人：</b> ${BUILD_USER}</li>
                <li><b>构建时间：</b> ${BUILD_TIMESTAMP}</li>
            </ul>
            <p>🔗 <a href='${BUILD_URL}'>查看失败详情</a></p>
            <hr/>
            <p style='color:#888;'>本邮件由 Jenkins 自动发送，请勿回复。</p>
        """
        // Maven 构建相关
        MAVEN_OPTS       = "-Dmaven.test.skip=true"
        MVN_CMD          = "./mvnw"
        ARTIFACT_PATTERN = "target/*.hpi"
        // 归档历史数
        ARCHIVE_HISTORY  = 1
    }
    tools {
        jdk 'jdk17'
    }
    stages {
        stage('构建') {
            steps {
                echo "\u001B[34m🚀 开始构建 Jenkins 插件包...\u001B[0m"
                withEnv(["JAVA_HOME=${tool 'jdk17'}", "PATH=${tool 'jdk17'}/bin:$PATH"]) {
                    sh "java -version"
                    sh "${MVN_CMD} clean package ${MAVEN_OPTS}"
                    sh "java -version"
                }
            }
        }
        stage('归档制品') {
            steps {
                echo "\u001B[32m📦 归档 HPI 制品包...\u001B[0m"
                archiveArtifacts artifacts: "${ARTIFACT_PATTERN}", onlyIfSuccessful: true, fingerprint: true, allowEmptyArchive: false
                // 只保留一次历史
                script {
                    def builds = currentBuild.rawBuild.getParent().getBuilds()
                    if (builds.size() > env.ARCHIVE_HISTORY.toInteger()) {
                        for (int i = env.ARCHIVE_HISTORY.toInteger(); i < builds.size(); i++) {
                            builds.get(i).deleteArtifacts()
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            echo "\u001B[36m🧹 清理源码...\u001B[0m"
            cleanWs()
        }
        success {
            echo "\u001B[32m✅ 构建成功，发送成功邮件通知...\u001B[0m"
            mail to: "${EMAIL_RECIPIENTS}", subject: "${EMAIL_SUBJECT_SUCCESS}", body: "${EMAIL_BODY_SUCCESS}", mimeType: 'text/html'
        }
        failure {
            echo "\u001B[31m❌ 构建失败，发送失败邮件通知...\u001B[0m"
            mail to: "${EMAIL_RECIPIENTS}", subject: "${EMAIL_SUBJECT_FAILURE}", body: "${EMAIL_BODY_FAILURE}", mimeType: 'text/html'
        }
    }
} 