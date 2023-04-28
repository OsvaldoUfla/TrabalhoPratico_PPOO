package br.ufla.gac106.s2022_2.PersonaOpina.modelos;

public class Comentario {
    private int id;
    private int id_usuario;
    private int id_personagem;
    private String comentario;
    private String data;
    private String hora;
    private Personagem personagem;
    private Usuario usuario;

    // Construtor
    public Comentario(int id_usuario, int id_personagem, String comentario){
        this.id_usuario = id_usuario;
        this.id_personagem = id_personagem;
        this.comentario = comentario;
    }

    // Construtor vazio
    public Comentario(){
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

    public String getComentario(){
        return this.comentario;
    }

    public String getData(){
        return this.data;
    }

    public String getHora(){
        return this.hora;
    }
    
    public void setId(int id){
        this.id = id;
    }

    // Seta o comentario
    public void setComentario(String comentario){
        this.comentario = comentario;
    }

    // Seta o id do usuario
    public void setId_usuario(int id_usuario){
        this.id_usuario = id_usuario;
    }

    // Seta o id do personagem
    public void setId_personagem(int id_personagem){
        this.id_personagem = id_personagem;
    }

    public void setData(String data){
        this.data = data;
    }

    public void setHora(String hora){
        this.hora = hora;
    }

    public void setPersonagem(Personagem personagem) {
        this.personagem = personagem;
    }

    public Personagem getPersonagem() {
        return personagem;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }
}
