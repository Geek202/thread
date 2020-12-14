@Library("Forge-Libs")_

pipeline {
  agent any
  environment {
    WEBHOOK_URL = credentials('discord-webhook')
    WEBHOOK_TITLE = "Thread #${BUILD_NUMBER}"
    JENKINS_HEAD = 'https://wiki.jenkins-ci.org/download/attachments/2916393/headshot.png'
    MAVEN_USER = 'buildslave'
    MAVEN_PASS = credentials('tomthegeek-maven-password')
  }

  stages {
    stage('Notify-Build-Start') {
      steps {
        discordSend(
          title: "${WEBHOOK_TITLE} Started",
          successful: true,
          result: 'ABORTED',
          thumbnail: JENKINS_HEAD,
          webhookURL: WEBHOOK_URL
        )
      }
    }

    stage('Build') {
      steps {
        sh 'chmod +x gradlew'
        sh './gradlew --stacktrace clean build'
      }
    }

    stage('Publish') {
      steps {
        sh './gradlew --stacktrace publish'
      }
    }

	stage('Documentation') {
	  steps {
	  	sh './gradlew --stacktrace docs'
	  	publishHTML(
	  	  reportName: "Docs",
	  	  reportDir: "build/docs",
	  	  reportFiles: "index.html",
	  	  keepAll: true,
	  	  alwaysLinkToLastBuild: false,
	  	  allowMissing: false,
	  	  escapeUnderscores: false,
	  	  reportTitles: "Thread Docs"
	  	)
	  }
	}
  }

  post {
    always {
      script {
        archiveArtifacts(artifacts: 'build/libs/*.jar', fingerprint: true, onlyIfSuccessful: true, allowEmptyArchive: true)

        if (env.CHANGE_ID == null) {
          discordSend(
            title: "${WEBHOOK_TITLE} Finished ${currentBuild.currentResult}",
            description: '```\n' + getChanges(currentBuild) + '\n```',
            successful: currentBuild.resultIsBetterOrEqualTo("SUCCESS"),
            result: currentBuild.currentResult,
            thumbnail: JENKINS_HEAD,
            webhookURL: WEBHOOK_URL
          )
        }
      }
    }
  }
}
