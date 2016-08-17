package View;

import java.awt.EventQueue;





import javax.swing.JFrame;
import javax.swing.JButton;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JScrollPane;

import Model.*;
import Services.*;
import Control.*;
public class View {
 
	
	private JFrame frame;
	private File[] files;
	private ILoadClasses lc ;
	private IXMLParser xp;
	private ICompare comp;
	private JLabel lblLoading,lblNewLabel;
	JTextArea textArea;
	JScrollPane scrollPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					View window = new View();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public View() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 274, 554);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		lblLoading = new JLabel("Loading...");
		lblLoading.setHorizontalAlignment(SwingConstants.CENTER);
		lblLoading.setFont(new Font("Aharoni", Font.PLAIN, 15));
		lblLoading.setForeground(Color.RED);
		lblLoading.setBounds(54, 278, 95, 31);
		frame.getContentPane().add(lblLoading);
		lblLoading.setVisible(false);
		
		scrollPane = new JScrollPane();
		scrollPane.setBounds(196, 29, 490, 397);
		frame.getContentPane().add(scrollPane);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		textArea.setVisible(false);
		scrollPane.setVisible(false);
		lblNewLabel = new JLabel("Done");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setForeground(Color.GREEN);
		lblNewLabel.setFont(new Font("Aharoni", Font.PLAIN, 15));
		lblNewLabel.setBounds(54, 320, 95, 31);
		frame.getContentPane().add(lblNewLabel);
		lblNewLabel.setVisible(false);
		
		JButton btnNewButton = new JButton("Load Diagram");
		btnNewButton.setBounds(35, 29, 130, 23);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblLoading.setVisible(true);
				LoadFiles lf =  new LoadFiles();
				files  = lf.selectfiles();
				xp = new XMLParser(files[0]);
				xp.loadXmlFile();
				lblLoading.setVisible(false);
				lblNewLabel.setVisible(true);
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btnNewButton);
		
		JButton btnNewButton_1 = new JButton("Load Classes");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblNewLabel.setVisible(false);
				lblLoading.setVisible(true);
				LoadFiles lf =  new LoadFiles();
				files  = lf.selectfiles();
				lc = new LoadClasses(files);
				printFileNames();
				lc.load();
				lblLoading.setVisible(false);
				lblNewLabel.setVisible(true);
			}
		});
		btnNewButton_1.setBounds(35, 63, 130, 23);
		frame.getContentPane().add(btnNewButton_1);
		
		
		
		
		JButton btnNewButton_2 = new JButton("Compare");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblNewLabel.setVisible(false);
				lblLoading.setVisible(true);
				frame.setBounds(100, 100, 800, 600);
				scrollPane.setVisible(true);
				textArea.setVisible(true);
				comp=new Compare(lc.getClasses(),xp.getClasses());
				String comparison=comp.compare();
				//System.out.println(comparison);
				textArea.setText(comparison);
				lblLoading.setVisible(false);
				lblNewLabel.setVisible(true);
			}
		});
		btnNewButton_2.setBounds(35, 169, 130, 23);
		frame.getContentPane().add(btnNewButton_2);
		
		JButton patchBtn = new JButton("Generate Correction");
		patchBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblNewLabel.setVisible(false);
				lblLoading.setVisible(true);
				comp.patch(1);
				textArea.setText("The files have been created");
				lblLoading.setVisible(false);
				lblNewLabel.setVisible(true);
			}
		});
		patchBtn.setBounds(10, 203, 176, 23);
		frame.getContentPane().add(patchBtn);
		
		JButton btnNewButton_3 = new JButton("Generate All Classes");
		btnNewButton_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				lblNewLabel.setVisible(false);
				lblLoading.setVisible(true);
				comp.patch(2);
				textArea.setText("The files have been created");
				lblLoading.setVisible(false);
				lblNewLabel.setVisible(true);
			}
		});
		btnNewButton_3.setBounds(10, 237, 176, 23);
		frame.getContentPane().add(btnNewButton_3);
		
		
	}
	public void printFileNames()
	{
		String text = "";
		for(int i = 0; i< files.length;i++)
			text += files[i] + "\n";
		textArea.setText(text);
	}
}