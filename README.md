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
