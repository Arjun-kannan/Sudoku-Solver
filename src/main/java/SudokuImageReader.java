import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class SudokuImageReader {
    private static final int SIZE = 9;

    public static int[][] extractSudokuBoard(File imageFile) {
        int SIZE = 9;
        int[][] board = new int[SIZE][SIZE];

        Mat original = Imgcodecs.imread(imageFile.getAbsolutePath());
        Mat gray = new Mat();
        Mat blurred = new Mat();
        Mat threshold = new Mat();

        // Convert to grayscale and preprocess
        Imgproc.cvtColor(original, gray, Imgproc.COLOR_BGR2GRAY);
        // Increase contrast
        Core.normalize(gray, gray, 0, 255, Core.NORM_MINMAX);
        Imgproc.GaussianBlur(gray, blurred, new Size(5, 5), 0);
        Imgproc.adaptiveThreshold(blurred, threshold, 255, Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY, 11, 2);
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(2, 2));
        Imgproc.morphologyEx(threshold, threshold, Imgproc.MORPH_CLOSE, kernel);
        Core.bitwise_not(threshold, threshold);

        // Convert to BufferedImage
        BufferedImage fullImage = Mat2BufferedImage(threshold);

        try {
            ITesseract tesseract = new Tesseract();
            tesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
            tesseract.setLanguage("eng");
            tesseract.setTessVariable("tessedit_char_whitelist", "123456789");
            tesseract.setTessVariable("user_defined_dpi", "300"); // Treat input as high-DPI
            tesseract.setPageSegMode(6); // Single uniform block
            tesseract.setOcrEngineMode(1); // OEM_LSTM_ONLY

            int cellWidth = fullImage.getWidth() / SIZE;
            int cellHeight = fullImage.getHeight() / SIZE;
            int padding = (int) (0.1 * cellWidth); // 10% padding instead of hardcoded 5

            for (int row = 0; row < SIZE; row++) {
                for (int col = 0; col < SIZE; col++) {
                    int x = col * cellWidth + padding;
                    int y = row * cellHeight + padding;
                    int w = cellWidth - 2 * padding;
                    int h = cellHeight - 2 * padding;

                    BufferedImage cellImage = fullImage.getSubimage(x, y, w, h);

                    int whitePixels = 0;
                    for (int p = 0; p < cellImage.getHeight(); p++) {
                        for (int q = 0; q < cellImage.getWidth(); q++) {
                            if ((cellImage.getRGB(q, p) & 0xFF) > 200) whitePixels++;
                        }
                    }

                    if (whitePixels > 0.9 * cellImage.getWidth() * cellImage.getHeight()) {
                        board[row][col] = 0;
                        continue; // Likely an empty cell
                    }


                    String cellText = tesseract.doOCR(cellImage).replaceAll("[^1-9]", "");
                    //extracts the text and stores the subimage in a required folder
                    if (cellText.length() == 1) {
                        char digit = cellText.charAt(0);

                        // Define the folder path for the digit
                        File labelFolder = new File("dataset/" + digit);
                        if (!labelFolder.exists()) labelFolder.mkdirs();

                        // Construct a unique filename (timestamp or row_col is fine)
                        String filename = System.currentTimeMillis() + "_" + row + "_" + col + ".png";
                        File outputFile = new File(labelFolder, filename);

                        // Save the cropped image
                        ImageIO.write(cellImage, "png", outputFile);

                        BufferedWriter writer = new BufferedWriter(new FileWriter("labels.csv", true));
                        writer.write(outputFile.getPath() + "," + digit);
                        writer.newLine();
                        writer.close();

                    }


                    // Keep only first digit if OCR returns extra
                    if (!cellText.isEmpty() && Character.isDigit(cellText.charAt(0))) {
                        board[row][col] = cellText.charAt(0) - '0';
                    } else {
                        board[row][col] = 0;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return board;
    }


    public static BufferedImage Mat2BufferedImage(Mat mat) {
        int type = BufferedImage.TYPE_BYTE_GRAY;
        if (mat.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = mat.channels() * mat.cols() * mat.rows();
        byte[] buffer = new byte[bufferSize];
        mat.get(0, 0, buffer);
        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
        return image;
    }
}
