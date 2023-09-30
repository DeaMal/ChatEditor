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
There is no interface in the program. When launched, the program displays a list of users specified in the line parameters

> List\<User> arrayList = usersRepository.findAll(3, 4);

where \
3 - is the number of the sheet being returned,\
4 - is the count of users on the sheet.

In addition, the program can do the following:
- search messages by ID
- adding new messages
- message update
- search for users by ID
- search for chatrooms by ID
- getting the maximum ID value for users, chatrooms and messages
- search for users by name
- search for chatrooms by name

You can edit the `src\main\java\chat\app\Program.java` file to test these features.

## Remove

    rm -r target