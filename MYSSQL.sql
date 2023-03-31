CREATE DATABASE OUBus;

use OUBus;



CREATE TABLE Location (
	ID int NOT NULL auto_increment,
    Name nvarchar(50) NOT NULL,
    
    PRIMARY KEY (ID)
);


CREATE TABLE Bus (
	ID int NOT NULL auto_increment,
    LicensePlates nvarchar(15) NOT NULL,
    NumberCode int NOT NULL,
    
    PRIMARY KEY (ID),
    UNIQUE (NumberCode)
);

CREATE TABLE Seat (
	ID int NOT NULL auto_increment,
    Name nvarchar(4) NOT NULL,
    
	PRIMARY KEY (ID)
);
CREATE TABLE Route (
	ID nvarchar(50) NOT NULL,
    DepartureID int NOT NULL,
    DestinationID int NOT NULL,
    Price decimal(15,2) NOT NULL,
    
    PRIMARY KEY (ID),
    FOREIGN KEY (DepartureID) REFERENCES Location(ID),
    FOREIGN KEY (DestinationID) REFERENCES Location(ID)
);

CREATE TABLE BusTrip (
	ID nvarchar(50) NOT NULL,
	RouteID nvarchar(50) NOT NULL,
    DepartureTime Time NOT NULL,
    Price decimal(15,2) NOT NULL,
    BusID int NOT NULL,
    
    PRIMARY KEY (ID),
    FOREIGN KEY (RouteID) REFERENCES Route(ID),
    FOREIGN KEY (BusID) REFERENCES Bus(ID)
);

CREATE TABLE User (
	ID nvarchar(50) NOT NULL,
    Username nvarchar(50) NOT NULL,
    Password nvarchar(255) NOT NULL,
    Name nvarchar(50) NULL NULL,
    UserRole enum('staff', 'admin'),
    
    PRIMARY KEY (ID),
    UNIQUE (Username)
);

CREATE TABLE Customer (
	ID nvarchar(50) NOT NULL,
    Name nvarchar(50) NOT NULL,
    Phone nvarchar(13) NOT NULL,
    
    PRIMARY KEY (ID),
    UNIQUE (Phone)
);

CREATE TABLE Ticket (
	ID nvarchar(50) NOT NULL,
    CustomerID nvarchar(50) NOT NULL,
    BusTripID nvarchar(50) NOT NULL,
    SeatID int NOT NULL,
    StaffID nvarchar(50) NOT NULL,
    Status enum('booked', 'purchased', 'empty'),

    PRIMARY KEY (ID),
    FOREIGN KEY (CustomerID) REFERENCES Customer(ID),
    FOREIGN KEY (BusTripID) REFERENCES BusTrip(ID),
    FOREIGN KEY (SeatID) REFERENCES Seat(ID),
    FOREIGN KEY (StaffID) REFERENCES User(ID)
);