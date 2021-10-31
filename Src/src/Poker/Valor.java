package Poker;
public enum Valor {
    Dois(1),
    Tres(2),
    Quatro(3),
    Cinco(4),
    Seis(5),
    Sete(6),
    Oito(7),
    Nove(8),
    Dez(9),
    Valete(10),
    Dama(11),
    Reis(12),
    As(13);

    private int valorCarta;

    Valor(int valorCarta){
        this.valorCarta = valorCarta;
    }

    public int getValorCarta(){
        return valorCarta;
    }
}
