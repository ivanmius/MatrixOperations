import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class DeterminantCalcTest {

    final int[][] mat2x2 = { { 3, 2 }, { 5, 3 } };
    final int[][] mat3x3 = { { 3, 5, 2 }, { 5, 3, 1 }, { 2, 5, 5 } };
    final int[][] mat4x4 = { { 3, 5, 2, -4 }, { 5, 3, 1, 0 }, { 2, 5, 5, -2 },
            { 9, 9, 6, -6 } };
    /*
     * Test of determinant.
     */

    @Test
    public void determinantTest1() {
        int ans = DeterminantCalc.determinant(this.mat2x2);
        assertEquals(-1, ans);
    }

    @Test
    public void determinantTest2() {
        int ans = DeterminantCalc.determinant(this.mat3x3);
        assertEquals(-47, ans);
    }

    @Test
    public void determinantTest3() {
        int ans = DeterminantCalc.determinant(this.mat4x4);
        assertEquals(186, ans);
    }

    /*
     * Tests of getMinorMatrix.
     */

    @Test
    public void getMinorMatrixTest1() {
        int row = 0;
        int col = 0;
        int[][] ans = DeterminantCalc.getMinorMatrix(this.mat3x3, row, col);
        final int[][] corAns = { { 3, 1 }, { 5, 5 } };

        for (int r = 0; row < ans.length; row++) {
            for (int c = 0; c < ans[0].length; c++) {
                assertEquals(ans[r][c], corAns[r][c]);
            }
        }
    }

    @Test
    public void getMinorMatrixTest2() {
        int row = 1;
        int col = 2;
        int[][] ans = DeterminantCalc.getMinorMatrix(this.mat3x3, row, col);
        final int[][] corAns = { { 3, 5 }, { 2, 5 } };

        for (int r = 0; row < ans.length; row++) {
            for (int c = 0; c < ans[0].length; c++) {
                assertEquals(ans[r][c], corAns[r][c]);
            }
        }
    }

}
