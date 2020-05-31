
import java.util.HashMap;
import java.util.Map;

public class Deduplication implements DeduplicationInterface{

    HashMap<Integer, String> dictionaryCiag = new HashMap<>();
    HashMap<String, Integer> dictionarySlowo = new HashMap<>();
    HashMap<String, Integer> dictionarySK = new HashMap<>();
    HashMap<Integer, String> dictionaryKS = new HashMap<>();
    int id = 1;



    private static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    public HashMap<String, Integer> getDictionarySlowo() {
        return dictionarySlowo;
    }

    /**
     * Metoda zwraca słownik używany przez system deduplikacji. Kluczem mapy jest
     * identyfikator liczbowy a wartością słowo, które jest zastępowane przez dany
     * klucz. Identyfikatory powinny być przydzielane od wartości 1. Kolejne słowa,
     * które zapisywane są w słowniku powinny uzyskiwać kolejne numery
     * identyfikacyjne.
     *
     * @return mapa słów używanych przez system deduplikacji.
     */
    @Override
    public Map<Integer, String> getDictionary() {
        return dictionaryKS;
    }


    /**
     * Metoda dokonuje odtworzenia oryginalnego ciągu na podstawie mapy-słownika
     * oraz ciągu do odkodowania. Dekodowaniu podlegają podciągi w postaci #numer.
     * Podciąg #numer należy zastąpić słowem znajdującym się w przekazanym słowniku
     * pod kluczem numer. Prawidłowy zapis sekwencji #numer to np. #1, #2, #3 czy
     * #13 a nie np. #001, #002, #003 czy #013.
     *
     * @param dictionary słownik użytych słów. Metoda nigdy nie zwraca null. Jeśli
     * w przekazanych ciągach brak słów, które można wpisać do słownika, metoda
     * zwraca pustą mapę.asdfasdf
     * @param toDecode   ciąg znaków do zdekodowania
     * @return Odkodowany ciąg znaków
     */
    @Override
    public String decode(Map<Integer, String> dictionary, String toDecode) {

       for (int liczba: dictionary.keySet()){
           toDecode = toDecode.replaceAll("#"+liczba, dictionary.get(liczba));
       }
       return toDecode;
    }


    /**
     * Metoda dodaje nowy ciąg znaków. Zostaje on zapisany i otrzymuje unikalny
     * numer. Numersdfa ten zostaje zwrócony użytkownikowi. Pierwszy ciąg powinien
     * uzyskać numer 1.
     *
     * @param newString dodawany ciąg znaków
     * @return unikalny numer, pod którym ciąg został zapisany.
     */
    @Override
    public int addString(String newString) {

        int stringNum = dictionaryCiag.size()+1;
        dictionaryCiag.put(stringNum, newString);

        String[] podzielone = newString.split("\\s+");
        for (String string: podzielone){
            if (string.length()<=1) continue;
            if (string.substring(string.length()-1, string.length()).equals(".") || string.substring(string.length()-1, string.length()).equals("!") || string.substring(string.length()-1, string.length()).equals(",")) {
                String help = removeLastChar(string);
                if(!help.chars().allMatch((Character::isLetter)))continue;
                if (help.length() >= 3){
                    if (dictionarySlowo.containsKey(help)){
                    dictionarySlowo.put(help, dictionarySlowo.get(help)+1);
                    if (dictionarySlowo.get(help) >= 3){
                        if (dictionaryKS.containsValue(help))continue;
                        dictionaryKS.put(id, help);
                        dictionarySK.put(help,id);
                        id++;

                    }
                    } else dictionarySlowo.put(help, 1);

                }
            }else if (string.length() >= 3){
                if(!string.chars().allMatch((Character::isLetter)))continue;
                if (dictionarySlowo.containsKey(string)){
                    dictionarySlowo.put(string, dictionarySlowo.get(string)+1);
                    if (dictionarySlowo.get(string) >= 3){
                        if (dictionaryKS.containsValue(string))continue;
                        dictionaryKS.put(id, string);
                        dictionarySK.put(string,id);
                        id++;
                    }
                } else dictionarySlowo.put(string, 1);
            }
        }


        return stringNum;
    }


    public HashMap<String, Integer> getDictionarySK() {
        return dictionarySK;
    }

    /**
     * Metoda zwraca ciąg o podanym numerze. Zwracany ciąg powinien przybrać postać
     * zależną od aktualnego stanu słownika. Słowa znajdujące się w słowniku powinny
     * zostać zastąpione w ciągu pofasdfasdciągami o postaci #numer, gdzie numer to klucz w
     * słowniku. Prawidłowy zapis podciągu #numer to np. #1, #2, #3 czy sdfasdfasd#13 a nie
     * np. #001, #002, #003 czy #013.
     *
     * @param id identyfikator zapisanego wcześniej w systemie ciągu.
     * @return aktualna postać ciągu z uwzględnieniem procesu deduplikacji.
     */
    @Override
    public String getString(int id) {
        dictionaryCiag.get(id);
        String zakoduj = dictionaryCiag.get(id);
        for (String string: this.dictionarySK.keySet()){
            zakoduj = zakoduj.replaceAll(string, "#"+dictionarySK.get(string));
        }
        return zakoduj;
    }
}
