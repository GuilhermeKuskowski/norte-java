# Norte – API RESTful (Domain Driven Design – Java)

**GS 2026.2 · CitizenTech · FIAP Engenharia de Software · Turma 2ESPI · Equipe Acesstech**

| Integrante | RM |
|---|---|
| Enzo de Faria Ferreira | 562448 |
| Guilherme de Paula Kuskowski | 562471 |
| Guilherme Eduardo de Lima | 566045 |

## Ficha da CitizenTech

| Campo | Norte |
|---|---|
| **Nome da CitizenTech** | Norte – seus direitos, com caminho |
| **Cidade e bairro** | São Paulo (SP) – Brasilândia, Zona Norte |
| **Cidadão beneficiado** | Morador da Brasilândia com um problema de direitos do dia a dia (trabalho, INSS, dívidas, aluguel, família, saúde, idosos e PcD, trânsito, serviços públicos) |
| **Problema, em uma frase** | Quem mais precisa perde direitos porque não sabe que lei o protege, que prazo está correndo e onde existe ajuda gratuita perto de casa. |
| **Como funciona** | A pessoa conta o problema, a triagem identifica a área e o Norte devolve um plano de ação com leis citadas, prazos com semáforo, checklist de documentos, a resposta para "preciso de advogado?" e encaminhamento para atendimento gratuito. |
| **O que cada parte faz** | **Java (este repositório):** API REST com o plano de ação · **Database Design:** banco Oracle (tabelas NRT_) · **Dynamic Programming:** triagem, priorização e rota acessível em Python · **Data Science:** previsão de resolução de reclamações · **Network:** rede do Ponto Norte · **AR/VR:** vídeo 3D da jornada · **Agile:** backlog e MVP |
| **Link do repositório** | https://github.com/GuilhermeKuskowski/norte |
| **Indicador de impacto** | Pessoas orientadas por mês e % que cumprem a primeira ação do plano dentro do prazo |
| **ODS conectados** | 1, 3, 5, 8, 10, 16 |

---

O **Norte** orienta moradores da Brasilândia (Zona Norte de São Paulo) sobre direitos do dia a dia. A pessoa conta o problema e o Norte devolve um **plano de ação** com:
- passos em ordem, cada um com prazo e semáforo;
- as leis vigentes citadas, com link oficial;
- o checklist de documentos;
- a resposta para "preciso de advogado?";
- o encaminhamento para atendimento gratuito.

Esta API implementa essa capacidade central sobre o banco Oracle da FIAP.

- **Repositório:** https://github.com/GuilhermeKuskowski/norte-java
- **Deploy (Render):** https://norte-rm562471-onrender-com.onrender.com
- **Swagger (testar os endpoints):** https://norte-rm562471-onrender-com.onrender.com/swagger-ui.html

> O plano gratuito do Render "dorme" sem uso: a primeira requisição pode levar até 1 minuto.

---

## Tecnologias e versões

| Item | Versão |
|---|---|
| Java (JDK) | 21 |
| Spring Boot | 3.3.5 (Spring Web + Spring JDBC/HikariCP) |
| Maven | 3.9+ (o IntelliJ já traz embutido) |
| Banco de dados | Oracle da FIAP (`oracle.fiap.com.br:1521:ORCL`) |
| Driver | ojdbc11 (versão gerenciada pelo Spring Boot) |
| Documentação da API | springdoc-openapi 2.6.0 (Swagger UI) |
| IDE | IntelliJ IDEA |
| Deploy | Docker + Render.com (plano gratuito) |

## Arquitetura (pacote `br.com.fiap`)

```
br.com.fiap
├── NorteApplication.java   → classe principal do Spring Boot
├── controller   → recebe as requisições HTTP (GET, POST, PUT, DELETE) e devolve JSON
├── bo           → regras de negócio (validações, semáforo, transações)
├── dao          → ConnectionFactory + DAOs com JDBC puro (PreparedStatement), um por tabela
├── model        → classes de domínio (uma por tabela usada)
├── exception    → exceções do domínio + GlobalExceptionHandler (erros em JSON)
└── config       → CorsConfig (libera o acesso de um front-end)

Fluxo: controller → bo → dao → Oracle (tabelas NRT_ do projeto de Database Design)
```

O acesso ao banco segue o padrão visto em aula: o **DAO recebe a `Connection` no construtor**, e o **BO** abre a conexão pela `ConnectionFactory`, chama os DAOs e fecha a conexão.

---

## 1. Preparar o banco (uma vez só)

No **SQL Developer**, conectado com o usuário (RM) que a API vai usar, rode com **F5** os scripts do projeto de Database Design, nesta ordem:
1. `01_ddl.sql`, que cria as 12 tabelas `NRT_`;
2. `02_dml.sql`, que insere os dados de exemplo.

## 2. Rodar localmente no IntelliJ

1. Abra a pasta do projeto no IntelliJ: **File > Open**, selecione a pasta que tem o `pom.xml` e espere o Maven baixar as dependências (barra de progresso no rodapé).
2. Confira se o projeto usa o JDK 21: **File > Project Structure > SDK**.
3. Abra `NorteApplication.java`, clique na seta verde ▶ ao lado do `main` e escolha **Modify Run Configuration...**.
4. Em **Environment variables**, cole (troque pelos seus dados):
   ```
   DB_USER=rm562448;DB_PASSWORD=SuaSenhaDoOracle
   ```
   O `DB_URL` é opcional, porque o padrão já é `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL`.
5. Rode o projeto. Quando aparecer `Started NorteApplication`, abra:
   - http://localhost:8080/ (página inicial da API)
   - http://localhost:8080/swagger-ui.html (para testar todos os endpoints)

> As credenciais **nunca** ficam no código: o `application.properties` lê `DB_URL`, `DB_USER` e `DB_PASSWORD` das variáveis de ambiente.

### Gerar o .jar (opcional)

Na aba **Maven** (lateral direita do IntelliJ), vá em **Lifecycle > clean** e depois em **package**. O arquivo sai em `target/norte-0.0.1-SNAPSHOT.jar`. Para rodar:
```
java -jar target/norte-0.0.1-SNAPSHOT.jar
```
Antes, defina as variáveis `DB_USER` e `DB_PASSWORD` no terminal.

## 3. Deploy no Render

1. Suba o projeto para o GitHub (veja "Git" abaixo).
2. Em https://render.com, entre com a conta do GitHub. No **Dashboard**, clique em **New > Web Service**.
3. Em **Git Provider**, escolha este repositório.
4. Preencha:
   - **Name:** um nome único, por exemplo `norte-rm562448`;
   - **Language / Runtime:** **Docker** (o projeto já tem `Dockerfile`);
   - **Instance Type:** **Free**.
5. Em **Environment Variables**, adicione:

   | Key | Value |
   |---|---|
   | `DB_URL` | `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL` |
   | `DB_USER` | seu RM (ex.: `rm562448`) |
   | `DB_PASSWORD` | sua senha do Oracle |

6. Clique em **Deploy Web Service** e espere alguns minutos. Quando aparecer **Live**, abra o link do serviço, que mostra a página inicial da API, e depois `/swagger-ui.html`.

> O plano gratuito "dorme" sem uso. A primeira requisição depois de um tempo parado pode levar até 1 minuto.

### Git

```
git init
git add .
git commit -m "API Norte"
git branch -M main
git remote add origin https://github.com/<conta>/<repositorio>.git
git push -u origin main
```

> **Atenção:** commits depois do prazo da GS (19/11/2026, 23h55) zeram a parte técnica.

---

## Endpoints

| Método | Endpoint | O que faz | Sucesso |
|---|---|---|---|
| GET | `/` | Informações da API | 200 |
| GET | `/cidadaos` | Lista cidadãos | 200 |
| GET | `/cidadaos/{id}` | Busca um cidadão | 200 |
| GET | `/cidadaos/{id}/solicitacoes` | Solicitações do cidadão | 200 |
| POST | `/cidadaos` | Cadastra (aceite dos termos registrado na hora) | 201 |
| PUT | `/cidadaos/{id}` | Altera dados | 200 |
| DELETE | `/cidadaos/{id}` | Exclui (só sem solicitações) | 204 |
| GET | `/solicitacoes?cidadao=&area=&status=` | Lista, com filtros opcionais | 200 |
| GET | `/solicitacoes/{id}` | Busca uma solicitação | 200 |
| GET | `/solicitacoes/{id}/plano` | Plano de ação da solicitação | 200 |
| GET | `/solicitacoes/{id}/encaminhamentos` | Encaminhamentos da solicitação | 200 |
| POST | `/solicitacoes` | Abre solicitação (status ABERTA) | 201 |
| PUT | `/solicitacoes/{id}` | Altera área, relato, canal, status, valor | 200 |
| DELETE | `/solicitacoes/{id}` | Exclui (só sem plano e sem encaminhamentos) | 204 |
| GET | `/planos` | Lista planos com passos e checklist | 200 |
| GET | `/planos/{id}` | Busca um plano completo | 200 |
| GET | `/planos/{id}/acoes` | Passos do plano | 200 |
| POST | `/planos` | Cria plano + passos + checklist (transação) | 201 |
| PUT | `/planos/{id}` | Altera resumo, "precisa de advogado?" e checklist | 200 |
| DELETE | `/planos/{id}` | Exclui o plano com passos e checklist | 204 |
| GET | `/acoes/{id}` | Busca um passo | 200 |
| POST | `/acoes` | Adiciona passo a um plano | 201 |
| PUT | `/acoes/{id}` | Altera passo (ex.: marcar concluído) | 200 |
| DELETE | `/acoes/{id}` | Exclui passo (o plano precisa ficar com pelo menos 1) | 204 |
| GET | `/encaminhamentos` | Lista encaminhamentos | 200 |
| GET | `/encaminhamentos/{id}` | Busca um encaminhamento | 200 |
| POST | `/encaminhamentos` | Encaminha a solicitação a um ponto gratuito | 201 |
| PUT | `/encaminhamentos/{id}` | Altera ponto, data agendada ou situação | 200 |
| DELETE | `/encaminhamentos/{id}` | Exclui | 204 |
| GET | `/areas`, `/areas/{id}`, `/areas/{id}/leis` | As 10 áreas e as leis de cada uma | 200 |
| GET | `/leis`, `/leis/{id}` | Leis vigentes, com link oficial | 200 |
| GET | `/pontos?tipo=&acessivel=` , `/pontos/{id}` | Pontos de atendimento gratuito | 200 |
| GET | `/bairros` | Bairros atendidos | 200 |

### Exemplos de JSON

**POST /cidadaos**
```json
{ "idBairro": 4, "nome": "Maria Exemplo", "dataNascimento": "1990-05-01",
  "email": "maria@example.com", "celular": "(11) 91234-5678", "pcd": false }
```

**POST /solicitacoes**
```json
{ "idCidadao": 8, "idArea": 3, "relato": "Cobraram uma tarifa que eu não contratei no cartão.",
  "canal": "TEXTO", "valorEstimado": 59.90 }
```

**POST /planos**: o semáforo de cada passo é calculado pela API.
```json
{ "idSolicitacao": 14, "precisaAdvogado": false,
  "resumo": "Cobrança indevida: contestar e pedir estorno.",
  "acoes": [
    { "ordem": 1, "descricao": "Contestar a cobrança com a operadora do cartão.", "prazo": "2026-11-20", "idLei": 3 },
    { "ordem": 2, "descricao": "Se não resolver, procurar o Juizado Especial Cível.", "prazo": "2026-12-05", "idLei": 11 }
  ],
  "checklist": [ { "idDocumento": 1, "possui": true }, { "idDocumento": 6, "possui": false } ] }
```

**PUT /acoes/{id}**: marcar um passo como concluído.
```json
{ "ordem": 1, "descricao": "Contestar a cobrança com a operadora do cartão.", "prazo": "2026-11-20", "idLei": 3, "concluida": true }
```

**POST /encaminhamentos**
```json
{ "idSolicitacao": 14, "idPonto": 3, "dataAgendada": "2026-11-25T10:30:00" }
```

## Regras de negócio (camada BO)

- **Semáforo automático:** passo concluído ou sem prazo fica **VERDE**; prazo vencido fica **VERMELHO**; faltando até 3 dias fica **AMARELO**; prazo mais distante fica **VERDE**. O semáforo é recalculado a cada consulta.
- **Um plano por solicitação.** Criar o plano muda a solicitação de ABERTA para EM ANDAMENTO. Excluir o plano devolve a solicitação para ABERTA.
- **O plano é gravado em uma transação:** plano, passos e checklist são salvos todos juntos ou nenhum (commit/rollback).
- **Acessibilidade:** quem é pessoa com deficiência só pode ser encaminhado para ponto **acessível**.
- O Norte só encaminha para pontos **gratuitos**. A data agendada não pode ser anterior ao encaminhamento.
- Solicitação CONCLUÍDA ou CANCELADA não pode ser alterada e não recebe plano nem encaminhamento.
- **Exclusões protegidas:** não dá para excluir cidadão com solicitações, nem solicitação com plano ou encaminhamento.
- **Minimização de dados (LGPD):** o cadastro não guarda CPF. O celular é normalizado (só dígitos) e o e-mail é único.

## Tratamento de erros

Toda falha volta como JSON:
```json
{ "timestamp": "2026-11-12T10:30:00", "status": 422, "erro": "Unprocessable Entity",
  "mensagem": "Canal invalido. Use: [TEXTO, AUDIO]", "caminho": "/solicitacoes" }
```

| Código | Quando |
|---|---|
| 400 | JSON inválido ou parâmetro com tipo errado |
| 404 | Registro ou endereço não encontrado |
| 405 | Método HTTP não suportado no endereço |
| 409 | Conflito (duplicado, segundo plano, exclusão de registro com dependentes; ORA-00001 / ORA-02292) |
| 422 | Regra de negócio violada (ORA-02291 / ORA-02290 também) |
| 503 | Falha de acesso ao Oracle |
| 500 | Erro inesperado |
