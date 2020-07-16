#! bin/bash
# script de apoyo para la generacion de claves publica y privada para el uso de JWT

# se genera una clave privada de 4096 bytes de longitud
openssl genrsa -out rs512-4096-private.pem 4096
# convertimos la clave privada al formato PKCS#8 para que java pueda leerlo
openssl pkcs8 -topk8 -inform PEM -outform DER -in rs512-4096-private.pem -out rs512-4096-private.der -nocrypt
# se genera una clave publica a partir de la clave privada
openssl rsa -in rs512-4096-private.pem -pubout -outform DER -out rs512-4096-public.der
