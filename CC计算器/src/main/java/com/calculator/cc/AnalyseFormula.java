package com.calculator.cc;

/**
 * Created by 丛 on 2017/1/18 0018.
 */

class AnalyseFormula {



    //传入字符串公式进行分析计算
    double calculateFormula(String str){
        char[] chars = str.toCharArray();
        int chars_index = 0;
        double result = 0;//最终返回变量
        int c_len = str.length();
        int i, k;//i当过数组下标
        double temp, j;
        double[] opnd = new double[99];//操作数栈,能存10个操作数,包括小数点
        int opnd_index = 0;
        char[] optr = new char[99];//操作符栈
        int optr_index = 0;
            /*当 ) 进入栈的时候，会将 (……)里的计算式都处理了,
            左栈存一个两个数结合的新数,右栈指针指向(,
            紧接着再右栈再输入 +-×÷* 就不许先不需要计算了,直接替换 ) */
        int CalByButton_r = 0;//等于0是 ) 处理式子之前
        char[] num_stack = new char[99];//临时数栈
        int num_stack_index = 0;//临时数下标
        double num_temp = 0;//临时数

        //注意：while的最后一次运行是 数字的，所以数字下面的判断都不会执行
        while (c_len != 0)
        {//+-×÷.肯不会在字符串开头 (可能在开头，需要处理
            c_len--;
            //chars[chars_index]是数字情况，包括小数点
            if ((chars[chars_index] <= '9' && chars[chars_index] >= '0') || (chars[chars_index] == '.'))
            {
                num_stack[num_stack_index] = chars[chars_index];
                num_stack_index++;
                chars_index++;
                continue;
            }
            //chars[chars_index]是 + 情况
            if (chars[chars_index] == '+')
            {
                num_stack_index--;
                if (num_stack_index == -1)
                {// ) 已经提取一遍左面了，) 后的运算符 就不用再提左面了
                    num_stack_index = 0;
                    //goto c1;
                }else {//将+前一个数抽出存入opnd，num_temp是从字符串中成功抽取的一个数,并且已经变成double
                    for (i = num_stack_index, j = 1; i >= 0; i--, j *= 10) {
                        if (num_stack[i] != '.')
                            num_temp += ((num_stack[i] - '0') * j);

                        if (num_stack[i] == '.') {
                            for (k = num_stack_index - i; k > 0; k--) {
                                num_temp /= 10;
                            }
                            j = 0.1;//下一次j还是1
                        }
                    }
                    opnd[opnd_index] = num_temp;
                    opnd_index++;
                    num_temp = 0;//临时数初始化
                    num_stack_index = 0;//临时栈下标初始化
                }
                //c1:
                //存 + 的一些操作
                if (optr[optr_index] == 0)
                {//如果optr为空,直接进入，之后直到到最终计算之前都不会为空了
                    optr[optr_index] = '+';//先不++，因为下面是判断上一个入栈负号
                }//如果optr不为空，+ 是 直接抽出里面的进行运算，再存
                else if (optr[optr_index] == '+')
                {//左提俩求和=新数，将新数存进opnd,+号不错废掉
                    opnd_index -= 2;//减去多加的 和 二合一之后位置为下面那个
                    temp = opnd[opnd_index] + opnd[opnd_index + 1];
                    opnd[opnd_index] = temp;
                    opnd_index++;//

                    optr[optr_index] = '+';//直接替换掉原来下标为这个的符号
                }
                else if (optr[optr_index] == '-')
                {
                    opnd_index -= 2;
                    temp = opnd[opnd_index] - opnd[opnd_index + 1];
                    opnd[opnd_index] = temp;
                    opnd_index++;

                    optr[optr_index] = '+';//直接替换掉原来下标为这个的符号
                }
                else if (optr[optr_index] == '*')
                {//+遇到
                    opnd_index -= 2;
                    temp = opnd[opnd_index] * opnd[opnd_index + 1];
                    opnd[opnd_index] = temp;
                    opnd_index++;
                    while (true)
                    {//处理 6+8-4/2+6-10 的 bug
                        if (optr_index - 1 < 0)//防止数组越界
                            break;
                        if (optr[optr_index - 1] == '+' || optr[optr_index - 1] == '-')
                        {
                            optr_index--;
                            opnd_index -= 2;
                            switch (optr[optr_index])
                            {
                                case '+': opnd[opnd_index] = opnd[opnd_index] + opnd[opnd_index + 1]; break;
                                case '-': opnd[opnd_index] = opnd[opnd_index] - opnd[opnd_index + 1]; break;
                            }
                            opnd_index++;
                        }
                        break;
                    }
                    optr[optr_index] = '+';//直接替换掉原来下标为这个的符号
                }
                else if (optr[optr_index] == '/')
                {
                    opnd_index -= 2;
                    temp = opnd[opnd_index] / opnd[opnd_index + 1];
                    opnd[opnd_index] = temp;
                    opnd_index++;

                    while (true)
                    {//处理 6+8-4/2+6-10 的 bug
                        if (optr_index - 1 < 0)//防止数组越界
                            break;
                        if (optr[optr_index - 1] == '+' || optr[optr_index - 1] == '-')
                        {
                            optr_index--;
                            opnd_index -= 2;
                            switch (optr[optr_index])
                            {
                                case '+': opnd[opnd_index] = opnd[opnd_index] + opnd[opnd_index + 1]; break;
                                case '-': opnd[opnd_index] = opnd[opnd_index] - opnd[opnd_index + 1]; break;
                            }
                            opnd_index++;
                        }
                        break;
                    }
                    optr[optr_index] = '+';//直接替换掉原来下标为这个的符号
                }
                else if (optr[optr_index] == '(')
                {//如果是 ( 直接存进去
                    if (CalByButton_r == 1)
                    {//这个情况是 由 ) 计算后 的 (
                        if ((optr_index - 1) >= 0)
                        {//处理 6*(1-2*2)+3会=0 的问题 存之前应该再判断一下 原 ( 下的符号*/
                            optr_index--;
                            opnd_index -= 2;
                            switch (optr[optr_index])
                            { //这是运算符栈顶符号，此时 ) 没存进去
                                case '+': opnd[opnd_index] = opnd[opnd_index] + opnd[opnd_index + 1]; break;
                                case '-': opnd[opnd_index] = opnd[opnd_index] - opnd[opnd_index + 1]; break;
                                case '*': opnd[opnd_index] = opnd[opnd_index] * opnd[opnd_index + 1]; break;
                                case '/': opnd[opnd_index] = opnd[opnd_index] / opnd[opnd_index + 1]; break;
                                //下面这个case是处理 ( 下还是 ( 的情况
                                case '(': optr_index++; opnd_index++; break;//只需将optr_index和opnd_index归位即可
                            }
                            opnd_index++;
                            optr[optr_index] = '+';
                            CalByButton_r = 0;//归位
                            while (true)
                            {
                                if (optr_index - 1 < 0)//防止数组越界
                                    break;
                                if (optr[optr_index - 1] == '+' || optr[optr_index - 1] == '-')
                                {
                                    optr_index--;
                                    opnd_index -= 2;
                                    switch (optr[optr_index])
                                    {
                                        case '+': opnd[opnd_index] = opnd[opnd_index] + opnd[opnd_index + 1]; break;
                                        case '-': opnd[opnd_index] = opnd[opnd_index] - opnd[opnd_index + 1]; break;
                                    }
                                    opnd_index++;
                                }
                                optr[optr_index] = '+';
                                break;
                            }
                            chars_index++;
                            continue;
                        }
                        optr[optr_index] = '+';
                        CalByButton_r = 0;//归位
                        chars_index++;
                        continue;
                    }
                    optr_index++;
                    optr[optr_index] = '+';
                }
                chars_index++;
                continue;
            }
            //chars[chars_index]是 - 情况，和 + 一样  负号处理在 符号(中
            if (chars[chars_index] == '-')
            {
                num_stack_index--;
                if (num_stack_index == -1)
                {// ) 已经提取一遍左面了，) 后的运算符 就不用再提左面了
                    num_stack_index = 0;
                    //goto c2;
                }else {//将-前一个数抽出存入opnd，num_temp是从字符串中成功抽取的一个数,并且已经变成double
                    for (i = num_stack_index, j = 1; i >= 0; i--, j *= 10) {
                        if (num_stack[i] != '.')
                            num_temp += ((num_stack[i] - '0') * j);

                        if (num_stack[i] == '.') {
                            for (k = num_stack_index - i; k > 0; k--) {
                                num_temp /= 10;
                            }
                            j = 0.1;//下一次j还是1
                        }
                    }
                    opnd[opnd_index] = num_temp;
                    opnd_index++;
                    num_temp = 0;//临时数初始化
                    num_stack_index = 0;//临时栈下标初始化
                }
                //c2:
                //存 - 的一些操作
                if (optr[optr_index] == 0)
                {//如果optr为空,直接进入
                    optr[optr_index] = '-';//先不++，因为下面是判断上一个入栈负号
                }//如果optr不为空，+ 是 直接抽出里面的进行运算，再存
                else if (optr[optr_index] == '+')
                {//左提俩求和=新数，将新数存进opnd,+号不错废掉
                    opnd_index -= 2;//减去多加的 和 二合一之后位置为下面那个
                    temp = opnd[opnd_index] + opnd[opnd_index + 1];
                    opnd[opnd_index] = temp;
                    opnd_index++;//

                    optr[optr_index] = '-';//直接替换掉原来下标为这个的符号
                }
                else if (optr[optr_index] == '-')
                {
                    opnd_index -= 2;
                    temp = opnd[opnd_index] - opnd[opnd_index + 1];
                    opnd[opnd_index] = temp;
                    opnd_index++;

                    optr[optr_index] = '-';//直接替换掉原来下标为这个的符号
                }
                else if (optr[optr_index] == '*')
                {//+遇到
                    opnd_index -= 2;
                    temp = opnd[opnd_index] * opnd[opnd_index + 1];
                    opnd[opnd_index] = temp;
                    opnd_index++;
                    while (true)
                    {//处理 6+8-4/2+6-10 的 bug
                        if (optr_index - 1 < 0)//防止数组越界
                            break;
                        if (optr[optr_index - 1] == '+' || optr[optr_index - 1] == '-')
                        {
                            optr_index--;
                            opnd_index -= 2;
                            switch (optr[optr_index])
                            {
                                case '+': opnd[opnd_index] = opnd[opnd_index] + opnd[opnd_index + 1]; break;
                                case '-': opnd[opnd_index] = opnd[opnd_index] - opnd[opnd_index + 1]; break;
                            }
                            opnd_index++;
                        }
                        break;
                    }
                    optr[optr_index] = '-';//直接替换掉原来下标为这个的符号
                }
                else if (optr[optr_index] == '/')
                {
                    opnd_index -= 2;
                    temp = opnd[opnd_index] / opnd[opnd_index + 1];
                    opnd[opnd_index] = temp;
                    opnd_index++;
                    while (true)
                    {//处理 6+8-4/2+6-10 的 bug
                        if (optr_index - 1 < 0)//防止数组越界
                            break;
                        if (optr[optr_index - 1] == '+' || optr[optr_index - 1] == '-')
                        {
                            optr_index--;
                            opnd_index -= 2;
                            switch (optr[optr_index])
                            {
                                case '+': opnd[opnd_index] = opnd[opnd_index] + opnd[opnd_index + 1]; break;
                                case '-': opnd[opnd_index] = opnd[opnd_index] - opnd[opnd_index + 1]; break;
                            }
                            opnd_index++;
                        }
                        break;
                    }
                    optr[optr_index] = '-';//直接替换掉原来下标为这个的符号
                }
                else if (optr[optr_index] == '(')
                {//如果是 ( 直接存进去
                    if (CalByButton_r == 1)
                    {//这个情况是 由 ) 计算后 的 ( 遗留
                        if ((optr_index - 1) >= 0)
                        {//处理 6*(1-2*2)+3会=0 的问题 /*存之前应该再判断一下 原 ( 下的符号*/
                            optr_index--;
                            opnd_index -= 2;
                            switch (optr[optr_index])
                            { //这是运算符栈顶符号，此时 ) 没存进去
                                case '+': opnd[opnd_index] = opnd[opnd_index] + opnd[opnd_index + 1]; break;
                                case '-': opnd[opnd_index] = opnd[opnd_index] - opnd[opnd_index + 1]; break;
                                case '*': opnd[opnd_index] = opnd[opnd_index] * opnd[opnd_index + 1]; break;
                                case '/': opnd[opnd_index] = opnd[opnd_index] / opnd[opnd_index + 1]; break;
                                //下面这个case是处理 ( 下还是 ( 的情况
                                case '(': optr_index++; opnd_index++; break;//只需将optr_index和opnd_index归位即可
                            }
                            opnd_index++;
                            optr[optr_index] = '-';
                            CalByButton_r = 0;//归位
                            chars_index++;
                            continue;
                        }
                        optr[optr_index] = '-';
                        CalByButton_r = 0;//归位
                        while (true)
                        {
                            if (optr_index - 1 < 0)//防止数组越界
                                break;
                            if (optr[optr_index - 1] == '+' || optr[optr_index - 1] == '-')
                            {
                                optr_index--;
                                opnd_index -= 2;
                                switch (optr[optr_index])
                                {
                                    case '+': opnd[opnd_index] = opnd[opnd_index] + opnd[opnd_index + 1]; break;
                                    case '-': opnd[opnd_index] = opnd[opnd_index] - opnd[opnd_index + 1]; break;
                                }
                                opnd_index++;
                            }
                            optr[optr_index] = '-';
                            break;
                        }
                        chars_index++;
                        continue;
                    }
                    optr_index++;
                    optr[optr_index] = '-';
                }
                chars_index++;
                continue;
            }
            //chars[chars_index]是 × 情况
            if (chars[chars_index] == '*')
            {
                num_stack_index--;
                if (num_stack_index == -1)
                {// ) 已经提取一遍左面了，) 后的运算符 就不用再提左面了
                    num_stack_index = 0;
                    //goto c3;
                }else {//将*前一个数抽出存入opnd，num_temp是从字符串中成功抽取的一个数,并且已经变成double
                    for (i = num_stack_index, j = 1; i >= 0; i--, j *= 10) {
                        if (num_stack[i] != '.')
                            num_temp += ((num_stack[i] - '0') * j);

                        if (num_stack[i] == '.') {
                            for (k = num_stack_index - i; k > 0; k--) {
                                num_temp /= 10;
                            }
                            j = 0.1;//下一次j还是1
                        }
                    }
                    opnd[opnd_index] = num_temp;
                    opnd_index++;
                    num_temp = 0;//临时数初始化
                    num_stack_index = 0;//临时栈下标初始化
                }
                //存 * 的一些操作
                //c3:
                if (optr[optr_index] == 0)
                {//如果optr为空,直接进入
                    optr[optr_index] = '*';//先不++，因为下面是判断上一个入栈负号
                }//如果optr不为空
                else if (optr[optr_index] == '+')
                {//遇到+号不运算直接存进去
                    optr_index++;
                    optr[optr_index] = '*';
                }
                else if (optr[optr_index] == '-')
                {
                    optr_index++;
                    optr[optr_index] = '*';
                }
                else if (optr[optr_index] == '*')
                {//*遇到*,左提俩=新数，新数放入,*放入原来*位置,也就是不用操作
                    opnd_index -= 2;
                    temp = opnd[opnd_index] * opnd[opnd_index + 1];
                    opnd[opnd_index] = temp;
                    opnd_index++;

                }
                else if (optr[optr_index] == '/')
                {
                    opnd_index -= 2;
                    temp = opnd[opnd_index] / opnd[opnd_index + 1];
                    opnd[opnd_index] = temp;
                    opnd_index++;

                    optr[optr_index] = '*';//*替换/的位置
                }
                else if (optr[optr_index] == '(')
                {//如果是 ( 直接存进去
                    if (CalByButton_r == 1)
                    {//这个情况是 由 ) 计算后 的 (
                        if ((optr_index - 1) >= 0)
                        {//处理 6*(1-2*2)+3会=0 的问题 /*存之前应该再判断一下 原 ( 下的符号*/
                            if (optr[optr_index - 1] != '+' && optr[optr_index - 1] != '-')
                            {//处理6-(5+2)*6 *入之前 把 - 号给算了
                                optr_index--;
                                opnd_index -= 2;
                                switch (optr[optr_index])
                                { //这是运算符栈顶符号，此时 ) 没存进去
                                    case '+': opnd[opnd_index] = opnd[opnd_index] + opnd[opnd_index + 1]; break;
                                    case '-': opnd[opnd_index] = opnd[opnd_index] - opnd[opnd_index + 1]; break;
                                    case '*': opnd[opnd_index] = opnd[opnd_index] * opnd[opnd_index + 1]; break;
                                    case '/': opnd[opnd_index] = opnd[opnd_index] / opnd[opnd_index + 1]; break;
                                    //下面这个case是处理 ( 下还是 ( 的情况
                                    case '(': optr_index++; opnd_index++; break;//只需将optr_index和opnd_index归位即可
                                }
                                opnd_index++;
                                optr[optr_index] = '*';
                                CalByButton_r = 0;//归位
                                chars_index++;
                                continue;
                            }
                        }
                        optr[optr_index] = '*';
                        CalByButton_r = 0;//归位
                        chars_index++;
                        continue;
                    }
                    optr_index++;
                    optr[optr_index] = '*';
                }
                chars_index++;
                continue;
            }
            //chars[chars_index]是 / 情况
            if (chars[chars_index] == '/')
            {
                num_stack_index--;
                if (num_stack_index == -1)
                {// ) 已经提取一遍左面了，) 后的运算符 就不用再提左面了
                    num_stack_index = 0;
                    //goto c4;
                }else {//将*前一个数抽出存入opnd，num_temp是从字符串中成功抽取的一个数,并且已经变成double
                    for (i = num_stack_index, j = 1; i >= 0; i--, j *= 10) {
                        if (num_stack[i] != '.')
                            num_temp += ((num_stack[i] - '0') * j);

                        if (num_stack[i] == '.') {
                            for (k = num_stack_index - i; k > 0; k--) {
                                num_temp /= 10;
                            }
                            j = 0.1;//下一次j还是1
                        }
                    }
                    opnd[opnd_index] = num_temp;
                    opnd_index++;
                    num_temp = 0;//临时数初始化
                    num_stack_index = 0;//临时栈下标初始化
                }
                //c4:
                //存 / 的一些操作
                if (optr[optr_index] == 0)
                {//如果optr为空,直接进入
                    optr[optr_index] = '/';//先不++，因为下面是判断上一个入栈负号
                }//如果optr不为空
                else if (optr[optr_index] == '+')
                {//遇到+号不运算直接存进去
                    optr_index++;
                    optr[optr_index] = '/';
                }
                else if (optr[optr_index] == '-')
                {
                    optr_index++;
                    optr[optr_index] = '/';
                }
                else if (optr[optr_index] == '*')
                {
                    opnd_index -= 2;
                    temp = opnd[opnd_index] * opnd[opnd_index + 1];
                    opnd[opnd_index] = temp;
                    opnd_index++;

                    optr[optr_index] = '/';// /替换*的位置
                }
                else if (optr[optr_index] == '/')
                {// /遇到/,左提俩=新数，新数放入,/放入原来/位置,也就是不用操作
                    opnd_index -= 2;
                    temp = opnd[opnd_index] / opnd[opnd_index + 1];
                    opnd[opnd_index] = temp;
                    opnd_index++;
                }
                else if (optr[optr_index] == '(')
                {//如果是 ( 直接存进去
                    if (CalByButton_r == 1)
                    {//这个情况是 由 ) 计算后 的 (
                        if ((optr_index - 1) >= 0)
                        {//处理 6*(1-2*2)+3会=0 的问题 /*存之前应该再判断一下 原 ( 下的符号*/
                            if (optr[optr_index - 1] != '+' && optr[optr_index - 1] != '-')
                            {//处理6-(5+2)/6 /入之前 把 - 号给算了
                                optr_index--;
                                opnd_index -= 2;
                                switch (optr[optr_index])
                                { //这是运算符栈顶符号，此时 ) 没存进去
                                    case '+': opnd[opnd_index] = opnd[opnd_index] + opnd[opnd_index + 1]; break;
                                    case '-': opnd[opnd_index] = opnd[opnd_index] - opnd[opnd_index + 1]; break;
                                    case '*': opnd[opnd_index] = opnd[opnd_index] * opnd[opnd_index + 1]; break;
                                    case '/': opnd[opnd_index] = opnd[opnd_index] / opnd[opnd_index + 1]; break;
                                    //下面这个case是处理 ( 下还是 ( 的情况
                                    case '(': optr_index++; opnd_index++; break;//只需将optr_index和opnd_index归位即可
                                }
                                opnd_index++;
                                optr[optr_index] = '/';
                                CalByButton_r = 0;//归位
                                chars_index++;
                                continue;
                            }
                        }
                        optr[optr_index] = '/';
                        CalByButton_r = 0;//归位
                        chars_index++;
                        continue;
                    }
                    optr_index++;
                    optr[optr_index] = '/';
                }
                chars_index++;
                continue;
            }
            //是 ( 情况
            if (chars[chars_index] == '(')
            {//直接入操作符栈
                if (optr_index == 0)
                {
                    if (optr[0] != '+' && optr[0] != '-' && optr[0] != '*' && optr[0] != '/' && optr[0] != '(')
                    {
                        optr[optr_index] = '(';
                        chars_index++;
                        continue;
                    }
                }
                optr_index++;
                optr[optr_index] = '(';
                chars_index++;
                continue;
            }
            //是 ) 情况
            if (chars[chars_index] == ')')
            {
                num_stack_index--;
                //将*前一个数抽出存入opnd，num_temp是从字符串中成功抽取的一个数,并且已经变成double
                for (i = num_stack_index, j = 1; i >= 0; i--, j *= 10)
                {
                    if (num_stack[i] != '.')
                        num_temp += ((num_stack[i] - '0') * j);

                    if (num_stack[i] == '.')
                    {
                        for (k = num_stack_index - i; k > 0; k--)
                        {
                            num_temp /= 10;
                        }
                        j = 0.1;//下一次j还是1
                    }
                }
                opnd[opnd_index] = num_temp;
                opnd_index++;
                num_temp = 0;//临时数初始化
                num_stack_index = 0;//临时栈下标初始化

                while (true)
                {//用while是处理 这种情况 (1-5*2) 也就是括号内的多项式
                    opnd_index -= 2;
                    switch (optr[optr_index])
                    { //这是运算符栈顶符号，此时 ) 没存进去
                        case '+': opnd[opnd_index] = opnd[opnd_index] + opnd[opnd_index + 1]; break;
                        case '-': opnd[opnd_index] = opnd[opnd_index] - opnd[opnd_index + 1]; break;
                        case '*': opnd[opnd_index] = opnd[opnd_index] * opnd[opnd_index + 1]; break;
                        case '/': opnd[opnd_index] = opnd[opnd_index] / opnd[opnd_index + 1]; break;
                    }
                    opnd_index++;
                    optr_index--;//运算符栈下降一位
                    if (optr[optr_index] == '(')
                    {
                        CalByButton_r = 1; //上面有说明
                        break;
                    }

                }

            }

            chars_index++;//指针在最后时刻移动
        }

        ///////////////////////////////////////////
        num_stack_index--;
        //将+前一个数抽出存入opnd，num_temp是从字符串中成功抽取的一个数,并且已经变成double
        for (i = num_stack_index, j = 1; i >= 0; i--, j *= 10)
        {
            if (num_stack[i] != '.')
                num_temp += ((num_stack[i] - '0') * j);

            if (num_stack[i] == '.')
            {
                for (k = num_stack_index - i; k > 0; k--)
                {
                    num_temp /= 10;
                }
                j = 0.1;//下一次j还是1
            }
        }
        opnd[opnd_index] = num_temp;
        opnd_index++;
        num_temp = 0;//临时数初始化
        num_stack_index = 0;//临时栈下标初始化

        i = optr_index;
        do
        {
            switch (optr[i])
            {
                case '+': opnd[i] = opnd[i] + opnd[i + 1]; break;
                case '-': opnd[i] = opnd[i] - opnd[i + 1]; break;
                case '*': opnd[i] = opnd[i] * opnd[i + 1]; break;
                case '/': opnd[i] = opnd[i] / opnd[i + 1]; break;
            }
            i--;
        } while (i != -1);
        result = opnd[0];
        return result;
    }

}
