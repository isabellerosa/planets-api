# Planets API 

## About this project
This project uses the following technologies:
- Java 8
- Lombok
- Spring Boot, Rest, Data, Actuator
- H2 in memory database

## Run
_Note: make sure you have `Docker` installed_

Run `make` to check on the available commands.

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

[Get details about 'Earth'](http://localhost:8080/api/planets/Earth)

[Get the first page of planets, displaying up to 10 planets per page](http://localhost:8080/api/planets?offset=10)
