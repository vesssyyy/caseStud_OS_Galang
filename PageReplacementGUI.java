package simulation;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class PageReplacementGUI {
    private JFrame frame;
    private JTextField nField;
    private JTextField frameField;
    private JTextArea fifoArea, lruArea, optArea;
    private JPanel cardPanel;
    private CardLayout cardLayout;

    private JButton generateButton, resetButton;
    private boolean dataGenerated = false;

    private static Map<String, JTextArea> outputAreas = new HashMap<>();
    private int[] pages;
    private int frames;

    public PageReplacementGUI() {
        frame = new JFrame("Page Replacement Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(900, 600);
        frame.setLayout(new BorderLayout());

        ImageIcon icon = new ImageIcon("yor.jpg");
        frame.setIconImage(icon.getImage());
        
        // Top input panel
        JPanel inputPanel = new JPanel();
        inputPanel.add(new JLabel("n:"));
        nField = new JTextField(5);
        inputPanel.add(nField);

        inputPanel.add(new JLabel("frame:"));
        frameField = new JTextField(5);
        inputPanel.add(frameField);

        generateButton = new JButton("Generate");
        resetButton = new JButton("Reset");

        generateButton.addActionListener(e -> generateData());
        resetButton.addActionListener(e -> resetAll());

        inputPanel.add(generateButton);
        inputPanel.add(resetButton);

        frame.add(inputPanel, BorderLayout.NORTH);

        // Card panel with areas
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        fifoArea = createTextArea();
        lruArea = createTextArea();
        optArea = createTextArea();

        cardPanel.add(new JScrollPane(fifoArea), "FIFO");
        cardPanel.add(new JScrollPane(lruArea), "LRU");
        cardPanel.add(new JScrollPane(optArea), "OPT");

        outputAreas.put("FIFO", fifoArea);
        outputAreas.put("LRU", lruArea);
        outputAreas.put("OPT", optArea);

        frame.add(cardPanel, BorderLayout.CENTER);

        // Navigation panel
        JPanel navPanel = new JPanel();

        JButton fifoBtn = new JButton("FIFO");
        JButton lruBtn = new JButton("LRU");
        JButton optBtn = new JButton("OPT");

        navPanel.add(fifoBtn);
        navPanel.add(lruBtn);
        navPanel.add(optBtn);

        fifoBtn.addActionListener(e -> {
            if (dataGenerated) {
                simulate("FIFO");
                cardLayout.show(cardPanel, "FIFO");
            }
        });

        lruBtn.addActionListener(e -> {
            if (dataGenerated) {
                simulate("LRU");
                cardLayout.show(cardPanel, "LRU");
            }
        });

        optBtn.addActionListener(e -> {
            if (dataGenerated) {
                simulate("OPT");
                cardLayout.show(cardPanel, "OPT");
            }
        });

        frame.add(navPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    private JTextArea createTextArea() {
        JTextArea area = new JTextArea();
        area.setFont(new Font("Monospaced", Font.PLAIN, 14));
        area.setEditable(false);
        return area;
    }

    private void generateData() {
        int n;
        try {
            n = Integer.parseInt(nField.getText().trim());
            frames = Integer.parseInt(frameField.getText().trim());
            if (n <= 0 || frames <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Please enter valid integers > 0 for n and frame.");
            return;
        }

        pages = new int[n];
        Random rand = new Random();
        for (int i = 0; i < n; i++) {
            pages[i] = rand.nextInt(10);  // You can change range here if needed
        }

        nField.setEditable(false);
        frameField.setEditable(false);
        generateButton.setEnabled(false);

        dataGenerated = true;

        // Display page sequence in each algorithm text area
        String header = "Generated Pages: " + Arrays.toString(pages) + "\nFrames: "+frames;
        fifoArea.setText(header);
        lruArea.setText(header);
        optArea.setText(header);

        JOptionPane.showMessageDialog(frame, "Data generated! Now choose an algorithm below.");
    }

    private void resetAll() {
        pages = null;
        dataGenerated = false;

        fifoArea.setText("");
        lruArea.setText("");
        optArea.setText("");

        nField.setText("");
        frameField.setText("");
        nField.setEditable(true);
        frameField.setEditable(true);
        generateButton.setEnabled(true);
    }

    private void simulate(String targetAlgo) {
        if (pages == null || frames <= 0) return;

        JTextArea area = outputAreas.get(targetAlgo);
        area.setText(targetAlgo + " Algorithm\n\n");

        printHeader(targetAlgo);

        int faults = 0;
        switch (targetAlgo) {
            case "FIFO":
                faults = PageReplacementAlgo.fifoPageReplacement(pages, frames);
                break;
            case "LRU":
                faults = PageReplacementAlgo.lruPageReplacement(pages, frames);
                break;
            case "OPT":
                faults = PageReplacementAlgo.optPageReplacement(pages, frames);
                break;
        }

        area.insert("Page Faults: " + faults + "\n\n", 0);
    }

    private void printHeader(String algo) {
        JTextArea area = outputAreas.get(algo);
        area.append("Pages: " + Arrays.toString(pages) + "\n\n");
        area.append(String.format("%-7s", "Page"));
        for (int i = 1; i <= frames; i++) {
            area.append(String.format("%-10s", "Frame" + i));
        }
        area.append("%-10s\n".formatted("Fault?"));
    }

    public static void displayCycle(String algo, int page, int[] memory, boolean fault) {
        JTextArea area = outputAreas.get(algo);
        if (area != null) {
            StringBuilder sb = new StringBuilder(); 
            sb.append(String.format("%-7s", page));
            for (int m : memory) {
                sb.append(String.format("%-10s", m == -1 ? "-" : m));
            }
            sb.append(String.format("%-10s", fault ? "Yes" : "No"));
            sb.append("\n");
            area.append(sb.toString());
        }
    }

    public static void main(String[] args) {
    	PageReplacementGUI start = new PageReplacementGUI();
    }
}
