package com.umpay.animal;

import java.io.DataInputStream;
import java.util.Scanner;

public class Bouns {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		System.out.print("输入利润:");
		Scanner sc = new Scanner(System.in);
		double profit = sc.nextDouble();
		sc.close();
		System.out.println("奖金:"+Bouns.bouns(profit));
	}
	
	public static double bouns(double profit){
		double bouns;
		if(profit<=100000){
			bouns = profit * 0.1;
			return bouns;
		}else{
			bouns = 100000 * 0.1;
		}
		if(profit<=200000){
			bouns += (profit - 100000)  * 0.075;
			return bouns;
		}else{
			bouns += (200000 - 100000) * 0.075;
		}
		if(profit<=400000){
			bouns += (profit - 200000) * 0.05;
			return bouns;
		}else{
			bouns += (400000 - 200000) * 0.05;
		}
		if(profit<=600000){
			bouns += (profit - 400000) * 0.03;
			return bouns;
		}else{
			bouns += (600000 - 400000) * 0.03;
		}
		if(profit<=1000000){
			bouns += (profit - 600000) * 0.015;
			return bouns;
		}else{
			bouns += (1000000 - 600000) * 0.015;
		}

		bouns += (profit - 1000000) * 0.01;
			
		return bouns;
	}
}
