package br.ufla.gac106.s2022_2.PersonaOpina.controladores;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import br.ufla.gac106.s2022_2.PersonaOpina.conexoes.ConexaoSQLite;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.AvaliacaoPersonagem;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Personagem;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Usuario;

public class ControladorAvaliacao {

    /**
     * Faz a insercao de uma nova avaliacao no BD
     * @param avaliacao 
     * @throws SQLException
     */
    public void inserir(AvaliacaoPersonagem avaliacao) throws SQLException {
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Criacao da query de insercao com valores dinamicos
        String query = "INSERT INTO tbl_avaliacao"
                        + "(avaliacao, data_publicacao, hora_publicacao, id_usuario, id_personagem) "
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
            preparedStatement.setInt(1, avaliacao.getAvaliacao());
            preparedStatement.setString(2, dataAtualString);
            preparedStatement.setString(3, horaAtualString);
            preparedStatement.setInt(4, avaliacao.getId_usuario());
            preparedStatement.setInt(5, avaliacao.getId_personagem());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Retorna uma lista com todos os avaliacoes do banco
     * @return List <Avaliacao>
     * @throws SQLException
     */
    public List <AvaliacaoPersonagem> buscar() throws SQLException{
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Query de busca
        String query = "SELECT a.*, (SELECT p.nome FROM tbl_personagem p WHERE p.id = a.id_personagem) as nomePersonagem FROM tbl_avaliacao a;";

        // Criacao da variavel que sera retornada
        List <AvaliacaoPersonagem> avaliacaoList = new ArrayList<>();

        // try-with-resources do PreparedStatement e do ResultSet
        try (Statement statement = conexaoSQLite.criarStatement(); ResultSet resultSet = statement.executeQuery(query);){
            
            // Para cada avaliacao no BD, cria uma novo objeto modelo.Avaliacao, preenche ela e adiciona ela na lista
            while(resultSet.next()){
                // Criacao do novo objeto modelo.Avaliacao
                AvaliacaoPersonagem avaliacao = new AvaliacaoPersonagem();
                
                // Preenchimento do objeto avaliacao criado, com os dados do resultSet (BD)
                avaliacao.setId(resultSet.getInt("id"));
                avaliacao.setAvaliacao(resultSet.getInt("avaliacao"));
                avaliacao.setData(resultSet.getString("data_publicacao"));
                avaliacao.setHora(resultSet.getString("hora_publicacao"));
                avaliacao.setId_usuario(resultSet.getInt("id_usuario"));
                avaliacao.setId_personagem(resultSet.getInt("id_personagem"));
                
                Personagem personagem = new Personagem();
                personagem.setNome(resultSet.getString("nomePersonagem"));
                avaliacao.setPersonagem(personagem);
                
                // Adicao do objeto na lista
                avaliacaoList.add(avaliacao);
            }
            return avaliacaoList;
        } catch(SQLException e){
            throw e;
        }
    }
    
    // atualiza apenas a nota da avaliacao, sem criar uma nova
    public void atualizar(AvaliacaoPersonagem avaliacao) throws SQLException {
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Criacao da query de atualizacao com valores dinamicos
        String query = "UPDATE tbl_avaliacao SET avaliacao = ?, data_publicacao = ?, hora_publicacao = ? WHERE id = ?";

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
            preparedStatement.setInt(1, avaliacao.getAvaliacao());
            preparedStatement.setString(2, dataAtualString);
            preparedStatement.setString(3, horaAtualString);
            preparedStatement.setInt(4, avaliacao.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Retorna uma avaliacao especifica
     * @return modelo.Avaliacao
     */
    public AvaliacaoPersonagem buscarUm(AvaliacaoPersonagem buscarAvaliacao){
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();
        
        ResultSet resultSet = null;
        
        String query = "SELECT * FROM tbl_avaliacao WHERE id_usuario = ? AND id_personagem = ?;";
        
        AvaliacaoPersonagem avaliacao = null;
        
        try (PreparedStatement preparedStatement = conexaoSQLite.criarPreparedStatement(query);) {
            
            preparedStatement.setInt(1, buscarAvaliacao.getId_usuario());
            preparedStatement.setInt(2, buscarAvaliacao.getId_personagem());
            
            resultSet = preparedStatement.executeQuery();
 
            while(resultSet.next()){
                avaliacao = new AvaliacaoPersonagem();
                avaliacao.setId(resultSet.getInt("id"));
                avaliacao.setId_usuario(resultSet.getInt("id_usuario"));
                avaliacao.setId_personagem(resultSet.getInt("id_personagem"));
                avaliacao.setAvaliacao(resultSet.getInt("avaliacao"));
                avaliacao.setData(resultSet.getString("data_publicacao"));
                avaliacao.setHora(resultSet.getString("hora_publicacao"));
            }

            return avaliacao;
        } catch(SQLException e){
            System.err.println(e.getMessage());
        } finally{
            try {
                resultSet.close();
            } catch(SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return avaliacao;
    }

    /**
     * Retorna uma lista dos usuarios que mais votaram
     * @return
     */
    public List <Usuario> usuariosMaisVotaram(){
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Query de busca (ja retorna os tres usuarios que mais votaram)
        String query = "SELECT u.id, u.nome, u.nivel, u.login, COUNT(*) AS num_ocorrencias "
                    + " FROM tbl_avaliacao a"
                    + " JOIN tbl_usuario u ON u.id = a.id_usuario"
                    + " GROUP BY u.id"
                    + " ORDER BY num_ocorrencias DESC"
                    + " LIMIT 3;";

        // Criacao da variavel que sera retornada
        List <Usuario> usuarios = new ArrayList<>();

        // try-with-resources do PreparedStatement e do ResultSet
        try (Statement statement = conexaoSQLite.criarStatement(); ResultSet resultSet = statement.executeQuery(query);){
            
            // Para cada avaliacao cira um novo objeto de usuario, preenche ele e adiciona ela na lista
            while(resultSet.next()){
                // Criacao do novo objeto modelo.Avaliacao
                Usuario avaliacao = new Usuario();
                
                // Preenchimento do objeto Usuario criado, com os dados do resultSet (BD)
                avaliacao.setId(resultSet.getInt("id"));
                avaliacao.setNome(resultSet.getString("nome"));
                avaliacao.setNivel(resultSet.getInt("nivel"));
                avaliacao.setLogin(resultSet.getString("login"));
                
                // Adicao do objeto na lista
                usuarios.add(avaliacao);
            }
            return usuarios;
        } catch(SQLException e){
            System.err.println(e.getMessage());
        } 
        return usuarios;
    }

    public List <Personagem> personagensMelhorVotados(int tpItem){
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Query de busca (ja retorna os tres usuarios que mais votaram)
        String query = "SELECT p.id, p.nome, AVG(a.avaliacao) AS media_avaliacao "
                    + " FROM tbl_avaliacao a"
                    + " JOIN tbl_personagem p ON p.id = a.id_personagem"
                    + " WHERE p.tipoItem = " + tpItem
                    + " GROUP BY p.id"
                    + " ORDER BY media_avaliacao DESC"
                    + " LIMIT 5;";

        // Criacao da variavel que sera retornada
        List <Personagem> personagens = new ArrayList<>();

        // try-with-resources do PreparedStatement e do ResultSet
        try (Statement statement = conexaoSQLite.criarStatement(); ResultSet resultSet = statement.executeQuery(query);){
            
            // Para cada avaliacao cira um novo objeto de usuario, preenche ele e adiciona ela na lista
            while(resultSet.next()){
                // Criacao do novo objeto modelo.Avaliacao
                Personagem avaliacao = new Personagem();
                
                // Preenchimento do objeto Usuario criado, com os dados do resultSet (BD)
                avaliacao.setId(resultSet.getInt("id"));
                avaliacao.setNome(resultSet.getString("nome"));
                
                // Adicao do objeto na lista
                personagens.add(avaliacao);
            }
            return personagens;
        } catch(SQLException e){
            System.err.println(e.getMessage());
        } 
        return personagens;
    }

    /**
     * Retorna o numero de personagens que aparecem na tabela de avaliacao
     * @return
     */
    public int qtdePersonagensAvaliados(){
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Query de busca (retorna o numero de personagens que aparecem na tabela de avaliacao)
        String query = "SELECT COUNT(DISTINCT id_personagem) AS num_personagens_avaliados FROM tbl_avaliacao;";

        int qtde = 0;

        // try-with-resources do PreparedStatement e do ResultSet
        try (Statement statement = conexaoSQLite.criarStatement(); ResultSet resultSet = statement.executeQuery(query);){
            
            while(resultSet.next())
                qtde = resultSet.getInt("num_personagens_avaliados");

            return qtde;
        } catch(SQLException e){
            System.err.println(e.getMessage());
        } 
        return qtde;
    }
    
    // media de avaliacao do personagem
    public double mediaAvaliacaoPersonagem(int idPersonagem){
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Query de busca (retorna o numero de personagens que aparecem na tabela de avaliacao)
        String query = "SELECT AVG(avaliacao) AS media_avaliacao FROM tbl_avaliacao WHERE id_personagem = " + idPersonagem + ";";

        double media = 0;

        // try-with-resources do PreparedStatement e do ResultSet
        try (Statement statement = conexaoSQLite.criarStatement(); ResultSet resultSet = statement.executeQuery(query);){
            
            while(resultSet.next())
                media = resultSet.getDouble("media_avaliacao");

            return media;
        } catch(SQLException e){
            System.err.println(e.getMessage());
        } 
        return media;
    }

    // quantidade de personagens nao avaliados
    public int qtdePersonagensNaoAvaliados(){
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Query de busca (retorna o numero de personagens que aparecem na tabela de avaliacao)
        String query = "SELECT COUNT(*) AS num_personagens_nao_avaliados FROM tbl_personagem WHERE id NOT IN (SELECT id_personagem FROM tbl_avaliacao);";

        int qtde = 0;

        // try-with-resources do PreparedStatement e do ResultSet
        try (Statement statement = conexaoSQLite.criarStatement(); ResultSet resultSet = statement.executeQuery(query);){
            
            while(resultSet.next())
                qtde = resultSet.getInt("num_personagens_nao_avaliados");

            return qtde;
        } catch(SQLException e){
            System.err.println(e.getMessage());
        } 
        return qtde;
    }

    // quantidade de avaliacoes de um usuario
    public int quantidadeAvaliacoesUsuario(int idUsuario){
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Query de busca (retorna o numero de personagens que aparecem na tabela de avaliacao)
        String query = "SELECT COUNT(*) AS num_avaliacoes FROM tbl_avaliacao WHERE id_usuario = " + idUsuario + ";";

        int qtde = 0;

        // try-with-resources do PreparedStatement e do ResultSet
        try (Statement statement = conexaoSQLite.criarStatement(); ResultSet resultSet = statement.executeQuery(query);){
            
            while(resultSet.next())
                qtde = resultSet.getInt("num_avaliacoes");

            return qtde;
        } catch(SQLException e){
            System.err.println(e.getMessage());
        } 
        return qtde;
    }
}