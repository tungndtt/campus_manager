package campus_manager.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import campus_manager.entities.*;


public class Manager {
	private static MainFrame mainFrame = null;
	public String studentFile = "txtdb/Students.txt";
	public ArrayList<Student> students = new ArrayList<Student>();
	public ArrayList<Student> filteredStudents = null;
	public String filterStudents = "";
	public String profFile = "txtdb/Professors.txt";
	public ArrayList<Professor> profs = new ArrayList<Professor>();
	public ArrayList<Professor> filteredProfs = null;
	public String filterProfs = "";
	public String moduleFile = "txtdb/Modules.txt";
	public ArrayList<Module> modules = new ArrayList<Module>();
	public ArrayList<Module> filteredModules = null;
	public String filterModules = "";

	static Manager inst = null;

	public static Manager createManagerInst(MainFrame mF) {
		if (mainFrame == null)
			mainFrame = mF;
		if (inst == null)
			inst = new Manager();
		return inst;
	}

	private Manager() {
		String infoMessage = null;
		try {
			readStudent(studentFile);
			readProf(profFile);
			readModule(moduleFile);
		} catch (FileNotFoundException e) {
			infoMessage = e.getMessage();
		}
		if (infoMessage != null)
			JOptionPane.showMessageDialog(null, infoMessage, "Information", JOptionPane.INFORMATION_MESSAGE);
	}

	@SuppressWarnings("resource")
	public void readStudent(String fileName) throws FileNotFoundException {
		String outString = "";
		ArrayList<String> lines = new ArrayList<String>();
		Scanner sc = new Scanner(new FileInputStream(new File(fileName)), "UTF-8");
		while (sc.hasNextLine()) {
			lines.add(sc.nextLine());
		}
		String[] data;
		for (String info : lines) {
			data = info.split("\t");
			if (getStudent(data[5]) == null) {
				students.add(new Student(data));
				mainFrame.studentModel.addRow(data);
			} else {
				if (!outString.isEmpty())
					outString = outString + ", ";
				outString = outString + data[5];
			}
		}
		mainFrame.resizeColumnWidth(mainFrame.studentTable);
		if (!outString.isEmpty())
			JOptionPane.showMessageDialog(null,
					"The student data with given matriculation nrs " + outString
							+ " already exists. They cannot be replaced !",
					"Information", JOptionPane.INFORMATION_MESSAGE);
	}

	@SuppressWarnings("resource")
	public void readProf(String fileName) throws FileNotFoundException {
		String outString = "";
		ArrayList<String> lines = new ArrayList<String>();
		Scanner sc = new Scanner(new FileInputStream(new File(fileName)), "UTF-8");
		while (sc.hasNextLine()) {
			lines.add(sc.nextLine());
		}
		String[] data;
		for (String info : lines) {
			data = info.split("\t");
			if (getProf(data[5]) == null) {
				profs.add(new Professor(data));
				mainFrame.profModel.addRow(data);
			} else {
				if (!outString.isEmpty())
					outString = outString + ", ";
				outString = outString + data[5];
			}
		}
		mainFrame.resizeColumnWidth(mainFrame.profTable);
		if (!outString.isEmpty())
			JOptionPane.showMessageDialog(null,
					"The professors data with given courses " + outString
							+ " already exists. They cannot be replaced !",
					"Information", JOptionPane.INFORMATION_MESSAGE);
	}

	@SuppressWarnings("resource")
	public void readModule(String fileName) throws FileNotFoundException {
		String outString = "";
		ArrayList<String> lines = new ArrayList<String>();
		Module m;
		Scanner sc = new Scanner(new FileInputStream(new File(fileName)), "UTF-8");
		while (sc.hasNextLine()) {
			lines.add(sc.nextLine());
		}
		String[] data;
		for (String info : lines) {
			data = info.split("\t");
			if (getModule(data[1]) == null) {
				m = new Module(data, this);
				if (getProf(m.profID) == null) {
					JOptionPane.showMessageDialog(null,
							"Professor with course " + m.profID + " does not exist. The module with given module nr "
									+ m.nr + " cannot be created !",
							"Information", JOptionPane.INFORMATION_MESSAGE);
				} else {
					modules.add(m);
					mainFrame.moduleModel.addRow(m.data);
				}
			} else {
				if (!outString.isEmpty())
					outString = outString + ", ";
				outString = outString + data[1];
			}
		}
		mainFrame.resizeColumnWidth(mainFrame.moduleTable);
		if (!outString.isEmpty())
			JOptionPane.showMessageDialog(null,
					"The modules data with module nrs " + outString
							+ " already exists. They cannot be replaced !",
					"Information", JOptionPane.INFORMATION_MESSAGE);
	}

	public ArrayList<Student> searchStudentName(String name) {
		ArrayList<Student> output = students.stream().filter(s -> s.name.contains(name) || s.famName.contains(name)).collect(Collectors.toCollection(ArrayList::new));
		if (output.size() == 0) {
			JOptionPane.showMessageDialog(null, "No student with given name: " + name, "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		return output;
	}

	public ArrayList<Professor> searchProfName(String name) {
		ArrayList<Professor> output =  profs.stream().filter(p -> p.name.contains(name) || p.famName.contains(name)).collect(Collectors.toCollection(ArrayList::new));
		if (output.size() == 0) {
			JOptionPane.showMessageDialog(null, "Es gibt keinen Professoren mit Teil - Namen: " + name, "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		return output;
	}

	public ArrayList<Module> searchModuleName(String name) {
		ArrayList<Module> output = modules.stream().filter(m -> m.name.contains(name)).collect(Collectors.toCollection(ArrayList::new));
		if (output.size() == 0) {
			JOptionPane.showMessageDialog(null, "No module with given name: " + name, "Information",
					JOptionPane.INFORMATION_MESSAGE);
			return null;
		}
		return output;
	}

	public Student getStudent(String studID) {
		for (Student s : students)
			if (s.studID.equals(studID))
				return s;
		return null;
	}

	public Professor getProf(String staffID) {
		for (Professor p : profs)
			if (p.staffID.equals(staffID))
				return p;
		return null;
	}

	public Module getModule(String nr) {
		for (Module m : modules)
			if (m.nr.equals(nr))
				return m;
		return null;
	}

	public void showStudents(ArrayList<Student> list) {
		while (mainFrame.studentModel.getRowCount() != 0)
			mainFrame.studentModel.removeRow(0);
		for (Student s : list) {
			mainFrame.studentModel.addRow(s.data);
		}
		if (list.size() != students.size())
			filteredStudents = list;
		else
			filteredStudents = null;
		mainFrame.resizeColumnWidth(mainFrame.studentTable);
	}

	public void showProfs(ArrayList<Professor> list) {
		while (mainFrame.profModel.getRowCount() != 0)
			mainFrame.profModel.removeRow(0);
		for (Professor p : list) {
			mainFrame.profModel.addRow(p.data);
		}
		if (list.size() != profs.size())
			filteredProfs = list;
		else
			filteredProfs = null;
		mainFrame.resizeColumnWidth(mainFrame.profTable);
	}

	public void showModules(ArrayList<Module> list) {
		while (mainFrame.moduleModel.getRowCount() != 0)
			mainFrame.moduleModel.removeRow(0);
		for (Module m : list) {
			mainFrame.moduleModel.addRow(m.data);
		}
		if (list.size() != modules.size())
			filteredModules = list;
		else
			filteredModules = null;
		mainFrame.resizeColumnWidth(mainFrame.moduleTable);
	}

	public ArrayList<Module> getStudentModules(String studID) {
		ArrayList<Module> out = new ArrayList<Module>();
		for (Module m : modules) {
			if (m.participants.contains(studID))
				out.add(m);
		}
		return out;
	}

	public ArrayList<Module> getProfModules(String staffID) {
		ArrayList<Module> out = new ArrayList<Module>();
		for (Module m : modules) {
			if (m.profID.equals(staffID))
				out.add(m);
		}
		return out;
	}

	public void writeStudent(String fileName) throws IOException {
		FileWriter fw = new FileWriter(fileName);
		BufferedWriter bw = new BufferedWriter(fw);

		for (int i = 0; i < students.size(); i++) {
			bw.write(students.get(i).getString());
			if (i < students.size() - 1)
				bw.newLine();
		}
		bw.close();
	}

	public void writeProf(String fileName) throws IOException {
		FileWriter fw = new FileWriter(fileName);
		BufferedWriter bw = new BufferedWriter(fw);

		for (int i = 0; i < profs.size(); i++) {
			bw.write(profs.get(i).getString());
			if (i < profs.size() - 1)
				bw.newLine();
		}
		bw.close();
	}

	public void writeModule(String fileName) throws IOException {
		FileWriter fw = new FileWriter(fileName);
		BufferedWriter bw = new BufferedWriter(fw);

		for (int i = 0; i < modules.size(); i++) {
			bw.write(modules.get(i).getString());
			if (i < modules.size() - 1)
				bw.newLine();
		}
		bw.close();
	}

	public void writeJoin(String fileName) throws IOException {
		FileWriter fw = new FileWriter(fileName);
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i = 0; i < mainFrame.joinModel.getRowCount(); i++) {
			String row = "";
			for (int j = 0; j < mainFrame.joinModel.getColumnCount(); j++) {
				row = row + mainFrame.joinModel.getValueAt(i, j);
				if (j < mainFrame.joinModel.getColumnCount() - 1)
					row = row + "\t";
			}
			bw.write(row);
			if (i < mainFrame.joinModel.getRowCount() - 1)
				bw.newLine();
		}
		bw.close();
	}
}