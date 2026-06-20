# MOTORPH EMPLOYEE APP GUI - GROUP 15

## Introduction
> Founded in 2020, MotorPH aims to be the top choice for Filipinos seeking competitive and affordable motorcycles. As the company expands its private transportation services, it requires a robust automated system to handle employee data and compensation. While the system's core calculation logic was successfully established in our previous release, this milestone marks the transition from a terminal interface to a robust, automated Graphical User Interface (GUI). Our primary goal is to shift from manual console inputs to an intuitive, event-driven desktop environment that maximizes user experience (UX) through responsive visual interactions, explicit structural feedback, and proactive error prevention.

## Program Details
> The MotorPH System has evolved into an interactive desktop application designed via NetBeans, balancing visual clarity (UI) with functional empathy (UX) while keeping all backend computations completely intact.

### + Interface & Experience (UI/UX) Enhancements
- Visually Anchored Grid Form (UI Alignment): Designed using a unified structural layout framework, replacing disjointed sequential terminal prompts (Scanner.nextLine()) with a clean, centralized workspace. Users can view, track, and process information using explicit, clearly labeled input components.
- Proactive Data Safeguarding (UX Error Prevention): The txtEmployeeName text field is explicitly configured as read-only (setEditable(false)) and assigned a distinct background (Color(241, 245, 249)). This visually signals to the operator that it cannot be altered manually, auto-populating programmatically only after the verification engine matches a valid, database-registered Employee ID to prevent data tampering.
- Streamlined Multi-Period Picker (UX Efficiency): Shifted period tracking from manually typed text entries to an elegant drop-down list (JComboBox). This component maps choice selections cleanly to the underlying operational month loops. Users can calculate single specific calendar months or trigger a sequential macro that generates a cumulative summary covering the full June-to-December operational window at once.
- Persistent Report Presentation (UI Clarity): Replaced volatile scrolling terminal logs with a dedicated JTextArea wrapped within a scrollable panel (JScrollPane). Utilizing a uniform monospaced font (Consolas), it captures intercepted system calculation logs to display semi-monthly itemized payroll summaries with unrounded, high-precision trailing decimals (RoundingMode.DOWN) aligned vertically like a corporate payslip.

### + Robust Validation & Forgiveness (UX Exception Handling)
  > To satisfy advanced correctness criteria, the visual layer acts as a defensive shield that intercepts human operational errors before they can affect runtime processing:
  - Empty State Validation: The system performs structural checks on inputs before calculations run. If field entries are missing (empId.isEmpty()), execution short-circuits safely, throwing an explicit warning dialogue.
  - Active Character Constraint: Equipped with a defensive input KeyAdapter stream on txtEmployeeId, the system filters key presses at the hardware layer, invoking e.consume() to discard non-numeric character events instantly. This ensures compliance with raw data structures without throwing parsing errors.
  - Dynamic Cutoff Period Processing: The system automatically determines the end day bounds for calculated semi-monthly cycles (evaluating 30-day boundaries for June, September, and November against standard 31-day marks) without requiring manual parameters from the operator, minimizing administrative configuration slips.
    
## System Access & Operational Guide
> Follow these sequential parameters to access, navigate, and utilize the interactive MotorPH desktop layout environment:

### Step 1: System Authentication (Login Gate)
> When the application initializes, the window constraints are set to handle secure gateway logging.
- Authorized Username: payroll_staff
- Default System Password: 12345
- - Note: Providing incorrect credentials breaks authentication constraints and triggers an explicit "Login Failed" alert message container.

### Step 2: Employee Profiling & Target Selection
> Upon passing authentication, the application unlocks the complete administrative workspace panel via card-swapping animations:
- Navigate to the Employee Number box (The hardware filter actively ignores non-digit keystrokes).
- Enter a valid database ID (e.g., 10001).
- Click the Pay Coverage Period drop-down menu selector element and pick your target analysis month (June through December), or choose the cumulative summary option.

### Step 3: Execution and Reset Workflows
- To Process Records: Press the Process Payroll button. The tracking engine validates inputs, automatically updates the read-only employee name field, intercepts output print streams, and updates the scrollable ledger canvas.
- To Transition Profiles: Press the Clear Form button to clear all visual data entry components instantly and shift focus straight back to the identification field.
- To End Sessions: Use the Logout button to flush fields and reset constraints back to the initial lock screen safely, or choose Exit to shut down the runtime session completely.
