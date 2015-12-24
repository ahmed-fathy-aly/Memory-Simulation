package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import controllers.SimulationController;
import controllers.Statistics;
import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;

public class MainFrame extends JFrame
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* constants */
	String INITIAL_ALGORITHM = "First Fit";
	int INITIAL_MEMORY_SIZE = 1000;
	int INITIAL_MEAN_PROCESS_SIZE = 100;
	double INITIAL_VARIANCE_PROCESS_SIZE = 0;
	int INITIAL_AVERAGE_INTERRAIVAL_TIME = 10;

	/* fields */
	SimulationController simulationController;

	/* gui */
	private JPanel contentPane;
	@SuppressWarnings("rawtypes")
	private JComboBox comboBoxAlgorithm;
	private MemoryLayoutDrawer memoryDrawer;
	private JSpinner spinnerNsteps;
	private JTextArea textAreaEvents;
	private DefaultComboBoxModel<String> comboBoxModel;

	private JSpinner spinnerMemorySize, spinnerMeanProcessSize, spinnerVarianceProcessSize, spinnerAvgInterrival;

	private JLabel labelFinishedProcesses, labelAverageWaitingTime, labelMemoryUtiilzation, labelAvgSystemTime;

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
		simulationController.setAlgorithm(INITIAL_ALGORITHM);
		simulationController.setMemorySize(INITIAL_MEMORY_SIZE);
		simulationController.setMeanProcessSize(INITIAL_MEAN_PROCESS_SIZE);
		simulationController.setVarianceProcessSize(INITIAL_VARIANCE_PROCESS_SIZE);
		simulationController.setAverageInterrival(INITIAL_AVERAGE_INTERRAIVAL_TIME);

		simulationController.initialize();
	}

	@SuppressWarnings(
	{ "unchecked", "rawtypes" })
	private void initGUI()
	{
		// title and icon
		setTitle("The Dark Memory Manager");
		setIconImage(new ImageIcon(MainFrame.class.getResource("/images/icon.png")).getImage());

		// content panel
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1211, 800);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// control panel
		JPanel panel_3 = new JPanel();
		panel_3.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Controls", TitledBorder.LEADING,
				TitledBorder.TOP, null, null));
		panel_3.setBounds(16, 11, 374, 739);
		contentPane.add(panel_3);
		panel_3.setLayout(null);

		// button reset
		JButton buttonReset = new JButton("Reset");
		buttonReset.setBounds(33, 671, 169, 57);
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
		buttonSingleStep.setBounds(33, 512, 169, 53);
		panel_3.add(buttonSingleStep);
		buttonSingleStep.setIcon(new ImageIcon(MainFrame.class.getResource("/images/singleStep.png")));
		buttonSingleStep.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));

		// button multi step
		JButton buttonMultiStep = new JButton("Multi Step");
		buttonMultiStep.setIcon(new ImageIcon(MainFrame.class.getResource("/images/multiStep.png")));
		buttonMultiStep.setFont(new Font("Trebuchet MS", Font.PLAIN, 14));
		buttonMultiStep.setBounds(33, 590, 169, 53);
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
		spinnerNsteps.setBounds(240, 590, 93, 53);
		panel_3.add(spinnerNsteps);

		comboBoxModel = new DefaultComboBoxModel<>(new String[]
		{ "First Fit", "Best Fit", "Worst Fit" });
		JLabel lblAlgorithm = new JLabel("algorithm");
		lblAlgorithm.setBounds(33, 38, 161, 44);
		panel_3.add(lblAlgorithm);
		lblAlgorithm.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		comboBoxAlgorithm = new JComboBox(comboBoxModel);
		comboBoxAlgorithm.setBounds(204, 48, 93, 27);
		comboBoxAlgorithm.addActionListener(new ActionListener()
		{

			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String algorithm = (String) comboBoxModel.getSelectedItem();
				simulationController.setAlgorithm(algorithm);
			}
		});
		panel_3.add(comboBoxAlgorithm);

		// memory size
		JLabel lblMemorySize = new JLabel("memory size");
		lblMemorySize.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblMemorySize.setBounds(33, 109, 161, 44);
		panel_3.add(lblMemorySize);

		spinnerMemorySize = new JSpinner();
		spinnerMemorySize.setModel(new SpinnerNumberModel(INITIAL_MEMORY_SIZE, 1, 1000000000, 1));
		spinnerMemorySize.setBounds(204, 106, 93, 53);
		spinnerMemorySize.addChangeListener(new ChangeListener()
		{

			@Override
			public void stateChanged(ChangeEvent e)
			{
				int newValue = (int) spinnerMemorySize.getValue();
				simulationController.setMemorySize((int) newValue);
			}
		});

		panel_3.add(spinnerMemorySize);

		JLabel lblAvgProcessSize = new JLabel("mean process size");
		lblAvgProcessSize.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblAvgProcessSize.setBounds(33, 189, 161, 44);
		panel_3.add(lblAvgProcessSize);

		// mean process size
		spinnerMeanProcessSize = new JSpinner();
		spinnerMeanProcessSize.setModel(new SpinnerNumberModel(INITIAL_MEAN_PROCESS_SIZE, 1, 1000000000, 1));
		spinnerMeanProcessSize.setBounds(204, 186, 93, 53);
		spinnerMeanProcessSize.addChangeListener(new ChangeListener()
		{

			@Override
			public void stateChanged(ChangeEvent e)
			{
				int newValue = (int) spinnerMeanProcessSize.getValue();
				simulationController.setMeanProcessSize((int) newValue);
			}
		});

		panel_3.add(spinnerMeanProcessSize);

		JLabel lblVarianceOfProcess = new JLabel("variance of process size");
		lblVarianceOfProcess.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblVarianceOfProcess.setBounds(33, 271, 161, 44);
		panel_3.add(lblVarianceOfProcess);

		// variance process size
		spinnerVarianceProcessSize = new JSpinner();
		spinnerVarianceProcessSize
				.setModel(new SpinnerNumberModel(INITIAL_VARIANCE_PROCESS_SIZE, 0, 1000000000.0, 1.0));
		spinnerVarianceProcessSize.setBounds(204, 268, 93, 53);
		spinnerVarianceProcessSize.addChangeListener(new ChangeListener()
		{

			@Override
			public void stateChanged(ChangeEvent e)
			{
				double newValue = (double) spinnerVarianceProcessSize.getValue();
				simulationController.setVarianceProcessSize(newValue);
			}
		});
		panel_3.add(spinnerVarianceProcessSize);

		// average interrival
		JLabel lblAvgProcessInterarrival = new JLabel("avg process interarrival time");
		lblAvgProcessInterarrival.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblAvgProcessInterarrival.setBounds(33, 352, 161, 44);
		panel_3.add(lblAvgProcessInterarrival);

		spinnerAvgInterrival = new JSpinner();
		spinnerAvgInterrival.setModel(new SpinnerNumberModel(INITIAL_AVERAGE_INTERRAIVAL_TIME, 1, 1000000000, 1));
		spinnerAvgInterrival.setBounds(204, 349, 93, 53);
		spinnerAvgInterrival.addChangeListener(new ChangeListener()
		{

			@Override
			public void stateChanged(ChangeEvent e)
			{
				int newValue = (int) spinnerAvgInterrival.getValue();
				simulationController.setAverageInterrival((int) newValue);
			}
		});
		panel_3.add(spinnerAvgInterrival);

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
		panel.setBounds(811, 16, 374, 346);
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

		// memory layout drawer
		memoryDrawer = new MemoryLayoutDrawer();
		memoryDrawer.setBounds(6, 16, 366, 683);
		panel_1.add(memoryDrawer);

		// statistics

		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "Statistics",
				TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(811, 385, 363, 353);
		contentPane.add(panel_2);
		panel_2.setLayout(null);

		JLabel lblFinishedProcesses = new JLabel("finished processes");
		lblFinishedProcesses.setBounds(6, 16, 161, 44);
		panel_2.add(lblFinishedProcesses);
		lblFinishedProcesses.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));

		labelFinishedProcesses = new JLabel("-");
		labelFinishedProcesses.setBounds(177, 16, 161, 44);
		panel_2.add(labelFinishedProcesses);
		labelFinishedProcesses.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));

		JLabel lblAvgWaitingTime = new JLabel("avg waiting time");
		lblAvgWaitingTime.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		lblAvgWaitingTime.setBounds(6, 71, 161, 44);
		panel_2.add(lblAvgWaitingTime);

		labelAverageWaitingTime = new JLabel("-");
		labelAverageWaitingTime.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		labelAverageWaitingTime.setBounds(177, 71, 161, 44);
		panel_2.add(labelAverageWaitingTime);

		JLabel label_3 = new JLabel("memory utiilzation %");
		label_3.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		label_3.setBounds(6, 192, 161, 44);
		panel_2.add(label_3);

		labelMemoryUtiilzation = new JLabel("-");
		labelMemoryUtiilzation.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		labelMemoryUtiilzation.setBounds(177, 192, 161, 44);
		panel_2.add(labelMemoryUtiilzation);

		JLabel label_4 = new JLabel("avg system time");
		label_4.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		label_4.setBounds(6, 126, 161, 44);
		panel_2.add(label_4);

		labelAvgSystemTime = new JLabel("-");
		labelAvgSystemTime.setFont(new Font("Trebuchet MS", Font.PLAIN, 12));
		labelAvgSystemTime.setBounds(177, 126, 161, 44);
		panel_2.add(labelAvgSystemTime);
	}

	protected void multiStep()
	{
		setControlsEnabled(false);
		int nSteps = (int) spinnerNsteps.getValue();
		for (int i = 0; i < nSteps; i++)
		{
			String event = simulationController.singleStep();
			textAreaEvents.append(event + "\n");
		}
		memoryDrawer.rePaint(simulationController.getProcesses(), simulationController.getHoles());
		updateStatistics();
	}

	protected void singleStep()
	{
		setControlsEnabled(false);
		String event = simulationController.singleStep();
		textAreaEvents.append(event + "\n");
		memoryDrawer.rePaint(simulationController.getProcesses(), simulationController.getHoles());
		updateStatistics();
	}

	private void updateStatistics()
	{
		Statistics statistics = simulationController.getStatistics();
		labelAverageWaitingTime.setText(statistics.getAverageWaitingTime() + "");
		labelAvgSystemTime.setText(statistics.getAverageTimeInSystem() + "");
		labelFinishedProcesses.setText(statistics.getNumberFinishedProcesses()+ "");
		labelMemoryUtiilzation.setText(statistics.getPercentageAverageMemoryUtilization() + "");
	}

	protected void reset()
	{
		// enable controls
		setControlsEnabled(true);

		// reset fields
		simulationController.initialize();
		memoryDrawer.reset();
		textAreaEvents.setText("");
	}

	private void setControlsEnabled(boolean b)
	{
		spinnerAvgInterrival.setEnabled(b);
		spinnerMemorySize.setEnabled(b);
		spinnerMeanProcessSize.setEnabled(b);
		spinnerVarianceProcessSize.setEnabled(b);
		comboBoxAlgorithm.setEnabled(b);
	}
}
