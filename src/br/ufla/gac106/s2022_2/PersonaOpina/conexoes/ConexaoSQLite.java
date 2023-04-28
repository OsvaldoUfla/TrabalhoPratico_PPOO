package br.ufla.gac106.s2022_2.PersonaOpina.conexoes;

import java.sql.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConexaoSQLite {
    
    private static ConexaoSQLite conexaoSQLite;

    private Connection conexao;
    
    private ConexaoSQLite(){}

    public static ConexaoSQLite getConexaoSQLite(){
        if(conexaoSQLite == null){
            conexaoSQLite = new ConexaoSQLite();
            conexaoSQLite.conectar();
        }
        return conexaoSQLite;
    }

    public boolean conectar(){
        try {
            String url = "jdbc:sqlite:banco/persona_opina.db";

            this.conexao = DriverManager.getConnection(url);

        } catch (SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean desconectar(){
        try {
            if(!this.conexao.isClosed())
                this.conexao.close();

        } catch(SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
        return true;
    }

    public Statement criarStatement(){
        try {
            return (Statement) this.conexao.createStatement();
        } catch (SQLException e) {
            return null;
        }
    }

    public PreparedStatement criarPreparedStatement(String sql){
        try {
            if(this.conexao.isClosed())
                conectar();
            
            return this.conexao.prepareStatement(sql);
        } catch (SQLException e) { // excecao que é lançada quando o banco de dados está fechado
            //System.out.println("ErrorCode: " + e.getErrorCode() + "\n-> ErrorMessage: " +  e.getMessage() + "\n-> SQLState: " + e.getSQLState());
            return null;
        }
    }

    public Connection getConexao(){
        if(this.conexao == null){
            conectar();
        }
        return this.conexao;
    }
}