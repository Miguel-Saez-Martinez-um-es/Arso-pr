# Build
mvn clean package && docker build -t ArSo/Alquileres .

# RUN

docker rm -f Alquileres || true && docker run -d -p 8080:8080 -p 4848:4848 --name Alquileres ArSo/Alquileres 