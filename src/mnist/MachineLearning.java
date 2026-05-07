package mnist;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.RandomForest;
import weka.classifiers.Evaluation;
import java.util.Random;

public class MachineLearning {

    private static final String BASE_PATH =
        "C:/Users/lenovo/eclipse-workspace/MiniProjetMNIST/";

    public static void main(String[] args) throws Exception {

        // ── Désactiver le package manager Weka ──
        System.setProperty("weka.core.WekaPackageManager.noPackageManagement", "true");

        // ── Charger les données ──
        System.out.println("📂 Chargement des données...");
        DataSource source = new DataSource(BASE_PATH + "train-data.arff");
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);
        System.out.println("✅ Données chargées : " + data.numInstances() + " instances");

        // ═══════════════════════════════════
        // Modèle 1 : Naive Bayes
        // ═══════════════════════════════════
        System.out.println("\n========== NAIVE BAYES ==========");
        NaiveBayes nb = new NaiveBayes();
        nb.buildClassifier(data);

        Evaluation evalNB = new Evaluation(data);
        evalNB.crossValidateModel(nb, data, 10, new Random(1));
        System.out.println(evalNB.toSummaryString());
        System.out.printf("✅ Précision Naive Bayes : %.2f%%%n",
                           evalNB.pctCorrect());

        // ═══════════════════════════════════
        // Modèle 2 : Random Forest
        // ═══════════════════════════════════
        System.out.println("\n========== RANDOM FOREST ==========");
        RandomForest rf = new RandomForest();
        rf.setNumIterations(50);
        rf.buildClassifier(data);

        Evaluation evalRF = new Evaluation(data);
        evalRF.crossValidateModel(rf, data, 10, new Random(1));
        System.out.println(evalRF.toSummaryString());
        System.out.printf("✅ Précision Random Forest : %.2f%%%n",
                           evalRF.pctCorrect());

        // ═══════════════════════════════════
        // Comparaison
        // ═══════════════════════════════════
        System.out.println("\n========== COMPARAISON ==========");
        System.out.printf("Naive Bayes  : %.2f%%%n", evalNB.pctCorrect());
        System.out.printf("Random Forest: %.2f%%%n", evalRF.pctCorrect());

        if (evalRF.pctCorrect() > evalNB.pctCorrect()) {
            System.out.println("🏆 Meilleur modèle : Random Forest");
        } else {
            System.out.println("🏆 Meilleur modèle : Naive Bayes");
        }
    }
}