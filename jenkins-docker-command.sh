docker volume create jenkins_home && docker run --name=jenkins --restart=on-failure -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home -e JENKINS_HOME=/var/jenkins_home jenkins/jenkins:lts
