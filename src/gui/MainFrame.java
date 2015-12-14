package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import controllers.SimulationController;
import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.JTextArea;

public class MainFrame extends JFrame
{
	/* fields */
	SimulationController simulationController;

	/* gui */
	private JPanel contentPane;
	private JComboBox comboBoxAlgorithm;
	private MemoryLayoutDrawer memoryDrawer;
	private JSpinner spinnerNsteps;
	private JTextArea textAreaEvents;
	private DefaultComboBoxModel<String> comboBoxModel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					// set look and feel
					try
					{
						UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
					} catch (Exception e1)
					{
						e1.printStackTrace();
					}

					MainFrame frame = new MainFrame();
					frame.setVisible(true);
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});

	}

	/**
	 * Create the frame.
	 */
	public MainFrame()
	{
		initGUI();
		initFields();
	}

	private void initFields()
	{
		simulationController = new SimulationController();
		simulationController.initialize();
	}

	private void initGUI()
	{
		// title and icon
		setTitle("The Dark Memory Manager");
		setIconImage(new ImageIcon(MainFrame.class.getResource("/images/icon.png")).getImage());

		// content panel
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 809, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// combo box algorithm
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Controls", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_3.setBounds(16, 11, 374, 362);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		comboBoxModel = new DefaultComboBoxModel<>(new String[]
		{ "First Fit", "Best Fit", "Worst Fit" });
		JLabel lblAlgorithm = new JLabel("algorithm");
		lblAlgorithm.setBounds(112, 38, 82, 44);
		panel_3.add(lblAlgorithm);
		lblAlgorithm.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		comboBoxAlgorithm = new JComboBox(comboBoxModel);
		comboBoxAlgorithm.setBounds(204, 48, 93, 27);
		panel_3.add(comboBoxAlgorithm);

		// button reset
		JButton buttonReset = new JButton("Reset");
		buttonReset.setBounds(33, 272, 169, 57);
		panel_3.add(buttonReset);
		buttonReset.setIcon(new ImageIcon(MainFrame.class.getResource("/images/reset.png")));
		buttonReset.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));

		// button single step
		JButton buttonSingleStep = new JButton("Single Step");
		buttonSingleStep.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent arg0)
			{
				singleStep();
			}
		});
		buttonSingleStep.setBounds(33, 113, 169, 53);
		panel_3.add(buttonSingleStep);
		buttonSingleStep.setIcon(new ImageIcon(MainFrame.class.getResource("/images/singleStep.png")));
		buttonSingleStep.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));

		// button multi step
		JButton buttonMultiStep = new JButton("Multi Step");
		buttonMultiStep.setIcon(new ImageIcon(MainFrame.class.getResource("/images/multiStep.png")));
		buttonMultiStep.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		buttonMultiStep.setBounds(33, 191, 169, 53);
		buttonMultiStep.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				multiStep();
			}
		});
		panel_3.add(buttonMultiStep);

		spinnerNsteps = new JSpinner();
		spinnerNsteps.setModel(new SpinnerNumberModel(1, 1, 1000000000, 1));
		spinnerNsteps.setBounds(240, 191, 93, 53);
		panel_3.add(spinnerNsteps);

		// reset button
		buttonReset.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent e)
			{
				reset();
			}
		});

		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Events", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel.setBounds(16, 404, 374, 346);
		contentPane.add(panel);
		panel.setLayout(null);

		textAreaEvents = new JTextArea();
		textAreaEvents.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(textAreaEvents);
		scrollPane.setBounds(10, 11, 354, 324);
		panel.add(scrollPane);

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Memory Layout",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(411, 16, 378, 734);
		contentPane.add(panel_1);
		panel_1.setLayout(null);

		// memory layout drawwer
		memoryDrawer = new MemoryLayoutDrawer();
		memoryDrawer.setBounds(6, 16, 366, 683);
		panel_1.add(memoryDrawer);
	}

	protected void multiStep()
	{
		String algorithm = (String) comboBoxModel.getSelectedItem();
		int nSteps = (int) spinnerNsteps.getValue();
		for (int i = 0; i < nSteps; i++)
		{
			String event = simulationController.singleStep(algorithm);
			textAreaEvents.append(event + "\n");
		}
		memoryDrawer.rePaint(simulationController.getProcesses(), simulationController.getHoles());
	}

	protected void singleStep()
	{
		String algorithm = (String) comboBoxModel.getSelectedItem();
		String event = simulationController.singleStep(algorithm);
		textAreaEvents.append(event + "\n");
		memoryDrawer.rePaint(simulationController.getProcesses(), simulationController.getHoles());
	}

	protected void reset()
	{
		simulationController.initialize();
		memoryDrawer.reset();
	}
}
