# 🤖 Multi-Agent System Deployment on AWS  
**Category:** Distributed Systems | AI | Cloud Computing | Networking | JADE | AWS  

## **🔹 Overview**  
This project implements a **Multi-Agent System (MAS)** using the **JADE (Java Agent DEvelopment) framework**, deployed on **AWS EC2 instances** with **Auto Scaling** and **Load Balancing**.  

**Key components include:**  
✅ **CoordinatorAgent** – GUI-based control panel to manage agent population  
✅ **AgentSmith** – Periodically connects to a **TCP server** for distributed task execution  
✅ **TCP Server** – Handles incoming agent connections & simulates time-consuming tasks  
✅ **AWS Auto Scaling Group & Load Balancer** – Ensures dynamic resource allocation  

This system simulates **real-world distributed AI environments**, where agents must interact via networking and adapt to varying workloads. The results analyze **scalability, network overhead, and system efficiency** under different loads.  

---

## **🛠️ Technical Framework**  

### **1️⃣ Multi-Agent System (JADE)**
🔹 **CoordinatorAgent** – Spawns and manages multiple `AgentSmith` instances  
🔹 **AgentSmith** – Uses `TickerBehaviour` to periodically **open TCP socket connections**  
🔹 **MessageHandler** – Handles **Agent Communication Language (ACL) messages**  

### **2️⃣ TCP Server & Networking**
✅ **TCPServer** – Accepts incoming connections from agents  
✅ **ClientHandler** – Simulates a **time-consuming task** per connection  
✅ **Port:** `5000`  
✅ **Server IP:** `13.53.47.77`  

### **3️⃣ AWS Deployment**
✅ **EC2 Instance 1:** Runs **CoordinatorAgent + AgentSmiths**  
✅ **EC2 Instance 2:** Runs **TCP Server**  
✅ **Auto Scaling & Load Balancing:** Ensures system stability  

### **📈 Performance Analysis & Results**

1️⃣ **Impact of Scaling Agents on CPU & Memory Usage**  

📌 Used `htop` to monitor system performance under different agent loads  
📌 Tested with **10, 100, 500, and 10,000 agents**  

### **Key Observations:**  

- **CPU & Memory Usage Scales with Agent Population** – As agents increased, system resource consumption grew.  
- **Auto Scaling Reduced Server Load** – AWS auto-scaling dynamically adjusted resources.  
- **Message Passing Overhead Observed** – High agent count caused significant network traffic.  

### 📊 Performance Results Summary:
To test scalability,  **CPU & memory usage** was monitored while running different numbers of agents.  
### **1️⃣ Resource Utilization Under Different Agent Loads**
| Agents Count  | CPU Usage (%) | Memory Usage (MB) | Network Requests/sec |
|--------------|--------------|------------------|------------------|
| **10 Agents**  | 5%   | 50MB  | 10/sec |
| **100 Agents**  | 20%  | 200MB | 100/sec |
| **1000 Agents** | 70%  | 2GB   | 1000/sec |
| **5000 Agents** | 90%  | 4GB   | 5000/sec |
| **10000 Agents** | **Out of Memory!** | **System Crash** | **Unstable** |

📌 **Observations:**  
✅ **CPU & Memory Usage Scale with Agent Population** – Increasing agents **dramatically increased resource consumption**  
✅ **Network Load Increased Linearly** – Each agent opened a **TCP connection at regular intervals**, adding network traffic  
✅ **AWS Auto Scaling Reduced Server Load** – CPU stabilized after auto-scaling new instances  
✅ **System Bottleneck at ~10,000 Agents** – Memory exceeded instance limits, causing **process failure**  

### **2️⃣ AWS Auto Scaling Performance**
AWS **Auto Scaling** dynamically adjusted the number of instances when **CPU usage exceeded 75%**.  

📊 **Scaling Behavior:**
- **Baseline:** 1 instance, ~500 agents  
- **Scaling Triggered at:** ~2000 agents (CPU > 75%)  
- **Final Instance Count:** 3 instances for ~10,000 agents  
- **Performance Impact:** Load balanced across instances, reducing single-server stress  

📌 **Conclusion:**  
The system **successfully scaled to thousands of agents**, but **further optimizations are required** for handling **10,000+ agents** without performance degradation.
