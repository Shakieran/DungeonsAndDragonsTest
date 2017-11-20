package com.kedraney.ddgame.gui.charactercreation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.math.*;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;


public class CharacterCreation extends JFrame
{
	private Container parent;
	private BorderLayout layout;
	private JButton update = new JButton("Update");
	private JButton finish = new JButton("Finish");
	
//	File jobs = new File("C:\\Users\\USER\\Documents\\JCreator LE\\MyProjects\\DDGame\\GameData", "Classes.txt");//\\Classes.txt
	
	private CC01 abilities;
	private CC02 classRace;
	private CC03 skills;
	private CC04 feats;
	
	public CharacterCreation()
	{
		super("Character Creation Manager");
		parent = getContentPane();
		layout = new BorderLayout();
		parent.setLayout(layout);
		
		abilities = new CC01();
		classRace = new CC02();
		skills = new CC03();
		feats = new CC04();
		
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); FIX THIS LATER!!!
		
		update();
		finish();
		
		add(update, BorderLayout.NORTH);
		add(finish, BorderLayout.SOUTH);
		
		setSize(200, 100);
		setVisible(true);
	}
	
	private void update()
	{
		update.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				updateStats();
			}
		});
	}
	
	private void updateStats()
	{
		skills.update(abilities.getScores());
	}
	
	private void finish()
	{
		finish.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				updateStats();
			}
		});
	}
	
	public static void main(String[] args)
	{
		CharacterCreation manager = new CharacterCreation();
	}
}