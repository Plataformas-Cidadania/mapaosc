				*****************************************************
				* 	   Mapa das Organizacoes da Sociedade Civil 	*
				* Analise da RAIS Estabelecimento Identificada 2013 *
				*      Instituto de Pesquisa Economica Aplicada		*
				*                   Outubro de 2015		 	 		*
				*****************************************************

/* Nota explicativa

A limpeza da RAIS Estabelecimento 2013 foi feita com base em criterios da FASFIL 2010
e filtros feitos limpeza da RAIS 2011.
*/

**************************************************************				
/***************** Leitura da base de dados *****************/
**************************************************************

use "\\srjn3\area_corporativa\Projeto_IPEADATA\Temporario\MapaOSC\estab2013.dta", clear

**************************************************************
/********** Selecao de observacoes de acordo com: ***********/

* a) Natureza juridica;
* b) Atividade no ano; e
* c) Vinculos ativos.
**************************************************************

keep if regexm(nat_jur2009, "^3")
gen rais_in_osc = .
replace rais_in_osc = 1 if nat_jur2009=="3069" | ///
	nat_jur2009=="3204" | ///
	nat_jur2009=="3212" | ///
	nat_jur2009=="3220" | ///
	nat_jur2009=="3239" | ///
	nat_jur2009=="3999"

replace rais_in_osc = 0 if ind_ativ_ano=="0" & ind_rais_neg=="1"

******************************************************************
/********* Identificacao de CNPJs e CEIs repetidos **************/
******************************************************************

sort id_estab
quietly by id_estab: gen dup_id_estab = cond(_N==1,0,_n)
count if dup_id_estab>0 

sort id_estab cei_vinc
quietly by id_estab cei_vinc: gen dup_id_cei = cond(_N==1,0,_n)
count if dup_id_cei>0

* Remover pessoas fisicas

replace rais_in_osc = 0 if tipo_estab=="3" & ///
	(regexm(razao_social, "^CONGREGACAO")==0 | ///
	regexm(razao_social, "^SITIO")==0 | ///
	regexm(razao_social, "^ASSOC")==0 | ///
	regexm(razao_social, "^SOCIEDADE")==0 | ///
	regexm(razao_social, "IGREJA")==0 | ///
	regexm(razao_social, "^COMUNIDADE")==0 | ///
	regexm(razao_social, "^CHACARA")==0 | ///
	regexm(razao_social, "^CAIXA")==0 | ///
	regexm(razao_social, "^CAMARA")==0 | ///
	regexm(razao_social, "^PAROQUIA")==0 | ///
	regexm(razao_social, "^ARQUIDIOCESE")==0 | ///
	regexm(razao_social, "^ORDEM")==0 | ///
	regexm(razao_social, "^FAZ")==0 | ///
	regexm(razao_social, "^FRATERNIDADE")==0 | ///
	regexm(razao_social, "DIOCESE")==0 | ///
	regexm(razao_social, "^OBRA")==0 | ///
	regexm(razao_social, "^LAR ")==0 | ///
	regexm(razao_social, "^MINISTERIO")==0 | ///
	regexm(razao_social, "ESTANCIA")==0 | ///
	regexm(razao_social, "UNIAO")==0 | ///
	regexm(razao_social, "MITRA DIOC")==0 | ///
	regexm(razao_social, "PARCERIA")==0 | ///
	regexm(razao_social, "IDN")==0 | ///
	regexm(razao_social, "EDIFICIO")==0 | ///
	regexm(razao_social, "ESPOLIO")==0 | ///
	regexm(razao_social, "AGRO ")==0 | ///
	regexm(razao_social, "CONTABILIDADE")==0)

*************************************************************************************
/******* Selecao de casos de acordo com expressoes regulares na Razao Social *******/
*************************************************************************************

/* A Fasfil 2010 excluiu as entidades dos seguintes subgrupos de finalidades da COPNI Ampliada:

a) Caixas escolares e similares;
b) Partidos politicos;
c) Sindicatos, federacoes e confederacoes;
d) Condominios; 
e) Cartorios; 
f) Sistema S; 
g) Entidade de mediaçao e arbitragem;
h) Comissao de conciliação prévia; 
i) Conselhos, fundos e consorcios municipais;
j) Cemiterios e funerarias. 
*/

/* Caixas escolares e similares */

replace rais_in_osc = 0 if regexm(razao_social, "^CX E") | ///
	regexm(razao_social, "CX[\.]E") | ///
	regexm(razao_social, "CX[\.] E") | ///
	regexm(razao_social, "^CX[\.] E[\.]") | ///
	regexm(razao_social, "^CXA E") | ///
	regexm(razao_social, "^CXA[\.]E") | ///
	regexm(razao_social, "^CAI E") | ///
	regexm(razao_social, "^CAIX E") | ///
	regexm(razao_social, "^CAIXA ESC") | ///
	regexm(razao_social, "^CAIXA E ") | ///
	regexm(razao_social, "^CAIA E ") | ///
	regexm(razao_social, "^CAIXA E[\.]") | ///
	regexm(razao_social, "^CA ES") | ///
	regexm(razao_social, "^CA[\.] ES") | ///
	regexm(razao_social, "^C ESC DA") | ///
	regexm(razao_social, "^C ESC DO") | ///
	regexm(razao_social, "^C ESCOLAR DA") | ///
	regexm(razao_social, "^C[\.] ESCOLAR DA")

replace rais_in_osc = 0 if regexm(razao_social, "CX ESC") & ///
	regexm(razao_social, "PAIS")==0 & regexm(razao_social, "CONS")==0

/* Partidos politicos */

replace rais_in_osc = 0 if regexm(razao_social, "^PARTIDO") & ///
	regexm(razao_social, "^PARTIDO DO MERITO")==0

replace rais_in_osc = 0 if regexm(razao_social, "DIR MUN") & ///
	regexm(razao_social, "UNDIME")==0 & ///
	regexm(razao_social, "PGU")==0

replace rais_in_osc = 0 if regexm(razao_social, "DIR[\.] MUN") | ///
	regexm(razao_social, "DIR[\.]MUN") | ///
	regexm(razao_social, "DIRETORIO MUN ") | ///
	regexm(razao_social, "DIRETORIO MUN[\.]") | ///
	regexm(razao_social, "DIRETORIO M ") | ///
	regexm(razao_social, "DIRETORIO M[\.]") | ///
	regexm(razao_social, "DIRETORIO DO") | ///
	regexm(razao_social, "DIRETRIO MUNICIPAL") | ///
	regexm(razao_social, "DIRETORIO MUNICIPAL") | ///
	regexm(razao_social, "DIRETORIO DA COMISSAO") | ///
	regexm(razao_social, "DIRETORIO MUNIC[\.]") | ///
	regexm(razao_social, "DIRETORIO MINIC[\.]") | ///
	regexm(razao_social, "DIRETORIO MUNIC ") | ///
	regexm(razao_social, "DIRETORIO PARTIDO") | ///
	regexm(razao_social, "DIRET MUN") | ///
	regexm(razao_social, "DIRET[\.] MUN") | ///
	regexm(razao_social, "DIRET.DO") | ///
	regexm(razao_social, "DIRET[\.] DO") | ///
	regexm(razao_social, "DIRETORIA MUN") | ///
	regexm(razao_social, "DIR[\.] MUN.") | ///
	regexm(razao_social, "DIR[\.]MUN[\.]") | ///
	regexm(razao_social, "DIR DO PARTIDO") | ///
	regexm(razao_social, "COMISSAO EXECUTIVA") | ///
	regexm(razao_social, "^COM EXEC") | ///
	regexm(razao_social, "COMIS E") | ///
	regexm(razao_social, "COMISSAO EXEC ") | ///
	regexm(razao_social, "COM PROVISORIA") | ///
	regexm(razao_social, "COM[\.] PROV") | ///
	regexm(razao_social, "COM PROV") | ///
	regexm(razao_social, "COMISSAO PROV") | ///
	regexm(razao_social, "COMISAO PROV") | ///
	regexm(razao_social, "C P M DO DEM") | ///
	regexm(razao_social, "C P M DO PSB") | ///
	regexm(razao_social, "COM EXER") | ///
	regexm(razao_social, "DIRECAO MUN")
	
replace rais_in_osc = 0 if regexm(razao_social, "COMISSAO MUNIC PROVI") | ///
	regexm(razao_social, "COMISSAO MUNICIPAL DO P") | ///
	regexm(razao_social, "COMISSAO MUN PROV")

replace rais_in_osc = 0 if regexm(razao_social, "DIRETORIO MINICIPAL")

replace rais_in_osc = 0 if regexm(razao_social, "COMITE MUN") & ///
	regexm(razao_social, "ASSOC")==0 & ///
	regexm(razao_social, "CIDADANIA")==0 & /// 
	regexm(razao_social, "APRENDIZ")==0
	
replace rais_in_osc = 0 if regexm(razao_social, "DIRETORIO REG") & ///
	regexm(razao_social, "ESTUDANTES")==0 & ///
	regexm(razao_social, "CONGADO")==0

replace rais_in_osc = 0 if regexm(razao_social, "COMISSAO DIRETORA") | ///
	regexm(razao_social, "COMISSAO DIRIGENTE") | ///
	regexm(razao_social, "DIR[\.] REGIONAL") | ///
	regexm(razao_social, "COMIS PROVIS") | ///
	regexm(razao_social, "COMIS PROVIS") | ///
	regexm(razao_social, "DIRETORIO MUNC") | ///
	regexm(razao_social, "ORGAO MUN")

replace rais_in_osc = 0 if regexm(razao_social, "DIRETORIO -") | ///
	regexm(razao_social, "DIRETORIO-") | ///
	regexm(razao_social, "DIRETORIO PART") | ///
	regexm(razao_social, "DIRET PARTIDO") | ///
	regexm(razao_social, "PARTRIDO") | ///
	regexm(razao_social, "DIRET PART") | ///
	regexm(razao_social, "COMIS[\.]PROV[\.]") | ///
	regexm(razao_social, "DIRETORIA PARTIDO") | ///
	regexm(razao_social, "DIRETORIO MUNICILA") | ///
	regexm(razao_social, "DIRETORIO MUNIPAL") | ///
	regexm(razao_social, "DIRETORIO ESTADUAL") | ///
	regexm(razao_social, "COMISSAO PRO MUN") | ///
	regexm(razao_social, "DIRETORIO MUNICIP") | ///
	regexm(razao_social, "DIRETO RIO") | ///
	regexm(razao_social, "COMISAO PRO") | ///
	regexm(razao_social, "DIRET ESTADUAL")

replace rais_in_osc = 0 if regexm(razao_social, "DIRETORIO DEMO") | ///
	regexm(razao_social, "DIRETORIO DE P") | ///
	regexm(razao_social, "PSDB - DIRETORIO") | ///
	regexm(razao_social, "PT DIRETORIO") | ///
	regexm(razao_social, "PART DO MOV") | ///
	regexm(razao_social, "DEMOCRATAS - DIRETORIO")

replace rais_in_osc = 0 if regexm(razao_social, "PTB") | ///
	regexm(razao_social, "PSDB") | ///
	regexm(razao_social, "PMDB") | ///
	regexm(razao_social, "^PT DE") | ///
	regexm(razao_social, "DO PT DE") | ///
	regexm(razao_social, "S PT DE") | ///
	regexm(razao_social, "[\.]PT DE") | ///
	regexm(razao_social, "^PT D") | ///
	regexm(razao_social, "PT DE C") | ///
	regexm(razao_social, "-PT D") | ///
	regexm(razao_social, "DO PT D") | ///
	regexm(razao_social, "[\.]PT D") | ///
	regexm(razao_social, "^PSD") | ///
	regexm(razao_social, " PSD$") | ///
	regexm(razao_social, "PDT") | ///
	regexm(razao_social, "^PSB") | ///
	regexm(razao_social, "^PSB ") | ///
	regexm(razao_social, "^PRB ") | ///
	regexm(razao_social, "PRB-") | ///
	regexm(razao_social, "^PPS ") | ///
	regexm(razao_social, "^PT ") | ///
	regexm(razao_social, "^PT[\.]") | ///
	regexm(razao_social, "^PMN ") | ///
	regexm(razao_social, "^PMN") | ///
	regexm(razao_social, "PRTB") | ///
	regexm(razao_social, "^PSL") | ///
	regexm(razao_social, "^PSC ") | ///
	regexm(razao_social, "^PTC") | ///
	regexm(razao_social, "^PTN") | ///
	regexm(razao_social, "^PV ") | ///
	regexm(razao_social, "PPB") | ///
	regexm(razao_social, "^PC DO") | ///
	regexm(razao_social, "PC DO B$") | ///
	regexm(razao_social, "PART COMUN") | ///
	regexm(razao_social, "^P[\.]P[\.]B") | ///
	regexm(razao_social, "P[\.]H[\.]S") | ///
	regexm(razao_social, "P S B") | ///
	regexm(razao_social, "P S D") | ///
	regexm(razao_social, "P T") | ///
	regexm(razao_social, "P D T") | ///
	regexm(razao_social, "^P T B") | ///
	regexm(razao_social, "P R T B") | ///
	regexm(razao_social, "^P P") | ///
	regexm(razao_social, "^P S L") | ///
	regexm(razao_social, "PARTIDO DOS T") | ///
	regexm(razao_social, "PART TRAB") | ///
	regexm(razao_social, "MOV DEM") | ///
	regexm(razao_social, "PART DEM") | ///
	regexm(razao_social, "PART PROGR") | ///
	regexm(razao_social, "^PFL") | ///
	regexm(razao_social, " PFL") | ///
	regexm(razao_social, "PARTIDO DA FR") | ///
	regexm(razao_social, "^PR PAR") | ///
	regexm(razao_social, "^PR -") | ///
	regexm(razao_social, "^PR P") | ///
	regexm(razao_social, "^PR S") | ///
	regexm(razao_social, "^PR DE") | ///
	regexm(razao_social, "^PR [\(]") | ///
	regexm(razao_social, "PARTIDO DA REP")

replace rais_in_osc = 0 if regexm(razao_social, "PARTIDO D") | ///
	regexm(razao_social, "PARTIDO DO MOVIM") | ///
	regexm(razao_social, "PARTIDO DO MOVT") | ///
	regexm(razao_social, "-PARTIDO") | ///
	regexm(razao_social, "^PART ") | ///
	regexm(razao_social, "^PART[\.]") | ///
	regexm(razao_social, "^PPS") | ///
	regexm(razao_social, "^DIRE[\.]M") | ///
	regexm(razao_social, "^DIRET[\.]M") | ///
	regexm(razao_social, "REG[\.]PARTIDO")

replace rais_in_osc = 0 if regexm(razao_social, " PARTIDO ") & /// 
	regexm(razao_social, "gremio")==0

replace rais_in_osc = 0 if regexm(razao_social, "DEMOCRATAS") & ///
	regexm(razao_social, "SINDICATO")==0
	
* Sindicatos, federacoes e confederacoes

replace rais_in_osc = 0 if clas_cnae20=="94201"

replace rais_in_osc = 0 if regexm(razao_social, "^SINDI") | ///
	regexm(razao_social, "^SIND ") | ///
	regexm(razao_social, "^SIND[\.]")
 
replace rais_in_osc = 0 if (regexm(razao_social, " SIND ") & ///
	regexm(razao_social, "ASS")==0 & ///
	regexm(razao_social, "GREMIO")==0 & ///
	regexm(razao_social, "OCB")==0 & ///
	regexm(razao_social, "CLUBE")==0 & ///
	regexm(razao_social, "SERV SOC")==0)

replace rais_in_osc = 0 if (regexm(razao_social, " SINDICATO D") & ///
	regexm(razao_social, "ASS")==0 & ///
	regexm(razao_social, "COLONIA")==0)

replace rais_in_osc = 0 if (regexm(razao_social, "^SIN") & ///
	regexm(razao_social, "^SINODO")==0 & ///
	regexm(razao_social, "^SINAGOGA")==0 & ///
	regexm(razao_social, "LTDA")==0 & ///
	regexm(razao_social, "ASSOC")==0 & ///
	regexm(razao_social, "CONSULT")==0 & ///
	regexm(razao_social, "SINE")==0 & ///
	regexm(razao_social, "SINVAL")==0)

replace rais_in_osc = 0 if regexm(razao_social, "SIND EMP") | ///
	regexm(razao_social, "SINDICATO TRAB") | ///
	regexm(razao_social, "SINDICATO PAT") | ///
	regexm(razao_social, "SINDICATO ESP") | ///
	regexm(razao_social, "SINDICATO IND") | ///
	regexm(razao_social, "-SIND") | ///
	regexm(razao_social, "SINDC[\.]") | ///
	regexm(razao_social, "SINDDOS") | ///
	regexm(razao_social, "SINDEGETURES") | ///
	regexm(razao_social, "SINDEMPCOMHORTRESTCHURPIZ") | ///
	regexm(razao_social, "SIEMESP")
	
replace rais_in_osc = 0 if regexm(razao_social, "SIND[\.]D") | ///
	regexm(razao_social, " SIND[\.] D") & ///
	regexm(razao_social, "ASSOC")==0 & ///
	regexm(razao_social, "CENTRO")==0 & ///
	regexm(razao_social, "FEDERACAO")==0
	
replace rais_in_osc = 0 if regexm(razao_social, "^FED") & ///
	regexm(razao_social, "SIND[\.]")

replace rais_in_osc = 0 if regexm(razao_social, "CONFED") & ///
	regexm(razao_social, "SIND")

replace rais_in_osc = 0 if regexm(razao_social, "^FEDERACAO DOS SIN")

replace rais_in_osc = 0 if regexm(razao_social, "^FED") & ///
	regexm(razao_social, "SIND ") | ///
	regexm(razao_social, "SIND[\.]") & ///
	regexm(razao_social, "GREMIO")==0 & ///
	regexm(razao_social, "^ASSOC")==0 & ///
	regexm(razao_social, "^CENTRO")==0 & ///
	regexm(razao_social, "^ASSC")==0 & ///
	regexm(razao_social, "^CEC")==0 & ///
	regexm(razao_social, "^SERV")==0
	
replace rais_in_osc = 0 if regexm(razao_social, "^FED") & ///
	regexm(razao_social, "TRAB") & ///
	regexm(razao_social, "IMORTAL")==0

* Condominios

replace rais_in_osc = 0 if regexm(razao_social, "^CONDOMINIO") | ///
	regexm(razao_social, "^COND ") | ///
	regexm(razao_social, "^CON RES") | ///
	regexm(razao_social, "^EDIFICIO CONDOMINIO") | ///
	regexm(razao_social, "^EDIFICIO DO CONDOMINIO") | ///
	regexm(razao_social, "^CONJUNTO RESID") | ///
	regexm(razao_social, "^CONJ RES") | ///
	regexm(razao_social, "^CONJ HAB") | ///
	regexm(razao_social, "^CONJUNTO HAB") | ///
	regexm(razao_social, "^NUCLEO HAB")
	
replace rais_in_osc = 0 if regexm(razao_social, "119 CONDOMINIO DO EDIFICIO") | ///
	regexm(razao_social, "ALLEGRO CONDOMINIO CLUB") | ///
	regexm(razao_social, "ILHAS DO SUL CONDOMINIO NAUTICO") | ///
	regexm(razao_social, "MORADIAS ABAETE 2 CONDOMINIO 8") | ///
	regexm(razao_social, "VILLA CONDOMINIO MORRO DA ESPERANCA") | ///
	regexm(razao_social, "A. CONDOMINIO RECANTO DO PARAISO") | ///
	regexm(razao_social, "APRECEA CONDOMINIO COMARY GLEBA 6 A") | ///
	regexm(razao_social, "HOTEL CAVALINHO BRANCO CONDOMINIO") | ///
	regexm(razao_social, "ESPORTE CLUBE CONDOMINIO") | ///
	regexm(razao_social, "MANUEL COSTA MELO CONDOMINIO") | ///
	regexm(razao_social, "MARIO JOSE BORLINI CONDOMINIO")

* Cartorios

replace rais_in_osc = 0 if regexm(razao_social, "^CARTORIO") | ////
	regexm(razao_social, "DE NOTAS$") | ///
	regexm(razao_social, "^OFICIO") | ///
	regexm(razao_social, "^REGISTRO") | ///
	regexm(razao_social, "^SEGUNDO TAB")

replace rais_in_osc = 0 if regexm(razao_social, "CARTORI") == 1 & ///
	regexm(razao_social, "ASSOC") == 0

* Sistema S (SENAI, SESI, SENAC, SESC, SEBRAE, SENAR, SEST, SENAT, SESCOOP)

replace rais_in_osc = 0 if regexm(razao_social, "^SENAI")

replace rais_in_osc = 0 if regexm(razao_social, "SENAI$") & ///
	regexm(razao_social, "^SERV")

replace rais_in_osc = 0 if regexm(razao_social, " SENAI ") == 1 & ///
	regexm(razao_social, "ASSOC") == 0 & ///
	regexm(razao_social, "AAPM") == 0 & ///
	regexm(razao_social, "^ASS.") == 0

replace rais_in_osc = 0 if regexm(razao_social, "SESI") == 1 & ///
	regexm(razao_social, "CONSELHO") == 0 & ///
	regexm(razao_social, "ASSOC") == 0 & ///
	regexm(razao_social, "FARMACIA") == 0 & ///
	regexm(razao_social, "^APM") == 0 & ///
	regexm(razao_social, "^ASS") == 0 & ///
	regexm(razao_social, "^CENTRO") == 0 & ///
	regexm(razao_social, "A.P.M") == 0 & ///
	regexm(razao_social, "FUNDACAO") == 0 & ///
	regexm(razao_social, "CLUBE") == 0

replace rais_in_osc = 0 if regexm(razao_social, "^SEBRAE")

replace rais_in_osc = 0 if regexm(razao_social, " SEBRAE") == 1 & ///
	regexm(razao_social, "ASSOC") == 0 & /// 
	regexm(razao_social, "ASSOSC") == 0 & ///
	regexm(razao_social, "COLABORADORES") == 0

replace rais_in_osc = 0 if regexm(razao_social, "^SENAR")

replace rais_in_osc = 0 if regexm(razao_social, "SENAR") == 1 & ///
	regexm(razao_social, "ASSOCIACAO") == 0 & ///
	regexm(razao_social, "ASSENAR") == 0

replace rais_in_osc = 0 if (regexm(razao_social, " SESC") == 1 | ///
	regexm(razao_social, "^SESC") == 1) & ///
	regexm(razao_social, "ASSOC") == 0 & ///
	regexm(razao_social, "ASS.") == 0 & ///
	regexm(razao_social, "GREMIO") == 0 & ///
	regexm(razao_social, "INSTITUTO") == 0

replace rais_in_osc = 0 if regexm(razao_social, "SENAC") == 1 & ///
	regexm(razao_social, "ASSOC") == 0 & ///
	regexm(razao_social, "TEMPLO") == 0
	
* Entidades de mediaçao e arbitragem

replace rais_in_osc = 0 if regexm(razao_social, " MEDIACAO")==1 & ///
	regexm(razao_social, "ASS.")==0 & ///
	regexm(razao_social, "INSTIT.")==0

* Comissao de conciliacao previa

replace rais_in_osc = 0 if regexm(razao_social, "COMISSAO DE CON")

* Conselhos, fundos e consorcios municipais

replace rais_in_osc = 0 if regexm(razao_social, "^FUNDO MUN")
replace rais_in_osc = 0 if regexm(razao_social, "CONSORCIO MUN")

* Cemiterios e funerarias

replace rais_in_osc = 0 if regexm(razao_social, "^CEMITER") == 1
replace rais_in_osc = 0 if regexm(razao_social, "^FUNERAR") == 1

replace rais_in_osc = 0 if regexm(razao_social, "ASSOCIACAO FUNERARIA") == 1 | ///
	regexm(razao_social, "ASSOCIACAO DAS FUNERARIAS") == 1 | ///
	regexm(razao_social, "CAIXA FUNERARIA") == 1 

* SAs, LTDAs, Cooperativas

replace rais_in_osc = 0 if regexm(razao_social, "COOPERATIVA") & ///
	regexm(razao_social, "^ASS")==0

replace rais_in_osc = 0 if regexm(razao_social, "^COOP ") | ///
	regexm(razao_social, "^COOP[\.]") | ///
	regexm(razao_social, " S A&") | ///
	regexm(razao_social, " S/A$") | ///
	regexm(razao_social, " S[\.]A$") | ///
	regexm(razao_social, "LTDA") | ///
	regexm(razao_social, "LTD$")
	
replace rais_in_osc = 0 if regexm(razao_social, "S[\.]A[\.]$") & ///
	regexm(razao_social, "^ASSOC")==0 & ///
	regexm(razao_social, "^APMF")==0 & ///
	regexm(razao_social, "^MITRA")==0

replace rais_in_osc = 0 if regexm(razao_social, "RADIO TAXI") & ///
	regexm(razao_social, "^AS")==0

replace rais_in_osc = 0 if regexm(razao_social, " LT$") & ///
	(regexm(razao_social, "^SAMMED") | ///
	regexm(razao_social, "CONSULTORIA") | ///
	regexm(razao_social, "COOP") | ///
	regexm(razao_social, "PREST SERV") | ///
	regexm(razao_social, "PARTICIPACOES") | ///
	regexm(razao_social, "BIJUTERIAS") | ///
	regexm(razao_social, "COMPANY") | ///
	regexm(razao_social, "COMERCIO") | ///
	regexm(razao_social, "PETROLEO"))
	
replace rais_in_osc = 0 if regexm(razao_social, "SOCIEDADE DE ENSINO SUPERIOR ESTACIO DE SA") == 1
	
* Caixas de previdencia

replace rais_in_osc = 0 if regexm(razao_social, "CAIXA DE PREVIDENCIA")
replace rais_in_osc = 0 if regexm(razao_social, "CAIXA DE PREV")
replace rais_in_osc = 0 if regexm(razao_social, "CX PRE")==1

* Laboratorios de analise clinica;

replace rais_in_osc = 0 if regexm(razao_social, "SCMI LABORATORIO DE ANALISES CLINICAS") == 1
replace rais_in_osc = 0 if regexm(razao_social, "^LABORATORIO DE ANALISES CLINICAS") == 1

* Orgaos publicos (prefeituras e secretarias);

replace rais_in_osc = 0 if regexm(razao_social, "^SECRETARIA DE ESTADO")
replace rais_in_osc = 0 if regexm(razao_social, "^SECRETARIA MUNICIPAL")
replace rais_in_osc = 0 if regexm(razao_social, "^PREFEITURA COMUNITARIA")
replace rais_in_osc = 0 if regexm(razao_social, "^PREFEITURA DA")
replace rais_in_osc = 0 if regexm(razao_social, "^PREFEITURA DO SETOR")
replace rais_in_osc = 0 if regexm(razao_social, "SECRETARIA DE ASSISTENCIA SOCIAL")
replace rais_in_osc = 0 if regexm(razao_social, "SECRETARIA DA EDUCACAO")

* Extras

replace rais_in_osc = 0 if regexm(razao_social, "^ESC") & ///
regexm(razao_social, " MUN")

replace rais_in_osc = 0 if regexm(razao_social, "^ESCO") & ///
	regexm(razao_social, " EST") & ///
	regexm(razao_social, "ESTUDOS")==0 & ///
	regexm(razao_social, "SAMBA")==0 & ///
	regexm(razao_social, "CICLISMO")==0 & ///
	regexm(razao_social, "MEDIUNS")==0 & ///
	regexm(razao_social, "ESCOLA EVANGELISTA ESTAVAO SLOOP")==0

replace rais_in_osc = 0 if regexm(razao_social, "^EMBAIXADA D")

* ME, EPP, LTDA

replace rais_in_osc = 0 if regexm(razao_social, "FUNDO DE INV") | ///
	regexm(razao_social, "MARATHON EMERGING MARKETS FUND") | ///
	regexm(razao_social, "TAEL INVESTMENTS (DELAWARE) LLC-MELLON") | ///
	regexm(razao_social, "THE MAXIMA MULTPORTIFOLIO FUND LLC") | ///
	regexm(razao_social, "TAEL INVESTMENTS (DELAWARE) LLC-MELLON") | ///
	regexm(razao_social, " EPP$")
	
replace rais_in_osc = 0 if regexm(razao_social, " ME$") & ///
	regexm(razao_social, "LOC ITALIA$")==0 & ///
	regexm(razao_social, "ASS DE")==0 & ///
	regexm(razao_social, "ASSOC")==0 & ///
	regexm(razao_social, "CONSELHO")==0 & ///
	regexm(razao_social, "COMISSAO")==0 & ///
	regexm(razao_social, "INSTITUTO")==0 & ///
	regexm(razao_social, "FUNDACAO")==0 & ///
	regexm(razao_social, "APMF")==0 & ///
	regexm(razao_social, "IGREJA")==0 & ///
	regexm(razao_social, "CONSORCIO")==0 & ///
	regexm(razao_social, "CONS ")==0 & ///
	regexm(razao_social, "CENTRO")==0 & ///
	regexm(razao_social, "NUCLEO")==0 & ///
	regexm(razao_social, "MEMORIAL")==0 & ///
	regexm(razao_social, "UNID EXEC")==0 & ///
	regexm(razao_social, "^SOC")==0 & ///
	regexm(razao_social, "^APM")==0 & ///
	regexm(razao_social, "INST")==0 & ///
	regexm(razao_social, "MINISTERIO")==0 & ///
	regexm(razao_social, "^A AP P")==0 & ///
	regexm(razao_social, "^ORG")==0 & ///
	regexm(razao_social, "^CE ESC")==0 & ///
	regexm(razao_social, "^ASS[\.]")==0 & ///
	regexm(razao_social, "^PATRONATO")==0 & ///
	regexm(razao_social, "^ABRIGO")==0 & ///
	regexm(razao_social, "^C0MITE")==0 & ///
	regexm(razao_social, "^CEC")==0 & ///
	regexm(razao_social, "^COMUNIDADE")==0 & ///
	regexm(razao_social, "^A P M F")==0 & ///
	regexm(razao_social, "^APP")==0 & ///
	regexm(razao_social, "^A[\.]P[\.]P[\.]")==0

