import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class UTF8toBOM {
    public static void main(String[] args) {
        createUI();


    }

    /**
     * Here we store the project path, as soon as it is set.
     */
    private static String projectPath = null;

    private static JFrame window;

    /**
     * We pack the UI-generation here, because I hate java swing...
     * The window frame is spartanic, and I will not work any more than necessary on it.
     * Strings are stored in {@link Strings}
     */
    private static void createUI() {
        // Main window of the tool
        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        window.setSize(600,300);

        window.setTitle("BOM Tool");

        // Adding UI elements
        // Defining the layout
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0, 0, 0};
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0};
        gbl_contentPane.columnWeights = new double[]{0.0, 1.0, 0.0, Double.MIN_VALUE};
        gbl_contentPane.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
        window.setLayout(gbl_contentPane);

        // Here the headline gets defined
        JLabel headline = new JLabel(Strings.main_window_headline);
        headline.setFont(new Font("Tahoma", Font.BOLD, 16));

        GridBagConstraints headlineConstraint = new GridBagConstraints();
        headlineConstraint.gridx = 0;
        headlineConstraint.gridy = 0;
        headlineConstraint.gridwidth = 3;
        headlineConstraint.insets = new Insets(15, 15, 5, 5);
        window.add(headline, headlineConstraint);

        // A short description
        JLabel description = new JLabel(Strings.main_window_tool_description);

        GridBagConstraints descriptionConstrain = new GridBagConstraints();
        descriptionConstrain.gridx = 0;
        descriptionConstrain.gridy = 1;
        descriptionConstrain.fill = GridBagConstraints.HORIZONTAL;
        descriptionConstrain.gridwidth = 3;
        descriptionConstrain.insets = new Insets(0, 15, 5, 15);

        window.add(description, descriptionConstrain);

        // Add a descriptive label
        JLabel descPathLabel = new JLabel(Strings.main_window_path_desc);

        GridBagConstraints gbc_pathDesc = new GridBagConstraints();
        gbc_pathDesc.gridx=0;
        gbc_pathDesc.gridy=2;
        gbc_pathDesc.insets = new Insets(0,15,5,5);

        window.add(descPathLabel, gbc_pathDesc);

        // Add a text field for the project path
        HintTextField projectPathTextField = new HintTextField(Strings.main_window_path_hint);
        // no manual entry
        projectPathTextField.setEditable(false);

        GridBagConstraints pathTextFieldGBC = new GridBagConstraints();
        pathTextFieldGBC.gridwidth = 1;
        pathTextFieldGBC.fill = GridBagConstraints.HORIZONTAL;
        pathTextFieldGBC.gridx=1;
        pathTextFieldGBC.gridy=2;
        pathTextFieldGBC.insets = new Insets(0, 0, 5, 5);

        // Clicking on the text field will open the file chooser
        projectPathTextField.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

                int choseOption = fileChooser.showOpenDialog(window);

                if(choseOption == JFileChooser.APPROVE_OPTION) {
                    projectPath = fileChooser.getSelectedFile().getAbsolutePath();
                    projectPathTextField.setText(projectPath);
                }
            }

            // We don't need those, therefore empty
            @Override
            public void mousePressed(MouseEvent e) { }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });

        window.add(projectPathTextField, pathTextFieldGBC);


        JLabel lblNote = new JLabel("");
        // Add the button to start the process
        JButton btnStart = new JButton(Strings.main_window_button_start);
        GridBagConstraints gbc_btnStart = new GridBagConstraints();
        gbc_btnStart.gridx = 2;
        gbc_btnStart.gridy = 2;
        gbc_btnStart.insets = new Insets(0, 0, 5, 15);

        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Here, the magic happens
                Converter.Status status = Converter.processProject(projectPath);

                // The info label will be manipulated here
                switch (status){
                    case success:
                        lblNote.setText(Strings.note_success);
                        break;
                    case emptyPath:
                        lblNote.setText(Strings.note_empty_path);
                        break;
                    case notAMod:
                        lblNote.setText(Strings.note_not_a_mod);
                        break;
                    case notADirectory:
                        lblNote.setText(Strings.note_not_a_directory);
                        break;
                    case unknownError:
                        lblNote.setText(Strings.note_unknown_error);
                        break;
                }
            }
        });

        window.add(btnStart, gbc_btnStart);

        // Adding a note label to post errors or success for the user
        GridBagConstraints gbc_lblNote = new GridBagConstraints();
        gbc_lblNote.insets = new Insets(0, 0, 0, 5);
        gbc_lblNote.gridx = 0;
        gbc_lblNote.gridy = 3;
        gbc_lblNote.gridwidth = 3;
        window.add(lblNote, gbc_lblNote);



        // Show everything
        window.setVisible(true);
    }

    /**
     *  Displaying a simple dialog for some tool feedback
     * @param ymlListSize Count of how many .yml files there were
     * @param ymlConverted Count of how many .yml files were converted
     * @param txtListSize Count of how many .txt files there were - unused as of yet
     * @param txtConverted Count of how many .txt files were converted - unused as of yet
     */
    public static void showSuccessDialog(int ymlListSize, int ymlConverted, int txtListSize, int txtConverted) {
        JOptionPane.showMessageDialog(window, "Of " + ymlListSize +" .yml files a total of " + ymlConverted + " were converted to the UTF-8 BOM encoding!");
    }
}
