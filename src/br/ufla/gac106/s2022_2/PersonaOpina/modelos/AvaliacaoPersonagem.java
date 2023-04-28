package br.ufla.gac106.s2022_2.PersonaOpina.modelos;

import br.ufla.gac106.s2022_2.PersonaOpina.Avaliacao;

public class AvaliacaoPersonagem implements Avaliacao{
    private int id;
    private int id_usuario;
    private int id_personagem;
    private int avaliacao;
    private String data;
    private String hora;
    private Personagem personagem;
    private double media;

    // Construtor 
    public AvaliacaoPersonagem(int id_usuario, int id_personagem, int avaliacao){
        this.id_usuario = id_usuario;
        this.id_personagem = id_personagem;
        this.avaliacao = avaliacao;
    }

    // Construtor vazio
    public AvaliacaoPersonagem(){
    }

    public void setMedia(double media) {
        this.media = media;
    }

    public double getMedia() {
        return media;
    }
    
    public void setPersonagem(Personagem personagem) {
        this.personagem = personagem;
    }

    public Personagem getPersonagem() {
        return personagem;
    }

    public int getId(){
        return this.id;
    }

    public int getId_usuario(){
        return this.id_usuario;
    }

    public int getId_personagem(){
        return this.id_personagem;
    }

    public int getAvaliacao(){
        return this.avaliacao;
    }

    public String getData(){
        return this.data;
    }

    public String getHora(){
        return this.hora;
    }
    
    // Seta o id
    public void setId(int id){
        this.id = id;
    }

    // Seta o id do usuario
    public void setId_usuario(int id_usuario){
        this.id_usuario = id_usuario;
    }

    // Seta o id do personagem  
    public void setId_personagem(int id_personagem){
        this.id_personagem = id_personagem;
    }

    // Seta a data
    public void setData(String data){
        this.data = data;
    }

    // Seta a hora
    public void setHora(String hora){
        this.hora = hora;
    }

    @Override
    public String nomeItemAvaliado() {
        return this.personagem.getNome();
    }

    @Override
    public double classificacaoMedia() {
        return this.media;
    }

    // Seta uma avaliacao
    public void setAvaliacao(int avaliacao2) {
        this.avaliacao = avaliacao2;
    }
}
