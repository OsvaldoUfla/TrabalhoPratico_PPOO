package br.ufla.gac106.s2022_2.PersonaOpina.views;

import br.ufla.gac106.s2022_2.PersonaOpina.conexoes.ConexaoSQLite;
import br.ufla.gac106.javaWikiAPI.Wiki;

/**
 * Classe que gerencia a tela inicial
 * Seleciona a tela de login ou cadastro
 * Caso o usuario queira fazer cadastro, o cadastro é feito com o nivel 1
 */
import br.ufla.gac106.s2022_2.PersonaOpina.GerenciadorDeUsuarios;


public class Index {
    private Wiki wiki;
    private GetInfo get;

    // Construtor
    public Index() {
        wiki = new Wiki();
        get = new GetInfo();
    }
    
    // Seleciona a tela de login ou cadastro
    public void index(){
        int opcao = get.getEscolhaInt(
            1, 
            3, 
            "Digite:" 
            + "\n1 - Fazer login" 
            + "\n2 - Cadastrar-se"
            + "\n3 - Fechar"
        );

        if(opcao == 1){
            String login = get.getEmStr("Login:");
            String senha =  get.getEmStr("Senha:");
            login(login, senha);
        }
        else if(opcao == 2){
            criarUsuarioNivel1();
            index();
        }
        else if(opcao == 3){
            System.out.println("Obrigado por usar o Persona Opina!");
            System.out.println("Saindo...");

            // Chama sigleton da conhexao
            ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();
            // Desconectando do BD
            conexaoSQLite.desconectar();

            try{
                wiki.close();
            } catch(Exception e){
                System.out.println("Erro ao fechar a Wiki");
            }
            System.exit(0);
        }
    }

    /**
    * Tela de login
    */
    private void login(String login, String senha){
        try{
            new InterfaceUsuario(new GerenciadorDeUsuarios().usuarioExiste(login, senha)).menu();
        }
        catch(IllegalArgumentException e){
            // Captura a exceção lançada quando o usuario não existe
            System.out.println(e.getMessage());
            // Volta para a tela inicial
            index();
        }
        // Captura qualquer outra exceção
        catch(Exception e){
            index();
        }
    }

    /**
     * Cria um usuario de Nivel 1
     * Essa opção é disponibilizada para o usuario de nivel 1
     * Que deseja fazer somente avaliações
     */
    private void criarUsuarioNivel1(){

        String nome = get.getEmStr("Informe seu nome:");
        String login = get.getEmStr("Informe o login do usuario:");
        String senha = get.getEmStr("Informe sua senha:");
        String confirmaSenha = get.getEmStr("Confirme sua senha:");
        
        // Verifica se as senhas são iguais
        if(senha.equals(confirmaSenha)){
            try{
                new GerenciadorDeUsuarios().criarUsuario(nome, login, senha);
            }
            catch (IllegalArgumentException e){
                // Exibe a mensagem de erro lançada pelo gerenciador de usuarios
                System.out.println(e.getMessage());
            }
        } 
        else {
            System.out.println("As senhas diferem. Tente novamente.");
            index();
        }
    }
}
