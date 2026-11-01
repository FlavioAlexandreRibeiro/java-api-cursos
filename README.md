# API de Cursos

Uma API RESTful desenvolvida com Spring Boot para gerenciamento de cursos e professores. Esta aplicação fornece endpoints completos para operações CRUD (Create, Read, Update, Delete) em um banco de dados PostgreSQL, além de funcionalidades de consulta dinâmica com filtros e paginação.

## 🚀 Funcionalidades

- **Gerenciamento de Cursos**: Operações completas de CRUD para cadastro de cursos
- **Gerenciamento de Professores**: Operações completas de CRUD para cadastro de professores
- **Relacionamento N:1**: Cada curso possui exatamente um professor associado
- **Validação de Dados**: Validação automática de campos obrigatórios, valores positivos e tamanho de campos
- **Tratamento de Exceções**: Tratamento global de erros com mensagens claras
- **Nome Único**: Garantia de unicidade de nome no cadastro de cursos e professores
- **Email Único**: Garantia de unicidade de email no cadastro de professores
- **Especialidade Única**: Garantia de unicidade de especialidade no cadastro de professores
- **DTOs**: Separação clara entre entidades de banco e objetos de transferência de dados
- **Testes Unitários**: Cobertura de testes para a camada de serviço com JUnit e Mockito
- **Paginação**: Listagem com suporte a paginação
- **Filtros Dinâmicos**: Consultas dinâmicas usando Specifications para cursos
- **Busca Parcial**: Busca case-insensitive por nome, categoria e descrição de cursos
- **Filtros de Carga Horária**: Filtragem por carga horária mínima e máxima

## 🛠️ Tecnologias Utilizadas

- Java 21
- Spring Boot 3.3.0
- Spring Data JPA
- Spring Web
- Spring Validation
- PostgreSQL
- Lombok
- Maven
- JUnit 5
- Mockito

## 📋 Pré-requisitos

- Java 21 ou superior instalado
- Maven 3.6+ instalado
- PostgreSQL 12+ instalado e rodando
- IDE de sua preferência (IntelliJ IDEA, Eclipse, VS Code, etc.)

## 🔧 Instalação e Configuração

### 1. Clone o repositório

```bash
git clone <repository-url>
cd api_cursos
```

### 2. Configure o Banco de Dados

Crie um banco de dados PostgreSQL:

```sql
CREATE DATABASE api_cursos;
```

### 3. Configure as Credenciais

Edite o arquivo `src/main/resources/application.properties` ou configure as variáveis de ambiente:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/api_cursos
spring.datasource.username=postgres
spring.datasource.password=sua_senha
```

Ou usando variáveis de ambiente:

```bash
export DB_URL=jdbc:postgresql://localhost:5432/api_cursos
export DB_USERNAME=postgres
export DB_PASSWORD=sua_senha
```

### 4. Compile o Projeto

```bash
./mvnw clean install
```

Ou usando Maven diretamente:

```bash
mvn clean install
```

## ▶️ Executando a Aplicação

### Via Maven

```bash
./mvnw spring-boot:run
```

### Via JAR

```bash
java -jar target/api_cursos-0.0.1-SNAPSHOT.jar
```

A aplicação estará disponível em: http://localhost:8080

## 📡 Endpoints da API

### Professores

#### Criar Professor

```http
POST /professores
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao.silva@email.com",
  "telefone": "11999999999",
  "especialidade": "Programação"
}
```

Response: 201 Created

```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@email.com",
  "telefone": "11999999999",
  "especialidade": "Programação"
}
```

#### Listar Todos os Professores

```http
GET /professores?page=0&size=10
```

Response: 200 OK

```json
{
  "content": [
    {
      "id": 1,
      "nome": "João Silva",
      "email": "joao.silva@email.com",
      "telefone": "11999999999",
      "especialidade": "Programação"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

#### Listar Professores com Filtros

```http
GET /professores?nome=João&email=joao.silva@email.com&telefone=11999999999&especialidade=Programação&page=0&size=10
```

Response: 200 OK

```json
{
  "content": [
    {
      "id": 1,
      "nome": "João Silva",
      "email": "joao.silva@email.com",
      "telefone": "11999999999",
      "especialidade": "Programação"
    }
  ]
}
```

#### Buscar Professor por ID

```http
GET /professores/{id}
```

Response: 200 OK

```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao.silva@email.com",
  "telefone": "11999999999",
  "especialidade": "Programação"
}
```

#### Atualizar Professor

```http
PUT /professores/{id}
Content-Type: application/json

{
  "nome": "João Silva Santos",
  "email": "joao.santos@email.com",
  "telefone": "11988888888",
  "especialidade": "Desenvolvimento Web"
}
```

Response: 200 OK

```json
{
  "id": 1,
  "nome": "João Silva Santos",
  "email": "joao.santos@email.com",
  "telefone": "11988888888",
  "especialidade": "Desenvolvimento Web"
}
```

#### Deletar Professor

```http
DELETE /professores/{id}
```

Response: 204 No Content

### Cursos

#### Criar Curso

```http
POST /cursos
Content-Type: application/json

{
  "nome": "Java para Iniciantes",
  "descricao": "Curso completo de Java para quem está começando",
  "categoria": "Programação",
  "cargaHoraria": 40,
  "professorId": 1
}
```

Response: 201 Created

```json
{
  "id": 1,
  "nome": "Java para Iniciantes",
  "descricao": "Curso completo de Java para quem está começando",
  "categoria": "Programação",
  "cargaHoraria": 40,
  "professorId": 1
}
```

#### Listar Todos os Cursos

```http
GET /cursos?page=0&size=10
```

Response: 200 OK

```json
{
  "content": [
    {
      "id": 1,
      "nome": "Java para Iniciantes",
      "descricao": "Curso completo de Java para quem está começando",
      "categoria": "Programação",
      "cargaHoraria": 40,
      "professorId": 1
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalElements": 1,
  "totalPages": 1
}
```

#### Listar Cursos com Filtros

```http
GET /cursos?nome=Java&descricao=curso&categoria=Programação&cargaHorariaMinima=30&cargaHorariaMaxima=50&professorId=1&page=0&size=10
```

Response: 200 OK

```json
{
  "content": [
    {
      "id": 1,
      "nome": "Java para Iniciantes",
      "descricao": "Curso completo de Java para quem está começando",
      "categoria": "Programação",
      "cargaHoraria": 40,
      "professorId": 1
    }
  ]
}
```

#### Buscar Curso por ID

```http
GET /cursos/{id}
```

Response: 200 OK

```json
{
  "id": 1,
  "nome": "Java para Iniciantes",
  "descricao": "Curso completo de Java para quem está começando",
  "categoria": "Programação",
  "cargaHoraria": 40,
  "professorId": 1
}
```

#### Atualizar Curso

```http
PUT /cursos/{id}
Content-Type: application/json

{
  "nome": "Java Avançado",
  "descricao": "Curso avançado de Java com Spring Boot",
  "categoria": "Programação",
  "cargaHoraria": 60,
  "professorId": 1
}
```

Response: 200 OK

```json
{
  "id": 1,
  "nome": "Java Avançado",
  "descricao": "Curso avançado de Java com Spring Boot",
  "categoria": "Programação",
  "cargaHoraria": 60,
  "professorId": 1
}
```

#### Deletar Curso

```http
DELETE /cursos/{id}
```

Response: 204 No Content

## 🧪 Testes

Para executar os testes unitários:

```bash
./mvnw test
```

Ou usando Maven:

```bash
mvn test
```

## 📄 Estrutura do Projeto

```
api_cursos/
├── src/
│   ├── main/
│   │   ├── java/com/API_de_Cursos/
│   │   │   ├── controller/     # Controladores REST
│   │   │   ├── dto/            # Data Transfer Objects
│   │   │   ├── entity/         # Entidades JPA
│   │   │   ├── exception/      # Tratamento de exceções
│   │   │   ├── repository/     # Repositórios JPA
│   │   │   ├── service/        # Lógica de negócio
│   │   │   └── specification/  # Specifications para consultas dinâmicas
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/API_de_Cursos/
│           ├── controller/     # Testes de controller
│           └── service/        # Testes unitários de serviço
├── pom.xml
└── README.md
```

## 🔍 Validações

### Professor

- **nome**: Obrigatório, máximo 100 caracteres, deve ser único
- **email**: Obrigatório, formato válido de email, deve ser único
- **telefone**: Opcional, deve conter apenas dígitos (10-15 caracteres)
- **especialidade**: Opcional, máximo 100 caracteres, deve ser único

### Curso

- **nome**: Obrigatório, máximo 100 caracteres, deve ser único
- **descricao**: Obrigatório, máximo 500 caracteres
- **categoria**: Obrigatório, máximo 100 caracteres
- **cargaHoraria**: Obrigatório, deve ser positivo
- **professorId**: Obrigatório, deve referenciar um professor existente

## ⚠️ Tratamento de Erros

A API retorna códigos de status HTTP apropriados:

- **201 Created**: Recurso criado com sucesso
- **200 OK**: Operação realizada com sucesso
- **204 No Content**: Recurso deletado com sucesso
- **400 Bad Request**: Dados inválidos ou erro de validação
- **404 Not Found**: Recurso não encontrado
- **409 Conflict**: Nome ou email já cadastrado
- **500 Internal Server Error**: Erro interno no servidor

## 📝 Coleção Postman

Uma coleção Postman está disponível no arquivo postman_collection.json para facilitar os testes dos endpoints.

Para importar:
1. Abra o Postman
2. Clique em "Import"
3. Selecione o arquivo postman_collection.json
4. A coleção será carregada com todos os endpoints organizados

## 🤝 Contribuindo

Contribuições são bem-vindas! Por favor:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona nova feature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo LICENSE para mais detalhes.

## 👤 Autor

Flavio Alexandre Ribeiro

Desenvolvido como parte do projeto de API de Cursos.

## 📧 Contato

GitHub: https://github.com/FlavioAlexandreRibeiro
LinkedIn: https://www.linkedin.com/in/flavioalexandreribeiro1986/?isSelfProfile=true

Para dúvidas ou sugestões, abra uma issue no repositório.
