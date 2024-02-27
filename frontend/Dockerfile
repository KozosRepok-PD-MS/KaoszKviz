FROM debian:bookworm-slim

RUN apt update && \
    apt install curl -y && \
    apt install sudo && \
    apt install apache2 -y

RUN curl -sL https://deb.nodesource.com/setup_20.x | sudo -E bash - && \
    apt update

RUN apt install nodejs -y && \
    mkdir /var/frontend

WORKDIR /var/frontend

COPY package*.json ./

RUN npm install

COPY . .

CMD apachectl -D FOREGROUND

CMD ["npm", "run", "start"]
