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
 * [Git](https://git-scm.com/)

### Execução

```bash
# Clone o repositório: 
git clone https://github.com/jvfranco/app-wishlist.git

# Acesse o diretório raiz da aplicação e execute o comando abaixo para gerar um .jar atualizado:
mvn clean install

# Ainda no diretório raiz da aplicação e execute o comando abaixo para gerar a imagem Docker da aplicação e uma imagem da instância do MongoDB:
docker-compose build

# Após o build da aplicação, executar o comando abaixo para criar os containers:
docker-compose up

# Acesse a documentação da API:
http://localhost:8080/swagger-ui/index.html#/
```
<p>Observação: Durante a execução da aplicação serão instanciados 3 produtos com ids 1, 2 e 3 respectivamente. Eles poderão
ser usados na criação de uma nova Lista de Desejos, informando um id aleatório para o cliente e um dos três citados
anteriormente para o produto.</p>

### Detalhes Técnicos

* Java 17
* Java Record
* Lombok
* MongoDB
* JUnit 5
* BDDMockito