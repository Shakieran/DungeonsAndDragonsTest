package com.kedraney.ddgame.gui.charactercreation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.math.*;

//6/10/17 - This window is the second character creeation menu, it lets the player choose their race and class, and
//it provides a brief description

public class CC02 extends JFrame
{
	private Container parent;
	private GridBagLayout layout;
	private GridBagConstraints constraints;
	private String[] classes = {"Barbarian", "Bard", "Cleric", "Druid", "Fighter", "Monk", "Paladin", "Ranger", "Rogue", "Sorcerer", "Wizard"};
	private String[] classDesc = {"A ferocious warrior who uses fury and instinct to bring down foes.", "A performer whose music works magic�a wanderer, a taleteller, and a jack-of-all trades.", "A master of divine magic and a capable warrior as well.", "One who draws energy from the natural world to cast divine spells and gain strange magical powers.", "A warrior with exceptional combat capability and unequaled skill with weapons.", "A martial artist whose unarmed strikes hit fast and hard� a master of exotic powers.", "A champion of justice and destroyer of evil, protected and strengthened by an array of divine powers.", "A cunning, skilled warrior of the wilderness.", "A tricky, skillful scout and spy who wins the battle by stealth rather than brute force.", "A spellcaster with inborn magical ability.", "A potent spellcaster schooled in the arcane arts."};
	private String[] races = {"Dwarf", "Elf", "Gnome", "Half-Elf", "Halfling", "Half-Orc", "Human"};
	private String[] raceDesc = {"+2 Constitution\n �2 Charisma \n\n Favored Class:\n Fighter", "+2 Dexterity\n �2 Constitution \n\n Favored Class:\n Wizard", "+2 Constitution\n �2 Strength \n\n Favored Class:\n Bard", "None \n\n Favored Class:\nAny", "+2 Dexterity\n �2 Strength \n\n Favored Class:\n Rogue", "+2 Strength\n �2 Intelligence\n �2 Charisma \n\n Favored Class:\n Barbarian ", "None \n\n Favored Class:\n Any"};
	private JComboBox<String> job = new JComboBox<>(classes);
	private JComboBox<String> race = new JComboBox<>(races);
	
	private JTextArea raceDescription = new JTextArea(raceDesc[0], 5, 20);
	private JTextArea classDescription = new JTextArea(classDesc[0]);
	
	public CC02()
	{
		super("Race/Class");
		parent = getContentPane();
		layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		parent.setLayout(layout);
		
		raceDescription.setLineWrap(true);
		classDescription.setLineWrap(true);
		
		race.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				raceDescription.setText(raceDesc[race.getSelectedIndex()]);
			}
		});
		
		job.addItemListener(new ItemListener()
		{
			public void itemStateChanged(ItemEvent e)
			{
				classDescription.setText(classDesc[job.getSelectedIndex()]);
			}
		});
		
		addComponent(race, 0, 0, 5, 1);
		addComponent(new JLabel("   "), 0, 5, 1, 1);
		addComponent(job, 0, 6, 5, 1);
		addComponent(raceDescription, 1, 0, 5, 5);
		addComponent(new JLabel("   "), 1, 5, 1, 1);
		addComponent(classDescription, 1, 6, 5, 5);
		
		setSize(240, 200);
		setVisible(true);
	}
	
	public String getJob()
	{
		return classes[job.getSelectedIndex()];
	}
	
	public String getRace()
	{
		return races[race.getSelectedIndex()];
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
		CC02 test = new CC02();
	}
}