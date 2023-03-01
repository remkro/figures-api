![logo_cutted](https://user-images.githubusercontent.com/105795682/222110035-ddc229ae-0dca-4b6a-8e7f-d9071bb9cd7e.jpg)


<h2>About</h2>

This api allows you to manage shapes. By default it supports three shapes: circle, square and rectangle. The application has been designed in such a way that handling new shapes involves only adding new classes without modifying the existing ones.

<h2>Functionalities</h2>

There is an endpoint for user registration available for everyone, the other endpoints are secured with jwt token. User can add new shapes to the system. 
User default role is CREATOR, but there is also a role ADMIN. 

User can add shapes to the system, providing shape type and its parameters (like radius, width, height etc.) available for users with role CREATOR. The system returns created resource with provided data and also with calculated values of area and perimeter.

Users can perform complex filtered search of shapes by multimple parameters, for example: 

<code>createdBy=...&type=...&areaFrom=...&areaTo...&perimeterFrom=...&perimeterTo=...&widthFrom=...&widthTo=...&radiusFrom=...&radiusTo=...</code>

Users can modify shapes and the endpoint is available only for ADMIN and user who created particular shape. The system saves information about each change, storing data like date of change, who made change, old valus and new values etc.

There is also the endpoint to get all users (with pagination) and each resource has basic data about user like username, email, sign-up date, roles, number of shapes created. 

<h2>Technologies</h2>
<ul>
<li>Java 17</li>
<li>Spring Framework</li>
<li>Hibernate</li>
<li>JWT Token authentication</h2>
<li>H2 (development)</li>
<li>PostgreSQL (production)</li>
<li>JUnit 5 (unit tests)</li>
<li>Testcontainers (integration tests)</li>
<li>Docker</li>
</ul>

<h2>Work in progress</h2>

Currently working on:

<ul>
<li>Writing tests</li>
<li>Fixing filtered search on production database</li>
</ul>


