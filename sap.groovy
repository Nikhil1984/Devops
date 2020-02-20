def abap_unit(LABEL,HOST,CREDENTIAL,PACKAGE,COVERAGE,OBJECT) {	
	println "LABEL=" + LABEL
	println "HOST=" + HOST
	println "CREDENTIAL=" + CREDENTIAL
	println "PACKAGE=" + PACKAGE 
	println "COVERAGE=" + COVERAGE
	
	withCredentials([usernamePassword(credentialsId: 'NPL', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
		stage( 'ABAP Unit Test') {
			dir('sap-pipeline') {
				bat "newman run abap_unit_coverage.postman_collection.json --insecure --bail " +
				"--environment NPL.postman_environment.json " +
				"--timeout-request 120000 " +
				"--global-var host=$HOST " +
				"--global-var username=$USERNAME " +
				"--global-var password=$PASSWORD " +
                 		"--global-var package=$PACKAGE " + 
				"--global-var coverage_min=$COVERAGE "
			}
		}
	}
} 	

def abap_sci(LABEL,HOST,CREDENTIAL,PACKAGE,VARIANT,OBJECT) {	
	println "LABEL=" + LABEL
	println "HOST=" + HOST
	println "CREDENTIAL=" + CREDENTIAL
	println "PACKAGE=" + PACKAGE
	println "VARIANT=" + VARIANT

	withCredentials([usernamePassword(credentialsId: 'NPL', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {	
		stage('ABAP Code Inpector') {
			dir('sap-pipeline') { def count = 0
					OBJECT.each{ def type = OBJECT[count].split( )
			                if ( type[0] == 'DEVC' ){ def prog = type[1].split('asx.xml') PACKAGE = prog[0] println PACKAGE }
					elseif ( type[0] == 'CLAS' ){  } 
			                count = count + 1
					bat "newman run abap_sci.postman_collection.json --insecure --bail " +
					"--environment NPL.postman_environment.json " +
					"--timeout-request 120000 " +
					"--global-var host=$HOST " +
					"--global-var username=$USERNAME " +
					"--global-var password=$PASSWORD " +
					"--global-var package=$PACKAGE " +
					"--global-var atc_variant=$VARIANT "
			}
		}
	}
}
}
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
}

return this
