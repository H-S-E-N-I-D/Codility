# Pancake Lab 🥞

A pancake shop software solution for Coding Dojo, enabling disciples to order pancakes and the Sensei's Chef/Delivery service to prepare and deliver them.

---

## 📖 Overview
Pancake Lab is a Java-based application designed to streamline pancake ordering, preparation, and delivery within the Coding Dojo. The system allows disciples to place orders, chefs to prepare them, and delivery personnel to fulfill them. Due to vulnerabilities exploited by the villain Dr. Fu Man Chu, the system requires a refactor to improve security, flexibility, and reliability.

---

## ✨ Features
- **Order Management**
    - Create, modify, or cancel pancake orders.
    - Specify building and room number for delivery.
- **Customizable Pancakes**
    - Dynamic ingredient selection using a design pattern (e.g., **Builder** or **Factory**).
- **Workflow Automation**
    - Chef preparation and delivery status tracking.
- **Validation & Security**
    - Input validation for orders/buildings.
    - Thread-safe operations to prevent data races.
- **Testing & Documentation**
    - TDD approach with unit/integration tests.
    - UML diagrams for design clarity.

---
## ️ Class Diagram

![Pancake Lab](https://github.com/H-S-E-N-I-D/Codility/blob/main/PancakeLab/pancake-class-diagram.png)

## ⚙️ Prerequisites
- **Java Development Kit (JDK) 17+**
- **Apache Maven** (for dependency management)
- **IntelliJ IDEA** (or any Java IDE)
- **Docker** (for containerized deployment)

---

## 🚀 Steps to Run the Code

<<<<<<< HEAD
1. Download the source code from the [#https://github.com/H-S-E-N-I-D/Codility/tree/main/PancakeLab](GIT).
=======
1. Download the source code from the [GitHub repository](https://github.com/H-S-E-N-I-D/Codility/tree/main/PancakeLab).
>>>>>>> 89febcbe136b5fdcfea2d502881263f5ff42bb3b
2. Extract and save the source into your workspace.
3. Open **IntelliJ IDEA** and navigate to **File -> Open**.
4. Browse to the source location and click **OK**.
5. Once the project is imported into IntelliJ, double-click on the project folder.
6. Navigate to `src/main/java` in the left navigation pane.
7. Expand and locate `com.codingdojo.pancakelab.PancakeLabApp` class.
8. Right-click on the `PancakeLabApp` class and select **Run**.
---

## Steps to Run the Tests
1. Once the project is imported, open the **terminal** in IntelliJ.
2. Type the following command and press **Enter**:
   ```sh
   mvn clean test jacoco:report
   ```
3. After execution completes, navigate to `target/site/jacoco/index.html`.
4. Open `index.html` in a web browser to check the test coverage.


##  🐳 Run with Docker
1. Pull the Docker image:
 ```sh
docker pull dockdino/pancake-lab:1.1.0
   ```
2. Run the container:
 ```sh
docker run -p 8080:8080 dockdino/pancake-lab:1.1.0
   ```

## License
This project is licensed under the ABC License - see the [LICENSE](LICENSE) file for details.

## Author
Dinesh Madushanka
