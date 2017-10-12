package ru.davidlevy.gspaceshooter.game;

/**
 * Перечисление TypePowerUp (тип вознаграждения)
 */
public enum TypePowerUp {
    MONEY10(0), MONEY25(1), MONEY50(2), MEDKIT(3), SHIELD(4);

    private int number;

    public int getNumber() {
        return number;
    }

    TypePowerUp(int number) {
        this.number = number;
    }
}
