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

        // Получаем данные о покемоне, который эволюционирует
        if (!reportData[4].equals("-")) {
            cardFrom = new CardImport().Fill(reportData[4]);
        }

        List<AttackSkill> skills = new ArrayList<>();

        // Предполагается, что данные атак находятся в JSON-ответе, который мы получаем из API
        PkmnHttpClient pkmnHttpClient = new PkmnHttpClient();
        JsonNode jsonResponse = pkmnHttpClient.getPokemonCard(reportData[1], reportData[12]); // Используем имя покемона и его номер

        // Извлекаем информацию об атаках
        JsonNode attacksNode = jsonResponse.path("data").get(0).path("attacks");

        for (JsonNode attackNode : attacksNode) {
            String name = attackNode.path("name").asText();

            // Получаем массив значений стоимости и конвертируем их в строку
            List<String> costs = new ArrayList<>();
            for (JsonNode costNode : attackNode.path("cost")) {
                costs.add(costNode.asText());
            }
            String cost = String.join(", ", costs);

            String text = attackNode.path("text").asText();

            // Предполагаем, что damage - это строка и может содержать символы (например, "20×")
            String damageString = attackNode.path("damage").asText();
            int damage = damageString.contains("×") ? Integer.parseInt(damageString.replace("×", "").trim()) : Integer.parseInt(damageString);

            AttackSkill attackSkill = new AttackSkill(name, text, cost, damage);
            skills.add(attackSkill);
        }

        // Извлечение информации о студенте
        String[] studentData = reportData[11].split("/");
        Student student = new Student(studentData[1], studentData[0], studentData[2], studentData[3]);

        // Извлечение информации о слабостях и сопротивлениях
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
