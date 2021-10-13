#!/bin/bash


echo "Generate key server"
keytool -genkey -alias registry -keyalg RSA -keystore registry.ks
#password: registrypw
echo "Export"
keytool -export -alias registry -keystore registry.ks -file registry.crt

echo "Generate key client"
keytool -genkey -alias client -keyalg RSA -keystore client.ks
#password: clientpw
echo "Export"
keytool -export -alias client -keystore client.ks -file client.crt

echo "Import server - enter client pw"
keytool -import -alias registry -keystore client.ts -file registry.crt
echo "Import client - enter server pw"
keytool -import -alias client -keystore registry.ts -file client.crt