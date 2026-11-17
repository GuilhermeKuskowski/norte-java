-- =====================================================================
-- NORTE - GS 2026.2 CitizenTech - Database Design
-- Equipe Acesstech - Turma 2ESPI
-- Arquivo: 02_dml.sql  (Script D.M.L. - item 4)
--
-- Rode DEPOIS do 01_ddl.sql. Insere dados de exemplo em todas as 12
-- tabelas, com no minimo 5 linhas em cada uma (nenhuma excecao).
-- Ordem: primeiro as tabelas "pai" (sem FK), depois as "filhas".
--
-- Todos os dados de pessoas sao FICTICIOS (personas da equipe).
-- E-mails usam o dominio reservado example.com; telefones e enderecos
-- sao ilustrativos. Nenhum dado pessoal real.
-- =====================================================================

SET DEFINE OFF;
ALTER SESSION SET NLS_DATE_FORMAT = 'DD/MM/YYYY HH24:MI';

-- ---------------------------------------------------------------------
-- NRT_BAIRRO (7 linhas)
-- ---------------------------------------------------------------------
INSERT INTO nrt_bairro (id_bairro, nm_bairro, nm_distrito, nm_subprefeitura)
VALUES (1, 'Vila Brasilândia', 'Brasilândia', 'Freguesia/Brasilândia');
INSERT INTO nrt_bairro (id_bairro, nm_bairro, nm_distrito, nm_subprefeitura)
VALUES (2, 'Jardim Maracanã', 'Brasilândia', 'Freguesia/Brasilândia');
INSERT INTO nrt_bairro (id_bairro, nm_bairro, nm_distrito, nm_subprefeitura)
VALUES (3, 'Parque Hollywood', 'Brasilândia', 'Freguesia/Brasilândia');
INSERT INTO nrt_bairro (id_bairro, nm_bairro, nm_distrito, nm_subprefeitura)
VALUES (4, 'Vila Penteado', 'Brasilândia', 'Freguesia/Brasilândia');
INSERT INTO nrt_bairro (id_bairro, nm_bairro, nm_distrito, nm_subprefeitura)
VALUES (5, 'Jardim Elisa Maria', 'Brasilândia', 'Freguesia/Brasilândia');
INSERT INTO nrt_bairro (id_bairro, nm_bairro, nm_distrito, nm_subprefeitura)
VALUES (6, 'Jardim Paulistano', 'Brasilândia', 'Freguesia/Brasilândia');
INSERT INTO nrt_bairro (id_bairro, nm_bairro, nm_distrito, nm_subprefeitura)
VALUES (7, 'Freguesia do Ó', 'Freguesia do Ó', 'Freguesia/Brasilândia');

-- ---------------------------------------------------------------------
-- NRT_AREA_DIREITO (10 linhas - as 10 areas da triagem)
-- ---------------------------------------------------------------------
INSERT INTO nrt_area_direito (id_area, nm_area, ds_area, st_modo_discreto, st_trilha_calculo)
VALUES (1, 'Trabalho e emprego', 'Demissão, rescisão, salário atrasado, FGTS, carteira assinada e horas extras.', 'N', 'S');
INSERT INTO nrt_area_direito (id_area, nm_area, ds_area, st_modo_discreto, st_trilha_calculo)
VALUES (2, 'INSS e benefícios sociais', 'BPC, aposentadoria, auxílio por incapacidade, Bolsa Família e CadÚnico.', 'N', 'S');
INSERT INTO nrt_area_direito (id_area, nm_area, ds_area, st_modo_discreto, st_trilha_calculo)
VALUES (3, 'Consumo e dívidas', 'Nome negativado, cobrança indevida, produto com defeito, golpes e superendividamento.', 'N', 'S');
INSERT INTO nrt_area_direito (id_area, nm_area, ds_area, st_modo_discreto, st_trilha_calculo)
VALUES (4, 'Moradia e aluguel', 'Contrato de aluguel, caução, multa, despejo e reajuste.', 'N', 'N');
INSERT INTO nrt_area_direito (id_area, nm_area, ds_area, st_modo_discreto, st_trilha_calculo)
VALUES (5, 'Família, crianças e escola', 'Pensão alimentícia, guarda, vaga em creche e escola.', 'N', 'N');
INSERT INTO nrt_area_direito (id_area, nm_area, ds_area, st_modo_discreto, st_trilha_calculo)
VALUES (6, 'Violência e discriminação', 'Violência doméstica, racismo e discriminação. Usa o modo discreto.', 'S', 'N');
INSERT INTO nrt_area_direito (id_area, nm_area, ds_area, st_modo_discreto, st_trilha_calculo)
VALUES (7, 'Saúde', 'SUS, remédios e negativas de plano de saúde.', 'N', 'N');
INSERT INTO nrt_area_direito (id_area, nm_area, ds_area, st_modo_discreto, st_trilha_calculo)
VALUES (8, 'Idosos e PcD', 'Direitos da pessoa idosa e da pessoa com deficiência, acompanhante e acessibilidade.', 'N', 'N');
INSERT INTO nrt_area_direito (id_area, nm_area, ds_area, st_modo_discreto, st_trilha_calculo)
VALUES (9, 'Trânsito', 'Multas, defesa prévia, CNH e acidentes.', 'N', 'N');
INSERT INTO nrt_area_direito (id_area, nm_area, ds_area, st_modo_discreto, st_trilha_calculo)
VALUES (10, 'Serviços públicos e acesso à justiça', 'Documentos, Defensoria Pública, Juizado Especial e serviços da prefeitura.', 'N', 'N');

-- ---------------------------------------------------------------------
-- NRT_LEI (14 linhas - normas vigentes, link da fonte oficial)
-- ---------------------------------------------------------------------
INSERT INTO nrt_lei (id_lei, tp_norma, nr_norma, dt_norma, nm_apelido, ds_resumo, ds_url_fonte, st_revogada)
VALUES (1, 'DECRETO-LEI', '5.452', TO_DATE('01/05/1943', 'DD/MM/YYYY'), 'CLT',
        'Consolidação das Leis do Trabalho. Art. 477, §6º: verbas rescisórias pagas em até 10 dias do fim do contrato.',
        'https://www.planalto.gov.br/ccivil_03/decreto-lei/del5452.htm', 'N');
INSERT INTO nrt_lei (id_lei, tp_norma, nr_norma, dt_norma, nm_apelido, ds_resumo, ds_url_fonte, st_revogada)
VALUES (2, 'LEI', '8.742', TO_DATE('07/12/1993', 'DD/MM/YYYY'), 'LOAS',
        'Lei Orgânica da Assistência Social. Art. 20: Benefício de Prestação Continuada (BPC) para idoso e pessoa com deficiência.',
        'https://www.planalto.gov.br/ccivil_03/leis/l8742.htm', 'N');
INSERT INTO nrt_lei (id_lei, tp_norma, nr_norma, dt_norma, nm_apelido, ds_resumo, ds_url_fonte, st_revogada)
VALUES (3, 'LEI', '8.078', TO_DATE('11/09/1990', 'DD/MM/YYYY'), 'Código de Defesa do Consumidor',
        'Proteção do consumidor: cobrança indevida, cadastros de inadimplentes e serviços com defeito.',
        'https://www.planalto.gov.br/ccivil_03/leis/l8078.htm', 'N');
INSERT INTO nrt_lei (id_lei, tp_norma, nr_norma, dt_norma, nm_apelido, ds_resumo, ds_url_fonte, st_revogada)
VALUES (4, 'LEI', '8.245', TO_DATE('18/10/1991', 'DD/MM/YYYY'), 'Lei do Inquilinato',
        'Locação de imóveis urbanos. Art. 4º: multa proporcional. Art. 38, §2º: caução em dinheiro de até 3 meses de aluguel.',
        'https://www.planalto.gov.br/ccivil_03/leis/l8245.htm', 'N');
INSERT INTO nrt_lei (id_lei, tp_norma, nr_norma, dt_norma, nm_apelido, ds_resumo, ds_url_fonte, st_revogada)
VALUES (5, 'LEI', '8.069', TO_DATE('13/07/1990', 'DD/MM/YYYY'), 'ECA',
        'Estatuto da Criança e do Adolescente. Art. 54, IV: atendimento em creche e pré-escola de 0 a 5 anos.',
        'https://www.planalto.gov.br/ccivil_03/leis/l8069.htm', 'N');
INSERT INTO nrt_lei (id_lei, tp_norma, nr_norma, dt_norma, nm_apelido, ds_resumo, ds_url_fonte, st_revogada)
VALUES (6, 'LEI', '11.340', TO_DATE('07/08/2006', 'DD/MM/YYYY'), 'Lei Maria da Penha',
        'Mecanismos para coibir a violência doméstica e familiar contra a mulher e medidas protetivas.',
        'https://www.planalto.gov.br/ccivil_03/_ato2004-2006/2006/lei/l11340.htm', 'N');
INSERT INTO nrt_lei (id_lei, tp_norma, nr_norma, dt_norma, nm_apelido, ds_resumo, ds_url_fonte, st_revogada)
VALUES (7, 'LEI', '9.656', TO_DATE('03/06/1998', 'DD/MM/YYYY'), 'Lei dos Planos de Saúde',
        'Planos e seguros privados de assistência à saúde: coberturas obrigatórias e regras de negativa.',
        'https://www.planalto.gov.br/ccivil_03/leis/l9656.htm', 'N');
INSERT INTO nrt_lei (id_lei, tp_norma, nr_norma, dt_norma, nm_apelido, ds_resumo, ds_url_fonte, st_revogada)
VALUES (8, 'LEI', '10.741', TO_DATE('01/10/2003', 'DD/MM/YYYY'), 'Estatuto da Pessoa Idosa',
        'Direitos da pessoa idosa. Art. 16: direito a acompanhante em internação ou observação.',
        'https://www.planalto.gov.br/ccivil_03/leis/2003/l10.741.htm', 'N');
INSERT INTO nrt_lei (id_lei, tp_norma, nr_norma, dt_norma, nm_apelido, ds_resumo, ds_url_fonte, st_revogada)
VALUES (9, 'LEI', '13.146', TO_DATE('06/07/2015', 'DD/MM/YYYY'), 'Lei Brasileira de Inclusão',
        'Estatuto da Pessoa com Deficiência: acessibilidade, atendimento prioritário e direito a acompanhante.',
        'https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2015/lei/l13146.htm', 'N');
INSERT INTO nrt_lei (id_lei, tp_norma, nr_norma, dt_norma, nm_apelido, ds_resumo, ds_url_fonte, st_revogada)
VALUES (10, 'LEI', '9.503', TO_DATE('23/09/1997', 'DD/MM/YYYY'), 'Código de Trânsito Brasileiro',
        'Infrações, penalidades e defesa contra multas de trânsito.',
        'https://www.planalto.gov.br/ccivil_03/leis/l9503.htm', 'N');
INSERT INTO nrt_lei (id_lei, tp_norma, nr_norma, dt_norma, nm_apelido, ds_resumo, ds_url_fonte, st_revogada)
VALUES (11, 'LEI', '9.099', TO_DATE('26/09/1995', 'DD/MM/YYYY'), 'Lei dos Juizados Especiais',
        'Juizados Especiais Cíveis. Art. 9º: causas de até 20 salários mínimos sem advogado.',
        'https://www.planalto.gov.br/ccivil_03/leis/l9099.htm', 'N');
INSERT INTO nrt_lei (id_lei, tp_norma, nr_norma, dt_norma, nm_apelido, ds_resumo, ds_url_fonte, st_revogada)
VALUES (12, 'LEI', '8.213', TO_DATE('24/07/1991', 'DD/MM/YYYY'), 'Lei de Benefícios da Previdência',
        'Planos de benefícios da Previdência Social: aposentadorias, auxílios e pensão por morte.',
        'https://www.planalto.gov.br/ccivil_03/leis/l8213cons.htm', 'N');
INSERT INTO nrt_lei (id_lei, tp_norma, nr_norma, dt_norma, nm_apelido, ds_resumo, ds_url_fonte, st_revogada)
VALUES (13, 'LEI', '7.716', TO_DATE('05/01/1989', 'DD/MM/YYYY'), 'Lei do Crime Racial',
        'Define os crimes resultantes de preconceito de raça, cor, etnia, religião ou procedência nacional.',
        'https://www.planalto.gov.br/ccivil_03/leis/l7716.htm', 'N');
INSERT INTO nrt_lei (id_lei, tp_norma, nr_norma, dt_norma, nm_apelido, ds_resumo, ds_url_fonte, st_revogada)
VALUES (14, 'SUMULA', 'STJ 548', TO_DATE('14/10/2015', 'DD/MM/YYYY'), 'Súmula 548 do STJ',
        'O credor deve excluir a negativação em até 5 dias úteis após o pagamento integral da dívida.',
        'https://scon.stj.jus.br/SCON/sumstj/toc.jsp?livre=548', 'N');

-- ---------------------------------------------------------------------
-- NRT_DOCUMENTO (12 linhas - catalogo do checklist)
-- ---------------------------------------------------------------------
INSERT INTO nrt_documento (id_documento, nm_documento, ds_orientacao)
VALUES (1, 'Documento de identidade com foto (RG ou CNH)', 'Leve o original. Serve para qualquer atendimento.');
INSERT INTO nrt_documento (id_documento, nm_documento, ds_orientacao)
VALUES (2, 'CPF', 'Pode estar no próprio RG ou na CNH.');
INSERT INTO nrt_documento (id_documento, nm_documento, ds_orientacao)
VALUES (3, 'Comprovante de residência', 'Conta de água, luz ou telefone dos últimos 3 meses.');
INSERT INTO nrt_documento (id_documento, nm_documento, ds_orientacao)
VALUES (4, 'Carteira de Trabalho Digital', 'Baixe pelo aplicativo ou pelo gov.br. Mostra vínculos e salários.');
INSERT INTO nrt_documento (id_documento, nm_documento, ds_orientacao)
VALUES (5, 'Termo de Rescisão (TRCT)', 'Entregue pela empresa no desligamento. Se não recebeu, anote isso.');
INSERT INTO nrt_documento (id_documento, nm_documento, ds_orientacao)
VALUES (6, 'Comprovante de pagamento', 'Recibo, boleto pago ou comprovante de Pix com data e valor.');
INSERT INTO nrt_documento (id_documento, nm_documento, ds_orientacao)
VALUES (7, 'Carta de indeferimento do INSS', 'Disponível no Meu INSS, em Consultar pedidos.');
INSERT INTO nrt_documento (id_documento, nm_documento, ds_orientacao)
VALUES (8, 'Contrato de locação', 'Cópia do contrato de aluguel assinado.');
INSERT INTO nrt_documento (id_documento, nm_documento, ds_orientacao)
VALUES (9, 'Certidão de nascimento da criança', 'Original ou cópia legível.');
INSERT INTO nrt_documento (id_documento, nm_documento, ds_orientacao)
VALUES (10, 'Negativa do plano de saúde por escrito', 'Peça ao plano a negativa com o motivo, por escrito ou por e-mail.');
INSERT INTO nrt_documento (id_documento, nm_documento, ds_orientacao)
VALUES (11, 'Notificação da multa de trânsito', 'Carta recebida em casa ou print do aplicativo do órgão de trânsito.');
INSERT INTO nrt_documento (id_documento, nm_documento, ds_orientacao)
VALUES (12, 'Relatório ou pedido médico', 'Documento do médico com a indicação do procedimento.');

-- ---------------------------------------------------------------------
-- NRT_CIDADAO (7 linhas - personas ficticias)
-- ---------------------------------------------------------------------
INSERT INTO nrt_cidadao (id_cidadao, id_bairro, nm_cidadao, dt_nascimento, ds_email, nr_celular, st_pcd, dt_cadastro, dt_aceite_termos)
VALUES (1, 2, 'Márcia Aparecida Souza', TO_DATE('14/03/1979', 'DD/MM/YYYY'), 'marcia.souza@example.com', '11900000001', 'N',
        TO_DATE('04/11/2026 08:50', 'DD/MM/YYYY HH24:MI'), TO_DATE('04/11/2026 08:51', 'DD/MM/YYYY HH24:MI'));
INSERT INTO nrt_cidadao (id_cidadao, id_bairro, nm_cidadao, dt_nascimento, ds_email, nr_celular, st_pcd, dt_cadastro, dt_aceite_termos)
VALUES (2, 1, 'José Raimundo Lima', TO_DATE('02/08/1959', 'DD/MM/YYYY'), NULL, '11900000002', 'N',
        TO_DATE('06/11/2026 14:20', 'DD/MM/YYYY HH24:MI'), TO_DATE('06/11/2026 14:22', 'DD/MM/YYYY HH24:MI'));
INSERT INTO nrt_cidadao (id_cidadao, id_bairro, nm_cidadao, dt_nascimento, ds_email, nr_celular, st_pcd, dt_cadastro, dt_aceite_termos)
VALUES (3, 4, 'Juliana Rocha Alves', TO_DATE('30/05/1997', 'DD/MM/YYYY'), 'juliana.alves@example.com', '11900000003', 'N',
        TO_DATE('03/11/2026 18:55', 'DD/MM/YYYY HH24:MI'), TO_DATE('03/11/2026 18:56', 'DD/MM/YYYY HH24:MI'));
INSERT INTO nrt_cidadao (id_cidadao, id_bairro, nm_cidadao, dt_nascimento, ds_email, nr_celular, st_pcd, dt_cadastro, dt_aceite_termos)
VALUES (4, 5, 'Rafael Moreira Santos', TO_DATE('11/01/1992', 'DD/MM/YYYY'), 'rafael.santos@example.com', '11900000004', 'N',
        TO_DATE('14/10/2026 19:30', 'DD/MM/YYYY HH24:MI'), TO_DATE('14/10/2026 19:31', 'DD/MM/YYYY HH24:MI'));
INSERT INTO nrt_cidadao (id_cidadao, id_bairro, nm_cidadao, dt_nascimento, ds_email, nr_celular, st_pcd, dt_cadastro, dt_aceite_termos)
VALUES (5, 6, 'Ana Paula Ferreira Dias', TO_DATE('19/09/1995', 'DD/MM/YYYY'), NULL, '11900000005', 'N',
        TO_DATE('08/11/2026 08:40', 'DD/MM/YYYY HH24:MI'), TO_DATE('08/11/2026 08:41', 'DD/MM/YYYY HH24:MI'));
INSERT INTO nrt_cidadao (id_cidadao, id_bairro, nm_cidadao, dt_nascimento, ds_email, nr_celular, st_pcd, dt_cadastro, dt_aceite_termos)
VALUES (6, 3, 'Carlos Eduardo Nunes', TO_DATE('07/06/1981', 'DD/MM/YYYY'), 'carlos.nunes@example.com', '11900000006', 'S',
        TO_DATE('09/11/2026 16:00', 'DD/MM/YYYY HH24:MI'), TO_DATE('09/11/2026 16:02', 'DD/MM/YYYY HH24:MI'));
INSERT INTO nrt_cidadao (id_cidadao, id_bairro, nm_cidadao, dt_nascimento, ds_email, nr_celular, st_pcd, dt_cadastro, dt_aceite_termos)
VALUES (7, 1, 'Lucas Oliveira Prado', TO_DATE('23/04/2007', 'DD/MM/YYYY'), 'lucas.prado@example.com', '11900000007', 'N',
        TO_DATE('11/11/2026 21:00', 'DD/MM/YYYY HH24:MI'), TO_DATE('11/11/2026 21:01', 'DD/MM/YYYY HH24:MI'));

-- ---------------------------------------------------------------------
-- NRT_PONTO_ATENDIMENTO (7 linhas - nomes e enderecos ilustrativos)
-- ---------------------------------------------------------------------
INSERT INTO nrt_ponto_atendimento (id_ponto, id_bairro, nm_ponto, tp_ponto, ds_endereco, nr_telefone, st_acessivel, st_gratuito)
VALUES (1, 1, 'CRAS Brasilândia', 'CRAS', 'Endereço ilustrativo - Vila Brasilândia', '1130000001', 'S', 'S');
INSERT INTO nrt_ponto_atendimento (id_ponto, id_bairro, nm_ponto, tp_ponto, ds_endereco, nr_telefone, st_acessivel, st_gratuito)
VALUES (2, 7, 'Defensoria Pública - atendimento regional', 'DEFENSORIA', 'Endereço ilustrativo - Freguesia do Ó', '1130000002', 'S', 'S');
INSERT INTO nrt_ponto_atendimento (id_ponto, id_bairro, nm_ponto, tp_ponto, ds_endereco, nr_telefone, st_acessivel, st_gratuito)
VALUES (3, 7, 'Procon - posto de atendimento', 'PROCON', 'Endereço ilustrativo - Freguesia do Ó', '1130000003', 'S', 'S');
INSERT INTO nrt_ponto_atendimento (id_ponto, id_bairro, nm_ponto, tp_ponto, ds_endereco, nr_telefone, st_acessivel, st_gratuito)
VALUES (4, 7, 'Juizado Especial Cível - anexo regional', 'JUIZADO', 'Endereço ilustrativo - Freguesia do Ó', '1130000004', 'S', 'S');
INSERT INTO nrt_ponto_atendimento (id_ponto, id_bairro, nm_ponto, tp_ponto, ds_endereco, nr_telefone, st_acessivel, st_gratuito)
VALUES (5, 1, 'Núcleo de Prática Jurídica parceiro', 'NPJ', 'Endereço ilustrativo - Vila Brasilândia (acesso por escada)', '1130000005', 'N', 'S');
INSERT INTO nrt_ponto_atendimento (id_ponto, id_bairro, nm_ponto, tp_ponto, ds_endereco, nr_telefone, st_acessivel, st_gratuito)
VALUES (6, 5, 'CREAS - atendimento especializado', 'CREAS', 'Endereço ilustrativo - Jardim Elisa Maria', '1130000006', 'S', 'S');
INSERT INTO nrt_ponto_atendimento (id_ponto, id_bairro, nm_ponto, tp_ponto, ds_endereco, nr_telefone, st_acessivel, st_gratuito)
VALUES (7, 6, 'Agência do INSS', 'INSS', 'Endereço ilustrativo - Jardim Paulistano', '135', 'S', 'S');

-- ---------------------------------------------------------------------
-- NRT_AREA_LEI (19 linhas - associativa N:N)
-- ---------------------------------------------------------------------
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (1, 1, 'S');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (2, 2, 'S');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (2, 12, 'S');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (3, 3, 'S');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (3, 11, 'N');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (3, 14, 'N');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (4, 4, 'S');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (4, 11, 'N');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (5, 5, 'S');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (6, 6, 'S');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (6, 13, 'S');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (7, 7, 'S');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (7, 3, 'N');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (8, 8, 'S');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (8, 9, 'S');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (8, 2, 'N');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (9, 10, 'S');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (10, 11, 'S');
INSERT INTO nrt_area_lei (id_area, id_lei, st_principal) VALUES (10, 3, 'N');

-- ---------------------------------------------------------------------
-- NRT_SOLICITACAO (13 linhas)
-- ---------------------------------------------------------------------
INSERT INTO nrt_solicitacao (id_solicitacao, id_cidadao, id_area, ds_relato, tp_canal, dt_abertura, st_solicitacao, vl_estimado)
VALUES (1, 1, 1, 'Fui demitida sem justa causa em 26/10 e até agora a empresa não pagou a rescisão nem liberou o FGTS.',
        'TEXTO', TO_DATE('05/11/2026 09:15', 'DD/MM/YYYY HH24:MI'), 'EM ANDAMENTO', 6200.00);
INSERT INTO nrt_solicitacao (id_solicitacao, id_cidadao, id_area, ds_relato, tp_canal, dt_abertura, st_solicitacao, vl_estimado)
VALUES (2, 2, 2, 'Pedi o BPC e o INSS negou dizendo que a renda da família passa do limite.',
        'AUDIO', TO_DATE('06/11/2026 14:40', 'DD/MM/YYYY HH24:MI'), 'EM ANDAMENTO', NULL);
INSERT INTO nrt_solicitacao (id_solicitacao, id_cidadao, id_area, ds_relato, tp_canal, dt_abertura, st_solicitacao, vl_estimado)
VALUES (3, 3, 3, 'Paguei a dívida da loja em 20/10 e meu nome continua negativado.',
        'TEXTO', TO_DATE('03/11/2026 19:05', 'DD/MM/YYYY HH24:MI'), 'EM ANDAMENTO', 850.00);
INSERT INTO nrt_solicitacao (id_solicitacao, id_cidadao, id_area, ds_relato, tp_canal, dt_abertura, st_solicitacao, vl_estimado)
VALUES (4, 4, 4, 'O proprietário pediu 4 meses de caução e quer cobrar a multa inteira se eu sair antes do fim do contrato.',
        'TEXTO', TO_DATE('07/11/2026 10:30', 'DD/MM/YYYY HH24:MI'), 'EM ANDAMENTO', 4800.00);
INSERT INTO nrt_solicitacao (id_solicitacao, id_cidadao, id_area, ds_relato, tp_canal, dt_abertura, st_solicitacao, vl_estimado)
VALUES (5, 5, 5, 'O pai da minha filha parou de pagar a pensão e não consegui vaga na creche perto de casa.',
        'AUDIO', TO_DATE('08/11/2026 08:50', 'DD/MM/YYYY HH24:MI'), 'EM ANDAMENTO', 1200.00);
INSERT INTO nrt_solicitacao (id_solicitacao, id_cidadao, id_area, ds_relato, tp_canal, dt_abertura, st_solicitacao, vl_estimado)
VALUES (6, 6, 7, 'Meu plano de saúde negou a cirurgia que o médico pediu e não me deu a negativa por escrito.',
        'TEXTO', TO_DATE('09/11/2026 16:20', 'DD/MM/YYYY HH24:MI'), 'EM ANDAMENTO', 18000.00);
INSERT INTO nrt_solicitacao (id_solicitacao, id_cidadao, id_area, ds_relato, tp_canal, dt_abertura, st_solicitacao, vl_estimado)
VALUES (7, 7, 8, 'O hospital não quer deixar eu acompanhar minha avó de 82 anos que está internada.',
        'TEXTO', TO_DATE('11/11/2026 21:10', 'DD/MM/YYYY HH24:MI'), 'ABERTA', NULL);
INSERT INTO nrt_solicitacao (id_solicitacao, id_cidadao, id_area, ds_relato, tp_canal, dt_abertura, st_solicitacao, vl_estimado)
VALUES (8, 4, 9, 'Recebi uma multa de trânsito de um lugar onde nunca estive com o carro.',
        'TEXTO', TO_DATE('15/10/2026 20:10', 'DD/MM/YYYY HH24:MI'), 'CONCLUIDA', 195.23);
INSERT INTO nrt_solicitacao (id_solicitacao, id_cidadao, id_area, ds_relato, tp_canal, dt_abertura, st_solicitacao, vl_estimado)
VALUES (9, 1, 3, 'Estão cobrando na minha fatura um seguro que eu nunca contratei.',
        'TEXTO', TO_DATE('10/11/2026 12:00', 'DD/MM/YYYY HH24:MI'), 'ABERTA', 89.90);
INSERT INTO nrt_solicitacao (id_solicitacao, id_cidadao, id_area, ds_relato, tp_canal, dt_abertura, st_solicitacao, vl_estimado)
VALUES (10, 2, 8, 'O motorista não aceitou minha carteira de idoso no ônibus.',
        'AUDIO', TO_DATE('09/11/2026 07:45', 'DD/MM/YYYY HH24:MI'), 'ABERTA', NULL);
INSERT INTO nrt_solicitacao (id_solicitacao, id_cidadao, id_area, ds_relato, tp_canal, dt_abertura, st_solicitacao, vl_estimado)
VALUES (11, 6, 8, 'O banco não me entrega o contrato em formato acessível para leitor de tela.',
        'TEXTO', TO_DATE('10/11/2026 10:15', 'DD/MM/YYYY HH24:MI'), 'ABERTA', NULL);
INSERT INTO nrt_solicitacao (id_solicitacao, id_cidadao, id_area, ds_relato, tp_canal, dt_abertura, st_solicitacao, vl_estimado)
VALUES (12, 5, 3, 'A operadora cobrou duas vezes a conta de internet deste mês.',
        'AUDIO', TO_DATE('11/11/2026 18:30', 'DD/MM/YYYY HH24:MI'), 'ABERTA', 119.80);
INSERT INTO nrt_solicitacao (id_solicitacao, id_cidadao, id_area, ds_relato, tp_canal, dt_abertura, st_solicitacao, vl_estimado)
VALUES (13, 7, 1, 'Saí do programa de jovem aprendiz e não recebi o 13º proporcional.',
        'TEXTO', TO_DATE('12/11/2026 07:30', 'DD/MM/YYYY HH24:MI'), 'ABERTA', 600.00);

-- ---------------------------------------------------------------------
-- NRT_PLANO_ACAO (7 linhas - as solicitacoes 7 e 9 a 13 ainda estao
-- ABERTAS e ainda nao tem plano; isso mostra o relacionamento 1:0..1)
-- ---------------------------------------------------------------------
INSERT INTO nrt_plano_acao (id_plano, id_solicitacao, dt_geracao, st_precisa_advogado, ds_resumo)
VALUES (1, 1, TO_DATE('05/11/2026 09:16', 'DD/MM/YYYY HH24:MI'), 'S',
        'Rescisão não paga no prazo de 10 dias. Cobrar a empresa e buscar orientação jurídica gratuita.');
INSERT INTO nrt_plano_acao (id_plano, id_solicitacao, dt_geracao, st_precisa_advogado, ds_resumo)
VALUES (2, 2, TO_DATE('06/11/2026 14:41', 'DD/MM/YYYY HH24:MI'), 'N',
        'BPC negado por renda. Conferir o motivo, atualizar o CadÚnico e recorrer no prazo.');
INSERT INTO nrt_plano_acao (id_plano, id_solicitacao, dt_geracao, st_precisa_advogado, ds_resumo)
VALUES (3, 3, TO_DATE('03/11/2026 19:06', 'DD/MM/YYYY HH24:MI'), 'N',
        'Negativação mantida após o pagamento. Prazo de 5 dias úteis vencido; cabe Procon e Juizado sem advogado.');
INSERT INTO nrt_plano_acao (id_plano, id_solicitacao, dt_geracao, st_precisa_advogado, ds_resumo)
VALUES (4, 4, TO_DATE('07/11/2026 10:31', 'DD/MM/YYYY HH24:MI'), 'N',
        'Caução acima do limite legal e multa que deve ser proporcional.');
INSERT INTO nrt_plano_acao (id_plano, id_solicitacao, dt_geracao, st_precisa_advogado, ds_resumo)
VALUES (5, 5, TO_DATE('08/11/2026 08:51', 'DD/MM/YYYY HH24:MI'), 'N',
        'Pensão atrasada pela Defensoria e pedido de vaga em creche.');
INSERT INTO nrt_plano_acao (id_plano, id_solicitacao, dt_geracao, st_precisa_advogado, ds_resumo)
VALUES (6, 6, TO_DATE('09/11/2026 16:21', 'DD/MM/YYYY HH24:MI'), 'S',
        'Negativa de cirurgia sem justificativa escrita. Exigir a negativa e reclamar na ANS e no Procon.');
INSERT INTO nrt_plano_acao (id_plano, id_solicitacao, dt_geracao, st_precisa_advogado, ds_resumo)
VALUES (7, 8, TO_DATE('15/10/2026 20:11', 'DD/MM/YYYY HH24:MI'), 'N',
        'Multa de local onde o veículo não esteve. Apresentar defesa prévia no prazo da notificação.');

-- ---------------------------------------------------------------------
-- NRT_ACAO_PLANO (18 linhas)
-- ---------------------------------------------------------------------
-- Plano 1 - Marcia (trabalho)
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (1, 1, 1, 1, 'Conferir o termo de rescisão e as verbas devidas na trilha de cálculo do Norte.',
        TO_DATE('07/11/2026', 'DD/MM/YYYY'), 'VERDE', 'S');
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (2, 1, 1, 2, 'Cobrar por escrito o pagamento da rescisão, que venceu em 05/11 (art. 477, §6º, da CLT).',
        TO_DATE('10/11/2026', 'DD/MM/YYYY'), 'VERMELHO', 'S');
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (3, 1, 1, 3, 'Levar os documentos ao Núcleo de Prática Jurídica para avaliar a ação trabalhista.',
        TO_DATE('21/11/2026', 'DD/MM/YYYY'), 'AMARELO', 'N');
-- Plano 2 - Jose (INSS)
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (4, 2, 2, 1, 'Baixar a carta de indeferimento no Meu INSS e conferir o motivo da negativa.',
        TO_DATE('10/11/2026', 'DD/MM/YYYY'), 'VERDE', 'S');
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (5, 2, 2, 2, 'Atualizar o CadÚnico no CRAS com a renda e as despesas da família.',
        TO_DATE('14/11/2026', 'DD/MM/YYYY'), 'AMARELO', 'N');
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (6, 2, NULL, 3, 'Apresentar recurso ao INSS em até 30 dias da ciência da decisão.',
        TO_DATE('04/12/2026', 'DD/MM/YYYY'), 'VERDE', 'N');
-- Plano 3 - Juliana (consumo)
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (7, 3, NULL, 1, 'Separar o comprovante de pagamento e o print da consulta que mostra a negativação.',
        TO_DATE('05/11/2026', 'DD/MM/YYYY'), 'VERDE', 'S');
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (8, 3, 14, 2, 'Exigir da loja a baixa da negativação: o prazo de 5 dias úteis venceu em 27/10.',
        TO_DATE('27/10/2026', 'DD/MM/YYYY'), 'VERMELHO', 'S');
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (9, 3, 3, 3, 'Registrar reclamação no Procon ou no consumidor.gov.br.',
        TO_DATE('12/11/2026', 'DD/MM/YYYY'), 'AMARELO', 'S');
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (10, 3, 11, 4, 'Se não resolver, entrar no Juizado Especial Cível sem advogado (até 20 salários mínimos).',
        TO_DATE('30/11/2026', 'DD/MM/YYYY'), 'VERDE', 'N');
-- Plano 4 - Rafael (moradia)
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (11, 4, 4, 1, 'Responder ao proprietário que a caução em dinheiro é limitada a 3 meses de aluguel.',
        TO_DATE('12/11/2026', 'DD/MM/YYYY'), 'AMARELO', 'N');
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (12, 4, 4, 2, 'Pedir o cálculo da multa proporcional ao tempo que falta do contrato.',
        TO_DATE('19/11/2026', 'DD/MM/YYYY'), 'VERDE', 'N');
-- Plano 5 - Ana Paula (familia)
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (13, 5, NULL, 1, 'Pedir à Defensoria Pública a cobrança da pensão atrasada.',
        TO_DATE('13/11/2026', 'DD/MM/YYYY'), 'AMARELO', 'S');
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (14, 5, 5, 2, 'Registrar o pedido de vaga em creche na Diretoria Regional de Educação.',
        TO_DATE('18/11/2026', 'DD/MM/YYYY'), 'VERDE', 'N');
-- Plano 6 - Carlos (saude)
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (15, 6, 7, 1, 'Pedir ao plano de saúde a negativa por escrito, com o motivo.',
        TO_DATE('11/11/2026', 'DD/MM/YYYY'), 'VERMELHO', 'N');
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (16, 6, 3, 2, 'Registrar reclamação na ANS e no Procon.',
        TO_DATE('13/11/2026', 'DD/MM/YYYY'), 'AMARELO', 'N');
-- Plano 7 - Rafael (transito)
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (17, 7, 10, 1, 'Apresentar defesa prévia ao órgão de trânsito dentro do prazo da notificação.',
        TO_DATE('30/10/2026', 'DD/MM/YYYY'), 'VERDE', 'S');
INSERT INTO nrt_acao_plano (id_acao, id_plano, id_lei, nr_ordem, ds_acao, dt_prazo, tp_semaforo, st_concluida)
VALUES (18, 7, NULL, 2, 'Guardar o protocolo e acompanhar o resultado da defesa.',
        TO_DATE('30/11/2026', 'DD/MM/YYYY'), 'VERDE', 'S');

-- ---------------------------------------------------------------------
-- NRT_PLANO_DOCUMENTO (19 linhas - checklist, associativa N:N)
-- ---------------------------------------------------------------------
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (1, 1, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (1, 4, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (1, 5, 'N');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (2, 1, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (2, 2, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (2, 3, 'N');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (2, 7, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (3, 1, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (3, 6, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (4, 1, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (4, 8, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (5, 1, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (5, 3, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (5, 9, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (6, 1, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (6, 10, 'N');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (6, 12, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (7, 1, 'S');
INSERT INTO nrt_plano_documento (id_plano, id_documento, st_possui) VALUES (7, 11, 'S');

-- ---------------------------------------------------------------------
-- NRT_ENCAMINHAMENTO (8 linhas - os pontos 6 e 7 ainda nao receberam
-- encaminhamentos, o que aparece na consulta com LEFT OUTER JOIN)
-- ---------------------------------------------------------------------
INSERT INTO nrt_encaminhamento (id_encaminhamento, id_solicitacao, id_ponto, dt_encaminhamento, dt_agendada, st_encaminhamento)
VALUES (1, 1, 5, TO_DATE('05/11/2026', 'DD/MM/YYYY'), TO_DATE('18/11/2026 14:00', 'DD/MM/YYYY HH24:MI'), 'AGENDADO');
INSERT INTO nrt_encaminhamento (id_encaminhamento, id_solicitacao, id_ponto, dt_encaminhamento, dt_agendada, st_encaminhamento)
VALUES (2, 2, 1, TO_DATE('06/11/2026', 'DD/MM/YYYY'), TO_DATE('10/11/2026 09:00', 'DD/MM/YYYY HH24:MI'), 'COMPARECEU');
INSERT INTO nrt_encaminhamento (id_encaminhamento, id_solicitacao, id_ponto, dt_encaminhamento, dt_agendada, st_encaminhamento)
VALUES (3, 2, 2, TO_DATE('06/11/2026', 'DD/MM/YYYY'), TO_DATE('25/11/2026 10:30', 'DD/MM/YYYY HH24:MI'), 'AGENDADO');
INSERT INTO nrt_encaminhamento (id_encaminhamento, id_solicitacao, id_ponto, dt_encaminhamento, dt_agendada, st_encaminhamento)
VALUES (4, 3, 3, TO_DATE('03/11/2026', 'DD/MM/YYYY'), TO_DATE('06/11/2026 11:00', 'DD/MM/YYYY HH24:MI'), 'COMPARECEU');
INSERT INTO nrt_encaminhamento (id_encaminhamento, id_solicitacao, id_ponto, dt_encaminhamento, dt_agendada, st_encaminhamento)
VALUES (5, 3, 4, TO_DATE('03/11/2026', 'DD/MM/YYYY'), NULL, 'INDICADO');
INSERT INTO nrt_encaminhamento (id_encaminhamento, id_solicitacao, id_ponto, dt_encaminhamento, dt_agendada, st_encaminhamento)
VALUES (6, 4, 5, TO_DATE('07/11/2026', 'DD/MM/YYYY'), TO_DATE('19/11/2026 15:30', 'DD/MM/YYYY HH24:MI'), 'AGENDADO');
INSERT INTO nrt_encaminhamento (id_encaminhamento, id_solicitacao, id_ponto, dt_encaminhamento, dt_agendada, st_encaminhamento)
VALUES (7, 5, 2, TO_DATE('08/11/2026', 'DD/MM/YYYY'), TO_DATE('11/11/2026 13:00', 'DD/MM/YYYY HH24:MI'), 'COMPARECEU');
INSERT INTO nrt_encaminhamento (id_encaminhamento, id_solicitacao, id_ponto, dt_encaminhamento, dt_agendada, st_encaminhamento)
VALUES (8, 6, 3, TO_DATE('09/11/2026', 'DD/MM/YYYY'), NULL, 'INDICADO');

COMMIT;

-- Conferencia rapida: quantidade de linhas por tabela (todas >= 5)
SELECT 'NRT_BAIRRO' AS tabela, COUNT(*) AS linhas FROM nrt_bairro
UNION ALL SELECT 'NRT_CIDADAO', COUNT(*) FROM nrt_cidadao
UNION ALL SELECT 'NRT_AREA_DIREITO', COUNT(*) FROM nrt_area_direito
UNION ALL SELECT 'NRT_LEI', COUNT(*) FROM nrt_lei
UNION ALL SELECT 'NRT_AREA_LEI', COUNT(*) FROM nrt_area_lei
UNION ALL SELECT 'NRT_SOLICITACAO', COUNT(*) FROM nrt_solicitacao
UNION ALL SELECT 'NRT_PLANO_ACAO', COUNT(*) FROM nrt_plano_acao
UNION ALL SELECT 'NRT_ACAO_PLANO', COUNT(*) FROM nrt_acao_plano
UNION ALL SELECT 'NRT_DOCUMENTO', COUNT(*) FROM nrt_documento
UNION ALL SELECT 'NRT_PLANO_DOCUMENTO', COUNT(*) FROM nrt_plano_documento
UNION ALL SELECT 'NRT_PONTO_ATENDIMENTO', COUNT(*) FROM nrt_ponto_atendimento
UNION ALL SELECT 'NRT_ENCAMINHAMENTO', COUNT(*) FROM nrt_encaminhamento;
