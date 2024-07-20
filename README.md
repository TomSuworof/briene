# `<briene>`

## About project

Briene is an open-source platform for sharing your ideas with the rest of the world. We are trying to provide a place
for sharing everything you find interesting and important. There you can create own blog and follow others ones about 
everything.

## Technologies used

* Backend
  * Spring (Boot, JPA, Security)
  * Manticore search
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

Run
```bash
docker compose up
```

## Manual start guide

1. Clone this repo
2. Run backend: ```cd backend```,  adjust PostgreSQL and Manticore paths via environment variables and run ```mvn spring-boot:run```.
3. Run frontend: in [`frontend/vue.config.js`](./frontend/vue.config.js) change API path from `briene-backend` to `localhost` and run `npm run serve` in `frontend` directory

## Code analysis

### PMD

Run build. For example:
```shell
mvn clean package -D"net.bytebuddy.experimental=true" -D"maven.test.skip=true" site
```

Run PMD:
```shell
mvn pmd:pmd
```

Open `target/site/pmd.html` to see results of analysis.

### SpotBugs

Run build. For example:
```shell
mvn clean package -D"net.bytebuddy.experimental=true" -D"maven.test.skip=true" site
```

Run SpotBugs GUI to see result of analysis:
```shell
mvn com.github.spotbugs:spotbugs-maven-plugin:gui
```