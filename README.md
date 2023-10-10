# ChatEditor
Chat business logic for searching and editing messages, users and chatrooms in the database.

Maven

Postgresql

DAO, Repository

## Overview

Program use the key mechanisms to work with PostgreSQL DBMS via JDBC. It allows you to search and edit messages in chat, as well as search for users and chat rooms by ID and name.

## Installation

To start the project you will need Maven and a working Postgresql database.

    sudo apt-get install postgresql
    sudo apt install maven

Compiling and running:

    mvn compile
    mvn exec:java -Dexec.mainClass="chat.app.Program"

## Usage
Follow the instructions in the terminal. When launched, the program displays a menu for interacting with the database.

> 1. View a message by Id
     
Requests a message ID to display it.

> 2. Add new message

To add a message, you need to enter the author ID and chat room ID and the corresponding message text. The time is set to the current one.

> 3. Update message

To update a message, enter the message ID. If the message is found, enter new data (author, chat room, message text and time). You can skip entering new values by pressing enter.

> 4. View list Users

To display a page-by-page list of users, enter the number of users on the sheet and the number of the sheet to be returned.

> 5. View count of Users

Displays valid values for Users.

> 6. View count of Chatroom's

Displays valid values for Chatroom's.

> 7. View count of Messages

Displays valid values for Messages.

> 8. Finish execution

Exit

## Remove

    rm -r target