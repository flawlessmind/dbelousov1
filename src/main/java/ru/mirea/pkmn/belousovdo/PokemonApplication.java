package ru.mirea.pkmn.belousovdo;


import com.fasterxml.jackson.databind.JsonNode;
import ru.mirea.pkmn.Card;
import ru.mirea.pkmn.belousovdo.web.http.PkmnHttpClient;

import java.util.stream.Collectors;

public class PokemonApplication {
    static String filename = "src\\main\\resources\\my_card.txt";
    public static void main(String[] args) throws Exception {

        CardImport cardimport = new CardImport();
        Card card = new Card();
        card = cardimport.Fill(filename);
        CardExport.Export(card);
        System.out.println(card.toString());

        PkmnHttpClient pkmnHttpClient = new PkmnHttpClient();

        JsonNode card1 = pkmnHttpClient.getPokemonCard("Mewtwo", "150");
        System.out.println(card1.toPrettyString());
        System.out.println(card1.findValues("attacks")
                .stream()
                .map(JsonNode::toPrettyString)
                .collect(Collectors.toSet()));
        Card cardik = CardImport.SImport("src\\main\\resources\\Mewtwo.crd");
        System.out.printf(cardik.toString());
    }
}
