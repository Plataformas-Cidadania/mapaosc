-- carregar as tabelas do schema temp referente aos dados atuais da Rais e do Siconv
INSERT INTO temp.tb_osc_rais_2011 SELECT * FROM data.tb_osc_rais;

INSERT INTO temp.tb_osc_siconv_2008_2012 SELECT * FROM data.tb_osc_siconv;