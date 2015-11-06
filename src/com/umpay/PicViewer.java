package com.umpay;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class PicViewer extends JFrame {
	JLabel jLabel = null;
	ImageIcon imgIcon = null;
	JFileChooser jfc = null;
	MenuBar menuBar = null;
	
	public PicViewer() {
		String img = "/Users/northwind/Downloads/4fa519385343fbf28ec0036eb67eca8067388f8c.jpg";
		imgIcon = new ImageIcon(img);
		
		jLabel = new JLabel(imgIcon);
		jLabel.setBounds(0, 0, imgIcon.getIconWidth(), imgIcon.getIconHeight());
		getContentPane().setLayout(null);
		getContentPane().add(jLabel);
		setSize(imgIcon.getIconWidth(), imgIcon.getIconHeight());
		setVisible(true);
		setLocation(100, 100);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		menuBar = new MenuBar();
		MenuItem open=new MenuItem("open");
		
        ActionListener menuListener = new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                String cmd = e.getActionCommand();  
                System.out.println("click " + cmd + " menu" + "\n");
                if (cmd.equals("exit")) {  
                    System.exit(0);  
                }else if(cmd.equals("open")){
//                	FileDialog fd = new FileDialog();
                }
            }  
        };  
		
		open.addActionListener(menuListener);
		Menu file = new Menu("file");
		file.add(open);
		menuBar.add(file);
		setMenuBar(menuBar);
	}

	public static void main(String[] args) throws Exception{
		PicViewer pv = new PicViewer();
	}
}