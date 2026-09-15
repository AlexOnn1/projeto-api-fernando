# Atividade Prática Integrada — Back-End Frameworks
## Projeto: API de Cursos

Este projeto consiste no desenvolvimento progressivo de uma **API RESTful** para gerenciamento de cursos, construída ao longo das Aulas 01 a 05 com as tecnologias:
- **Java 21**
- **Spring Boot 3**
- **Spring Data JPA**
- **PostgreSQL**

---

## Aula 01 — Introdução ao Back-End e Frameworks

### Atividade 01 — Fluxo Cliente e Servidor

Quando um cliente realiza uma consulta `GET /cursos`, ocorre um ciclo completo de comunicação baseado no protocolo HTTP (Hypertext Transfer Protocol):

```
Cliente -> requisição HTTP (Request) -> Back-End (Processamento) -> resposta HTTP (Response) -> Cliente
```

#### Detalhamento dos Componentes e Etapas:
1. **Cliente:** É o agente emissor da solicitação. Pode ser um navegador web, uma aplicação mobile, uma interface frontend (React/Angular/Vue) ou ferramentas de teste como Postman/Insomnia/cURL. O cliente prepara e dispara a **requisição HTTP (Request)** com o método `GET` e a URL de destino `http://localhost:8080/cursos`.
2. **Requisição HTTP (Request):** Contém os cabeçalhos (headers como `Accept: application/json`), o método `GET`, a URL de destino e não possui corpo (body), pois trata-se de uma leitura.
3. **Servidor / Back-End (Processamento):** A aplicação Spring Boot em execução escuta as requisições na porta `8080`. Ao receber a requisição, o servidor:
   - Identifica a rota e o verbo HTTP através do controlador (`CursoController`);
   - Executa o **processamento** interno, acionando as camadas de serviço e dados para recuperar a lista de cursos cadastrados;
   - Serializa os objetos Java recuperados para o formato JSON.
4. **Resposta HTTP (Response):** O servidor gera a resposta com o código de status HTTP adequado (`200 OK`), cabeçalho `Content-Type: application/json` e o corpo contendo a lista dos cursos em JSON (`[{"id": 1, "nome": "...", "cargaHoraria": 60}, ...]`).
5. **Retorno ao Cliente:** O cliente recebe o pacote de resposta HTTP, encerra o ciclo e pode exibir os dados da lista de cursos para o usuário final.

---

### Atividade 02 — Responsabilidades do Back-End

Ao receber uma requisição para cadastrar um novo curso (`POST /cursos`), o back-end não funciona meramente como um intermediário visual ou repassador de dados. Ele executa uma sequência rigorosa de etapas de processamento e governança:

1. **Receber os dados enviados pelo cliente:** O servidor acolhe a requisição HTTP com o payload JSON contendo as informações enviadas pelo cliente.
2. **Transformar os dados em um objeto Java:** O framework realiza a desserialização do JSON recebido no corpo da requisição (`@RequestBody`) para uma instância da classe Java `Curso` (Model).
3. **Verificar as regras do curso (Regras de Negócio):** O back-end aplica a lógica de negócio na camada de serviço (`CursoService`), validando:
   - Se o campo `nome` não é nulo nem está vazio/em branco;
   - Se a `cargaHoraria` é um valor estritamente maior que zero.
   Caso alguma regra seja violada, a operação é interrompida com status de erro informativo apropriado.
4. **Salvar o curso:** Uma vez válidos os dados, o back-end gera o identificador automático e persiste a nova entidade de forma segura no repositório de dados.
5. **Devolver uma resposta ao cliente:** O back-end empacota o curso recém-criado (agora contendo seu `id` gerado) em formato JSON e o devolve ao cliente com o código HTTP `201 Created` e cabeçalhos correspondentes.

---

### Atividade 03 — Contrato Inicial da API

A API disponibiliza cinco endpoints principais para atender ao CRUD completo do recurso **Curso**:

| Método HTTP | Endpoint (URL) | Operação | Descrição | Corpo da Requisição (Body) | Resposta Esperada (Status & Body) |
|---|---|---|---|---|---|
| **GET** | `/cursos` | Listar todos os cursos | Retorna a coleção completa de cursos cadastrados. | *Nenhum* | `200 OK`<br>`[{"id": 1, "nome": "Java 21", "cargaHoraria": 40}]` |
| **GET** | `/cursos/{id}` | Buscar curso por ID | Retorna os dados detalhados do curso correspondente ao ID informado. | *Nenhum* | `200 OK` (se encontrado):<br>`{"id": 1, "nome": "Java 21", "cargaHoraria": 40}`<br>`404 Not Found` (se não existir) |
| **POST** | `/cursos` | Cadastrar novo curso | Recebe os dados de um curso, valida as regras de negócio e persiste. | `{"nome": "Back-End Frameworks", "cargaHoraria": 60}` | `201 Created`<br>`{"id": 1, "nome": "Back-End Frameworks", "cargaHoraria": 60}` |
| **PUT** | `/cursos/{id}` | Atualizar curso | Atualiza os dados de um curso existente pelo seu ID. | `{"nome": "Back-End Avançado", "cargaHoraria": 80}` | `200 OK`<br>`{"id": 1, "nome": "Back-End Avançado", "cargaHoraria": 80}`<br>`404 Not Found` (se ID não existir) |
| **DELETE** | `/cursos/{id}` | Remover curso | Exclui permanentemente o curso correspondente ao ID informado. | *Nenhum* | `204 No Content` (sucesso, sem corpo)<br>`404 Not Found` (se ID não existir) |

#### Exemplo de JSON para Cadastro/Atualização:
```json
{
  "nome": "Back-End Frameworks",
  "cargaHoraria": 60
}
```

---

## Aula 02 — Criação da Aplicação Spring Boot

### Atividade 04 — Criação e Inicialização do Projeto
O projeto foi estruturado com as seguintes especificações recomendadas:
- **Ferramenta de Build:** Maven com Maven Wrapper (`mvnw` e `mvnw.cmd`)
- **Linguagem:** Java 21 LTS
- **Empacotamento:** JAR
- **Dependência Principal:** Spring Web (`spring-boot-starter-web`)
- **Group ID:** `br.edu.nassau`
- **Artifact ID:** `api-cursos`
- **Classe Principal:** `br.edu.nassau.apicursos.ApiCursosApplication`

---

### Atividade 05 — Estrutura e Configuração do Projeto

Abaixo está a descrição e a finalidade de cada item estrutural do projeto:

1. **`pom.xml` (Project Object Model):**
   - É o arquivo central de configuração do Maven.
   - Declara as informações do projeto (Group ID, Artifact ID, versão), herança do Spring Boot Starter Parent, versão do Java (`21`), gerenciamento de dependências (`spring-boot-starter-web`, `spring-boot-starter-test`) e plugins de compilação e empacotamento (`spring-boot-maven-plugin`).

2. **`src/main/java`:**
   - Diretório raiz onde reside todo o código-fonte Java da aplicação.
   - Abriga a árvore de pacotes (`br.edu.nassau.apicursos`) e todas as classes que formam a lógica de negócio, controladores, serviços, modelos e repositórios.

3. **`src/main/resources`:**
   - Diretório destinado aos arquivos de configuração estáticos e recursos não compilados da aplicação.
   - Contém arquivos de propriedades de configuração (`application.properties`), templates, schemas de banco ou arquivos de inicialização (`schema.sql`, `data.sql`).

4. **Classe com `@SpringBootApplication` (`ApiCursosApplication.java`):**
   - É o ponto de entrada (*entry point*) da aplicação.
   - A anotação `@SpringBootApplication` combina três recursos essenciais do Spring:
     - `@Configuration`: Marca a classe como fonte de definições de beans.
     - `@EnableAutoConfiguration`: Habilita o mecanismo de autoconfiguração inteligente do Spring Boot.
     - `@ComponentScan`: Escaneia automaticamente o pacote atual e seus subpacotes em busca de componentes gerenciados (`@Component`, `@Service`, `@RestController`, `@Repository`).
   - Contém o método `public static void main(String[] args)` que invoca `SpringApplication.run(ApiCursosApplication.class, args)`.

5. **`application.properties`:**
   - Arquivo de propriedades em chave-valor utilizado para parametrizar o comportamento da aplicação em tempo de execução.
   - Configurado especificamente para esta atividade com:
     ```properties
     spring.application.name=api-cursos
     server.port=8080
     ```
   - Garante que a aplicação se identifique como `api-cursos` e que o servidor web embutido (Tomcat) atenda na porta `8080`.

---

### Atividade 06 — Beans e Injeção de Dependência

Para demonstrar os conceitos de **Inversão de Controle (IoC)** e **Injeção de Dependência (DI)** do Spring Framework:

1. **`CursoService`:**
   - Anotado com `@Service`, tornando-o um componente Spring (Bean) gerenciado no ApplicationContext.
   - Implementa o método `obterMensagem()`, responsável por produzir o conteúdo textual da resposta.
2. **`CursoController`:**
   - Anotado com `@RestController` e mapeado na rota base `@RequestMapping("/cursos")`.
   - **Injeção pelo Construtor:** Recebe a instância gerenciada de `CursoService` diretamente em seu construtor público:
     ```java
     public CursoController(CursoService cursoService) {
         this.cursoService = cursoService;
     }
     ```
   - **Regra Atendida:** O controller **jamais** instancia o serviço com operador `new CursoService()`, delegando integralmente o ciclo de vida ao container IoC do Spring.
3. **Endpoint Temporário:**
   - `GET /cursos/mensagem`: Retorna a mensagem gerada dinamicamente pelo `CursoService`.
   - Exemplo de resposta HTTP (Status `200 OK`):
     ```
     Servico de cursos ativo! Mensagem produzida pelo CursoService e gerenciada pelo Spring.
     ```

---

## Aula 03 — HTTP e APIs REST

### Atividade 07 — Model e Conversão JSON
Foi implementada a classe de domínio `Curso` contendo os atributos essenciais:
- `private Long id;`
- `private String nome;`
- `private Integer cargaHoraria;`
- Construtor padrão (sem argumentos), construtores parametrizados, getters e setters.
- Integração com a anotação `@RequestBody` no método de cadastro do `CursoController`, permitindo que o Jackson (conversor JSON padrão do Spring) desserialize a carga útil HTTP em uma instância Java e serialize a resposta Java de volta para formato JSON.

---

### Atividade 08 — CRUD em Memória
Para viabilizar as operações da API antes da integração com banco de dados, foi implementado o armazenamento em memória na camada de serviço (`CursoService`) utilizando `List<Curso>` com controle sequencial de chaves primárias via `AtomicLong`:
1. **Cadastrar (`POST /cursos`):** Atribui o próximo ID gerado em sequência, insere na lista e retorna o curso criado com status `201 Created`.
2. **Listar (`GET /cursos`):** Retorna uma cópia da lista com todos os cursos e status `200 OK`.
3. **Buscar por ID (`GET /cursos/{id}`):** Filtra a lista pelo ID. Retorna `200 OK` caso exista ou `404 Not Found` caso inexistente.
4. **Atualizar (`PUT /cursos/{id}`):** Localiza o curso correspondente, atualiza `nome` e `cargaHoraria`, devolvendo `200 OK`, ou `404 Not Found` se o curso não existir.
5. **Remover (`DELETE /cursos/{id}`):** Exclui o curso pelo ID, retornando `204 No Content` caso encontrado e removido, ou `404 Not Found` caso contrário.

---

### Atividade 09 — Testes HTTP e Validação de Endpoints

Os testes de requisição e resposta foram estruturados em dois formatos práticos prontos para uso:
1. **Coleção Postman / Insomnia:** Disponível no arquivo `testes/api-cursos.postman_collection.json` (basta importar diretamente no Postman ou Insomnia).
2. **Arquivo de Requisições HTTP:** Disponível no arquivo `testes/requisicoes.http` (compatível com a extensão REST Client do VS Code e IntelliJ IDEA).

#### Matriz de Evidências dos Testes HTTP:

| Operação | Método | URL | Body Enviado | Resposta Obtida | Status HTTP |
|---|---|---|---|---|---|
| Listar cursos (início) | `GET` | `/cursos` | *Nenhum* | `[]` | `200 OK` |
| Cadastrar curso 1 | `POST` | `/cursos` | `{"nome": "Back-End Frameworks", "cargaHoraria": 60}` | `{"id": 1, "nome": "Back-End Frameworks", "cargaHoraria": 60}` | `201 Created` |
| Cadastrar curso 2 | `POST` | `/cursos` | `{"nome": "Banco de Dados PostgreSQL", "cargaHoraria": 40}` | `{"id": 2, "nome": "Banco de Dados PostgreSQL", "cargaHoraria": 40}` | `201 Created` |
| Buscar curso por ID | `GET` | `/cursos/1` | *Nenhum* | `{"id": 1, "nome": "Back-End Frameworks", "cargaHoraria": 60}` | `200 OK` |
| Buscar ID inexistente | `GET` | `/cursos/999` | *Nenhum* | *Corpo vazio* | `404 Not Found` |
| Atualizar curso | `PUT` | `/cursos/1` | `{"nome": "Back-End Frameworks Avançado", "cargaHoraria": 80}` | `{"id": 1, "nome": "Back-End Frameworks Avançado", "cargaHoraria": 80}` | `200 OK` |
| Atualizar ID inexistente | `PUT` | `/cursos/999` | `{"nome": "Fantasma", "cargaHoraria": 10}` | *Corpo vazio* | `404 Not Found` |
| Remover curso | `DELETE` | `/cursos/2` | *Nenhum* | *Sem corpo* | `204 No Content` |
| Remover ID inexistente | `DELETE` | `/cursos/999` | *Nenhum* | *Corpo vazio* | `404 Not Found` |

---

## Aula 04 — Arquitetura em Camadas

### Atividade 10 — Organização dos Pacotes
Para garantir uma separação limpa de responsabilidades e atender às boas práticas de engenharia de software e padrões corporativos, o projeto foi reorganizado nos seguintes pacotes:

```
br.edu.nassau.apicursos
├── controller
│   └── CursoController.java
├── service
│   └── CursoService.java
├── repository
│   └── CursoRepository.java
├── model
│   └── Curso.java
└── exception
    ├── RegraNegocioException.java
    └── GlobalExceptionHandler.java
```

- **`model`:** Contém as entidades e modelos de dados do domínio do negócio (`Curso`).
- **`repository`:** Camada de acesso, persistência e manipulação dos dados (`CursoRepository`).
- **`service`:** Camada central de regras de negócio, validações e orquestração de chamadas (`CursoService`).
- **`controller`:** Camada de apresentação REST que gerencia exclusivamente o protocolo HTTP (`CursoController`).
- **`exception`:** Tratamento centralizado de erros de validação e regras de negócio com `@RestControllerAdvice`.

---

### Atividade 11 — Repository em Memória
Foi criada a classe `CursoRepository` com a anotação `@Repository`.
- **Isolamento de Responsabilidade:** A `List<Curso>` e o controle de geração sequencial de IDs (`sequenceId`) foram removidos do `CursoService` e encapsulados no repositório.
- **Injeção de Dependência:** O `CursoRepository` é injetado no `CursoService` via construtor.
- **Princípio da Responsabilidade Única (SRP):**
  - O `CursoService` desconhece os detalhes de armazenamento interno (não gerencia mais listas nem IDs).
  - O `CursoController` desconhece completamente o `CursoRepository`, comunicando-se exclusivamente com a camada de serviço.

---

### Atividade 12 — Regras de Negócio no Service

O `CursoService` concentra todas as validações de domínio antes de delegar as chamadas ao repositório:
1. **Nome Obrigatório:** O campo `nome` não pode ser nulo nem conter apenas espaços em branco (`trim().isEmpty()`).
2. **Carga Horária Positiva:** O campo `cargaHoraria` deve ser estritamente maior que zero (`cargaHoraria > 0`).

#### Fluxo Unidirecional da Aplicação:
```
Cliente -> Controller -> Service -> Repository -> Dados
```

- **Papel do Controller:** Recebe os dados HTTP da requisição (`@RequestBody`, `@PathVariable`), repassa os dados ao `CursoService` e converte o resultado nos códigos de status HTTP correspondentes (`200 OK`, `201 Created`, `204 No Content`, `404 Not Found`).
- **Papel do Service:** Executa e fiscaliza as regras de negócio (`validarRegrasCurso`). Se houver inconsistência, lança `RegraNegocioException`, capturada pelo `GlobalExceptionHandler` e convertida em resposta HTTP `400 Bad Request`.
- **Papel do Repository:** Realiza as operações de leitura e escrita sobre a coleção de dados.
- **Conformidade:** Nenhuma regra de negócio reside no `Controller` ou no `Repository`.

---

## Aula 05 — Spring Data JPA e PostgreSQL

### Atividade 13 — Banco de Dados e Configuração da Conexão
1. **Dependências Configuradas no `pom.xml`:**
   - `spring-boot-starter-data-jpa`: Habilita o framework Hibernate/JPA e a infraestrutura de repositórios do Spring Data.
   - `postgresql`: Driver JDBC para comunicação nativa com o SGBD PostgreSQL.
   - `h2` (escopo de teste): Prover banco em memória isolado para suíte de testes automatizados sem impactar o banco de produção.

2. **Criação do Banco de Dados no PostgreSQL:**
   ```sql
   CREATE DATABASE api_cursos;
   ```

3. **Configuração do `src/main/resources/application.properties`:**
   ```properties
   spring.application.name=api-cursos
   server.port=8080

   spring.datasource.url=jdbc:postgresql://localhost:5432/api_cursos
   spring.datasource.username=postgres
   spring.datasource.password=postgres
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.format_sql=true
   ```

---

### Atividade 14 — Entity e JpaRepository

1. **Entidade JPA (`Curso.java`):**
   - Mapeada com `@Entity` e vinculada à tabela `cursos` com `@Table(name = "cursos")`.
   - Identificador gerado automaticamente com `@Id` e estratégia nativa de auto-incremento do PostgreSQL:
     ```java
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private Long id;
     ```
   - Campos `nome` e `cargaHoraria` definidos como não-nulos (`@Column(nullable = false)`).

2. **Interface `CursoRepository`:**
   - O repositório em memória foi substituído pela interface que estende `JpaRepository`:
     ```java
     @Repository
     public interface CursoRepository extends JpaRepository<Curso, Long> {
     }
     ```

3. **Adaptação do `CursoService`:**
   - O armazenamento anterior em `List<Curso>` foi totalmente extinto.
   - O CRUD passou a operar diretamente através dos métodos do Spring Data JPA:
     - `listar()` -> `cursoRepository.findAll()`
     - `buscarPorId(id)` -> `cursoRepository.findById(id)`
     - `cadastrar(curso)` -> `cursoRepository.save(curso)` com `curso.setId(null)`
     - `atualizar(id, curso)` -> `cursoRepository.findById(id)` seguido de `cursoRepository.save(cursoExistente)`
     - `remover(id)` -> `cursoRepository.deleteById(id)`

---

### Atividade 15 — Comprovação da Persistência

Foi executado o roteiro estrito de comprovação da persistência relacional:
1. A aplicação foi iniciada com conexão ativa ao PostgreSQL 16 na porta `5432`.
2. Foram cadastrados 3 cursos via `POST /cursos`.
3. Os registros foram validados via `GET /cursos`.
4. Os dados foram consultados diretamente no banco de dados com `psql -d api_cursos -c "SELECT * FROM cursos;"`.
5. A aplicação Spring Boot foi encerrada (`Stop-Process`), liberando a porta `8080`.
6. A aplicação foi reiniciada a partir do zero (`.\mvnw.cmd spring-boot:run`).
7. Uma nova consulta `GET /cursos` foi efetuada, comprovando que **todos os cursos cadastrados antes do reinício continuam perfeitamente disponíveis**.

O relatório completo detalhado encontra-se no arquivo [`testes/evidencias_persistencia.md`](testes/evidencias_persistencia.md).

---

## Como Executar o Projeto

### Pré-requisitos
- **Java 21 LTS**
- **PostgreSQL** em execução na porta `5432` com banco `api_cursos` (ou Docker)

### Opção 1: Subir o PostgreSQL via Docker
Na raiz do projeto, execute:
```bash
docker compose up -d
```

### Opção 2: Usar PostgreSQL já instalado localmente
Certifique-se de que o serviço do PostgreSQL está ativo e execute no `psql`:
```sql
CREATE DATABASE api_cursos;
```
*(Caso a senha do usuário `postgres` seja diferente de `postgres`, atualize a propriedade `spring.datasource.password` em `src/main/resources/application.properties`).*

### Executar a Aplicação Spring Boot
Na raiz do projeto, execute com o Maven Wrapper:
```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```
A API estará acessível em: `http://localhost:8080/cursos`

### Executar os Testes Automatizados
```bash
# Windows
.\mvnw.cmd test

# Linux / macOS
./mvnw test
```

---

## Entrega do Projeto

1. **Repositório Git e Arquivo Compactado:** Histórico de commits sequenciais cobrindo as 5 aulas e arquivo `api-cursos-projeto-completo.zip` gerado na raiz.
2. **`README.md` Completo:** Detalhamento do fluxo cliente-servidor, responsabilidades do back-end, estrutura de pacotes, contrato da API e comprovação de persistência.
3. **Coleção de Testes:**
   - [`testes/api-cursos.postman_collection.json`](testes/api-cursos.postman_collection.json) para importação no Postman ou Insomnia.
   - [`testes/requisicoes.http`](testes/requisicoes.http) para testes diretos via VS Code REST Client.
4. **Evidências de Persistência:**
   - Arquivo [`testes/evidencias_persistencia.md`](testes/evidencias_persistencia.md) contendo os logs e saídas de comandos.

---

## Checklist Final

- [x] Existem exatamente cinco endpoints principais de CRUD.
- [x] O Controller trata apenas questões HTTP.
- [x] O Service contém as regras de negócio.
- [x] O Repository realiza o acesso aos dados.
- [x] Curso está anotado como entidade JPA.
- [x] O projeto usa PostgreSQL.
- [x] Os status HTTP estão adequados às operações.
- [x] Os testes foram documentados.
- [x] Os dados permanecem depois do reinício.
