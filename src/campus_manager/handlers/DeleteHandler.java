package campus_manager.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import campus_manager.entities.*;
import campus_manager.main.*;


public class DeleteHandler implements ActionListener {
	MainFrame mF;

	public DeleteHandler(MainFrame mF) {
		this.mF = mF;
	}

	public void actionPerformed(ActionEvent e) {
		Manager manager = Manager.createManagerInst(null);
		String delID = mF.deleteField.getText();
		if (mF.deleteStudentRadioButton.isSelected()) {
			Student s;
			if ((s = manager.getStudent(delID)) != null) {
				// find the correct row from JTable if the JTable contains a line corresponding
				// to s
				int index;
				if (manager.filteredStudents == null)
					index = manager.students.indexOf(s);
				else
					index = manager.filteredStudents.indexOf(s);

				// update Modules
				for (Module m : manager.getStudentModules(delID)) {
					m.participants.remove(delID);
					m.data[4] = m.getParticipants();
					
					int modIndex;
					if (manager.filteredModules == null)
						modIndex = manager.modules.indexOf(m);
					else
						modIndex = manager.filteredModules.indexOf(m);

					// do not modify a row from JTable if the JTable does not contain a line
					// corresponding to newModule
					if (modIndex != -1) {
						mF.moduleModel.removeRow(modIndex);
						mF.moduleModel.insertRow(modIndex, m.data);
					}
				}
					
				// do not remove a row from JTable if the JTable does not contain a line
				// corresponding to s
				if (index != -1)
					mF.studentModel.removeRow(index);
				manager.students.remove(s);
			} else {
				JOptionPane.showMessageDialog(null, "There is no student with given matriculation nr: " + delID,
						"Information", JOptionPane.INFORMATION_MESSAGE);
			}
		} else if (mF.deleteProfRadioButton.isSelected()) {
			Professor p;
			if ((p = manager.getProf(delID)) != null) {
				// find the correct row from JTable if the JTable contains a line corresponding
				// to p
				int index;
				if (manager.filteredProfs == null)
					index = manager.profs.indexOf(p);
				else
					index = manager.filteredProfs.indexOf(p);

				int delete = JOptionPane.OK_OPTION;
				ArrayList<Module> profModules = manager.getProfModules(delID);
				String modString = "";
				if (profModules.size() != 0) {
					for (Module m : profModules)
						modString = modString + "\n" + m.nr;
					delete = JOptionPane.showConfirmDialog(null, "The professor with given course " + delID
							+ " offers also following modules:" + modString
							+ "\nAre you sure to deleter the professor ? (Modules are cascading deleted as well!)",
							"Delete?", JOptionPane.OK_CANCEL_OPTION);
					if (delete == JOptionPane.OK_OPTION) {
						// update ModuleTable
						for (Module m : profModules) {
							int modIndex;
							if (manager.filteredModules == null)
								modIndex = manager.modules.indexOf(m);
							else
								modIndex = manager.filteredModules.indexOf(m);

							// do not remove a row from JTable if the JTable does not contain a line
							// corresponding to m
							if (modIndex != -1)
								mF.moduleModel.removeRow(modIndex);
							manager.modules.remove(m);
						}
					}

				}
				if (delete == JOptionPane.OK_OPTION) {
					// do not remove a row from JTable if the JTable does not contain a line
					// corresponding to p
					if (index != -1)
						mF.profModel.removeRow(index);
					manager.profs.remove(p);
				}
			} else {
				JOptionPane.showMessageDialog(null, "There is no professor with given course: " + delID, "Information",
						JOptionPane.INFORMATION_MESSAGE);
			}
		} else {
			Module m;
			if ((m = manager.getModule(delID)) != null) {
				// find the correct row from JTable if the JTable contains a line corresponding
				// to m
				int index;
				if (manager.filteredModules == null)
					index = manager.modules.indexOf(m);
				else
					index = manager.filteredModules.indexOf(m);

				// do not remove a row from JTable if the JTable does not contain a line
				// corresponding to m
				if (index != -1)
					mF.moduleModel.removeRow(index);
				manager.modules.remove(m);
			} else {
				JOptionPane.showMessageDialog(null, "There is no module with given module nr: " + delID, "Information",
						JOptionPane.INFORMATION_MESSAGE);
			}
		}
		mF.deleteField.setText("");
	}
}
