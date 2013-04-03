/**
 * @Class 		Machine
 * @Author		Ben Koury
 * @Description A class that defines a new object Machine.  This object has variables to store the model name, memory, CPU speed,
 * 				graphics card, and price of a Machine.  It is written in standard Java bean format with getter and setter
 * 				methods for each variable.  This allows for compatibility with the jBoss Drools rules system.  There is also a
 * 				HashSet for storing the descriptors to be added by the MachineRules.drl file.  This class
 * 				is also marked as PropertyReactive with each method that alters data marked with the variable that it alters
 * 				so that Drools will be able to fire rules only when related data is modified.
 * 
 */
package objects;

import java.util.Collection;
import java.util.HashSet;

import org.drools.definition.type.PropertyReactive;
import org.drools.definition.type.Modifies;

@PropertyReactive
public class Machine {

	private String model;
	private int memory;
	private int cpuSpeed;
	private String graphicsCard;
	private int price;
	private Collection<String> descriptors = new HashSet<String>();

	public Machine() {
		super();
	}

	@Modifies({ "descriptors" })
	public Collection<String> getDescriptors() {
		return descriptors;
	}

	public void setDescriptors(Collection<String> descriptors) {
		this.descriptors = descriptors;
	}

	public String getModel() {
		return model;
	}

	@Modifies({ "model" })
	public void setModel(String model) {
		this.model = model;
	}

	public int getMemory() {
		return memory;
	}

	@Modifies({ "memory" })
	public void setMemory(int memory) {
		this.memory = memory;
	}

	public int getCpuSpeed() {
		return cpuSpeed;
	}

	@Modifies({ "cpuSpeed" })
	public void setCpuSpeed(int cpuSpeed) {
		this.cpuSpeed = cpuSpeed;
	}

	public String getGraphicsCard() {
		return graphicsCard;
	}

	@Modifies({ "graphicsCard" })
	public void setGraphicsCard(String graphicsCard) {
		this.graphicsCard = graphicsCard;
	}

	public int getPrice() {
		return price;
	}

	@Modifies({ "price" })
	public void setPrice(int price) {
		this.price = price;
	}
}