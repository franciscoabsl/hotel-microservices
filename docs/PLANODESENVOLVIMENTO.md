# 📖 Documentação do Desenvolvimento (Checkin.com)

## 1. Visão Geral Arquitetural e Contratos

### A. Contrato de Segurança (API Gateway)
O **API Gateway** é o único responsável pela validação do JWT. Após a validação, ele propaga as *claims* de autorização nos headers para os serviços *downstream*:

| Header | Conteúdo | Uso |
| :--- | :--- | :--- |
| **`X-User-ID`** | O `id` do usuário autenticado. | Autorização Fina (pertencimento) e Contexto. |
| **`X-User-Roles`** | O `perfil` do usuário (`HOSPEDE`, `PROPRIETARIO`, `ADMIN`). | Autorização Role-Based (`@PreAuthorize('hasRole(...)')`). |

### B. Contrato de Comunicação Assíncrona (RabbitMQ)
O **Reservas MS** publica no tópico **`EVENTO_RESERVA`**. O payload é um evento **rico em dados** para isolar o **Notificação MS**.

#### **Modelo do Evento: `EVENTO_RESERVA`**

| Campo | Tipo | Descrição | Exemplo de Valor |
| :--- | :--- | :--- | :--- |
| **`tipo`** | String (Enum) | Define a ação: `CRIACAO`, `ALTERACAO`, `CANCELAMENTO`, `LEMBRETE`. | `"CRIACAO"` |
| `id` | UUID | ID da Reserva. | |
| `quartoId` | UUID | ID do quarto reservado. | |
| `nomeQuarto` | String | Nome do Quarto (Agregado). | |
| `checkIn`, `checkOut` | Date/Instant | Datas da reserva. | |
| `valorTotal` | BigDecimal | Valor final da reserva. | |
| **`hospedeId`** | UUID | ID do hóspede. | |
| **`nomeHospede`, `emailHospede`** | String | Dados de contato para notificação (Agregado). | |
| **`proprietarioId`** | UUID | ID do proprietário. | |
| **`nomeProprietario`, `emailProprietario`** | String | Dados de contato para notificação (Agregado). | |
| `nomePropriedade` | String | Nome da propriedade. | |
| `propriedadeId` | UUID | ID da propriedade. | |

## 2. Detalhamento dos Microsserviços

### 1. Auth-User MS

**Responsabilidade Principal:** Gerenciamento da identidade do usuário (CRUD, Autenticação e Autorização inicial).

#### **Modelo de Dados (Entidade `Usuario`)**

| Campo | Tipo | Descrição | Persistência |
| :--- | :--- | :--- | :--- |
| `id` | UUID | Chave primária. | `authuser_db` |
| `nome` | String | Nome completo do usuário. | |
| `email` | String | E-mail (único, credencial de login). | |
| `cpf` | String | CPF (único). | |
| `senha` | String | Senha criptografada. | |
| **`perfil`** | Enum | **`HOSPEDE`** / **`PROPRIETARIO`** / **`ADMIN`**. | |
| `telefone` | String | Telefone de contato. | |

#### **Rotas da API**

| Rota | Método | Descrição | Regra de Segurança |
| :--- | :--- | :--- | :--- |
| `/auth/register` | POST | Cria um novo usuário. | **PÚBLICO** |
| `/auth/login` | POST | Gera o JWT e o retorna. | **PÚBLICO** |
| `/usuario/{id}` | GET | Busca dados de um usuário. | **AUTENTICADO** e (`X-User-ID` == `{id}` **OU** `ADMIN`). |
| `/usuario/{id}` | PUT | Atualiza o perfil. | **AUTENTICADO** e (`X-User-ID` == `{id}` **OU** `ADMIN`). |

---

### 2. Propriedade MS

**Responsabilidade Principal:** Gerenciamento do domínio de propriedades e quartos.

#### **Modelos de Dados**

| Entidade | Campos Chave | Persistência |
| :--- | :--- | :--- |
| **`Propriedade`** | `id`, `nome`, **`proprietarioId`** (ID de quem registrou), `endereco`, `tipo`. | `propriedade_db` |
| **`Quarto`** | `id`, `propriedade` (FK para Propriedade), `valorDiaria`, `descricao`. | `propriedade_db` |

#### **Rotas da API**

| Rota | Método | Descrição | Regra de Segurança |
| :--- | :--- | :--- | :--- |
| `/propriedades/` | GET | Lista todas as propriedades disponíveis. | **PÚBLICO** |
| `/propriedades/` | POST | Cria uma nova propriedade. | **`PROPRIETARIO`** ou **`ADMIN`** (e `proprietarioId` deve ser `X-User-ID`). |
| `/propriedades/{id}` | GET | Detalhe de uma propriedade. | **PÚBLICO** |
| `/propriedades/{id}` | PUT/DELETE | Atualiza/Exclui propriedade. | **`PROPRIETARIO`** ou **`ADMIN`** (e `proprietarioId` da propriedade == `X-User-ID`). |
| `/propriedades/{id}/quarto` | POST | Adiciona um quarto. | **`PROPRIETARIO`** ou **`ADMIN`** (e `proprietarioId` da propriedade == `X-User-ID`). |
| `/propriedades/{id}/quarto/{qid}` | PUT/DELETE | Edita/Exclui um quarto. | **`PROPRIETARIO`** ou **`ADMIN`** (e `proprietarioId` da propriedade == `X-User-ID`). |

---

### 3. Reservas MS

**Responsabilidade Principal:** Regras de negócio de reservas, verificação de disponibilidade e orquestração de dados.

#### **Modelo de Dados (Entidade `Reserva`)**
Esta é a entidade **Rica em Dados**, armazenando todos os dados necessários para o histórico e o `EVENTO_RESERVA`.

| Campo | Tipo | Descrição | Persistência |
| :--- | :--- | :--- |:-------------|
| `id`, `quartoId`, `checkIn`, `checkOut`, `valorTotal`. | Primitivo | Dados base da reserva. | `reserva_db` |
| `hospedeId`, `nomeHospede`, `emailHospede`. | String | Dados agregados do Auth-User MS. |              |
| `proprietarioId`, `nomeProprietario`, `emailProprietario`. | String | Dados agregados do Propriedade MS. |              |
| `nomeQuarto`, `nomePropriedade`. | String | Dados agregados do Propriedade MS. |              |

#### **Rotas da API**

| Rota | Método | Descrição | Regra de Segurança |
| :--- | :--- | :--- | :--- |
| `/reservas/quarto/{id}` | POST | Cria uma nova reserva. **(Fluxo Crítico)** | **`HOSPEDE`** (Implica `hospedeId` == `X-User-ID`). |
| `/reservas/{id}` | GET | Busca reserva pelo ID. | **AUTENTICADO** (Hóspede dono, Proprietário dono do quarto, ou Admin). |
| `/reservas/{id}` | PUT/DELETE | Altera/Cancela uma reserva. | **AUTENTICADO** (Hóspede dono, Proprietário dono do quarto, ou Admin). |
| `/reservas/propriedade/{id}` | GET | Lista reservas de uma propriedade. | **`PROPRIETARIO`** ou **`ADMIN`** (e `proprietarioId` == `X-User-ID`). |

#### **Padrões de Maturidade Técnica**
* **Resiliência:** As chamadas síncronas para **Auth-User MS** e **Propriedade MS** são protegidas com **Resilience4j** (Circuit Breaker e Timeouts).
* **Agendamento:** Implementação de `@Scheduled` para verificar reservas futuras e publicar o evento `EVENTO_RESERVA` com `tipo: LEMBRETE` 7 dias antes do *check-in*.

---

### 4. Notificacao MS

**Responsabilidade Principal:** Consumir eventos e notificar usuários (e-mail via MailHog).

#### **Modelo de Dados**

* Não possui entidade de domínio. Apenas armazena logs de notificações enviadas (`notificacao_db`).

#### **Consumo de Mensageria**

| Tópico | Ação | Lógica de Consumo |
| :--- | :--- | :--- |
| **`EVENTO_RESERVA`** | Criação, Alteração, Cancelamento, Lembrete. | 1. Recebe a mensagem rica. 2. Verifica o campo `tipo`. 3. Usa os campos `emailHospede` e `emailProprietario` para enviar o e-mail adequado via **MailHog**. |

---

-Dspring.profiles.active=dev
para rodar em dev 