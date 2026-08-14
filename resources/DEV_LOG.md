# Focust - Development Logs (WIP)
Previously, I mentioned all of the problems I was facing, problems I fixed, and features I added through [git commit messages](https://github.com/allandeboe/focust/commits/main/), making them self-documenting. However, I felt like it would be a good idea to make a document within the project to encapsulate all of the changes that I have done in the past few years and in current time within a single document (or series of inter-connected documents) that better describe my thought process.

My progress will be rather slow since this is meant to be a personal project that I work on to demonstrate my skills, not something I depend on financially, so I don't have the kind of urgency to continuously work on the project.

Dates are written as `YYYY-MM-DD`.

## 2026

### 2026-08-14
Roughly 5-6 months ago, when running the tests as per the Jenkins pipeline I set up, I noticed that something was wrong; the Testcontainers mocking the MySQL database weren't being set up. After some digging into the logs present in the Docker containers, [some probing](https://github.com/allandeboe/focust/commits/spring-development?since=2026-02-11&until=2026-02-11), and research, I realized that one of the culprits is a *dependency* issue, with the MySQL database version, constantly being updated to the latest version, not working with older versions of Testcontainers and some other dependencies.

However, that wasn't only problem that I was facing. For instance, the version of Jenkins that I was using (with JDK 17) is *outdated* and needs to have a newer version of JDK, meaning I would need to shut down the docker image, create a *new* docker image for Jenkins that has a newer JDK version, and potentially have to set-up everything again, including credentials and pipelines.

This is also ignoring some of the other plans I want to do for the project, like minimizing the size of the Docker image of the back-end server and potentially using the hardened docker image as a base for both the front-end and back-end to enhance security.

So, here are some of the actions I plan on making at some point within the remaining part of the year:

* Create a new GitHub project for the updated Jenkins docker image (likely named [jenkins-jdk25]()), and archiving the existing [jenkins-jdk17](https://github.com/allandeboe/jenkins-jdk17) project.

* Update the versions of Testcontainers for the back-end server and version-lock the MySQL version used during testing (which might risk having to manually update the versions, which can pose a security risk).

* Figure out how to modify the back-end server Docker image (and the Jenkins pipeline) to best minimize the size of the docker container.

* Incorporate the hardened Docker images into the Dockerfiles for both the front-end and back-end servers.