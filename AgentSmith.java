import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class AgentSmith extends Agent {
    protected void setup() {
        System.out.println("Agent " + getLocalName() + " started.");

        // Send a request for Fibonacci calculation
        addBehaviour(new OneShotBehaviour() {
            public void action() {
                ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
                
                // Create the AID of the CoordinatorAgent
                AID coordinator = new AID("CoordinatorAgent", AID.ISLOCALNAME);
                msg.addReceiver(coordinator);
                
                msg.setContent("30"); // Replace with the desired Fibonacci number as a string
                send(msg);
                System.out.println(getLocalName() + " sent a request to CoordinatorAgent.");
            }
        });
    }
}
