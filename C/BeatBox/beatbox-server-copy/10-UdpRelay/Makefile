# Deploy this Node.js project to the public folder
PROJECT_NAME=10-UdpRelay

#LOCATION = NFS/public/as3/node
LOCATION = /cmpt433/public/myApps/
DEPLOY_PATH= $(HOME)/$(LOCATION)/$(PROJECT_NAME)-copy

all: deploy node_install

deploy:
	@echo 'COPYING THE NODE.JS FILES TO $(DEPLOY_PATH)'
	@echo ''
	mkdir -p $(DEPLOY_PATH)
	chmod a+rwx $(DEPLOY_PATH)
	cp -r * $(DEPLOY_PATH)
	@echo 'Do not edit any files in this folder; they are copied!' > $(DEPLOY_PATH)/DO_NOT_EDIT-FILES_COPIED.txt
	@echo ''
	@echo 'NOTE: On the host, in $(DEPLOY_PATH), it is best to run: npm install'
	@echo '      Or, just run the node_install target in this makefile.'

node_install:
	@echo ''
	@echo ''
	@echo 'INSTALLING REQUIRED NODE PACKAGES'
	@echo '(This may take some time)'
	@echo ''
	cd $(DEPLOY_PATH) && npm install
