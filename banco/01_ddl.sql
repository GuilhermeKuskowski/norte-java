-- =====================================================================
-- NORTE - GS 2026.2 CitizenTech - Database Design
-- Equipe Acesstech - Turma 2ESPI
-- Arquivo: 01_ddl.sql  (Script D.D.L. - item 3)
--
-- Banco: Oracle (servidor FIAP). Cria as 12 tabelas do modelo
-- logico-relacional com todas as constraints:
--   PK (chave primaria), FK (chave estrangeira), UNIQUE, CHECK, NOT NULL.
-- As FKs ficam em ALTER TABLE no fim do arquivo, depois que todas as
-- tabelas existem.
--
-- Este mesmo arquivo e importado no Oracle Data Modeler para gerar o
-- diagrama (File > Import > DDL File).
--
-- Padrao de nomes: prefixo NRT_ (Norte) nas tabelas.
--   id_ = identificador  nm_ = nome     ds_ = descricao   dt_ = data
--   nr_ = numero         tp_ = tipo     st_ = status/flag vl_ = valor
-- =====================================================================


-- ---------------------------------------------------------------------
-- 1. NRT_BAIRRO - bairros do territorio atendido (Brasilandia e entorno)
-- ---------------------------------------------------------------------
CREATE TABLE nrt_bairro (
    id_bairro         NUMBER(4)      NOT NULL,
    nm_bairro         VARCHAR2(80)   NOT NULL,
    nm_distrito       VARCHAR2(60)   NOT NULL,
    nm_subprefeitura  VARCHAR2(80)   NOT NULL,
    CONSTRAINT pk_nrt_bairro PRIMARY KEY (id_bairro),
    CONSTRAINT uk_nrt_bairro_nome UNIQUE (nm_bairro, nm_distrito)
);

-- ---------------------------------------------------------------------
-- 2. NRT_CIDADAO - pessoa que usa o Norte (sem CPF: minimizacao LGPD)
-- ---------------------------------------------------------------------
CREATE TABLE nrt_cidadao (
    id_cidadao        NUMBER(8)      NOT NULL,
    id_bairro         NUMBER(4)      NOT NULL,
    nm_cidadao        VARCHAR2(100)  NOT NULL,
    dt_nascimento     DATE           NOT NULL,
    ds_email          VARCHAR2(120),
    nr_celular        VARCHAR2(15)   NOT NULL,
    st_pcd            CHAR(1)        DEFAULT 'N' NOT NULL,
    dt_cadastro       DATE           DEFAULT SYSDATE NOT NULL,
    dt_aceite_termos  DATE           NOT NULL,
    CONSTRAINT pk_nrt_cidadao PRIMARY KEY (id_cidadao),
    CONSTRAINT uk_nrt_cidadao_email UNIQUE (ds_email),
    CONSTRAINT ck_nrt_cidadao_pcd CHECK (st_pcd IN ('S', 'N')),
    CONSTRAINT ck_nrt_cidadao_aceite CHECK (dt_aceite_termos >= dt_cadastro)
);

-- ---------------------------------------------------------------------
-- 3. NRT_AREA_DIREITO - as 10 areas da triagem do Norte
-- ---------------------------------------------------------------------
CREATE TABLE nrt_area_direito (
    id_area           NUMBER(2)      NOT NULL,
    nm_area           VARCHAR2(80)   NOT NULL,
    ds_area           VARCHAR2(300)  NOT NULL,
    st_modo_discreto  CHAR(1)        DEFAULT 'N' NOT NULL,
    st_trilha_calculo CHAR(1)        DEFAULT 'N' NOT NULL,
    CONSTRAINT pk_nrt_area_direito PRIMARY KEY (id_area),
    CONSTRAINT uk_nrt_area_nome UNIQUE (nm_area),
    CONSTRAINT ck_nrt_area_discreto CHECK (st_modo_discreto IN ('S', 'N')),
    CONSTRAINT ck_nrt_area_trilha CHECK (st_trilha_calculo IN ('S', 'N'))
);

-- ---------------------------------------------------------------------
-- 4. NRT_LEI - base de normas citadas nos planos (link da fonte oficial)
-- ---------------------------------------------------------------------
CREATE TABLE nrt_lei (
    id_lei            NUMBER(5)      NOT NULL,
    tp_norma          VARCHAR2(20)   NOT NULL,
    nr_norma          VARCHAR2(15)   NOT NULL,
    dt_norma          DATE           NOT NULL,
    nm_apelido        VARCHAR2(80),
    ds_resumo         VARCHAR2(400)  NOT NULL,
    ds_url_fonte      VARCHAR2(250)  NOT NULL,
    st_revogada       CHAR(1)        DEFAULT 'N' NOT NULL,
    CONSTRAINT pk_nrt_lei PRIMARY KEY (id_lei),
    CONSTRAINT uk_nrt_lei_norma UNIQUE (tp_norma, nr_norma),
    CONSTRAINT ck_nrt_lei_tipo CHECK (tp_norma IN ('CONSTITUICAO', 'LEI',
        'LEI COMPLEMENTAR', 'DECRETO-LEI', 'DECRETO', 'SUMULA')),
    CONSTRAINT ck_nrt_lei_revogada CHECK (st_revogada IN ('S', 'N'))
);

-- ---------------------------------------------------------------------
-- 5. NRT_AREA_LEI - associativa N:N entre area e lei
--    (uma lei serve a varias areas; uma area usa varias leis)
-- ---------------------------------------------------------------------
CREATE TABLE nrt_area_lei (
    id_area           NUMBER(2)      NOT NULL,
    id_lei            NUMBER(5)      NOT NULL,
    st_principal      CHAR(1)        DEFAULT 'N' NOT NULL,
    CONSTRAINT pk_nrt_area_lei PRIMARY KEY (id_area, id_lei),
    CONSTRAINT ck_nrt_area_lei_princ CHECK (st_principal IN ('S', 'N'))
);

-- ---------------------------------------------------------------------
-- 6. NRT_SOLICITACAO - o problema contado pelo cidadao (texto ou audio)
--    ja classificado pela triagem em uma area
-- ---------------------------------------------------------------------
CREATE TABLE nrt_solicitacao (
    id_solicitacao    NUMBER(10)     NOT NULL,
    id_cidadao        NUMBER(8)      NOT NULL,
    id_area           NUMBER(2)      NOT NULL,
    ds_relato         VARCHAR2(1000) NOT NULL,
    tp_canal          VARCHAR2(5)    NOT NULL,
    dt_abertura       DATE           DEFAULT SYSDATE NOT NULL,
    st_solicitacao    VARCHAR2(12)   DEFAULT 'ABERTA' NOT NULL,
    vl_estimado       NUMBER(12, 2),
    CONSTRAINT pk_nrt_solicitacao PRIMARY KEY (id_solicitacao),
    CONSTRAINT ck_nrt_solic_canal CHECK (tp_canal IN ('TEXTO', 'AUDIO')),
    CONSTRAINT ck_nrt_solic_status CHECK (st_solicitacao IN ('ABERTA',
        'EM ANDAMENTO', 'CONCLUIDA', 'CANCELADA')),
    CONSTRAINT ck_nrt_solic_valor CHECK (vl_estimado >= 0)
);

-- ---------------------------------------------------------------------
-- 7. NRT_PLANO_ACAO - plano de acao gerado para a solicitacao (1:1)
-- ---------------------------------------------------------------------
CREATE TABLE nrt_plano_acao (
    id_plano            NUMBER(10)    NOT NULL,
    id_solicitacao      NUMBER(10)    NOT NULL,
    dt_geracao          DATE          DEFAULT SYSDATE NOT NULL,
    st_precisa_advogado CHAR(1)       NOT NULL,
    ds_resumo           VARCHAR2(400) NOT NULL,
    CONSTRAINT pk_nrt_plano_acao PRIMARY KEY (id_plano),
    CONSTRAINT uk_nrt_plano_solic UNIQUE (id_solicitacao),
    CONSTRAINT ck_nrt_plano_advogado CHECK (st_precisa_advogado IN ('S', 'N'))
);

-- ---------------------------------------------------------------------
-- 8. NRT_ACAO_PLANO - passos do plano, com prazo, semaforo e lei citada
-- ---------------------------------------------------------------------
CREATE TABLE nrt_acao_plano (
    id_acao           NUMBER(10)     NOT NULL,
    id_plano          NUMBER(10)     NOT NULL,
    id_lei            NUMBER(5),
    nr_ordem          NUMBER(2)      NOT NULL,
    ds_acao           VARCHAR2(300)  NOT NULL,
    dt_prazo          DATE,
    tp_semaforo       VARCHAR2(8)    NOT NULL,
    st_concluida      CHAR(1)        DEFAULT 'N' NOT NULL,
    CONSTRAINT pk_nrt_acao_plano PRIMARY KEY (id_acao),
    CONSTRAINT uk_nrt_acao_ordem UNIQUE (id_plano, nr_ordem),
    CONSTRAINT ck_nrt_acao_ordem CHECK (nr_ordem > 0),
    CONSTRAINT ck_nrt_acao_semaforo CHECK (tp_semaforo IN ('VERDE',
        'AMARELO', 'VERMELHO')),
    CONSTRAINT ck_nrt_acao_concluida CHECK (st_concluida IN ('S', 'N'))
);

-- ---------------------------------------------------------------------
-- 9. NRT_DOCUMENTO - catalogo de documentos pedidos nos checklists
-- ---------------------------------------------------------------------
CREATE TABLE nrt_documento (
    id_documento      NUMBER(4)      NOT NULL,
    nm_documento      VARCHAR2(100)  NOT NULL,
    ds_orientacao     VARCHAR2(300)  NOT NULL,
    CONSTRAINT pk_nrt_documento PRIMARY KEY (id_documento),
    CONSTRAINT uk_nrt_documento_nome UNIQUE (nm_documento)
);

-- ---------------------------------------------------------------------
-- 10. NRT_PLANO_DOCUMENTO - checklist: associativa N:N plano x documento
-- ---------------------------------------------------------------------
CREATE TABLE nrt_plano_documento (
    id_plano          NUMBER(10)     NOT NULL,
    id_documento      NUMBER(4)      NOT NULL,
    st_possui         CHAR(1)        DEFAULT 'N' NOT NULL,
    CONSTRAINT pk_nrt_plano_documento PRIMARY KEY (id_plano, id_documento),
    CONSTRAINT ck_nrt_plano_doc_possui CHECK (st_possui IN ('S', 'N'))
);

-- ---------------------------------------------------------------------
-- 11. NRT_PONTO_ATENDIMENTO - atendimento gratuito (Defensoria, Procon,
--     Juizado, CRAS, CREAS, NPJ, INSS...)
-- ---------------------------------------------------------------------
CREATE TABLE nrt_ponto_atendimento (
    id_ponto          NUMBER(5)      NOT NULL,
    id_bairro         NUMBER(4)      NOT NULL,
    nm_ponto          VARCHAR2(120)  NOT NULL,
    tp_ponto          VARCHAR2(12)   NOT NULL,
    ds_endereco       VARCHAR2(150)  NOT NULL,
    nr_telefone       VARCHAR2(15),
    st_acessivel      CHAR(1)        DEFAULT 'N' NOT NULL,
    st_gratuito       CHAR(1)        DEFAULT 'S' NOT NULL,
    CONSTRAINT pk_nrt_ponto_atendimento PRIMARY KEY (id_ponto),
    CONSTRAINT uk_nrt_ponto_nome UNIQUE (nm_ponto),
    CONSTRAINT ck_nrt_ponto_tipo CHECK (tp_ponto IN ('DEFENSORIA', 'PROCON',
        'JUIZADO', 'CRAS', 'CREAS', 'NPJ', 'INSS', 'OUTRO')),
    CONSTRAINT ck_nrt_ponto_acessivel CHECK (st_acessivel IN ('S', 'N')),
    CONSTRAINT ck_nrt_ponto_gratuito CHECK (st_gratuito IN ('S', 'N'))
);

-- ---------------------------------------------------------------------
-- 12. NRT_ENCAMINHAMENTO - associativa N:N solicitacao x ponto,
--     com data, agendamento e situacao
-- ---------------------------------------------------------------------
CREATE TABLE nrt_encaminhamento (
    id_encaminhamento NUMBER(10)     NOT NULL,
    id_solicitacao    NUMBER(10)     NOT NULL,
    id_ponto          NUMBER(5)      NOT NULL,
    dt_encaminhamento DATE           DEFAULT SYSDATE NOT NULL,
    dt_agendada       DATE,
    st_encaminhamento VARCHAR2(15)   DEFAULT 'INDICADO' NOT NULL,
    CONSTRAINT pk_nrt_encaminhamento PRIMARY KEY (id_encaminhamento),
    CONSTRAINT uk_nrt_encam_solic_ponto UNIQUE (id_solicitacao, id_ponto),
    CONSTRAINT ck_nrt_encam_status CHECK (st_encaminhamento IN ('INDICADO',
        'AGENDADO', 'COMPARECEU', 'NAO COMPARECEU')),
    CONSTRAINT ck_nrt_encam_datas CHECK (dt_agendada IS NULL
        OR dt_agendada >= dt_encaminhamento)
);


-- =====================================================================
-- CHAVES ESTRANGEIRAS (relacionamentos)
-- =====================================================================

-- BAIRRO 1:N CIDADAO
ALTER TABLE nrt_cidadao
    ADD CONSTRAINT fk_nrt_cidadao_bairro
    FOREIGN KEY (id_bairro) REFERENCES nrt_bairro (id_bairro);

-- AREA_DIREITO 1:N AREA_LEI
ALTER TABLE nrt_area_lei
    ADD CONSTRAINT fk_nrt_area_lei_area
    FOREIGN KEY (id_area) REFERENCES nrt_area_direito (id_area);

-- LEI 1:N AREA_LEI
ALTER TABLE nrt_area_lei
    ADD CONSTRAINT fk_nrt_area_lei_lei
    FOREIGN KEY (id_lei) REFERENCES nrt_lei (id_lei);

-- CIDADAO 1:N SOLICITACAO
ALTER TABLE nrt_solicitacao
    ADD CONSTRAINT fk_nrt_solic_cidadao
    FOREIGN KEY (id_cidadao) REFERENCES nrt_cidadao (id_cidadao);

-- AREA_DIREITO 1:N SOLICITACAO
ALTER TABLE nrt_solicitacao
    ADD CONSTRAINT fk_nrt_solic_area
    FOREIGN KEY (id_area) REFERENCES nrt_area_direito (id_area);

-- SOLICITACAO 1:1 PLANO_ACAO (FK + UNIQUE uk_nrt_plano_solic)
ALTER TABLE nrt_plano_acao
    ADD CONSTRAINT fk_nrt_plano_solic
    FOREIGN KEY (id_solicitacao) REFERENCES nrt_solicitacao (id_solicitacao);

-- PLANO_ACAO 1:N ACAO_PLANO
ALTER TABLE nrt_acao_plano
    ADD CONSTRAINT fk_nrt_acao_plano
    FOREIGN KEY (id_plano) REFERENCES nrt_plano_acao (id_plano);

-- LEI 1:N ACAO_PLANO (opcional: nem toda acao cita uma lei)
ALTER TABLE nrt_acao_plano
    ADD CONSTRAINT fk_nrt_acao_lei
    FOREIGN KEY (id_lei) REFERENCES nrt_lei (id_lei);

-- PLANO_ACAO 1:N PLANO_DOCUMENTO
ALTER TABLE nrt_plano_documento
    ADD CONSTRAINT fk_nrt_plano_doc_plano
    FOREIGN KEY (id_plano) REFERENCES nrt_plano_acao (id_plano);

-- DOCUMENTO 1:N PLANO_DOCUMENTO
ALTER TABLE nrt_plano_documento
    ADD CONSTRAINT fk_nrt_plano_doc_documento
    FOREIGN KEY (id_documento) REFERENCES nrt_documento (id_documento);

-- BAIRRO 1:N PONTO_ATENDIMENTO
ALTER TABLE nrt_ponto_atendimento
    ADD CONSTRAINT fk_nrt_ponto_bairro
    FOREIGN KEY (id_bairro) REFERENCES nrt_bairro (id_bairro);

-- SOLICITACAO 1:N ENCAMINHAMENTO
ALTER TABLE nrt_encaminhamento
    ADD CONSTRAINT fk_nrt_encam_solic
    FOREIGN KEY (id_solicitacao) REFERENCES nrt_solicitacao (id_solicitacao);

-- PONTO_ATENDIMENTO 1:N ENCAMINHAMENTO
ALTER TABLE nrt_encaminhamento
    ADD CONSTRAINT fk_nrt_encam_ponto
    FOREIGN KEY (id_ponto) REFERENCES nrt_ponto_atendimento (id_ponto);


-- =====================================================================
-- COMENTARIOS (aparecem no Data Modeler e no dicionario de dados)
-- =====================================================================
COMMENT ON TABLE nrt_bairro            IS 'Bairros do territorio atendido pelo Norte (Brasilandia e entorno).';
COMMENT ON TABLE nrt_cidadao           IS 'Pessoa que usa o Norte. Nao guarda CPF (minimizacao de dados - LGPD).';
COMMENT ON TABLE nrt_area_direito      IS 'As 10 areas de direitos do dia a dia usadas na triagem.';
COMMENT ON TABLE nrt_lei               IS 'Normas vigentes citadas nos planos, com link da fonte oficial.';
COMMENT ON TABLE nrt_area_lei          IS 'Associativa N:N: quais leis fundamentam cada area.';
COMMENT ON TABLE nrt_solicitacao       IS 'Problema relatado pelo cidadao (texto ou audio) e area definida na triagem.';
COMMENT ON TABLE nrt_plano_acao        IS 'Plano de acao gerado para uma solicitacao (1:1).';
COMMENT ON TABLE nrt_acao_plano        IS 'Passos do plano com prazo, semaforo e lei citada.';
COMMENT ON TABLE nrt_documento         IS 'Catalogo de documentos usados nos checklists.';
COMMENT ON TABLE nrt_plano_documento   IS 'Checklist de documentos de cada plano (associativa N:N).';
COMMENT ON TABLE nrt_ponto_atendimento IS 'Locais de atendimento gratuito para onde o Norte encaminha.';
COMMENT ON TABLE nrt_encaminhamento    IS 'Encaminhamento de uma solicitacao a um ponto de atendimento.';
