CREATE OR REPLACE FUNCTION data.load_tb_osc_rais()

RETURNS void AS $$

DECLARE
	row RECORD;

BEGIN
	DELETE FROM data.tb_osc_rais;
	
	FOR row IN
		SELECT * 
		FROM temp.tb_osc_rais_2013_completa AS rais LEFT JOIN data.tb_osc AS osc 
		ON rais.id_estab::NUMERIC = osc.bosc_nr_identificacao
	LOOP
		BEGIN
			IF row.bosc_sq_osc IS NULL
			THEN
				IF length(row.id_estab) = 14
				THEN
					INSERT INTO data.tb_osc (bosc_nr_identificacao, dcti_cd_tipo, bosc_nm_osc)
					VALUES (row.id_estab::NUMERIC, 1, row.razao_social);
				ELSE
					INSERT INTO data.tb_osc (bosc_nr_identificacao, dcti_cd_tipo, bosc_nm_osc)
					VALUES (row.id_estab::NUMERIC, 2, row.razao_social);
				END IF;
				
				SELECT INTO row.bosc_sq_osc bosc_sq_osc FROM data.tb_osc WHERE bosc_nr_identificacao = row.id_estab::NUMERIC;
				
				IF row.cep_estab = '' THEN row.cep_estab = '-1'; END IF;
				
				BEGIN
					INSERT INTO data.tb_localizacao ("bosc_sq_osc", "mdfd_cd_fonte_dados", "loca_ds_endereco", "loca_nm_bairro", "edmu_cd_municipio", "loca_nm_cep")
					VALUES(row.bosc_sq_osc, 
					       1, 
					       SUBSTRING(COALESCE(row.nome_logradouro, '') || ' ' || COALESCE(row.num_logradouro, '') FROM 1 FOR 200), 
					       SUBSTRING(row.nome_bairro FROM 1 FOR 200), 
					       (SELECT edmu_cd_municipio FROM spat.ed_municipio WHERE SUBSTRING(edmu_cd_municipio::TEXT FROM 1 FOR 6) = row.codemun), 
					       row.cep_estab::NUMERIC);
				EXCEPTION
					WHEN not_null_violation THEN
						--RAISE NOTICE 'data.tb_localizacao / not_null_violation: %', row.id_estab;
						
						INSERT INTO syst.ct_log_carga (mdfd_cd_fonte_dados, ctlc_nr_cnpj_osc, dcsc_cd_satus, ctlc_tx_mensagem)
						VALUES (1, row.id_estab::NUMERIC, 2, 'Não foi possível cadastrar o endereço devido a não identificação do código do município.');
				END;
			
				INSERT INTO data.tb_contatos ("bosc_sq_osc", "mdfd_cd_fonte_dados", "cont_tx_telefone", "cont_ee_email")
				VALUES(row.bosc_sq_osc, 
				       1, 
				       SUBSTRING(row.num_telefone FROM 1 FOR 200), 
				       SUBSTRING(row.email_estab FROM 1 FOR 200));
			END IF;
			SELECT INTO row.bosc_sq_osc bosc_sq_osc FROM data.tb_osc WHERE bosc_nr_identificacao = row.id_estab::NUMERIC;
			
			INSERT INTO data.tb_osc_rais ("bosc_sq_osc", "dcsc_cd_subclasse", "dcnj_cd_natureza_juridica", "dcte_cd_tamanho_estabelecimento", "rais_qt_vinculo_clt", "rais_qt_vinculo_ativo", "rais_qt_vinculo_estatutario", "rais_in_atividade", "rais_in_negativa", "rais_in_osc") 
			VALUES (row.bosc_sq_osc, 
				row.sbcl_cnae20::NUMERIC, 
				row.nat_jur2009::NUMERIC, 
				row.tamestab::NUMERIC, 
				row.qtd_vinc_clt::INTEGER, 
				row.qtd_vinc_ativos::INTEGER, 
				row.qtd_vinc_estat::INTEGER, 
				row.ind_ativ_ano::BOOLEAN, 
				row.ind_rais_neg::BOOLEAN, 
				row.rais_in_osc::BOOLEAN);
			
		EXCEPTION WHEN unique_violation THEN
			--RAISE NOTICE 'data.tb_osc_rais / unique_violation %', row.id_estab;

			INSERT INTO syst.ct_log_carga (mdfd_cd_fonte_dados, ctlc_nr_cnpj_osc, dcsc_cd_satus, ctlc_tx_mensagem)
			VALUES (1, row.id_estab::NUMERIC, 3, 'CNPJ ' || row.id_estab || 'repetido. Já existe outra entidade com o mesmo identificador cadastrado.');
		END;

	END LOOP;
	RETURN;
END;

$$ LANGUAGE plpgsql;

SELECT data.load_tb_osc_rais()