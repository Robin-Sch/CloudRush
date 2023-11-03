package components;

import java.util.prefs.Preferences;

/**
 * Class to get/set user's language.
 */
public class Language {
    public static int code = 0;

    /**
     * Load the user's language in memory.
     */
    public static void loadLanguage() {
        Preferences preferences = Preferences.userRoot().node("/"); // Java preferences
        Language.code = Integer.parseInt(preferences.get("language", "0")); // Get language code
    }

    /**
     * Change the user's language.
     * @param code the code of the language (0 = english, 1 = dutch, 2 = polish)
     */
    public static void setLanguage(int code) {
        Language.code = code;
        Preferences preferences = Preferences.userRoot().node("/"); // Java preferences
        preferences.put("language", String.valueOf(code)); // Set language code
    }

    /**
     * Get text for the english language.
     * Split up per language switch case to make checkstyle happy (limiting the line length),
     * while still keeping the code readable.
     * @param identifier the text
     * @return The text in English
     */
    private static String getEnglishString(String identifier) {
        String result = "";

        switch (identifier) {
            case "eindhoven" -> result = "Eindhoven";
            case "easy" -> result = "easy";
            case "warsaw" -> result = "Warsaw";
            case "medium" -> result = "medium";
            case "newyork" -> result = "New York";
            case "hard" -> result = "hard";
            case "instructions" -> result = "Instructions";
            case "settings" -> result = "Settings";
            case "exit" -> result = "Exit";
            case "gameOver" -> result = "Game over";
            case "language" -> result = "Language";
            case "save" -> result = "Save";
            case "close" -> result = "Close";
            case "score" -> result = "Score";
            case "submit" -> result = "Submit";
            case "nickname" -> result = "Nickname";
            case "points" -> result = "Points";
            case "time" -> result = "Time";
            case "positiveCloudSize" -> result = "Feeling stronger!\n1.5x bigger cloud";
            case "positiveRainIntensity" -> result = "Flood generator\n3x more raindrops";
            case "positiveMovementSpeed" -> result = "Lightning fast\n200% movement speed";
            case "positiveWindStrength" -> result = "Nice weather\n2x weaker wind";
            case "positiveRainQuality" -> result = "Bigger drops\n2x more water in rain";
            case "positiveFlowersCount" -> result = "Flower meadow\n2x more flowers";
            case "positivePeopleCount" -> result = "Depopulation\n2x less people";
            case "positiveLanternsCount" -> result = "Well lit\n2x more lanterns";
            case "positivePositivePoints" -> result = "Positive bonus\n2x more positive points";
            case "positiveNegativePoints" -> result = "Negative debuff\n2x less negative points";
            case "positiveLevelSpeed" -> result = "Slow motion\n2x slower level";
            case "positiveFlowerSize" -> result = "Extra growth\n2x bigger flowerpots";
            case "positivePeopleSpeed" -> result = "No stress\n2x slower people";
            case "positiveEffectsCount" -> result = "Extra packages\n2x more effects";
            case "negativeCloudSize" -> result = "Feeling weaker!\n50% of cloud size";
            case "negativeRainIntensity" -> result = "Drought\n2x less raindrops";
            case "negativeMovementSpeed" -> result = "Snail slow\n50% movement speed";
            case "negativeWindStrength" -> result = "Tornado warning\n2x stronger wind";
            case "negativeRainQuality" -> result = "Acid rain!\nRaindrops kill flowers";
            case "negativeFlowersCount" -> result = "Infertile area\n2x less flowers";
            case "negativePeopleCount" -> result = "Rush hour\n2x more people";
            case "negativeLanternsCount" -> result = "Dark district\n2x less lanterns";
            case "negativePositivePoints" -> result = "Positive debuff\n2x less positive points";
            case "negativeNegativePoints" -> result = "Negative bonus\n2x more negative points";
            case "negativeLevelSpeed" -> result = "Fast forward\n2x faster level";
            case "negativeFlowerSize" -> result = "Bad growth\n2x smaller flowerpots";
            case "negativePeopleSpeed" -> result = "In rush\n2x faster people";
            case "negativeEffectsCount" -> result = "Bonus debuff\n2x less effects";
            case "instructionsDescription" -> result = """
                Welcome to CloudRush, an arcade styled 2-dimensional game. In this game, you are a
                cloud and you have to save the city, by watering the flowers and turning ond the
                street lanterns. Be carefully, since people don't like to get wet or electrocuted.
                Make sure to collect various bonuses from the sky, which might make
                your cloud life a lot easier, but also be aware that some might make it a bit
                more difficult.

                CONTROLS
                Use the wasd keys to move, use space to let it rain and use enter to create a
                lightning beam.

                OBJECTIVE
                Your goal is to collect as many points as possible. You get points for different
                actions, like watering the flowers and (deducted) points for making people wet.
                Actions have different weights (you don't get the same amount of points for 
                different actions), and are affected by the collected bonuses/debuffs. After
                the game ends, you can type in your nickname and admire your score on the
                leaderboard in the main menu.""";
            default -> result = "";
        }
        return result;
    }

    /**
     * Get text for the dutch language.
     * Split up per language switch case to make checkstyle happy (limiting the line length),
     * while still keeping the code readable.
     * @param identifier the text
     * @return The text in Dutch
     */
    private static String getDutchString(String identifier) {
        String result = "";

        switch (identifier) {
            case "eindhoven" -> result = "Eindhoven";
            case "easy" -> result = "Makkelijk";
            case "warsaw" -> result = "Warsaw";
            case "medium" -> result = "Gemiddeld";
            case "newyork" -> result = "New York";
            case "hard" -> result = "Moeilijk";
            case "instructions" -> result = "Instructies";
            case "settings" -> result = "Instellingen";
            case "exit" -> result = "Stoppen";
            case "gameOver" -> result = "Je hebt verloren";
            case "language" -> result = "Taal";
            case "save" -> result = "Opslaan";
            case "close" -> result = "Sluiten";
            case "score" -> result = "Score";
            case "submit" -> result = "Indienen";
            case "nickname" -> result = "Spelersnaam";
            case "points" -> result = "Punten";
            case "time" -> result = "Tijd";
            case "positiveCloudSize" -> result = "Je voelt je sterker!\n1.5x grotere wolk";
            case "positiveRainIntensity" -> result = "Vloed generator\n3x meer regendruppels";
            case "positiveMovementSpeed" -> result = "Bliksem snel\n200% bewegings snelheid";
            case "positiveWindStrength" -> result = "Good weer\n2x zwakkere wind";
            case "positiveRainQuality" -> result = "Grote druppels\n2x meer water per druppel";
            case "positiveFlowersCount" -> result = "Bloemenweide\n2x meer bloemen";
            case "positivePeopleCount" -> result = "Minder bevolking\n2x minder mensen";
            case "positiveLanternsCount" -> result = "Goed verlicht\n2x meer lantaarnpalen";
            case "positivePositivePoints" -> result = "Positieve bonus\n2x meer positieve punten";
            case "positiveNegativePoints" -> result = "Negatieve debuf\n2x minder negative punten";
            case "positiveLevelSpeed" -> result = "Langzame beweging\n2x slomere level";
            case "positiveFlowerSize" -> result = "Extra groeite\n2x grotere bloempotten";
            case "positivePeopleSpeed" -> result = "Geen stress\n2x langzamere mensen";
            case "positiveEffectsCount" -> result = "Extra pakketten\n2x meer effecten";
            case "negativeCloudSize" -> result = "Je voelt je zwakker!\n50% kleinere wolk";
            case "negativeRainIntensity" -> result = "Droogte\n2x minder regendruppels";
            case "negativeMovementSpeed" -> result = "Zo langzaam als een slak\n50% snelheid";
            case "negativeWindStrength" -> result = "Tornado waarschuwwing\n2x sterkere wind";
            case "negativeRainQuality" -> result = "Giftige regen!\nRegendruppels doden bloemen";
            case "negativeFlowersCount" -> result = "Onvruchtbaar gebied\n2x minder bloemen";
            case "negativePeopleCount" -> result = "Spitsuur\n2x meer mensen";
            case "negativeLanternsCount" -> result = "Duistere buurt\n2x minder lantaarnpalen";
            case "negativePositivePoints" -> result = "Positieve debuff\n2x minder punten";
            case "negativeNegativePoints" -> result = "Negatieve bonus\n2x meer negatieve punten";
            case "negativeLevelSpeed" -> result = "Speedrun\n2x sneller level";
            case "negativeFlowerSize" -> result = "Slechte oogst\n2x kleinere bloempotten";
            case "negativePeopleSpeed" -> result = "Haastige spoed\n2x snellere mensen";
            case "negativeEffectsCount" -> result = "Bonus debuff\n2x minder effecten";
            case "instructionsDescription" -> result = """
                Welkom bij CloudRush, een arcade gebaseerde 2-dimensionale game. In deze game ben
                jij een wolk en moet je de stad redden. Dit doe je door de planten water te geven
                en de straatlantaarnpalen aan te zetten. Pas op, mensen worden niet graag nat, of
                geelectrocuteerd. Vergeet niet om de verschillende bonussen uit de lucht te
                verzamelen, want deze kunnen jouw level als cloud een stuk helpen, maar sommige
                juist ook niet.
                
                BESTURING
                Gebruik de toetsen wasd om te bewegen, spatiebalk om te regenen en enter om bliksem
                te creeren.
                
                DOEL
                Jouw doel is om zo veel mogelijk punten te verzamelen. Je krijgt punten voor
                verschillende acties, zoals het water geven van planten en het aan zetten van
                lantaarnpalen. Je kan ook punten verliezen, zoals wanneer je mensen nat maakt of
                electrocuteerd. Aan het einde van de game, kan je je naam invullen zodat je voor
                altijd op het leaderboard staat.""";
            default -> result = "";
        }
        return result;
    }

    /**
     * Get text for the polish language.
     * Split up per language switch case to make checkstyle happy (limiting the line length),
     * while still keeping the code readable.
     * @param identifier the text
     * @return The text in Polish
     */
    private static String getPolishString(String identifier) {
        String result = "";

        switch (identifier) {
            case "eindhoven" -> result = "Eindhoven";
            case "easy" -> result = "łatwy";
            case "warsaw" -> result = "Warszawa";
            case "medium" -> result = "średni";
            case "newyork" -> result = "Nowy York";
            case "hard" -> result = "trudny";
            case "instructions" -> result = "Instrukcje";
            case "settings" -> result = "Ustawienia";
            case "exit" -> result = "Wyjście";
            case "gameOver" -> result = "Koniec gry";
            case "language" -> result = "Język";
            case "save" -> result = "Zapisz";
            case "close" -> result = "Zamknij";
            case "score" -> result = "Wynik";
            case "submit" -> result = "Zapisz wynik";
            case "nickname" -> result = "Nazwa gracza";
            case "points" -> result = "Punkty";
            case "time" -> result = "Czas";
            case "positiveCloudSize" -> result = "Uczucie wielkości!\n1.5x większa chmura";
            case "positiveRainIntensity" -> result = "Generator powodzi\n3x więcej kropel";
            case "positiveMovementSpeed" -> result = "Szybki jak błyskawica\n200% większa "
                                                    + "prędkość";
            case "positiveWindStrength" -> result = "Dobra pogoda\n2x słabszy wiatr";
            case "positiveRainQuality" -> result = "Większe krople\n2x więcej wody w deszczu";
            case "positiveFlowersCount" -> result = "Łąka kwiatowa\n2x więcej kwiatów";
            case "positivePeopleCount" -> result = "Wyludnienie\n2x mniej ludzi";
            case "positiveLanternsCount" -> result = "Dobre oświetlenie\n2x więcej latarni";
            case "positivePositivePoints" -> result = "Pozytywny bonus\n2x więcej dodatnich "
                                                    + "punktów";
            case "positiveNegativePoints" -> result = "Negatywny debuff\n2x mniej ujemnych "
                                                    + " punktów";
            case "positiveLevelSpeed" -> result = "Zwolnione tempo\n2x wolniejszy poziom";
            case "positiveFlowerSize" -> result = "Super wzrost\n2x większe kwiaty";
            case "positivePeopleSpeed" -> result = "Bez stresu\n2x wolniejsi ludzie";
            case "positiveEffectsCount" -> result = "Dodatkowe bonusy\n2x więcej bonusów";
            case "negativeCloudSize" -> result = "Uczucie słąbości!\n50% wielkości chmury";
            case "negativeRainIntensity" -> result = "Susza\n2x mniej kropel";
            case "negativeMovementSpeed" -> result = "Wolny jak ślimak\n50% prędkości";
            case "negativeWindStrength" -> result = "Tornado\n2x silniejszy wiatr";
            case "negativeRainQuality" -> result = "Kwaśny deszcz!\nKrople zabijają kwiaty";
            case "negativeFlowersCount" -> result = "Nieurodzajny teren\n2x mniej kwiatów";
            case "negativePeopleCount" -> result = "Godziny szczytu\n2x więcej ludzi";
            case "negativeLanternsCount" -> result = "Ciemna dzielnica\n2x mniej latarni";
            case "negativePositivePoints" -> result = "Pozytywny debuff\n2x mniej pozytywnych "
                                                        + "punktów";
            case "negativeNegativePoints" -> result = "Negatywny bonus\n2x więcej negatywnych "
                                                        + "punktów";
            case "negativeLevelSpeed" -> result = "Przyspieszone tempo\n2x szybszy poziom";
            case "negativeFlowerSize" -> result = "Słaby wzrost\n2x mniejsze kwiaty";
            case "negativePeopleSpeed" -> result = "W biegu\n2x szybsi ludzi";
            case "negativeEffectsCount" -> result = "Deficyt bonusów\n2x mniej bonusów";
            case "instructionsDescription" -> result = """
                Witamy w CloudRush, dwuwymiarowej grze w stylu arcade. W tej grze jesteś chmurą
                i musisz ocalić miasto, podlewając kwiaty i zapalając latarnie uliczne.
                Uważaj żeby nie zmoczyć ludzi, ani nie porazić ich prądem.
                Pamiętaj, aby zbierać różne bonusy z nieba, które mogą znacznie ułatwić Ci życie,
                ale pamiętaj też, że niektóre z nich mogą je nieco utrudnić.
                
                STEROWANIE
                Użyj klawiszy WASD do poruszania się, użyj spacji, aby spadł deszcz i naciśnij
                Enter, aby stworzyć błyskawicę.
                
                CEL
                Twoim celem jest zdobycie jak największej liczby punktów. Otrzymujesz punkty za
                różne czynności, takie jak podlewanie kwiatów, a tracisz je za np. za zmoczenie
                ludzi. Punkty mają różną wagę i wpływają na nie zebrane bonusy/debuffy. Po
                zakończeniu gry możesz wpisać swój pseudonim i podziwiać swój wynik na tablicy
                wyników w menu głównym.""";
            default -> result = "";
        }
        return result;
    }

    /**
     * Get text in the user's language.
     * Split up per language switch case to make checkstyle happy (limiting the line length),
     * while still keeping the code readable.
     * @param identifier the text
     * @return The text in correct language
     */
    public static String getString(String identifier) {
        if (code == 1) {
            return getDutchString(identifier);
        } else if (code == 2) {
            return getPolishString(identifier);
        } else {
            return getEnglishString(identifier);
        }
    }
}
