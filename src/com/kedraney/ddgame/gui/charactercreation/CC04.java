package com.kedraney.ddgame.gui.charactercreation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.math.*;

//6/10/17 - This window is the fourth character creeation menu, it lets the player choose their feats
//IT MUST READ FEATS FROM XML FILE!

public class CC04 extends JFrame
{
	private Container parent;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	
	public CC04()
	{
		super("Feats");
		parent = getContentPane();
		layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		parent.setLayout(layout);
		
		setSize(240, 200);
		setVisible(true);
	}
	
	private void addComponent(Component component, int row, int column, int width, int height)
	{
		constraints.gridx = column;
		constraints.gridy = row;

		constraints.gridwidth = width;
		constraints.gridheight = height;

		layout.setConstraints(component, constraints);
		parent.add(component);
	}
	
	public static void main(String[] args)
	{
		CC04 test = new CC04();
	}
}