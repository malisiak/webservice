package serviceWebsite.enums;

/**
 * Definicja własnego typu danych, określających nazwy
 * aktywności wykonywanych przez użytkownika.
 */
public enum ActivityTypeEnum {

    /**
     * Aktywność: Na nogach, użytkownik może iść lub biec.
     */
    OnFoot,

    /**
     * Aktywność: W pojezdzie.
     */
    InVehicle,

    /**
     * Aktywność: Chodzenie.
     */
    Walking,

    /**
     * Aktywność: Bieganie.
     */
    Running,

    /**
     * Aktywność: Bezruch.
     */
    Still,

    /**
     * Aktywność: Nieznana.
     */
    Unknown,

    /**
     * Aktywność: Wstrząsy urządzenia.
     */
    Tilting
}
