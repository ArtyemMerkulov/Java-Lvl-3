package homework7;

public class Snail {

    public Snail(int nDim) {
        int[][] arr = new int[nDim][nDim];

        fillHelix(arr);
        draw(arr);
    }

    private void fillHelix(int[][] arr) {
        if (arr == null) throw new NullPointerException("Wrong null array!");

        int totalRow = arr.length - 1, totalCol = arr[0].length;

        if (totalRow + 1 != totalCol) throw new IllegalArgumentException("Wrong array! Need square matrix!");

        int i = 0, j = 0;
        int step = 0;
        int side = 0;

        int num = 0;

        do {
            if (step == totalCol && side == 0) {
                i++;
                totalCol--;
                side = 1;
                step = 0;
            } else if (step == totalRow && side == 1) {
                i--;
                j--;
                totalRow--;
                side = 2;
                step = 0;
            } else if (step == totalCol && side == 2) {
                i--;
                totalCol--;
                side = 3;
                step = 0;
            } else if (step == totalRow && side == 3) {
                i++;
                j++;
                totalRow--;
                side = 0;
                step = 0;
            }

            arr[i][j] = num++;

            if (step != totalRow && side == 0) j++;
            else if (step != totalCol && side == 1) i++;
            else if (step != totalRow && side == 2) j--;
            else if (step != totalCol && side == 3) i--;

            step++;

        } while (totalRow != 0 && totalCol != 0);
    }

    private void draw(int[][] Arr) {
        StringBuilder sb = new StringBuilder();
        for (int[] line : Arr) {
            for (int o : line) {
                sb.append(o);
                sb.append("\t\t");
            }
            sb.append("\n");
        }
        System.out.println(sb);
    }
}
