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

public class issou extends JFrame {
    private JTextArea outputTextArea;
    private JTextArea inputTextArea;
    private JButton loadButton;
    private JButton saveButton;
    private JButton valuesButton;

    public issou() {
        setTitle("Employee Info Modifyer and Reader");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(1, 2));

        // Fenêtre pour afficher le contenu des fichiers JSON
        JPanel jsonViewerPanel = new JPanel(new BorderLayout());

        outputTextArea = new JTextArea();
        outputTextArea.setEditable(false);

        JScrollPane outputScrollPane = new JScrollPane(outputTextArea);
        jsonViewerPanel.add(outputScrollPane, BorderLayout.CENTER);

        loadButton = new JButton("Load Employee File");
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadJSONData();
            }
        });
        jsonViewerPanel.add(loadButton, BorderLayout.SOUTH);

        mainPanel.add(jsonViewerPanel);



        // Fenêtre pour créer des fichiers JSON
        JPanel jsonCreatorPanel = new JPanel(new BorderLayout());

        inputTextArea = new JTextArea();

        JScrollPane inputScrollPane = new JScrollPane(inputTextArea);
        jsonCreatorPanel.add(inputScrollPane, BorderLayout.CENTER);

        saveButton = new JButton("Save Employee File");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveJSONData();
            }
        });
        jsonCreatorPanel.add(saveButton, BorderLayout.SOUTH);

        mainPanel.add(jsonCreatorPanel);

        valuesButton = new JButton("Configure Values");
        valuesButton.addActionListener(new ActionListener () {
            @Override
            public void actionPerformed(ActionEvent e) {
                settingsButton();
            }
        });

        jsonViewerPanel.add(valuesButton, BorderLayout.NORTH);


        add(mainPanel);
        setVisible(true);
    }

    private void loadJSONData() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Employee File", "eimr"));

        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            try {
                File selectedFile = fileChooser.getSelectedFile();
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(selectedFile));

                JSONArray jsonArray = (JSONArray) obj;

                outputTextArea.setText("");

                for (Object itemObj : jsonArray) {
                    JSONObject item = (JSONObject) itemObj;
                    String name = (String) item.get("name");
                    String age = (String) item.get("age");
                    String job = (String) item.get("job");

                    outputTextArea.append("Name: " + name + ", Age: " + age + ", job:"+ job + "\n");
                }
            } catch (IOException | ParseException ex) {
                JOptionPane.showMessageDialog(this, "Error loading JSON file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


private void saveJSONData() {
    JFileChooser fileChooser = new JFileChooser();
    int result = fileChooser.showSaveDialog(this);
    fileChooser.setFileFilter(new FileNameExtensionFilter("Employee File", "eimr"));

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


public static void settingsButton(){
    settings.main(null);

}



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new issou();
            }
        });
    }
}
