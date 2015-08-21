	var alturaTopo = $("#barra-brasil").outerHeight() + $("#topo").outerHeight();
	var alturaRodape = $("#rodape").outerHeight();

	$(document).ready(function(){
		$('input[placeholder]').inputHints();
		$("input.cpf").mask('000.000.000-00');
		$('.contraste').hide();
		tooltips_padrao();
		$('#gotosearch').click(function(){
			$('#campobusca').focus();
		});
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
	}
	// Masks
	// jQuery Mask Plugin v1.3.2
	// github.com/igorescobar/jQuery-Mask-Plugin
	(function(c){var w=function(a,d,e){var f=this;a=c(a);var l;d="function"==typeof d?d(a.val(),void 0,a,e):d;f.init=function(){e=e||{};f.byPassKeys=[8,9,16,36,37,38,39,40,46,91];f.translation={0:{pattern:/\d/},9:{pattern:/\d/,optional:!0},"#":{pattern:/\d/,recursive:!0},A:{pattern:/[a-zA-Z0-9]/},S:{pattern:/[a-zA-Z]/}};f.translation=c.extend({},f.translation,e.translation);f=c.extend(!0,{},f,e);a.each(function(){!1!==e.maxlength&&a.attr("maxlength",d.length);a.attr("autocomplete","off");g.destroyEvents();
		g.events();g.val(g.getMasked())})};var g={events:function(){a.on("keydown.mask",function(){l=g.val()});a.on("keyup.mask",g.behaviour);a.on("paste.mask",function(){setTimeout(function(){a.keydown().keyup()},100)})},destroyEvents:function(){a.off("keydown.mask").off("keyup.mask").off("paste.mask")},val:function(v){var d="input"===a.get(0).tagName.toLowerCase();return 0<arguments.length?d?a.val(v):a.text(v):d?a.val():a.text()},behaviour:function(a){a=a||window.event;if(-1===c.inArray(a.keyCode||a.which,
			f.byPassKeys))return g.val(g.getMasked()),g.callbacks(a)},getMasked:function(){var a=[],c=g.val(),b=0,q=d.length,h=0,l=c.length,k=1,r="push",m=-1,n,s;e.reverse?(r="unshift",k=-1,n=0,b=q-1,h=l-1,s=function(){return-1<b&&-1<h}):(n=q-1,s=function(){return b<q&&h<l});for(;s();){var t=d.charAt(b),u=c.charAt(h),p=f.translation[t];p?(u.match(p.pattern)?(a[r](u),p.recursive&&(-1==m?m=b:b==n&&(b=m-k),n==m&&(b-=k)),b+=k):p.optional&&(b+=k,h-=k),h+=k):(a[r](t),u==t&&(h+=k),b+=k)}return a.join("")},callbacks:function(f){var c=
			g.val(),b=g.val()!==l;if(!0===b&&"function"==typeof e.onChange)e.onChange(c,f,a,e);if(!0===b&&"function"==typeof e.onKeyPress)e.onKeyPress(c,f,a,e);if("function"===typeof e.onComplete&&c.length===d.length)e.onComplete(c,f,a,e)}};f.remove=function(){g.destroyEvents();g.val(f.getCleanVal()).removeAttr("maxlength")};f.getCleanVal=function(){for(var a=[],c=g.val(),b=0,e=d.length;b<e;b++)f.translation[d.charAt(b)]&&a.push(c.charAt(b));return a.join("")};f.init()};c.fn.mask=function(a,d){return this.each(function(){c(this).data("mask",
				new w(this,a,d))})};c.fn.unmask=function(){return this.each(function(){try{c(this).data("mask").remove()}catch(a){}})};c("input[data-mask]").each(function(){var a=c(this),d={};"true"===a.attr("data-mask-reverse")&&(d.reverse=!0);"false"===a.attr("data-mask-maxlength")&&(d.maxlength=!1);a.mask(a.attr("data-mask"),d)})})(window.jQuery||window.Zepto);

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