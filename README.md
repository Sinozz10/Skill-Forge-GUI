# Skill Forge System

## Overview
Learning management system built with Java Swing and JSON persistence. 
Supports three roles: **Admin**, **Instructor**, and **Student**.

---

### 1. Admin Role & Course Approval
- ✅ Admin user role with elevated permissions
- ✅ Review and approve/reject instructor courses
- ✅ Course status: PENDING, APPROVED, REJECTED
- ✅ Only approved courses visible to students
- ✅ Persisted in courses.json

### 2. Instructor Insights & Analytics
- ✅ Quiz results and lesson completion tracking
- ✅ Performance statistics per course/lesson
- ✅ Data synced in users.json and courses.json
- ✅ Charts for student performance (bar charts)
- ✅ Quiz averages and completion percentages

### 3. Quiz System & Assessment
- ✅ Structured Quiz with questions and answers
- ✅ Student quiz attempts and scores stored
- ✅ Lesson completion based on quiz success (70% pass)
- ✅ Multiple-choice and text questions
- ✅ Immediate results and correct answers shown
- ✅ Auto-save to database

### 4. Certificate Generation
- ✅ Course completion tracking (all quizzes passed)
- ✅ PDF certificate generation (iText library)
- ✅ Certificate metadata: ID, date, student, course
- ✅ Stored in users.json
- ✅ View/download certificates from dashboard

### 5. OOP Concepts
- ✅ Inheritance (User → Admin/Instructor/Student, etc.)
- ✅ Polymorphism (abstract methods, interfaces)
- ✅ Abstraction (abstract classes, interfaces)
- ✅ Encapsulation (private fields, getters/setters)

---

## Libraries Required

1. **FlatLaf** - Modern dark UI theme
2. **Gson** - JSON serialization/deserialization
3. **JFreeChart** - Data visualization (bar charts)
4. **iText** - PDF certificate generation

---

## Project Structure
```
src/
├── Main.java                    # Entry point
├── AdminMain.java               # Admin signup entry
├── MainUI/                      # All UI components
├── Dialogs/                     # Dialog windows
├── Statistics/                  # Charts and analytics
├── DataManagment/               # Database managers
├── CustomUIElements/            # Custom UI components
└── CustomDataTypes/             # Domain models
```
```
User (parent)
├── Admin
├── Instructor  
└── Student

Question (parent)
├── TextQuestion
└── ChoiceQuestion

DashBoard (abstract parent)
├── AdminDashboard
├── InstructorDashboard
└── StudentDashboard

CourseView (abstract parent)
├── AdminCourseView
├── EditableCourseView
└── StudentCourseView

SuperChart (abstract parent)
├── CompletionChartCreation
├── LessonChartCreation
└── QuizAnalyticsCompletion
```
---

## Key Files

**Main Entry Points:**
- `Main.java` - Regular login
- `AdminMain.java` - Admin signup

**Data Storage:**
- `files/users.json` - User data
- `files/courses.json` - Course data
- `files/certificates/` - PDF certificates

---

## OOP Concepts Used

**Inheritance:**
- User → Admin, Instructor, Student
- Question → TextQuestion, ChoiceQuestion
- DashBoard → AdminDashboard, InstructorDashboard, StudentDashboard

**Polymorphism:**
- Abstract methods in DashBoard, CourseView, SuperChart
- Interface implementations (Record, CardFactory)

**Abstraction:**
- Abstract classes: DashBoard, CourseView, SuperChart, Question
- Interfaces: Record, CardFactory

**Encapsulation:**
- Private fields with getters/setters
- Singleton pattern for database managers
- Password hashing (SHA-256)

---

## Run Requirments

**Prerequisites:**
- Java 17+
- Required JARs: FlatLaf, Gson, JFreeChart, iText

**Run:**
```bash
java Main.java          # Normal login
java AdminMain.java     # Admin signup
```

---

## User Features

**Admin:**
- Review pending courses
- Approve/Reject courses
- View all system courses

**Instructor:**
- Create courses with chapters/lessons
- Add quizzes (multiple choice or text)
- View student performance analytics

**Student:**
- Enroll in approved courses
- Complete lessons and take quizzes
- Earn certificates upon completion

---

## Security

# SHA-256 password hashing
- SHA-256 processes data in 512-bit blocks, and the input message is first padded to ensure its length is a multiple of 512 bits.
  The algorithm uses a series of bitwise operations, including AND, XOR, OR, rotation (Rot), and addition modulo $2^{32}$, 
  along with a message schedule that expands the initial 16 words into 64 words for processing over 64 rounds.
  The initial hash value is a fixed 256-bit value, and the compression function iteratively processes each block to produce the final hash.

---

**Authors : [Sinozz10](https://github.com/Sinozz10) - [CosmicBreadCat](https://github.com/CosmicBreadCat)**  
**Alexandria University - Faculty of Engineering**  
**CC272 Programming II - Fall 2025/2026**
