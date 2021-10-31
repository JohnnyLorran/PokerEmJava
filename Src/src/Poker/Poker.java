package Poker;
import java.util.*;

public class Poker {
    private Baralho baralho = new Baralho();
    private Scanner input = new Scanner(System.in);
    private List<Jogador> jogadores = new ArrayList<>();
    private List<Carta> Mesa = new ArrayList<>();
    private Random random = new Random();
    private int deal, nextJ,players, cobrir, vl_Total, aposta, sb ,sbP, bb,bbP, ganhador, quiters[],op, pRaiser, pRaiserF;
    private boolean play,firstp, cdeal, pedirMesa, flopmais;

    public Poker() {
        jogo();
    }

    private void jogo() {
        play = true;
        cdeal = false;
        pedirMesa = false;
        flopmais = false;
        System.out.println(ConsoleColors.RESET +"\n\nBem vindo ao jogo de Poker !");
        System.out.println("\nQuantos jogadores irão jogar?");
        do {
            try {
                players = isInt();
                if (players <= 1) {
                    System.out.println("Para iniciar o jogo, é necessário no mínimo 2 jogadores.");
                } else if (players >= 9) {
                    System.out.println("O limite máximo de jogadores é 8.");
                } else {
                    break;
                }
            } catch (Exception e) {
                System.out.println("\nErro [" + ConsoleColors.RED + e + ConsoleColors.RESET + "]: Digite apenas a quantidade de jogadores!\n");
            }
        } while (play);
        defineJogadores();
        do{
            baralho.Embaralhar();
            baralho.c = 0;
            Mesa.clear();
            ganhador = 9;
            for (int i = 0; i < quiters.length; i++) {
                quiters[i] = 9;
                jogadores.get(i).limpaCarta();
            }
            if(cdeal){
                if(deal+1 <= (jogadores.size() -1)){
                    deal += 1;
                }else{
                    deal = 0;
                }
            }
            cdeal = true;
            preFlop();
            ganhador = verificaJogadores();
            if(ganhador != 9) {
                LinhaPontilhada();
                System.out.println("\n\nParabéns Jogador: " + ConsoleColors.BLUE + jogadores.get(ganhador).getNome() + ConsoleColors.GREEN + " você venceu!" + ConsoleColors.RESET);
                System.out.println("Levou a bolada de: " + ConsoleColors.GREEN + "R$ " +ConsoleColors.YELLOW + vl_Total + ConsoleColors.RESET);
            }else{
                LinhaPontilhada();
                System.out.println(ConsoleColors.PURPLE + "\nPré-Flop Terminou, Próxima Rodada!\n" + ConsoleColors.RESET);
                LinhaPontilhada();
                for (int i = 0; i < 3; i++) {
                    cartasMesa();
                }
                flop();
                ganhador = verificaJogadores();
                if(ganhador != 9) {
                    LinhaPontilhada();
                    System.out.println("\n\nParabéns Jogador: " + ConsoleColors.BLUE + jogadores.get(ganhador).getNome() + ConsoleColors.GREEN + " você venceu!" + ConsoleColors.RESET);
                    System.out.println("Levou a bolada de: " + ConsoleColors.GREEN + "R$ " +ConsoleColors.YELLOW + vl_Total + ConsoleColors.RESET);
                }else{
                    LinhaPontilhada();
                    System.out.println(ConsoleColors.PURPLE + "\nFlop Terminou, Próxima Rodada!\n" + ConsoleColors.RESET);
                    LinhaPontilhada();
                    cartasMesa();
                    turn();
                    ganhador = verificaJogadores();
                    if(ganhador != 9) {
                        LinhaPontilhada();
                        System.out.println("\n\nParabéns Jogador: " + ConsoleColors.BLUE + jogadores.get(ganhador).getNome() + ConsoleColors.GREEN + " você venceu!" + ConsoleColors.RESET);
                        System.out.println("Levou a bolada de: " + ConsoleColors.GREEN + "R$ " +ConsoleColors.YELLOW + vl_Total + ConsoleColors.RESET);
                    }else{
                        LinhaPontilhada();
                        System.out.println(ConsoleColors.PURPLE + "\nTurn Terminou, Próxima Rodada!\n" + ConsoleColors.RESET);
                        LinhaPontilhada();
                        cartasMesa();
                        river();
                        ganhador = verificaJogadores();
                        if(ganhador != 9) {
                            LinhaPontilhada();
                            System.out.println("\n\nParabéns Jogador: " + ConsoleColors.BLUE + jogadores.get(ganhador).getNome() + ConsoleColors.GREEN + " você venceu!" + ConsoleColors.RESET);
                            System.out.println("Levou a bolada de: " + ConsoleColors.GREEN + "R$ " +ConsoleColors.YELLOW + vl_Total + ConsoleColors.RESET);
                        }else{
                            LinhaPontilhada();
                            System.out.println(ConsoleColors.PURPLE + "\nRiver Terminou, Próxima Rodada!\n" + ConsoleColors.RESET);
                            LinhaPontilhada();
                            showDown();
                        }
                    }
                }
            }
            System.out.println("\nDeseja continuar jogando com o mesmo grupo?");
            System.out.println("Sim ---> 1 ");
            System.out.println("Não ---> 2 ");
            System.out.println(ConsoleColors.RED +"Obs: Qualquer valor diferente, encerrará o grupo." +ConsoleColors.RESET);
            op = isInt();
            pedirMesa = false;
            flopmais = false;
        }while(op == 1);
    }

    private void defineJogadores(){
        deal = random.nextInt((players - 1));
        String checkNome;
        String nome;
        if (deal < 0) {
            deal = 0;
        }
        quiters = new int[players];
        for (int i = 0; i <= players - 1; i++) {
            Jogador jogador = new Jogador();
            System.out.println("\nDigite o nome do jogador [" + ConsoleColors.GREEN + (i + 1) + ConsoleColors.RESET+ "] :");
            do{
                nome = input.nextLine();
                checkNome = nome.replace(" ", "");
                if(nome.isEmpty() || checkNome.isEmpty()){
                    System.out.println("Nome vázio, por favor digite um nome válido!");
                }
            }while(nome.isEmpty() || checkNome.isEmpty());
            nome = nome.toUpperCase();
            jogador.setNome(nome);
            jogadores.add(jogador);
        }
    }

    private void preFlop(){
        firstp = false;
        System.out.println(ConsoleColors.GREEN + "\n\nRodada Pré-Flop irá começar: " + ConsoleColors.RESET );
        sbP = deal + 1;
        bbP = deal + 2;
        nextJ = deal + 3;
        if (sbP > (jogadores.size() - 1)) {
            sbP = 0;
            bbP = 1;
            nextJ = 2;
        }
        if (sbP == (jogadores.size() - 1)) {
            bbP = 0;
            nextJ = 1;
        }
        if (bbP == (jogadores.size() - 1)) {
            nextJ = 0;
        }
        System.out.println("O Jogador: " + jogadores.get(sbP).getNome() + " é o Smal Blind, faça sua aposta:");
        sb = isInt();
        LinhaPontilhada();
        System.out.println("O Jogador: " + jogadores.get(bbP).getNome() + " é o Big Blind, faça sua aposta:");
        bb = isInt();
        do{
            if (sb >= bb){
                System.out.println("\n\nO valor deve ser maior que o "+ConsoleColors.RED+"Smal Blind "+ConsoleColors.RESET+"("+ConsoleColors.YELLOW+sb+ConsoleColors.RESET+")");
                bb = isInt();
            }
        }
        while(bb <= sb);
        for (int i = 0; i <= 1; i++) {
            for (int l = nextJ; l <= jogadores.size() - 1; l++) {
                jogadores.get(l).setMao(baralho.daCarta());
            }
            if (nextJ > 0) {
                for (int k = 0; k < nextJ; k++) {
                    jogadores.get(k).setMao(baralho.daCarta());
                }
            }
        }
        vl_Total = sb + bb;
        cobrir = bb;
        int l = nextJ;
        pRaiser = (nextJ - 1);
        if (pRaiser < 0) {
            pRaiser = (jogadores.size() - 1);
        }
        do {
            if (dentrodojogo(l)) {
                System.out.println("Valor atual da mesa: " +ConsoleColors.GREEN + "R$ " + ConsoleColors.YELLOW + vl_Total + ConsoleColors.RESET +"\n");
                if (firstp) {
                    LinhaPontilhada();
                    System.out.print(ConsoleColors.GREEN + "Próximo a jogar é o  " + ConsoleColors.RESET);
                } else {
                    LinhaPontilhada();
                    System.out.print(ConsoleColors.BLUE + "\nO jogador que começará é o " + ConsoleColors.RESET);
                }
                cartasJogador(l);
                escolha(l);
            }
            firstp = true;
            l += 1;
            if (l > (jogadores.size() - 1)){
                l = 0;
            }
        }
        while (l != pRaiser && l >= 0 && l <= (jogadores.size() - 1));
    }

    private void flop(){
        System.out.println(ConsoleColors.GREEN + "\nRodada Flop irá começar!" + ConsoleColors.RESET);
        mostraMesa();
        firstp = false;
        flopmais = true;
        pedirMesa = true;
        int l = sbP;
        pRaiserF = sbP;
        cobrir = 0;
        do{
            if (dentrodojogo(l)) {
                System.out.println("Valor atual da mesa: " +ConsoleColors.GREEN + "R$ " + ConsoleColors.YELLOW + vl_Total + ConsoleColors.RESET);
                if (firstp) {
                    LinhaPontilhada();
                    System.out.print(ConsoleColors.GREEN + "Próximo a jogar é o  " + ConsoleColors.RESET);
                } else {
                    LinhaPontilhada();
                    System.out.print(ConsoleColors.BLUE + "\nO jogador que começará é o " + ConsoleColors.RESET);
                }
                cartasJogador(l);
                escolha(l);
            }
            firstp = true;
            l += 1;
            if (l > (jogadores.size() - 1)){
                l = 0;
            }
        }
        while (l != pRaiserF && l >= 0 && l <= (jogadores.size() - 1));
    }

    private void turn(){
        System.out.println(ConsoleColors.GREEN + "\nRodada Turn irá começar!" + ConsoleColors.RESET);
        mostraMesa();
        flopmais = true;
        pedirMesa = true;
        firstp = false;
        int l = sbP;
        pRaiserF = sbP;
        cobrir = 0;
        do{
            if (dentrodojogo(l)) {
                System.out.println("Valor atual da mesa: " +ConsoleColors.GREEN + "R$ " + ConsoleColors.YELLOW + vl_Total + ConsoleColors.RESET);

                if (firstp) {
                    LinhaPontilhada();
                    System.out.print(ConsoleColors.GREEN + "Próximo a jogar é o  " + ConsoleColors.RESET);
                } else {
                    LinhaPontilhada();
                    System.out.print(ConsoleColors.BLUE + "\nO jogador que começará é o " + ConsoleColors.RESET);
                }
                cartasJogador(l);
                escolha(l);
            }
            firstp = true;
            l += 1;
            if (l > (jogadores.size() - 1)){
                l = 0;
            }
        }
        while (l != pRaiserF && l >= 0 && l <= (jogadores.size() - 1));
    }

    private void river(){
        System.out.println(ConsoleColors.GREEN + "\nRodada River irá começar!" + ConsoleColors.RESET);
        mostraMesa();
        flopmais = true;
        pedirMesa = true;
        firstp = false;
        int l = sbP;
        pRaiserF = sbP;
        cobrir = 0;
        do {
            if (dentrodojogo(l)) {
                System.out.println("Valor atual da mesa: " +ConsoleColors.GREEN + "R$ " + ConsoleColors.YELLOW + vl_Total + ConsoleColors.RESET);

                if (firstp) {
                    LinhaPontilhada();
                    System.out.print(ConsoleColors.GREEN + "Próximo a jogar é o  " + ConsoleColors.RESET);
                } else {
                    LinhaPontilhada();
                    System.out.print(ConsoleColors.BLUE + "\nO jogador que começará é o " + ConsoleColors.RESET);
                }
                cartasJogador(l);
                escolha(l);
            }
            firstp = true;
            l += 1;
            if (l > (jogadores.size() - 1)){
                l = 0;
            }
        }
        while (l != pRaiserF && l >= 0 && l <= (jogadores.size() - 1));
    }


    private void showDown() {
        int maior = 0,igual = 0, pos [] = new int [7];
        System.out.println(ConsoleColors.GREEN + "\nRodada ShowDown irá começar!" + ConsoleColors.RESET);
        for(int i = 0; i < jogadores.size(); i++){
            if (dentrodojogo(i)){
                resultadoCombinacoes(i);
            }
        }
        for(int i = 0; i <= (jogadores.size() -1); i++){
            if (dentrodojogo(i)){
                if (jogadores.get(i).getPontos() > maior ){
                    maior = jogadores.get(i).getPontos();
                    pos[0] = i;
                    igual = 0;
                }else if(jogadores.get(i).getPontos() == maior ){
                    igual += 1;
                    pos[igual] = i;
                }
            }
        }
        if(igual > 0){
            for(int i = 0; i <= igual ; i++){
                if(jogadores.get(pos[i]).getMao().get(0).getValor().getValorCarta() >= jogadores.get(pos[i]).getMao().get(1).getValor().getValorCarta()){
                    jogadores.get(pos[i]).setPontos(jogadores.get(pos[i]).getPontos() + jogadores.get(pos[i]).getMao().get(0).getValor().getValorCarta());
                }else{
                    jogadores.get(pos[i]).setPontos(jogadores.get(pos[i]).getPontos() + jogadores.get(pos[i]).getMao().get(1).getValor().getValorCarta());
                }
            }
            for(int i = 0; i <= (jogadores.size() -1); i++){
                if (dentrodojogo(i)){
                    if (jogadores.get(i).getPontos() > maior ){
                        maior = jogadores.get(i).getPontos();
                        pos[0] = i;
                        igual = 0;
                    }else if(jogadores.get(i).getPontos() == maior ){
                        igual += 1;
                        pos[igual] = i;
                    }
                }
            }
            if(igual > 0){
                for(int i = 0; i <= igual ; i++){
                    if(jogadores.get(pos[i]).getMao().get(0).getValor().getValorCarta() <= jogadores.get(pos[i]).getMao().get(1).getValor().getValorCarta()){
                        jogadores.get(pos[i]).setPontos(jogadores.get(pos[i]).getPontos() + jogadores.get(pos[i]).getMao().get(0).getValor().getValorCarta());
                    }else{
                        jogadores.get(pos[i]).setPontos(jogadores.get(pos[i]).getPontos() + jogadores.get(pos[i]).getMao().get(1).getValor().getValorCarta());
                    }
                }
                for(int i = 0; i <= (jogadores.size() -1); i++){
                    if (dentrodojogo(i)){
                        if (jogadores.get(i).getPontos() > maior ){
                            maior = jogadores.get(i).getPontos();
                            pos[0] = i;
                            igual = 0;
                        }else if(jogadores.get(i).getPontos() == maior ){
                            igual += 1;
                            pos[igual] = i;
                        }
                    }
                }
                if(igual > 0){
                    System.out.println("Empate, o prêmio sera divido entre os jogadores: ");
                    for(int i = 0; i <= igual ; i++){
                        System.out.println(""+jogadores.get(pos[i]).getNome());
                    }
                    System.out.println("Dividiram a bolada de: " + ConsoleColors.GREEN + "R$ " +ConsoleColors.YELLOW + vl_Total + ConsoleColors.RESET);
                }else{
                    System.out.println("O vencedor é o jogador: " + jogadores.get(pos[0]).getNome());
                    System.out.println("Levou a bolada de: " + ConsoleColors.GREEN + "R$ " +ConsoleColors.YELLOW + vl_Total + ConsoleColors.RESET);
                }
            }else{
                System.out.println("O vencedor é o jogador: " + jogadores.get(pos[0]).getNome());
                System.out.println("Levou a bolada de: " + ConsoleColors.GREEN + "R$ " +ConsoleColors.YELLOW + vl_Total + ConsoleColors.RESET);
            }
        }else{
            System.out.println("O vencedor é o jogador: " + jogadores.get(pos[0]).getNome());
            System.out.println("Levou a bolada de: " + ConsoleColors.GREEN + "R$ " +ConsoleColors.YELLOW + vl_Total + ConsoleColors.RESET);
        }
    }

    private boolean dentrodojogo(int j){
        if (verificaJogadores() != 9){
            return false;
        }
        //verifica se o jogador não escolheu "correr"
        if (quiters[j] == j){
            return false;
        }
        return true;
    }


    private int verificaJogadores(){
        int countJ = 0 ,qt,verifica = 0, gravaVencedor = 9;
        for (int i = 0; i <= (quiters.length - 1) ; i++) {
            if (quiters[i] != 9 ){
                countJ++;
            }
        }
        if (countJ == (quiters.length - 1) ){
            for (qt = 0; qt <= (quiters.length - 1); qt++) {
                if (quiters[qt] == 9) {
                    ganhador = qt;
                    return ganhador;
                }
            }
        }
        return 9;
    }

    private void cartasMesa() {
        Mesa.add(baralho.daCarta());
    }

    private void mostraMesa() {
        String cor2;
        System.out.println("As cartas da mesa são: ");
        for (Carta c : Mesa) {
            if (Naipe.OURO == c.getNaipe() ) {
                cor2 = ConsoleColors.YELLOW;
            }else if(Naipe.COPAS == c.getNaipe()){
                cor2 = ConsoleColors.RED;
            }else if(Naipe.ESPADA == c.getNaipe()){
                cor2 = ConsoleColors.BLUE;
            }else{
                cor2 = ConsoleColors.CYAN;
            }
            System.out.println(  c.getValor() + " de " + cor2 + c.getNaipe() + ConsoleColors.RESET+ "");
        }
    }

    private void cartasJogador(int l) {
        String cor, cor1;
        if (Naipe.OURO == jogadores.get(l).getMao().get(0).getNaipe()) {
            cor = ConsoleColors.YELLOW;
        }else if(Naipe.COPAS == jogadores.get(l).getMao().get(0).getNaipe()){
            cor = ConsoleColors.RED;
        }else if(Naipe.ESPADA == jogadores.get(l).getMao().get(0).getNaipe()){
            cor = ConsoleColors.BLUE;
        }else{
            cor = ConsoleColors.CYAN;
        }
        if (Naipe.OURO == jogadores.get(l).getMao().get(1).getNaipe()) {
            cor1 = ConsoleColors.YELLOW;
        }else if(Naipe.COPAS == jogadores.get(l).getMao().get(1).getNaipe()){
            cor1 = ConsoleColors.RED;
        }else if(Naipe.ESPADA == jogadores.get(l).getMao().get(1).getNaipe()){
            cor1 = ConsoleColors.BLUE;
        }else{
            cor1 = ConsoleColors.CYAN;
        }
        System.out.println("jogador: " + jogadores.get(l).getNome());
        System.out.println("Cartas: " + jogadores.get(l).getMao().get(0).getValor() + " de " + cor + "" + jogadores.get(l).getMao().get(0).getNaipe() + ConsoleColors.RESET + " e " + jogadores.get(l).getMao().get(1).getValor() + " de " + cor1 + "" + jogadores.get(l).getMao().get(1).getNaipe() +ConsoleColors.RESET + "");
    }

    private void escolha(int j) {
        System.out.println(ConsoleColors.RESET+"\n\nEscolha a ação desejada: ");
        int a;
        do {
            if(pedirMesa){
                System.out.println(ConsoleColors.PURPLE + "Mesa     ---> 1");
            }else{
                System.out.println(ConsoleColors.BLUE + "Cobrir   ---> 1 ["+ConsoleColors.RESET+" Aposta atual: "+ConsoleColors.GREEN + "R$ " + ConsoleColors.YELLOW + cobrir + ConsoleColors.BLUE + " ]");
            }
            System.out.println(ConsoleColors.YELLOW + "Aumentar ---> 2");
            System.out.println(ConsoleColors.RED + "Correr   ---> 3");
            System.out.println(ConsoleColors.WHITE + "Ação desejada: ");
            a = isInt();
            if (a > 3 || a < 0) {
                System.out.println(ConsoleColors.WHITE + " Escolha uma opção válida !");
            }
        } while (a > 3 || a < 0);
        if (a == 1) {
            if (pedirMesa){
                System.out.println(ConsoleColors.RESET + "Opção escolhida: [" + ConsoleColors.PURPLE + "MESA" + ConsoleColors.RESET + "]");
            }else{
                System.out.println(ConsoleColors.RESET + "Opção escolhida: [" + ConsoleColors.BLUE + "COBRIR" + ConsoleColors.RESET + "]");
                vl_Total += cobrir;
            }
            return;
        } else if (a == 2) {
            do {
                System.out.println(ConsoleColors.RESET + "Opção escolhida: [" + ConsoleColors.YELLOW + "Aumentar" + ConsoleColors.RESET + "]");
                System.out.println(ConsoleColors.RESET + "\nDigite o valor que deseja (Acima de "+ ConsoleColors.GREEN +"R$ "+ ConsoleColors.YELLOW + cobrir+ ConsoleColors.RESET+"): ");
                aposta = isInt();
                if (aposta <= cobrir) {
                    System.out.println(ConsoleColors.RED + "\nValor abaixo ou igual ao atual da aposta.");
                }
            } while (aposta <= cobrir);
            vl_Total += aposta;
            cobrir = aposta;
            pRaiser = j;
            pRaiserF = j;
            if (flopmais){
                pedirMesa = false;
                flopmais = false;
            }
            return;
        } else if (a == 3) {
            System.out.println(ConsoleColors.RESET + "Opção escolhida: [" + ConsoleColors.RED + "CORRER" + ConsoleColors.RESET + "]");
            System.out.println(ConsoleColors.RED + "Jogador: " + jogadores.get(j).getNome() + " está fora do jogo." + ConsoleColors.WHITE);
            quiters[j] = j;
            return;
        }
    }

    private int isInt(){
        do {
            try {
                return Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                System.out.println( "\nErro ["+ConsoleColors.RED + e + ConsoleColors.RESET+"]: Digite apenas números inteiros!\n");
            }
        } while(true);
    }

    private void resultadoCombinacoes(int j){
        String corShowDown;
        int countNaipe[] = new int[4];
        String tipoNaipe[] = {"OURO", "COPAS", "ESPADA", "PAUS"};
        int seq = 0, k = 0, cartlast = 0;
        CartasCompara cartaComparator = new  CartasCompara();
        List<Carta> Sequencia = new ArrayList<>();
        int naipes = 0;
        for (int i = 0; i < 2 ; i++) {
            Carta cartaJogador = new Carta();
            cartaJogador.setValor(jogadores.get(j).getMao().get(i).getValor());
            cartaJogador.setNaipe(jogadores.get(j).getMao().get(i).getNaipe());
            cartaJogador.setValorCarta(jogadores.get(j).getMao().get(i).getValorCarta());
            Sequencia.add(cartaJogador);
        }
        for (int i = 0; i < 5; i++) {
            Sequencia.add(Mesa.get(i));
        }
        Collections.sort(Sequencia, cartaComparator);
        System.out.println("Checando sequencia do Jogador: " + jogadores.get(j).getNome());
        for (int i = 0; i < 4 ; i++) {
            countNaipe[i] = 0;
        }
        for (Carta c : Sequencia) {
            if (Naipe.OURO == c.getNaipe() ) {
                corShowDown = ConsoleColors.YELLOW;
                countNaipe[0] += 1;
            }else if(Naipe.COPAS == c.getNaipe()){
                corShowDown = ConsoleColors.RED;
                countNaipe[1] += 1;
            }else if(Naipe.ESPADA == c.getNaipe()){
                corShowDown = ConsoleColors.BLUE;
                countNaipe[2] += 1;
            }else{
                corShowDown = ConsoleColors.CYAN;
                countNaipe[3] += 1;
            }
            System.out.println(c.getValor() + " de " + corShowDown + c.getNaipe() + ConsoleColors.RESET + "");
        }
        //Check FLUSHs
        for (int n = 0; n < 4; n++){
            // --> Se entrar no if tem no minimo flush!!!
            if (countNaipe[n] >= 5) {
                // Checando se possui Royal Straight Flush
                for (int h = 0; h < 3; h++){
                    // Checando se umas das 3 primeiras cartas é um (Dez) do naipe com maior "pontuação"
                    if (Sequencia.get(h).getValor().getValorCarta() == 9 && (String.valueOf(Sequencia.get(h).getNaipe()) == tipoNaipe[n])) {
                        //Checar se sequencia há uma sequencia
                        seq = 1;
                        k = h + 1;
                        int nc = 10;
                        // l --> L
                        for (int l = k; l < 7 ; l++){
                            if (Sequencia.get(l).getValor().getValorCarta() == nc && (String.valueOf(Sequencia.get(l).getNaipe()) == tipoNaipe[n])) {
                                nc += 1;
                                seq += 1;
                            }else if(Sequencia.get(l).getValor().getValorCarta() == (nc-1)) {
                                continue;
                            }else{
                                break;
                            }
                        }
                        if (seq == 5 ){
                            System.out.println("Jogador '" + jogadores.get(j).getNome() + "' Conseguiu um"+ConsoleColors.RED+" ROYAL Straight FLUSH"+ConsoleColors.RESET +"\n");
                            jogadores.get(j).setPontos(10000);
                            LinhaPontilhada();
                            return;
                        }
                    }
                }
                // Straight Flush
                for (int h = 6; h > 3; h--){
                    if (Sequencia.get(h).getValor().getValorCarta() > 5 && Sequencia.get(h).getValor().getValorCarta() < 13 &&  (String.valueOf(Sequencia.get(h).getNaipe()) == tipoNaipe[n])) {
                        int vlLastC = Sequencia.get(h).getValor().getValorCarta() -1;
                        cartlast = Sequencia.get(h).getValor().getValorCarta();
                        seq = 1;
                        k = h - 1;
                        for (int l = k; l >= 0 ; l--){
                            if (Sequencia.get(l).getValor().getValorCarta() == vlLastC && (String.valueOf(Sequencia.get(l).getNaipe()) == tipoNaipe[n])) {
                                vlLastC -= 1;
                                seq += 1;
                            }else if(Sequencia.get(l).getValor().getValorCarta() == (vlLastC + 1)) {
                                continue;
                            }else{
                                //FLUSH
                                System.out.println("Jogador '" + jogadores.get(j).getNome() + "' Conseguiu um"+ConsoleColors.YELLOW+" FLUSH"+ConsoleColors.RESET +"\n");
                                jogadores.get(j).setPontos(6000);
                                LinhaPontilhada();
                                return;
                            }
                        }
                    }
                    if (seq == 5 ){
                        System.out.println("Jogador '" + jogadores.get(j).getNome() + "' Conseguiu um"+ConsoleColors.GREEN+" Straight FLUSH"+ConsoleColors.RESET +"\n");
                        jogadores.get(j).setPontos(9000 + cartlast);
                        LinhaPontilhada();
                        return;
                    }
                }
            }
        }
        seq = 1;
        k = 1;
        // Quadra
        for (k = 6; k > 0; k--) {
            //Checar se possui 4 cartas de valores iguais...
            if ((Sequencia.get(k - 1).getValor().getValorCarta()) == (Sequencia.get(k).getValor().getValorCarta())) {
                cartlast = Sequencia.get(k).getValor().getValorCarta();
                seq +=1;
                if (seq == 4) {
                    System.out.println("Jogador '" + jogadores.get(j).getNome() + "' Conseguiu uma"+ConsoleColors.BLUE+" QUADRA"+ConsoleColors.RESET +"\n");
                    jogadores.get(j).setPontos(8000 + cartlast);
                    LinhaPontilhada();
                    return;
                }
            } else {
                seq = 1;
            }
        }
        // Verificar Full House (Uma trinca mais um par)
        boolean par = false;
        boolean trinca = false;
        int tempValor = 0;
        k = 6;
        seq = 1;
        for (k = 6; k > 0; k--) {
            //Checar se possui 3 cartas de valores iguais...
            if ((Sequencia.get(k - 1).getValor().getValorCarta()) == (Sequencia.get(k).getValor().getValorCarta())) {
                tempValor = Sequencia.get(k).getValor().getValorCarta();
                seq += 1;
                if (seq == 3) {
                    trinca = true;
                    seq = 1;
                    cartlast = tempValor;
                    break;
                }
            } else {
                seq = 1;
                tempValor = 0;
            }
        }
        // Se ja posuir Trinca... Checa se tem um par (tem tratamento para checar, se ele não esta se "enganando com mesmo valor da Trinca")
        k = 6;
        if (trinca == true) {
            for (k = 6; k > 0; k--) {
                //Checar se possui 2 cartas de valores iguais... Sem contar com o valor da Trinca! Para não gerar um falso positivo!
                if (((Sequencia.get(k - 1).getValor().getValorCarta()) == (Sequencia.get(k).getValor().getValorCarta())) && (Sequencia.get(k).getValor().getValorCarta() != tempValor)) {
                    seq += 1;
                    if (seq == 2) {
                        par = true;
                        seq = 1;
                        cartlast += Sequencia.get(k).getValor().getValorCarta();
                        break;
                    }
                } else {
                    seq = 1;
                }
            }
        }
        if (par == true){
            System.out.println("Jogador '" + jogadores.get(j).getNome() + "' Conseguiu um"+ConsoleColors.PURPLE+" FULL HOUSE"+ConsoleColors.RESET +"\n");
            jogadores.get(j).setPontos(7000 + cartlast);
            LinhaPontilhada();
            trinca= false;
            par = false;
            return;
        }
        // Verificar Sequencia idependente do naipe!
        seq = 1;
        for (int h = 6; h > 3; h--){
            if (Sequencia.get(h).getValor().getValorCarta() > 5 && Sequencia.get(h).getValor().getValorCarta() < 13) {
                int vlLastC = Sequencia.get(h).getValor().getValorCarta() -1;
                cartlast = Sequencia.get(h).getValor().getValorCarta();
                seq = 1;
                k = h - 1;
                for (int l = k; l >= 0 ; l--){
                    if (Sequencia.get(l).getValor().getValorCarta() == vlLastC) {
                        vlLastC -= 1;
                        seq += 1;
                    }else if(Sequencia.get(l).getValor().getValorCarta() == (vlLastC + 1)) {
                        continue;
                    }else{
                        break;
                    }
                }
            }
            if (seq == 5){
                System.out.println("Jogador '" + jogadores.get(j).getNome() + "' Conseguiu um"+ConsoleColors.RED+" Straight (Sequencia)"+ConsoleColors.RESET +"\n");
                jogadores.get(j).setPontos(5000 + cartlast);
                LinhaPontilhada();
                return;
            }
        }
        // Verificar Trinca
        seq = 1;
        k = 6;
        for (k = 6; k > 0; k--) {
            //Checar se possui 3 cartas de valores iguais...
            if ((Sequencia.get(k - 1).getValor().getValorCarta()) == (Sequencia.get(k).getValor().getValorCarta())) {
                tempValor = Sequencia.get(k).getValor().getValorCarta();
                seq += 1;
                if (seq == 3) {
                    cartlast = tempValor;
                    System.out.println("Jogador '" + jogadores.get(j).getNome() + "' Conseguiu uma"+ConsoleColors.BLUE+" TRINCA"+ConsoleColors.RESET +"\n");
                    jogadores.get(j).setPontos(4000 + cartlast);
                    LinhaPontilhada();
                    return;
                }
            } else {
                seq = 1;
            }
        }
        // Verificar Dois pares
        seq = 1;
        boolean par1 = false;
        boolean par2 = false;
        int cartlast2 = 0;
        tempValor = 0;
        k = 6;
        for (k = 6; k > 0; k--) {
            //Checar se possui 2 cartas de valores iguais...
            if ((Sequencia.get(k - 1).getValor().getValorCarta()) == (Sequencia.get(k).getValor().getValorCarta())) {
                tempValor = Sequencia.get(k).getValor().getValorCarta();
                seq += 1;
                if (seq == 2) {
                    par1 = true;
                    seq = 1;
                    cartlast = tempValor;
                    break;
                }
            } else {
                tempValor = 0;
                seq = 1;
            }
        }
        // Se ja posuir um par... Checa se tem um par (tem tratamento para checar, se ele não esta se "enganando com mesmo valor do primeiro par")
        seq = 1;
        if (par1 == true) {
            k = 6;
            for (k = 6; k > 0; k--) {
                //Checar se possui 2 cartas de valores iguais... Sem contar com o valor do primeiro par! Para não gerar um falso positivo!
                if (((Sequencia.get(k - 1).getValor().getValorCarta()) == (Sequencia.get(k).getValor().getValorCarta())) && (Sequencia.get(k).getValor().getValorCarta() != tempValor)) {
                    seq += 1;
                    if (seq == 2) {
                        par2 = true;
                        seq = 1;
                        cartlast2 += Sequencia.get(k).getValor().getValorCarta();
                        break;
                    }
                } else {
                    seq = 1;
                }
            }
        }
        if (par1 == true && par2 == true){
            System.out.println("Jogador '" + jogadores.get(j).getNome() + "' Conseguiu "+ConsoleColors.GREEN+"DOIS PARES"+ConsoleColors.RESET +"\n");
            jogadores.get(j).setPontos(3000 + cartlast + cartlast2);
            par1 = false;
            par2 = false;
            LinhaPontilhada();
            return;
        }
        // Aproveitando o par da verificação anterior
        if (par1 == true){
            System.out.println("Jogador '" + jogadores.get(j).getNome() + "' Conseguiu "+ConsoleColors.PURPLE+"UM PAR"+ConsoleColors.RESET +"\n");
            jogadores.get(j).setPontos(2000 + cartlast);
            LinhaPontilhada();
            return;
        }
        // Verificando qual maior carta da mao do jogador[j]
        if (jogadores.get(j).getMao().get(0).getValor().getValorCarta() >= jogadores.get(j).getMao().get(1).getValor().getValorCarta()){
            jogadores.get(j).setPontos(jogadores.get(j).getMao().get(0).getValor().getValorCarta());
            System.out.println("Jogador '" + jogadores.get(j).getNome() + "' com sua maior carta " + jogadores.get(j).getMao().get(0).getValor()  + "\n");
        }else{
            jogadores.get(j).setPontos(jogadores.get(j).getMao().get(1).getValor().getValorCarta());
            System.out.println("Jogador '" + jogadores.get(j).getNome() + "' com sua maior carta " + jogadores.get(j).getMao().get(1).getValor()  + "\n");
        }
        LinhaPontilhada();
        return ;
    }

    public class CartasCompara implements Comparator<Carta> {
        @Override
        public int compare(Carta firstPlayer, Carta secondPlayer) {
            return Integer.compare(firstPlayer.getValorCarta(), secondPlayer.getValorCarta());
        }
    }
    public class CartaValorCompara implements Comparator<Carta> {
        @Override
        public int compare(Carta firstPlayer, Carta secondPlayer) {
            return Integer.compare(firstPlayer.getValorCarta(), secondPlayer.getValorCarta());
        }
    }

    public void LinhaPontilhada(){
        System.out.println(ConsoleColors.RED +"---------------------------------------------------------------------------" + ConsoleColors.RESET);
    }
}