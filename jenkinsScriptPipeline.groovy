node{
    catchError{
        stage 'chekout'
        properties([pipelineTriggers([[$class: 'GitHubPushTrigger'], pollSCM('* * * * *')])])
        checkout([$class: 'GitSCM',
                  branches: [[name: '*/jenkins']],
                  extensions: [],
                  userRemoteConfigs: [[url: 'https://github.com/IvanVarabei/giftCertificateAdvanced']]])

        stage 'build'
        build job: 'project-build'

        stage 'sonar'
        build job: 'project-sonar'

        stage 'deploy'
        build job: 'project-deploy'
    }
    echo 'finished'
}