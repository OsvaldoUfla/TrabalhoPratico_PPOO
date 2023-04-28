package br.ufla.gac106.s2022_2.PersonaOpina.controladores;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import br.ufla.gac106.s2022_2.PersonaOpina.conexoes.ConexaoSQLite;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Comentario;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Personagem;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Usuario;

public class ControladorComentario {

    /**
     * Faz a insercao de um novo comentario
     * @param comentario 
     * @throws SQLException
     */
    public void inserir(Comentario comentario) throws SQLException {
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Criacao da query de insercao com valores dinamicos
        String query = "INSERT INTO tbl_comentario"
                        + "(comentario, data_publicacao, hora_publicacao, id_usuario, id_personagem) "
                        + "VALUES (?, ?, ?, ?, ?);";
        
        // Pega a data atual do sistema se converte ela para String
        LocalDate dataAtual = LocalDate.now();
        DateTimeFormatter formatadorData = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataAtualString = dataAtual.format(formatadorData);

        // Pega a hora atual do sistema se converte ela para String
        LocalTime horaAtual = LocalTime.now();
        DateTimeFormatter formatadorHora = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horaAtualString = horaAtual.format(formatadorHora);

        // try-with-resources do PreparedStatement
        try (PreparedStatement preparedStatement = conexaoSQLite.criarPreparedStatement(query);) {

            // Preparando o PreparedStatement, settando os valores dinamicos
            preparedStatement.setString(1, comentario.getComentario());
            preparedStatement.setString(2, dataAtualString);
            preparedStatement.setString(3, horaAtualString);
            preparedStatement.setInt(4, comentario.getId_usuario());
            preparedStatement.setInt(5, comentario.getId_personagem());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Retorna uma lista com todos os comentarios do banco
     * @return List <Comentario>
     * @throws SQLException
     */
    public List <Comentario> buscar() throws SQLException{
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Query de busca
        String query = "SELECT c.*, (SELECT p.nome FROM tbl_personagem p WHERE p.id = c.id_personagem) as nome_personagem, (SELECT u.nome FROM tbl_usuario u WHERE u.id = c.id_usuario) as nome_usuario FROM tbl_comentario c;";

        // Criacao da variavel que sera retornada
        List <Comentario> comentarios = new ArrayList<>();

        // try-with-resources do PreparedStatement e do ResultSet
        try (Statement statement = conexaoSQLite.criarStatement(); ResultSet resultSet = statement.executeQuery(query);){
            
            // Para cada comentario no BD, cria uma novo objeto modelo.Comentario, preenche ela e adiciona ela na lista
            while(resultSet.next()){
                // Criacao do novo objeto modelo.Comentario
                Comentario comentario = new Comentario();
                
                // Preenchimento do objeto comentario criado, com os dados do resultSet (BD)
                comentario.setId(resultSet.getInt("id"));
                comentario.setComentario(resultSet.getString("comentario"));
                comentario.setData(resultSet.getString("data_publicacao"));
                comentario.setHora(resultSet.getString("hora_publicacao"));
                comentario.setId_usuario(resultSet.getInt("id_usuario"));
                comentario.setId_personagem(resultSet.getInt("id_personagem"));

                Personagem personagem = new Personagem();
                personagem.setNome(resultSet.getString("nome_personagem"));
                comentario.setPersonagem(personagem);

                Usuario usuario = new Usuario();
                usuario.setNome(resultSet.getString("nome_usuario"));
                comentario.setUsuario(usuario);

                // Adicao do objeto na lista
                comentarios.add(comentario);
            }
            return comentarios;
        } catch(SQLException e){
            throw e;
        } 
    }

    /**
     * Retorna uma lista dos usuarios que mais comentaram
     * @return
     */
    public List <Usuario> usuariosMaisComentaram(){
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Query de busca (ja retorna os tres usuarios que mais comentaram)
        String query = "SELECT u.id, u.nome, u.nivel, u.login, COUNT(*) AS num_comentarios "
                    + " FROM tbl_comentario c"
                    + " JOIN tbl_usuario u ON u.id = c.id_usuario"
                    + " GROUP BY u.id"
                    + " ORDER BY num_comentarios DESC"
                    + " LIMIT 3;";

        // Criacao da variavel que sera retornada
        List <Usuario> usuarios = new ArrayList<>();

        // try-with-resources do PreparedStatement e do ResultSet
        try (Statement statement = conexaoSQLite.criarStatement(); ResultSet resultSet = statement.executeQuery(query);){
            
            // Para cada comentario retornado da consulta, cria um novo objeto e adiciona na lista
            while(resultSet.next()){
                // Criacao do novo objeto modelo.Usuario
                Usuario usuario = new Usuario();
                
                // Preenchimento do objeto usuario criado
                usuario.setId(resultSet.getInt("id"));
                usuario.setNome(resultSet.getString("nome"));
                usuario.setNivel(resultSet.getInt("nivel"));
                usuario.setLogin(resultSet.getString("login"));
                
                // Adicao do objeto na lista
                usuarios.add(usuario);
            }
            return usuarios;
        } catch(SQLException e){
            System.err.println(e.getMessage());
        } 
        return usuarios;
    }

    //quantidade de comentarios do usuario
    public int quantidadeComentariosUsuario(int id_usuario){
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Query de busca
        String query = "SELECT COUNT(*) AS num_comentarios "
                    + " FROM tbl_comentario c"
                    + " JOIN tbl_usuario u ON u.id = c.id_usuario"
                    + " WHERE u.id = ?;";

        // try-with-resources do PreparedStatement e do ResultSet
        try (PreparedStatement preparedStatement = conexaoSQLite.criarPreparedStatement(query);) {
            preparedStatement.setInt(1, id_usuario);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.getInt("num_comentarios");
        } catch(SQLException e){
            System.err.println(e.getMessage());
        } 
        return 0;
    }
}
