package actv.ccs.model.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SpringLayout;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import actv.ccs.sageTest.MyGame;

public class SimulationPrompter extends JFrame {

	private ResultSet rs, rss;
	private String saveFishID, saveObjID;
	private MyGame myGame;
	private static final Logger logger = LoggerFactory.getLogger(SimulationPrompter.class);

	public SimulationPrompter(MyGame gg) {
		myGame = gg;
		setTitle("Convict Cichlid Fish Simulator Simulation Prompter");
		setSize(492, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		SpringLayout springLayout = new SpringLayout();
		getContentPane().setLayout(springLayout);

		JLabel lblHelloWelcomeTo = new JLabel(
				"Hello! Welcome to this humble version of a Convict Cichlid Simulator! Please choose your poison!");
		springLayout.putConstraint(SpringLayout.NORTH, lblHelloWelcomeTo, 10,
				SpringLayout.NORTH, getContentPane());
		springLayout.putConstraint(SpringLayout.WEST, lblHelloWelcomeTo, 10,
				SpringLayout.WEST, getContentPane());
		getContentPane().add(lblHelloWelcomeTo);

		JLabel lblPickAndStart = new JLabel("PICK AND START THY SIMULATION!");
		springLayout.putConstraint(SpringLayout.NORTH, lblPickAndStart, 19,
				SpringLayout.SOUTH, lblHelloWelcomeTo);
		springLayout.putConstraint(SpringLayout.WEST, lblPickAndStart, 0,
				SpringLayout.WEST, lblHelloWelcomeTo);
		getContentPane().add(lblPickAndStart);

		JButton btnCreateYourOwn = new JButton(
				"Create your Own Simulation (hah)");
		springLayout.putConstraint(SpringLayout.NORTH, btnCreateYourOwn, 18,
				SpringLayout.SOUTH, lblPickAndStart);
		springLayout.putConstraint(SpringLayout.WEST, btnCreateYourOwn, 10,
				SpringLayout.WEST, getContentPane());
		getContentPane().add(btnCreateYourOwn);
		btnCreateYourOwn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {
					NewSimulation newSim = new NewSimulation(myGame);
					Connection conn;
					try {
						conn = DriverManager
								.getConnection("jdbc:ucanaccess://FishPool.accdb");

						Statement s = conn.createStatement();
						rs = s.executeQuery("SELECT ID FROM [ScenarioFlag]");
						while (rs.next()) {
							int a = s
									.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 1 where ID = 1");
						}
						conn.close();
					} catch (SQLException Ex) {
						// TODO Auto-generated catch block
						Ex.printStackTrace();
					}
				} catch (SecurityException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				shutDown();

			}
		});

		JLabel lblIfNewSimulation = new JLabel(
				"If new simulation ... then autoclose this and go to new simulation");
		springLayout.putConstraint(SpringLayout.NORTH, lblIfNewSimulation, 70,
				SpringLayout.SOUTH, btnCreateYourOwn);
		springLayout.putConstraint(SpringLayout.WEST, lblIfNewSimulation, 0,
				SpringLayout.WEST, lblHelloWelcomeTo);
		getContentPane().add(lblIfNewSimulation);

		JButton btnScenario1 = new JButton("Scenario 1");
		springLayout.putConstraint(SpringLayout.NORTH, btnScenario1, 22,
				SpringLayout.SOUTH, lblIfNewSimulation);
		springLayout.putConstraint(SpringLayout.WEST, btnScenario1, 0,
				SpringLayout.WEST, lblHelloWelcomeTo);
		getContentPane().add(btnScenario1);
		btnScenario1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {
					Connection conn;
					try {
						conn = DriverManager
								.getConnection("jdbc:ucanaccess://FishPool.accdb");

						Statement s = conn.createStatement();
						rs = s.executeQuery("SELECT ID FROM [ScenarioFlag]");
						while (rs.next()) {
							int a = s
									.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 2 where ID = 2");
						}
						conn.close();
					} catch (SQLException Ex) {
						// TODO Auto-generated catch block
						Ex.printStackTrace();
					}

					// then initialize the fish and objects from here
					try {
						Connection connn;
						try {
							connn = DriverManager
									.getConnection("jdbc:ucanaccess://FishPool.accdb");
							Statement s = connn.createStatement();
							rs = s.executeQuery("SELECT ID FROM [SimulationFish]");
							while (rs.next()) {
								int a = s
										.executeUpdate("UPDATE SimulationFish set fishID = 1 where ID = 1");
								int b = s
										.executeUpdate("UPDATE SimulationFish set fishID = 2 where ID = 2");
								int c = s
										.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 3");

							}
							connn.close();
						} catch (Exception p1) {
							p1.printStackTrace();
						}

					} catch (Exception pp) {
						pp.printStackTrace();
					}
				} catch (SecurityException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				scenarioShutDown();
			}
		});
		JButton btnScenario2 = new JButton("Scenario 2");
		springLayout.putConstraint(SpringLayout.NORTH, btnScenario2, 20,
				SpringLayout.SOUTH, btnScenario1);
		springLayout.putConstraint(SpringLayout.EAST, btnScenario2, 0,
				SpringLayout.EAST, btnScenario1);
		getContentPane().add(btnScenario2);
		btnScenario2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {
					Connection conn;
					try {
						conn = DriverManager
								.getConnection("jdbc:ucanaccess://FishPool.accdb");

						Statement s = conn.createStatement();
						rs = s.executeQuery("SELECT ID FROM [ScenarioFlag]");
						while (rs.next()) {
							int a = s
									.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 3 where ID = 3");
						}
						conn.close();
					} catch (SQLException Ex) {
						// TODO Auto-generated catch block
						Ex.printStackTrace();
					}
				} catch (SecurityException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}
				try {
					Connection connn;
					try {
						connn = DriverManager
								.getConnection("jdbc:ucanaccess://FishPool.accdb");
						Statement s = connn.createStatement();
						rs = s.executeQuery("SELECT ID FROM [SimulationFish]");
						while (rs.next()) {
							int a = s
									.executeUpdate("UPDATE SimulationFish set fishID = 1 where ID = 1");
							int b = s
									.executeUpdate("UPDATE SimulationFish set fishID = 2 where ID = 2");
							int c = s
									.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 3");

						}
						connn.close();
					} catch (Exception p1) {
						p1.printStackTrace();
					}

				} catch (Exception pp) {
					pp.printStackTrace();
				}
				try {
					Connection conne;
					try {
						conne = DriverManager
								.getConnection("jdbc:ucanaccess://FishPool.accdb");
						Statement s = conne.createStatement();
						rs = s.executeQuery("SELECT ID FROM [SimulationObjects]");
						while (rs.next()) {
							int a = s
									.executeUpdate("UPDATE SimulationObjects set objID = 1 where ID = 4");

						}
						conne.close();
					} catch (Exception p1) {
						p1.printStackTrace();
					}

				} catch (Exception pp) {
					pp.printStackTrace();
				}
				scenarioShutDown();

			}
		});
		JButton btnScenario3 = new JButton("Scenario 3");
		springLayout.putConstraint(SpringLayout.NORTH, btnScenario3, 19,
				SpringLayout.SOUTH, btnScenario2);
		springLayout.putConstraint(SpringLayout.WEST, btnScenario3, 0,
				SpringLayout.WEST, lblHelloWelcomeTo);
		getContentPane().add(btnScenario3);
		btnScenario3.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {
					Connection conn;
					try {
						conn = DriverManager
								.getConnection("jdbc:ucanaccess://FishPool.accdb");

						Statement s = conn.createStatement();
						rs = s.executeQuery("SELECT ID FROM [ScenarioFlag]");
						while (rs.next()) {
							int a = s
									.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 4 where ID = 4");
						}
						conn.close();
					} catch (SQLException Ex) {
						// TODO Auto-generated catch block
						Ex.printStackTrace();
					}
				} catch (SecurityException e4) {
					// TODO Auto-generated catch block
					e4.printStackTrace();
				}
				try {
					Connection connn;
					try {
						connn = DriverManager
								.getConnection("jdbc:ucanaccess://FishPool.accdb");
						Statement s = connn.createStatement();
						rs = s.executeQuery("SELECT ID FROM [SimulationFish]");
						while (rs.next()) {
							int a = s
									.executeUpdate("UPDATE SimulationFish set fishID = 1 where ID = 1");
							int b = s
									.executeUpdate("UPDATE SimulationFish set fishID = 2 where ID = 2");
							int c = s
									.executeUpdate("UPDATE SimulationFish set fishID = 0 where ID = 3");

						}
						connn.close();
					} catch (Exception p1) {
						p1.printStackTrace();
					}

				} catch (Exception pp) {
					pp.printStackTrace();
				}
				try {
					Connection conne;
					try {
						conne = DriverManager
								.getConnection("jdbc:ucanaccess://FishPool.accdb");
						Statement s = conne.createStatement();
						rs = s.executeQuery("SELECT ID FROM [SimulationObjects]");
						while (rs.next()) {
							int a = s
									.executeUpdate("UPDATE SimulationObjects set objID = 4 where ID = 4");
							int b = s
									.executeUpdate("UPDATE SimulationObjects set objID = 1 where ID = 1");
						}
						conne.close();
					} catch (Exception p1) {
						p1.printStackTrace();
					}

				} catch (Exception pp) {
					pp.printStackTrace();
				}

				scenarioShutDown();

			}
		});
		JButton btnScenario4 = new JButton("Scenario 4");
		springLayout.putConstraint(SpringLayout.NORTH, btnScenario4, 16,
				SpringLayout.SOUTH, btnScenario3);
		springLayout.putConstraint(SpringLayout.WEST, btnScenario4, 0,
				SpringLayout.WEST, lblHelloWelcomeTo);
		getContentPane().add(btnScenario4);
		btnScenario4.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {
					Connection conn;
					try {
						conn = DriverManager
								.getConnection("jdbc:ucanaccess://FishPool.accdb");

						Statement s = conn.createStatement();
						rs = s.executeQuery("SELECT ID FROM [ScenarioFlag]");
						while (rs.next()) {
							int a = s
									.executeUpdate("UPDATE ScenarioFlag set ScenarioNumber = 5 where ID = 5");
						}
						conn.close();
					} catch (SQLException Ex) {
						// TODO Auto-generated catch block
						Ex.printStackTrace();
					}
				} catch (SecurityException e5) {
					// TODO Auto-generated catch block
					e5.printStackTrace();
				}
				try {
					Connection connn;
					try {
						connn = DriverManager
								.getConnection("jdbc:ucanaccess://FishPool.accdb");
						Statement s = connn.createStatement();
						rs = s.executeQuery("SELECT ID FROM [SimulationFish]");
						while (rs.next()) {
							int a = s
									.executeUpdate("UPDATE SimulationFish set fishID = 1 where ID = 1");
							int b = s
									.executeUpdate("UPDATE SimulationFish set fishID = 2 where ID = 2");
							int c = s
									.executeUpdate("UPDATE SimulationFish set fishID = 3 where ID = 3");

						}
						connn.close();
					} catch (Exception p1) {
						p1.printStackTrace();
					}

				} catch (Exception pp) {
					pp.printStackTrace();
				}
				try {
					Connection conne;
					try {
						conne = DriverManager
								.getConnection("jdbc:ucanaccess://FishPool.accdb");
						Statement s = conne.createStatement();
						rs = s.executeQuery("SELECT ID FROM [SimulationObjects]");
						while (rs.next()) {
							int a = s
									.executeUpdate("UPDATE SimulationObjects set objID = 1 where ID = 1");
							int b = s
									.executeUpdate("UPDATE SimulationObjects set objID = 2 where ID = 2");
							int c = s
									.executeUpdate("UPDATE SimulationObjects set objID = 3 where ID = 3");
							int d = s
									.executeUpdate("UPDATE SimulationObjects set objID = 4 where ID = 4");
							int g = s
									.executeUpdate("UPDATE SimulationObjects set objID = 5 where ID = 5");
							int f = s
									.executeUpdate("UPDATE SimulationObjects set objID = 6 where ID = 6");

						}
						conne.close();
					} catch (Exception p1) {
						p1.printStackTrace();
					}

				} catch (Exception pp) {
					pp.printStackTrace();
				}
				scenarioShutDown();

			}
		});

		JLabel lblCichlidsOn = new JLabel("2 Cichlids");
		springLayout.putConstraint(SpringLayout.NORTH, lblCichlidsOn, 4,
				SpringLayout.NORTH, btnScenario1);
		getContentPane().add(lblCichlidsOn);

		JLabel lblCichlids = new JLabel("2 Cichlids, 1 Large Pot");
		springLayout.putConstraint(SpringLayout.WEST, lblCichlids, 24,
				SpringLayout.EAST, btnScenario2);
		springLayout.putConstraint(SpringLayout.WEST, lblCichlidsOn, 0,
				SpringLayout.WEST, lblCichlids);
		springLayout.putConstraint(SpringLayout.NORTH, lblCichlids, 4,
				SpringLayout.NORTH, btnScenario2);
		getContentPane().add(lblCichlids);

		JLabel lblCichlids_1 = new JLabel(
				"2 Cichlids, 1 Large Pot, 1 Large Plant");
		springLayout.putConstraint(SpringLayout.NORTH, lblCichlids_1, 4,
				SpringLayout.NORTH, btnScenario3);
		springLayout.putConstraint(SpringLayout.WEST, lblCichlids_1, 24,
				SpringLayout.EAST, btnScenario3);
		getContentPane().add(lblCichlids_1);

		JLabel lblCichlidsAnd = new JLabel("3 Cichlids and the works.");
		springLayout.putConstraint(SpringLayout.NORTH, lblCichlidsAnd, 0,
				SpringLayout.NORTH, btnScenario4);
		springLayout.putConstraint(SpringLayout.WEST, lblCichlidsAnd, 0,
				SpringLayout.WEST, lblCichlidsOn);
		getContentPane().add(lblCichlidsAnd);

		JButton btnLoadSavedState = new JButton("Load Saved State");
		springLayout.putConstraint(SpringLayout.NORTH, btnLoadSavedState, 20,
				SpringLayout.SOUTH, btnScenario4);
		springLayout.putConstraint(SpringLayout.WEST, btnLoadSavedState, 0,
				SpringLayout.WEST, lblHelloWelcomeTo);
		getContentPane().add(btnLoadSavedState);
		btnLoadSavedState.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				try {
					Connection conn;
					conn = DriverManager
							.getConnection("jdbc:ucanaccess://FishPool.accdb");

					Statement s = conn.createStatement();
					rs = s.executeQuery("SELECT * FROM [SimulationFishS]");
					while (rs.next()) {
						saveFishID = rs.getString("fishID");
						logger.info("Fish ID IS " + saveFishID);
						if (saveFishID.equals("1")) {
							int a = s
									.executeUpdate("UPDATE SimulationFish set fishID = 1 where ID = 1");
							// then call for swapping stuff.
							rss = s.executeQuery("SELECT * FROM [FishPoolSaveState] WHERE ID = 1");
							while (rss.next()) {
								String xValue = rss.getString("StartingXPos");
								String yValue = rss.getString("StartingYPos");
								String zValue = rss.getString("StartingZPos");

								int xV = Integer.parseInt(xValue);
								int yV = Integer.parseInt(yValue);
								int zV = Integer.parseInt(zValue);

								int aa = s
										.executeUpdate("UPDATE FishPool set StartingXPos = "
												+ xV + " WHERE ID = 1");
								int b = s
										.executeUpdate("UPDATE FishPool set StartingYPos = "
												+ yV + " WHERE ID = 1");
								int c = s
										.executeUpdate("UPDATE FishPool set StartingZPos = "
												+ zV + " WHERE ID = 1");

							}
						}
						if (saveFishID.equals("2")) {
							int a = s
									.executeUpdate("UPDATE SimulationFish set fishID = 2 where ID = 2");
							rss = s.executeQuery("SELECT * FROM [FishPoolSaveState] WHERE ID = 2");
							while (rss.next()) {
								String xValue = rss.getString("StartingXPos");
								String yValue = rss.getString("StartingYPos");
								String zValue = rss.getString("StartingZPos");

								int xV = Integer.parseInt(xValue);
								int yV = Integer.parseInt(yValue);
								int zV = Integer.parseInt(zValue);

								int aa = s
										.executeUpdate("UPDATE FishPool set StartingXPos = "
												+ xV + " WHERE ID = 2");
								int b = s
										.executeUpdate("UPDATE FishPool set StartingYPos = "
												+ yV + " WHERE ID = 2");
								int c = s
										.executeUpdate("UPDATE FishPool set StartingZPos = "
												+ zV + " WHERE ID = 2");

							}

						}
						if (saveFishID.equals("3")) {
							int a = s
									.executeUpdate("UPDATE SimulationFish set fishID = 3 where ID = 3");
							rss = s.executeQuery("SELECT * FROM [FishPoolSaveState] WHERE ID = 3");
							while (rss.next()) {
								String xValue = rss.getString("StartingXPos");
								String yValue = rss.getString("StartingYPos");
								String zValue = rss.getString("StartingZPos");

								int xV = Integer.parseInt(xValue);
								int yV = Integer.parseInt(yValue);
								int zV = Integer.parseInt(zValue);

								int aa = s
										.executeUpdate("UPDATE FishPool set StartingXPos = "
												+ xV + " WHERE ID = 3");
								int b = s
										.executeUpdate("UPDATE FishPool set StartingYPos = "
												+ yV + " WHERE ID = 3");
								int c = s
										.executeUpdate("UPDATE FishPool set StartingZPos = "
												+ zV + " WHERE ID = 3");
							}

						}
					}

				} catch (Exception savel) {
					savel.printStackTrace();
				}
				try {
					Connection conn;
					conn = DriverManager
							.getConnection("jdbc:ucanaccess://FishPool.accdb");

					Statement s = conn.createStatement();
					rs = s.executeQuery("SELECT * FROM [SimulationObjectsS]");
					while (rs.next()) {
						saveObjID = rs.getString("objID");
						logger.debug("objID IS " + saveObjID);
						if (saveObjID.equals("1")) {
							int a = s
									.executeUpdate("UPDATE SimulationObjects set objID = 1 where ID = 1");
						}
						if (saveObjID.equals("2")) {
							int a = s
									.executeUpdate("UPDATE SimulationObjects set objID = 2 where ID = 2");
						}
						if (saveObjID.equals("3")) {
							int a = s
									.executeUpdate("UPDATE SimulationObjects set objID = 3 where ID = 3");
						}
						if (saveObjID.equals("4")) {
							int a = s
									.executeUpdate("UPDATE SimulationObjects set objID = 4 where ID = 4");
						}
						if (saveObjID.equals("5")) {
							int a = s
									.executeUpdate("UPDATE SimulationObjects set objID = 5 where ID = 5");
						}
						if (saveObjID.equals("6")) {
							int a = s
									.executeUpdate("UPDATE SimulationObjects set objID = 6 where ID = 6");
						}

					}

				} catch (Exception savel) {
					savel.printStackTrace();
				}
				scenarioShutDown();
				// cool

			}
		});

		this.setVisible(true);
	}

	public void shutDown() {
		super.dispose();
	}

	public void scenarioShutDown() {
		super.dispose();
		this.myGame.setPauseSim(false);
		this.myGame.initObjects();
		this.myGame.spawnCichlids();
		this.myGame.spawnObjects();
		this.myGame.createPerson();

		this.myGame.initActions();

		this.myGame.createFishTankWalls();
		this.myGame.startRunner();
	//	this.myGame.createHUD();
		this.myGame.setUpTank();
		this.myGame.setAnimation(true);
	//	this.myGame.startAnimationProcess();
	}
}
