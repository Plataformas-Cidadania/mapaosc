	var alturaTopo = $("#barra-brasil").outerHeight() + $("#topo").outerHeight();
	var alturaRodape = $("#rodape").outerHeight();

	$(document).ready(function(){
		$('input[placeholder]').inputHints();
		$('a[href=#busca]').click(function(){
			$('#campobusca').focus();
			return false;
		});
		/* Menu lateral expansivel */
		$("#mapa_expandir button#expandir").attr('title','Fechar menu');
		$('#expandir_menu').hide();
		$('#fechar_menu').show();

		/* Dados gerais */
		$(".tabbody#regiao ul.dados").hide();
		$(".tabbody#regiao h4 a:first-child").click(function(){
			$(this).parent().next('ul.dados').slideToggle(500);
			$(this).children('em.dados').toggleClass('dados_abertos');
			return false;
		});

		$('.collapse-click').click(function() 
			{ 
				$(this).closest('.collapsable').toggleClass('collapsed') 
			}
		);

		$('.organizacao span').text($('.organizacao span').text().substring(0,90)+'...');

		/* Tabs no menu expansivel */
		$(".tabs:not(.infograficos)").accessibleTabs({
			tabhead:'h2',
			fx:"show",
			fxspeed:null,
			autoAnchor:true
		});

		/* Contraste */
		$('.contraste').hide();

		/* Identificar a resolucao da tela para redimensionar o mapa e o menu */
		redimensionarTela();

		tooltips_padrao();

		$('.box_frame').fancybox({
				type: 'iframe',
				closeClick	: false,
				openEffect	: 'none',
				closeEffect	: 'none',
				autoSize : false,
				padding: 0,
				helpers : {
					overlay : {
						css : {
							'background' : 'rgba(0,0,0,0.6)'
						}
					}
				},
				width: 350,
				height: 180,
				tpl: {
					closeBtn:'none'
				},
				scrollOutside: true
		});

	});

	/* Contraste */
	$("#contraste_normal").click(function(){
		chooseStyle('none');
		$('.normal').show();
		$('.contraste').hide();
	});

	$("#alto_contraste").click(function(){
		chooseStyle('contrast', 0);
		$('.contraste').show();
		$('.normal').hide();
	});
	
	$(window).resize(function(){
		redimensionarTela();
	});	

	/* Tooltips */

	function tooltips_padrao(){

		$(document).on('mouseover', '.tooltip', function(event) {
			$(this).qtip({
				overwrite: true,
				show: {
					event: event.type,
					ready: true
				},
				position: {
					my: 'bottom center',
					at: 'top center',
					viewport: $(window)
				},
				style: {
					classes: 'qtip-tipsy',
					tip: false
				}
			}, event);
		});

		$('#mapa_navegacao h4 a').qtip({
			position: {
				my: 'bottom left',
				at: 'top center'
			},
			style: {
				classes: 'qtip-tipsy',
				tip: false
			}
		});
		$('#ajuda').qtip({
			position: {
				viewport: $(window)
			},
			content: {
				text: $('div.ajuda.busca')  
			},
			style: {
				classes: 'qtip-tipsy',
				tip: false
			}
		});
		$('#ajuda_filtros').qtip({
			position: {
				viewport: $(window)
			},
			content: {
				text: $('div.ajuda.filtros')  
			},
			style: {
				classes: 'qtip-tipsy',
				tip: false
			}
		});
		$('#ajuda_local button').qtip({
			position: {
				viewport: $(window)
			},
			content: {
				text: $('#tip_ajuda_local')
			},
			style: {
				classes: 'qtip-tipsy',
				tip: false
			}
		});
		$('.tip_recomendacao').qtip({
			position: {
				my: 'bottom center',
				at: 'top center',
				viewport: $(window)
			},
			style: {
				classes: 'qtip-tipsy',
				width: 140,
				tip: false
			}
		});
		$('#logo').qtip({
			position: {
				my: 'top center',
				at: 'bottom center',
				viewport: $(window)
			},
			style: {
				classes: 'qtip-tipsy',
				tip: false
			}
		});
		$('#logo_contraste').qtip({
			position: {
				my: 'top center',
				at: 'bottom center',
				viewport: $(window)
			},
			style: {
				classes: 'qtip-tipsy-contraste',
				tip: false
			}
		});
		$('.tip_menu').qtip({
			position: {
				my: 'bottom center',
				at: 'top center',
				viewport: $(window)
			},
			style: {
				classes: 'qtip-tipsy',
				tip: false
			}
		});
	}
	/* Redimensionar o mapa e o menu no resize da janela */
	function redimensionarTela() {
		if($(window).height()<600) {
			var newh = parseInt($(window).height())-alturaTopo-alturaRodape;
			if(newh<370) newh = 370;	// 370 px é a altura mínima do mapa		
			if(newh<600) {
				$('#mapa:not(.infograficos)').css('min-height', newh + 'px');
				$("#mapa_navegacao").css('min-height',newh-110);
			}
		}
		else {
			$('#mapa:not(.infograficos)').css('min-height', parseInt($(window).height())-alturaTopo-alturaRodape); // volta à altura original
			$("#mapa_navegacao").css('min-height',parseInt($(window).height())-alturaTopo-alturaRodape-110);
		}
	}

	/* Texto dentro do campo de busca */

	// jQuery Input Hints plugin
	// Copyright (c) Rob Volk

	(function ($) {
		$.fn.inputHints = function () {
			function showHints(el) {
				if (jQuery(el).val() == '')
					jQuery(el).val($(el).attr('placeholder'))
				.addClass('hint');
			};

			function hideHints(el) {
				if ($(el).val() == $(el).attr('placeholder'))
					$(el).val('')
				.removeClass('hint');
			};

	        // hides the input display text stored in the placeholder on focus
	        // and sets it on blur if the user hasn't changed it.

	        var el = $(this);

	        // show the display text on empty elements
	        el.each(function () {
	        	showHints(this);
	        });

	        // clear the hints on form submit
	        el.closest('form').submit(function () {
	        	el.each(function () {
	        		hideHints(this);
	        	});
	        	return true;
	        });

	        // hook up the blur & focus
	        return el.focus(function () {
	        	hideHints(this);
	        }).blur(function () {
	        	showHints(this);
	        });
	    };
	})(jQuery);


	$("#mapa_expandir button#expandir").click(function(){
		$("#mapa_navegacao").slideToggle(500);
		if ($(this).attr('title')==='Fechar menu') {
			$(this).attr('title','Abrir menu');
			$('#fechar_menu').hide();
			$('#expandir_menu').show();
		} else {
			$(this).attr('title','Fechar menu');
			$('#expandir_menu').hide();
			$('#fechar_menu').show();
		}

	});

