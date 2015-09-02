-- Municípios

DELETE FROM olap.ft_municipio;

INSERT INTO olap.ft_municipio(edmu_cd_municipio, indi_cd_indicadores, ftmu_vl_valor)
    SELECT c.edmu_cd_municipio, 1, count(a.bosc_sq_osc) FROM data.tb_osc a , portal.tb_osc_interacao b , data.tb_localizacao c
  WHERE a.bosc_sq_osc = b.bosc_sq_osc 
  AND b.bosc_sq_osc = c.bosc_sq_osc 
  AND b.inte_in_osc = true 
  --AND c.mdfd_cd_fonte_dados = (SELECT min(d.mdfd_cd_fonte_dados) FROM data.tb_localizacao d WHERE d.bosc_sq_osc = c.bosc_sq_osc AND d.loca_geometry = a.bosc_geometry) 
  GROUP BY c.edmu_cd_municipio
  ORDER BY c.edmu_cd_municipio;

INSERT INTO olap.ft_municipio(edmu_cd_municipio, indi_cd_indicadores, ftmu_vl_valor)
  SELECT c.edmu_cd_municipio, 2, sum(a.siconv_qt_parceria_finalizada + a.siconv_qt_parceria_execucao) 
  FROM data.tb_osc_siconv a , portal.tb_osc_interacao b , data.tb_localizacao c
  WHERE a.bosc_sq_osc = b.bosc_sq_osc 
  AND b.bosc_sq_osc = c.bosc_sq_osc 
  AND b.inte_in_osc = true 
  --AND c.mdfd_cd_fonte_dados = (SELECT min(d.mdfd_cd_fonte_dados) FROM data.tb_localizacao d JOIN data.tb_osc e ON (d.bosc_sq_osc = e.bosc_sq_osc) WHERE d.bosc_sq_osc = c.bosc_sq_osc AND d.loca_geometry = e.bosc_geometry) 
  GROUP BY c.edmu_cd_municipio
  ORDER BY c.edmu_cd_municipio;

INSERT INTO olap.ft_municipio(edmu_cd_municipio, indi_cd_indicadores, ftmu_vl_valor)
  SELECT c.edmu_cd_municipio, 3, sum(a.siconv_vl_global) 
  FROM data.tb_osc_siconv a , portal.tb_osc_interacao b , data.tb_localizacao c
  WHERE a.bosc_sq_osc = b.bosc_sq_osc 
  AND b.bosc_sq_osc = c.bosc_sq_osc 
  AND b.inte_in_osc = true 
  --AND c.mdfd_cd_fonte_dados = (SELECT min(d.mdfd_cd_fonte_dados) FROM data.tb_localizacao d JOIN data.tb_osc e ON (d.bosc_sq_osc = e.bosc_sq_osc) WHERE d.bosc_sq_osc = c.bosc_sq_osc AND d.loca_geometry = e.bosc_geometry) 
  GROUP BY c.edmu_cd_municipio
  ORDER BY c.edmu_cd_municipio;

INSERT INTO olap.ft_municipio(edmu_cd_municipio, indi_cd_indicadores, ftmu_vl_valor)
SELECT c.edmu_cd_municipio, 4, sum(a.rais_qt_vinculo_ativo) 
  FROM data.tb_osc_rais a , portal.tb_osc_interacao b , data.tb_localizacao c
  WHERE a.bosc_sq_osc = b.bosc_sq_osc 
  AND b.bosc_sq_osc = c.bosc_sq_osc 
  AND b.inte_in_osc = true 
  --AND c.mdfd_cd_fonte_dados = (SELECT min(d.mdfd_cd_fonte_dados) FROM data.tb_localizacao d JOIN data.tb_osc e ON (d.bosc_sq_osc = e.bosc_sq_osc) WHERE d.bosc_sq_osc = c.bosc_sq_osc AND d.loca_geometry = e.bosc_geometry) 
  GROUP BY c.edmu_cd_municipio
  ORDER BY c.edmu_cd_municipio;  


INSERT INTO olap.ft_municipio(edmu_cd_municipio, indi_cd_indicadores, ftmu_vl_valor)
SELECT edmu_cd_municipio, 1, 0
FROM spat.ed_municipio WHERE edmu_cd_municipio NOT IN (SELECT edmu_cd_municipio  FROM olap.ft_municipio WHERE indi_cd_indicadores = 1)
ORDER BY edmu_cd_municipio ;

INSERT INTO olap.ft_municipio(edmu_cd_municipio, indi_cd_indicadores, ftmu_vl_valor)
SELECT edmu_cd_municipio, 2, 0
FROM spat.ed_municipio WHERE edmu_cd_municipio NOT IN (SELECT edmu_cd_municipio  FROM olap.ft_municipio WHERE indi_cd_indicadores = 2)
ORDER BY edmu_cd_municipio ;

INSERT INTO olap.ft_municipio(edmu_cd_municipio, indi_cd_indicadores, ftmu_vl_valor)
SELECT edmu_cd_municipio, 3, 0
FROM spat.ed_municipio WHERE edmu_cd_municipio NOT IN (SELECT edmu_cd_municipio  FROM olap.ft_municipio WHERE indi_cd_indicadores = 3)
ORDER BY edmu_cd_municipio ;

INSERT INTO olap.ft_municipio(edmu_cd_municipio, indi_cd_indicadores, ftmu_vl_valor)
SELECT edmu_cd_municipio, 4, 0
FROM spat.ed_municipio WHERE edmu_cd_municipio NOT IN (SELECT edmu_cd_municipio  FROM olap.ft_municipio WHERE indi_cd_indicadores = 4)
ORDER BY edmu_cd_municipio ;


-- UF

DELETE FROM olap.ft_uf;

INSERT INTO olap.ft_uf(eduf_cd_uf, indi_cd_indicadores, ftuf_vl_valor)
  SELECT c.eduf_cd_uf, 1, sum(a.ftmu_vl_valor) 
  FROM olap.ft_municipio a, spat.ed_municipio b, spat.ed_uf c
  WHERE a.edmu_cd_municipio = b.edmu_cd_municipio
  AND a.indi_cd_indicadores = 1
  AND b.eduf_cd_uf = c.eduf_cd_uf
  GROUP BY c.eduf_cd_uf
  ORDER BY c.eduf_cd_uf;

INSERT INTO olap.ft_uf(eduf_cd_uf, indi_cd_indicadores, ftuf_vl_valor)
  SELECT c.eduf_cd_uf, 2, sum(a.ftmu_vl_valor) 
  FROM olap.ft_municipio a, spat.ed_municipio b, spat.ed_uf c
  WHERE a.edmu_cd_municipio = b.edmu_cd_municipio
  AND a.indi_cd_indicadores = 2
  AND b.eduf_cd_uf = c.eduf_cd_uf
  GROUP BY c.eduf_cd_uf
  ORDER BY c.eduf_cd_uf;

INSERT INTO olap.ft_uf(eduf_cd_uf, indi_cd_indicadores, ftuf_vl_valor)
  SELECT c.eduf_cd_uf, 3, sum(a.ftmu_vl_valor) 
  FROM olap.ft_municipio a, spat.ed_municipio b, spat.ed_uf c
  WHERE a.edmu_cd_municipio = b.edmu_cd_municipio
  AND a.indi_cd_indicadores = 3
  AND b.eduf_cd_uf = c.eduf_cd_uf
  GROUP BY c.eduf_cd_uf
  ORDER BY c.eduf_cd_uf;

INSERT INTO olap.ft_uf(eduf_cd_uf, indi_cd_indicadores, ftuf_vl_valor)
  SELECT c.eduf_cd_uf, 4, sum(a.ftmu_vl_valor) 
  FROM olap.ft_municipio a, spat.ed_municipio b, spat.ed_uf c
  WHERE a.edmu_cd_municipio = b.edmu_cd_municipio
  AND a.indi_cd_indicadores = 4
  AND b.eduf_cd_uf = c.eduf_cd_uf
  GROUP BY c.eduf_cd_uf
  ORDER BY c.eduf_cd_uf;

-- Região

DELETE FROM olap.ft_regiao;

INSERT INTO olap.ft_regiao(edre_cd_regiao, indi_cd_indicadores, ftre_vl_valor)
  SELECT c.edre_cd_regiao, 1, sum(a.ftuf_vl_valor) 
  FROM olap.ft_uf a, spat.ed_uf b, spat.ed_regiao c
  WHERE a.eduf_cd_uf = b.eduf_cd_uf
  AND a.indi_cd_indicadores = 1
  AND b.edre_cd_regiao = c.edre_cd_regiao
  GROUP BY c.edre_cd_regiao
  ORDER BY c.edre_cd_regiao;

INSERT INTO olap.ft_regiao(edre_cd_regiao, indi_cd_indicadores, ftre_vl_valor)
  SELECT c.edre_cd_regiao, 2, sum(a.ftuf_vl_valor) 
  FROM olap.ft_uf a, spat.ed_uf b, spat.ed_regiao c
  WHERE a.eduf_cd_uf = b.eduf_cd_uf
  AND a.indi_cd_indicadores = 2
  AND b.edre_cd_regiao = c.edre_cd_regiao
  GROUP BY c.edre_cd_regiao
  ORDER BY c.edre_cd_regiao; 

INSERT INTO olap.ft_regiao(edre_cd_regiao, indi_cd_indicadores, ftre_vl_valor)
  SELECT c.edre_cd_regiao, 3, sum(a.ftuf_vl_valor) 
  FROM olap.ft_uf a, spat.ed_uf b, spat.ed_regiao c
  WHERE a.eduf_cd_uf = b.eduf_cd_uf
  AND a.indi_cd_indicadores = 3
  AND b.edre_cd_regiao = c.edre_cd_regiao
  GROUP BY c.edre_cd_regiao
  ORDER BY c.edre_cd_regiao; 

INSERT INTO olap.ft_regiao(edre_cd_regiao, indi_cd_indicadores, ftre_vl_valor)
  SELECT c.edre_cd_regiao, 4, sum(a.ftuf_vl_valor) 
  FROM olap.ft_uf a, spat.ed_uf b, spat.ed_regiao c
  WHERE a.eduf_cd_uf = b.eduf_cd_uf
  AND a.indi_cd_indicadores = 4
  AND b.edre_cd_regiao = c.edre_cd_regiao
  GROUP BY c.edre_cd_regiao
  ORDER BY c.edre_cd_regiao;
