package shudu;

import java.util.ArrayList;
import java.util.Scanner;


/**
 * This class named Sudoku can auto calculate Sudoku but
 * you should input some nums which are already known.
 * For instance:
 * 1 5 3
 * 2 4 7
 * There two rows means [1][5]=3 [2][4]=7
 * i.e. In row 1 col 5 is value:5
 * you can write all known num into one txt file
 * and input into this program .
 * such as java Sudoku < num.txt
 */

/**
 * �����߼�������
 * 1������һ��9X9���󱣴�������ȷ��ֵ
 * 2������һ��9X9�б�ÿ���б��ﱣ���Ÿ�λ�ã�������������Ŀ���ֵ
 * ��ArrayList[1][1]={1,2,3}��1,1���λ�õ��������ܿ�����������һ��
 * �������λ���ѱ�������ȷֵʱ����ն�Ӧλ�õ�ArrayList
 * 3�����б�ArrayList��ֻ��һ������ֵʱ���Ǹ�ֵ������������ȷֵ������
 * ֵ������������
 * ������ArrayList
 * <p>
 * PS�������㷨ֻ�����������Ѽ������������Ϊ��������Ϸ��ʱ���֣�ÿ
 * ��ʱ�̱ض�����
 * һ��������λ����ֻ������һ��ֵ�ģ���������㷨��������
 * ��һ������(����֪λ�õ�ֵ����ʱ)�����ܻ�������еĿ�λ������������
 * ��ֵʱ��
 * ��Ҫ�Ľ��㷨��ͨ���������ж���������Ƿ����
 * <p>
 * 4�����Ǻ����Ҽ����˱����ƽⷨ��������Ĳ���ִ�к��޷��ó���������ʹ
 * �ñ����ƽ�
 */


public class Sudoku {
    //Ūʮ��ԭ����Ϊ�˺ü��䣬0,0���ã�ֻ��1-9��list
    private ArrayList<Integer>[][] availableNum = new ArrayList[10][10];
    private int[][] correctNum = new int[10][10];
    private Scanner scan = new Scanner(System.in);
    private int countNum = 0;


    {
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                availableNum[i][j] = new ArrayList<>();
            }
        }

        for (int row = 1; row < 10; row++) {
            for (int col = 1; col < 10; col++) {
                for (int i = 1; i < 10; i++)
                    availableNum[row][col].add(new Integer(i));
            }
        }

        //�ȶ���ʼ��Ϊ-1������ʱû���������
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                correctNum[i][j] = -1;


    }

    public Sudoku() {
    }

    //�ڶ�Ӧ����λ�ò�����ȷֵ
    public void insert(int row, int col, int value) {
        correctNum[row][col] = value;
        availableNum[row][col] = null;
        delete(row, col, value);

    }
    //ÿ����һ����ֵ����ɾ����Ӧ�����к�С���3X3�������Ӧ��ArrayList����ܵĸ�ֵ

    public void delete(int row, int col, int value) {
        //delte row
        for (int i = 1; i < 10; i++) {
            if (availableNum[row][i] != null)
                availableNum[row][i].remove(new Integer(value));
        }

        //delete column
        for (int i = 1; i < 10; i++) {
            if (availableNum[i][col] != null)
                availableNum[i][col].remove(new Integer(value));
        }

        //delete box num
        int[] itsCenter = judgeCenterPos(row, col);
        for (int temp1 = itsCenter[0] - 1; temp1 <= itsCenter[0] + 1; temp1++)
            for (int temp2 = itsCenter[1] - 1; temp2 <= itsCenter[1] + 1; temp2++)
                if (availableNum[temp1][temp2] != null) {
                    availableNum[temp1][temp2].remove(new Integer(value));
                }

    }

    //�жϲ����ֵʱ�����ĸ�С���������
    public int[] judgeCenterPos(int row, int col) {
        int[] itsCenter = new int[2];
        for (int centerRow = 2; centerRow < 9; centerRow += 3)
            for (int centerCol = 2; centerCol < 9; centerCol += 3) {
                if (Math.abs(row - centerRow) <= 1 &&
                        Math.abs(col - centerCol) <= 1) {
                    itsCenter[0] = centerRow;
                    itsCenter[1] = centerCol;
                    return itsCenter;
                }

            }
        System.out.println("Some unchecked error was happened");
        return itsCenter;

    }

    //�жϿո���������������ǲ���ֻ����һ��,������-1ʱͨ����ⱨ��
    public int[] judgeIfOnlyOne() {

        for (int row = 1; row < 10; row++)
            for (int col = 1; col < 10; col++) {
                if (availableNum[row][col] != null)
                    if (availableNum[row][col].size() == 1)
                        return new int[]{row, col};
            }

        return new int[]{-1, -1};

    }

    // �ж�ΪΨһ�����ǿո��ﻹ�ж���1������ʱ������ֱ�ӽ��ĸ���ȷ��ֵ����

    public void insertIfCan() {

        for (int row = 1; row <= 7; row += 3) {
            for (int col = 1; col <= 7; col += 3) {
                for (int z = 1; z < 10; z++) {
                    int count = 0;
                    Integer temp = new Integer(z);
                    int itemp = 0, jtemp = 0;
                    outer:
                    for (int i = row; i < row + 3; i++) {
                        for (int j = col; j < col + 3; j++) {
                            if (availableNum[i][j] != null) {
                                if (availableNum[i][j].contains(temp)) {
                                    count++;
                                    itemp = i;
                                    jtemp = j;
                                    if (count > 1)
                                        break outer;
                                }
                            }
                        }
                    }
                    if (count == 1 && itemp != 0) {
                        insert(itemp, jtemp, z);
                    }
                }

            }
        }
    }


    //�ж������ľ����Ƿ�������û�������
    public boolean judgeMatrixFull() {
        for (int i = 1; i < 10; i++)
            for (int j = 1; j < 10; j++)
                if (correctNum[i][j] == -1)
                    return false;
        return true;
    }

    //��������֪λ�õ�����
    public void inputNumKnown() {
        while (scan.hasNextInt()) {
            int row = scan.nextInt();
            int col = scan.nextInt();
            int value = scan.nextInt();
            insert(row, col, value);
            delete(row, col, value);
        }
    }


    //��ӡ�������
    public void printSudoku() {
        printSudoku(correctNum);

    }

    public void printSudoku(int[][] arr) {
        System.out.println("Sudoku result:");
        for (int i = 1; i < 10; i++) {
            for (int j = 1; j < 10; j++)
                System.out.print(arr[i][j] + " ");
            System.out.println(" ");
        }
    }

    public void printList() {
        for (int i = 1; i < 10; i++)
            for (int j = 1; j < 10; j++) {
                System.out.print(i + " " + j + ":");
                if (availableNum[i][j] != null)
                    for (int z = 0; z < availableNum[i][j].size(); z++) {
                        System.out.print(availableNum[i][j].get(z) + " ");
                    }
                System.out.println(" ");
            }
    }


    //�����ƽ�
    public void violentBreak() {
        int i = 1, j = 1;
        outer:
        for (; i < 10; i++)
            for (; j < 10; j++)
                if (correctNum[i][j] != -1)
                    break outer;

        violentInsert(i, j, correctNum[i][j], correctNum);
    }


    public void violentInsert(int row, int col, int value, int[][] arr) {
        countNum++;
        int[][] tempMatrix = new int[10][10];

        for (int i = 1; i < 10; i++)
            for (int j = 1; j < 10; j++)
                tempMatrix[i][j] = arr[i][j];

        tempMatrix[row][col] = value;
        //����insert�Ļ�˵��������
        int[] insertPos = canInsert(tempMatrix);
        if (insertPos[0] == -1) {
            System.out.println("all insert is done.This is the last Sudoku:");
            printSudoku(tempMatrix);
            return;
        }


        for (int val = 1; val <= 10; val++) {
            if (val == 10) {
                tempMatrix = null; //��JVM��������
                //System.out.println("value=10 happened.");
                return;
            }
            if (judgeIfViolentInsert(insertPos[0], insertPos
                    [1], val, tempMatrix)) {
                //System.out.println("insert happened.");
                violentInsert(insertPos[0], insertPos[1], val, tempMatrix);
            }
        }

    }

    public int[] canInsert(int[][] tempMatrix) {
        int[] pos = {-1, -1};
        for (int i = 1; i < 10; i++)
            for (int j = 1; j < 10; j++) {
                if (tempMatrix[i][j] == -1) {
                    pos[0] = i;
                    pos[1] = j;
                    return pos;
                }
            }
        return pos;
    }


    public boolean judgeIfViolentInsert(int row, int col, int value, int
            [][] tempMatrix) {
        for (int j = 1; j < 10; j++)
            if (value == tempMatrix[row][j])
                return false;

        for (int i = 1; i < 10; i++)
            if (value == tempMatrix[i][col])
                return false;


        int[] itsCenter = judgeCenterPos(row, col);
        for (int temp1 = itsCenter[0] - 1; temp1 <= itsCenter[0] + 1; temp1++)
            for (int temp2 = itsCenter[1] - 1; temp2 <= itsCenter[1] + 1; temp2++)
                if (value == tempMatrix[temp1][temp2])
                    return false;

        return true;
    }


    //������ʼ����ĺ���
    public void start() {
        int[] nextInsert = new int[2];
        int count = 0;
        this.inputNumKnown();

        while (!judgeMatrixFull()) {
            nextInsert = judgeIfOnlyOne();
            if (nextInsert[0] == -1) {
                this.insertIfCan();
                count++;
                if (count == 15) {
                    System.out.println("Cannot fullfill this sodoku through finding the only one left.");
                    System.out.println("count:" + count);
                    break;
                }
                continue;

            }
            int value = availableNum[nextInsert[0]][nextInsert[1]].get(0);
            insert(nextInsert[0], nextInsert[1], value);
        }

        printSudoku();
        //���˾Ͳ��ñ����ƽ���
        if (judgeMatrixFull())
            return;
        System.out.println("Now we should break this Sudoku by violent method.");
        violentBreak();
        System.out.println("Recursion times:" + countNum);
    }


    public static void main(String[] args) {

        Sudoku test1 = new Sudoku();
        test1.start();

        int[] a = new int[2];
        System.out.println(a);
        System.out.println(a[0]);

    }

}