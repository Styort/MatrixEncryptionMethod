package MatrixDecryptionMessage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        int n, numOfVect;
        String alphabet = " abcdefghijklmnopqrstuvwxyz";
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Scanner in = new Scanner(System.in);

        System.out.println("Введите размерность матрицы-ключа: ");
        n = in.nextInt();
        double[][] mA = new double[n][n]; //матрица-ключ

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.println("Введите значение элемента [" + (i + 1) + "," + (j + 1) + "]: ");
                mA[i][j] = in.nextInt();
            }
        }

        System.out.println("Введите информацию, которую необходимо декодировать: ");
        System.out.println("Слово конец означает конец ввода");
        String inf = reader.readLine();
        //создаем лист, который будет хранить все векторы
        List<Double> codeInform = new ArrayList<>();
        while (!inf.equals("конец")){
            codeInform.add(Double.parseDouble(inf));
            inf = reader.readLine();
        }

        List<double[]> vect = new ArrayList<>();
        //вычисляем количество векторов
        if (codeInform.size() % n == 0)
            numOfVect = codeInform.size() / n;
        else
            numOfVect = (codeInform.size() / n) + 1;

        int count = 0;

        //разбиваем значения по векторам
        for (int i = 0; i < numOfVect; i++) {
            double[] sketch = new double[n];
            for (int j = 0; j < n; j++) {
                if (count < codeInform.size()) {
                    sketch[j] = codeInform.get(count);
                    count++;
                } else {
                    sketch[j] = 0;
                }
            }
            vect.add(i, sketch);
        }

        System.out.println();
        System.out.println("Определитель матрицы: " + determinant(mA,n));
        System.out.println();
        double[][] adj = new double[n][n];
        adjoint(mA, adj);
        System.out.println("Транспонированная матрица:");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(adj[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Обратная матрица: ");
        double[][] inv = new double[n][n];
        inverse(mA,inv);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(inv[i][j] + " ");
            }
            System.out.println();
        }

        List<double[][]> decodeInf = new ArrayList<>();
        System.out.println("Дешифрованные номера букв в алфавите:");
        for (int i = 0; i < vect.size(); i++) {
            //умножаем обратную матрицу на вектора закодированной информации
            double[][] deshifr = matrixMultiplection(inv,vect.get(i));
            for (int j = 0; j < deshifr.length; j++) {
                System.out.print(Math.round(deshifr[j][0]) + " ");
            }
            decodeInf.add(deshifr);
        }

        System.out.println();
        System.out.println("Дешифрованная информация:");
        //выводим символы по их номеру в алфавите
        for (int i = 0; i < decodeInf.size(); i++) {
            double[][] toAlph = decodeInf.get(i);
            for (int j = 0; j < toAlph.length; j++) {
                System.out.print(alphabet.charAt((int) Math.round(toAlph[j][0])));
            }
        }
    }

    //умножение матриц
    private static double[][] matrixMultiplection(double[][] mA, double[] vec) {
        int n = mA[0].length;

        double[][] C;
        C = new double[n][1];
        int i, j, k;

        //записываем произведение двух матриц в матрицу С
        for (i = 0; i < n; i++) {
            for (j = 0; j < 1; j++) {
                for (k = 0; k < n; k++) {
                    C[i][j] += mA[i][k] * vec[k];
                }
            }
        }
        return C;
    }

    // Функция, чтобы получить алгебраического дополнения А[р][Q] при temp[][]. n - размерность
    private static void getCofactor(double[][] A, double[][] temp, int p, int q, int n) {
        int i = 0, j = 0;

        //проходим по каждому элементу матрицы
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                // Копирование во временную матрицу только те элементы, которые не находятся в данной строке и столбце
                if (row != p && col != q) {
                    temp[i][j++] = A[row][col];

                    // Строка заполняется, таким образом, увеличивает индекс строки и происходит сброс индекса столбцов.
                    if (j == n - 1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    /* Рекурсивная функция для нахождения определителя матрицы.
               n - размерность A[][]. */
    private static double determinant(double[][] A, int n) {
        int D = 0;

        //если матрица содержит один элемент
        if (n == 1)
            return A[0][0];

        double[][] temp = new double[A.length][A.length]; // для хранения кофакторов

        int sign = 1;  // для хранения знака множителя

        //итерация для каждого элемента первой строки
        for (int f = 0; f < n; f++) {
            // получаем кофактор для A[0][f]
            getCofactor(A, temp, 0, f, n);
            D += sign * A[0][f] * determinant(temp, n - 1);

            // terms are to be added with alternate sign
            sign = -sign;
        }

        return D;
    }

    // Функция, для того чтобы получить транспонированную матрицу для A[N][N] в adj[N][N].
    private static void adjoint(double[][] A, double[][] adj) {
        if (A.length == 1) {
            adj[0][0] = 1;
            return;
        }

        // Temp используется для хранения алгебраических дополнений A[][]
        int sign = 1;
        double[][] temp = new double[A.length][A.length];

        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A.length; j++) {
                // Получаем алгебраическое дополнение для A[i][j]
                getCofactor(A, temp, i, j, A.length);

                //знак для adj[j][i] положительный если сумма строки и столбца
                // sign of adj[j][i] positive if sum of row
                // and column indexes is even.
                sign = ((i + j) % 2 == 0) ? 1 : -1;

                // Перестановка строк и столбцов, чтобы получить транспонированную матрицу кофактора
                adj[j][i] = (sign) * (determinant(temp, A.length - 1));
            }
        }
    }

    // Функция для вычисления и хранения обратной матрицы, возвращает ложь, если матрица вырождена
    private static boolean inverse(double[][] A, double[][] inverse) {
        // находим определитель для A[][]
        double det = determinant(A, A.length);
        if (det == 0) {
            System.out.println("Singular matrix, can't find its inverse");
            return false;
        }

        //находим транспонированную матрицу
        double[][] adj = new double[A.length][A.length];
        adjoint(A, adj);

        // Находим обратную матрицу при помощи формулы "inverse(A) = adj(A)/det(A)"
        for (int i = 0; i < A.length; i++)
            for (int j = 0; j < A.length; j++)
                inverse[i][j] = adj[i][j] / (float) (det);

        return true;
    }
}
