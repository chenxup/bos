package cn.itcast.bos.test;

import org.junit.Test;

import java.util.Scanner;

public class TestDemo {

    @Test
    public void test1() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("请输入：");
        int a = scanner.nextInt();
        System.out.println(a);


    }

}
