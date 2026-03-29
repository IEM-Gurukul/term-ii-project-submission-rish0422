[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/pG3gvzt-)
  # Inventory Management System
  
  ## Project Title
  Inventory Management System with DAO Pattern and File Persistence
  
  ---
  
  ## Problem Statement
  Small businesses and retail stores often struggle to manage their product
  inventory efficiently without expensive software. This project addresses
  that gap by providing a lightweight desktop application to manage products,
  track stock levels, and get alerts when items run low. The system uses the
  DAO (Data Access Object) design pattern to separate business logic from
  data persistence, storing all data locally in a binary file (products.dat)
  without needing any database. It provides a clean Java Swing GUI for
  non-technical users to perform all inventory operations easily.
  
  ---
  
  ## Target User
  Small business owners, shopkeepers, and retail store managers who need a
  simple, offline desktop tool to manage their product inventory without
  relying on internet connectivity or expensive enterprise software.
  
  ---
  
  ## Core Features
  - Add, edit, and delete products
  - Restock and deduct stock quantities
  - Search products by name or category
  - Low stock alert with red row highlighting
  - Persistent data storage via products.dat (survives app restart)
  - Clean and intuitive Java Swing GUI
  
  ---
  
  ## OOP Concepts Used
  
  ### Abstraction
  ProductDAO.java is an interface that defines all data operations
  (add, update, delete, search) without exposing how they are implemented.
  The UI and Service layers only interact with this abstraction, not the
  actual file-handling code.
  
  ### Inheritance
  ProductDAOImpl.java implements the ProductDAO interface, inheriting
  its contract and providing the concrete file-based implementation.
  This follows the principle of programming to an interface.
  
  ### Polymorphism
  The ProductService class holds a reference of type ProductDAO but
  at runtime it points to ProductDAOImpl. This allows the implementation
  to be swapped (e.g. database-backed) without changing any business logic.
  
  ### Encapsulation
  Product.java keeps all fields private and exposes them only through
  public getters and setters. ProductService hides all validation logic
  internally. Each UI panel manages its own state privately.
  
  ### Exception Handling
  All DAO and Service methods declare throws Exception. Errors such as
  product not found, insufficient stock, invalid input, and file I/O
  failures are caught at the UI layer and shown to the user via
  JOptionPane dialogs, preventing crashes.
  
  ### Collections
  List<Product> (ArrayList) is used throughout the application to store,
  retrieve, filter, and display products. Java Streams are used in
  ProductDAOImpl for searching and filtering with Collectors.toList().
  
  ---
  
  ## Proposed Architecture Description
  The application follows a 3-layer architecture:
  
  UI Layer (Swing)
  │   MainFrame, ProductTablePanel, ProductFormPanel, SearchPanel
  │   Handles user interaction, displays data, captures input
  │
  ▼
  Service Layer
  │   ProductService
  │   Contains business logic and input validation
  │
  ▼
  DAO Layer
  │   ProductDAO (interface) → ProductDAOImpl (implementation)
  │   Handles all file read/write operations
  │
  ▼
  Persistence
  products.dat
  Binary file storing serialized List<Product> objects
  
  
  Each layer only communicates with the layer directly below it,
  ensuring separation of concerns and maintainability.
  
  ---
  
  ## How to Run
  
  ### Requirements
  - Java 17 or higher installed
  - Any OS (Windows / macOS / Linux)
  
  ### Steps
  
  1. Clone the repository:
  git clone https://github.com/rish0422/term-ii-project-submission-rish0422.git
  cd term-ii-project-submission-rish0422
  
  2. Compile all Java files:
  javac  src/*.java
  
  3. Run the application:
  java  Main
