package Poker;
import java.util.ArrayList;
import java.util.List;

public class Jogador {

    private String nome;

    private List<Carta> mao = new ArrayList<Carta>();

    private int pontos;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Carta> getMao() {
        return mao;
    }

    public void setMao(Carta mao) {
        this.mao.add(mao);
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public void limpaCarta(){
        mao.clear();
    }
}
