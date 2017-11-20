package com.kedraney.ddgame.gui.charactercreation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.math.*;

//6/10/17 - This window is the first character creeation menu, it lets the player choose their ability scores

public class CC01 extends JFrame
{
	private Container parent;
	private GridBagLayout layout;
	private GridBagConstraints constraints;

	private int[] rolls = {10, 10, 10, 10, 10, 10};
	private String[] abils = {"Dex = ", "Str = ", "Con = ", "Int = ", "Wis = ", "Cha = "};
	private JButton[] abilMinus = {new JButton("-"),new JButton("-"),new JButton("-"),new JButton("-"),new JButton("-"),new JButton("-")};
	private JButton[] abilPlus = {new JButton("+"),new JButton("+"),new JButton("+"),new JButton("+"),new JButton("+"),new JButton("+")};
	private JTextField[] score = {new JTextField(rolls[0] + ""),new JTextField(rolls[1] + ""),new JTextField(rolls[2] + ""),new JTextField(rolls[3] + ""),new JTextField(rolls[4] + ""),new JTextField(rolls[5] + "")};
	
	private JTextField[] mods = new JTextField[6];//holds mods for the skills
	private JButton submit = new JButton("Update Mods");
//6/11/2017 - replaced submit with update mods so if you type in a score it will update the mods

//	private String[] options = {"--", "9", "10", "11", "12", "13", "14"};//set as 10 once done debugging
//	private String[] lastOption = {"--", "--", "--", "--", "--", "--"};
//	private String[] lastOption = new String[6];
	private final int[] hardLined = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
	
	public CC01()
	{
		super("Ability Scores");
		parent = getContentPane();
		layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		parent.setLayout(layout);
		
		addComponent(new JLabel("Ability" + ""), 0, 0, 2, 1);
		addComponent(new JLabel(""), 0, 1, 2, 1);
		addComponent(new JLabel("-1"), 0, 2, 2, 1);
		addComponent(new JLabel(""), 0, 3, 2, 1);
		addComponent(new JLabel("Score"), 0, 4, 2, 1);
		addComponent(new JLabel(""), 0, 5, 2, 1);
		addComponent(new JLabel("+1"), 0, 6, 2, 1);
		addComponent(new JLabel(""), 0, 7, 1, 1);
		addComponent(new JLabel("Mod"), 0, 8, 1, 1);
		
		for(int y = 1; y < 7; y++)
		{
			mods[y-1] = new JTextField("+0");
			mods[y-1].setEditable(false);
			
			addComponent(new JLabel("  " + abils[y-1] + "       "), y, 0, 2, 1);
			addComponent(abilMinus[y-1], y, 2, 1, 1);
			addComponent(new JLabel("    "), y, 3, 1, 1);
			addComponent(score[y-1], y, 4, 1, 1);
			addComponent(new JLabel("    "), y, 5, 1, 1);
			addComponent(abilPlus[y-1], y, 6, 1, 1);
			addComponent(new JLabel("    "), y, 7, 1, 1);
			addComponent(mods[y-1], y, 8, 1, 1);
			
			mods[y-1].setText("+0");
			
			addListenerPlus(y-1);
			addListenerMinus(y-1);
			addListenerCheck(y-1);
		}
		

		//Above bit gets buttons working - now for submit
		
		
		submit.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				for(int x = 0; x < 6; x++)
				{
					rolls[x] = Integer.parseInt(score[x].getText());
					resetMod(x);
				}
				
//				System.out.println(rolls[0] + "" + rolls[1] + "" + rolls[2] + "" + rolls[3] + "" + rolls[4] + "" + rolls[5]);
			}
		});
		
		addComponent(new JLabel("          "), 7, 0, 7, 1);
		addComponent(submit, 8, 0, 7, 1);
		setSize(250, 280);
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
	
	private void addListenerCheck(int x)
	{
		score[x].addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				resetMod(x);
			}
		});
	}
	
	private void resetMod(int x)
	{
		int t = ((int)((rolls[x]-10)/2));
		
		if(t < 0)
			mods[x].setText("" + t);
		else
			mods[x].setText("+" + t);
	}
	
	private void addListenerPlus(int x)
	{
		abilPlus[x].addActionListener(new ActionListener()
		{
			final int num = hardLined[x];
			public void actionPerformed(ActionEvent e)
			{
				rolls[num]++;
				score[num].setText(rolls[num] + "");
				
				if(rolls[num] == 19)
				{
					JOptionPane.showMessageDialog(null, "Abilities can't be greater than 18. Achievements will be disabled.", "CHEATING", JOptionPane.WARNING_MESSAGE);
				}
				
				resetMod(x);
			}
		});
	}
	
	private void addListenerMinus(int x)
	{
		abilMinus[x].addActionListener(new ActionListener()
		{
			final int num = hardLined[x];
			public void actionPerformed(ActionEvent e)
			{
				if(rolls[num] > 3)
				{
					rolls[num]--;
					score[num].setText(rolls[num] + "");
				}
				else
					JOptionPane.showMessageDialog(null, "Abilities can't be less than 3.", "Foolish Error", JOptionPane.ERROR_MESSAGE);
					
				resetMod(x);
			}
		});
	}
	
	public int[] getScores()
	{
		updateRolls();
		return rolls;
	}
	
	public static void main(String[] args)
	{
		CC01 test = new CC01();
		test.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	private void updateRolls()
	{
		for(int x = 0; x < 6; x++)
		{
			rolls[x] = Integer.parseInt(score[x].getText());
			if(rolls[x] < 3)
			{
				rolls[x] = 3;
				score[x].setText("3");
				JOptionPane.showMessageDialog(null, abils[x].substring(0,3) + " was under 3, and therefore reset to 3.", "\"Dumb Much\" Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}