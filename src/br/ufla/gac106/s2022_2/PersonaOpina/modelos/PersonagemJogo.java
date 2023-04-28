package br.ufla.gac106.s2022_2.PersonaOpina.modelos;

public class PersonagemJogo extends Personagem {
    // Construtor
    public PersonagemJogo(String nome, String descricao, int idade, String localOrigem, int tipoItem, String nomeObra, String habilidade){
        super(nome, descricao, idade, localOrigem, tipoItem, nomeObra, habilidade);
    }
    // Retorna true se o personagem for de um jogo
    public boolean ehPersonagemJogo(){
        if(this.getTipoItem() == 2){
            return true;
        }
        return false;
    }
}
