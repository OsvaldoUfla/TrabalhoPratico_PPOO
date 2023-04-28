package br.ufla.gac106.s2022_2.PersonaOpina.modelos;

import java.util.Collection;

import br.ufla.gac106.s2022_2.PersonaOpina.Avaliacao;
import br.ufla.gac106.s2022_2.PersonaOpina.Avaliacoes;

public class AvaliacoesFilmeJogo implements Avaliacoes {
    private String tema;
    private Collection<Avaliacao> colecaoAvaliacoes;

    public AvaliacoesFilmeJogo(String tema, Collection<Avaliacao> colecaoAvaliacoes) {
        this.tema = tema;
        this.colecaoAvaliacoes = colecaoAvaliacoes;
    }

    @Override
    public String temaAvaliacao() {
        return this.tema;
    }

    @Override
    public Collection<Avaliacao> colecaoAvaliacoes() {
        return this.colecaoAvaliacoes;
    }
    
}
