# Stage 1 — build com Node 24.3.0 (igual ao seu ambiente local)
FROM node:24.3.0-alpine AS build

WORKDIR /app

# Copia apenas os manifests primeiro para aproveitar cache
COPY package*.json ./

# Instala dependências de forma reprodutível
RUN npm ci --silent

# Copia todo o código
COPY . .

# Build em modo production, força saída em /app/dist
RUN npm run build -- --configuration=production --output-path=dist --source-map=false

# Stage 2 — runtime com nginx
FROM nginx:stable-alpine AS production

# Remove conteúdo default do nginx
RUN rm -rf /usr/share/nginx/html/*

# Copia arquivos do build — cobre o caso dist/<appname>/...
# (isso copia todo conteúdo das subpastas de /app/dist para /usr/share/nginx/html)
COPY --from=build /app/dist/* /usr/share/nginx/html/

# Copia seu nginx.conf customizado (certifique-se de ter criado nginx.conf na raiz)
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Garante que o usuário nginx consiga ler os arquivos
RUN chown -R nginx:nginx /usr/share/nginx/html \
 && chmod -R a+rX /usr/share/nginx/html

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
