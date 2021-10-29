package swingdemo;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;

import org.mindrot.bcrypt.BCrypt;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;

public class FirstWindow {

	public static final Pattern NAMES_REGEX = Pattern.compile("^[a-zA-Z]+", Pattern.CASE_INSENSITIVE);
	public static final Pattern NUMBERS_REGEX = Pattern.compile("[0-9]+", Pattern.CASE_INSENSITIVE);
	public static final Pattern EMAIL_ADDRESS_REGEX = Pattern
			.compile("^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", Pattern.CASE_INSENSITIVE);

	private static int workload = 12;

	private JFrame frame;
	private JTextField firstName;
	private JTextField lastName;
	private JTextField email;

	private JTextField username;
	private JPasswordField password;
	private JTextField mobile;

	private JLabel mobileLbl;
	private JLabel usernameLbl;
	private JLabel passwordLbl;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FirstWindow window = new FirstWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public FirstWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 547, 368);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		firstName = new JTextField();
		firstName.setBounds(114, 104, 130, 26);
		frame.getContentPane().add(firstName);
		firstName.setColumns(10);

		JLabel firstNameLbl = new JLabel("First Name");
		firstNameLbl.setBounds(25, 109, 77, 16);
		frame.getContentPane().add(firstNameLbl);

		JLabel lastNameLbl = new JLabel("Last Name");
		lastNameLbl.setBounds(25, 147, 77, 16);
		frame.getContentPane().add(lastNameLbl);

		lastName = new JTextField();
		lastName.setColumns(10);
		lastName.setBounds(114, 142, 130, 26);
		frame.getContentPane().add(lastName);

		JLabel emailLbl = new JLabel("Email");
		emailLbl.setBounds(25, 185, 77, 16);
		frame.getContentPane().add(emailLbl);

		email = new JTextField();
		email.setColumns(10);
		email.setBounds(114, 180, 130, 26);
		frame.getContentPane().add(email);

		usernameLbl = new JLabel("Username");
		usernameLbl.setBounds(283, 109, 77, 16);
		frame.getContentPane().add(usernameLbl);

		username = new JTextField();
		username.setColumns(10);
		username.setBounds(372, 104, 130, 26);
		frame.getContentPane().add(username);

		passwordLbl = new JLabel("Password");
		passwordLbl.setBounds(283, 147, 77, 16);
		frame.getContentPane().add(passwordLbl);

		mobileLbl = new JLabel("Mobile");
		mobileLbl.setBounds(283, 185, 77, 16);
		frame.getContentPane().add(mobileLbl);

		mobile = new JTextField();
		mobile.setColumns(10);
		mobile.setBounds(372, 180, 130, 26);
		frame.getContentPane().add(mobile);

		JButton registerBtn = new JButton("Register");
		registerBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String fName = firstName.getText();
				String lName = lastName.getText();
				String emailAddress = email.getText();
				String uname = username.getText();
				String pword = String.valueOf(password.getPassword());
				String number = mobile.getText();
				int mobileLength = number.length();

				Matcher fNameMatcher = NAMES_REGEX.matcher(fName);
				Matcher lNameMatcher = NAMES_REGEX.matcher(lName);
				Matcher emailMatcher = EMAIL_ADDRESS_REGEX.matcher(emailAddress);
				Matcher numberMatcher = NUMBERS_REGEX.matcher(number);

				if (!fNameMatcher.find()) {
					JOptionPane.showMessageDialog(registerBtn, "Enter a valid first name");
				} else if (!lNameMatcher.find()) {
					JOptionPane.showMessageDialog(registerBtn, "Enter a valid last name");
				} else if (!emailMatcher.find()) {
					JOptionPane.showMessageDialog(registerBtn, "Enter a valid email address");
				} else if (mobileLength != 10 || !numberMatcher.find()) {
					JOptionPane.showMessageDialog(registerBtn, "Enter a valid mobile number");
				} else {
					try {
						String salt = BCrypt.gensalt(workload);
						String hpassword = BCrypt.hashpw(pword, salt);

						Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:port/db_name",
								"username", "password");

						String query = "insert into table_name(first_name, last_name, user_name, password, email_id, mobile_number) values('"
								+ fName + "','" + lName + "','" + uname + "','" + hpassword + "','" + emailAddress
								+ "','" + number + "')";

						Statement sta = connection.createStatement();

						int x = sta.executeUpdate(query);

						if (x == 0) {
							JOptionPane.showMessageDialog(registerBtn, "Already exist");
						} else {
							JOptionPane.showMessageDialog(registerBtn, "Your account is sucessfully created");
							firstName.setText("");
							lastName.setText("");
							email.setText("");
							username.setText("");
							password.setText("");
							mobile.setText("");
						}
						connection.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			}
		});
		registerBtn.setBounds(212, 236, 117, 29);
		frame.getContentPane().add(registerBtn);

		JLabel lblNewLabel_1 = new JLabel("User Registration");
		lblNewLabel_1.setBounds(212, 42, 117, 16);
		frame.getContentPane().add(lblNewLabel_1);

		password = new JPasswordField();
		password.setBounds(372, 142, 130, 26);
		frame.getContentPane().add(password);
	}
}
