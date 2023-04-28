package br.ufla.gac106.s2022_2.PersonaOpina.views;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import br.ufla.gac106.s2022_2.PersonaOpina.SistemaDeAvaliacao;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.AvaliacaoPersonagem;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Comentario;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Personagem;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Usuario;

public class ModuloAvaliativo extends Menu{
    private GetInfo get;
    private Usuario usuario;
    private SistemaDeAvaliacao sa ;

    public ModuloAvaliativo(Usuario usuario){
        get = new GetInfo();
        this.usuario = usuario;
        sa = new SistemaDeAvaliacao(usuario);
        menu();
    }
    
    /**
     * Faz a listagem dos campos ID, nome e tipoItem de todos os personagens
     */
    private void listarPersonagens(){
        List <Personagem> personagens;
        int opcao = 0;
        try{
            opcao = get.getEmInteito("Deseja listar por:\n1 - ID\n2 - Nome\n3 - Media");
            if (opcao == 1){
                personagens = sa.listarPersonagens();
                System.out.println("Personagens cadastrados:");
                for (Personagem personagem : personagens) {
                    System.out.println("\nID do Personagem: " + personagem.getId()
                                    + "\nNome: " + personagem.getNome ()
                                    + "\nPersonagem de: " + (personagem.getTipoItem() == 1 ? "Filme" : "Jogo"));
                }
            }
            else if (opcao == 2){
                personagens = sa.listarPersonagensNome();
                System.out.println("Personagens cadastrados:");
                for (Personagem personagem : personagens) {
                    System.out.println("\nNome: " + personagem.getNome ()
                                    +"\nID do Personagem: " + personagem.getId()
                                    + "\nPersonagem de: " + (personagem.getTipoItem() == 1 ? "Filme" : "Jogo"));
                }
            }
            else if(opcao == 3){
                List<AvaliacaoPersonagem> avaliacoes = sa.listarPersonagensMedia();
                System.out.println("Personagens cadastrados:");
                for (AvaliacaoPersonagem avaliacao : avaliacoes) {
                    System.out.println("\nNome: " + avaliacao.getPersonagem().getNome()
                                    +"\nID do Personagem: " + avaliacao.getPersonagem().getId()
                                    + "\nPersonagem de: " + (avaliacao.getPersonagem().ehPersonagemFilme() ? "Filme" : "Jogo")
                                    + "\nAvaliacao media: " + avaliacao.getMedia());
                }
            }
            else{
                System.out.println("Opção inválida");
            }

        } catch (SQLException e) {
            //System.out.println("Houve um erro ao buscar os dados. Por favor, tente novamente mais tarde.");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Lista os dados de um personagem especifico, de acordo com o ID informado
     */
    private void listarPersonagemDetalhado(){
        // Coleta do ID do personagem que sera buscado
        int id = get.getEmInteito("Informe o ID do personagem que deseja buscar:");

        //Personagem personagem;
        AvaliacaoPersonagem personagem;

        try{
            personagem = sa.listarPersonagemDetalhado(id);
    
            System.out.println("Detalhes do personagem:");

            System.out.println("ID: " + personagem.getPersonagem().getId()
                            + "\nNome: " + personagem.getPersonagem().getNome ()
                            + "\nDescricao: " + personagem.getPersonagem().getDescricao()
                            + "\n" + (personagem.getPersonagem().getTipoItem() == 1 ? "Filme" : "Jogo") + ": " + personagem.getPersonagem().getNomeObra()
                            + "\nHabilidade: " + personagem.getPersonagem().getHabilidade()
                            + "\nIdade: " + personagem.getPersonagem().getIdade()
                            + "\nLocal de origem: " + personagem.getPersonagem().getLocalOrigem()
                            + "\nMedia de avaliacao: " + personagem.getMedia());
        } catch(IllegalArgumentException e) {
            System.out.println("O valor informado para o ID eh invalido. Tente outro valor.");
        } catch(SQLException e) {
            System.out.println("Houve um erro ao buscar os dados. Por favor, tente novamente mais tarde." + e.getMessage());
        }
    }

    /**
     * Coleta os dados para inserir um novo comentario no BD
     */
    private void comentar(){
        int idPersonagem = get.getEmInteito("ID do personagem");

        String comentario = get.getEmStr("Comentario:");

        try{
            sa.novoComentario(idPersonagem,  comentario);
            System.out.println("Comentario inserido com sucesso!");
        } catch(SQLException e) {
            System.out.println("Erro ao inserir a comentario. Verifique se o ID do personagem informado esta correto e tente novamente.");
        } catch(NullPointerException e) {
            System.out.println("Erro ao inserir comentario. Por favor, verifique o ID informado e tente novamente.");  
        }
    }

    /**
     * Exibe uma lista com todos os comentarios do BD
     */
    private void listarComentarios(){
        try{
            List<Comentario> comentarios = sa.listarComentarios();
         
            if(comentarios.size() > 0){
                System.out.println("Lista de comentarios:");
                
                for(Comentario comentario : comentarios){
                    System.out.println("Comentario feito por: " + comentario.getUsuario().getNome()
                                    + "\nID do Comentario: "+ comentario.getId() 
                                    + "\nNome do personagem: " + comentario.getPersonagem().getNome()
                                    + "\nComentario: " + comentario.getComentario()
                                    + "\nData/Hora: " + comentario.getData() + " - " + comentario.getHora() + "\n");
                }
            } else {
                System.out.println("Nao ha comentarios para fazer a listagem.");
            }
        } catch(SQLException e){
            System.out.println("Houve um erro ao buscar os dados. Por favor, tente novamente mais tarde.");
        }
    }

    /**
     * Coleta os dados para inserir uma nova avaliacao no BD
     */
    private void avaliar(){
        //pega o ID do personagem
        int idPersonagem = get.getEmInteito("ID do personagem");

        // a avaliacao deve ser entre 1 e 5
        int avaliacao = get.getEscolhaInt(1, 5, "Avaliacao (1-5):");

        try{
            sa.avaliar(idPersonagem, avaliacao);
            System.out.println("Avaliacao inserida com sucesso!");
        } catch (IllegalArgumentException e){
            System.out.println("Erro ao inserir avaliacao. Por favor, verifique o ID informado e tente novamente.");
        } catch(SQLException e) {
            System.out.println("Houve um erro ao inserir a avaliacao. Por favor, tente novamente mais tarde.");
        } catch(Exception e){
            System.out.println("Erro ao inserir avaliacao. Por favor, verifique o ID informado e tente novamente.");
        }
        
   }

    /**
     * Exibe uma lista com todas as avaliacoes do BD
     */
    private void listarAvaliacoes(){
        try{
            Collection<AvaliacaoPersonagem> avaliacoes = sa.listarAvaliacoes();
    
            if(avaliacoes.size() > 0){
                System.out.println("Lista de avaliacoes:");
    
                for(AvaliacaoPersonagem avaliacao : avaliacoes){
                    System.out.println("\nID do Personagem: " + avaliacao.getId_personagem()
                                    + "\nNome do Personagem: " + avaliacao.getPersonagem().getNome()
                                    + "\nNota de avaliacao: " + avaliacao.getAvaliacao()
                                    + "\nData/Hora: " + avaliacao.getData() + " - " + avaliacao.getHora() + "\n");
                }
            } else {
                System.out.println("Ainda nao ha avaliacoes para fazer a listagem.");
            }
        } catch(SQLException e) {
            System.out.println("Houve um erro ao buscar os dados. Por favor, tente novamente mais tarde.");
        }
    }

    public void menu() {
        int comando = -1;

        while(comando != 7){
            
            comando = get.getEscolhaInt(1,7,
                "Digite:" 
                + "\n1 - Listar todos os personagens" 
                + "\n2 - Listar dados de um personagem"
                + "\n3 - Fazer um comentario"
                + "\n4 - Listar comentarios"
                + "\n5 - Fazer uma avaliacao"
                + "\n6 - Listar avaliacoes"
                + "\n7 - Voltar"
            );
            
            switch (comando) {
                case 7:
                    System.out.println("Voltando ao menu anterior...");
                    InterfaceUsuario iu = new InterfaceUsuario(usuario);
                    iu.menu();
                    break;
                    
                case 1:
                    listarPersonagens();
                    break;
                    
                case 2:
                    listarPersonagemDetalhado();
                    break;
                    
                case 3:
                    comentar();
                    break;
                    
                case 4:
                    listarComentarios();
                    break;

                case 5:
                    avaliar();
                    break;

                case 6:
                    listarAvaliacoes();
                    break;
            }
        }
    }
}
