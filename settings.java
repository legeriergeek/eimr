import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.*;


public class settings extends JFrame {
    private JButton Accept;
    private JTextArea inputTextArea;
    private JTextArea inputTextArea2;
    private JTextArea inputTextArea3;
    


    public settings(){
        setTitle("Args Settings Window");
        setSize(500, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel mainPanel = new JPanel(new GridLayout(1, 6));

        // Fenêtre pour afficher le contenu des fichiers JSON
        JPanel jsonViewerPanel = new JPanel(new BorderLayout());

        Accept = new JButton("Configure Values");
        Accept.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveArgsData();
            }
        });

        jsonViewerPanel.add(Accept, BorderLayout.SOUTH);
        mainPanel.add(jsonViewerPanel);

    
        inputTextArea = new JTextArea();
        inputTextArea.setEditable(true);

        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        jsonViewerPanel.add(inputScrollPane);

        inputTextArea2 = new JTextArea(2, 2);
        inputTextArea2.setEditable(true);
        
        JScrollPane inputScrollPane2 = new JScrollPane(inputTextArea2);
        jsonViewerPanel.add(inputScrollPane2);

        inputTextArea3 = new JTextArea(1, 1);
        inputTextArea3.setEditable(true);
        
        JScrollPane inputScrollPane3 = new JScrollPane(inputTextArea3);
        jsonViewerPanel.add(inputScrollPane3);



        add(mainPanel);
        setVisible(true);


        

   

    
}

public void HaHA() {
    System.out.println("HAHa");
}

private void saveArgsData(){
    JFileChooser fileChooser = new JFileChooser();
    int result = fileChooser.showSaveDialog(this);
    fileChooser.setFileFilter(new FileNameExtensionFilter("Args.json file", "json"));

    if (result == JFileChooser.APPROVE_OPTION) {
        File selectedFile = fileChooser.getSelectedFile();

        try {
            // Récupérer la chaîne JSON de la zone de texte
            String input = inputTextArea.getText();

            // Supprimer les espaces supplémentaires et le point-virgule final
            input = input.trim().replaceAll(";$", "");

            // Diviser la chaîne en utilisant la virgule comme séparateur des paires clé-valeur
            String[] keyValuePairs = input.split(",\\s*");


            // Créer un nouvel objet JSON
            JSONObject jsonObject = new JSONObject();

            for (String pair : keyValuePairs) {
                // Séparer la clé et la valeur en utilisant le premier deux-points
                int separatorIndex = pair.indexOf(":");
                if (separatorIndex > 0) {
                    String key = pair.substring(0, separatorIndex);
                    String value = pair.substring(separatorIndex + 1);

                    // Supprimer les espaces autour de la clé et de la valeur
                    key = key.trim();
                    value = value.trim();

                    // Convertir la clé en minuscules
                    key = key.toLowerCase();

                    // Ajouter la paire clé-valeur à l'objet JSON
                    jsonObject.put(key, value);
                    String arg1 = (key);
                    System.out.println(arg1);
                    
                }
            }
            // Créer un tableau JSON contenant l'objet JSON créé
            JSONArray jsonArray = new JSONArray();
            jsonArray.add(jsonObject);

            // Enregistrer le contenu JSON dans le fichier
            Files.write(Paths.get(selectedFile.toURI()), jsonArray.toJSONString().getBytes());

            JOptionPane.showMessageDialog(this, "JSON data saved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error saving JSON data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}



public static void main(String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
        @Override
        public void run() {
            new settings();
        }
    });
}


}