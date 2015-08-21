$(document).ready(function(){

        $('#rcpf').click(function() {
          $('input:radio[name=rcadastro_cpf]')[0].checked = true;
        });

        $('#rcpf_nao').click(function() {
          $('#rcpf').val('');
          $('#rcpf').removeClass('error');
          $('label.error[for=rcpf]').hide();
        });

         function cancelar(validacao,formulario) {
              document.getElementById(formulario).reset();
              validacao.resetForm();
              $('input.error').removeClass('error');
              if (formulario == 'form_redes') {$('input:radio[name=rcadastro_cpf]')[0].checked = true};
              $.fancybox.close();
              return false;
         }

        jQuery.validator.addMethod("verificaCPF", 
          function(value, element) {
              value = value.replace('.','');
              value = value.replace('.','');
              cpf = value.replace('-','');
              while(cpf.length < 11) cpf = "0"+ cpf;
              var expReg = /^0+$|^1+$|^2+$|^3+$|^4+$|^5+$|^6+$|^7+$|^8+$|^9+$/;
              var a = [];
              var b = new Number;
              var c = 11;
              for (i=0; i<11; i++){
                  a[i] = cpf.charAt(i);
                  if (i < 9) b += (a[i] * --c);
              }
              if ((x = b % 11) < 2) { a[9] = 0 } else { a[9] = 11-x }
              b = 0;
              c = 11;
              for (y=0; y<10; y++) b += (a[y] * c--);
              if ((x = b % 11) < 2) { a[10] = 0; } else { a[10] = 11-x; }
              if ((cpf.charAt(9) != a[9]) || (cpf.charAt(10) != a[10]) || cpf.match(expReg)) return false;
              return true;
          }, 
          "Informe um CPF válido."); // Mensagem padrão

          $(function() { 
            var validate_redes = $('#form_redes').validate({
                ignore: [],
                rules:{
                      rusuario:{
                        required: true,
                        minlength: 5
                      },
                      rsenha:{
                        required: true,
                        minlength: 5
                      },
                      rcadastro_cpf: {
                        required: true
                      },
                      redes: {
                        required: true
                      },
                      rcpf:{ //input do CPF
                      verificaCPF: {
                        depends: function() {
                          if ($("#rcpf_sim").is(':checked')){
                            return true;
                          } else { 
                            return false;
                          }
                        }
                      },
                      required: "#rcpf_sim:checked"
                    }
                  },
                messages:{
                      rusuario: {
                        required: 'Informe seu nome de usuário',
                        minlength: 'Digite ao menos {0} caracteres.'
                      },
                      rsenha: {
                        required: 'Informe sua senha',
                        minlength: 'Digite ao menos {0} caracteres.'
                      },
                      redes: {
                        required: 'Selecione uma rede.'
                      },
                      rcpf:{
                      verificaCPF: 'CPF inválido.',
                      required: 'Informe seu CPF.'
                    }
                },
                errorPlacement: function(error, element) {
                    if (element.attr("name") == "redes")
                        {
                            error.insertAfter("div.b-redes");
                        }
                    else
                        {
                            error.insertAfter(element);
                        }
                    }
            });
            
           $('.cancelar').click(function() {
              cancelar(validate_redes, 'form_redes');
            });
           
        });

        $(function() { 
            var validate_cadastro = $('#form_cadastro').validate({
            ignore: [],
            rules:{
              cemail:{
                required: true,
                email: true
              },
              cnome: {
                required: true,
                minlength: 5
              },
              ccpf: {
                verificaCPF: true,
                required: true
              },
              csenha: {
                required: true,
                minlength: 5
              },
              ccsenha: {
                required: true,
                equalTo: '#csenha'
              },
              cinscrever: {
                required: false
              }
            },
            messages:{
              csenha:{
                required: 'Campo obrigatório.',
                minlength: 'Senha muito curta.'
              },
              cemail:{
                required: 'Campo obrigatório.',
                email: 'Por favor, informe um email válido.'
              },
              ccsenha:{
                required: 'Campo obrigatório.',
                equalTo: 'As duas senhas devem ser iguais.'
              },
              cnome:{
                required: 'Campo obrigatório.',
                minlength: 'O nome deve conter ao menos {0} caracteres.'
              },
              ccpf:{
                verificaCPF: 'CPF inválido.',
                required: 'Informe seu CPF.'
              }
            }
          });
          $('.cancelar').click(function() {
              cancelar(validate_cadastro, 'form_cadastro');
          });
        });

        $(function() { 
          var validate_entidade = $('#form_entidade').validate({
            rules: {
              eemail: {
                required: true,
                email: true
              },
              esenha: {
                required: true,
                minlength: 5
              },
              ecsenha: {
                required: true,
                equalTo: '#esenha'
              }
            },
            messages:{
              esenha:{
                required: 'Campo obrigatório.',
                minlength: 'Senha muito curta.'
              },
              eemail:{
                required: 'Campo obrigatório.',
                email: 'Por favor, informe um email válido.'
              },
              ecsenha:{
                required: 'Campo obrigatório.',
                equalTo: 'As duas senhas devem ser iguais.'
              }
            }
          });
          $('.cancelar').click(function() {
              cancelar(validate_entidade, 'form_entidade');
          });
        });


    $(function() { 
              var validate_entidade = $('#form_entidade_config').validate({
                ignore: [],
                  rules: {
                    eemail: {
                      required: true,
                      email: true
                    },
                    enome: {
                      minlength: 5,
                      required: true
                    },
                    esenha: {
                      required: true,
                      minlength: 5
                    },
                    ensenha: {
                      minlength: 5
                    },
                    ecnsenha: {
                      equalTo: '#ensenha'
                    },
                    ecpf: {
                      verificaCPF: true,
                      required: true
                    }
                  },
                  messages:{
                    eemail:{
                      required: 'Campo obrigatório.',
                      email: 'Por favor, informe um email válido.'
                    },
                    enome: {
                      required: 'Campo obrigatório.',
                      minlength: 'O nome deve conter ao menos {0} caracteres.'
                    },
                    esenha:{
                      required: 'Campo obrigatório.',
                      minlength: 'Senha muito curta.'
                    },
                    ensenha: {
                      minlength: 'Senha muito curta.'
                    },
                    ecnsenha:{
                      equalTo: 'As duas senhas devem ser iguais.'
                    },
                    ecpf:{
                      verificaCPF: 'CPF inválido.',
                      required: 'Informe seu CPF.'
                    }
                  },
                  invalidHandler: function(event, validator) {
                    var errors = validator.numberOfInvalids();
                    if (errors) {
                      $('ul.tabs-list li#accessibletabsnavigation0-0 a').click();
                    } // Volta para a primeira aba se ocorrer um erro
                  }
              });
        });


        $(function() { 
            var validate_cadastro = $('#form_cadastro_config').validate({
            ignore: [],
            rules:{
              cemail:{
                required: true,
                email: true
              },
              cnome: {
                required: true,
                minlength: 5
              },
              ccpf: {
                verificaCPF: true,
                required: true
              },
              csenha: {
                required: true,
                minlength: 5
              },
              ccsenha: {
                required: true,
                equalTo: '#csenha'
              }
            },
            messages:{
              csenha:{
                required: 'Campo obrigatório.',
                minlength: 'Senha muito curta.'
              },
              cemail:{
                required: 'Campo obrigatório.',
                email: 'Por favor, informe um email válido.'
              },
              ccsenha:{
                required: 'Campo obrigatório.',
                equalTo: 'As duas senhas devem ser iguais.'
              },
              cnome:{
                required: 'Campo obrigatório.',
                minlength: 'O nome deve conter ao menos {0} caracteres.'
              },
              ccpf:{
                verificaCPF: 'CPF inválido.',
                required: 'Informe seu CPF.'
              }
            },
            invalidHandler: function(event, validator) {
                    var errors = validator.numberOfInvalids();
                    if (errors) {
                      $('ul.tabs-list li#accessibletabsnavigation0-0 a').click();
                    } // Volta para a primeira aba se ocorrer um erro
            }
          });
        });
});