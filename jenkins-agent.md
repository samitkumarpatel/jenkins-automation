## jenkins-agent

To create a jenkins agent by using jenkins CLI , follow below command's:

```sh
java -jar jenkins-cli.jar -s http://localhost:8080 create-node < node.xml
```

make sure , you have the valid xml with valid xml tag with value. An example looks like below:

```xml
<slave>
  <name>terraform</name>
  <description></description>
  <remoteFS>/var/jenkins_home/terraform</remoteFS>
  <numExecutors>1</numExecutors>
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

Once this command is success, you can see in Jenkins that the node are created.

### Activate the node on the respective machine or vm or on a docker image, follow below command:

Download agent.jar

```sh
curl -sO http://localhost:8080/jnlpJars/agent.jar
```

Get the necessary credential to be used for connecting during the agent connection establishment.

```sh
curl -L -s -u user:user123 -H "Jenkins-Crumb:dac7ce5614f8cb32a6ce75153aaf2398" -X GET http://localhost:8080/computer/docker/slave-agent.jnlp | sed "s/.*<jnlp><application-desc><argument>\([a-z0-9]*\).*/\1/"
```
> Note : The Header `Jenkins-Crumb:dac7ce5614f8cb32a6ce75153aaf2398` can be any like `Jenkins-Crumb:1` or `Jenkins-Crumb:abc123`

Finally run this command in a non-diteched mode 

```sh
java -jar agent.jar -url http://localhost:8080/ -secret c4ea3636f9c8464cf59fbc68a534ab8510734007de4953a1ee225c62a0c0bb71 -name terraform -webSocket -workDir "/var/jenkins_home/terraform"
```

OR follow the below command

> Note : If you wanted to know what are the supported and valid parameter for agent.jar it can be found by executing `java -jar agent.jar` command. 

```sh
echo c4ea3636f9c8464cf59fbc68a534ab8510734007de4953a1ee225c62a0c0bb71 > secret-file;curl -sO http://localhost:8080/jnlpJars/agent.jar;java -jar agent.jar -url http://localhost:8080/ -secret @secret-file -name terraform -webSocket -workDir "/var/jenkins_home/terraform"

```
OR

You can save the jnlp file to a file and give that as an input to agent.jar, like below:

```sh
curl -L -s -u user:user123 -H "Jenkins-Crumb:a" -X GET http://localhost:8080/computer/docker/slave-agent.jnlp -o slave-agent.jnlp
```

While running the agent.jar , make sure pass you supply the file this way:

```sh
java -jar agent.jar -jnlpUrl file://$(pwd)/slave-agent.jnlp
```

Then you are done!
