#!groovy

final String MASTER_BRANCH = 'master'
String gitCredentials = 'jenkins-ssh'
String stashName = 'sources'
String branchName = ''

node('master'){
nodeName = 'master'
     
      stage 'Checkout' 
                node(nodeName){
            	    stage('Checkout'){
                        checkOut( gitCredentials: gitCredentials)
                        stash name: stashName, useDefaultExcludes: false
                        
                        branchName = gitBranchName()
                        echo "Working off branch: ${branchName}"
    			    }
    			}
            ,
       stage 'Release' {\
                node(nodeName){
            	    stage('Release'){
                        deleteDir()
                        unstash stashName
                        gradlew '--version'
                        gradlew 'clean build test --stacktrace codeCoverageReport sonarqube'
                        String buildArgs = (branchName == MASTER_BRANCH)? 'release -Prelease.useAutomaticVersion=true': 'uploadArchives'
                        echo "buildArgs ==> ${buildArgs}"
                        gradlew '--version'
                        gradlew "${buildArgs}"

                        
                        step([
                            $class: 'JacocoPublisher',
                            execPattern:'**/build/jacoco/*.exec',
                            classPattern: '**/classes',
                            sourcePattern: '**/src/main/java',
                            exclusionPattern: '**/*Test.class'
                        ])
    			    }
    			}
            
        
    initGradle = 'jenkins-ws-init-gradle'
    nexusCreds = 'nexus-login'
}