def abap_atc(LABEL,HOST,CREDENTIAL,TRANSPORT,VARIANT) {	
	println "LABEL=" + LABEL
	println "HOST=" + HOST
	println "CREDENTIAL=" + CREDENTIAL
	println "TRANSPORT=" + TRANSPORT 
	println "VARIANT=" + VARIANT
	
	withCredentials([usernamePassword(credentialsId: 'NPL', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
		stage( 'ABAP TEST COCKPIT') {
			dir('sap-pipeline') {
				try{
				bat "newman run abap_stdcheck.postman_collection.json --insecure --bail " +
				"--environment NPL.postman_environment.json " +
				"--timeout-request 120000 " +
				"--global-var host=$HOST " +
				"--global-var username=$USERNAME " +
				"--global-var password=$PASSWORD " +
                 		"--global-var tran=$TRANSPORT "    +
				"--global-var atc_variant=$VARIANT " 
				}catch(e) {
					return 'FAILURE'
				}
			}
		}
	}
} 	

def abap_cva(LABEL,HOST,CREDENTIAL,TRANSPORT,VARIANT) {	
	println "LABEL=" + LABEL
	println "HOST=" + HOST
	println "CREDENTIAL=" + CREDENTIAL
	println "TRANSPORT=" + TRANSPORT 
	println "VARIANT=" + VARIANT

	withCredentials([usernamePassword(credentialsId: 'NPL', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {	
		stage('Code Vurnability Analysis') {
			dir('sap-pipeline') {
				try{
					bat "newman run abap_stdcheck.postman_collection.json --insecure --bail " +
					"--environment NPL.postman_environment.json " +
					"--timeout-request 120000 " +
					"--global-var host=$HOST " +
					"--global-var username=$USERNAME " +
					"--global-var password=$PASSWORD " +
					"--global-var package=$PACKAGE " +
					"--global-var atc_variant=$VARIANT " 
				}catch(e) {
					return 'FAILURE'
				}
			}
		}
	}
}

def abap_unit(LABEL,HOST,CREDENTIAL,TRANSPORT,VARIANT) {	
	println "LABEL=" + LABEL
	println "HOST=" + HOST
	println "CREDENTIAL=" + CREDENTIAL
	println "TRANSPORT=" + TRANSPORT 
	println "VARIANT=" + VARIANT

	withCredentials([usernamePassword(credentialsId: 'NPL', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {	
		stage('Code Vurnability Analysis') {

		stage('Code Vurnability Analysis') {
			dir('sap-pipeline') {
				try{
					bat "newman run abap_stdcheck.postman_collection.json --insecure --bail " +
					"--environment NPL.postman_environment.json " +
					"--timeout-request 120000 " +
					"--global-var host=$HOST " +
					"--global-var username=$USERNAME " +
					"--global-var password=$PASSWORD " +
					"--global-var package=$PACKAGE " +
					"--global-var atc_variant=$VARIANT " 
				}catch(e) {
					return 'FAILURE'
				}
			}
		}
	}
}

/*
def sap_api_test(LABEL,HOST,CREDENTIAL) {
	println "LABEL=" + LABEL
	println "HOST=" + HOST
	println "CREDENTIAL=" + CREDENTIAL
	
	withCredentials([usernamePassword(credentialsId: 'NPL', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
		stage('[' + LABEL + '] SAP API Tests') {
			dir('sap-pipeline') {
				try {
					bat "newman run SimpleRESTTest.postman_collection.json --insecure --bail " + 
					"--environment NPL.postman_environment.json " + 
					"--reporters junit " +
					"--timeout-request 10000 " +
					"--global-var host=$HOST " +
					"--global-var username=$USERNAME " + 
					"--global-var password=$PASSWORD "
				} catch(e) {
					return 'FAILURE'
				}
				junit 'newman/*.xml'
			}
		}
	}
} */

return this
