package br.ufla.gac106.s2022_2.PersonaOpina.modelos;

public class Personagem {
    private int id;
    private String nome;
    private String descricao;
    private int idade;
    private String localOrigem;
    private int tipoItem; // filme ou game
    private String nomeObra; // nome do filme ou jogo
    private String habilidade;

    // Construtor
    public Personagem(String nome, String descricao, int idade, String localOrigem, int tipoItem, String nomeObra, String habilidade){
        
        this.nome = nome;
        this.descricao = descricao;
        this.idade = idade;
        this.localOrigem = localOrigem;
        this.tipoItem = tipoItem;
        this.nomeObra = nomeObra;
        this.habilidade = habilidade;
    }

    // Construtor vazio
    public Personagem(){
    }

    public void setId(int id){
        this.id = id;
    }
    
    public void setNome(String nome){
        this.nome = nome;
    }

    public int getId(){
        return this.id;
    }

    public String getNome(){
        return this.nome;
    }

    public String getDescricao(){
        return this.descricao;
    }

    public int getIdade(){
        return this.idade;
    }

    public String getLocalOrigem(){
        return this.localOrigem;
    }

    // 1 = filme, 2 = jogo
    public int getTipoItem(){
        return this.tipoItem;
    }

    public String getNomeObra(){
        return this.nomeObra;
    }

    public String getHabilidade(){
        return this.habilidade;
    }

    public boolean ehPersonagemFilme(){
        return this.getTipoItem() == 1;
    } 
    public boolean ehPersonagemJogo(){
        return this.getTipoItem() == 2;
    }
}

