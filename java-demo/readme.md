前言

数独相信很多人都玩过，趣味性很强，十分的耐玩。可有没有程序员想过玩实现一个数独布局的算法呢？算法是个很有意思，很神奇的东西。

算法如下，需要预先给出几个固定的值，目前解决的一个最难的数独是大概26个已知值的情况，理论上应该能解决任意已知值的数独，不过不知道会不会迭代栈溢出……因为在26个已知值的情况下就迭代了3000多次了，囧~~~

结果显示如下：



这是已知值:
1 1 2
1 4 8 
1 5 5
1 6 1
1 7 7
1 8 3
2 1 1
2 2 6
2 4 4
3 5 9
3 7 8
3 8 4
4 7 9
5 8 7
6 1 3
6 6 8
6 7 4
6 8 6
7 3 7
7 6 4
7 7 1
8 3 3
8 6 7
9 3 4
9 4 6
9 7 3
9 8 2
（PS：如9 8 2表示第9行第二列的值是2）
将上面的数字保存到num.txt文件中,再把底下附的源代码保存为Sudoku.java。

然后在cmd命令行模型下输入：
javac Sudoku.java 
java Sudoku <num.txt 
即可得到结果。
这个解法是我之前看到八皇后排列问题的解法后结合应用的，在数独中采用了这种解法，不过应该算是比较暴力的拆解，所以我后面命名成violentBreak。。。
虽然只是一个很小的事，但是能尝试着用编程去解决日常遇到的事，突然觉得很开心，学以致用！