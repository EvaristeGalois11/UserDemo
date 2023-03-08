# UserDemo
Demo project for simple user management

### Compile
Java 19 is required to compile the project

Use the maven wrapper to compile with `./mvnw verify`

### Run
Use `podman-run.sh` to run the application and a postgres instance (podman is needed, more info on https://podman.io/)

### Use
Postgres is accessible at the 5432 port with credentials user:password

The application is accessible at the 8080 port

You can use http://localhost:8080/swagger-ui/index.html to access the swagger ui

An example of csv file is located inside the `example` directory
