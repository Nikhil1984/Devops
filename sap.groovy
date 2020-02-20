def abap_unit(LABEL,HOST,CREDENTIAL,PACKAGE,COVERAGE,OBJECT) {	
	def CLAS = ' '
	println "LABEL=" + LABEL
	println "HOST=" + HOST
	println "CREDENTIAL=" + CREDENTIAL
	println "PACKAGE=" + PACKAGE 
	println "COVERAGE=" + COVERAGE
	
	withCredentials([usernamePassword(credentialsId: 'NPL', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
		stage( 'ABAP Unit Test') {
			dir('sap-pipeline') { def count = 0
					OBJECT.each{ def type = OBJECT[count].split( )
					def prog = type[1].split('.asx.xml')	    
			                if ( type[0] == 'DEVC' ){ PACKAGE = prog[0] 
								  println "PACKAGE=" + PACKAGE 
				bat "newman run abap_unit_coverage_pack.postman_collection.json --insecure --bail " +
				"--environment NPL.postman_environment.json " +
				"--timeout-request 120000 " +
				"--global-var host=$HOST " +
				"--global-var username=$USERNAME " +
				"--global-var password=$PASSWORD " +
                 		"--global-var package=$PACKAGE " + 
			        "--global-var coverage_min=$COVERAGE " }

			        if ( type[0] == 'CLAS' ){ CLAS = prog[0]
								  println "CLASS=" + CLAS 
				bat "newman run abap_unit_coverage_clas.postman_collection.json --insecure --bail " +
				"--environment NPL.postman_environment.json " +
				"--timeout-request 120000 " +
				"--global-var host=$HOST " +
				"--global-var username=$USERNAME " +
				"--global-var password=$PASSWORD " +
                 		"--global-var clas=$CLAS " + 
			        "--global-var coverage_min=$COVERAGE " } 
			                count = count + 1

			}
		}
	}
} 	

def abap_sci(LABEL,HOST,CREDENTIAL,PACKAGE,VARIANT,OBJECT) {	
	def CLAS = ' '
	println "LABEL=" + LABEL
	println "HOST=" + HOST
	println "CREDENTIAL=" + CREDENTIAL
	println "PACKAGE=" + PACKAGE
	println "VARIANT=" + VARIANT

	withCredentials([usernamePassword(credentialsId: 'NPL', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {	
		stage('ABAP Code Inpector') {
			dir('sap-pipeline') { def count = 0
					OBJECT.each{ def type = OBJECT[count].split( )
					def prog = type[1].split('.asx.xml')	    
			                if ( type[0] == 'DEVC' ){ PACKAGE = prog[0] 
								  println "PACKAGE=" + PACKAGE 
					bat "newman run abap_scipack.postman_collection.json --insecure --bail " +
					"--environment NPL.postman_environment.json " +
					"--timeout-request 120000 " +
					"--global-var host=$HOST " +
					"--global-var username=$USERNAME " +
					"--global-var password=$PASSWORD " +
					"--global-var package=$PACKAGE " +
					"--global-var atc_variant=$VARIANT " }

			                if ( type[0] == 'CLAS' ){ CLAS = prog[0]
								  println "CLASS=" + CLAS 
					bat "newman run abap_sciclas.postman_collection.json --insecure --bail " +
					"--environment NPL.postman_environment.json " +
					"--timeout-request 120000 " +
					"--global-var host=$HOST " +
					"--global-var username=$USERNAME " +
					"--global-var password=$PASSWORD " +
					"--global-var clas=$CLAS " +
					"--global-var atc_variant=$VARIANT " } 
			                count = count + 1
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
