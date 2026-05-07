package mnist;

public class Test {
    public static void main(String[] args) throws Exception {

        // ── Test 1 : Créer chiffres.txt ──
        System.out.println("=== Test cerateTextFile ===");
        Util.cerateTextFile(400);

        // ── Test 2 : Créer fichier Excel ──
        System.out.println("\n=== Test createExcelFile ===");
        Util.createExcelFile("chiffres.txt");

        // ── Test 3 : Générer ARFF pour Weka ──
        System.out.println("\n=== Génération ARFF ===");
        Util.generateArff("chiffres.txt", "train-data.arff");

        System.out.println("\n✅ Tous les tests terminés !");
    }
}