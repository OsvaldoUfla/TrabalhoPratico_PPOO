package br.ufla.gac106.s2022_2.PersonaOpina.views;
import java.util.Scanner;

/**
 * 
 * @class de metodos estaticos
 * Os metodos recebe uma mensagem e retornam um valor de acordo com um padrao informado por parametro
 */
public class GetInfo {

    private Scanner entrada = new Scanner(System.in);

    /**
     * 
     * @param msg 
     * Formatacao da mensagem passada ao usuario
     */
    private void msgGetEm(String msg){
        System.out.print(msg + "\n> ");
    }

    /**
     * 
     * @param msg
     * @return valor string informado na entrada de dados
     */
    public String getEmStr(String msg){
        msgGetEm(msg);
        String valor = entrada.nextLine();
        return valor;
    }

    /**
     * 
     * @param msg
     * @return valor inteiro passado pela etrada de dados
     * O metodo trata a entrada de valores invalidos por try_catch e recursividade
     */
    public int getEmInteito(String msg){
        msgGetEm(msg);
        int valor;
        try{
            valor = Integer.parseInt(entrada.nextLine());
            return valor;
        } catch (NumberFormatException e) {
            System.out.println("FAVOR INFORMAR UM NUMERO INTEIRO");
            return getEmInteito(msg);
        }
    }


    /**
     * 
     * @param inicio incluso no intervalo de numeros
     * @param fim incluso no intervalo de numeros
     * @param msg 
     * @return valor inteiro dentro de um intervalo especificado passado pela entrada de dados
     * O metodo trata a entrada de valores invalidos por try_catch e recursividade
     */
    public int getEscolhaInt(int inicio, int fim, String msg){
        msgGetEm(msg);
        int valor;
        try{
            valor = Integer.parseInt(entrada.nextLine());
            if((valor < inicio) || (valor > fim)){
                System.out.println("Comando invalido! Os valores devem estar entre " + inicio + " e " + fim + ".");
                return getEscolhaInt(inicio, fim, msg);
            }

            return valor;

        } catch (NumberFormatException e) {
            System.out.println("FAVOR INFORMAR UM NUMERO INTEIRO");
            return getEscolhaInt(inicio, fim, msg);
        }
    }

    /**
     * 
     * @param conjunto string contendo os caracteres que serao aceitos
     * @param msg
     * @return valor char passado pela entrada de dados
     * O metodo trata a entrada de valores invalidos por recursividade
     */
    public char getEscolhaChar(String conjunto, String msg){
        msgGetEm(msg);
        char valor = entrada.nextLine().charAt(0);
        boolean teste = false;
        for(int i = 0; i < conjunto.length(); i++){
            if(valor == conjunto.charAt(i))
                teste = true;
        }
        if(teste)
            return valor;
        else {
            System.out.println("Comando invalido");
            return getEscolhaChar(conjunto, msg);
        }
    }
}
