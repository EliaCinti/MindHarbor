# 🧠 MindHarbor

> **A comprehensive appointment management system for psychological consultations**

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![JavaFX](https://img.shields.io/badge/JavaFX-21-blue.svg)](https://openjfx.io/)
[![Maven](https://img.shields.io/badge/Maven-3.6+-red.svg)](https://maven.apache.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green.svg)](LICENSE)

MindHarbor is a desktop application built with JavaFX that facilitates appointment management between patients and psychologists. It features dual persistence support (CSV files and MySQL database) with automatic synchronization, making it robust and flexible for different deployment scenarios.

## 📋 Table of Contents

- [Features](#-features)
- [Architecture](#-architecture)
- [Installation](#-installation)
- [Usage](#-usage)
- [Configuration](#-configuration)
- [Design Patterns](#-design-patterns)
- [Project Structure](#-project-structure)
- [Technologies](#-technologies)
- [Contributing](#-contributing)
- [License](#-license)

## ✨ Features

### 👤 User Management
- **Dual User Types**: Separate registration and login for patients and psychologists
- **Secure Authentication**: BCrypt password hashing with salt
- **Session Management**: Persistent user sessions throughout the application

### 📅 Appointment System
- **Easy Booking**: Patients can request appointments with their assigned psychologist
- **Confirmation Workflow**: Psychologists can review and confirm appointment requests
- **Smart Notifications**: Visual indicators for new appointments
- **Conflict Prevention**: Automatic detection of scheduling conflicts

### 💾 Dual Persistence
- **CSV Storage**: File-based storage for simple deployments
- **MySQL Database**: Full relational database support for enterprise use
- **Automatic Sync**: Real-time synchronization between storage types
- **Conflict Resolution**: Intelligent handling of data conflicts

### 🎨 User Interface
- **Intuitive Design**: Clean, user-friendly interface
- **Responsive Layout**: Optimized for desktop use
- **Visual Feedback**: Smooth animations and transitions
- **Accessibility**: Proper contrast and semantic design

## 🏗️ Architecture

MindHarbor follows a robust **Model-View-Controller (MVC)** architecture enhanced with multiple design patterns:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     View        │    │   Controller    │    │     Model       │
│                 │    │                 │    │                 │
│ FXML Files      │◄──►│ App Controllers │◄──►│ Domain Objects  │
│ Graphic Ctrl    │    │ Business Logic  │    │ User, Patient   │
│ UI Components   │    │ Validation      │    │ Psychologist    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │ Data Access     │
                       │                 │
                       │ DAO Layer       │
                       │ CSV ◄──► MySQL  │
                       │ Auto Sync       │
                       └─────────────────┘
```

## 🚀 Installation

### Prerequisites

- **Java 21** or higher
- **Maven 3.6+** for building
- **MySQL 8.0+** (optional, for database persistence)

### Quick Start

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/mindharbor.git
   cd mindharbor
   ```

2. **Build the project**
   ```bash
   mvn clean compile
   ```

3. **Run the application**
   ```bash
   mvn javafx:run
   ```

### Building JAR

```bash
mvn clean package
java -jar target/mindharbor-1.0-SNAPSHOT.jar
```

## 🎮 Usage

### First Launch

1. **Start the application** - You'll see the welcome screen with Sign In/Sign Up options
2. **Create an account** - Choose between Patient or Psychologist registration
3. **Complete your profile** - Add specific information based on your user type

### For Patients

- **Book Appointments**: Request sessions with your assigned psychologist
- **View Schedule**: Check your upcoming and past appointments
- **Get Notifications**: See visual indicators for confirmed appointments
- **Manage Profile**: Update your personal information

### For Psychologists

- **Manage Patients**: View your assigned patient list
- **Confirm Appointments**: Review and approve appointment requests
- **Schedule Overview**: Monitor your daily/weekly schedule
- **Patient History**: Access appointment history for each patient

### Command Line Options

```bash
# Run with MySQL as primary persistence
java -jar mindharbor.jar mysql gui

# Run with CSV as primary persistence (default)
java -jar mindharbor.jar csv gui
```

## ⚙️ Configuration

### Database Setup (Optional)

If you want to use MySQL persistence:

1. **Create the database**
   ```sql
   CREATE DATABASE mindharbor;
   ```

2. **Run the schema script**
   ```bash
   mysql -u your_username -p mindharbor < db/mysql/mindharbor_db.sql
   ```

3. **Configure connection** - Edit `src/main/resources/config.properties`:
   ```properties
   jdbcURL=jdbc:mysql://localhost:3306/mindharbor
   jdbcUsername=your_username
   jdbcPassword=your_password
   ```

### CSV Storage

No configuration needed! CSV files are automatically created in the `db/csv/` directory:
- `user_db.csv` - User credentials and basic info
- `patient_db.csv` - Patient-specific data
- `psychologist_db.csv` - Psychologist-specific data  
- `appointment_db.csv` - All appointment records

## 🎯 Design Patterns

MindHarbor showcases several **Gang of Four** design patterns in action:

| Pattern | Implementation | Purpose |
|---------|---------------|---------|
| **MVC** | App structure | Separation of concerns |
| **Singleton** | `SessionManager`, `NavigatorSingleton` | Single instance management |
| **Factory** | `UserDaoFactory`, `PatientDaoFactory` | Object creation abstraction |
| **Facade** | `DaoFactoryFacade` | Simplified interface |
| **Builder** | `PatientBean.Builder`, `UserBean.Builder` | Complex object construction |
| **Observer** | `CrossPersistenceSyncObserver` | Auto-sync between storage types |
| **Strategy** | CSV vs MySQL DAOs | Interchangeable algorithms |

### Synchronization System

The **Observer Pattern** enables seamless data synchronization:

```java
CSV Operation → Observer → MySQL Replication
MySQL Operation → Observer → CSV Replication
```

This ensures data consistency regardless of which storage system is primary.

## 📁 Project Structure

```
src/main/java/it/uniroma2/mindharbor/
├── 📱 app_controller/          # Business logic controllers
├── 🎨 graphic_controller/      # UI event handlers  
├── 📊 model/                   # Domain objects
├── 💾 dao/                     # Data access layer
│   ├── csv/                    # CSV implementations
│   └── mysql/                  # MySQL implementations
├── 🧱 beans/                   # Data transfer objects
├── 🎭 patterns/                # Design pattern implementations
├── 🔐 session/                 # User session management
├── 🔄 sync/                    # Data synchronization
├── 🛠️ utilities/               # Helper classes
└── ⚠️ exception/               # Custom exceptions

src/main/resources/
├── 🎨 css/                     # Stylesheets
├── 🖼️ Img/                     # Images and icons
└── 📋 fxml/                    # UI layouts
```

## 🛠️ Technologies

### Core Technologies
- ![Java](https://img.shields.io/badge/Java-21-orange?logo=openjdk) **Java 21** - Modern LTS Java version
- ![JavaFX](https://img.shields.io/badge/JavaFX-21-blue?logo=java) **JavaFX 21** - Rich desktop UI framework
- ![Maven](https://img.shields.io/badge/Maven-3.6+-red?logo=apache-maven) **Maven** - Project management and build

### Database & Persistence
- ![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue?logo=mysql) **MySQL 8.0+** - Relational database
- ![HikariCP](https://img.shields.io/badge/HikariCP-5.0.1-green) **HikariCP** - High-performance connection pooling
- ![OpenCSV](https://img.shields.io/badge/OpenCSV-5.7.0-yellow) **OpenCSV** - CSV file processing

### Security & Utilities
- ![BCrypt](https://img.shields.io/badge/BCrypt-0.4-purple) **jBCrypt** - Secure password hashing
- ![Apache Commons](https://img.shields.io/badge/Commons_Text-1.10.0-orange) **Apache Commons Text** - Text utilities

## 🤝 Contributing

We welcome contributions! Here's how you can help:

### Getting Started
1. **Fork** the repository
2. **Create** a feature branch (`git checkout -b feature/amazing-feature`)
3. **Commit** your changes (`git commit -m 'Add amazing feature'`)
4. **Push** to the branch (`git push origin feature/amazing-feature`)
5. **Open** a Pull Request

### Development Guidelines
- Follow existing code style and patterns
- Add unit tests for new features
- Update documentation as needed
- Ensure all tests pass before submitting

### Areas for Contribution
- 🐛 Bug fixes and improvements
- ✨ New features (appointment reminders, calendar integration)
- 📱 UI/UX enhancements
- 🔒 Security improvements
- 📚 Documentation updates
- 🧪 Additional test coverage

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- Thanks to the JavaFX community for excellent documentation
- Inspired by modern healthcare management systems
- Built as part of software engineering coursework

---

<div align="center">

**🌟 Star this repository if you find it helpful! 🌟**

Made with ❤️ for better healthcare management

[Report Bug](https://github.com/your-username/mindharbor/issues) · [Request Feature](https://github.com/your-username/mindharbor/issues) · [Documentation](https://github.com/your-username/mindharbor/wiki)

</div>