#!/bin/bash
cert_folder="/app/cert"
cert_file="jssecacerts"
echo "$CMAN_CERT"
echo "$CMAN_CERT" >> $cert_folder/cman.crt
certSecret=$CERT_SECRET
echo "The certSecret is $certSecret"
mkdir -p $cert_folder
#The DB_SECRET is a secret that you have to set in the environment variables of the container, it is stored in github secrets and created manually and is not linked to the Database credentials.
generate_cert() {
  keytool -import -alias "${DB_HOST}" -keystore $cert_folder/$cert_file -file "$cert_folder/cman.crt" -storepass "${certSecret}" -noprompt || exit 1
}

if [ "$(ls -A $cert_folder)" ]; then
  echo "The $cert_folder folder is not empty."
  if [ -e "$cert_folder/$cert_file" ]; then
    echo "The "$cert_folder/$cert_file" certificate file is present."
  else
    generate_cert
  fi
else
  generate_cert
fi
