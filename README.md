# Jenkins-automation

To get some insperation to automate Jenkins in various way, Explore below repository:
Or search on this github account with name `jenkins`

[tool-jenkins](https://github.com/samitkumarpatel/tool-jenkins/tree/main)


There are hints and tips on other `.md` file like [jenkins-agent.md](./jenkins-agent.md) or [jenkins-cli.md](./jenkins-cli.md) for more details.

The easy way to automate are documented below.

## Automate Jenkins. 

There are various way a jenkins can be run like `jenkins.war` or `Jenkins docker image (jenkins/jenkins:lts) `

> On this steps we are using jenkins docker image and docker volume to mount the data to a volume , so that data is not gonna lost.

- **Run Jenkins**
```sh
docker volume create jenkins_home && \
docker run --name=jenkins --restart=always -p 8080:8080 -p 50000:50000 -v jenkins_home:/var/jenkins_home -e JENKINS_HOME=/var/jenkins_home jenkins/jenkins:lts
```
> Note: On the log you can find a initial password. Copy and keep it somewhere. That's something we can use in the further command.and for this password username is `admin`.

- **Install `Java`. Skip it If you have Java is already Installed on your machine.** 

```sh
wget https://download.java.net/openjdk/jdk17.0.0.1/ri/openjdk-17.0.0.1+2_linux-x64_bin.tar.gz
tar xvf openjdk-17*_bin.tar.gz
#or
unzip openjdk-17*_bin.zip
```

- **Install necessary Plugins**

*Necessary plugins needed for Jenkins can be found in different way, The easy way is to Initially install it manually on a running Jenkins instance and export it via Jenkins rest api or with jenkins-cli.jar but for this guide, We have it in `plugins.txt` file on this repository.*

```sh
#Download jenkins-cli.jar
curl -sO http://localhost:8080/jnlpJars/jenkins-cli.jar
#This command will download jenkins-cli.jar on the current folder.

#Install plugins
java -jar jenkins-cli.jar -s http://localhost:8080/ -webSocket -auth admin:<InitialPassword> install-plugin $(cat plugins.txt) -restart
``` 
*`-restart` flag on the above command is gonna restart Jenkins after plugins Installation. As we are running Jenkins as docker container , you can resume Jenkins log by running `docker log -f jenkins`*

- **Access Jenkins**
    - As we expose Jenkins port 8080 to our host, Jenkins can be accessable with [http://localhost:8080](http://localhost:8080).
    - On the screen , you can still see Jenkins `Unlock Jenkins setup screen`. Provide the Initial password you copied earlier or find the same from Jenkins log.
    - Click [Next] you can see `Customize Jenkins`. Click on the Basic setup.
    - Click [Next] click on `Skip and continue as admin` link on extrem below.
    - Click [Next] then Click `Save nd Finish` button.
    - Click Start Using Jenkins

- **Agent/Node creation**
    - Login to the agent machine ( The one you want to make as terraform agent/node)
    - Create a xml file with node(agent) name ex. docker.xml or terraform.xml
    - The file content will be like below.
    - Just and example , We are creating a terraform agent. make sure you save the below xml content with name `terraform.xml`. 
    ```xml
    <slave>
        <name>terraform</name>
        <description></description>
        <remoteFS>/tmp</remoteFS>
        <numExecutors>3</numExecutors>
        <mode>NORMAL</mode>
        <retentionStrategy class="hudson.slaves.RetentionStrategy$Always"/>
        <launcher class="hudson.slaves.JNLPLauncher">
            <workDirSettings>
            <disabled>false</disabled>
            <internalDir>remoting</internalDir>
            <failIfWorkDirIsMissing>false</failIfWorkDirIsMissing>
            </workDirSettings>
            <webSocket>false</webSocket>
        </launcher>
        <label>terraform</label>
        <nodeProperties/>
    </slave>
    ```
    - Run below command to create a agent
    ```sh
    java -jar jenkins-cli.jar -s http://localhost:8080 -auth <admin>:<token> create-node < node.xml
    ```
    > Make sure `http://localhost:8080` Url (Jenkins master Url) is correct. and `-auth` details are correct.
    
    - Get agent credential
    > The `curl` Url need to be based on agent name you have given and It has to be like http://<jenkins_host>:<jenkins_port>/computer/<agent/node name>/slave-agent.jnlp
    ```sh
    curl -L -s -u user:user123 -H "Jenkins-Crumb:a123random456number" -X GET http://localhost:8080/computer/docker/slave-agent.jnlp -O slave-agent.jnlp
    ```
    - Get agent.jar from Jenkins master
    ```sh
    curl -sO http://localhost:8080/jnlpJars/agent.jar
    ```
    > Make sure `http://localhost:8080` Url (Jenkins master Url) is correct.
    
    - Run agent , So that it will get connect with Jenkins master.
    ```sh
    java -jar agent.jar -workDir=/tmp -jnlpUrl file://$(pwd)/slave-agent.jnlp
    ```
    > `-jnlpUrl` is Depricated, find a way to use `-url` one.

    > When you run this command , make it ditachable, otherwise after you close the terminal this will be connection close.


- **Usere creation**

*under construction*

