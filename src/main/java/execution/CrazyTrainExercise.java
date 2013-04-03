/**
 * @program 	CrazyTrainExercise
 * @author 		Ben Koury
 * @description This program creates Machine objects and uses the jBoss Drools system to
 * 				add descriptors to each object that can then be used to answer user generated
 * 				queries.  It then returns the results of these queries.  Also allows for
 * 				the user creation of new Machine objects that are then added to the Drools
 * 				KnowledgeBase and the Collection of Machine objects for future queries.
 */
package execution;

import objects.Machine;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderError;
import org.drools.builder.KnowledgeBuilderErrors;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;

public class CrazyTrainExercise {

	/**
	 * @description Main method that creates the initial Machine facts and adds
	 *              them to the created StatefulKnowledgeSessions after running
	 *              readKnowledgeBase() to create the Knowledge base from the
	 *              rules file. It then adds the Machine objects to a HashSet in
	 *              order to allow for easier iteration through all machines.
	 *              Also runs the machineFinder method to allow for user input
	 *              and queries, passing it the machines HashSet and the
	 *              StateFullKnowledgeSession as parameters.
	 */
	public static final void main(String[] args) {
		Collection<Machine> machines = new HashSet<Machine>();
		try {
			// load up the knowledge base
			KnowledgeBase kbase = readKnowledgeBase();
			StatefulKnowledgeSession ksession = kbase.newStatefulKnowledgeSession();
			// Create Machine objects
			Machine machine1 = new Machine();
			machine1.setModel("Ugly Forgetful Genius");
			machine1.setMemory(2);
			machine1.setCpuSpeed(5);
			machine1.setGraphicsCard("none");
			machine1.setPrice(800);
			machines.add(machine1);

			Machine machine2 = new Machine();
			machine2.setModel("Average Joe");
			machine2.setMemory(8);
			machine2.setCpuSpeed(3);
			machine2.setGraphicsCard("average");
			machine2.setPrice(1500);
			machines.add(machine2);

			Machine machine3 = new Machine();
			machine3.setModel("Abacus");
			machine3.setMemory(1);
			machine3.setCpuSpeed(1);
			machine3.setGraphicsCard("none");
			machine3.setPrice(400);
			machines.add(machine3);

			Machine machine4 = new Machine();
			machine4.setModel("Mega 64");
			machine4.setMemory(16);
			machine4.setCpuSpeed(4);
			machine4.setGraphicsCard("premium");
			machine4.setPrice(5000);
			machines.add(machine4);

			Machine machine5 = new Machine();
			machine5.setModel("Deep Blue");
			machine5.setMemory(32);
			machine5.setCpuSpeed(5);
			machine5.setGraphicsCard("none");
			machine5.setPrice(8000);
			machines.add(machine5);
			// insert into session knowledge and execute all rules on machines
			ksession.insert(machine1);
			ksession.insert(machine2);
			ksession.insert(machine3);
			ksession.insert(machine4);
			ksession.insert(machine5);
			ksession.fireAllRules();

			machineFinder(machines, ksession);
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	/**
	 * @method machineFinder
	 * @param machines
	 *            Collection of Machine objects that have been created and added
	 *            to KnowledgeBase.
	 * @param ksession
	 *            The StatefulKnowledgeSession to which new machines are added.
	 * @description Takes in the HashSet of machine objects and the
	 *              StatfulKnowledge session as arguments. Copies the machine
	 *              HashSet into a new HashSet called options and then removes
	 *              Machine objects from the copy as they stop matching the
	 *              query. It then outputs all the modelNames of the Machine
	 *              objects left in the options copy and repeats the process
	 *              with a new query until the user types quit. If the user
	 *              requests a new machine to be made, this method runs the
	 *              machineGenerator method.
	 */
	private static void machineFinder(Collection<Machine> machines, StatefulKnowledgeSession ksession) throws IOException {
		String query;
		BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
		Machine tempM;
		String tempD = "";
		System.out.println("Type new machine to add a machine or name properties (loud, hot, can game, can crunch numbers, expenisve, cheap, risky, safe) or type quit to quit.");
		while (!(query = input.readLine()).equals("quit")) {
			Collection<Machine> options = new HashSet<Machine>(machines);
			StringTokenizer properties = new StringTokenizer(query, ",");
			while (properties.hasMoreTokens()) {
				tempD = properties.nextToken().trim();
				Iterator<Machine> mIterator = machines.iterator();
				if (tempD.equals("new machine")) {
					machineGenerator(machines, ksession, input);
				} else {
					while (mIterator.hasNext()) {
						if (!(tempM = mIterator.next()).getDescriptors().contains(tempD)) {
							options.remove(tempM);
						}
					}
				}
			}
			if (!tempD.equals("new machine")) {
				if (options.isEmpty())
					System.out.println("No machines match that criteria.");
				else {
					Iterator<Machine> oIterator = options.iterator();
					while (oIterator.hasNext()) {
						System.out.println(oIterator.next().getModel());
					}
				}
			}
			System.out.println("Type new machine to add a machine or name properties (loud, hot, can game, can crunch numbers, expenisve, cheap, risky, safe) or type quit to quit.");
		}
	}

	/**
	 * @method machineGenerator
	 * @param machines
	 *            Collection of Machine objects that have been created and added
	 *            to KnowledgeBase.
	 * @param ksession
	 *            The StatefulKnowledgeSession to which new machines are added.
	 * @param input
	 *            Buffered reader for the user input into the console.
	 * @description Called by the machineFinder method and passed the machines
	 *              HashSet, the StatefulKnowledgeSession, and the
	 *              BufferedReader. Takes in input from the user as to the
	 *              specifications of a machine and generates a Machine,
	 *              newMachine to be added to the machines HashSet from that
	 *              data. It then adds that machine to the knowledge and
	 *              executes all the rules in the MachineRules.drl file to add
	 *              the appropriate descriptors to the new Machine.
	 */
	private static void machineGenerator(Collection<Machine> machines, StatefulKnowledgeSession ksession, BufferedReader input) throws IOException {
		Machine newMachine = new Machine();
		System.out.println("What is the name of your new machine?");
		newMachine.setModel(input.readLine());
		System.out.println("What is the memory of " + newMachine.getModel() + " in GBs?");
		newMachine.setMemory(Integer.parseInt(input.readLine()));
		System.out.println("What is the CPU speed of " + newMachine.getModel() + " in GHz?");
		newMachine.setCpuSpeed(Integer.parseInt(input.readLine()));
		System.out.println("What kind of graphics card does " + newMachine.getModel() + " have? (premium, averarge, or none)");
		newMachine.setGraphicsCard(input.readLine());
		System.out.println("How much does " + newMachine.getModel() + " cost in dollars?");
		newMachine.setPrice(Integer.parseInt(input.readLine()));
		machines.add(newMachine);
		ksession.insert(newMachine);
		ksession.fireAllRules();
		Iterator<String> newIterator = newMachine.getDescriptors().iterator();
		System.out.println(newMachine.getModel() + ", is:");
		while (newIterator.hasNext()) {
			System.out.println(newIterator.next());
		}
	}

	/**
	 * @Method readKnowledgeBase()
	 * @Description Reads the rules from MachineRules.drl and adds those rules
	 *              to a KnowledgeBase that it then returns.
	 * @return kbase: the KnowledgeBase that has been built from the
	 *         MachineRules.drl file
	 */
	private static KnowledgeBase readKnowledgeBase() throws Exception {
		KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		kbuilder.add(ResourceFactory.newClassPathResource("MachineRules.drl"), ResourceType.DRL);
		KnowledgeBuilderErrors errors = kbuilder.getErrors();
		if (errors.size() > 0) {
			for (KnowledgeBuilderError error : errors) {
				System.err.println(error);
			}
			throw new IllegalArgumentException("Could not parse knowledge.");
		}
		KnowledgeBase kbase = KnowledgeBaseFactory.newKnowledgeBase();
		kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
		return kbase;
	}

}
