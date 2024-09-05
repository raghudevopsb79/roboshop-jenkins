def getSecret(secret_engine, secret_name, secret_parameter) {
  withCredentials([string(credentialsId: 'vault-token', variable: 'token')]) {
    // values will be masked
    sh "vault login ${token}"
    secret = sh (
        script: "vault kv get  -format json -mount=${secret_engine} ${secret_name} | jq .data.data.${secret_parameter} |sed -e 's/\"//g'",
        returnStdout: true
    ).trim()
    return secret
  }
}