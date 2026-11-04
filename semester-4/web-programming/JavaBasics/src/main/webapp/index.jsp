<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Grade Management System</title>
</head>
<body>

<%
    // Read the logged-in user's role from the session, if any.
    // session is an implicit object JSP gives us automatically — no import needed.
    String role = (session != null) ? (String) session.getAttribute("role") : null;
    String userName = (session != null) ? (String) session.getAttribute("userName") : null;
%>

<h1>Grade Management System</h1>

<% if (role == null) { %>

    <!-- Not logged in: show the login form -->
    <% if ("1".equals(request.getParameter("error"))) { %>
        <p style="color:red;">Invalid username or password.</p>
    <% } %>

    <h2>Login</h2>
    <form action="LoginServlet" method="POST">
        <label for="role">I am a:</label>
        <select id="role" name="role">
            <option value="student">Student</option>
            <option value="professor">Professor</option>
            <option value="secretary">Secretary</option>
        </select><br><br>

        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required><br><br>

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required><br><br>

        <button type="submit">Log In</button>
    </form>

<% } else { %>

    <!-- Logged in: show a role-specific menu -->
    <h2>Welcome, <%= userName %>!</h2>

    <% if ("student".equals(role)) { %>
        <ul>
            <li><a href="StudentServlet?action=viewGradesByCourse">View grades per course</a></li>
            <li><a href="StudentServlet?action=viewGradesBySemester">View grades per semester</a></li>
            <li><a href="StudentServlet?action=viewOverallGrade">View overall grade (GPA)</a></li>
        </ul>

    <% } else if ("professor".equals(role)) { %>
        <ul>
            <li><a href="ProfessorServlet?action=viewGradedCourse">View grades for a graded course</a></li>
            <li><a href="ProfessorServlet?action=selectCourseToGrade">Enter grades for a course</a></li>
        </ul>

    <% } else if ("secretary".equals(role)) { %>
        <ul>
            <li><a href="SecretaryServlet?action=viewCoursesWithProfessors">View courses (title, semester, professor)</a></li>
            <li><a href="SecretaryServlet?action=showAssignForm">Assign a course to a professor</a></li>
            <li><a href="SecretaryServlet?action=showAddStudentForm">Add new student</a></li>
            <li><a href="SecretaryServlet?action=showAddCourseForm">Add new course</a></li>
        </ul>
    <% } %>

    <br>
    <a href="LogoutServlet">Log out</a>

<% } %>

</body>
</html>