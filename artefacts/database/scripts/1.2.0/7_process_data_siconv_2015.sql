CREATE OR REPLACE FUNCTION data.load_siconv()

RETURNS void AS $$

DECLARE
	row RECORD;
	conv RECORD;
	id INTEGER;
BEGIN
	DELETE FROM data.tb_osc_siconv;
	
	FOR row IN
		SELECT s.cd_identif_proponente, count(*) 
		FROM temp.tb_osc_siconv_2015_completa AS s LEFT JOIN data.tb_osc AS o 
		ON s.cd_identif_proponente = o.bosc_nr_identificacao::TEXT 
		WHERE s.tx_esfera_adm_proponente = 'PRIVADA'
		GROUP BY s.cd_identif_proponente
	LOOP
		SELECT INTO id bosc_sq_osc 
		FROM data.tb_osc 
		WHERE bosc_nr_identificacao = row.cd_identif_proponente::NUMERIC;
		
		IF id IS null
		THEN
			SELECT INTO conv * 
			FROM temp.tb_osc_siconv_2015_completa 
			WHERE cd_identif_proponente = row.cd_identif_proponente
			LIMIT 1;
			
			INSERT INTO data.tb_osc (bosc_nr_identificacao, dcti_cd_tipo, bosc_nm_osc)
			VALUES (conv.cd_identif_proponente::NUMERIC, 
				1, 
				SUBSTRING(conv.nm_proponente FROM 1 FOR 250));
			
			SELECT INTO id bosc_sq_osc 
			FROM data.tb_osc 
			WHERE bosc_nr_identificacao = row.cd_identif_proponente::NUMERIC;
			
			BEGIN
				INSERT INTO data.tb_localizacao ("bosc_sq_osc", "mdfd_cd_fonte_dados", "loca_ds_endereco", "loca_nm_bairro", "edmu_cd_municipio", "loca_nm_cep")
				SELECT id, 
				       13, 
				       SUBSTRING(tx_endereco_proponente FROM 1 FOR 200), 
				       SUBSTRING(tx_bairro_proponente FROM 1 FOR 200), 
				       (SELECT edmu_cd_municipio FROM spat.ed_municipio WHERE UNACCENT(LOWER(edmu_nm_municipio)) = UNACCENT(LOWER(nm_municipio_proponente)) AND eduf_cd_uf = (SELECT eduf_cd_uf FROM spat.ed_uf WHERE LOWER(eduf_sg_uf) = LOWER(uf_proponente))),
				       REPLACE(nr_cep_proponente, '-', '')::NUMERIC
				FROM temp.tb_osc_siconv_2015_completa 
				WHERE cd_identif_proponente = row.cd_identif_proponente;
				
				INSERT INTO data.tb_contatos ("bosc_sq_osc", "mdfd_cd_fonte_dados", "cont_nm_representante")
				SELECT id, 
				       13, 
				       SUBSTRING(nm_respons_proponente FROM 1 FOR 250)
				FROM temp.tb_osc_siconv_2015_completa
				WHERE cd_identif_proponente = row.cd_identif_proponente;
			EXCEPTION
				WHEN unique_violation THEN
					RAISE NOTICE 'data.tb_localizacao or data.tb_contatos / unique_violation: %', id;
					
					INSERT INTO syst.ct_log_carga (mdfd_cd_fonte_dados, ctlc_nr_cnpj_osc, dcsc_cd_satus, ctlc_tx_mensagem)
					VALUES (1, row.id_estab::NUMERIC, 13, 'CNPJ ' || id || 'repetido. Já existe outra entidade com o mesmo identificador cadastrado.');
			END;
		END IF;
		
		INSERT INTO data.tb_osc_siconv ("bosc_sq_osc", "siconv_qt_parceria_finalizada", "siconv_qt_parceria_execucao", "siconv_vl_global", "siconv_vl_repasse", "siconv_vl_contrapartida_financeira", "siconv_vl_contrapartida_outras", "siconv_vl_empenhado", "siconv_vl_desembolsado")
		SELECT id, 
		       (SELECT COUNT(*) FROM temp.tb_osc_siconv_2015_completa WHERE (tx_situacao = 'Em execução' OR tx_situacao = 'Assinado') AND cd_identif_proponente = row.cd_identif_proponente), 
		       (SELECT COUNT(*) FROM temp.tb_osc_siconv_2015_completa WHERE (tx_situacao ILIKE '%Prestação de Contas%') AND cd_identif_proponente = row.cd_identif_proponente), 
		       SUM(vl_global), 
		       SUM(vl_repasse), 
		       SUM(vl_contrapartida_financ), 
		       SUM(vl_contrapartida_total) - SUM(vl_contrapartida_financ), 
		       SUM(vl_empenhado), 
		       SUM(vl_desembolsado)
		FROM temp.tb_osc_siconv_2015_completa
		WHERE cd_identif_proponente = row.cd_identif_proponente;
	END LOOP;
	RETURN;
END;

$$ LANGUAGE plpgsql;

SELECT data.load_siconv()