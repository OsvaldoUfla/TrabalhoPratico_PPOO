package br.ufla.gac106.s2022_2.PersonaOpina.controladores;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import br.ufla.gac106.s2022_2.PersonaOpina.conexoes.ConexaoSQLite;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.AvaliacaoPersonagem;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Personagem;

public class ControladorPersonagem {

    public void inserir(Personagem personagem) {
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        String query = "INSERT INTO tbl_personagem"
                        + "(nome, descricao, idade, localOrigem, tipoItem, nomeObra, habilidade) "
                        + "VALUES (?, ?, ?, ?, ?, ?, ?);";

        try (PreparedStatement preparedStatement = conexaoSQLite.criarPreparedStatement(query);) {
            preparedStatement.setString(1, personagem.getNome());
            preparedStatement.setString(2, personagem.getDescricao());
            preparedStatement.setInt(3, personagem.getIdade());
            preparedStatement.setString(4, personagem.getLocalOrigem());
            preparedStatement.setInt(5, personagem.getTipoItem());
            preparedStatement.setString(6, personagem.getNomeObra());
            preparedStatement.setString(7, personagem.getHabilidade());

            int resultado = preparedStatement.executeUpdate();

            if (resultado == 1)
                System.out.println("Personagem inserido com sucesso!");
            else
                System.out.println("Erro ao inserir a personagem.");
        } catch (SQLException e) {
            // O ErrorCode 19 ocorre quando o login informado ja existe no BD
            if (e.getErrorCode() == 19)
                throw new IllegalArgumentException("Já existe um personagem com o nome informado. Por favor, escolha outro nome.");
            else
                System.out.println("Erro ao inserir a personagem");
        }
    }

    public void editar() {

    }

    public void remover(int id) {
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        String query = "DELETE FROM tbl_personagem WHERE id = ?;";

        try (PreparedStatement preparedStatement = conexaoSQLite.criarPreparedStatement(query);) {

            preparedStatement.setInt(1, id);
            int linhasDeletadas = preparedStatement.executeUpdate();

            if(linhasDeletadas > 0)
                System.out.println("Personagem removido com sucesso!");
            else 
                System.out.println("Nenhum personagem foi removido.");

        } catch (SQLException e){
            System.err.println(e.getMessage());
        } 
    }

    public List <Personagem> buscar() throws SQLException{ // lista por ordem de id
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        String query = "SELECT * FROM tbl_personagem;";

        List <Personagem> personagens = new ArrayList<>();

        try (Statement statement = conexaoSQLite.criarStatement(); ResultSet resultSet = statement.executeQuery(query);) {

            while(resultSet.next()){
                Personagem personagem = new Personagem(
                resultSet.getString("nome"),
                resultSet.getString("descricao"), 
                resultSet.getInt("idade"), 
                resultSet.getString("localOrigem"), 
                resultSet.getInt("tipoItem"), 
                resultSet.getString("nomeObra"), 
                resultSet.getString("habilidade"));
                personagem.setId(resultSet.getInt("ID")
                );

                personagens.add(personagem);
            }
            return personagens;
        } catch(SQLException e){
            System.err.println(e.getMessage());
            throw e;
        }
    }

    // listar pelo nome do personagem
    public List <Personagem> listarPorNome() throws SQLException{
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        String query = "SELECT * FROM tbl_personagem ORDER BY nome;"; // seleciona todos os personagens ordenados pelo nome

        List <Personagem> personagens = new ArrayList<>(); // cria uma lista de personagens

        // cria um statement e executa a query
        try (Statement statement = conexaoSQLite.criarStatement(); ResultSet resultSet = statement.executeQuery(query);) {

            while(resultSet.next()){ // enquanto houver personagens na lista
                Personagem personagem = new Personagem( // cria um novo personagem
                resultSet.getString("nome"),
                resultSet.getString("descricao"), 
                resultSet.getInt("idade"), 
                resultSet.getString("localOrigem"), 
                resultSet.getInt("tipoItem"), 
                resultSet.getString("nomeObra"), 
                resultSet.getString("habilidade"));
                personagem.setId(resultSet.getInt("ID")
                );

                personagens.add(personagem);
            }
            return personagens;
        } catch(SQLException e){
            throw e;
        }
    }

    public List <AvaliacaoPersonagem> listarPorMedia() throws SQLException {
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        String query = "SELECT p.*, (SELECT AVG(a.avaliacao) FROM tbl_avaliacao a WHERE a.id_personagem = p.id) as media_avaliacoes FROM tbl_personagem p ORDER BY media_avaliacoes DESC;";

        List <AvaliacaoPersonagem> avaliacoes = new ArrayList<>();

        
        try (Statement statement = conexaoSQLite.criarStatement(); ResultSet resultSet = statement.executeQuery(query);) {
            
            while(resultSet.next()){
                
                AvaliacaoPersonagem ap = new AvaliacaoPersonagem();
                Personagem personagem = new Personagem(
                    resultSet.getString("nome"),
                    resultSet.getString("descricao"), 
                    resultSet.getInt("idade"), 
                    resultSet.getString("localOrigem"), 
                    resultSet.getInt("tipoItem"), 
                    resultSet.getString("nomeObra"), 
                    resultSet.getString("habilidade")
                );
                    
                personagem.setId(resultSet.getInt("ID"));
                ap.setPersonagem(personagem);
                ap.setMedia(resultSet.getInt("media_avaliacoes"));

                //personagens.add(personagem);
                avaliacoes.add(ap);
            }
            //return personagens;
            return avaliacoes;
        } catch(SQLException e){
            throw e;
        }
    }

    public AvaliacaoPersonagem buscarUm(int id) throws SQLException {
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();
        
        ResultSet resultSet = null;
        
        //String query = "SELECT * FROM tbl_personagem WHERE id = ?;";
        String query = "SELECT p.*, (SELECT AVG(a.avaliacao) FROM tbl_avaliacao a WHERE a.id_personagem = p.id) as media_avaliacao FROM tbl_personagem p WHERE id = ?;";

        Personagem personagem = null;

        AvaliacaoPersonagem ap = new AvaliacaoPersonagem();
        
        try (PreparedStatement preparedStatement = conexaoSQLite.criarPreparedStatement(query);) {
            personagem = new Personagem();

            preparedStatement.setInt(1, id);
            
            // Devido ao valor dinamico, o resultSet eh executado dentro do try{} e eh necessario um finally para fechar ele
            resultSet = preparedStatement.executeQuery();
            
            while(resultSet.next()){
                personagem = new Personagem(
                    resultSet.getString("nome"),
                    resultSet.getString("descricao"), 
                    resultSet.getInt("idade"), 
                    resultSet.getString("localOrigem"), 
                    resultSet.getInt("tipoItem"), 
                    resultSet.getString("nomeObra"), 
                    resultSet.getString("habilidade"));
                    personagem.setId(resultSet.getInt("ID")
                );

                personagem.setId(resultSet.getInt("ID"));
                
                ap.setPersonagem(personagem);
                ap.setMedia(resultSet.getInt("media_avaliacao"));
            }
            return ap;
        } 
        catch(SQLException e){
            throw e;
        } 
        finally{
            try {
                resultSet.close();
            } 
            catch(SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }
}
