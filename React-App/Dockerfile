#docker build -t tezine/react-sample:1.0.0 .
#docker run -p 3000:80 tezine/react-sample:1.0.0
FROM node:14.13.1 as build-stage
WORKDIR /app
COPY build /app/dist/out
FROM nginx:1.19.3
COPY --from=build-stage /app/dist/out/ /usr/share/nginx/html
COPY nginx-custom.conf /etc/nginx/conf.d/default.conf
