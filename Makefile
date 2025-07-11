#======================================================================
#
# example 'make package SKIP_TEST=true ENV=dev'
#
# author: mingsha
# date: 2025-07-10	
#======================================================================

SHELL := /bin/bash -o pipefail

export BASE_PATH := $(shell dirname $(realpath $(lastword $(MAKEFILE_LIST))))

# ----------------------------- variables <-----------------------------
# unit test flag
SKIP_TEST ?= false
# ----------------------------- variables >-----------------------------

# ----------------------------- maven <-----------------------------
#
clean:
	@echo -e "$(BLUE)🧹 [CLEAN]$(RESET) 执行 mvn clean..."
	$(BASE_PATH)/mvnw --batch-mode --errors -f ${BASE_PATH}/pom.xml clean

#
test:
	@echo -e "$(BLUE)🧪 [TEST]$(RESET) 执行 mvn test..."
	$(BASE_PATH)/mvnw --batch-mode --errors -f ${BASE_PATH}/pom.xml clean test -D test=*Test -DfailIfNoTests=false

#
package:
	@echo -e "$(BLUE)📦 [PACKAGE]$(RESET) 执行 mvn package..."
	$(BASE_PATH)/mvnw --batch-mode --errors --fail-at-end --update-snapshots -f ${BASE_PATH}/pom.xml clean package -D maven.test.skip=$(SKIP_TEST)
# ----------------------------- maven >-----------------------------

# 颜色定义
GREEN  := \033[0;32m
YELLOW := \033[0;33m
BLUE   := \033[0;34m
RESET  := \033[0m

.PHONY: help clean test package

help:
	@echo -e "$(BLUE)用法: make [目标]$(RESET)"
	@echo -e "$(YELLOW)可用目标:$(RESET)"
	@echo -e '  🆘  $(GREEN)help$(RESET)      显示帮助信息'
	@echo -e '  🧹  $(GREEN)clean$(RESET)     清理构建产物 (mvn clean)'
	@echo -e '  🧪  $(GREEN)test$(RESET)      执行单元测试 (mvn test)'
	@echo -e '  📦  $(GREEN)package$(RESET)   打包插件 (mvn package)，可用 SKIP_TEST=true 跳过测试'
	@echo -e "\n示例: make package SKIP_TEST=true ENV=dev"
