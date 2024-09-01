FROM node:lts-alpine
WORKDIR /app
COPY ./frontend ./
RUN npm i
EXPOSE 8080

CMD [ "npm", "run", "serve-docker" ]