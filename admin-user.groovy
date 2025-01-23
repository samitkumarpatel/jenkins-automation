import jenkins.model.*
import hudson.security.*

import java.util.logging.Level
import java.util.logging.Logger

def logger = Logger.getLogger("2.user.groovy")

def userName = "root"
def instance = Jenkins.getInstance()
def randText = new Random().with {(1..9).collect {(('a'..'z')).join()[ nextInt((('a'..'z')).join().length())]}.join()}
println "******************************************"
println randText
println "******************************************"

new File(System.getenv("JENKINS_HOME")+"/secrets/rootPassword").write randText
def hudsonRealm = new HudsonPrivateSecurityRealm(false)
hudsonRealm.createAccount(userName,randText)
instance.setSecurityRealm(hudsonRealm)

//FullControlOnceLoggedInAuthorizationStrategy 
def strategy = new hudson.security.FullControlOnceLoggedInAuthorizationStrategy()
strategy.setAllowAnonymousRead(false)
instance.setAuthorizationStrategy(strategy)

logger.log(Level.INFO,"***********************USER CREATION DONE!**************************")
instance.save()
