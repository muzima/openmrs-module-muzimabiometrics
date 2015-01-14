package org.muzima.applet;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.applet.*;

public class SimpleApplet extends JApplet implements ActionListener {
	String msg=" ";
	int v1,v2,result;
	TextField t1;
	Button b[]=new Button[10];
	Button fingerPrintButton;
	char OP;
	public void init()
	{
		Color k=new Color(120,89,90);
		setBackground(k);
		fingerPrintButton=new Button("Scan");
		add(fingerPrintButton);
		fingerPrintButton.addActionListener(this);
		
	}
 
	public void actionPerformed(ActionEvent ae)
	{
		String str=ae.getActionCommand();
		if(str.equals("Scan"))
		{
			//Call the function
		}
	
	}
}
