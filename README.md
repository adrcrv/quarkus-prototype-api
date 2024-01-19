## 🤖 quarkus-prototype-api
This project uses Quarkus, the Supersonic Subatomic Java Framework.  
If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

This API empowers to encrypt plaintext messages securely using both RSA and AES algorithms. Easily crypt plaintext messages, store them, and retrieve them later, ensuring robust protection for valuable information.

---
### 🔍 Features
- RSA Encryption: Employ the RSA algorithm for secure asymmetric encryption.
- AES Encryption: Utilize the AES algorithm for efficient symmetric encryption. Used to protect the private key

---

### 📋 Requirements
[Docker (v24.0+)](https://get.docker.com/)  
[OpenJDK (v17.1+)](https://sdkman.io/jdks/)  

_Docker is the only requirement for tests, checks, and runtime._  
_For development purposes, OpenJDK can be easily provided by [SdkMan](https://sdkman.io/jdks/)._  

---

### 🛠️ Running in Dev Mode

Run the application in dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

_Quarkus now ships with a Dev UI, which is available in dev mode only at http://localhost:8080/q/dev/__

---

### 🚀 Packaging and Running
The application can be packaged using:
```shell script
./mvnw package
```

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

_Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory._

---

For _über-jar_ build, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

---

### 🐳 Running in Container
The application runtime can be set up by:
```shell script
# Build Image
docker build -f src/main/docker/Dockerfile.jvm -t quarkus-prototype-api-jvm .

# Run Tests
docker run -it --rm quarkus-prototype-api-jvm
```

_Now API is available at http://localhost:8080/q/swagger-ui/_

---

The tests can also be executed and validated using:
```shell script
# Build Image
docker build -f src/main/docker/Dockerfile.test -t quarkus-prototype-api-test .

# Run Tests
docker run -it --rm quarkus-prototype-api-test
```

---

### 📄 API Documentation

- Swagger Documentation: The API is documented using Swagger. Access the Swagger UI at http://localhost:8080/q/swagger-ui to explore and test the API.  

- Postman Collection: A Postman collection is available for convenient API testing. Download the collection from `/dev/postman-collection.json` and import it into Postman workspace.

---

### 📜 License
This project is licensed under the MIT License.

---

### ✍️ Author
Made with ❤️. Get in Touch!

[![LinkedIn Badge](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/adrcrv/)