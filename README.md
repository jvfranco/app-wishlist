## APP-WISHLIST 

<p>Um backend que contempla funcionalidades para manipulação de uma lista de desejos.
Possui como funcinalidades básicas a consulta da lista de desejo com todos os produtos,
adição de um novo produto a lista de um determinado cliente, a remoção de um produto
desta lista e a consulta que checa se determinado produto esta na lista ou não.</p>

### Recursos

- [x] Consulta lista de desejos;
- [x] Adiciona um produto na lista;
- [x] Remove um produto da lista;
- [x] Checa se determinado produto esta na lista.

### Pré-requisitos

<p>Existem alguns pré-requisitos antes da execução desta API. A máquina que rodará a aplicação 
deverá possuir:</p>

 * [OpenJDK 17](https://openjdk.java.net/projects/jdk/17/) 
 * [Docker](https://www.docker.com/) 
 * [MongoDB](https://www.mongodb.com/)
 * [Git](https://git-scm.com/)

### Execução 

```bash
# Através do Docker, baixe uma imagem do MongoDB:
docker pull mongo

# Para criação de uma instância do MongoDB execute o comando:
docker run -d --name mongodb -p 27017:27017 -e MONGO_INITDB_ROOT_USERNAME=admin -e MONGO_INITDB_ROOT_PASSWORD=123456 mongo

# Clone o repositório: 
git clone https://github.com/jvfranco/app-wishlist.git

# Acesse o diretório raiz da aplicação e execute o comando:
./mvnw package

# Ainda no diretório raiz da aplicação e execute o comando abaixo para gerar a imagem Docker:
docker build -t wishlist .

# Após o build da aplicação, executar o comando abaixo para criar o container:
docker run -d wishlist

# Acesse a documentação da API:
http://localhost:8080/swagger-ui/index.html#/
```

### Detalhes Técnicos

* Java 17
* Java Record
* Lombok
* MongoDB
* JUnit 5
* BDDMockito