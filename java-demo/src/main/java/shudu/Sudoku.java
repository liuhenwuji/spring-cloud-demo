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
 * 代码逻辑解析：
 * 1、建立一个9X9矩阵保存数独正确的值
 * 2、建立一个9X9列表，每个列表里保存着该位置，数独可以填入的可能值
 * 如ArrayList[1][1]={1,2,3}即1,1这个位置的数独可能可以填入其中一个
 * 当矩阵该位置已保存了正确值时，清空对应位置的ArrayList
 * 3、当列表ArrayList里只有一个可能值时，那个值就是数独的正确值，将该
 * 值填入数独矩阵
 * 并更新ArrayList
 * <p>
 * PS：以上算法只能用于求困难级别的数独，因为我在玩游戏的时候发现，每
 * 个时刻必定会有
 * 一个数独空位，是只能填入一个值的，所以这个算法才能运行
 * 当一个数独(即已知位置的值变少时)，可能会出现所有的空位都最起码有两
 * 个值时，
 * 需要改进算法，通过代入来判断这个数独是否成立
 * <p>
 * 4、于是后期我加入了暴力破解法，在上面的步骤执行后无法得出数独，即使
 * 用暴力破解
 */


public class Sudoku {
    //弄十的原因是为了好记忆，0,0不用，只用1-9的list
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

        //先都初始化为-1，即此时没有填充数字
        for (int i = 0; i < 10; i++)
            for (int j = 0; j < 10; j++)
                correctNum[i][j] = -1;


    }

    public Sudoku() {
    }

    //在对应数独位置插入正确值
    public void insert(int row, int col, int value) {
        correctNum[row][col] = value;
        availableNum[row][col] = null;
        delete(row, col, value);

    }
    //每插入一个数值，就删除相应的行列和小框框3X3数独里对应的ArrayList里可能的该值

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

    //判断插入的值时处于哪个小框框数独里
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

    //判断空格里所能填的数字是不是只能有一个,当返回-1时通过检测报错
    public int[] judgeIfOnlyOne() {

        for (int row = 1; row < 10; row++)
            for (int col = 1; col < 10; col++) {
                if (availableNum[row][col] != null)
                    if (availableNum[row][col].size() == 1)
                        return new int[]{row, col};
            }

        return new int[]{-1, -1};

    }

    // 判断为唯一，但是空格里还有多于1个的数时，我们直接将哪个正确的值填入

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


    //判断数独的矩阵是否填满，没有则继续
    public boolean judgeMatrixFull() {
        for (int i = 1; i < 10; i++)
            for (int j = 1; j < 10; j++)
                if (correctNum[i][j] == -1)
                    return false;
        return true;
    }

    //先输入已知位置的数字
    public void inputNumKnown() {
        while (scan.hasNextInt()) {
            int row = scan.nextInt();
            int col = scan.nextInt();
            int value = scan.nextInt();
            insert(row, col, value);
            delete(row, col, value);
        }
    }


    //打印数独结果
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


    //暴力破解
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
        //不能insert的话说明填满了
        int[] insertPos = canInsert(tempMatrix);
        if (insertPos[0] == -1) {
            System.out.println("all insert is done.This is the last Sudoku:");
            printSudoku(tempMatrix);
            return;
        }


        for (int val = 1; val <= 10; val++) {
            if (val == 10) {
                tempMatrix = null; //让JVM回收垃圾
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


    //数独开始运算的函数
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
        //满了就不用暴力破解了
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