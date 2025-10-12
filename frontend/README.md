# checkin-com

Descrição
---------
Projeto frontend em Angular + Bootstrap para o sistema **checkin.com**.
Este repositório contém a aplicação Angular responsável pela interface.

Pré-requisitos
--------------
- Node.js (LTS recomendado, ex: 18+)
- npm (ou yarn)
- Angular CLI (`npm install -g @angular/cli`)

Como iniciar (local)
--------------------
```bash
# criar o projeto (caso ainda não tenha criado)
ng new checkin-com --routing --style=scss --strict
cd checkin-com

# instalar dependências extras
npm install bootstrap

# rodar em modo desenvolvimento
ng serve --open
