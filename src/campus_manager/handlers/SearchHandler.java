package campus_manager.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import campus_manager.entities.*;
import campus_manager.main.*;


public class SearchHandler implements ActionListener {
	MainFrame mF;

	public SearchHandler(MainFrame mF) {
		this.mF = mF;
	}

	public void actionPerformed(ActionEvent e) {
		String filter = mF.searchField.getText();
		if (filter.equals(""))
			JOptionPane.showMessageDialog(null, "This field is not filled yet.", "Information",
					JOptionPane.INFORMATION_MESSAGE);
		else {
			Manager manager = Manager.createManagerInst(null);

			if (mF.searchStudentRadioButton.isSelected()) {
				ArrayList<Student> searchResult = manager.searchStudentName(filter);
				if (searchResult != null) {
					manager.showStudents(searchResult);
					manager.filterStudents = filter;
					mF.resetButton.setVisible(true);
				}
			} else if (mF.searchProfRadioButton.isSelected()) {
				ArrayList<Professor> searchResult = manager.searchProfName(filter);
				if (searchResult != null) {
					manager.showProfs(searchResult);
					manager.filterProfs = filter;
					mF.resetButton.setVisible(true);
				}
			} else {
				ArrayList<Module> searchResult = manager.searchModuleName(filter);
				if (searchResult != null) {
					manager.showModules(searchResult);
					manager.filterModules = filter;
					mF.resetButton.setVisible(true);
				}
			}
		}
	}
}
