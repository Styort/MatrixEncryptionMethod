package MatrixEncryptingMessage;

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
        System.out.println("Введите информацию, которую необходимо закодировать: ");
        String inf = reader.readLine();
        int mB[] = new int[inf.length()]; //массив с порядковыми номерами символов введенной информации

        //определяем числовой эквивалент слова
        for (int i = 0; i < inf.length(); i++) {
            mB[i] = alphabet.indexOf(inf.charAt(i));
        }

        //создаем лист, который будет хранить все векторы
        List<double[]> vect = new ArrayList<>();

        //вычисляем количество векторов
        if (inf.length() % n == 0)
            numOfVect = inf.length() / n;
        else
            numOfVect = (inf.length() / n) + 1;
        for (int i = 0; i < numOfVect; i++) {
            vect.add(new double[n]);
        }
        int count = 0;

        //разбиваем значения по векторам
        for (int i = 0; i < numOfVect; i++) {
            double[] sketch = new double[n];
            for (int j = 0; j < n; j++) {
                if(count<mB.length){
                    sketch[j] = mB[count];
                    count++;
                }else {
                    sketch[j] = 0;
                }
            }
            vect.set(i,sketch);
        }

        List<double[]> codeInf = new ArrayList<>();
        System.out.println("Зашифрованная информация: ");
        for (int i = 0; i < vect.size() ; i++) {
            double[][] codeV =  matrixMultiplection(mA, vect.get(i)); //перемноженная матрица mA с вектором под номером i
            double[] sketch = new double[codeV.length];
            //записываем значение в одномерный массив 33для удобства
            for (int j = 0; j < codeV.length; j++) {
                sketch[j] = codeV[j][0];
                System.out.print((int)sketch[j] + " ");
            }
            codeInf.add(sketch);
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
}
