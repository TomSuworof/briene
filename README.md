# briene

## About project

Briene is an open-source platform for sharing your ideas with the rest of the world. We are trying to provide a place
for sharing everything you find interesting and important. There you can create own blog and follow others ones about 
everything.

## Technologies used

* Backend
  * Spring (Boot, JPA, Security)
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
  * jQuery

## Quick start guide

1. Clone this repo
2. Run Maven build: ```mvn clean install```
3. Change directory to frontend: ```cd frontend```
4. Run npm install: ```npm install```
5. Run npm serve script: ```npm run serve```
6. Run [BrieneApplication](backend/src/main/java/com/salat/briene/BrieneApplication.java)

## Postman API collection

[JSON Link](https://www.getpostman.com/collections/1dbdaf8d88be5621909d)

Mentioned endpoints:
* Profile
  * Log in
  * Sign Up
* Articles
  * Publish articles
  * Get my articles
  * Get article
  * Get all published articles
  * Get all published articles paginated
* Authors
  * Get published articles of authors
* Bookmarks
  * Get my bookmarks
  * Edit bookmarks
  * Is the article in bookmarks?
* Admin
  * Get users
  * Get all articles
