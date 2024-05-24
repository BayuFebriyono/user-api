# user-api

Untuk menjalankan aplikasi ini pastikan sudah terinstal postgreSql pada sistem anda.
kemudian buka file application.properties pada directory <b>src/main/java/resource</b> 
ubah bagian configurasi database sesuai sistem anda

```
quarkus.datasource.db-kind=postgresql
quarkus.datasource.username=postgres
quarkus.datasource.password=secret
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/px-shredder
```



## Menjalankan Aplikasi

Jika semua setup sudah dilakukan silahkan jalankan perintah tersbut di terminal:

```shell script
./mvnw compile quarkus:dev
```
Aplikasi akan berjalan pada url http://localhost:8080

> **_NOTE:_**  Anda tidak memerlukan file migrasi database saat run aplikasi akan otomatis menyusun database sendiri. anda hanya perlu membuat satu database baru saja dengan nama sesuai yang sudah anda tuliskan pada application.properties.


## Dokumentasi Endpoint

<b>buka endpoint berikut untuk melihat endpoint yang tesedia</b>

```shell script
http://localhost:8080/q/swagger-ui/
```
