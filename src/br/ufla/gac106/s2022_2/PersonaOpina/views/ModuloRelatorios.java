package br.ufla.gac106.s2022_2.PersonaOpina.views;

import java.sql.SQLException;
import java.util.List;

import br.ufla.gac106.s2022_2.PersonaOpina.controladores.ControladorAvaliacao;
import br.ufla.gac106.s2022_2.PersonaOpina.controladores.ControladorComentario;
import br.ufla.gac106.s2022_2.PersonaOpina.SistemaDeAvaliacao;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Personagem;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Usuario;

public class ModuloRelatorios extends Menu{
    private GetInfo get;
    private Usuario usuario = new Usuario();
    private ControladorAvaliacao ca;

    public ModuloRelatorios(Usuario usuario){
        get = new GetInfo();
        this.usuario = usuario;
        ca = new ControladorAvaliacao();
        menu();
    }

    /**
     * Lista os tres usuarios quem mais votaram
     */
    private void listarQuemMaisVotou(){
        List<Usuario> usuarios = ca.usuariosMaisVotaram();

        System.out.println("Os 3 usuarios que mais votaram:");
        for(Usuario usuario: usuarios){
            System.out.print("ID do Usuário: " + usuario.getId() + " | Nome: "  + usuario.getNome());
            System.out.println(" | Quantidade de votos: " + ca.quantidadeAvaliacoesUsuario(usuario.getId()));
        }
    }

    /**
     * Lista os tres usuarios quem mais comentaram
     */
    private void listarQuemMaisComentou(){
        ControladorComentario cc = new ControladorComentario();
        List<Usuario> usuarios = cc.usuariosMaisComentaram();

        System.out.println("Os 3 usuarios que mais comentaram:");
        for(Usuario usuario : usuarios){
            System.out.print("ID do Usuário: " + usuario.getId() + " | Nome: "  + usuario.getNome());
            System.out.println(" | Quantidade de comentários: " + cc.quantidadeComentariosUsuario(usuario.getId()));
        }
    }

    /**
     * Lista os cinco personagens mais bem avaliados de cada genero
     */
    private void listarPersonagensMelhorVotados(){
        List<Personagem> personagens = ca.personagensMelhorVotados(1);
        System.out.println("Os 5 Personagens de filmes mais bem avaliados"
        +"\nPos | Nota/Média | ID do Personagem | Nome");
        int cont = 1;
        for(Personagem personagem : personagens){ // os espaços são para alinhar a tabela que será exibida no terminal
            System.out.print(cont+ "      "+ ca.mediaAvaliacaoPersonagem(personagem.getId()));
            System.out.println("              " + personagem.getId()+ "            "+ personagem.getNome());
            cont++;

        }

        System.out.println("\nOs 5 Personagens de jogos mais bem avaliados"
        +"\nPos | Nota/Média | ID do Personagem | Nome");
        cont = 1;
        personagens = ca.personagensMelhorVotados(2);
        for(Personagem personagem : personagens){ // os espaços são para alinhar a tabela que será exibida no terminal
            System.out.print(cont+ "      "+ ca.mediaAvaliacaoPersonagem(personagem.getId()));
            System.out.println("              " + personagem.getId()+ "            "+ personagem.getNome());
            cont++;

        }
    }

    private void qtdeItensCassificados(){
        System.out.println("Quantidade de personagens avaliados: " + ca.qtdePersonagensAvaliados());
        System.out.println("Quantidade de personagens nao avaliados: " + ca.qtdePersonagensNaoAvaliados());
        this.menu();
    }

    //
    private void graficoDeAvaliacao(Usuario usuario) throws SQLException{
        new SistemaDeAvaliacao(usuario).exibirGrafico();
    }

    
    public void menu() {
        int comando = -1;

        while(comando != 1){
            
            comando = get.getEscolhaInt(1,6,
                "\nDigite:" 
                + "\n1 - Listar a quantidade de itens classificados/nao classificados"
                + "\n2 - Listar os personagems com melhor avaliacao"
                + "\n3 - Listar os usuarios que mais votaram"
                + "\n4 - Listar os usuarios que mais comentaram"
                + "\n5 - Grafico de avalicao de itens"
                + "\n6 - Voltar"
            );
            
            switch (comando) {
                case 6:
                    System.out.println("Voltando ao menu anterior...");
                    InterfaceUsuario iu = new InterfaceUsuario(usuario);
                    iu.menu();
                    break;

                case 1:
                    qtdeItensCassificados();
                    break;

                case 2:
                    listarPersonagensMelhorVotados();
                    break;
                    
                case 3:
                    listarQuemMaisVotou();
                    break;
                    
                case 4:
                    listarQuemMaisComentou();
                    break;
                    
                case 5:
                    System.out.println("MOSTRAR GRAFICO");
                    try {
                        graficoDeAvaliacao(usuario);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    }
}