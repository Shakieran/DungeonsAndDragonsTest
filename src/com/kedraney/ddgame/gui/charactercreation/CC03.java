package com.kedraney.ddgame.gui.charactercreation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.math.*;

//6/10/17 - This window is the third character creeation menu, it lets the player choose their skills
//dex = 0, str = 1, con = 2, int = 3, wis = 4, cha = 5
//So it works like this - shows the skill, the ability and it's modifier,
//the rranks in the skill as  well as plus/minus buttons on either side. Finally,
//a check box that has a check if it's a class skill - a seperate(small) window
//shows both skill points left and max ranks for a skill(cc and class skills)

public class CC03 extends JFrame
{
	private Container parent;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	//44 skills(45 with Speak Languages taken out)
	private String[] skills = {"Appraise", "Balance", "Bluff", "Climb", "Concentration", "Craft", "Decipher Script", "Diplomacy", "Disable Device", "Disguise", "Escape Artist", "Forgery", "Gather Information", "Handle Animal", "Heal", "Hide", "Intimidate", "Jump", "Knowledge(arcana)", "Knowledge(architecture and engineering)", "Knowledge(dungeoneering)", "Knowledge(geography)", "Knowledge(history)", "Knowledge(local)", "Knowledge(nature)", "Knowledge(nobility and royalty)", "Knowledge(religion)", "Knowledge(the planes)", "Listen", "Move Silently", "Open Lock", "Perform", "Profession", "Ride", "Search", "Sense Motive", "Sleight of Hand", "Spellcraft", "Spot", "Survival", "Swim", "Tumble", "Use Magic Device", "Use Rope"};
	private String[] abils = {"Int", "Dex", "Cha", "Str", "Con", "Int", "Int", "Cha", "Int", "Cha", "Dex", "Int", "Cha", "Cha", "Wis", "Dex", "Cha", "Str", "Int", "Int", "Int", "Int", "Int", "Int", "Int", "Int", "Int", "Int", "Wis", "Dex", "Dex", "Cha", "Wis", "Dex", "Int", "Wis", "Dex", "Int", "Wis", "Wis", "Str", "Dex", "Cha", "Dex"};
	private int[] abilsNum = {3, 0, 5, 1, 2, 3, 3, 5, 3, 5, 0, 3, 5, 5, 4, 0, 5, 1, 3, 3, 3, 3, 3, 3, 3, 3, 3, 3, 4, 0, 0, 5, 4, 0, 3, 4, 0, 3, 4, 4, 1, 0, 5, 0};
	private int[] mods = {0, 0, 0, 0, 0, 0};//modifiers for the rolls
	private int[] rolls = {100, 100, 100, 100, 100, 100};//the rolls that the player is using
	
	private CC03counter counter;//holds points left to spend
	
	private JTextField[] mod = new JTextField[44];
	private JButton[] minus = new JButton[44];
	private JTextField[] ranks = new JTextField[44];
	private int[] rankCounter = new int[44];
	private JButton[] plus = new JButton[44];
	private JCheckBox[] classSkill = new JCheckBox[44];
	private boolean[] classSkillCounter = new boolean[44];
	
	public CC03()
	{
		super("Skills");
		parent = getContentPane();
		layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		parent.setLayout(layout);
		
		counter = new CC03counter();
		
		addComponent(new JLabel("Skill:    "), 0, 0, 2, 1);
		addComponent(new JLabel("Ability:    "), 0, 2, 1, 1);
		addComponent(new JLabel("Mod:    "), 0, 3, 1, 1);
		addComponent(new JLabel("-1    "), 0, 4, 1, 1);
		addComponent(new JLabel("Ranks:    "), 0, 5, 1, 1);
		addComponent(new JLabel("+1    "), 0, 6, 1, 1);
		addComponent(new JLabel("Class Skill"), 0, 7, 1, 1);
		
		classSkillCounter[0] = true;
		
		for(int x = 0; x < 44; x++)
		{
			addComponent(new JLabel(skills[x]), x+1, 0, 2, 1);//Skill name
			addComponent(new JLabel(abils[x]), x+1, 2, 1, 1);//What ability?
			mod[x] = new JTextField("+000");			
//			if(mods[x] > -1)
//				mod[x].setText("+" + mods[x]);
				
			addComponent(mod[x], x+1, 3, 1, 1);//-3 is already -3, no need for + "-"
			minus[x] = new JButton("-");
			ranks[x] = new JTextField("0.0");
			plus[x] = new JButton("+");
			setUpListener(x);
			
			classSkill[x] = new JCheckBox();
			classSkill[x].setSelected(classSkillCounter[x]);//is a class skill? Yay or nay? ect.
			classSkill[x].setEnabled(false);
			
			addComponent(minus[x], x+1, 4, 1, 1);
			addComponent(ranks[x], x+1, 5, 1, 1);
			addComponent(plus[x], x+1, 6, 1, 1);
			
			addComponent(classSkill[x], x+1, 7, 1, 1);
		}
		
		update(rolls);
		for(int i = 0; i < 6; i++)
			rolls[i]=10;
			
		update(rolls);
		
		JScrollPane scroll = new JScrollPane(parent);
		setContentPane(scroll);
		setSize(600, 300);
		setVisible(true);
	}
	
	private void setUpListener(int x)
	{
		minus[x].addActionListener(new ActionListener()
		{
			final int num = x;
			public void actionPerformed(ActionEvent e)
			{
				if(rankCounter[x] > 0)
				{
					rankCounter[x]--;
					if(classSkillCounter[x])
						ranks[x].setText("" + (double)rankCounter[x]);
					else
						ranks[x].setText("" + (double)(rankCounter[x])/2);
						
					counter.addPoints();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Skills cannot have less than 0 ranks", "Dumb error", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
		
		plus[x].addActionListener(new ActionListener()
		{
			final int num = x;
			public void actionPerformed(ActionEvent e)
			{
				if(rankCounter[x] < 4 && counter.getPoints() > 0)
				{
					rankCounter[x]++;
					if(classSkillCounter[x])
						ranks[x].setText("" + (double)rankCounter[x]);
					else
						ranks[x].setText("" + (double)(rankCounter[x])/2);
						
					counter.minusPoints();
				}
				else if(counter.getPoints() > 0)
				{
					JOptionPane.showMessageDialog(null, "Skills cannot have over 4 ranks(2 if not a class skill)", "Dumb error", JOptionPane.ERROR_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "No points left", "Realy F*****g Obvious Error(DUMB)", JOptionPane.ERROR_MESSAGE);
				}
			}
		});
	}
	
	public void update(int[] demRolls)
	{
//		rolls = demRolls;		
		for(int x = 0; x < 6; x++)
		{
			rolls[x] = demRolls[x];
			mods[x] = ((int)((rolls[x]-10)/2));
		}
			
		for(int x = 0; x < 44; x++)
		{
			if(mods[abilsNum[x]] < 0)
				mod[x].setText("" + mods[abilsNum[x]]);
			else
				mod[x].setText("+" + mods[abilsNum[x]]);
				
			mod[x].setEditable(false);
		}
			
		counter.setPoints((4 + mods[3]) * 4);
		
		if(counter.getPoints() < 0)
			JOptionPane.showMessageDialog(null, "You've spent more skillpoints than you have. Achievements will be disabled.", "CHEATING", JOptionPane.WARNING_MESSAGE);
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
		CC03 test = new CC03();
	}
}

class CC03counter extends JFrame
{
	private Container parent;
	private BorderLayout layout;
	private int pointsLeft = 40;//Will deal with this later once tables and shit are internalized
	private JTextField ptsLeft = new JTextField("40");
	private int pointsTotal = 40;
	
	public CC03counter()
	{
		super("Skill Points");
		parent = getContentPane();
		layout = new BorderLayout();
		parent.setLayout(layout);
		
		add(new JLabel("Points Left:"), BorderLayout.WEST);
		
		ptsLeft.setEditable(false);
		
		add(ptsLeft, BorderLayout.CENTER);
		
		add(new JLabel("Max Rank: 4(Class), 2(CC)"), BorderLayout.SOUTH);
		
//		JScrollPane scroll = new JScrollPane(parent);
//		setContentPane( scroll );
		setSize(180, 70);
		setVisible(true);
	}
	
	protected int getPoints()
	{
		return pointsLeft;
	}
	
	protected void setPoints(int newTotal)
	{
		pointsLeft += newTotal - pointsTotal;//50 - 40 = 10
		pointsTotal = newTotal;
		ptsLeft.setText(pointsLeft + "");
	}
	
	protected void addPoints()
	{
		pointsLeft++;
		ptsLeft.setText("" + pointsLeft);
	}
	
	protected void minusPoints()
	{
		pointsLeft--;
		ptsLeft.setText("" + pointsLeft);
	}
}

//Skill: Abilite: Mod: -1: Ranks: +1: