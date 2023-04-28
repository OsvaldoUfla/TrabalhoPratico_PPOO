package br.ufla.gac106.s2022_2.PersonaOpina;

/**
 * Interface que representa uma avaliação de um item do tema trabalhado
 * (ex: avaliação de um filme ou sére)
 */
public interface Avaliacao {
    /**
     * Nome do item avaliado (ex: "O Senhor dos Anéis")
     * @return o nome do item
     */
    String nomeItemAvaliado();

    /**
     * Classificação média avaliada do item (ex: 4.5)
     */
    double classificacaoMedia();

    int getId_usuario();

    // Retorna o id do personagem
    int getId_personagem();

    // Retorna o id d avaliador
    int getId();

    // Retorna id da avaliação
    int getAvaliacao();

    // Retorna a data da avaliação
    String getData();

    // Retorna a hora da avaliação
    String getHora();
}
