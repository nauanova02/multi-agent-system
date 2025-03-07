import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AgentWithGUI extends GuiAgent {
    private JTextField receiverField;
    private JTextField contentField;
    private JTextArea sentMessagesArea;
    private JTextArea receivedMessagesArea;
    private JComboBox<String> performativeComboBox;

    @Override
    protected void setup() {
        createAndShowGUI();
        addBehaviour(new MessageReceiver());
    }

    private void createAndShowGUI() {
        JFrame frame = new JFrame("Agent Communication");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel receiverLabel = new JLabel("Receiver:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        frame.add(receiverLabel, gbc);

        receiverField = new JTextField(15);
        gbc.gridx = 1;
        frame.add(receiverField, gbc);

        JLabel contentLabel = new JLabel("Content:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        frame.add(contentLabel, gbc);

        contentField = new JTextField(15);
        gbc.gridx = 1;
        frame.add(contentField, gbc);

        JLabel performativeLabel = new JLabel("Message Performative:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        frame.add(performativeLabel, gbc);

        String[] performatives = {
            String.valueOf(ACLMessage.INFORM), 
            String.valueOf(ACLMessage.REQUEST), 
            String.valueOf(ACLMessage.AGREE), 
            String.valueOf(ACLMessage.REJECT_PROPOSAL)  // Replaced DISAGREE with REJECT_PROPOSAL
        };
        performativeComboBox = new JComboBox<>(performatives);
        gbc.gridx = 1;
        frame.add(performativeComboBox, gbc);

        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());
        gbc.gridx = 0;
        gbc.gridy = 3;
        frame.add(sendButton, gbc);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(e -> System.exit(0));
        gbc.gridx = 1;
        frame.add(cancelButton, gbc);

        sentMessagesArea = new JTextArea(10, 20);
        sentMessagesArea.setEditable(false);
        JScrollPane sentScroll = new JScrollPane(sentMessagesArea);
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.gridheight = 4;
        frame.add(sentScroll, gbc);

        receivedMessagesArea = new JTextArea(10, 20);
        receivedMessagesArea.setEditable(false);
        JScrollPane receivedScroll = new JScrollPane(receivedMessagesArea);
        gbc.gridx = 3;
        gbc.gridy = 0;
        frame.add(receivedScroll, gbc);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @Override
    protected void onGuiEvent(GuiEvent guiEvent) {
        // Handle GUI events if necessary
    }

    private class SendButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String receiver = receiverField.getText();
            String content = contentField.getText();
            String performative = (String) performativeComboBox.getSelectedItem();
            
            ACLMessage msg = new ACLMessage(Integer.parseInt(performative));
            msg.addReceiver(new AID(receiver, AID.ISLOCALNAME));
            msg.setContent(content);
            send(msg);
            sentMessagesArea.append("Sent: " + content + " to " + receiver + "\n");
            contentField.setText("");
        }
    }

    private class MessageReceiver extends CyclicBehaviour {
        @Override
        public void action() {  // Updated to correctly override action
            ACLMessage msg = receive();
            if (msg != null) {
                receivedMessagesArea.append("From: " + msg.getSender().getLocalName() + " - " + msg.getContent() + "\n");
            } else {
                block();
            }
        }
    }
}
