package br.ufla.gac106.s2022_2.PersonaOpina.views;

import java.util.List;

import br.ufla.gac106.javaWikiAPI.JavaWikiInternalException;
import br.ufla.gac106.javaWikiAPI.PaginaWiki;
import br.ufla.gac106.javaWikiAPI.UnsuccessfulHTTPRequestException;
import br.ufla.gac106.javaWikiAPI.Wiki;
import br.ufla.gac106.s2022_2.PersonaOpina.Administracao;
import br.ufla.gac106.s2022_2.PersonaOpina.GerenciadorDeUsuarios;
import br.ufla.gac106.s2022_2.PersonaOpina.NovaBuscaException;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Usuario;

public class ModuloAdministrativo extends Menu{
    private GetInfo get;
    private Usuario usuario;
    private Wiki wiki;
    private Administracao Ad;

    public ModuloAdministrativo(Usuario usuario){
        get = new GetInfo();
        this.usuario = usuario;
        wiki = new Wiki();
        Ad = new Administracao(usuario);
        menu();
    }

    // Pega páginas de título semelhante
    private String getPaginasSemelhantes(String nome){
        String paginas = "";
        try{
            List <String> listaPaginas = wiki.pesquisarTitulosDePaginas(nome);
            if(listaPaginas.size() == 0){
                paginas = "Sentimos muito. Nao foi possivel encontrar uma descricao para " + nome + ".";
            }
            else{
                for(String pagina : listaPaginas){
                    paginas += pagina + "\n";
                }

                /* System.out.println("Selecione: ");
                for(int i = 0; i < listaPaginas.size(); i++){
                    System.out.println((i + 1) + " - " + listaPaginas.get(i));
                }
                int index = GetInfo.getEscolhaInt(1, listaPaginas.size() - 1, "");
                String resumoEscolhido = listaPaginas.get(index);
                return resumoEscolhido; */
            }
        }
        catch(Exception e){
            System.out.println("Sentimos muito. Nao foi possivel encontrar uma descricao para " + nome + ". Por favor, tente novamente.");
            this.menu();
        }
        return paginas;
    }
    
    /**
     * Pega o resumo de acordo com o nome informado
     * @param nome Nome do personagem
     * @return Resumo do personagem de acordo com a pagina Wiki
     */
    private String buscarResumo(String nome){
        String resumo = "";

        try {
            PaginaWiki pagina = wiki.consultarPagina(nome);
            resumo = pagina.getResumo();
        }
        //se nao encontrar a pagina, retorna uma mensagem de erro e mostra as paginas semelhantes
        catch(JavaWikiInternalException e){ //
            resumo = "Nao foi possivel encontrar a pagina. Tente novamente.\n"+ "\nPaginas semelhantes que podem ser usadas como titulo:\n";
            resumo += getPaginasSemelhantes(nome);
        } catch(UnsuccessfulHTTPRequestException un){
            System.out.println("Ocorreu um erro ao buscar por " + nome + ". Por favor, tente novamente.");
            this.menu();
        } catch(Exception e){
            resumo = "Nao foi possivel encontrar a pagina. Tente novamente.\n"+ "\nPaginas semelhantes que podem ser usadas como titulo:\n";
            resumo += getPaginasSemelhantes(nome);
        }

        return resumo;
    }
    
    /**
     * Menu para selecionar o resumo do personagem
     */
    private String menuResumo(String nome){
        String resumo = "";
        resumo = buscarResumo(nome);

        System.out.println("Resumo do personagem: " + resumo);
        int comando = -1;
        
        while(comando != 1){    
            comando = get.getEscolhaInt(1,3,
                "\nDigite:" 
                + "\n1 - Resumo está correto!"
                + "\n2 - Realizar nova busca"
            );
            
            switch (comando) {
                case 1:
                    // Resumo correto, continua o programa
                    System.out.println("Resumo Selecionado!");
                    break;
    
                case 2:
                // Caso o nome do personagem esteja errado, o usuario pode realizar uma nova busca
                // necessario garantir que a exceção não vá parar
                    throw new NovaBuscaException("Realizando nova busca...");
            }
        }
        return resumo;
    }

    /**
     * Insere um novo personagem no BD
     */
    public void cadastrarPersonagem(){
        String nome = get.getEmStr("Nome do personagem:");
        String resumo = menuResumo(nome);
        int idade = get.getEmInteito("Idade do personagem:");
        String localOrigem = get.getEmStr("Local de origem do personagem:");
        int filmeJogo = get.getEscolhaInt(1, 2, "Digite:" + "\n1 - Personagem de um filme" + "\n2 - Personagem de um jogo");
        String obra = get.getEmStr("Nome da obra a qual o personagem pertence");
        String habilidade = get.getEmStr("Habilidades do personagem:");
        
        try{
            Ad.novoPersonagem(0, nome, resumo, idade, localOrigem, filmeJogo, obra, habilidade);
        } catch(IllegalArgumentException e){
            System.out.println("O nome do personagem informado ja existe no banco");
            menu();
        }
    }

    /**
     * Chama o metodo de excluir personagens do ControladorPersonagem
     */
    public void excluirPersonagem(){
        // ID do personagem que sera excluido
        int id = get.getEmInteito("Digite o ID do personagem:");
        Ad.excluirPersonagem(id, usuario);
    }

    /**
     * Cadastrar usuario
     */
    private void cadastrarUsuario(){
        String nome = get.getEmStr("Informe o nome do usuario:");
        String login = get.getEmStr("Informe o login do usuario:");
        String senha = get.getEmStr("Informe a senha do usuario:");
        String confirmaSenha = get.getEmStr("Confirme a senha:");
        int nivel = get.getEscolhaInt(1, 3, "Informe o tipo de usuario (1: Avaliador, 2: Modelador ou 3: Administrador): ");
        
        // Verifica se as senhas são iguais
        if(senha.equals(confirmaSenha)){
            // Cadastra o usuario
            try{
                GerenciadorDeUsuarios gu = new GerenciadorDeUsuarios();

                gu.criarUsuario(usuario, nome, login, senha, nivel);

                System.out.println("Usuario cadastrado com sucesso!");
            }
            catch(IllegalArgumentException e){
                System.out.println(e.getMessage());
                menu();
            }
        } 
        else {
            System.out.println("As senhas diferem. Tente novamente.");
            cadastrarUsuario();
        }
    }
    
    @Override
    public void menu() {
        int comando = -1;

        while(comando != 4){
            
            comando = get.getEscolhaInt(1,4,
                "Digite:" 
                + "\n1 - Cadastrar personagem"
                + "\n2 - Excluir personagem"
                + "\n3 - Cadastrar Usuário"
                + "\n4 - Voltar"
            );
            switch (comando) {
                case 4:
                    System.out.println("Voltando ao menu anterior...");
                    InterfaceUsuario iu = new InterfaceUsuario(usuario);
                    iu.menu();
                    break;
                    
                case 1:
                    boolean realizarNovaBusca = true;
                    while (realizarNovaBusca){
                        try{
                            cadastrarPersonagem();
                            realizarNovaBusca = false;
                        }
                        /*
                        * Durante o cadastro de um personagem, pode ocorrer de o usuario
                        * digitar um nome que nao existe na wiki. Nesse caso, o programa
                        * deve voltar ao menu de cadastro de personagem.
                        */
                        catch(NovaBuscaException e){
                            // Volta ao menu de cadastro de personagem
                            System.out.println(e.getMessage());
                        }
                    }
                    break;

                case 2:
                    try{
                        excluirPersonagem();
                    }
                    catch(IllegalArgumentException e){
                        System.out.println(e.getMessage());
                        menu();
                    }
                    break;

                case 3:
                    cadastrarUsuario();
                    break;
            }
        }
    }
}

