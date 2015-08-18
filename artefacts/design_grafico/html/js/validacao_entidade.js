$(document).ready(function(){
        
        jQuery.validator.addMethod("dateBR", function(value, element) {
          return value.match(/^\d\d?\/\d\d\/\d\d\d\d$/);
          },
          "Por favor, informe uma data válida"
          );

        jQuery.validator.addMethod("verificaCNPJ", function(cnpj, element) {
             cnpj = jQuery.trim(cnpj);// retira espaços em branco
             // DEIXA APENAS OS NÚMEROS
             cnpj = cnpj.replace('/','');
             cnpj = cnpj.replace('.','');
             cnpj = cnpj.replace('.','');
             cnpj = cnpj.replace('-','');
           
             var numeros, digitos, soma, i, resultado, pos, tamanho, digitos_iguais;
             digitos_iguais = 1;
           
             if (cnpj.length < 14 && cnpj.length < 15){
                return false;
             }
             for (i = 0; i < cnpj.length - 1; i++){
                if (cnpj.charAt(i) != cnpj.charAt(i + 1)){
                   digitos_iguais = 0;
                   break;
                }
             }
           
             if (!digitos_iguais){
                tamanho = cnpj.length - 2;
                numeros = cnpj.substring(0,tamanho);
                digitos = cnpj.substring(tamanho);
                soma = 0;
                pos = tamanho - 7;
           
                for (i = tamanho; i >= 1; i--){
                   soma += numeros.charAt(tamanho - i) * pos--;
                   if (pos < 2){
                      pos = 9;
                   }
                }
                resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
                if (resultado != digitos.charAt(0)){
                   return false;
                }
                tamanho = tamanho + 1;
                numeros = cnpj.substring(0,tamanho);
                soma = 0;
                pos = tamanho - 7;
                for (i = tamanho; i >= 1; i--){
                   soma += numeros.charAt(tamanho - i) * pos--;
                   if (pos < 2){
                      pos = 9;
                   }
                }
                resultado = soma % 11 < 2 ? 0 : 11 - soma % 11;
                if (resultado != digitos.charAt(1)){
                   return false;
                }
                return true;
             }else{
                return false;
             }
          }, "Informe um CNPJ válido."); // Mensagem padrão 

          
        $('#entidade_edicao').validate({
            rules:{
              ecnpj:{
                required: true,
                verificaCNPJ: true
              },
              eendereco:{
                required: true
              },
              elat: {
                required: true
              },
              elong: {
                required: true
              },
              eatividade: {
                required: true
              },
              edesc: {
                required: true
              },
              efundacao: {
                required: true,
                digits: true
              },
              esite: {
                required: true
              },
              eemail_org: {
                required: true,
                email: true
              },
              epublico: {
                required: true
              },
              erecursos: {
                required: true
              },
              efuncionarios: {
                required: true,
                digits: true
              },
              eprestadores: {
                required: true,
                digits: true
              },
              evoluntarios: {
                required: true,
                digits: true
              },
              nparcerias: {
                required: true,
                digits: true
              },
              vparcerias: {
                required: true,
                digits: true
              },
              ehabilitacao: {
                required: true
              },
              eqtd: {
                required: true,
                digits: true
              },
              data_inicio: {
                dateBR: true
              },
              data_termino: {
                dateBR: true
              }
            },
            messages:{
              ecnpj:{
                required: 'Campo obrigatório.',
                verificaCNPJ: 'CNPJ inválido'
              },
              eendereco:{
                required: 'Campo obrigatório.'
              },
              elat: {
                required: 'Campo obrigatório.'
              },
              elong: {
                required: 'Campo obrigatório.'
              },
              eatividade: {
                required: 'Campo obrigatório.'
              },
              edesc: {
                required: 'Campo obrigatório.'
              },
              efundacao: {
                required: 'Campo obrigatório.',
                digits: 'O campo deve conter apenas números.'
              },
              esite: {
                required: 'Campo obrigatório.'
              },
              eemail_org: {
                required: 'Campo obrigatório.',
                email: 'Informe um endereço de e-mail válido.'
              },
              epublico: {
                required: 'Campo obrigatório.'
              },
              erecursos: {
                required: 'Campo obrigatório.'
              },
              efuncionarios: {
                required: 'Campo obrigatório.',
                digits: 'O campo deve conter apenas números.'
              },
              eprestadores: {
                required: 'Campo obrigatório.',
                digits: 'O campo deve conter apenas números.'
              },
              evoluntarios: {
                required: 'Campo obrigatório.',
                digits: 'O campo deve conter apenas números.'
              },
              nparcerias: {
                required: 'Campo obrigatório.',
                digits: 'O campo deve conter apenas números.'
              },
              vparcerias: {
                required: 'Campo obrigatório.',
                digits: 'O campo deve conter apenas números.'
              },
              ehabilitacao: {
                required: 'Campo obrigatório.'
              },
              eqtd: {
                required: 'Campo obrigatório.',
                digits: 'O campo deve conter apenas números.'
              },
              data_inicio: {
                dateBR: 'Data inválida.'
              },
              data_termino: {
                dateBR: 'Data inválida.'
              }
            },
            errorClass: 'erro_ent'
          });
        
});
