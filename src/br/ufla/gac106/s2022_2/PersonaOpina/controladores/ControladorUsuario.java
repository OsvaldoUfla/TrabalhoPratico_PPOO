package br.ufla.gac106.s2022_2.PersonaOpina.controladores;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import br.ufla.gac106.s2022_2.PersonaOpina.conexoes.ConexaoSQLite;
import br.ufla.gac106.s2022_2.PersonaOpina.conexoes.CriarBancoSQLite;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Usuario;

public class ControladorUsuario{

    /**
     * Faz a insercao de um novo usuario no banco, de acordo com os dados informados por parametro
     * @param modelos.Usuario 
     */
    public void inserir(Usuario usuario) {
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Chama o metodo de criacao de uma nova tabela. A tabela so eh criada se ela nao existe ainda
        CriarBancoSQLite criar = new CriarBancoSQLite();
        criar.criarTabelaPessoa();

        // Query de insercao na tabela, com valore dinamicos
        String query = "INSERT INTO tbl_usuario"
                        + "(nome, login, senha, nivel) "
                        + "VALUES (?, ?, ?, ?);";

        // try-with-resources para o PreparedStatement
        try (PreparedStatement preparedStatement = conexaoSQLite.criarPreparedStatement(query)) {

            // Preenchendo os valores dinamicos da query
            preparedStatement.setString(1, usuario.getNome());
            preparedStatement.setString(2, usuario.getLogin());
            preparedStatement.setString(3, usuario.getSenha());
            preparedStatement.setInt(4, usuario.getNivel());

            // Executando a query e salvando o retorno de PreparedStatement.executeUpdate() na variavel `resultado`
            int resultado = preparedStatement.executeUpdate();

            // Se nao foi possivel inserir o usuario, informa o erro
            if (resultado != 1)
                throw new RuntimeException("Erro ao inserir a pessoa.");

        } 
        catch (SQLException e) {
            // O ErrorCode 19 ocorre quando o login informado ja existe no BD
            if (e.getErrorCode() == 19)
                throw new IllegalArgumentException("Já existe um usuário com o login informado. Por favor, escolha outro login.");
            else
                throw new RuntimeException("Erro ao inserir a pessoa.");
        }        
    }
    
    /**
     * Faz a edicao de um usuario especifico com os valores passados por parametro
     * @param usuario
     */
    /*public void editar(Usuario usuario) {
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Query de edicao, passando um valores dinamicos
        String query = "UPDATE tbl_pessoa"
                    + " SET " 
                    + " nome = ?,"
                    + " login = ?, "
                    + " senha = ? "
                    + " WHERE id = ?;";

        // try-with-resources para o PreparedStatement
        try (PreparedStatement preparedStatement = conexaoSQLite.criarPreparedStatement(query);) {
           
            // Definindo os valores dinamicos da query
            preparedStatement.setString(1, usuario.getNome());
            preparedStatement.setString(2, usuario.getLogin());
            preparedStatement.setString(3, usuario.getSenha());
            preparedStatement.setInt(4, usuario.getId());

            // Execucao da query SQL
            preparedStatement.executeUpdate();

        } catch(SQLException e){
            // O ErrorCode 19 ocorre quando o login informado ja existe no BD
            if (e.getErrorCode() == 19) {
                System.out.println("Já existe um usuário com o login informado. Por favor, escolha outro login.");
                // Voltar para a tela de edicao
            } else {
                System.out.println("Erro ao inserir a pessoa");
            }
        }
    }*/

    /**
     * Faz a remocao de um usuario especifico do BD
     * @param id
     */
    /*public void remover(int id) {
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Query de remocao, passando um valor dinamico
        String query = "DELETE FROM tbl_pessoa WHERE id = ?;";

        // try-with-resources para o PreparedStatement
        try (PreparedStatement preparedStatement = conexaoSQLite.criarPreparedStatement(query);) {
            
            // Definindo o valor dinamico da query
            preparedStatement.setInt(1, id);
            
            // Executa a query SQL e salva o retorno de preparedStatement.executeUpdate() (qtde de linhas removidas) na variavel `linhasDeletadas`
            int linhasDeletadas = preparedStatement.executeUpdate();

            // Se alguma linha for removida, mostra-se a mensagem de remocao
            if(linhasDeletadas > 0)
                System.out.println("Foram deletadas " + linhasDeletadas + " linhas"); //throw exception
            // Se nenhuma linha for removida, mostra-se a mensagem informando o ocorrido
            else
                System.out.println("Nao foi encontrado nenhum usuario com ID " + id); //throw exception

        } catch (SQLException e){
            System.err.println(e.getMessage());
        }
    }*/

    /**
     * Verifica se o login e a senha infordamos constam no BD
     * 
     * @param login
     * @param senha
     * @return objeto de modelo.Usuario, se foi encontrado um registro com o login e a senha informados / null se os dados na forem encontrados
     */
    public Usuario autenticar(String login, String senha){
        // Chamada Singleton do objeto ConexaoSQLite
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

        // Chama o metodo de criacao de uma nova tabela. A tabela so eh criada se ela nao existe ainda
        CriarBancoSQLite criar = new CriarBancoSQLite();
        criar.criarTabelaPessoa();
        
        // Variavel ResultSet que guarda o retorno do BD
        ResultSet resultSet = null;

        // Query de consulta por login e senha informados
        String query = "SELECT * FROM tbl_usuario WHERE login = ? AND senha = ?;";

        // Variavel de retorno
        Usuario pessoa = null;

        // try-with-resources para o PreparedStatement
        try(PreparedStatement preparedStatement = conexaoSQLite.criarPreparedStatement(query);) {

            // Fazendo a tratativa dos valores dinamicos da query
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, senha);

            // Recebendo o retorno do BD
            resultSet = preparedStatement.executeQuery();

            // Enquanto a leitura do retorno eh feita, preenche a variavel modelo.Usuario
            while(resultSet.next()){
                pessoa = new Usuario();
                pessoa.setId(resultSet.getInt("id"));
                pessoa.setNome(resultSet.getString("nome"));
                pessoa.setLogin(resultSet.getString("login"));
                pessoa.setSenha(resultSet.getString("senha"));
                pessoa.setNivel(resultSet.getInt("nivel"));
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }finally{
            try {
                resultSet.close();
            } catch(SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return pessoa;
    }
    
    /*public Usuario buscar(){
        return null;
    }*/

    /**
     * Busca um usuario especifico pelo id
     * @param id
     * @return modelo.Usuario
     */
    /*public Usuario buscarUm(int id){
        ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();
        
        ResultSet resultSet = null;

        Usuario usuario = null;
        
        String query = "SELECT * FROM tbl_pessoa WHERE id = ?;";

        try (PreparedStatement preparedStatement = conexaoSQLite.criarPreparedStatement(query);) {
            preparedStatement.setInt(1, id);

            resultSet = preparedStatement.executeQuery();

            while(resultSet.next()){
                usuario.setId(resultSet.getInt("id"));
                usuario.setNome(resultSet.getString("nome"));
                usuario.setLogin(resultSet.getString("login"));
                usuario.setSenha(resultSet.getString("senha"));
                usuario.setNivel(resultSet.getInt("nivel")); 
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }finally{
            try {
                resultSet.close();
            } catch(SQLException ex) {
                System.err.println(ex.getMessage());
            }
        }
        return usuario;
    }*/
}
