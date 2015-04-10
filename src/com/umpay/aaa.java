package com.umpay;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class aaa extends JFrame {
	JLabel n1 = null;
	ImageIcon img = null;

	public aaa() {
		String n2 = "/Users/northwind/Downloads/1.jpg";
		img = new ImageIcon(n2);
		n1 = new JLabel(img);
		n1.setBounds(0, 0, 200, 150);
		getContentPane().setLayout(null);
		getContentPane().add(n1);
		setSize(600, 500);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}

	public static void main(String[] args) throws Exception{
		aaa n2 = new aaa();
		for (int i = 0; i < 7; i++) {
			n2.n1.setBounds(i * 50, i * 50, 100, 100);
			if(i==6){
				i=0;
			}
			Thread.sleep(20);;
		}
	}

//	public static void delay(int n) {
//		for (int i = 0; i < n; i++) {
//
//			System.out.println("want todelay time 20ms");
//		}
//	}
}