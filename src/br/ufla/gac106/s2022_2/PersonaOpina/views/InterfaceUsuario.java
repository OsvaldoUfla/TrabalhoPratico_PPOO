package br.ufla.gac106.s2022_2.PersonaOpina.views;
import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Usuario;

//import br.ufla.gac106.s2022_2.PersonaOpina.views.ModuloAvaliativo;

public class InterfaceUsuario {
    private ModuloFactory ma;
    private GetInfo get;

    private Usuario usuario;
    // Construtor
    public InterfaceUsuario(Usuario usuario){
        this.usuario = usuario;
        get = new GetInfo();
    }

    /**
     * Seleciona um modulo para o usuario
     */
    public void menu() {

        int modulo = get.getEscolhaInt(1, 4, "Escolha um modulo:"
        +"\n1 - Administração"
        +"\n2 - Avaliação"
        +"\n3 - Relatórios"
        +"\n4 - Sair ");
        switch(modulo){
            case 1:
                try{
                    ma = new MenuFactoryADM(); //polimorfismo
                    ma.criarMenu(usuario);
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                    menu();
                }
                break;

            case 2:
                ma = new MenuFactoryAVAL(); //polimorfismo
                ma.criarMenu(usuario);
                break;

            case 3:
                ma = new MenuFactoryREL(); //polimorfismo
                ma.criarMenu(usuario);
                break;

            case 4:
                new Index().index();
                break;
        }
    }
}
