package ru.mirea.pkmn.belousovdo;

import com.fasterxml.jackson.databind.JsonNode;
import ru.mirea.pkmn.*;
import ru.mirea.pkmn.belousovdo.web.http.PkmnHttpClient;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class CardImport {
    private String[] reportData;

    public CardImport() {}

    private void Import(String filename) throws IOException {
        FileInputStream fileInputStream = new FileInputStream(filename);
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
        byte[] data = bufferedInputStream.readAllBytes();
        reportData = new String(data).split("\r\n");
        bufferedInputStream.close();
        fileInputStream.close();
    }

    public Card Fill(String filename) throws IOException {
        Import(filename);
        Card cardFrom = null;

        if (!reportData[4].equals("-")) {
            cardFrom = new CardImport().Fill(reportData[4]);
        }

        List<AttackSkill> skills = new ArrayList<>();

        PkmnHttpClient pkmnHttpClient = new PkmnHttpClient();
        JsonNode jsonResponse = pkmnHttpClient.getPokemonCard(reportData[1], reportData[12]); // Используем имя покемона и его номер

        JsonNode attacksNode = jsonResponse.path("data").get(0).path("attacks");

        for (JsonNode attackNode : attacksNode) {
            String name = attackNode.path("name").asText();


            List<String> costs = new ArrayList<>();
            for (JsonNode costNode : attackNode.path("cost")) {
                costs.add(costNode.asText());
            }
            String cost = String.join(", ", costs);

            String text = attackNode.path("text").asText();

            String damageString = attackNode.path("damage").asText();
            int damage = damageString.contains("×") ? Integer.parseInt(damageString.replace("×", "").trim()) : Integer.parseInt(damageString);

            AttackSkill attackSkill = new AttackSkill(name, text, cost, damage);
            skills.add(attackSkill);
        }

        String[] studentData = reportData[11].split("/");
        Student student = new Student(studentData[1], studentData[0], studentData[2], studentData[3]);

        EnergyType weakness = parseEnergyType(reportData[6]);
        EnergyType resistance = parseEnergyType(reportData[7]);

        Card card = new Card(
                PokemonStage.valueOf(reportData[0].toUpperCase()),
                reportData[1],
                Integer.parseInt(reportData[2]),
                EnergyType.valueOf(reportData[3].toUpperCase()),
                cardFrom,
                skills,
                weakness,
                resistance,
                reportData[8],
                reportData[9],
                reportData[10].charAt(0),
                student,
                reportData[12]
        );

        return card;
    }

    public static Card SImport(String filename) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(filename);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        return (Card) objectInputStream.readObject();
    }

    // Метод для парсинга EnergyType
    private EnergyType parseEnergyType(String energyTypeString) {
        try {
            return EnergyType.valueOf(energyTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // Метод для получения информации о навыках с описанием (можно использовать для тестирования)
    private void SkillsWithDescription() throws IOException {
        PkmnHttpClient pkmnHttpClient = new PkmnHttpClient();
        JsonNode card1 = pkmnHttpClient.getPokemonCard("Blissey", "203");
        System.out.println(card1.toPrettyString());
    }
}
