# briene

## About project

Briene is an open-source platform for sharing your ideas with the rest of the world. We are trying to provide a place
for sharing everything you find interesting and important. There you can create own blog and follow others ones about 
everything.

## Technologies used

* Backend
  * Spring (Boot, JPA, Security)
  * Spring ElasticSearch (+ Heroku Bonsai as cloud search)
  * PostgreSQL 
  * Lombok
  * Flyway 
  * JSON Web Token 
  * Hibernate (Validator)


* Frontend
  * Vue
  * Bootstrap
  * [Vue Markdown Editor](https://github.com/code-farmer-i/vue-markdown-editor)
  * Moment
  * axios

## Quick start guide

1. Clone this repo
2. Run Maven build: ```mvn clean install```
3. Change directory to frontend: ```cd frontend```
4. Run npm install: ```npm install```
5. Run npm serve script: ```npm run serve```
6. Run [BrieneApplication](backend/src/main/java/com/salat/briene/BrieneApplication.java)
