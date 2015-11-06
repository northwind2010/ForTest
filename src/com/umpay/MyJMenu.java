package com.umpay;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JLabel;
public class MyJMenu
{
	Frame myFrame;
	MenuBar menubar;
	Menu file,edit,editSon,about;
	MenuItem open,save,line,exit,
			copy,pause,
				cut,put,
			author,help;
	JLabel label = new JLabel();
	public MyJMenu()
	{
		myFrame=new Frame("NORTHWIND-1");
		myFrame.setBounds(400,400,300,300);
		
		//添加关闭事件
		myFrame.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});
		//初始菜单项
		menubar=new MenuBar();
		file=new Menu("file");
		edit=new Menu("edit");
		editSon=new Menu("二级菜单");
		about=new Menu("about");
		
		open=new MenuItem("open");
		save=new MenuItem("save");
		line=new MenuItem("-");
		exit=new MenuItem("exit");
		file.add(open);
		file.add(save);
		file.add(line);
		file.add(exit);
		menubar.add(file);
		copy=new MenuItem("copy");
		pause=new MenuItem("pause");
		edit.add(copy);
		edit.add(pause);
		cut=new MenuItem("cut");
		put=new MenuItem("put");
		editSon.add(cut);
		editSon.add(put);
		
		//Menu add Menu......!!
		edit.add(editSon);
		menubar.add(edit);
		author=new MenuItem("author");
		help=new MenuItem("help");
		about.add(author);
		about.add(help);
		menubar.add(about);
		//设置menuBar
		myFrame.setMenuBar(menubar);
		
		myFrame.setVisible(true);
	}
	public static void main(String[] args) //~!@
	{
		new MyJMenu();
	}
}