package com.umpay;

import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Image;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
/*
 * AWT之FileDialog简单应用
 */
public class ImgViewer extends JFrame {
	MenuBar mb = new MenuBar();
	Menu m1 = new Menu("File");
	Menu m2 = new Menu("Edit");
	Menu m3 = new Menu("Help");
	MenuItem mi1 = new MenuItem("Save");
	MenuItem mi2 = new MenuItem("open");
	MenuItem mi3 = new MenuItem("Quit");
	JLabel jLabel = new JLabel();
	
	FileDialog fd = new FileDialog(this);				//文件对话框
	
	TextArea ta = new TextArea();
	
	private ActionListener al = new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource() == mi1){
				fd.setMode(FileDialog.SAVE);
				fd.setTitle("保存");
				fd.setVisible(true);
				saveFile();
			} else {
				fd.setMode(FileDialog.LOAD);
				fd.setTitle("打开");
				fd.setVisible(true);
				ta.setText("");
				showImage(null);
			}
		}
	};
	
	ImgViewer() {
		mb.add(m1);
		mb.add(m2);
		mb.setHelpMenu(m3);
		mi1.addActionListener(al);
		mi2.addActionListener(al);
//		m1.add(mi1);
		m1.add(mi2);
		m1.addSeparator();
		m1.add(mi3);
		
		setMenuBar(mb);
//		add(ta);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);								//窗口关闭
			}
		});
		setSize(500, 500);
		setLocation(400,200);
//		setUndecorated(true);
//		com.sun.awt.AWTUtilities.setWindowOpaque(this, false);
	}
	
	//读取文件
	private void openFile(){
		String filename = fd.getDirectory() + fd.getFile();
		
		try {
			BufferedReader in = new BufferedReader(new FileReader(new File(filename).getAbsoluteFile()));
			try {
				String str;
				while((str = in.readLine())!= null) {
					ta.append(str);
					ta.append("/n");
				}
			} finally {
				in.close();
			}
		} catch (IOException e) {
			System.out.println("文件没找到！");
		}
	}
	
	private void showImage(String file){
		String path=null;
		if(file!=null)
			path=file;
		else
			path = fd.getDirectory() + fd.getFile();
		ImageIcon imgIcon = new ImageIcon(path);
		jLabel.setIcon(null);
		jLabel = new JLabel(imgIcon);
		
		getContentPane().setLayout(null);
		getContentPane().add(jLabel);
		
		int width=imgIcon.getIconWidth(),height=imgIcon.getIconHeight();
		Dimension dms = Toolkit.getDefaultToolkit().getScreenSize();
		if(imgIcon.getIconWidth() > dms.getWidth()||imgIcon.getIconHeight() > dms.getHeight()){
			double rateWidth = dms.getWidth()/imgIcon.getIconWidth();
			double rateHeight = dms.getHeight()/imgIcon.getIconHeight();
			if(rateWidth < rateHeight){
				width = (int)dms.getWidth();
				height = (int)(dms.getHeight()*rateWidth);
				setLocation(0, (int)((dms.getHeight()-(dms.getHeight()*rateWidth))/2));
			}else{
				width = (int)(dms.getWidth()*rateHeight);
				height = (int)dms.getHeight();
				setLocation((int)((dms.getWidth()-(dms.getWidth()*rateHeight))/2), 0);
			}
		}else{
			setLocation((int)((dms.getWidth()-imgIcon.getIconWidth())/2), (int)((dms.getHeight()-imgIcon.getIconHeight())/2));
		}
		
		jLabel.setBounds(0, 0, width, height);
		imgIcon.setImage(imgIcon.getImage().getScaledInstance(width, height, Image.SCALE_DEFAULT));
		setSize(width, height+20);
		setVisible(true);
	}
	
	//保存文件
	private void saveFile() {
		String filepath = fd.getDirectory() + fd.getFile();
		PrintWriter out;
		try {
			out = new PrintWriter(new File(filepath).getAbsoluteFile());
			out.print(ta.getText());
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		ImgViewer viewer = new ImgViewer();
		viewer.setVisible(true);
		if(args.length!=0){
			viewer.showImage(args[0]);
		}
	}
}
