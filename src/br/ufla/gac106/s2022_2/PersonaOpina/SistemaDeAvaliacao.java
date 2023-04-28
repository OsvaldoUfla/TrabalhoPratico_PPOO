package br.ufla.gac106.s2022_2.PersonaOpina;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.ufla.gac106.s2022_2.PersonaOpina.controladores.ControladorAvaliacao;
import br.ufla.gac106.s2022_2.PersonaOpina.controladores.ControladorComentario;
import br.ufla.gac106.s2022_2.PersonaOpina.controladores.ControladorPersonagem;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.AvaliacaoPersonagem;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.AvaliacoesFilmeJogo;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Comentario;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Personagem;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Usuario;
import br.ufla.gac106.s2022_2.PersonaOpina.relatorios.Grafico;


public class SistemaDeAvaliacao {
    // Objeto do tipo ControladorAvaliacao
    private ControladorAvaliacao ca;// Variavel do tipo ControladorAvaliacao
    private ControladorPersonagem cp;// Variavel do tipo controladorPersonagem
    private ControladorComentario cc;// Variavel do tipo ControladorComentario
    private Usuario usuario;

    public SistemaDeAvaliacao(Usuario usuario){
        // Objeto do tipo ControladorAvaliacao
        this.usuario = usuario;
        ca = new ControladorAvaliacao();// Variavel do tipo ControladorAvaliacao
        cp = new ControladorPersonagem();// Objeto do tipo controladorPersonagem
        cc = new ControladorComentario();// Objeto do tipo ControladorComentario
    }

    /**
     * Retorna uma lista todos os personagens cadastrados no banco de dados
     * @return
     * @throws SQLException
     */ 
    public List<Personagem> listarPersonagens() throws SQLException{ //lista por id
        /* Chamada do metodo buscar do controladorPersonagem, 
        que retorna uma List<> com os dados dos personagens */
        List<Personagem> personagens = cp.buscar();
        return personagens;
    }

    /*
     * Retorna uma lista todos os personagens cadastrados no banco de dados
     * // lista personagens por nome
     */
    public List<Personagem> listarPersonagensNome() throws SQLException{ //lista por nome
        /* Chamada do metodo buscar do controladorPersonagem, 
        que retorna uma List<> com os dados dos personagens */
        List<Personagem> personagens = cp.listarPorNome();
        return personagens;
    }

    /*
     * Retorna uma lista todos os personagens cadastrados no banco de dados
     * // lista personagens por media
     */
    public List<AvaliacaoPersonagem> listarPersonagensMedia() throws SQLException {
        /* Chamada do metodo buscar do controladorPersonagem, 
        que retorna uma List<> com os dados dos personagens */
        List<AvaliacaoPersonagem> personagens = cp.listarPorMedia();
        return personagens;
    }

    /**
     * Retorna uma lista os dados de um personagem especifico, de acordo com o ID informado
     * @param id ID do personagem que deseja buscar
     * @throws SQLException
     */
    public AvaliacaoPersonagem listarPersonagemDetalhado(int id) throws SQLException {        
        /* Chamada do metodo buscarUm do controladorPersonagem, 
        que retorna o personagem de ID informado */
        AvaliacaoPersonagem ap = cp.buscarUm(id);

        /* Como o SQLite nao gera uma exception para dados nao encontrados,
         verifica-se se a variavel personagem recebeu null. 
        Se ela for null o banco nao retornou o registro e lanca-se a exception
         para tratar na view */
        if(ap.getPersonagem().getId() == 0) {
            throw new IllegalArgumentException("O ID do personagem nao foi encontrado no Banco de Dados.");
        }
        
        return ap;
    }

    /**
     * Cria um novo comentario e o insere no banco de dados
     * @throws SQLException
     */
    public void novoComentario(int idPersonagem, String texto) throws SQLException{
        // Criacao do objeto comentario
        Comentario comentario = new Comentario(usuario.getId(), idPersonagem, texto);
        AvaliacaoPersonagem personagem = cp.buscarUm(idPersonagem);
        if(personagem.getPersonagem().getId() != 0) {
            // A funcao inserir soh espera o ID do usuario, o ID do personagem e o comentario
            // `data atual` e `hora atual` sao inseridos automaticamente por default do BD
            cc.inserir(comentario);
        }
    }

    /**
     * Retorna uma lista todos os comentarios do BD
     * @throws SQLException
     */
    public List<Comentario> listarComentarios() throws SQLException{
        List<Comentario> comentarios = cc.buscar();
        return comentarios;
    }

    /**
     * Insere uma nova avaliacao de personagem no BD
     * @throws SQLException
     */
    public void avaliar(int idPersonagem, int nota) throws SQLException{
        //cria um objeto do tipo AvaliacaoPersonagem
        AvaliacaoPersonagem avaliacaoPersonagem = new AvaliacaoPersonagem(usuario.getId(), idPersonagem, nota);

        try {
            // Busca pelo personagem no banco e salva o resultado da busca em `personagem`
            AvaliacaoPersonagem personagem = cp.buscarUm(idPersonagem);

            // Se o personagem for encontrado, seguir com a insercao da avalicao
            if(personagem.getPersonagem().getId() != 0){
                
                // Busca por uma avaliacao com o mesmo `id_usuario` e `id_persogagem` informados
                AvaliacaoPersonagem avl = ca.buscarUm(avaliacaoPersonagem);

                // Se a avaliacao em questao NAO for encontrada, fazer a insercao de uma nova avaliacao
                // Caso contrario, atualizar a insercao antiga
                if(avl == null){
                    ca.inserir(avaliacaoPersonagem);
                } else {
                    avaliacaoPersonagem.setId(avl.getId());
                    ca.atualizar(avaliacaoPersonagem);
                }
            } else {
                throw new IllegalArgumentException();
            }
        } catch(SQLException e) {
            throw e;
        }
    }

    // Retorna uma lista de todas as avaliacoes do BD
    public List<AvaliacaoPersonagem> listarAvaliacoes() throws SQLException{
        List<AvaliacaoPersonagem> avaliacoes = ca.buscar();
        return avaliacoes;
    }

    //Exibe o gráfico de avaliação de um personagem
    public void exibirGrafico() throws SQLException{
        List<AvaliacaoPersonagem> avaliacao1 = listarPersonagensMedia();
        Collection<Avaliacao> avaliacao = new ArrayList<>();
        for(AvaliacaoPersonagem ap : avaliacao1){
            avaliacao.add(ap);
        }
        Avaliacoes avaliacaoFilmeJogo = new AvaliacoesFilmeJogo("Nome dos Personagens",avaliacao);
        new Grafico().exibir("Personagens Filmes/Jogos", avaliacaoFilmeJogo);
    }

}