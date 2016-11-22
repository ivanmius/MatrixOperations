import components.simplereader.SimpleReader;
import components.simplereader.SimpleReader1L;
import components.simplewriter.SimpleWriter;
import components.simplewriter.SimpleWriter1L;

/**
 * Simple HelloWorld program (clear of Checkstyle and FindBugs warnings).
 *
 * @author P. Bucci
 */
public final class DeterminantCalc {

    /**
     * Default constructor--private to prevent instantiation.
     */
    private DeterminantCalc() {
        // no code needed here
    }

    /**
     * Gets the size of the desired matrix from the user.
     *
     * @param in
     *            the input stream
     * @param out
     *            the output stream
     * @return the size as {rows, columns}
     */
    private static int[] askSize(SimpleReader in, SimpleWriter out) {
        int[] answer = new int[2];
        out.print("How many rows in the matrix?: ");
        answer[0] = in.nextInteger();
        out.print("How many columns in the matrix?: ");
        answer[1] = in.nextInteger();
        return answer;
    }

    /**
     * Gets a whole matrix from the user.
     *
     * @param in
     *            the input stream
     * @param out
     *            the output stream
     * @param size
     *            the expected size of the matrix
     * @return the matrix in the form of a nested array
     */
    private static int[][] askMatrix(SimpleReader in, SimpleWriter out,
            int[] size) {
        int[][] matrix = new int[size[0]][size[1]];

        out.println(
                "Enter matrix, with spaces between values, and newlines between rows:");
        for (int row = 0; row < size[0]; row++) {
            String line = in.nextLine();
            for (int col = 0; col < size[1]; col++) {
                matrix[row][col] = eat(line);
                line = pop(line);
            }
        }
        return matrix;
    }

    /**
     * Takes a row from the matrix and returns the first int in it.
     *
     * @param line
     *            The string representing the matrix row
     * @return The first num in the row
     */
    private static int eat(String line) {
        assert line.length() != 0 : "Passed empty line to eat!";
        int spaceIndex = line.indexOf(" ");
        if (spaceIndex == -1) {
            return Integer.parseInt(line);
        } else {
            String num = line.substring(0, spaceIndex);
            return Integer.parseInt(num);
        }

    }

    /**
     * Removes the first num from a matrix row.
     *
     * @param line
     *            The original matrix row
     * @return the matrix row without the first num
     */
    private static String pop(String line) {
        if (line.length() != 0) {
            String newLine = line.trim();
            int spaceIndex = newLine.indexOf(" ");
            spaceIndex++;
            newLine = newLine.substring(spaceIndex, newLine.length());
            return newLine;
        } else {
            return line;
        }
    }

    /**
     * Prints a given matrix to the console.
     *
     * @param out
     *            The output stream
     * @param mat
     *            The matrix to be printed
     */
    public static void printMatrix(SimpleWriter out, int[][] mat) {
        final int maxDigits = getMaxDigits(mat);
        int[] size = { mat.length, mat[0].length };
        int rowLength = (maxDigits * size[1]) + (size[1] - 1); // digit fields + spaces
        out.print('\u250C');
        for (int i = 0; i < rowLength; i++) {
            out.print(" ");
        }
        out.println('\u2510');

        for (int row = 0; row < mat.length; row++) {
            out.print('\u2502');
            printNum(out, maxDigits, mat[row][0]);
            for (int col = 1; col < mat[row].length; col++) {
                out.print(" ");
                printNum(out, maxDigits, mat[row][col]);
            }
            out.println('\u2502');
        }

        out.print('\u2514');
        for (int i = 0; i < rowLength; i++) {
            out.print(" ");
        }
        out.println('\u2518');
    }

    /**
     * Finds the number of digits in the largest number in the matrix.
     *
     * @param mat
     *            The matrix to search
     * @return the maximum number of digits in any entry in the matrix
     */
    private static int getMaxDigits(int[][] mat) {
        int max = 0;
        for (int row = 0; row < mat.length; row++) {
            for (int col = 0; col < mat[row].length; col++) {
                int numLength = String.valueOf(mat[row][col]).length();
                if (numLength > max) {
                    max = numLength;
                }
            }
        }
        return max;
    }

    /**
     * Prints a number for a matrix with whitespace.
     *
     * @param out
     *            the output stream
     * @param maxDigits
     *            the maximum number of digits in any entry in the matrix
     * @param num
     *            the number to print
     */
    private static void printNum(SimpleWriter out, int maxDigits, int num) {
        int numLength = String.valueOf(num).length();
        int whitespace = maxDigits - numLength;
        if (whitespace % 2 != 0) { // odd
            out.print(" ");
        }
        whitespace /= 2;
        for (int i = 0; i < whitespace; i++) {
            out.print(" ");
        }
        out.print(num);
        for (int i = 0; i < whitespace; i++) {
            out.print(" ");
        }
    }

    /**
     * Calculates the determinant of the a matrix.
     *
     * @param matrix
     *            the square matrix
     * @return the determinant of the matrix
     * @requires the matrix is square and at least 2 x 2
     */
    public static int determinant(int[][] matrix) {
        assert matrix.length == matrix[0].length : "Violation: matrix isn't square";
        assert matrix.length >= 2 : "Violation: matrix must be at least 2x2";
        int determinant = 0;

        if (matrix.length == 2) {
            determinant = (matrix[0][0] * matrix[1][1])
                    - (matrix[0][1] * matrix[1][0]);
        } else {
            int sign = 1;

            for (int col = 0; col < matrix.length; col++) {
                int cofactor = matrix[0][col];
                cofactor *= sign;
                int[][] minorMatrix = getMinorMatrix(matrix, 0, col);
                cofactor *= determinant(minorMatrix); // this is the minor
                determinant += cofactor;
                sign = -sign;
            }
        }
        return determinant;
    }

    /**
     * Returns a minor matrix of the given matrix and coordinates.
     *
     * @param matrix
     *            the big matrix to take a minor from
     * @param coRow
     *            the row to skip over in the minor
     * @param coCol
     *            the col to skip over in the minor
     * @requires the matrix is at least 3x3 (minor of a 2x2 is a number).
     *
     * @return the desired minor matrix
     */
    public static int[][] getMinorMatrix(int[][] matrix, int coRow, int coCol) {
        assert matrix.length > 2
                && matrix[0].length > 2 : "Violation: matrix must be at least 3x3";
        int[][] minorMatrix = new int[matrix.length - 1][matrix[0].length - 1];
        int minorRow = 0;

        for (int row = 0; row < matrix.length; row++) {
            if (coRow != row) {
                int minorCol = 0;

                for (int col = 0; col < matrix[row].length; col++) {
                    if (coCol != col) {
                        minorMatrix[minorRow][minorCol] = matrix[row][col];
                        minorCol++;
                    }
                }
                minorRow++;
            }
        }

        return minorMatrix;
    }

    /**
     * Main method.
     *
     * @param args
     *            the command line arguments; unused here
     */
    public static void main(String[] args) {
        SimpleWriter out = new SimpleWriter1L();
        SimpleReader in = new SimpleReader1L();
        int[] size = askSize(in, out);
        int[][] matrix = askMatrix(in, out, size);
        printMatrix(out, matrix);
        out.println("Determinant: " + determinant(matrix));
        in.close();
        out.close();
    }

}
