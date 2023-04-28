package br.ufla.gac106.s2022_2.PersonaOpina.views;

import br.ufla.gac106.s2022_2.PersonaOpina.modelos.Usuario;

public class MenuFactoryAVAL extends ModuloFactory{

    @Override
    public void criarMenu(Usuario usuario) {
        new ModuloAvaliativo(usuario);
    }
    
}
