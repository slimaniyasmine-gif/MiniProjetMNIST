package mnist;

import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.ArrayList;
import java.util.List;

public class Util {

    // Chemin de base du projet
    private static final String BASE_PATH = 
        "C:/Users/lenovo/eclipse-workspace/MiniProjetMNIST/";

    // ================================================================
    // a) cerateTextFile(int n)
    // ================================================================
    public static void cerateTextFile(int n) throws IOException {

        String imagesPath = BASE_PATH + "data/train-images";
        String labelsPath = BASE_PATH + "data/train-labels";

        // Vérifier que les fichiers existent
        File imgFile = new File(imagesPath);
        File lblFile = new File(labelsPath);

        if (!imgFile.exists()) {
            System.out.println("❌ Fichier introuvable : " + imagesPath);
            System.out.println("   Taille : " + imgFile.length() + " bytes");
            return;
        }
        if (!lblFile.exists()) {
            System.out.println("❌ Fichier introuvable : " + labelsPath);
            return;
        }

        System.out.println("📂 train-images trouvé : " + imgFile.length() + " bytes");
        System.out.println("📂 train-labels trouvé : " + lblFile.length() + " bytes");

        try (
            DataInputStream imagesStream = new DataInputStream(
                new BufferedInputStream(new FileInputStream(imagesPath)));
            DataInputStream labelsStream = new DataInputStream(
                new BufferedInputStream(new FileInputStream(labelsPath)))
        ) {
            imagesStream.readInt();
            int totalImages = imagesStream.readInt();
            imagesStream.readInt();
            imagesStream.readInt();

            labelsStream.readInt();
            labelsStream.readInt();

            System.out.println("📊 Total images dans MNIST : " + totalImages);

            List<int[]> trois = new ArrayList<>();
            List<int[]> cinq  = new ArrayList<>();

            for (int i = 0; i < totalImages; i++) {
                byte[] pixels = new byte[784];
                imagesStream.readFully(pixels);
                int label = labelsStream.readUnsignedByte();

                int[] data = new int[784];
                for (int j = 0; j < 784; j++) {
                    data[j] = pixels[j] & 0xFF;
                }

                if (label == 3 && trois.size() < n) trois.add(data);
                else if (label == 5 && cinq.size() < n) cinq.add(data);

                if (trois.size() >= n && cinq.size() >= n) break;
            }

            System.out.println("🔢 Chiffres 3 trouvés : " + trois.size());
            System.out.println("🔢 Chiffres 5 trouvés : " + cinq.size());

            String outputPath = BASE_PATH + "chiffres.txt";
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputPath))) {
                for (int[] pixels : trois) {
                    StringBuilder line = new StringBuilder();
                    for (int val : pixels) line.append(val).append(",");
                    line.append("trois");
                    writer.println(line.toString());
                }
                for (int[] pixels : cinq) {
                    StringBuilder line = new StringBuilder();
                    for (int val : pixels) line.append(val).append(",");
                    line.append("cinq");
                    writer.println(line.toString());
                }
            }

            System.out.println("✅ chiffres.txt créé avec " + (n * 2) + " lignes.");
            System.out.println("📁 Emplacement : " + outputPath);
        }
    }

    // ================================================================
    // b) imageToFile(String nomImage)
    // ================================================================
    public static void imageToFile(String nomImage) throws IOException {
        BufferedImage img = ImageIO.read(new File(nomImage));
        if (img == null) {
            System.out.println("❌ Image introuvable : " + nomImage);
            return;
        }
        String nomFichier = nomImage.replace(".png", ".txt");

        try (PrintWriter writer = new PrintWriter(new FileWriter(nomFichier))) {
            StringBuilder line = new StringBuilder();
            for (int y = 0; y < 28; y++) {
                for (int x = 0; x < 28; x++) {
                    int rgb  = img.getRGB(x, y);
                    int r    = (rgb >> 16) & 0xFF;
                    int g    = (rgb >> 8)  & 0xFF;
                    int b    =  rgb        & 0xFF;
                    int gray = (r + g + b) / 3;
                    line.append(gray).append(",");
                }
            }
            if (line.length() > 0) line.deleteCharAt(line.length() - 1);
            writer.println(line.toString());
        }
        System.out.println("✅ Fichier texte créé : " + nomFichier);
    }

    // ================================================================
    // c) fileToImage(String nomFichier)
    // ================================================================
    public static void fileToImage(String nomFichier) throws IOException {
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichier))) {
            line = reader.readLine();
        }

        String[] values = line.split(",");
        BufferedImage img = new BufferedImage(28, 28, BufferedImage.TYPE_INT_RGB);

        int index = 0;
        for (int y = 0; y < 28; y++) {
            for (int x = 0; x < 28; x++) {
                int gray = Integer.parseInt(values[index++].trim());
                int rgb  = (gray << 16) | (gray << 8) | gray;
                img.setRGB(x, y, rgb);
            }
        }

        String nomImage = nomFichier.replace(".txt", "_restored.png");
        ImageIO.write(img, "png", new File(nomImage));
        System.out.println("✅ Image restaurée : " + nomImage);
    }

    // ================================================================
    // d) createExcelFile(String nomFichier)
    // ================================================================
    public static void createExcelFile(String nomFichier) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Chiffres");

        Row header = sheet.createRow(0);
        for (int i = 0; i < 784; i++) {
            header.createCell(i).setCellValue("pixel_" + i);
        }
        header.createCell(784).setCellValue("label");

        try (BufferedReader reader = new BufferedReader(new FileReader(nomFichier))) {
            String line;
            int rowNum = 1;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                String[] values = line.split(",");
                Row row = sheet.createRow(rowNum++);
                for (int i = 0; i < 784; i++) {
                    row.createCell(i).setCellValue(
                        Integer.parseInt(values[i].trim()));
                }
                row.createCell(784).setCellValue(values[784].trim());
            }
        }

        String excelName = nomFichier.replace(".txt", ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(excelName)) {
            workbook.write(fos);
        }
        workbook.close();
        System.out.println("✅ Fichier Excel créé : " + excelName);
    }

    // ================================================================
    // generateArff pour Weka
    // ================================================================
    public static void generateArff(String inputFile, 
                                     String outputFile) throws IOException {
        try (
            PrintWriter writer = new PrintWriter(new FileWriter(outputFile));
            BufferedReader reader = new BufferedReader(new FileReader(inputFile))
        ) {
            writer.println("@RELATION chiffres");
            writer.println();
            for (int i = 0; i < 784; i++) {
                writer.println("@ATTRIBUTE pixel_" + i + " NUMERIC");
            }
            writer.println("@ATTRIBUTE classe {trois,cinq}");
            writer.println();
            writer.println("@DATA");

            String line;
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                writer.println(line);
            }
        }
        System.out.println("✅ Fichier ARFF créé : " + outputFile);
    }
}