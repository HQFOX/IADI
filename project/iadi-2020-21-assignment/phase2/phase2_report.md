# IADI Report - Grant Management System 2020/21


### Overview 
    The project went accordingly with smooth implementation, almost all of the endpoints were implemented at least at the best of our understanding without having the full inclosure of what the front-end needs.
    Controllers are responsible for receiving DTO (Data Transfer Objects), execute specific service method passing a DAO (Data Access Object) that was constructed from the DTO received. Some of the endpoints are secured using both roles and principal methods.
    The service layer is then responsible for make some crucial validations and throw errors when needed. They execute methods on the repositories.
<<<<<<< HEAD:reports/phase2.md
    The repository layer consists of the repositories that are responsible to execute queries and retrieve or save data to the database layer.
=======
    The repository layer is responsible to execute queries and retrieve or save data to the database layer.
>>>>>>> 5d9dad38b3ed07488911d3a81a8e930cc94e32a5:reports/phase2_report.md
    Bitbucket pipeline was used so our JUnit tests run and get verified. More tests and examples were made using Postman.

<br/><br/>
### Focus features:
- [X] Endpoints needed with full discrimination on API Operation and Response.
- [X] Security using roles and principal authority validation.
- [X] Multiple tests on controllers, services and repositories. On execution and security. 
- [X] Custom JPQL queries for data retrieve.
- [X] Postman tests and examples.

<br/><br/>
### Not implemented but desired:
- [ ] Service level security (instead of controller).
- [ ] Full integration test.

<br/><br/>
## Made by group 12
> César Nero - 58659 <br>
> Henrique Raposo - 57059 <br>
<<<<<<< HEAD:reports/phase2.md
> Ihor Yushchak - 53176

=======
> Ihor Yushchak - 53176
>>>>>>> 5d9dad38b3ed07488911d3a81a8e930cc94e32a5:reports/phase2_report.md
