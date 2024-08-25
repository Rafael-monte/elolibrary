## Documentação Elolibrary.

### O que é este projeto?

Este projeto é um teste técnico de um backend para uma aplicação que controla
emprestimos de livros, tendo como entidades:

- Usuário;
- Livro;
- Empréstimo.

### Documentação da API (Postman):

- [Endpoint de Empréstimos;](https://www.postman.com/interstellar-rocket-25956/workspace/elolibrary-api-tests/collection/16760485-7f136791-d63e-44de-95cc-f7cd91eca0f3?action=share&creator=16760485&active-environment=16760485-a8039370-7966-4674-8852-dcdbfb41e36f)
- [Endpoint de Livros;](https://www.postman.com/interstellar-rocket-25956/workspace/elolibrary-api-tests/collection/16760485-bee0b402-cbc6-4383-92b2-5db127700477?action=share&creator=16760485&active-environment=16760485-a8039370-7966-4674-8852-dcdbfb41e36f)
- [Endpoint de Usuários;](https://www.postman.com/interstellar-rocket-25956/workspace/elolibrary-api-tests/collection/16760485-bcac3eb5-55ca-46ac-87f8-99fbbe27c87f?action=share&creator=16760485&active-environment=16760485-a8039370-7966-4674-8852-dcdbfb41e36f)

### O que já foi realizado:

- ✅ Mapeamento das classes;
- ✅ Criação dos repositories;
- ✅ Criação dos services;
- ✅ Criação dos controllers;
- ✅ Validators para ISBN, telefones brasileiros e email;
- ✅ Criação e atualização de empréstimos;
- ✅ Testes unitários;
- ✅ Documentação (Postman).
- ✅  Jobs para deletar os registros inativos por um certo tempo na base (por exemplo 1 mês);
- ✅ Jobs para validar todos os dias se existem registros de empréstimos que já venceram, e consequentemente finalizar eles.

### O que não foi realizado, mas pode ser implementado futuramente:

- 📝 Autenticação com JWT (Atualmente só existem as roles definidas).