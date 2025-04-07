# 🧩 Sudoku Solver (Java Swing + Live Visualization)

A Sudoku Solver desktop application built with **Java Swing** that lets you input Sudoku puzzles, visualize the solving process live, and optionally upgrade it to solve puzzles directly from images using OCR.

---

## 🚀 Features

- ✅ 9x9 Sudoku grid with input validation
- 🎯 Backtracking algorithm for solving
- 🖥️ Live solving visualization with GUI updates
- 🔄 Reset and Solve functionality
- 🔍 OCR integration to read puzzles from images (to be implemented)

---

## 🖼️ GUI Preview

![screenshot](./screenshot.png)

---

## 🛠️ Tech Stack

- **Java**
- **Java Swing** (GUI)
- `SwingWorker` (for multithreading with GUI updates)
- (Optional) **Tesseract OCR** for image input

---

## 🧠 How It Works

1. Enter Sudoku numbers into the grid.
2. Click the **"Solve"** button.
3. The algorithm runs in the background (`SwingWorker`) and updates the GUI step-by-step.
4. Displays a success or error message on completion.

---

## 🖼️ (Optional) Image Input via OCR

To enable OCR input:
1. Install [Tesseract OCR](https://github.com/tesseract-ocr/tesseract).
2. Add `OCRReader.java` to your project.
3. Use `OCRReader.readGridFromImage("path/to/image.png")` to auto-fill the board.

---

## ▶️ How to Run

1. Clone the repo:
   ```bash
   git clone https://github.com/yourusername/java.SudokuSolver.git

2. Compile and Run
    ```bash
   javac src/*.java
   java src.java.SudokuSolverGUI

