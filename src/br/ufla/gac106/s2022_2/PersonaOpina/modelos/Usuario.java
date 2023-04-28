package br.ufla.gac106.s2022_2.PersonaOpina.modelos;

public class Usuario {
    private int id;
    private String nome;
    private String login;
    private String senha;
    private int nivel;

    // Construtor 
    public Usuario(String nome, String login, String senha, int nivel){
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.nivel = nivel;
    }

    // Construtor vazio
    public Usuario(){
    }

    public void setId(int id){
        this.id = id;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setLogin(String login){
        this.login = login;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public void setNivel(int nivel){
        this.nivel = nivel;
    }

    public int getId(){
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public String getLogin(){
        return this.login;
    }

    public String getSenha(){
        return this.senha;
    }

    public int getNivel(){
        return this.nivel;
    }

    public String getDadosParaCadastro(){
        return "Nome: " + this.nome
             + "\nLogin: " + this.login
             + "\nNivel de acesso: " + this.nivel;
    }

    public boolean ehAdministrador(){
        return this.nivel >= 3;
    }
}