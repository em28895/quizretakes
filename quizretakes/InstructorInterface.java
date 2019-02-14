package quizretakes;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* 
 * Group Members:
 * SM Nazibullah Touhid
 * Edwin Moran Gomez
 * Gussan Ali
 * 
 */

/**
* @author SM Nazibullah Touhid, Gussan Ali, Edwin Moran Gomez
*         Date: February, 2019
* InstructorInterface.java -- Read all the quiz retake appointments from 
* 							  file and creates an Interface for Instructor to
* 							  view all the appointments.
*/
public class InstructorInterface {

	private static quizschedule schedule;

	public static void main(String[] args) throws IOException {

		Scanner keyboard = new Scanner(System.in);
		int choice = 0;
		boolean validInput = false;
		do {
			System.out.println("------------------GMU Quiz Scheduler----------------");
			System.out.println("Instructor View: Press 1\nStudent View: Press 2\n");
			System.out.print("Enter your choice: ");
			choice = keyboard.nextInt();
			keyboard.nextLine();

			if (choice == 1) {
				System.out.println("--------------Welcome Instructor---------------");
				callProfessorInterface();
				validInput = true;
			} else if (choice == 2) {
				System.out.println("---------------Welcome Student------------------");
				callStudent();
				validInput = true;
			} else {
				System.out.println("Invalid Input! Please Try again\n");
			}
		} while (validInput == false);

		keyboard.close();
	}

	public static void callProfessorInterface() throws IOException {
		// change the data location
		final String dataLocation = "E:\\Mason\\CS 310\\Workspace\\quizretakes\\src\\quizretakes\\";

		Scanner keyboard = new Scanner(System.in);
		System.out.println("Enter the course ID");
		String courseID = keyboard.nextLine();
		//file names
		String apptsFileName = dataLocation + "quiz-appts-" + courseID + ".txt";
		String retakesFileName = dataLocation + "quiz-retakes-" + courseID + ".xml";
		
		//reads the data fine and puts each line into an array of object
		try (Stream<String> lines = Files.lines(Paths.get(apptsFileName))) {
			Object[] thisArray = lines.toArray();

			ArrayList<String[]> quizList = new ArrayList<>();
			//takes each line form object array and splits the string by "," and puts it into a array of String
			for (int i = 0; i < thisArray.length; i++) {
				String[] s = thisArray[i].toString().split(",");
				quizList.add(s);
			}
			// read quiz-retakes-swe437.xml for quiz date, location and time
			try {
				File fXmlFile = new File(retakesFileName);
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(fXmlFile);
				doc.getDocumentElement().normalize();
				System.out.println("---------------All Retake Schedule----------------");
				for (String[] student : quizList) {
					NodeList nList = doc.getElementsByTagName("retake");

					System.out.print(
							"Student Name: " + student[2] + "\n" + "Quiz: " + student[1] + "\n" + "Retake Date: ");
					// looks for specific tag in the .xml file for date, location and time
					for (int temp = 0; temp < nList.getLength(); temp++) {
						Node nNode = nList.item(temp);
						Element eElement = (Element) nNode;

						int stuID = Integer.parseInt(student[0]);
						int xmlID = Integer.parseInt(eElement.getElementsByTagName("id").item(0).getTextContent());

						if (stuID == xmlID) {

							if (nNode.getNodeType() == Node.ELEMENT_NODE) {
								System.out.print(eElement.getElementsByTagName("month").item(0).getTextContent() + "/");
								System.out.print(eElement.getElementsByTagName("day").item(0).getTextContent());
							}
							System.out.print("\n");
							System.out.print("Location: ");
							if (nNode.getNodeType() == Node.ELEMENT_NODE) {
								System.out.print(eElement.getElementsByTagName("location").item(0).getTextContent());
								System.out.print("\n");
								System.out.print("Time: ");
								System.out.print(eElement.getElementsByTagName("hour").item(0).getTextContent() + ":");
								System.out.print(eElement.getElementsByTagName("minute").item(0).getTextContent());
							}
							System.out.println("\n");

						} else {
						}
					}
				}
			}

			catch (Exception e) {
				System.out.println("Error in File!");
			}
		}

		catch (Exception e) {
			System.out.println("Can't find the file: " + apptsFileName);
		}

		keyboard.close();
	}
	// this method call the student view interface
	public static void callStudent() {
		schedule = new quizschedule();
		schedule.callMain();
	}
}
