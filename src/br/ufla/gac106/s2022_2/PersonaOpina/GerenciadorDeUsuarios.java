package br.ufla.gac106.s2022_2.PersonaOpina;

import br.ufla.gac106.s2022_2.PersonaOpina.controladores.ControladorUsuario;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Usuario;

public class GerenciadorDeUsuarios {
    ControladorUsuario controladorUsuario;// Variavel do tipo controladorPersonagem
    // Construtor
    public GerenciadorDeUsuarios(){
        controladorUsuario = new ControladorUsuario();// Objeto do tipo controladorPersonagem
    }
    /**
     * Retorna se o usuario Existe
     * Se existir retorna o usuario
     * Se não existir Lança uma exceção
     * @return modelo.Usuario
     * @throws IllegalArgumentException
     */
    public Usuario usuarioExiste(String login, String senha){
        Usuario usuario = controladorUsuario.autenticar(login, senha);
        if(usuario != null){
            return usuario;
        } else {
            // Se o usuario não existe, lança uma exceção
            throw new IllegalArgumentException("Usuario ou senha invalido");
        }
    }

    /**
     * Cria um usuario
     * @throws IllegalArgumentException
     * @throws RuntimeException
     */
    public void criarUsuario(Usuario usuarioCriador, String nome, String login, String senha, int nivel){
        if(usuarioCriador.getNivel() < nivel){
            // Lança uma exceção se o usuario que está criando o usuario não tem 
            //permissão para criar um usuario com o nivel informado
            throw new IllegalArgumentException("Você não tem permissão para cadastrar um usuario com esse nivel");
        }
        // Cria o usuario
        Usuario usuario = new Usuario(nome, login, senha, nivel);
        try{
            // Insere o usuario no banco de dados
            controladorUsuario.inserir(usuario);
        } 
        catch (IllegalArgumentException e){   
            // Se o usuario já existe, lança uma exceção      
            throw new IllegalArgumentException("Já existe um usuário com o login informado. Por favor, escolha outro login.");
        }
        catch (Exception e){
            // Se ocorrer algum outro erro, lança uma exceção
            throw new RuntimeException("Erro ao cadastrar o usuario");
        }
    }

    /**
     * Cria um usuario sem pedir o nivel
     * Usado para criar um usuario de nivel 1 ou administrador inicial
     * @throws IllegalArgumentException
     */
    public void criarUsuario(String nome, String login, String senha){
        // Nivel 1 é o nivel de usuario comum
        int nivel = 1; 
        
        // Cria o usuario e insere no banco de dados
        try{
            controladorUsuario.inserir(new Usuario(nome, login, senha, nivel));
        } catch (IllegalArgumentException e){
            // Se o usuario já existe, lança uma exceção
            throw new IllegalArgumentException("Já existe um usuário com o login informado. Por favor, escolha outro login.");
        }
    }
}
