package br.ufla.gac106.s2022_2.PersonaOpina;

import br.ufla.gac106.s2022_2.PersonaOpina.controladores.ControladorPersonagem;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.PersonagemFilme;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.PersonagemJogo;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Usuario;

public class Administracao {
    ControladorPersonagem cp;// Variavel do tipo controladorPersonagem

    // Construtor
    public Administracao(Usuario usuario){
        cp = new ControladorPersonagem();// Objeto do tipo controladorPersonagem
        if(usuario.getNivel() < 2){
            throw new IllegalArgumentException("Usuario nao tem permissao para acessar o modulo administrativo.");
        }
    } 
    // Cria um novo personagem e o insere no banco de dados
    public void novoPersonagem(int id, String nome, String descricao, int idade, String localOrigem, int tipoItem, String nomeObra, String habilidade) throws IllegalArgumentException{
        if(tipoItem == 1){
            cp.inserir(new PersonagemFilme(nome, descricao, idade, localOrigem, tipoItem, nomeObra, habilidade));
        } else if(tipoItem == 2){
            cp.inserir(new PersonagemJogo(nome, descricao, idade, localOrigem, tipoItem, nomeObra, habilidade));        
        }
            
    }
    
    /**
     * Chama o metodo de excluir personagens do controlador de personagem
     * @param id ID do personagem que deseja excluir
     */
    public void excluirPersonagem(int id, Usuario usuario){
        if(usuario.getNivel() < 3){
            throw new IllegalArgumentException("Usuario nao tem permissao para excluir personagens.");
        }
        // Chamada do metodo remover de controladorPersonagem
        cp.remover(id);
    }

}
