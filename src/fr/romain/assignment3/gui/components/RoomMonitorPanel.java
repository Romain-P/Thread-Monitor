package fr.romain.assignment3.gui.components;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

public class RoomMonitorPanel extends JPanel {
	private final JTextField waitingVisitors;
	private final JTextField waitingStudents;
	private final JTextField waitingLecturers;
	private final JTextField visitors;
	private final JTextField students;
	private final JTextField lecturer;
	private final RoomPanel room;
	
	public RoomMonitorPanel(String title, int x, int y, int width, int height, int capacity) {
		setBorder(new TitledBorder(null, title, TitledBorder.LEADING, TitledBorder.TOP, null, null));
		setBounds(x, y, width, height);
		setLayout(null);
		
		JPanel waitingPanel = new JPanel();
		waitingPanel.setBorder(new TitledBorder(null, "Waiting", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		waitingPanel.setBounds(10, 27, 386, 67);
		add(waitingPanel);
		waitingPanel.setLayout(null);
		
		JLabel lblVisitors = new JLabel("Visitors");
		lblVisitors.setBounds(10, 21, 46, 14);
		waitingPanel.add(lblVisitors);
		
		waitingVisitors = new JTextField("0");
		waitingVisitors.setEditable(false);
		waitingVisitors.setBounds(10, 36, 61, 20);
		waitingPanel.add(waitingVisitors);
		waitingVisitors.setColumns(10);
		
		waitingStudents = new JTextField("0");
		waitingStudents.setEditable(false);
		waitingStudents.setColumns(10);
		waitingStudents.setBounds(161, 36, 61, 20);
		waitingPanel.add(waitingStudents);
		
		JLabel lblStudents = new JLabel("Students");
		lblStudents.setBounds(161, 21, 46, 14);
		waitingPanel.add(lblStudents);
		
		waitingLecturers = new JTextField("0");
		waitingLecturers.setEditable(false);
		waitingLecturers.setColumns(10);
		waitingLecturers.setBounds(315, 36, 61, 20);
		waitingPanel.add(waitingLecturers);
		
		JLabel lblLecturer = new JLabel("Lecturers");
		lblLecturer.setBounds(315, 21, 46, 14);
		waitingPanel.add(lblLecturer);
		
		JPanel roomPanel = new JPanel();
		roomPanel.setBorder(new TitledBorder(null, "Room", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		roomPanel.setBounds(10, 105, 386, 255);
		add(roomPanel);
		roomPanel.setLayout(null);
		
		visitors = new JTextField("0");
		visitors.setEditable(false);
		visitors.setColumns(10);
		visitors.setBounds(10, 52, 61, 20);
		roomPanel.add(visitors);
		
		JLabel label = new JLabel("Visitors");
		label.setBounds(10, 37, 46, 14);
		roomPanel.add(label);
		
		students = new JTextField("0");
		students.setEditable(false);
		students.setColumns(10);
		students.setBounds(10, 129, 61, 20);
		roomPanel.add(students);
		
		JLabel label_1 = new JLabel("Students");
		label_1.setBounds(10, 114, 46, 14);
		roomPanel.add(label_1);
		
		lecturer = new JTextField();
		lecturer.setEditable(false);
		lecturer.setColumns(10);
		lecturer.setBounds(10, 209, 61, 20);
		roomPanel.add(lecturer);
		
		JLabel label_2 = new JLabel("Lecturer");
		label_2.setBounds(10, 194, 46, 14);
		roomPanel.add(label_2);
		
		room = new RoomPanel(222, 222, capacity);
		room.setBounds(123, 22, 222, 222);
		roomPanel.add(room);
	}

	public RoomPanel room() {
		return this.room;
	}

	public void addWaitingLecturer() {
		int count = Integer.parseInt(waitingLecturers.getText());
		String sCount = String.valueOf(++count);
		waitingLecturers.setText(sCount);
	}

	public void delWaitingLecturer() {
		int count = Integer.parseInt(waitingLecturers.getText());
		String sCount = String.valueOf(--count);
		waitingLecturers.setText(sCount);
	}

	public void addWaitingVisitor() {
		int count = Integer.parseInt(waitingVisitors.getText());
		String sCount = String.valueOf(++count);
		waitingVisitors.setText(sCount);
	}

	public void delWaitingVisitor() {
		int count = Integer.parseInt(waitingVisitors.getText());
		String sCount = String.valueOf(--count);
		waitingVisitors.setText(sCount);
	}

	public void addWaitingStudent() {
		int count = Integer.parseInt(waitingStudents.getText());
		String sCount = String.valueOf(++count);
		waitingStudents.setText(sCount);
	}

	public void delWaitingStudent() {
		int count = Integer.parseInt(waitingStudents.getText());
		String sCount = String.valueOf(--count);
		waitingStudents.setText(sCount);
	}

	public void addVisitor() {
		int count = Integer.parseInt(visitors.getText());
		String sCount = String.valueOf(++count);
		visitors.setText(sCount);
	}

	public void delVisitor() {
		int count = Integer.parseInt(visitors.getText());
		String sCount = String.valueOf(--count);
		visitors.setText(sCount);
	}

	public void addStudent() {
		int count = Integer.parseInt(students.getText());
		String sCount = String.valueOf(++count);
		students.setText(sCount);
	}

	public void delStudent() {
		int count = Integer.parseInt(students.getText());
		String sCount = String.valueOf(--count);
		students.setText(sCount);
	}

	public void removeLecturer() {
		lecturer.setText("");
	}

	public void setLecturer(String lecturer) {
		this.lecturer.setText(lecturer);
	}

	public RoomPanel getRoom() {
		return room;
	}
}
