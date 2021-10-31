package Poker;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Baralho {
    public List<Carta> Cartas = new ArrayList<>();
    public int c = 0;
    public Baralho(){montar();}
    public void Embaralhar() {
        embaralhar();
    }
    public void Exibir(){
        exibir();
    }
    private void montar(){
        for (Naipe n: Naipe.values()) {
            for (Valor v: Valor.values()) {
                Carta carta = new Carta();
                carta.setNaipe(n);
                carta.setValor(v);
                carta.setValorCarta(v.getValorCarta());
                Cartas.add(carta);
            }
        }
    }
    private void embaralhar(){
        Random random = new Random();
        int rand;
        do
        {
            rand = random.nextInt(10);
        }while (rand < 3);

        for (int i = 0; i <= rand; i++) {

            Collections.shuffle(Cartas);
        }
    }
    private void exibir(){
        for (Carta c : Cartas) {
            System.out.println("Carta: " + c.getValor() + " de " + c.getNaipe());
        }
    }
    public  Carta daCarta(){
        c++;
        return Cartas.get(c-1);
    }
}
