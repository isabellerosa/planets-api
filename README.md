# Planets API 

## About this project
This project uses the following technologies:
- Java 8
- Lombok
- Spring Boot, Rest, Data, Actuator
- H2 in memory database

## Run on Docker
Under the project's folder, run the following command on your terminal:

Create the docker image
```
docker build -t isabellerosa/planets-api .
```

Run in a container
```
docker container run -d -p 8080:8080 isabellerosa/planets-api
```

## Endpoints

Request schema example:
```
{
    "planet": "Earth",
    "radius": 1737.5,
    "type": "terrestrial",
    "moons": 1,
    "rotation_period": 0.9972,
    "orbital_period": 365.25
}
```
Key `planet` is required

---

| Description   | Method | URI | Req. Body? |
| ------ |:------:| :---:| :---: |
| Health check | `GET` | `/api/check/health` | no |
| Register planet | `POST` | `/api/planets` | yes |
| *Get planets (paginated) | `GET` | `/api/planets` | no |
| Get planet details  | `GET` | `/api/planets/{planet-name}` | no |
| Update record | `PUT` | `/api/planets/{planet-name}` | yes |
| Delete a record | `DELETE` | `/api/planets/{planet-name}` | no |

_(*) You may pass query params `page` and `offset` to determine what page you want to see and how many items per page, respectively, as in `/api/planets?page=0&offset=20`_


Examples:

[Get details about 'Earth'](http://192.168.99.100:8080/api/planets/Earth)

[Get the first page of planets, displaying up to 10 planets per page](http://localhost:8080/api/planets?offset=10)
