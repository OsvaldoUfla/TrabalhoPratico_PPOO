package br.ufla.gac106.s2022_2.PersonaOpina.conexoes;

import java.sql.Statement;
import java.sql.SQLException;

public class CriarBancoSQLite {
    
    private ConexaoSQLite conexaoSQLite = ConexaoSQLite.getConexaoSQLite();

    public void criarTabelaPessoa(){
        String sql1 = "CREATE TABLE IF NOT EXISTS tbl_usuario "
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, " 
                    + "nome TEXT NOT NULL, " 
                    + "login TEXT NOT NULL UNIQUE, "
                    + "senha TEXT NOT NULL, "
                    + "nivel INTEGER NOT NULL);";

        String sql2 = "CREATE TABLE IF NOT EXISTS tbl_personagem "
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, " 
                    + "nome TEXT NOT NULL UNIQUE, " 
                    + "descricao TEXT NOT NULL, "
                    + "idade INTEGER NOT NULL, "
                    + "localOrigem TEXT NOT NULL, "
                    + "tipoItem TEXT NOT NULL, "
                    + "nomeObra TEXT NOT NULL, "
                    + "habilidade TEXT NOT NULL);";

        String sql3 = "CREATE TABLE IF NOT EXISTS tbl_comentario"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "comentario TEXT NOT NULL, "
                    + "data_publicacao TEXT NOT NULL, "
                    + "hora_publicacao TEXT NOT NULL, "
                    + "id_usuario INT NOT NULL, "
                    + "id_personagem INT NOT NULL, "
                    + "FOREIGN KEY (id_usuario) REFERENCES tbl_usuario(id), "
                    + "FOREIGN KEY (id_personagem) REFERENCES tbl_personagem(id));";
            
        String sql4 = "CREATE TABLE IF NOT EXISTS tbl_avaliacao"
                    + "(id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "avaliacao INT NOT NULL, "
                    + "data_publicacao TEXT NOT NULL, "
                    + "hora_publicacao TEXT NOT NULL, "
                    + "id_usuario INT NOT NULL, "
                    + "id_personagem INT NOT NULL, "
                    + "FOREIGN KEY (id_usuario) REFERENCES tbl_usuario(id), "
                    + "FOREIGN KEY (id_personagem) REFERENCES tbl_personagem(id));";

        String sql5 = "INSERT INTO tbl_usuario (nome, login, senha, nivel) VALUES ('adm', 'adm', 'adm', 3);";

        try (Statement stmt = this.conexaoSQLite.criarStatement()){

            stmt.execute(sql1);
            stmt.execute(sql2);
            stmt.execute(sql3);
            stmt.execute(sql4);
            
            try{
                stmt.executeUpdate(sql5);
            } catch (SQLException e){
                // Nao foi necessario tratar o erro
                // Sabe-se que se trata do erro no unique
                // SQLError: [SQLITE_CONSTRAINT_UNIQUE] A UNIQUE constraint failed
                // Error code: 19
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}