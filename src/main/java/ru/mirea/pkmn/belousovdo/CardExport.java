package ru.mirea.pkmn.belousovdo;

import ru.mirea.pkmn.Card;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class CardExport {
    public static void Export(Card cardToExport) throws IOException {
        FileOutputStream outputStream = new FileOutputStream("src\\main\\resources\\" + cardToExport.getName() + ".crd");
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        objectOutputStream.writeObject(cardToExport);
        objectOutputStream.close();
    }
}

