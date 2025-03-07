import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CoordinatorAgent extends Agent {
    private JFrame frame;
    private JTextField agentCountField, tickerTimeField;
    private JTextArea logArea;

    @Override
    protected void setup() {
        System.out.println("Coordinator Agent started.");
        createGUI();

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null) {
                    String content = msg.getContent();
                    log("Received from " + msg.getSender().getName() + ": " + content);

                    log("Forwarding message to server: " + content);
                    // Forward the message to the server and get the response
                    String serverResponse = forwardToServer(content);

                    // Send the response back to AgentSmith
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setContent(serverResponse);
                    send(reply);
                } else {
                    block(); // Wait for new messages
                }
            }
        });
    }

    private void createGUI() {
        frame = new JFrame("Coordinator Agent GUI");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        frame.setLocationRelativeTo(null); // Center the window

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Padding

        // Label and TextField for Number of Agents
        gbc.gridx = 0; gbc.gridy = 0;
        frame.add(new JLabel("Number of Agents:"), gbc);
        agentCountField = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 0;
        frame.add(agentCountField, gbc);

        // Label and TextField for Ticker Time
        gbc.gridx = 0; gbc.gridy = 1;
        frame.add(new JLabel("Ticker Time (ms):"), gbc);
        tickerTimeField = new JTextField(10);
        gbc.gridx = 1; gbc.gridy = 1;
        frame.add(tickerTimeField, gbc);

        // Start Button
        JButton startButton = new JButton("Start Agents");
        startButton.addActionListener(new StartButtonListener());
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2; // Span two columns
        gbc.fill = GridBagConstraints.HORIZONTAL;
        frame.add(startButton, gbc);

        // Log Area
        logArea = new JTextArea(8, 30);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        frame.add(scrollPane, gbc);

        frame.setVisible(true);
    }

    private class StartButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String agentCountText = agentCountField.getText();
            String tickerTimeText = tickerTimeField.getText();

            try {
                int agentCount = Integer.parseInt(agentCountText);
                int tickerTime = Integer.parseInt(tickerTimeText);

                log("Starting " + agentCount + " agents with ticker time: " + tickerTime + " ms.");
                for (int i = 0; i < agentCount; i++) {
                    getContainerController().createNewAgent("AgentSmith" + i, "AgentSmith", null).start();
                }
            } catch (NumberFormatException ex) {
                log("Error: Please enter valid integers for both fields.");
            } catch (Exception ex) {
                log("Error starting agents: " + ex.getMessage());
            }
        }
    }

    private String forwardToServer(String message) {
        String response = "";
        try (Socket socket = new Socket("13.53.47.77", 5000);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println(message);
            out.flush();
            log("Sent to server: " + message);

            response = in.readLine();
            log("Received from server: " + response);
        } catch (Exception e) {
            log("Error communicating with server: " + e.getMessage());
        }
        return response;
    }

    private void log(String message) {
        logArea.append(message + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength()); // Auto-scroll to the bottom
    }
}
