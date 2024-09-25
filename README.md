# API Rest para a Organização de Feiras
Este repositório contém os componentes da API de organização de feiras, projetada para permitir que os usuários tenham um melhor controle das suas 
feiras. A API fornece funcionalidades para cadastro de usuários, onde cada usuário poderá cadastrar seus próprios produtos, as categorias destes 
produtos e as suas feiras.

## Visão Geral
Quem nunca passou pelos problemas de: 
  - Preciso fazer a feira, mas quais são os itens que estão faltando exatamente?
  - Preciso criar a lista da feira com os produtos e as quantidades para não esquecer de comprar nada no mercado.
  - Quanto foi o meu gasto com as feiras neste mês? Ou no mês passado?
  - Quanto paguei no produto X na feira que realizei a 3 meses atrás? E quanto este produto está custando agora?

Então, buscando auxiliar na solução desses problemas, que esta API foi desenvolvida. 

Ao desenvolver a aplicação, busquei aplicar os princípios do Clean Code, utilizando nomenclaturas claras e significativas,
funções curtas e com propósito único, procurando sempre a reutilização de código e  também mantendo uma boa cobertura de testes.
Além disso, implementei o princípio do "Early Return", evitando aninhamentos desnecessários, tornando o código mais legível e de
fácil manutenção.

## Ferramentas utilizadas
  - **Java 17 / Spring Boot**: Para o desenvolvimento da API Rest;
  - **PostgreSQL**: Banco de dados relacional para o armazenamento de dados;
  - **Spring Data JPA**: Para a comunicação com o banco de dados;
  - **Spring Validation**: Para realizar a validação dos campos;
  - **Spring Security / JWT**: Para realizar a autenticação dos usuários;
  - **Java Mail Sender**: Para o envio de email;
  - **JUnit5 / Mockito**: Para a criação dos testes unitários;
  - **Docker**: Para containerização e gerenciamento de ambientes;
  - **Swagger**: Para a documentação da API;
  - **Kanban (Trello)**: Para a organização das tarefas a serem realizadas.

## Funcionalidades
  - ### Usuário:
    - `POST /users`: Cadastra um novo usuário;
    - `GET /users/details`: Mostra os dados do usuário logado;
    - `PUT /users`: Edita os dados do usuário logado;
    - `PUT /users/password`: Edita a senha do usuário logado;
    - `POST /users/email-recovery-password`: Envia email com token de recuperação de senha, caso o usuário tenha esquecido sua senha;
    - `PUT /users/recovery-password`: Altera a senha através do token enviado por email;
    - `DELETE /users`: Deleta o usuário logado.

  - ### Autenticação:
    - `POST /login`: Autenticação do usuário;
  
  - ### Categoria dos produtos:
    - `POST /categories`: Cadastra uma nova categoria para o usuário logado;
    - `GET /categories/{idCategory}`: Mostra os dados de uma categoria do usuário logado;
    - `GET /categories`: Lista todas as categorias do usuário logado;
    - `PUT /categories/{idCategory}`: Altera o nome de uma categoria do usuário logado;
    - `DELETE /categories/{idCategory}`: Deleta uma categoria do usuário logado.

  - ### Produto:
    - `POST /products`: Cadastra um novo produto para o usuário logado;
    - `GET /products/{idProduct}`: Mostra os dados de um produto do usuário logado;
    - `GET /products`: Lista todos os produtos do usuário logado;
    - `GET /products/category/{idCategory}`: Lista todos os produtos de uma categoria do usuário logado;
    - `PUT /products/{idProduct}`: Altera os dados de um produto do usuário logado;
    - `DELETE /products/{idProduct}`: Deleta um produto do usuário logado.

  - ### Feira:
    - `POST /markets`: Cadastra uma nova feira para o usuário logado;
    - `GET /markets/{idMarket}`: Mostra os dados de uma feira do usuário logado;
    - `GET /markets`: Lista todas as feiras do usuário logado;
    - `GET /markets/byRangeDate`: Lista todas as feiras, em um intervalo de datas, do usuário logado;
    - `PUT /markets/{idMarket}`: Altera os dados de uma feira do usuário logado;
    - `DELETE /markets/{idMarket}`: Deleta uma feira do usuário logado.

## Fluxo da Aplicação
1. Cadastre um novo usuário no sistema: `POST /users`
2. Faça o login no sistema com o usuário cadastrado: `POST /login`
3. Cadastre as categorias de produtos que deseja: `POST /categories`
4. Cadastre novos produtos, podendo vinculá-los a alguma categoria cadastrada: `POST /products`
5. Cadastre uma nova feira, setando os produtos e quantidades e, caso queira, alguma observação sobre a feira: `POST /markets` 
6. Ao finalizar a feira, caso o usuário queira ter um maior controle financeiro, poderá atualizar os dados da feira, adicionando os valores unitários
dos produtos e/ou o valor total da feira: `PUT /markets/{idMarket}`

## Diagrama de Entidade e Relacionamento
![Captura de Tela (190)](https://github.com/user-attachments/assets/63ca459c-10ec-47b2-bc3b-fb53e9e0efef)

## Configuração e Execução
  - ### Dependências:
    - Docker / Docker Compose
  - ### Passo-a-passo:
    1. Clone o repositório:
        ```bash
        git clone https://github.com/Dev-Vagner/minha-feira-api.git
        ```
    2. Navegue até o diretório do projeto:
        ```bash
        cd minha-feira-api
        ```
    3. Insira todas as variáveis de ambiente do arquivo .env:
       - Caso tenha dúvidas em como conseguir criar uma senha de aplicativo para o seu gmail, siga esse tutorial: https://support.google.com/accounts/answer/185833?hl=pt-BR
    4. No diretório raiz do projeto, execute o comando:
      ```bash
        docker-compose up -d
      ```
    5. Acesse os dados da aplicação:
       - Acesse a API em: `http://localhost:8080`
       - Acesse a documentação Swagger da API em: `http://localhost:8080/docs`
       - Acesse a interface gráfica do PgAdmin4 em: `http://localhost:9000`

---

**Autor:** Vagner Bruno

**Data:** Setembro de 2024
