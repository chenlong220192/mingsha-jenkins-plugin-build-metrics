#======================================================================
#
# example 'make init'
# example 'make package SKIP_TEST=true ENV=dev'
#
# author: chenlong
# date: 2023-09-27
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
	$(BASE_PATH)/mvnw --batch-mode --errors -f ${BASE_PATH}/pom.xml clean

#
test:
	$(BASE_PATH)/mvnw --batch-mode --errors -f ${BASE_PATH}/pom.xml clean test -D test=*Test -DfailIfNoTests=false

#
package:
	$(BASE_PATH)/mvnw --batch-mode --errors --fail-at-end --update-snapshots -f ${BASE_PATH}/pom.xml clean package -D maven.test.skip=$(SKIP_TEST)
# ----------------------------- maven >-----------------------------
