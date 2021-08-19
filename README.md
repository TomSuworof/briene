# briene

## About project

Briene is an open-source platform for sharing your ideas with the rest of the world. We are trying to provide a place
for sharing everything you find interesting and important. There you can create own blog and follow others ones about 
everything.

## Technologies used

* Backend
  * Spring Boot, JPA, Security
  * PostgreSQL
  * Lombok
  * [Flexmark](https://github.com/vsch/flexmark-java)
  * Gson

* Frontend
  * Thymeleaf
  * Bootstrap
  * [Lepture's Editor](https://github.com/lepture/editor)
  * jQuery
  * Vue.JS

## Quick start guide

1. Clone this repo
2. Create database using data from [application.yml](src/main/resources/application.yml)
   1. **Warning**: make sure you created table 't_roles' with roles itself. Here is script:
    ```postgresql
    create table t_roles (
      "id" bigint not null constraint t_roles_pkey primary key,
      "name" varchar(255)
    );
    
    alter table t_roles owner to postgres;
    
    insert into t_roles (id, name) values
        (0, 'ROLE_BLOCKED'),
        (1, 'ROLE_ADMIN'),
        (2, 'ROLE_USER');
    ```
    2. Other tables will be created automatically 

3. Run Maven build: ```mvn clean install```
4. Run [BrieneApplication](src/main/java/com/salat/briene/BrieneApplication.java)