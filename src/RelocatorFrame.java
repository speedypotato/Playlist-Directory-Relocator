import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

public class RelocatorFrame extends JFrame {
	/**
	 * Runner main method
	 */
	public static void main(String[] args) {
		new RelocatorFrame();
	}
	
	/**
	 * Constructor
	 */
	public RelocatorFrame() {
		setTitle("Playlist Directory Relocator");
		setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		
		checkmk[0] = new JLabel("x");
		fields[0] = new JTextField("Path to PlayList?");
		fields[0].addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
				if (!accessed[0]) fields[0].setText("");
			}
			@Override
			public void focusLost(FocusEvent fe) {
				if (fields[0].getText().equals("") || fields[0].getText().equals("Path to PlayList?")) {
					fields[0].setText("Path to PlayList?");
					accessed[0] = false;
					checkmk[0].setText("x");
				} else {
					accessed[0] = true;
					if (verifyPath(fields[0].getText())) checkmk[0].setText("o");
					else checkmk[0].setText("x");
				}
			}
		});
		
		checkmk[1] = new JLabel("x");
		fields[1] = new JTextField("Current PlayList Directory?");
		fields[1].addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
				if (!accessed[1]) fields[1].setText("");
			}
			@Override
			public void focusLost(FocusEvent fe) {
				if (fields[1].getText().equals("") || fields[1].getText().equals("Current PlayList Directory?")) {
					fields[1].setText("Current PlayList Directory?");
					accessed[1] = false;
					checkmk[1].setText("x");
				} else {
					accessed[1] = true;
					checkmk[1].setText("o");
				}
			}
		});
		
		checkmk[2] = new JLabel("x");
		fields[2] = new JTextField("Desired PlayList Directory?");
		fields[2].addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent fe) {
				if (!accessed[2]) fields[2].setText("");
			}
			@Override
			public void focusLost(FocusEvent fe) {
				if (fields[2].getText().equals("") || fields[2].getText().equals("Desired PlayList Directory?")) {
					fields[2].setText("Desired PlayList Directory?");
					accessed[2] = false;
					checkmk[2].setText("x");
				} else {
					accessed[2] = true;
					checkmk[2].setText("o");
				}
			}
		});
		
		JButton goButton = new JButton("Go!");
		goButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent ae) {
				for (JLabel l : checkmk) {
					if (l.getText().equals("x")) {
						JOptionPane.showMessageDialog(new JFrame(), "One of the parameters is invalid.");
						return;
					}
				}
				File file = new File(fields[0].getText());
				Relocator r = new Relocator(file, fields[1].getText(), fields[2].getText());
				if (r.write()) JOptionPane.showMessageDialog(new JFrame(), "Operation Successful.  Check same folder for file with the prefix \"rel_\".");
				else JOptionPane.showMessageDialog(new JFrame(), "An Error Occured");
			}
		});
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setPreferredSize(new Dimension((int)screenSize.getWidth() / 8, (int)screenSize.getHeight() / 7));
		
		Container[] cFields = new Container[3];
		for (int i = 0; i < 3; i++) {
			cFields[i] = new Container();
			cFields[i].setLayout(new BoxLayout(cFields[i], BoxLayout.X_AXIS));
			fields[i].setFont(new Font("SansSerif", Font.PLAIN, 25));
			cFields[i].add(fields[i]);
			cFields[i].add(checkmk[i]);
		}
		
		for (Container c : cFields) add(c);
		goButton.setAlignmentX(Component.CENTER_ALIGNMENT);
		goButton.setFont(new Font("SansSerif", Font.PLAIN, 25));
		add(goButton);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		pack();
		setVisible(true);
	}
	
	/**
	 * Verify if file exists and ends in .m3u or .m3u8
	 * @param path Path to file
	 * @return If file exists
	 */
	private boolean verifyPath(String path) {
		if (path.lastIndexOf(".") != -1 && 
				(path.substring(path.lastIndexOf(".")).equals(".m3u") || path.substring(path.lastIndexOf(".")).equals(".m3u8"))) {
			File f = new File(path);
			return f.exists();
		} else {
			return false;
		}
	}
	
	JLabel[] checkmk = new JLabel[3];
	JTextField[] fields = new JTextField[3];
	boolean[] accessed = new boolean[3];
}
