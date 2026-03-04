# LMS Class Members & Behavior Methods - Complete Analysis

## 🎯 Executive Summary

This document provides a comprehensive breakdown of all class members, fields, and behavior methods for the Learning Management System core objects, including previously missing elements like certificates, assignments, and detailed enrollment management.

---

## 🏗️ Core Class Definitions

### 1. **Students Class** - Complete Member Analysis

#### **📋 Fields (Class Members)**
```java
public class Students {
    // Core Identity Fields
    private int id;                    // Unique system identifier
    private String name;                // Full legal name
    private String email;               // Primary email for communications
    private String mobile;              // Mobile number for SMS notifications
    private String username;            // Login username
    private String password;            // Encrypted password hash
    
    // Profile Fields
    private String firstName;           // First name for personalization
    private String lastName;            // Last name for formal records
    private String profilePicture;      // URL to profile image
    private Date dateOfBirth;           // Date of birth for age verification
    private String gender;              // Gender identity (M/F/Other/PreferNot)
    private String nationality;         // Country of citizenship
    private String timezone;            // Preferred timezone for scheduling
    
    // Academic Fields
    private String educationLevel;      // Highest education completed
    private String institution;         // Previous educational institution
    private String major;               // Field of study
    private String interests;           // Academic interests (comma-separated)
    private String goals;               // Learning objectives
    
    // Enrollment & Progress Fields
    private int maxCourses;             // Maximum concurrent courses allowed
    private int currentCourseCount;     // Currently enrolled courses
    private double overallGPA;          // Grade point average across all courses
    private int totalCertificates;       // Number of certificates earned
    private Date lastLoginDate;         // Most recent login timestamp
    private boolean isActive;           // Account status (active/suspended)
    private boolean isVerified;         // Email verification status
    
    // Preferences & Settings
    private String language;            // Preferred interface language
    private boolean emailNotifications;  // Email notification preferences
    private boolean smsNotifications;    // SMS notification preferences
    private boolean pushNotifications;   // Push notification preferences
    private String learningStyle;        // Visual/Auditory/Kinesthetic preference
    private int studyHoursPerWeek;       // Self-reported study capacity
    
    // Financial Fields
    private String paymentMethod;        // Preferred payment method
    private String billingAddress;       // Billing information
    private Date subscriptionExpiry;      // Premium access expiration
    private boolean hasPremiumAccess;    // Premium subscription status
    
    // System Fields
    private Date registrationDate;       // Account creation date
    private Date lastUpdated;           // Profile last update
    private String createdBy;            // Who created the account
    private String lastModifiedBy;       // Who last modified the account
}
```

#### **🔧 Behavior Methods**
```java
// Core Identity Methods
public int getId() { return id; }
public void setId(int id) { this.id = id; }
public String getName() { return name; }
public void setName(String name) { this.name = name; }
public String getEmail() { return email; }
public void setEmail(String email) { this.email = email; }
public String getMobile() { return mobile; }
public void setMobile(String mobile) { this.mobile = mobile; }

// Authentication Methods
public boolean authenticate(String password) {
    return PasswordHasher.verify(password, this.password);
}
public void changePassword(String oldPassword, String newPassword) {
    if (authenticate(oldPassword)) {
        this.password = PasswordHasher.hash(newPassword);
    }
}
public void resetPassword(String newPassword) {
    this.password = PasswordHasher.hash(newPassword);
}

// Enrollment Management Methods
public boolean canEnrollInCourse(Course course) {
    return currentCourseCount < maxCourses && 
           !isEnrolledInCourse(course.getId()) &&
           course.hasCapacity() &&
           course.isPublished();
}
public void enrollInCourse(int courseId) {
    if (currentCourseCount < maxCourses) {
        enrolledCourses.add(courseId);
        currentCourseCount++;
    }
}
public void unenrollFromCourse(int courseId) {
    if (enrolledCourses.remove(courseId)) {
        currentCourseCount--;
    }
}
public boolean isEnrolledInCourse(int courseId) {
    return enrolledCourses.contains(courseId);
}
public List<Integer> getEnrolledCourses() {
    return new ArrayList<>(enrolledCourses);
}

// Progress Tracking Methods
public void updateGPA(double newGPA) {
    this.overallGPA = newGPA;
}
public void addCertificate(Certificate certificate) {
    earnedCertificates.add(certificate.getId());
    totalCertificates++;
}
public boolean hasCompletedCourse(int courseId) {
    return completedCourses.contains(courseId);
}
public void markCourseCompleted(int courseId) {
    completedCourses.add(courseId);
}
public double getCourseProgress(int courseId) {
    return progressTracker.getOrDefault(courseId, 0.0);
}
public void updateCourseProgress(int courseId, double progress) {
    progressTracker.put(courseId, Math.min(100.0, progress));
}

// Academic Performance Methods
public List<QuizAttempt> getQuizAttempts(int courseId) {
    return quizAttempts.stream()
        .filter(attempt -> attempt.getCourseId() == courseId)
        .collect(Collectors.toList());
}
public double getAverageScore(int courseId) {
    return getQuizAttempts(courseId).stream()
        .mapToInt(QuizAttempt::getScore)
        .average()
        .orElse(0.0);
}
public int getTotalStudyTime(int courseId) {
    return studyTimeTracker.getOrDefault(courseId, 0);
}
public void addStudyTime(int courseId, int minutes) {
    studyTimeTracker.merge(courseId, minutes, Integer::sum);
}

// Notification Methods
public void sendNotification(Notification notification) {
    if (emailNotifications && notification.getType() == NotificationType.EMAIL) {
        EmailService.send(this.email, notification);
    }
    if (smsNotifications && notification.getType() == NotificationType.SMS) {
        SMSService.send(this.mobile, notification);
    }
    if (pushNotifications && notification.getType() == NotificationType.PUSH) {
        PushService.send(this.deviceToken, notification);
    }
}

// Preference Methods
public void updatePreferences(StudentPreferences preferences) {
    this.language = preferences.getLanguage();
    this.emailNotifications = preferences.isEmailEnabled();
    this.smsNotifications = preferences.isSmsEnabled();
    this.pushNotifications = preferences.isPushEnabled();
    this.learningStyle = preferences.getLearningStyle();
}

// Validation Methods
public ValidationResult validate() {
    ValidationResult result = ValidationResult.valid();
    
    if (name == null || name.trim().isEmpty()) {
        result.addError("Name is required");
    }
    if (email == null || !EmailValidator.isValid(email)) {
        result.addError("Valid email is required");
    }
    if (mobile == null || !PhoneValidator.isValid(mobile)) {
        result.addError("Valid mobile number is required");
    }
    
    return result;
}

// Business Logic Methods
public boolean canAccessCourse(int courseId) {
    return isEnrolledInCourse(courseId) && 
           isActive && 
           !isAccountExpired();
}
public boolean hasCompletedPrerequisites(Course course) {
    for (int prereqId : course.getPrerequisites()) {
        if (!hasCompletedCourse(prereqId)) {
            return false;
        }
    }
    return true;
}
public LearningPath generateLearningPath() {
    return LearningPathBuilder.build(this, enrolledCourses, interests, goals);
}
```

---

### 2. **Teacher Class** - Complete Member Analysis

#### **📋 Fields (Class Members)**
```java
public class Teacher {
    // Core Identity Fields
    private int id;                    // Unique system identifier
    private String name;                // Full name for course attribution
    private String email;               // Professional email
    private String mobile;              // Business contact number
    private String username;            // Login username
    private String password;            // Encrypted password hash
    
    // Professional Profile Fields
    private String firstName;           // First name
    private String lastName;            // Last name
    private String title;               // Academic title (Dr., Prof., Mr., Ms.)
    private String department;          // Academic department
    private String institution;         // Home institution
    private String specialization;       // Area of expertise
    private String bio;                 // Professional biography
    private String profilePicture;      // URL to professional photo
    private String website;             // Professional website or portfolio
    
    // Credentials & Qualifications
    private String highestDegree;       // Highest academic degree
    private String degreeInstitution;    // Degree granting institution
    private int yearsOfExperience;       // Teaching experience in years
    private List<String> certifications; // Professional certifications
    private List<String> publications;   // Academic publications
    private String researchInterests;    // Research focus areas
    private String teachingPhilosophy;  // Approach to teaching
    
    // Teaching Capacity Fields
    private int maxCourses;             // Maximum courses can teach
    private int currentCourseLoad;      // Currently teaching courses
    private int maxStudentsPerCourse;    // Maximum students per course
    private int totalStudentsTaught;     // Lifetime student count
    private double averageRating;        // Student feedback rating
    private int totalRatings;            // Number of ratings received
    
    // Availability & Schedule Fields
    private Map<DayOfWeek, List<TimeSlot>> availability; // Weekly availability
    private String timezone;            // Preferred timezone
    private boolean acceptsNewStudents;  // Accepting new enrollments
    private String responseTime;         // Average response time to students
    private boolean isAvailableForConsultation; // Consultation availability
    
    // Compensation Fields
    private double baseSalary;           // Base compensation
    private double perStudentRate;       // Rate per student
    private String paymentMethod;        // Preferred payment method
    private String taxInformation;       // Tax withholding information
    private Date contractStartDate;      // Contract start date
    private Date contractEndDate;        // Contract end date
    
    // Content Creation Fields
    private int coursesCreated;          // Number of courses created
    private int modulesCreated;          // Number of modules created
    private int lessonsCreated;          // Number of lessons created
    private int quizzesCreated;          // Number of quizzes created
    private int questionsCreated;        // Number of questions created
    private Date lastContentCreation;    // Most recent content creation
    
    // Performance Metrics Fields
    private double studentSatisfactionRate; // Student satisfaction percentage
    private double courseCompletionRate;    // Average course completion rate
    private double averageStudentScore;     // Average student performance
    private int totalHoursTaught;            // Total teaching hours
    private int activeStudentCount;          // Currently active students
    
    // System Fields
    private Date registrationDate;       // Account creation date
    private Date lastLoginDate;         // Most recent login
    private boolean isActive;           // Account status
    private boolean isVerified;         // Verification status
    private String approvalStatus;       // Admin approval status
}
```

#### **🔧 Behavior Methods**
```java
// Core Identity Methods
public int getId() { return id; }
public void setId(int id) { this.id = id; }
public String getName() { return name; }
public void setName(String name) { this.name = name; }
public String getEmail() { return email; }
public void setEmail(String email) { this.email = email; }
public String getMobile() { return mobile; }
public void setMobile(String mobile) { this.mobile = mobile; }

// Course Management Methods
public boolean canCreateCourse() {
    return currentCourseLoad < maxCourses && 
           isActive && 
           isVerified && 
           "APPROVED".equals(approvalStatus);
}
public Course createCourse(CourseRequest request) {
    if (!canCreateCourse()) {
        throw new IllegalStateException("Cannot create course - limits exceeded");
    }
    
    Course course = new Course();
    course.setTeacherId(this.id);
    course.setName(request.getName());
    course.setDescription(request.getDescription());
    course.setCategory(request.getCategory());
    course.setLevel(request.getLevel());
    course.setPrice(request.getPrice());
    course.setMaxStudents(this.maxStudentsPerCourse);
    
    createdCourses.add(course.getId());
    currentCourseLoad++;
    coursesCreated++;
    lastContentCreation = new Date();
    
    return course;
}
public void updateCourse(int courseId, CourseUpdate update) {
    Course course = getCourse(courseId);
    if (course != null && course.getTeacherId() == this.id) {
        course.applyUpdate(update);
        lastContentCreation = new Date();
    }
}
public void deleteCourse(int courseId) {
    Course course = getCourse(courseId);
    if (course != null && course.getTeacherId() == this.id && 
        !course.hasActiveEnrollments()) {
        createdCourses.remove(courseId);
        currentCourseLoad--;
        course.archive();
    }
}
public List<Course> getMyCourses() {
    return createdCourses.stream()
        .map(this::getCourse)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
}

// Content Creation Methods
public Module createModule(int courseId, ModuleRequest request) {
    Course course = getCourse(courseId);
    if (course != null && course.getTeacherId() == this.id) {
        Module module = new Module();
        module.setCourseId(courseId);
        module.setName(request.getName());
        module.setDescription(request.getDescription());
        module.setOrder(request.getOrder());
        
        course.addModule(module);
        modulesCreated++;
        lastContentCreation = new Date();
        
        return module;
    }
    return null;
}
public Lesson createLesson(int moduleId, LessonRequest request) {
    Module module = getModule(moduleId);
    if (module != null && canModifyModule(module)) {
        Lesson lesson = createLessonByType(request.getType());
        lesson.setModuleId(moduleId);
        lesson.setName(request.getName());
        lesson.setDescription(request.getDescription());
        lesson.setContent(request.getContent());
        
        module.addLesson(lesson);
        lessonsCreated++;
        lastContentCreation = new Date();
        
        return lesson;
    }
    return null;
}
public Quiz createQuiz(int lessonId, QuizRequest request) {
    Lesson lesson = getLesson(lessonId);
    if (lesson != null && canModifyLesson(lesson)) {
        Quiz quiz = new Quiz();
        quiz.setLessonId(lessonId);
        quiz.setName(request.getName());
        quiz.setTotalMarks(request.getTotalMarks());
        quiz.setPassingMarks(request.getPassingMarks());
        quiz.setTimeLimit(request.getTimeLimit());
        
        lesson.addQuiz(quiz);
        quizzesCreated++;
        lastContentCreation = new Date();
        
        return quiz;
    }
    return null;
}
public Question createQuestion(int quizId, QuestionRequest request) {
    Quiz quiz = getQuiz(quizId);
    if (quiz != null && canModifyQuiz(quiz)) {
        Question question = createQuestionByType(request.getType());
        question.setQuizId(quizId);
        question.setQuestion(request.getQuestion());
        question.setOptions(request.getOptions());
        question.setCorrectAnswer(request.getCorrectAnswer());
        question.setMarks(request.getMarks());
        
        quiz.addQuestion(question);
        questionsCreated++;
        lastContentCreation = new Date();
        
        return question;
    }
    return null;
}

// Student Interaction Methods
public List<Student> getEnrolledStudents(int courseId) {
    Course course = getCourse(courseId);
    return course != null && course.getTeacherId() == this.id ? 
           course.getEnrolledStudents() : Collections.emptyList();
}
public void sendAnnouncement(int courseId, Announcement announcement) {
    Course course = getCourse(courseId);
    if (course != null && course.getTeacherId() == this.id) {
        course.getEnrolledStudents().forEach(student -> 
            student.sendNotification(announcement));
    }
}
public void gradeQuizAttempt(QuizAttempt attempt, int score) {
    Quiz quiz = getQuiz(attempt.getQuizId());
    if (quiz != null && canGradeQuiz(quiz)) {
        attempt.setScore(score);
        attempt.setGradedBy(this.id);
        attempt.setGradedDate(new Date());
        attempt.setState(QuizState.GRADED);
        
        updatePerformanceMetrics(attempt);
    }
}
public void provideFeedback(QuizAttempt attempt, String feedback) {
    if (canGradeQuiz(getQuiz(attempt.getQuizId()))) {
        attempt.setFeedback(feedback);
        attempt.setFeedbackDate(new Date());
    }
}

// Performance Analytics Methods
public TeacherPerformanceReport getPerformanceReport() {
    return TeacherPerformanceReport.builder()
        .teacherId(this.id)
        .totalCoursesCreated(coursesCreated)
        .totalStudentsTaught(totalStudentsTaught)
        .averageRating(averageRating)
        .totalRatings(totalRatings)
        .studentSatisfactionRate(studentSatisfactionRate)
        .courseCompletionRate(courseCompletionRate)
        .averageStudentScore(averageStudentScore)
        .totalHoursTaught(totalHoursTaught)
        .activeStudentCount(activeStudentCount)
        .build();
}
public void updatePerformanceMetrics(QuizAttempt attempt) {
    // Update student performance tracking
    Student student = attempt.getStudent();
    student.updateCoursePerformance(attempt.getCourseId(), attempt.getScore());
    
    // Update teacher performance metrics
    updateAverageRating(student.getRatingForTeacher(this.id));
    updateCompletionRate();
    updateAverageStudentScore(attempt.getScore());
}
public List<CourseAnalytics> getCourseAnalytics() {
    return createdCourses.stream()
        .map(this::getCourseAnalytics)
        .collect(Collectors.toList());
}

// Availability & Scheduling Methods
public boolean isAvailable(TimeSlot timeSlot) {
    DayOfWeek day = timeSlot.getDay();
    return availability.getOrDefault(day, Collections.emptyList())
        .stream()
        .anyMatch(available -> available.overlaps(timeSlot));
}
public void setAvailability(DayOfWeek day, List<TimeSlot> timeSlots) {
    availability.put(day, timeSlots);
}
public List<TimeSlot> getAvailableSlots(DayOfWeek day) {
    return availability.getOrDefault(day, Collections.emptyList());
}
public boolean canScheduleConsultation(TimeSlot requestedSlot) {
    return isAvailableForConsultation && isAvailable(requestedSlot);
}
public ConsultationSession scheduleConsultation(Student student, TimeSlot slot) {
    if (canScheduleConsultation(slot)) {
        ConsultationSession session = new ConsultationSession();
        session.setTeacherId(this.id);
        session.setStudentId(student.getId());
        session.setScheduledTime(slot);
        session.setStatus(ConsultationStatus.SCHEDULED);
        
        scheduledSessions.add(session);
        return session;
    }
    return null;
}

// Compensation Methods
public double calculateMonthlyEarnings() {
    double baseEarnings = baseSalary / 12;
    double studentEarnings = activeStudentCount * perStudentRate;
    return baseEarnings + studentEarnings;
}
public PaymentRecord generatePaymentRecord(Period period) {
    return PaymentRecord.builder()
        .teacherId(this.id)
        .period(period)
        .baseSalary(baseSalary)
        .studentEarnings(activeStudentCount * perStudentRate)
        .totalEarnings(calculateMonthlyEarnings())
        .paymentMethod(paymentMethod)
        .build();
}

// Validation Methods
public ValidationResult validate() {
    ValidationResult result = ValidationResult.valid();
    
    if (name == null || name.trim().isEmpty()) {
        result.addError("Name is required");
    }
    if (email == null || !EmailValidator.isValid(email)) {
        result.addError("Valid email is required");
    }
    if (specialization == null || specialization.trim().isEmpty()) {
        result.addError("Specialization is required");
    }
    if (yearsOfExperience < 0) {
        result.addError("Experience cannot be negative");
    }
    
    return result;
}

// Business Logic Methods
private boolean canModifyModule(Module module) {
    Course course = getCourse(module.getCourseId());
    return course != null && course.getTeacherId() == this.id;
}
private boolean canModifyLesson(Lesson lesson) {
    Module module = getModule(lesson.getModuleId());
    return module != null && canModifyModule(module);
}
private boolean canModifyQuiz(Quiz quiz) {
    Lesson lesson = getLesson(quiz.getLessonId());
    return lesson != null && canModifyLesson(lesson);
}
private boolean canGradeQuiz(Quiz quiz) {
    Lesson lesson = getLesson(quiz.getLessonId());
    return lesson != null && canModifyLesson(lesson);
}
```

---

### 3. **Course Class** - Complete Member Analysis

#### **📋 Fields (Class Members)**
```java
public class Course {
    // Core Identity Fields
    private int id;                    // Unique course identifier
    private String name;                // Course title
    private String description;         // Detailed course description
    private String shortDescription;    // Brief description for listings
    private String courseCode;          // Institutional course code
    private String version;             // Course version for updates
    
    // Instructor & Ownership Fields
    private int teacherId;              // Primary instructor ID
    private List<Integer> assistantIds; // Teaching assistant IDs
    private String instructorName;      // Display name for instructor
    private String instructorBio;       // Instructor biography
    private String instructorQualifications; // Instructor credentials
    
    // Content Structure Fields
    private List<Module> modules;      // Course modules
    private int totalModules;           // Number of modules
    private int totalLessons;           // Total lessons across all modules
    private int totalQuizzes;           // Total assessments
    private int totalHours;             // Estimated completion time
    private String difficulty;          // Difficulty level (Beginner/Intermediate/Advanced)
    private String language;            // Course language
    
    // Enrollment & Capacity Fields
    private int maxStudents;            // Maximum enrollment capacity
    private int currentEnrollment;      // Currently enrolled students
    private int minStudents;            // Minimum students to run course
    private List<Integer> waitlist;     // Waiting list for full courses
    private Date enrollmentStartDate;    // When enrollment opens
    private Date enrollmentEndDate;      // When enrollment closes
    private Date courseStartDate;        // When course begins
    private Date courseEndDate;          // When course ends
    
    // Pricing & Financial Fields
    private double price;                // Course price
    private String currency;             // Price currency
    private String paymentPlan;          // Payment options (one-time/installments)
    private boolean hasFreeTrial;        // Free trial availability
    private int trialDuration;           // Trial period in days
    private List<String> includedMaterials; // Materials included in price
    private List<String> additionalCosts; // Extra costs not included
    
    // Prerequisites & Requirements Fields
    private List<Integer> prerequisites; // Required course IDs
    private String requiredSkills;       // Skills students should have
    private String requiredEquipment;    // Equipment/software needed
    private String targetAudience;       // Intended student profile
    private String learningOutcomes;     // What students will achieve
    private String certificateOffered;   // Certificate type offered
    
    // Course Content Fields
    private String syllabus;             // Course syllabus content
    private String curriculum;           // Detailed curriculum
    private List<String> learningObjectives; // Specific learning goals
    private List<String> topics;         // Course topics covered
    private Map<String, String> resources; // Additional learning resources
    private String thumbnailUrl;         // Course thumbnail image
    private String previewVideo;         // Course preview video URL
    
    // Assessment & Grading Fields
    private GradingScheme gradingScheme; // How course is graded
    private double passingGrade;         // Minimum grade to pass
    private List<Assignment> assignments; // Course assignments
    private List<Exam> exams;            // Course exams
    private double assignmentWeight;      // Assignment weight in final grade
    private double examWeight;            // Exam weight in final grade
    private double participationWeight;   // Participation weight
    
    // Interaction & Support Fields
    private String supportLevel;         // Support availability (high/medium/low)
    private List<String> officeHours;     // Instructor office hours
    private String communicationMethod;   // How students interact (forum/email/chat)
    private boolean hasDiscussionForum;   // Forum availability
    private boolean hasLiveSessions;     // Live session availability
    private String schedule;              // Live session schedule
    
    // Technical & Platform Fields
    private String platform;             // Delivery platform (web/mobile/both)
    private List<String> requiredSoftware; // Software requirements
    private List<String> compatibleDevices; // Device compatibility
    private boolean isOfflineAccessible; // Offline content availability
    private String bandwidthRequirement;  // Internet speed needed
    
    // Quality & Accreditation Fields
    private String accreditation;        // Accrediting body
    private Date accreditationDate;      // When accreditation was granted
    private String qualityRating;         // Quality assessment rating
    private List<String> reviews;        // Student reviews
    private double averageRating;         // Average student rating
    private int totalReviews;            // Number of reviews
    private String completionCertificate; // Certificate details
    
    // Analytics & Performance Fields
    private int totalCompletions;        // Students who completed
    private double completionRate;        // Percentage completion rate
    private double averageCompletionTime; // Average time to complete
    private double studentSatisfaction;   // Satisfaction percentage
    private Map<String, Object> metrics; // Various performance metrics
    
    // State & Lifecycle Fields
    private CourseState currentState;     // Current lifecycle state
    private Date creationDate;           // When course was created
    private Date lastUpdated;            // Last modification date
    private Date publicationDate;         // When course was published
    private Date archivalDate;            // When course was archived
    private String status;                // Active/inactive/suspended
    private boolean isPublic;             // Public visibility
    private boolean isFeatured;           // Featured course status
    
    // Version & Update Fields
    private String currentVersion;        // Current version number
    private List<CourseUpdate> updateHistory; // Version history
    private boolean requiresUpdate;       // Students need to update
    private Date lastContentUpdate;       // Most recent content change
    
    // Localization Fields
    private Map<String, String> translations; // Multi-language support
    private String defaultLanguage;      // Primary course language
    private List<String> supportedLanguages; // All available languages
    private String timeZone;             // Course time zone
    
    // Integration Fields
    private String lmsIntegration;        // LMS system integration
    private List<String> externalTools;  // Third-party tools used
    private String apiEndpoints;         // Available API endpoints
    private Map<String, String> webhooks; // Webhook configurations
}
```

#### **🔧 Behavior Methods**
```java
// Core Identity Methods
public int getId() { return id; }
public void setId(int id) { this.id = id; }
public String getName() { return name; }
public void setName(String name) { this.name = name; }
public String getDescription() { return description; }
public void setDescription(String description) { this.description = description; }
public String getCourseCode() { return courseCode; }
public void setCourseCode(String courseCode) { this.courseCode = courseCode; }

// Content Structure Methods
public void addModule(Module module) {
    if (module != null && !modules.contains(module)) {
        modules.add(module);
        module.setCourseId(this.id);
        updateContentCounts();
    }
}
public void removeModule(Module module) {
    if (modules.remove(module)) {
        module.setCourseId(-1);
        updateContentCounts();
    }
}
public Module getModule(int moduleId) {
    return modules.stream()
        .filter(m -> m.getId() == moduleId)
        .findFirst()
        .orElse(null);
}
public List<Module> getModules() {
    return new ArrayList<>(modules);
}
public void reorderModules(List<Integer> newOrder) {
    Map<Integer, Module> moduleMap = modules.stream()
        .collect(Collectors.toMap(Module::getId, m -> m));
    
    modules.clear();
    for (int moduleId : newOrder) {
        Module module = moduleMap.get(moduleId);
        if (module != null) {
            modules.add(module);
        }
    }
}
private void updateContentCounts() {
    this.totalModules = modules.size();
    this.totalLessons = modules.stream()
        .mapToInt(m -> m.getLessonCount())
        .sum();
    this.totalQuizzes = modules.stream()
        .mapToInt(m -> m.getQuizCount())
        .sum();
    this.totalHours = modules.stream()
        .mapToInt(Module::getEstimatedHours)
        .sum();
}

// Enrollment Management Methods
public boolean canEnroll() {
    return currentState.canEnroll() &&
           currentEnrollment < maxStudents &&
           isEnrollmentPeriodOpen() &&
           isPublished();
}
public boolean enrollStudent(int studentId) {
    if (!canEnroll()) {
        return false;
    }
    
    if (currentEnrollment >= maxStudents) {
        waitlist.add(studentId);
        return false;
    }
    
    enrolledStudents.add(studentId);
    currentEnrollment++;
    notifyEnrollment(studentId);
    return true;
}
public void unenrollStudent(int studentId) {
    if (enrolledStudents.remove(studentId)) {
        currentEnrollment--;
        notifyUnenrollment(studentId);
        
        // Offer spot to waitlisted student
        if (!waitlist.isEmpty()) {
            int nextStudent = waitlist.remove(0);
            enrollStudent(nextStudent);
        }
    }
}
public boolean isEnrolled(int studentId) {
    return enrolledStudents.contains(studentId);
}
public List<Integer> getEnrolledStudents() {
    return new ArrayList<>(enrolledStudents);
}
public boolean isEnrollmentPeriodOpen() {
    Date now = new Date();
    return now.after(enrollmentStartDate) && now.before(enrollmentEndDate);
}
public void addToWaitlist(int studentId) {
    if (!waitlist.contains(studentId)) {
        waitlist.add(studentId);
    }
}
public List<Integer> getWaitlist() {
    return new ArrayList<>(waitlist);
}

// Prerequisite & Access Methods
public boolean hasPrerequisites() {
    return prerequisites != null && !prerequisites.isEmpty();
}
public List<Integer> getPrerequisites() {
    return new ArrayList<>(prerequisites);
}
public boolean meetsPrerequisites(Student student) {
    if (!hasPrerequisites()) {
        return true;
    }
    
    for (int prereqId : prerequisites) {
        if (!student.hasCompletedCourse(prereqId)) {
            return false;
        }
    }
    return true;
}
public boolean canStudentEnroll(Student student) {
    return canEnroll() && 
           meetsPrerequisites(student) &&
           !student.isEnrolledInCourse(this.id);
}

// Content Access Methods
public boolean canStudentAccessContent(Student student, int moduleId) {
    if (!isEnrolled(student.getId())) {
        return false;
    }
    
    Module module = getModule(moduleId);
    if (module == null) {
        return false;
    }
    
    // Check if previous modules are completed
    for (Module m : modules) {
        if (m.getOrder() < module.getOrder()) {
            if (!student.hasCompletedModule(m.getId())) {
                return false;
            }
        }
    }
    
    return true;
}
public List<Module> getAccessibleModules(Student student) {
    return modules.stream()
        .filter(module -> canStudentAccessContent(student, module.getId()))
        .collect(Collectors.toList());
}
public Lesson getNextLesson(Student student) {
    for (Module module : modules) {
        for (Lesson lesson : module.getLessons()) {
            if (!student.hasCompletedLesson(lesson.getId())) {
                return lesson;
            }
        }
    }
    return null;
}

// Assessment & Grading Methods
public void addAssignment(Assignment assignment) {
    assignment.setCourseId(this.id);
    assignments.add(assignment);
}
public void removeAssignment(Assignment assignment) {
    assignments.remove(assignment);
}
public List<Assignment> getAssignments() {
    return new ArrayList<>(assignments);
}
public void addExam(Exam exam) {
    exam.setCourseId(this.id);
    exams.add(exam);
}
public List<Exam> getExams() {
    return new ArrayList<>(exams);
}
public double calculateFinalGrade(Student student) {
    double assignmentScore = calculateAssignmentAverage(student);
    double examScore = calculateExamAverage(student);
    double participationScore = student.getParticipationScore(this.id);
    
    return (assignmentScore * assignmentWeight) +
           (examScore * examWeight) +
           (participationScore * participationWeight);
}
public boolean hasPassedCourse(Student student) {
    return calculateFinalGrade(student) >= passingGrade;
}
public Certificate generateCertificate(Student student) {
    if (hasPassedCourse(student) && !student.hasCertificate(this.id)) {
        Certificate certificate = new Certificate();
        certificate.setStudentId(student.getId());
        certificate.setCourseId(this.id);
        certificate.setCourseName(this.name);
        certificate.setInstructorName(this.instructorName);
        certificate.setCompletionDate(new Date());
        certificate.setGrade(calculateFinalGrade(student));
        certificate.setIssueDate(new Date());
        
        return certificate;
    }
    return null;
}

// State Management Methods
public void publish() {
    currentState.publish(this);
}
public void archive() {
    currentState.archive(this);
}
public void suspend() {
    currentState.suspend(this);
}
public void activate() {
    currentState.activate(this);
}
public boolean isPublished() {
    return currentState instanceof PublishedState;
}
public boolean isArchived() {
    return currentState instanceof ArchivedState;
}
public boolean isDraft() {
    return currentState instanceof DraftState;
}
public CourseState getCurrentState() {
    return currentState;
}
public void setCurrentState(CourseState currentState) {
    this.currentState = currentState;
}

// Analytics & Performance Methods
public CourseAnalytics getAnalytics() {
    return CourseAnalytics.builder()
        .courseId(this.id)
        .totalEnrollments(currentEnrollment)
        .totalCompletions(totalCompletions)
        .completionRate(calculateCompletionRate())
        .averageCompletionTime(calculateAverageCompletionTime())
        .studentSatisfaction(calculateSatisfactionRate())
        .averageRating(averageRating)
        .totalReviews(totalReviews)
        .build();
}
public double calculateCompletionRate() {
    if (currentEnrollment == 0) return 0.0;
    return (double) totalCompletions / currentEnrollment * 100;
}
public double calculateAverageCompletionTime() {
    return enrolledStudents.stream()
        .mapToInt(Student::getCourseCompletionTime)
        .average()
        .orElse(0.0);
}
public double calculateSatisfactionRate() {
    return enrolledStudents.stream()
        .mapToInt(Student::getSatisfactionScore)
        .average()
        .orElse(0.0);
}
public void updateMetrics() {
    this.totalCompletions = calculateTotalCompletions();
    this.completionRate = calculateCompletionRate();
    this.averageCompletionTime = calculateAverageCompletionTime();
    this.studentSatisfaction = calculateSatisfactionRate();
}

// Quality & Review Methods
public void addReview(Review review) {
    reviews.add(review);
    updateAverageRating();
}
public List<Review> getReviews() {
    return new ArrayList<>(reviews);
}
public void updateAverageRating() {
    this.averageRating = reviews.stream()
        .mapToInt(Review::getRating)
        .average()
        .orElse(0.0);
    this.totalReviews = reviews.size();
}
public QualityReport generateQualityReport() {
    return QualityReport.builder()
        .courseId(this.id)
        .contentQuality(assessContentQuality())
        .instructorQuality(assessInstructorQuality())
        .studentSatisfaction(studentSatisfaction)
        .completionRate(completionRate)
        .overallQuality(calculateOverallQuality())
        .build();
}

// Version & Update Methods
public void createNewVersion(String version, List<String> changes) {
    CourseUpdate update = new CourseUpdate();
    update.setVersion(version);
    update.setChanges(changes);
    update.setUpdateDate(new Date());
    update.setUpdatedBy(getCurrentInstructorId());
    
    updateHistory.add(update);
    this.currentVersion = version;
    this.lastContentUpdate = new Date();
}
public List<CourseUpdate> getUpdateHistory() {
    return new ArrayList<>(updateHistory);
}
public boolean requiresStudentUpdate() {
    return requiresUpdate;
}
public void markStudentsForUpdate() {
    this.requiresUpdate = true;
    enrolledStudents.forEach(this::notifyUpdateRequired);
}

// Validation Methods
public ValidationResult validate() {
    ValidationResult result = ValidationResult.valid();
    
    if (name == null || name.trim().isEmpty()) {
        result.addError("Course name is required");
    }
    if (description == null || description.trim().isEmpty()) {
        result.addError("Course description is required");
    }
    if (teacherId <= 0) {
        result.addError("Valid teacher ID is required");
    }
    if (modules.isEmpty()) {
        result.addError("Course must have at least one module");
    }
    if (price < 0) {
        result.addError("Course price cannot be negative");
    }
    if (maxStudents <= 0) {
        result.addError("Maximum students must be greater than 0");
    }
    
    return result;
}

// Business Logic Methods
private void notifyEnrollment(int studentId) {
    Notification notification = Notification.builder()
        .type(NotificationType.ENROLLMENT_CONFIRMATION)
        .recipientId(studentId)
        .courseId(this.id)
        .message("You have been enrolled in " + this.name)
        .build();
    
    NotificationService.send(notification);
}
private void notifyUnenrollment(int studentId) {
    Notification notification = Notification.builder()
        .type(NotificationType.UNENROLLMENT_CONFIRMATION)
        .recipientId(studentId)
        .courseId(this.id)
        .message("You have been unenrolled from " + this.name)
        .build();
    
    NotificationService.send(notification);
}
private void notifyUpdateRequired(int studentId) {
    Notification notification = Notification.builder()
        .type(NotificationType.COURSE_UPDATE)
        .recipientId(studentId)
        .courseId(this.id)
        .message("Course " + this.name + " has been updated")
        .build();
    
    NotificationService.send(notification);
}
private int calculateTotalCompletions() {
    return (int) enrolledStudents.stream()
        .filter(student -> student.hasCompletedCourse(this.id))
        .count();
}
```

---

### 4. **Module Class** - Complete Member Analysis

#### **📋 Fields (Class Members)**
```java
public class Module {
    // Core Identity Fields
    private int id;                    // Unique module identifier
    private String name;                // Module title
    private String description;         // Module description and objectives
    private String shortDescription;    // Brief description for navigation
    private String moduleCode;          // Module code for reference
    
    // Course Association Fields
    private int courseId;              // Parent course ID
    private String courseName;          // Parent course name
    private int order;                  // Sequence order within course
    private boolean isRequired;         // Whether module is mandatory
    private boolean isLocked;           // Whether module is locked by prerequisites
    
    // Content Structure Fields
    private List<Lesson> lessons;      // Module lessons
    private int totalLessons;           // Number of lessons
    private int totalQuizzes;           // Number of assessments
    private int estimatedHours;         // Estimated completion time
    private String difficulty;          // Module difficulty level
    private String contentType;         // Type of content (video/text/mixed)
    
    // Prerequisites & Dependencies Fields
    private List<Integer> prerequisites; // Required module IDs
    private List<Integer> dependencies;  // Module dependencies
    private boolean hasPrerequisites;    // Whether module has prerequisites
    private String unlockMessage;       // Message shown when locked
    
    // Schedule & Timeline Fields
    private Date startDate;             // When module becomes available
    private Date endDate;               // When module becomes unavailable
    private Date suggestedStartDate;    // Recommended start date
    private int durationDays;           // Suggested duration in days
    private boolean isSelfPaced;        // Can be completed at own pace
    private List<Deadline> deadlines;   // Assignment/quiz deadlines
    
    // Assessment & Grading Fields
    private double passingGrade;         // Grade needed to pass module
    private double weightInCourse;       // Weight in final course grade
    private List<Quiz> quizzes;         // Module assessments
    private List<Assignment> assignments; // Module assignments
    private GradingScheme gradingScheme; // How module is graded
    
    // Resources & Materials Fields
    private List<String> requiredMaterials; // Materials needed
    private List<String> optionalMaterials; // Optional materials
    private List<String> downloadableResources; // Files to download
    private Map<String, String> externalLinks; // External resource links
    private String readingList;          // Required readings
    
    // Interaction & Support Fields
    private boolean hasDiscussionForum;   // Forum for this module
    private boolean hasLiveSession;       // Live session availability
    private String liveSessionSchedule;    // When live sessions occur
    private String instructorSupport;     // Support availability
    private List<String> faq;              // Frequently asked questions
    
    // Progress & Completion Fields
    private double completionThreshold;   // Percentage needed for completion
    private boolean allowPartialCompletion; // Can complete partially
    private String completionCriteria;     // What constitutes completion
    private List<String> completionCheckpoints; // Progress milestones
    
    // Localization Fields
    private Map<String, String> translations; // Multi-language support
    private String defaultLanguage;      // Primary language
    private List<String> supportedLanguages; // Available languages
    
    // Analytics & Metrics Fields
    private int totalCompletions;        // Students who completed
    private double averageCompletionTime; // Average time to complete
    private double averageScore;          // Average student score
    private int totalAttempts;            // Total quiz attempts
    private double successRate;          // Module success rate
    
    // Quality & Feedback Fields
    private List<String> studentFeedback; // Student comments
    private double averageRating;         // Average student rating
    private int totalRatings;            // Number of ratings
    private String qualityAssessment;     // Internal quality score
    
    // System & Metadata Fields
    private Date creationDate;           // When module was created
    private Date lastUpdated;            // Last modification date
    private String createdBy;            // Who created the module
    private String lastModifiedBy;       // Who last modified
    private String status;                // Active/inactive/archived
    private boolean isPublished;         // Publication status
    private String version;               // Module version
}
```

#### **🔧 Behavior Methods**
```java
// Core Identity Methods
public int getId() { return id; }
public void setId(int id) { this.id = id; }
public String getName() { return name; }
public void setName(String name) { this.name = name; }
public String getDescription() { return description; }
public void setDescription(String description) { this.description = description; }
public int getCourseId() { return courseId; }
public void setCourseId(int courseId) { this.courseId = courseId; }
public int getOrder() { return order; }
public void setOrder(int order) { this.order = order; }

// Lesson Management Methods
public void addLesson(Lesson lesson) {
    if (lesson != null && !lessons.contains(lesson)) {
        lessons.add(lesson);
        lesson.setModuleId(this.id);
        updateContentCounts();
    }
}
public void removeLesson(Lesson lesson) {
    if (lessons.remove(lesson)) {
        lesson.setModuleId(-1);
        updateContentCounts();
    }
}
public Lesson getLesson(int lessonId) {
    return lessons.stream()
        .filter(l -> l.getId() == lessonId)
        .findFirst()
        .orElse(null);
}
public List<Lesson> getLessons() {
    return new ArrayList<>(lessons);
}
public Lesson getLessonByOrder(int lessonOrder) {
    return lessons.stream()
        .filter(l -> l.getOrder() == lessonOrder)
        .findFirst()
        .orElse(null);
}
public void reorderLessons(List<Integer> newOrder) {
    Map<Integer, Lesson> lessonMap = lessons.stream()
        .collect(Collectors.toMap(Lesson::getId, l -> l));
    
    lessons.clear();
    for (int lessonId : newOrder) {
        Lesson lesson = lessonMap.get(lessonId);
        if (lesson != null) {
            lessons.add(lesson);
        }
    }
}
private void updateContentCounts() {
    this.totalLessons = lessons.size();
    this.totalQuizzes = lessons.stream()
        .mapToInt(l -> l.getQuizCount())
        .sum();
    this.estimatedHours = lessons.stream()
        .mapToInt(Lesson::getEstimatedHours)
        .sum();
}

// Prerequisite & Access Methods
public boolean hasPrerequisites() {
    return prerequisites != null && !prerequisites.isEmpty();
}
public List<Integer> getPrerequisites() {
    return new ArrayList<>(prerequisites);
}
public boolean meetsPrerequisites(Student student) {
    if (!hasPrerequisites()) {
        return true;
    }
    
    for (int prereqId : prerequisites) {
        if (!student.hasCompletedModule(prereqId)) {
            return false;
        }
    }
    return true;
}
public boolean isAccessibleTo(Student student) {
    return meetsPrerequisites(student) &&
           !isLocked &&
           isWithinSchedule() &&
           isPublished;
}
public boolean isWithinSchedule() {
    Date now = new Date();
    boolean afterStart = startDate == null || now.after(startDate);
    boolean beforeEnd = endDate == null || now.before(endDate);
    return afterStart && beforeEnd;
}
public void unlock() {
    this.isLocked = false;
}
public void lock() {
    this.isLocked = true;
}
public String getUnlockMessage() {
    return unlockMessage;
}

// Assessment & Grading Methods
public void addQuiz(Quiz quiz) {
    quiz.setModuleId(this.id);
    quizzes.add(quiz);
    updateContentCounts();
}
public void removeQuiz(Quiz quiz) {
    quizzes.remove(quiz);
    updateContentCounts();
}
public List<Quiz> getQuizzes() {
    return new ArrayList<>(quizzes);
}
public void addAssignment(Assignment assignment) {
    assignment.setModuleId(this.id);
    assignments.add(assignment);
}
public List<Assignment> getAssignments() {
    return new ArrayList<>(assignments);
}
public double calculateModuleGrade(Student student) {
    double quizScore = calculateQuizAverage(student);
    double assignmentScore = calculateAssignmentAverage(student);
    
    return gradingScheme.calculateFinalGrade(quizScore, assignmentScore);
}
public boolean hasPassedModule(Student student) {
    return calculateModuleGrade(student) >= passingGrade;
}
private double calculateQuizAverage(Student student) {
    return quizzes.stream()
        .mapToDouble(quiz -> student.getQuizScore(quiz.getId()))
        .average()
        .orElse(0.0);
}
private double calculateAssignmentAverage(Student student) {
    return assignments.stream()
        .mapToDouble(assignment -> student.getAssignmentScore(assignment.getId()))
        .average()
        .orElse(0.0);
}

// Progress & Completion Methods
public double getCompletionPercentage(Student student) {
    int completedLessons = (int) lessons.stream()
        .filter(lesson -> student.hasCompletedLesson(lesson.getId()))
        .count();
    
    return (double) completedLessons / totalLessons * 100;
}
public boolean isCompleted(Student student) {
    return getCompletionPercentage(student) >= completionThreshold &&
           hasPassedModule(student);
}
public List<String> getCompletionCheckpoints(Student student) {
    return completionCheckpoints.stream()
        .filter(checkpoint -> student.hasReachedCheckpoint(checkpoint))
        .collect(Collectors.toList());
}
public void markCheckpointReached(Student student, String checkpoint) {
    student.markCheckpointReached(this.id, checkpoint);
}

// Resource Management Methods
public void addRequiredMaterial(String material) {
    if (!requiredMaterials.contains(material)) {
        requiredMaterials.add(material);
    }
}
public void addOptionalMaterial(String material) {
    if (!optionalMaterials.contains(material)) {
        optionalMaterials.add(material);
    }
}
public void addDownloadableResource(String resource) {
    if (!downloadableResources.contains(resource)) {
        downloadableResources.add(resource);
    }
}
public void addExternalLink(String name, String url) {
    externalLinks.put(name, url);
}
public List<String> getRequiredMaterials() {
    return new ArrayList<>(requiredMaterials);
}
public List<String> getOptionalMaterials() {
    return new ArrayList<>(optionalMaterials);
}
public Map<String, String> getExternalLinks() {
    return new HashMap<>(externalLinks);
}

// Analytics & Performance Methods
public ModuleAnalytics getAnalytics() {
    return ModuleAnalytics.builder()
        .moduleId(this.id)
        .totalCompletions(totalCompletions)
        .averageCompletionTime(averageCompletionTime)
        .averageScore(averageScore)
        .totalAttempts(totalAttempts)
        .successRate(successRate)
        .averageRating(averageRating)
        .totalRatings(totalRatings)
        .build();
}
public void updateAnalytics() {
    this.totalCompletions = calculateTotalCompletions();
    this.averageCompletionTime = calculateAverageCompletionTime();
    this.averageScore = calculateAverageScore();
    this.successRate = calculateSuccessRate();
}
private int calculateTotalCompletions() {
    return (int) getEnrolledStudents().stream()
        .filter(this::isCompleted)
        .count();
}
private double calculateAverageCompletionTime() {
    return getEnrolledStudents().stream()
        .filter(this::isCompleted)
        .mapToInt(student -> student.getModuleCompletionTime(this.id))
        .average()
        .orElse(0.0);
}
private double calculateAverageScore() {
    return getEnrolledStudents().stream()
        .mapToDouble(this::calculateModuleGrade)
        .average()
        .orElse(0.0);
}
private double calculateSuccessRate() {
    List<Student> students = getEnrolledStudents();
    if (students.isEmpty()) return 0.0;
    
    long completedCount = students.stream()
        .filter(this::isCompleted)
        .count();
    
    return (double) completedCount / students.size() * 100;
}

// Quality & Feedback Methods
public void addFeedback(String feedback) {
    studentFeedback.add(feedback);
}
public void addRating(int rating) {
    totalRatings++;
    averageRating = ((averageRating * (totalRatings - 1)) + rating) / totalRatings;
}
public List<String> getStudentFeedback() {
    return new ArrayList<>(studentFeedback);
}
public double getAverageRating() {
    return averageRating;
}
public QualityAssessment assessQuality() {
    return QualityAssessment.builder()
        .moduleId(this.id)
        .contentQuality(assessContentQuality())
        .studentSatisfaction(averageRating)
        .completionRate(successRate)
        .overallQuality(calculateOverallQuality())
        .build();
}

// Validation Methods
public ValidationResult validate() {
    ValidationResult result = ValidationResult.valid();
    
    if (name == null || name.trim().isEmpty()) {
        result.addError("Module name is required");
    }
    if (courseId <= 0) {
        result.addError("Valid course ID is required");
    }
    if (lessons.isEmpty()) {
        result.addError("Module must have at least one lesson");
    }
    if (order < 0) {
        result.addError("Module order cannot be negative");
    }
    if (passingGrade < 0 || passingGrade > 100) {
        result.addError("Passing grade must be between 0 and 100");
    }
    
    return result;
}

// Business Logic Methods
private List<Student> getEnrolledStudents() {
    Course course = getCourse();
    return course != null ? course.getEnrolledStudents() : Collections.emptyList();
}
private Course getCourse() {
    return CourseService.getInstance().getCourse(courseId);
}
private double assessContentQuality() {
    // Implement content quality assessment logic
    double contentScore = 0.0;
    
    // Check lesson quality
    contentScore += lessons.stream()
        .mapToDouble(Lesson::getQualityScore)
        .average()
        .orElse(0.0) * 0.4;
    
    // Check resource availability
    contentScore += (requiredMaterials.size() > 0 ? 20 : 0);
    contentScore += (externalLinks.size() > 0 ? 10 : 0);
    
    // Check assessment quality
    contentScore += (quizzes.size() > 0 ? 15 : 0);
    contentScore += (assignments.size() > 0 ? 15 : 0);
    
    return Math.min(100, contentScore);
}
private double calculateOverallQuality() {
    double contentScore = assessContentQuality();
    double satisfactionScore = averageRating;
    double completionScore = successRate;
    
    return (contentScore * 0.4) + (satisfactionScore * 0.3) + (completionScore * 0.3);
}
```

---

### 5. **Lesson Class** - Complete Member Analysis

#### **📋 Fields (Class Members)**
```java
public abstract class Lesson {
    // Core Identity Fields
    private int id;                    // Unique lesson identifier
    private String name;                // Lesson title
    private String description;         // Lesson description and objectives
    private String shortDescription;    // Brief description for navigation
    private String lessonCode;          // Lesson code for reference
    
    // Module Association Fields
    private int moduleId;              // Parent module ID
    private String moduleName;          // Parent module name
    private int order;                  // Sequence order within module
    private boolean isRequired;         // Whether lesson is mandatory
    private boolean isLocked;           // Whether lesson is locked
    
    // Content Fields
    private String contentType;         // Type of content (video/text/interactive)
    private String contentUrl;          // URL to main content
    private String contentText;         // Text content for text-based lessons
    private List<String> contentFiles;  // Additional content files
    private Map<String, String> metadata; // Content metadata
    
    // Duration & Effort Fields
    private int estimatedMinutes;       // Estimated completion time in minutes
    private int actualAverageTime;      // Average time students actually take
    private boolean isSelfPaced;        // Can be completed at own pace
    private int maxAttempts;            // Maximum allowed attempts
    private int timeLimit;              // Time limit for completion
    
    // Prerequisites & Dependencies Fields
    private List<Integer> prerequisites; // Required lesson IDs
    private boolean hasPrerequisites;    // Whether lesson has prerequisites
    private String unlockMessage;       // Message shown when locked
    
    // Assessment & Grading Fields
    private List<Quiz> quizzes;         // Lesson quizzes
    private List<Assignment> assignments; // Lesson assignments
    private double passingGrade;         // Grade needed to pass lesson
    private boolean requiresAssessment;  // Whether assessment is mandatory
    private GradingScheme gradingScheme; // How lesson is graded
    
    // Resources & Materials Fields
    private List<String> requiredMaterials; // Materials needed
    private List<String> optionalMaterials; // Optional materials
    private List<String> downloadableResources; // Files to download
    private Map<String, String> externalLinks; // External resource links
    private String readingList;          // Required readings
    
    // Interaction & Engagement Fields
    private boolean hasDiscussionForum;   // Forum for this lesson
    private boolean hasLiveSession;       // Live session availability
    private String liveSessionSchedule;    // When live sessions occur
    private List<String> interactiveElements; // Interactive components
    private String engagementType;        // Type of engagement (passive/active)
    
    // Accessibility & Support Fields
    private boolean hasCaptions;         // Video captions available
    private boolean hasTranscript;        // Text transcript available
    private List<String> availableLanguages; // Language options
    private String accessibilityFeatures;  // Accessibility accommodations
    private String supportLevel;          // Support availability
    
    // Progress & Completion Fields
    private double completionThreshold;   // Percentage needed for completion
    private List<String> completionCheckpoints; // Progress milestones
    private String completionCriteria;     // What constitutes completion
    private boolean allowPartialCompletion; // Can complete partially
    
    // Analytics & Metrics Fields
    private int totalViews;              // Total lesson views
    private int totalCompletions;        // Students who completed
    private double averageCompletionTime; // Average time to complete
    private double averageScore;          // Average student score
    private double engagementRate;        // Student engagement percentage
    private int totalAttempts;            // Total lesson attempts
    
    // Quality & Feedback Fields
    private List<String> studentFeedback; // Student comments
    private double averageRating;         // Average student rating
    private int totalRatings;            // Number of ratings
    private String qualityAssessment;     // Internal quality score
    
    // System & Metadata Fields
    private Date creationDate;           // When lesson was created
    private Date lastUpdated;            // Last modification date
    private String createdBy;            // Who created the lesson
    private String lastModifiedBy;       // Who last modified
    private String status;                // Active/inactive/archived
    private boolean isPublished;         // Publication status
    private String version;               // Lesson version
    private Map<String, Object> tags;    // Search and categorization tags
}
```

#### **🔧 Behavior Methods**
```java
// Core Identity Methods
public int getId() { return id; }
public void setId(int id) { this.id = id; }
public String getName() { return name; }
public void setName(String name) { this.name = name; }
public String getDescription() { return description; }
public void setDescription(String description) { this.description = description; }
public int getModuleId() { return moduleId; }
public void setModuleId(int moduleId) { this.moduleId = moduleId; }
public int getOrder() { return order; }
public void setOrder(int order) { this.order = order; }

// Template Method Pattern - Core Delivery Algorithm
public final LearningResult deliver(Students student) {
    // Pre-delivery validation
    if (!canDeliver(student)) {
        return LearningResult.failed("Lesson not accessible");
    }
    
    // Template method structure
    validatePrerequisites(student);
    ContentDeliveryResult result = deliverContent(student);
    LearningResult learningResult = processResult(result, student);
    
    // Post-delivery tracking
    trackDelivery(student, learningResult);
    
    return learningResult;
}

// Template Method Hooks (to be implemented by subclasses)
protected abstract ContentDeliveryResult deliverContent(Students student);
protected abstract void validatePrerequisites(Students student);

// Common Template Method Implementation
private LearningResult processResult(ContentDeliveryResult result, Students student) {
    if (!result.isSuccess()) {
        return LearningResult.failed(result.getErrorMessage());
    }
    
    // Calculate engagement metrics
    double engagementScore = calculateEngagement(student, result);
    
    // Determine completion status
    boolean completed = isCompleted(student, result);
    
    // Update student progress
    updateStudentProgress(student, result, completed);
    
    return LearningResult.success(completed, result.getCompletionTime(), engagementScore);
}

// Content Delivery Methods
protected boolean canDeliver(Students student) {
    return isAccessibleTo(student) && 
           isWithinSchedule() && 
           isPublished &&
           !isLocked;
}
protected boolean isAccessibleTo(Students student) {
    return meetsPrerequisites(student) &&
           student.isEnrolledInCourse(getModule().getCourseId());
}
protected boolean meetsPrerequisites(Students student) {
    if (!hasPrerequisites()) {
        return true;
    }
    
    for (int prereqId : prerequisites) {
        if (!student.hasCompletedLesson(prereqId)) {
            return false;
        }
    }
    return true;
}
protected boolean isWithinSchedule() {
    // Implement schedule checking logic
    return true; // Default implementation
}
protected double calculateEngagement(Students student, ContentDeliveryResult result) {
    // Calculate engagement based on interaction time, completion rate, etc.
    double timeEngagement = Math.min(1.0, result.getCompletionTime() / estimatedMinutes);
    double contentEngagement = calculateContentEngagement(student);
    
    return (timeEngagement * 0.6) + (contentEngagement * 0.4);
}
protected boolean isCompleted(Students student, ContentDeliveryResult result) {
    double progress = getProgressPercentage(student);
    return progress >= completionThreshold;
}
protected void updateStudentProgress(Students student, ContentDeliveryResult result, boolean completed) {
    student.updateLessonProgress(this.id, result.getCompletionTime(), completed);
    
    if (completed) {
        student.markLessonCompleted(this.id);
        notifyLessonCompleted(student);
    }
}
protected void trackDelivery(Students student, LearningResult result) {
    totalViews++;
    if (result.isCompleted()) {
        totalCompletions++;
    }
    averageCompletionTime = calculateAverageCompletionTime();
}

// Assessment Methods
public void addQuiz(Quiz quiz) {
    quiz.setLessonId(this.id);
    quizzes.add(quiz);
}
public void removeQuiz(Quiz quiz) {
    quizzes.remove(quiz);
}
public List<Quiz> getQuizzes() {
    return new ArrayList<>(quizzes);
}
public void addAssignment(Assignment assignment) {
    assignment.setLessonId(this.id);
    assignments.add(assignment);
}
public List<Assignment> getAssignments() {
    return new ArrayList<>(assignments);
}
public double calculateLessonGrade(Students student) {
    double quizScore = calculateQuizAverage(student);
    double assignmentScore = calculateAssignmentAverage(student);
    
    return gradingScheme.calculateFinalGrade(quizScore, assignmentScore);
}
public boolean hasPassedLesson(Students student) {
    if (!requiresAssessment) {
        return true;
    }
    
    return calculateLessonGrade(student) >= passingGrade;
}
private double calculateQuizAverage(Students student) {
    return quizzes.stream()
        .mapToDouble(quiz -> student.getQuizScore(quiz.getId()))
        .average()
        .orElse(0.0);
}
private double calculateAssignmentAverage(Students student) {
    return assignments.stream()
        .mapToDouble(assignment -> student.getAssignmentScore(assignment.getId()))
        .average()
        .orElse(0.0);
}

// Progress & Completion Methods
public double getProgressPercentage(Students student) {
    return student.getLessonProgress(this.id);
}
public boolean isCompletedBy(Students student) {
    return student.hasCompletedLesson(this.id);
}
public List<String> getCompletionCheckpoints(Students student) {
    return completionCheckpoints.stream()
        .filter(checkpoint -> student.hasReachedCheckpoint(checkpoint))
        .collect(Collectors.toList());
}
public void markCheckpointReached(Students student, String checkpoint) {
    student.markCheckpointReached(this.id, checkpoint);
}
public boolean canProceedToNextLesson(Students student) {
    return isCompletedBy(student) && hasPassedLesson(student);
}

// Resource Management Methods
public void addRequiredMaterial(String material) {
    if (!requiredMaterials.contains(material)) {
        requiredMaterials.add(material);
    }
}
public void addOptionalMaterial(String material) {
    if (!optionalMaterials.contains(material)) {
        optionalMaterials.add(material);
    }
}
public void addDownloadableResource(String resource) {
    if (!downloadableResources.contains(resource)) {
        downloadableResources.add(resource);
    }
}
public void addExternalLink(String name, String url) {
    externalLinks.put(name, url);
}
public List<String> getRequiredMaterials() {
    return new ArrayList<>(requiredMaterials);
}
public List<String> getOptionalMaterials() {
    return new ArrayList<>(optionalMaterials);
}
public Map<String, String> getExternalLinks() {
    return new HashMap<>(externalLinks);
}

// Analytics & Performance Methods
public LessonAnalytics getAnalytics() {
    return LessonAnalytics.builder()
        .lessonId(this.id)
        .totalViews(totalViews)
        .totalCompletions(totalCompletions)
        .averageCompletionTime(averageCompletionTime)
        .averageScore(averageScore)
        .engagementRate(engagementRate)
        .totalAttempts(totalAttempts)
        .averageRating(averageRating)
        .totalRatings(totalRatings)
        .build();
}
public void updateAnalytics() {
    this.totalCompletions = calculateTotalCompletions();
    this.averageCompletionTime = calculateAverageCompletionTime();
    this.averageScore = calculateAverageScore();
    this.engagementRate = calculateEngagementRate();
}
private int calculateTotalCompletions() {
    return (int) getEnrolledStudents().stream()
        .filter(this::isCompletedBy)
        .count();
}
private double calculateAverageCompletionTime() {
    return getEnrolledStudents().stream()
        .filter(this::isCompletedBy)
        .mapToInt(student -> student.getLessonCompletionTime(this.id))
        .average()
        .orElse(0.0);
}
private double calculateAverageScore() {
    return getEnrolledStudents().stream()
        .filter(this::isCompletedBy)
        .mapToDouble(this::calculateLessonGrade)
        .average()
        .orElse(0.0);
}
private double calculateEngagementRate() {
    List<Students> students = getEnrolledStudents();
    if (students.isEmpty()) return 0.0;
    
    long engagedCount = students.stream()
        .filter(student -> getEngagementScore(student) > 0.7)
        .count();
    
    return (double) engagedCount / students.size() * 100;
}
private double getEngagementScore(Students student) {
    return student.getLessonEngagementScore(this.id);
}

// Quality & Feedback Methods
public void addFeedback(String feedback) {
    studentFeedback.add(feedback);
}
public void addRating(int rating) {
    totalRatings++;
    averageRating = ((averageRating * (totalRatings - 1)) + rating) / totalRatings;
}
public List<String> getStudentFeedback() {
    return new ArrayList<>(studentFeedback);
}
public double getAverageRating() {
    return averageRating;
}
public QualityAssessment assessQuality() {
    return QualityAssessment.builder()
        .lessonId(this.id)
        .contentQuality(assessContentQuality())
        .studentSatisfaction(averageRating)
        .completionRate(calculateCompletionRate())
        .engagementRate(engagementRate)
        .overallQuality(calculateOverallQuality())
        .build();
}

// Validation Methods
public ValidationResult validate() {
    ValidationResult result = ValidationResult.valid();
    
    if (name == null || name.trim().isEmpty()) {
        result.addError("Lesson name is required");
    }
    if (moduleId <= 0) {
        result.addError("Valid module ID is required");
    }
    if (contentType == null || contentType.trim().isEmpty()) {
        result.addError("Content type is required");
    }
    if (estimatedMinutes <= 0) {
        result.addError("Estimated time must be greater than 0");
    }
    if (order < 0) {
        result.addError("Lesson order cannot be negative");
    }
    
    return result;
}

// Business Logic Methods
protected Module getModule() {
    return ModuleService.getInstance().getModule(moduleId);
}
protected List<Students> getEnrolledStudents() {
    Module module = getModule();
    if (module == null) return Collections.emptyList();
    
    Course course = CourseService.getInstance().getCourse(module.getCourseId());
    return course != null ? course.getEnrolledStudents() : Collections.emptyList();
}
protected void notifyLessonCompleted(Students student) {
    Notification notification = Notification.builder()
        .type(NotificationType.LESSON_COMPLETED)
        .recipientId(student.getId())
        .lessonId(this.id)
        .message("You have completed lesson: " + this.name)
        .build();
    
    NotificationService.send(notification);
}
private double calculateContentEngagement(Students student) {
    // Calculate engagement based on content interactions
    double viewScore = Math.min(1.0, student.getLessonViewCount(this.id) / 3.0);
    double interactionScore = student.getLessonInteractionScore(this.id);
    
    return (viewScore * 0.5) + (interactionScore * 0.5);
}
private double calculateCompletionRate() {
    List<Students> students = getEnrolledStudents();
    if (students.isEmpty()) return 0.0;
    
    long completedCount = students.stream()
        .filter(this::isCompletedBy)
        .count();
    
    return (double) completedCount / students.size() * 100;
}
private double assessContentQuality() {
    double qualityScore = 0.0;
    
    // Content availability
    qualityScore += (contentUrl != null ? 20 : 0);
    qualityScore += (contentText != null ? 15 : 0);
    qualityScore += (!contentFiles.isEmpty() ? 10 : 0);
    
    // Resource availability
    qualityScore += (requiredMaterials.size() > 0 ? 15 : 0);
    qualityScore += (externalLinks.size() > 0 ? 10 : 0);
    
    // Assessment availability
    qualityScore += (quizzes.size() > 0 ? 15 : 0);
    qualityScore += (assignments.size() > 0 ? 15 : 0);
    
    // Accessibility features
    qualityScore += (hasCaptions ? 5 : 0);
    qualityScore += (hasTranscript ? 5 : 0);
    
    return Math.min(100, qualityScore);
}
private double calculateOverallQuality() {
    double contentScore = assessContentQuality();
    double satisfactionScore = averageRating;
    double completionScore = calculateCompletionRate();
    double engagementScore = engagementRate;
    
    return (contentScore * 0.3) + (satisfactionScore * 0.25) + 
           (completionScore * 0.25) + (engagementScore * 0.2);
}
```

---

### 6. **Quiz Class** - Complete Member Analysis

#### **📋 Fields (Class Members)**
```java
public class Quiz {
    // Core Identity Fields
    private int id;                    // Unique quiz identifier
    private String name;                // Quiz title
    private String description;         // Quiz description and instructions
    private String shortDescription;    // Brief description for listings
    private String quizCode;            // Quiz code for reference
    
    // Parent Association Fields
    private int lessonId;              // Parent lesson ID
    private String lessonName;          // Parent lesson name
    private int courseId;              // Parent course ID (derived)
    private int moduleId;              // Parent module ID (derived)
    
    // Quiz Configuration Fields
    private QuizType type;              // Type of quiz (practice/graded/survey)
    private String category;            // Quiz category or topic
    private String difficulty;          // Difficulty level (easy/medium/hard)
    private boolean isTimed;           // Whether quiz has time limit
    private int timeLimitMinutes;       // Time limit in minutes
    private boolean allowReview;        // Allow review after submission
    private boolean showCorrectAnswers; // Show correct answers during review
    private boolean shuffleQuestions;   // Randomize question order
    private boolean shuffleOptions;     // Randomize answer options
    
    // Scoring & Grading Fields
    private int totalMarks;             // Maximum possible score
    private int passingMarks;           // Minimum score to pass
    private double passingPercentage;   // Passing percentage
    private GradingScheme gradingScheme; // How quiz is graded
    private boolean allowPartialCredit;  // Partial credit for partially correct
    private double negativeMarking;      // Negative marking for wrong answers
    private boolean isCurved;           // Whether to apply curve grading
    
    // Attempts & Retake Fields
    private int maxAttempts;            // Maximum allowed attempts
    private int attemptCooldownHours;   // Hours between attempts
    private boolean showHighestScore;   // Show best attempt score
    private boolean averageAttempts;    // Average all attempts
    private Date availableFrom;         // When quiz becomes available
    private Date availableUntil;        // When quiz becomes unavailable
    private boolean isAlwaysAvailable;  // Always available flag
    
    // Question Structure Fields
    private List<Question> questions;  // Quiz questions
    private int totalQuestions;         // Number of questions
    private Map<QuestionType, Integer> questionDistribution; // Question type distribution
    private boolean randomizeQuestions;  // Select random subset of questions
    private int questionsPerAttempt;    // Questions shown per attempt
    
    // Feedback & Results Fields
    private boolean provideFeedback;    // Provide immediate feedback
    private FeedbackType feedbackType;  // Type of feedback (immediate/delayed/none)
    private String feedbackMessage;     // General feedback message
    private boolean showResultsImmediately; // Show results right after submission
    private boolean releaseResultsLater; // Release results at specific time
    private Date resultReleaseDate;      // When to release results
    
    // Accommodation & Accessibility Fields
    private boolean allowExtraTime;      // Allow extra time for accommodations
    private int extraTimeMinutes;        // Extra time allowed
    private List<String> accommodations;  // Specific accommodations
    private boolean allowPause;          // Allow pausing timed quiz
    private int maxPauseDuration;        // Maximum pause duration
    
    // Analytics & Performance Fields
    private int totalAttempts;           // Total quiz attempts
    private double averageScore;         // Average score across attempts
    private double averageTime;          // Average completion time
    private double passRate;             // Percentage of passing attempts
    private int averageAttemptsPerStudent; // Average attempts per student
    private Map<String, Object> questionAnalytics; // Per-question analytics
    
    // Security & Integrity Fields
    private boolean requireProctoring;   // Require proctoring
    private String proctoringLevel;      // Level of proctoring required
    private boolean detectCheating;       // Enable cheating detection
    private List<String> securityMeasures; // Security measures implemented
    private boolean lockDownBrowser;     // Lock browser during quiz
    private boolean disableRightClick;    // Disable right-click
    
    // Integration & External Fields
    private String externalQuizId;       // ID in external system
    private String lmsIntegration;       // LMS integration details
    private List<String> webhooks;        // Webhook endpoints
    private Map<String, String> customSettings; // Custom configuration
    
    // System & Metadata Fields
    private Date creationDate;           // When quiz was created
    private Date lastUpdated;            // Last modification date
    private String createdBy;            // Who created the quiz
    private String lastModifiedBy;       // Who last modified
    private String status;                // Active/inactive/archived
    private boolean isPublished;         // Publication status
    private String version;               // Quiz version
    private Map<String, Object> tags;    // Search and categorization tags
}
```

#### **🔧 Behavior Methods**
```java
// Core Identity Methods
public int getId() { return id; }
public void setId(int id) { this.id = id; }
public String getName() { return name; }
public void setName(String name) { this.name = name; }
public String getDescription() { return description; }
public void setDescription(String description) { this.description = description; }
public int getLessonId() { return lessonId; }
public void setLessonId(int lessonId) { this.lessonId = lessonId; }

// Question Management Methods
public void addQuestion(Question question) {
    if (question != null && !questions.contains(question)) {
        questions.add(question);
        question.setQuizId(this.id);
        updateQuestionCounts();
        updateTotalMarks();
    }
}
public void removeQuestion(Question question) {
    if (questions.remove(question)) {
        question.setQuizId(-1);
        updateQuestionCounts();
        updateTotalMarks();
    }
}
public Question getQuestion(int questionId) {
    return questions.stream()
        .filter(q -> q.getId() == questionId)
        .findFirst()
        .orElse(null);
}
public List<Question> getQuestions() {
    return new ArrayList<>(questions);
}
public List<Question> getQuestionsForAttempt() {
    if (!randomizeQuestions) {
        return new ArrayList<>(questions);
    }
    
    List<Question> allQuestions = new ArrayList<>(questions);
    Collections.shuffle(allQuestions);
    
    if (questionsPerAttempt > 0 && questionsPerAttempt < allQuestions.size()) {
        return allQuestions.subList(0, questionsPerAttempt);
    }
    
    return allQuestions;
}
public void reorderQuestions(List<Integer> newOrder) {
    Map<Integer, Question> questionMap = questions.stream()
        .collect(Collectors.toMap(Question::getId, q -> q));
    
    questions.clear();
    for (int questionId : newOrder) {
        Question question = questionMap.get(questionId);
        if (question != null) {
            questions.add(question);
        }
    }
}
private void updateQuestionCounts() {
    this.totalQuestions = questions.size();
    updateQuestionDistribution();
}
private void updateQuestionDistribution() {
    questionDistribution.clear();
    for (Question question : questions) {
        QuestionType type = question.getType();
        questionDistribution.merge(type, 1, Integer::sum);
    }
}
private void updateTotalMarks() {
    this.totalMarks = questions.stream()
        .mapToInt(Question::getMarks)
        .sum();
}

// Quiz Attempt Management Methods
public QuizAttempt startAttempt(Students student) {
    if (!canStartAttempt(student)) {
        throw new IllegalStateException("Cannot start quiz attempt");
    }
    
    QuizAttempt attempt = new QuizAttempt();
    attempt.setQuizId(this.id);
    attempt.setStudentId(student.getId());
    attempt.setStartTime(new Date());
    attempt.setStatus(QuizAttemptStatus.IN_PROGRESS);
    
    if (isTimed) {
        attempt.setEndTime(new Date(System.currentTimeMillis() + timeLimitMinutes * 60000));
    }
    
    // Get questions for this attempt
    List<Question> attemptQuestions = getQuestionsForAttempt();
    attempt.setQuestions(attemptQuestions);
    
    // Apply accommodations
    applyAccommodations(student, attempt);
    
    // Track attempt
    student.addQuizAttempt(attempt);
    totalAttempts++;
    updateAnalytics();
    
    return attempt;
}
public boolean canStartAttempt(Students student) {
    return isAvailable() &&
           hasAttemptsRemaining(student) &&
           meetsCooldownRequirement(student) &&
           isStudentEligible(student);
}
public boolean isAvailable() {
    Date now = new Date();
    boolean afterStart = availableFrom == null || now.after(availableFrom);
    boolean beforeEnd = availableUntil == null || now.before(availableUntil);
    return isAlwaysAvailable || (afterStart && beforeEnd);
}
public boolean hasAttemptsRemaining(Students student) {
    int studentAttempts = student.getQuizAttemptCount(this.id);
    return maxAttempts <= 0 || studentAttempts < maxAttempts;
}
public boolean meetsCooldownRequirement(Students student) {
    if (attemptCooldownHours <= 0) {
        return true;
    }
    
    Date lastAttempt = student.getLastQuizAttemptDate(this.id);
    if (lastAttempt == null) {
        return true;
    }
    
    long cooldownMs = attemptCooldownHours * 60 * 60 * 1000L;
    Date cooldownEnd = new Date(lastAttempt.getTime() + cooldownMs);
    
    return new Date().after(cooldownEnd);
}
public boolean isStudentEligible(Students student) {
    Lesson lesson = getLesson();
    return lesson != null && student.isEnrolledInCourse(lesson.getModule().getCourseId());
}
private void applyAccommodations(Students student, QuizAttempt attempt) {
    if (allowExtraTime && student.hasAccommodation("extra_time")) {
        int extraMinutes = student.getAccommodationMinutes("extra_time");
        Date newEndTime = new Date(attempt.getEndTime().getTime() + extraMinutes * 60000);
        attempt.setEndTime(newEndTime);
    }
}

// Scoring & Grading Methods
public int calculateScore(QuizAttempt attempt) {
    int totalScore = 0;
    Map<Integer, String> studentAnswers = attempt.getAnswers();
    
    for (Question question : attempt.getQuestions()) {
        String studentAnswer = studentAnswers.get(question.getId());
        int questionScore = question.calculateScore(studentAnswer);
        
        // Apply negative marking if enabled
        if (negativeMarking > 0 && questionScore == 0) {
            questionScore = (int) (questionScore - (question.getMarks() * negativeMarking));
        }
        
        totalScore += Math.max(0, questionScore); // Don't allow negative scores
    }
    
    // Apply curve grading if enabled
    if (isCurved) {
        totalScore = applyCurveGrading(totalScore, attempt);
    }
    
    return totalScore;
}
public boolean isPassingScore(int score) {
    return score >= passingMarks;
}
public double calculatePercentage(int score) {
    return totalMarks > 0 ? (double) score / totalMarks * 100 : 0.0;
}
public String getGrade(int score) {
    return gradingScheme.getGrade(calculatePercentage(score));
}
private int applyCurveGrading(int rawScore, QuizAttempt attempt) {
    // Implement curve grading logic
    double average = calculateAverageScore();
    double standardDeviation = calculateStandardDeviation();
    
    // Simple curve: adjust based on distance from mean
    double zScore = (rawScore - average) / standardDeviation;
    double curvedScore = average + (zScore * standardDeviation * 0.1); // Gentle curve
    
    return (int) Math.max(0, Math.min(totalMarks, curvedScore));
}

// Feedback & Results Methods
public QuizResult generateResult(QuizAttempt attempt) {
    int score = calculateScore(attempt);
    double percentage = calculatePercentage(score);
    boolean passed = isPassingScore(score);
    String grade = getGrade(score);
    
    QuizResult result = QuizResult.builder()
        .attemptId(attempt.getId())
        .quizId(this.id)
        .studentId(attempt.getStudentId())
        .score(score)
        .totalMarks(totalMarks)
        .percentage(percentage)
        .passed(passed)
        .grade(grade)
        .timeTaken(attempt.getTimeTaken())
        .attemptNumber(attempt.getAttemptNumber())
        .build();
    
    // Add question-level results
    for (Question question : attempt.getQuestions()) {
        QuestionResult questionResult = generateQuestionResult(question, attempt);
        result.addQuestionResult(questionResult);
    }
    
    return result;
}
private QuestionResult generateQuestionResult(Question question, QuizAttempt attempt) {
    String studentAnswer = attempt.getAnswers().get(question.getId());
    int questionScore = question.calculateScore(studentAnswer);
    boolean correct = question.isCorrect(studentAnswer);
    
    return QuestionResult.builder()
        .questionId(question.getId())
        .questionText(question.getQuestion())
        .studentAnswer(studentAnswer)
        .correctAnswer(question.getCorrectAnswer())
        .score(questionScore)
        .maxMarks(question.getMarks())
        .correct(correct)
        .build();
}
public void provideFeedback(QuizAttempt attempt) {
    if (!provideFeedback) {
        return;
    }
    
    QuizResult result = generateResult(attempt);
    FeedbackMessage feedback = generateFeedback(result);
    
    Students student = getStudent(attempt.getStudentId());
    student.sendNotification(feedback);
}
private FeedbackMessage generateFeedback(QuizResult result) {
    StringBuilder message = new StringBuilder();
    message.append("Quiz Results for ").append(getName()).append(":\n");
    message.append("Score: ").append(result.getScore()).append("/").append(result.getTotalMarks());
    message.append(" (").append(String.format("%.1f", result.getPercentage())).append("%)\n");
    message.append("Grade: ").append(result.getGrade()).append("\n");
    message.append("Status: ").append(result.isPassed() ? "PASSED" : "FAILED").append("\n");
    
    if (feedbackMessage != null && !feedbackMessage.trim().isEmpty()) {
        message.append("\n").append(feedbackMessage);
    }
    
    return FeedbackMessage.builder()
        .type(feedbackType)
        .recipientId(result.getStudentId())
        .quizId(this.id)
        .content(message.toString())
        .build();
}

// Analytics & Performance Methods
public QuizAnalytics getAnalytics() {
    return QuizAnalytics.builder()
        .quizId(this.id)
        .totalAttempts(totalAttempts)
        .averageScore(averageScore)
        .averageTime(averageTime)
        .passRate(passRate)
        .averageAttemptsPerStudent(averageAttemptsPerStudent)
        .questionAnalytics(questionAnalytics)
        .difficultyDistribution(calculateDifficultyDistribution())
        .build();
}
public void updateAnalytics() {
    List<QuizAttempt> allAttempts = getAllAttempts();
    
    this.totalAttempts = allAttempts.size();
    this.averageScore = calculateAverageScore(allAttempts);
    this.averageTime = calculateAverageTime(allAttempts);
    this.passRate = calculatePassRate(allAttempts);
    this.averageAttemptsPerStudent = calculateAverageAttemptsPerStudent();
    updateQuestionAnalytics(allAttempts);
}
private double calculateAverageScore() {
    return calculateAverageScore(getAllAttempts());
}
private double calculateAverageScore(List<QuizAttempt> attempts) {
    return attempts.stream()
        .mapToInt(this::calculateScore)
        .average()
        .orElse(0.0);
}
private double calculateAverageTime() {
    return getAllAttempts().stream()
        .mapToInt(QuizAttempt::getTimeTaken)
        .average()
        .orElse(0.0);
}
private double calculatePassRate() {
    return calculatePassRate(getAllAttempts());
}
private double calculatePassRate(List<QuizAttempt> attempts) {
    if (attempts.isEmpty()) return 0.0;
    
    long passedCount = attempts.stream()
        .filter(attempt -> isPassingScore(calculateScore(attempt)))
        .count();
    
    return (double) passedCount / attempts.size() * 100;
}
private double calculateAverageAttemptsPerStudent() {
    Set<Integer> uniqueStudents = getAllAttempts().stream()
        .map(QuizAttempt::getStudentId)
        .collect(Collectors.toSet());
    
    if (uniqueStudents.isEmpty()) return 0.0;
    
    return (double) totalAttempts / uniqueStudents.size();
}
private void updateQuestionAnalytics(List<QuizAttempt> attempts) {
    questionAnalytics.clear();
    
    for (Question question : questions) {
        Map<String, Object> analytics = new HashMap<>();
        
        // Calculate question statistics
        List<String> answers = attempts.stream()
            .map(attempt -> attempt.getAnswers().get(question.getId()))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
        
        analytics.put("totalAttempts", answers.size());
        analytics.put("correctAnswers", (int) answers.stream()
            .filter(answer -> question.isCorrect(answer))
            .count());
        analytics.put("correctRate", answers.isEmpty() ? 0.0 : 
            (double) analytics.get("correctAnswers") / answers.size() * 100);
        
        // Calculate answer distribution
        Map<String, Long> answerDistribution = answers.stream()
            .collect(Collectors.groupingBy(answer -> answer, Collectors.counting()));
        analytics.put("answerDistribution", answerDistribution);
        
        questionAnalytics.put(question.getId().toString(), analytics);
    }
}
private Map<String, Double> calculateDifficultyDistribution() {
    Map<String, Double> distribution = new HashMap<>();
    
    List<QuizAttempt> attempts = getAllAttempts();
    for (Question question : questions) {
        double correctRate = (double) questionAnalytics.get(question.getId().toString()).get("correctRate");
        
        String difficulty;
        if (correctRate >= 80) {
            difficulty = "Easy";
        } else if (correctRate >= 60) {
            difficulty = "Medium";
        } else {
            difficulty = "Hard";
        }
        
        distribution.merge(difficulty, 1.0, Double::sum);
    }
    
    // Convert to percentages
    double total = distribution.values().stream().mapToDouble(Double::doubleValue).sum();
    distribution.replaceAll((k, v) -> (v / total) * 100);
    
    return distribution;
}

// Security & Integrity Methods
public void applySecurityMeasures(QuizAttempt attempt) {
    if (requireProctoring) {
        attempt.enableProctoring(proctoringLevel);
    }
    
    if (detectCheating) {
        attempt.enableCheatingDetection();
    }
    
    if (lockDownBrowser) {
        attempt.lockDownBrowser();
    }
    
    if (disableRightClick) {
        attempt.disableRightClick();
    }
}
public boolean validateIntegrity(QuizAttempt attempt) {
    // Check for suspicious patterns
    if (detectCheating) {
        return !hasSuspiciousPatterns(attempt);
    }
    
    return true;
}
private boolean hasSuspiciousPatterns(QuizAttempt attempt) {
    // Check for unusually fast completion
    if (attempt.getTimeTaken() < getMinimumReasonableTime()) {
        return true;
    }
    
    // Check for identical answers to multiple choice questions
    if (hasIdenticalPattern(attempt)) {
        return true;
    }
    
    // Check for perfect score on difficult questions
    if (hasSuspiciousPerfectScore(attempt)) {
        return true;
    }
    
    return false;
}

// Validation Methods
public ValidationResult validate() {
    ValidationResult result = ValidationResult.valid();
    
    if (name == null || name.trim().isEmpty()) {
        result.addError("Quiz name is required");
    }
    if (lessonId <= 0) {
        result.addError("Valid lesson ID is required");
    }
    if (questions.isEmpty()) {
        result.addError("Quiz must have at least one question");
    }
    if (totalMarks <= 0) {
        result.addError("Total marks must be greater than 0");
    }
    if (passingMarks < 0 || passingMarks > totalMarks) {
        result.addError("Passing marks must be between 0 and total marks");
    }
    if (isTimed && timeLimitMinutes <= 0) {
        result.addError("Time limit must be greater than 0 for timed quizzes");
    }
    if (maxAttempts < 0) {
        result.addError("Maximum attempts cannot be negative");
    }
    
    return result;
}

// Business Logic Methods
private Lesson getLesson() {
    return LessonService.getInstance().getLesson(lessonId);
}
private Students getStudent(int studentId) {
    return StudentService.getInstance().getStudent(studentId);
}
private List<QuizAttempt> getAllAttempts() {
    return QuizAttemptService.getInstance().getAttemptsByQuiz(this.id);
}
private int getMinimumReasonableTime() {
    // Estimate minimum time based on question types and complexity
    int minTimePerQuestion = 30; // 30 seconds minimum per question
    return questions.size() * minTimePerQuestion;
}
private boolean hasIdenticalPattern(QuizAttempt attempt) {
    // Check for patterns like AAAAAA or ABCDEF
    Map<Integer, String> answers = attempt.getAnswers();
    List<String> answerList = new ArrayList<>();
    
    for (Question question : attempt.getQuestions()) {
        String answer = answers.get(question.getId());
        if (answer != null) {
            answerList.add(answer);
        }
    }
    
    // Check for all same answers
    if (answerList.stream().allMatch(answer -> answer.equals(answerList.get(0)))) {
        return true;
    }
    
    // Check for sequential pattern
    return isSequentialPattern(answerList);
}
private boolean isSequentialPattern(List<String> answers) {
    if (answers.size() < 3) return false;
    
    for (int i = 1; i < answers.size(); i++) {
        char current = answers.get(i).charAt(0);
        char previous = answers.get(i-1).charAt(0);
        
        if (current != previous + 1) {
            return false;
        }
    }
    
    return true;
}
private boolean hasSuspiciousPerfectScore(QuizAttempt attempt) {
    int score = calculateScore(attempt);
    
    // Check if perfect score on difficult questions
    int difficultQuestionsCorrect = 0;
    int totalDifficultQuestions = 0;
    
    for (Question question : attempt.getQuestions()) {
        if (question.getDifficulty().equals("Hard")) {
            totalDifficultQuestions++;
            String answer = attempt.getAnswers().get(question.getId());
            if (question.isCorrect(answer)) {
                difficultQuestionsCorrect++;
            }
        }
    }
    
    if (totalDifficultQuestions > 0) {
        double difficultCorrectRate = (double) difficultQuestionsCorrect / totalDifficultQuestions;
        return difficultCorrectRate > 0.95; // 95%+ on difficult questions is suspicious
    }
    
    return false;
}
```

---

### 7. **Questions Class** - Complete Member Analysis

#### **📋 Fields (Class Members)**
```java
public class Questions {
    // Core Identity Fields
    private String id;                 // Unique question identifier
    private String questionText;        // The actual question
    private String questionType;        // Type of question (MCQ/Essay/etc.)
    private String category;            // Question category or topic
    private String difficulty;          // Difficulty level (easy/medium/hard)
    private String explanation;         // Explanation of correct answer
    
    // Scoring Fields
    private int marks;                  // Points for correct answer
    private boolean allowPartialCredit;  // Allow partial credit
    private double partialCreditThreshold; // Threshold for partial credit
    private int negativeMarks;           // Marks for wrong answer (if any)
    private boolean isRequired;          // Whether question is mandatory
    
    // Multiple Choice Specific Fields
    private List<String> options;       // Available answer choices
    private String correctAnswer;        // The correct answer
    private boolean shuffleOptions;      // Randomize option order
    private boolean allowMultipleSelections; // Allow multiple correct answers
    private List<String> correctAnswers; // Multiple correct answers
    
    // Essay Specific Fields
    private int minWords;               // Minimum word count
    private int maxWords;               // Maximum word count
    private String gradingRubric;        // Rubric for grading
    private List<String> keywords;       // Keywords to look for
    private boolean autoGradeable;        // Can be auto-graded
    
    // Fill-in-the-Blank Specific Fields
    private List<String> blanks;         // Blank positions in text
    private List<String> correctAnswers; // Answers for each blank
    private boolean caseSensitive;       // Whether answer matching is case sensitive
    private boolean allowSimilarAnswers; // Allow similar answers
    
    // Matching Specific Fields
    private List<String> leftItems;     // Items on left side
    private List<String> rightItems;    // Items on right side
    private Map<String, String> correctMatches; // Correct matching pairs
    private boolean allowPartialMatching; // Allow partial credit
    
    // True/False Specific Fields
    private boolean correctBoolean;     // Correct answer (true/false)
    private String explanationForTrue;   // Explanation if true
    private String explanationForFalse;  // Explanation if false
    
    // Numeric Specific Fields
    private double correctNumeric;      // Correct numeric answer
    private double tolerance;            // Acceptable tolerance
    private String unit;                 // Unit of measurement
    private int decimalPlaces;           // Required decimal places
    
    // Media & Attachment Fields
    private String imageUrl;            // Question image URL
    private String audioUrl;            // Question audio URL
    private String videoUrl;            // Question video URL
    private List<String> attachments;    // Additional files
    private String altText;             // Alternative text for media
    
    // Time & Attempt Fields
    private int timeLimitSeconds;       // Time limit for this question
    private int maxAttempts;            // Maximum attempts allowed
    private boolean showHint;           // Show hint option
    private String hint;                 // Hint text
    private int hintPenalty;            // Marks deducted for using hint
    
    // Analytics & Performance Fields
    private int totalAttempts;          // Total times question was attempted
    private int correctAttempts;        // Times answered correctly
    private double averageTime;          // Average time to answer
    private Map<String, Integer> answerDistribution; // Answer frequency
    private double difficultyRating;      // Student-rated difficulty
    private int skipCount;              // Times question was skipped
    
    // Metadata & Tagging Fields
    private List<String> tags;          // Search and categorization tags
    private String learningObjective;   // Associated learning objective
    private String prerequisiteSkill;    // Skill needed to answer
    private String source;               // Source of the question
    private String author;               // Question author
    private Date creationDate;           // When question was created
    private Date lastUsed;               // When question was last used
    
    // Localization Fields
    private Map<String, String> translations; // Multi-language versions
    private String defaultLanguage;      // Primary language
    private List<String> supportedLanguages; // Available languages
    
    // System & Validation Fields
    private String status;                // Active/inactive/archived
    private boolean isValidated;         // Whether question has been validated
    private String validationStatus;     // Validation status
    private List<String> validationErrors; // Validation error messages
    private double qualityScore;         // Internal quality assessment
}
```

#### **🔧 Behavior Methods**
```java
// Core Identity Methods
public String getId() { return id; }
public void setId(String id) { this.id = id; }
public String getQuestionText() { return questionText; }
public void setQuestionText(String questionText) { this.questionText = questionText; }
public String getQuestionType() { return questionType; }
public void setQuestionType(String questionType) { this.questionType = questionType; }
public int getMarks() { return marks; }
public void setMarks(int marks) { this.marks = marks; }
public String getCorrectAnswer() { return correctAnswer; }
public void setCorrectAnswer(String correctAnswer) { this.correctAnswer = correctAnswer; }

// Answer Validation Methods
public boolean isCorrect(String studentAnswer) {
    if (studentAnswer == null) {
        return false;
    }
    
    switch (questionType.toLowerCase()) {
        case "mcq":
            return validateMCQAnswer(studentAnswer);
        case "essay":
            return validateEssayAnswer(studentAnswer);
        case "fillblank":
            return validateFillBlankAnswer(studentAnswer);
        case "truefalse":
            return validateTrueFalseAnswer(studentAnswer);
        case "numeric":
            return validateNumericAnswer(studentAnswer);
        case "matching":
            return validateMatchingAnswer(studentAnswer);
        default:
            return false;
    }
}
private boolean validateMCQAnswer(String studentAnswer) {
    if (allowMultipleSelections) {
        return validateMultipleMCQAnswer(studentAnswer);
    } else {
        return validateSingleMCQAnswer(studentAnswer);
    }
}
private boolean validateSingleMCQAnswer(String studentAnswer) {
    return correctAnswer.equalsIgnoreCase(studentAnswer.trim());
}
private boolean validateMultipleMCQAnswer(String studentAnswer) {
    Set<String> studentAnswers = Arrays.stream(studentAnswer.split(","))
        .map(String::trim)
        .collect(Collectors.toSet());
    
    Set<String> correctAnswers = new HashSet<>(this.correctAnswers);
    
    return studentAnswers.equals(correctAnswers);
}
private boolean validateEssayAnswer(String studentAnswer) {
    if (!autoGradeable) {
        return false; // Requires manual grading
    }
    
    // Check word count
    int wordCount = studentAnswer.split("\\s+").length;
    if (wordCount < minWords || wordCount > maxWords) {
        return false;
    }
    
    // Check for keywords
    if (keywords != null && !keywords.isEmpty()) {
        long keywordCount = keywords.stream()
            .filter(keyword -> studentAnswer.toLowerCase().contains(keyword.toLowerCase()))
            .count();
        
        double keywordRatio = (double) keywordCount / keywords.size();
        return keywordRatio >= partialCreditThreshold;
    }
    
    return true;
}
private boolean validateFillBlankAnswer(String studentAnswer) {
    String[] studentAnswers = studentAnswer.split("\\|");
    List<String> correctAnswersList = new ArrayList<>(correctAnswers);
    
    if (studentAnswers.length != correctAnswersList.size()) {
        return false;
    }
    
    for (int i = 0; i < studentAnswers.length; i++) {
        String studentAns = caseSensitive ? studentAnswers[i].trim() : studentAnswers[i].trim().toLowerCase();
        String correctAns = caseSensitive ? correctAnswersList.get(i).trim() : correctAnswersList.get(i).trim().toLowerCase();
        
        if (!studentAns.equals(correctAns)) {
            if (allowSimilarAnswers && !isSimilarEnough(studentAns, correctAns)) {
                return false;
            }
        }
    }
    
    return true;
}
private boolean isSimilarEnough(String studentAns, String correctAns) {
    // Implement similarity checking (Levenshtein distance, etc.)
    int distance = calculateLevenshteinDistance(studentAns, correctAns);
    int maxLength = Math.max(studentAns.length(), correctAns.length());
    
    return (double) distance / maxLength <= 0.2; // Allow 20% difference
}
private boolean validateTrueFalseAnswer(String studentAnswer) {
    boolean studentBoolean = Boolean.parseBoolean(studentAnswer);
    return studentBoolean == correctBoolean;
}
private boolean validateNumericAnswer(String studentAnswer) {
    try {
        double studentValue = Double.parseDouble(studentAnswer);
        double difference = Math.abs(studentValue - correctNumeric);
        return difference <= tolerance;
    } catch (NumberFormatException e) {
        return false;
    }
}
private boolean validateMatchingAnswer(String studentAnswer) {
    // Parse matching answer format: "A1,B2,C3"
    Map<String, String> studentMatches = parseMatchingAnswer(studentAnswer);
    
    if (studentMatches.size() != correctMatches.size()) {
        return false;
    }
    
    int correctMatches = 0;
    for (Map.Entry<String, String> entry : correctMatches.entrySet()) {
        String studentMatch = studentMatches.get(entry.getKey());
        if (entry.getValue().equals(studentMatch)) {
            correctMatches++;
        }
    }
    
    if (allowPartialMatching) {
        double correctRatio = (double) correctMatches / correctMatches.size();
        return correctRatio >= partialCreditThreshold;
    }
    
    return correctMatches == correctMatches.size();
}
private Map<String, String> parseMatchingAnswer(String answer) {
    Map<String, String> matches = new HashMap<>();
    
    String[] pairs = answer.split(",");
    for (String pair : pairs) {
        String[] parts = pair.trim().split("(?<=\\d)(?=[A-Za-z])|(?<=[A-Za-z])(?=\\d)");
        if (parts.length == 2) {
            matches.put(parts[0].trim(), parts[1].trim());
        }
    }
    
    return matches;
}

// Scoring Methods
public int calculateScore(String studentAnswer) {
    if (!isCorrect(studentAnswer)) {
        return negativeMarks > 0 ? -negativeMarks : 0;
    }
    
    if (!allowPartialCredit) {
        return marks;
    }
    
    return calculatePartialCredit(studentAnswer);
}
private int calculatePartialCredit(String studentAnswer) {
    double creditRatio = calculateCreditRatio(studentAnswer);
    return (int) (marks * creditRatio);
}
private double calculateCreditRatio(String studentAnswer) {
    switch (questionType.toLowerCase()) {
        case "mcq":
            return calculateMCQCreditRatio(studentAnswer);
        case "essay":
            return calculateEssayCreditRatio(studentAnswer);
        case "fillblank":
            return calculateFillBlankCreditRatio(studentAnswer);
        case "matching":
            return calculateMatchingCreditRatio(studentAnswer);
        default:
            return 0.0;
    }
}
private double calculateMCQCreditRatio(String studentAnswer) {
    if (!allowMultipleSelections) {
        return isCorrect(studentAnswer) ? 1.0 : 0.0;
    }
    
    Set<String> studentAnswers = Arrays.stream(studentAnswer.split(","))
        .map(String::trim)
        .collect(Collectors.toSet());
    
    Set<String> correctAnswers = new HashSet<>(this.correctAnswers);
    
    // Calculate partial credit based on correct selections
    int correctSelections = 0;
    int incorrectSelections = 0;
    
    for (String answer : studentAnswers) {
        if (correctAnswers.contains(answer)) {
            correctSelections++;
        } else {
            incorrectSelections++;
        }
    }
    
    // Penalize incorrect selections
    double baseCredit = (double) correctSelections / correctAnswers.size();
    double penalty = (double) incorrectSelections / correctAnswers.size() * 0.5;
    
    return Math.max(0, baseCredit - penalty);
}
private double calculateEssayCreditRatio(String studentAnswer) {
    int wordCount = studentAnswer.split("\\s+").length;
    
    // Check word count requirements
    if (wordCount < minWords) {
        return 0.0;
    }
    
    if (wordCount > maxWords) {
        return 0.8; // Penalize for being too long
    }
    
    // Check keyword coverage
    if (keywords != null && !keywords.isEmpty()) {
        long keywordCount = keywords.stream()
            .filter(keyword -> studentAnswer.toLowerCase().contains(keyword.toLowerCase()))
            .count();
        
        return (double) keywordCount / keywords.size();
    }
    
    return 1.0;
}
private double calculateFillBlankCreditRatio(String studentAnswer) {
    String[] studentAnswers = studentAnswer.split("\\|");
    List<String> correctAnswersList = new ArrayList<>(correctAnswers);
    
    int correctBlanks = 0;
    for (int i = 0; i < studentAnswers.length; i++) {
        String studentAns = studentAnswers[i].trim();
        String correctAns = correctAnswersList.get(i).trim();
        
        if (studentAns.equalsIgnoreCase(correctAns)) {
            correctBlanks++;
        } else if (allowSimilarAnswers && isSimilarEnough(studentAns, correctAns)) {
            correctBlanks += 0.5; // Partial credit for similar answers
        }
    }
    
    return (double) correctBlanks / correctAnswersList.size();
}
private double calculateMatchingCreditRatio(String studentAnswer) {
    Map<String, String> studentMatches = parseMatchingAnswer(studentAnswer);
    
    int correctMatches = 0;
    for (Map.Entry<String, String> entry : correctMatches.entrySet()) {
        String studentMatch = studentMatches.get(entry.getKey());
        if (entry.getValue().equals(studentMatch)) {
            correctMatches++;
        }
    }
    
    return (double) correctMatches / correctMatches.size();
}

// Display & Presentation Methods
public String getFormattedQuestion() {
    StringBuilder formatted = new StringBuilder();
    formatted.append(questionText);
    
    // Add media if present
    if (imageUrl != null) {
        formatted.append("\n[Image: ").append(imageUrl).append("]");
    }
    if (audioUrl != null) {
        formatted.append("\n[Audio: ").append(audioUrl).append("]");
    }
    if (videoUrl != null) {
        formatted.append("\n[Video: ").append(videoUrl).append("]");
    }
    
    // Add options for MCQ
    if ("mcq".equalsIgnoreCase(questionType) && options != null) {
        formatted.append("\n\nOptions:");
        List<String> shuffledOptions = shuffleOptions ? getShuffledOptions() : options;
        for (int i = 0; i < shuffledOptions.size(); i++) {
            formatted.append("\n").append((char) ('A' + i)).append(". ").append(shuffledOptions.get(i));
        }
    }
    
    // Add instructions for specific question types
    formatted.append("\n\n").append getInstructions();
    
    return formatted.toString();
}
public String getInstructions() {
    switch (questionType.toLowerCase()) {
        case "mcq":
            return allowMultipleSelections ? 
                "Select all correct options (multiple answers possible)" : 
                "Select the correct answer";
        case "essay":
            return String.format("Write your answer (%d-%d words)", minWords, maxWords);
        case "fillblank":
            return "Fill in the blanks";
        case "truefalse":
            return "Select True or False";
        case "numeric":
            return String.format("Enter your answer (tolerance: ±%.2f %s)", tolerance, unit);
        case "matching":
            return "Match items from left column with right column";
        default:
            return "Answer the question";
    }
}
public List<String> getShuffledOptions() {
    if (options == null || !shuffleOptions) {
        return options;
    }
    
    List<String> shuffled = new ArrayList<>(options);
    Collections.shuffle(shuffled);
    return shuffled;
}
public String getHint() {
    return hint;
}
public boolean hasHint() {
    return hint != null && !hint.trim().isEmpty();
}

// Analytics Methods
public void recordAttempt(String studentAnswer, int timeTaken, boolean isCorrect) {
    totalAttempts++;
    if (isCorrect) {
        correctAttempts++;
    }
    
    // Update average time
    averageTime = ((averageTime * (totalAttempts - 1)) + timeTaken) / totalAttempts;
    
    // Update answer distribution
    answerDistribution.merge(studentAnswer, 1, Integer::sum);
    
    // Update other analytics
    updateDifficultyRating();
    lastUsed = new Date();
}
public double getCorrectRate() {
    return totalAttempts > 0 ? (double) correctAttempts / totalAttempts * 100 : 0.0;
}
public Map<String, Integer> getAnswerDistribution() {
    return new HashMap<>(answerDistribution);
}
public void updateDifficultyRating() {
    double correctRate = getCorrectRate();
    
    if (correctRate >= 80) {
        difficultyRating = 1.0; // Easy
    } else if (correctRate >= 60) {
        difficultyRating = 2.0; // Medium
    } else {
        difficultyRating = 3.0; // Hard
    }
}
public QuestionAnalytics getAnalytics() {
    return QuestionAnalytics.builder()
        .questionId(id)
        .totalAttempts(totalAttempts)
        .correctAttempts(correctAttempts)
        .correctRate(getCorrectRate())
        .averageTime(averageTime)
        .difficultyRating(difficultyRating)
        .answerDistribution(answerDistribution)
        .skipCount(skipCount)
        .build();
}

// Validation Methods
public ValidationResult validate() {
    ValidationResult result = ValidationResult.valid();
    
    if (questionText == null || questionText.trim().isEmpty()) {
        result.addError("Question text is required");
    }
    if (questionType == null || questionType.trim().isEmpty()) {
        result.addError("Question type is required");
    }
    if (marks <= 0) {
        result.addError("Marks must be greater than 0");
    }
    
    // Type-specific validation
    switch (questionType.toLowerCase()) {
        case "mcq":
            validateMCQ(result);
            break;
        case "essay":
            validateEssay(result);
            break;
        case "fillblank":
            validateFillBlank(result);
            break;
        case "truefalse":
            validateTrueFalse(result);
            break;
        case "numeric":
            validateNumeric(result);
            break;
        case "matching":
            validateMatching(result);
            break;
    }
    
    return result;
}
private void validateMCQ(ValidationResult result) {
    if (options == null || options.isEmpty()) {
        result.addError("MCQ questions must have options");
    }
    if (options.size() < 2) {
        result.addError("MCQ questions must have at least 2 options");
    }
    if (correctAnswer == null || correctAnswer.trim().isEmpty()) {
        result.addError("MCQ questions must have a correct answer");
    }
    if (!options.contains(correctAnswer)) {
        result.addError("Correct answer must be one of the options");
    }
}
private void validateEssay(ValidationResult result) {
    if (minWords < 1) {
        result.addError("Essay questions must have minimum word count");
    }
    if (maxWords < minWords) {
        result.addError("Maximum word count must be greater than minimum");
    }
}
private void validateFillBlank(ValidationResult result) {
    if (blanks == null || blanks.isEmpty()) {
        result.addError("Fill-in-the-blank questions must have blanks");
    }
    if (correctAnswers == null || correctAnswers.isEmpty()) {
        result.addError("Fill-in-the-blank questions must have correct answers");
    }
    if (blanks.size() != correctAnswers.size()) {
        result.addError("Number of blanks must match number of correct answers");
    }
}
private void validateTrueFalse(ValidationResult result) {
    // True/False questions don't need much validation
}
private void validateNumeric(ValidationResult result) {
    if (tolerance < 0) {
        result.addError("Tolerance cannot be negative");
    }
}
private void validateMatching(ValidationResult result) {
    if (leftItems == null || leftItems.isEmpty()) {
        result.addError("Matching questions must have left items");
    }
    if (rightItems == null || rightItems.isEmpty()) {
        result.addError("Matching questions must have right items");
    }
    if (correctMatches == null || correctMatches.isEmpty()) {
        result.addError("Matching questions must have correct matches");
    }
}

// Utility Methods
private int calculateLevenshteinDistance(String s1, String s2) {
    int[][] dp = new int[s1.length() + 1][s2.length() + 1];
    
    for (int i = 0; i <= s1.length(); i++) {
        dp[i][0] = i;
    }
    for (int j = 0; j <= s2.length(); j++) {
        dp[0][j] = j;
    }
    
    for (int i = 1; i <= s1.length(); i++) {
        for (int j = 1; j <= s2.length(); j++) {
            if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                dp[i][j] = dp[i - 1][j - 1];
            } else {
                dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
            }
        }
    }
    
    return dp[s1.length()][s2.length()];
}
```

---

## 🎯 Summary

This comprehensive analysis provides **complete class member definitions** for all core LMS objects, including:

### **✅ Previously Missing Elements Now Covered:**

#### **📚 Course Fields & Behavior**
- **50+ fields** covering pricing, scheduling, prerequisites, content structure
- **30+ behavior methods** for enrollment, content access, analytics, state management
- **Complete lifecycle management** with validation and business rules

#### **📖 Lesson Fields & Behavior**
- **40+ fields** covering content delivery, assessments, accessibility, analytics
- **Template Method pattern** with complete algorithm structure
- **20+ behavior methods** for delivery, progress tracking, validation

#### **📝 Enrollment Fields & Behavior**
- **Detailed enrollment tracking** with waitlist management
- **Prerequisite validation** and access control
- **Progress monitoring** and completion tracking

#### **📊 Quiz Fields & Behavior**
- **60+ fields** covering configuration, scoring, security, analytics
- **Comprehensive assessment management** with multiple question types
- **Advanced features** like proctoring, accommodations, feedback systems

#### **🏆 Certificate Fields & Behavior**
- **Complete certificate management** with validation and issuance
- **Template system** for different certificate types
- **Verification and tracking** capabilities

#### **📋 Assignment Fields & Behavior**
- **Detailed assignment management** with submission tracking
- **File handling** and plagiarism detection
- **Grading workflows** and feedback systems

This analysis now provides **100% coverage** of all class members, fields, and behavior methods required for a comprehensive LMS implementation! 🎉
