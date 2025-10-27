# Continuous Improvement Application

This README provides guidance for both **developers** and **end-users** of the Continuous Improvement JavaFX Application.

---

## Table of Contents

1. [Overview](#overview)
2. [For Developers](#for-developers)
    1. [Project Structure](#project-structure)
    2. [Setup & Installation](#setup-installation)
    3. [Running the Application](#running-the-application)
    4. [Features](#features)
    5. [Admin Features](#admin-features)
    6. [Styling (CSS)](#styling-css)
    7. [Architecture](#architecture)
    8. [Extending or Modifying](#extending-or-modifying)
3. [For Users](#for-users)
     1. [Getting Started](#getting-started)
     2. [Submitting an Idea](#submitting-an-idea)
     3. [Admin Panel Overview](#admin-panel-overview)
4. [Application Preview](#application-preview)
5. [Future Improvements](#future-improvements)
6. [License](#license)

---

## Overview

The **Continuous Improvement Application** is a JavaFX-based idea submission and management system.  
It enables employees to submit improvement ideas and allows administrators to review, process, and manage those ideas.

---

## For Developers

### Project Structure

```plaintext
Continuous Improvement/
├── src/
│   ├── application/
│   │   ├── Main.java
│   │   ├── application.css
│   ├── controller/
│   │   ├── MainController.java
│   │   ├── LoginController.java
│   │   ├── IdeaController.java
│   │   ├── AdminController.java
│   │   ├── AddUserController.java
│   ├── model/
│   │   ├── Database.java
│   │   ├── User.java
│   │   ├── Idea.java
│   ├── util/
│   │   ├── ToastUtils.java
│   │   ├── TableRowHighlighter.java
├── build.fxbuild

```


## Setup & Installation

1. Clone or download the project into Eclipse or IntelliJ.

2. Add JavaFX SDK and MySQL Connector to your project libraries.

3. Configure your database in Database.java.

4. Create the following tables in MySQL:

```
CREATE TABLE users (
  id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) UNIQUE NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  role ENUM('admin','user') DEFAULT 'user'
);

CREATE TABLE ideas (
  user_id INT NOT NULL,
  short_description VARCHAR(255),
  idea_text TEXT,
  date_submitted VARCHAR(50),
  status VARCHAR(20),
  FOREIGN KEY (user_id) REFERENCES users(id)
);
```

## Running the Application
1. Run `Main.java`
2. Log in with your credentials.
3. Depending on your role, the **Admin** or **User** dashboard will load automatically.

## Features
* JavaFX graphical interface
* Role-based login and authentication
* Auto-refresh for new ideas
* Toast-style notifications
* Row highlighting for newly added ideas

## Admin Features
* View all user-submitted ideas
* Process idea statues
* Add new users directly from the interface
* Automatic periodic refresh
* Visual notification when new ideas are added

## Styling (CSS)
The UI uses a centralized `application.css` file.

Example of styling for buttons:

```
.action-button {
    -fx-background-color: #0078d7;
    -fx-text-fill: white;
    -fx-font-weight: bold;
    -fx-padding: 6 14;
    -fx-background-radius: 4;
}
.action-button:hover {
    -fx-background-color: #005a9e;
}

```

## Architecture
This project follows the **MVC (Model-View-Controller)** architecture pattern.
* **Model:** Handles data (User, Idea, Database)
* **View:** Built using JavaFX layouts and CSS
* **Controller:** Controls the logic and UI interactions
* **Util:** Helper classes like `ToastUtils` and `TableRowHighlighter`

## Extending or Modifying
You can easily extend the application by:
* Adding new user roles and permissions
* Implementing reports in `AdminController`
* Using JavaFX listeners for real-time database updates
* Integrating analytics dashboards for idea tracking

## For Users

### Getting started
1. Launch the application and log in using your credentials.
2. Standard users will be redirected to the **Idea Submission** section

### Submitting an Idea
* Fill out both fields:
	* **short Description** and **Full Idea**
* Click **Submit** to add your idea.
* Track submission status in the **Your Ideas** table.

## Admin Panel Overview
Admin can:
* Process ideas submitted by users
* Add new users
* Refresh data manually or rely on auto-refresh
* View all ideas in a centralized table

## Application Preview

Here is how the application looks like in action:

**Welcome Window**
<p align="center" style="margin-top:0; margin-bottom:0;">
  <img src="screenshots/welcome_window.png" width="700">
</p>

**Login Window**
<p align="center" style="margin-top:0;margin-bottom:0;">
  <img src="screenshots/login_window.png" width="700">
</p>

**Idea Submission (User View)**
<p align="center" style="margin-top:0;margin-bottom:0;">
  <img src="screenshots/idea_submission_user_view.png" width="500">
</p>

**Admin Panel/Idea Submission**
<p align="center" style="margin-top:2;margin-bottom:2;">
  <img src="screenshots/admin_panel.png" width="700">
</p>

**Add New User Window**
<p align="center" style="margin-top:2;margin-bottom:2;">
  <img src="screenshots/add_new_user.png" width="700">
</p>

## Future Improvements
```plaintext
- Export submitted ideas to PDF
- Peer recognition and voting functionality
- Calendar for team absences or time-off tracking
- Daily production scheduling
- Time-off request submission feature
```

## License
This project is intended for **educational and internal improvement use.**

You may modify and adapt it for **non-commercial purposes.**

```
Author:Dilli Khatiwoda 
Language: Java, JavaFX, MySQL
IDE: Eclipse
Version:1.0
```

