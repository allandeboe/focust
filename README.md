<picture>
    <source media="(prefers-color-scheme: dark)" srcset="resources/images/focust-banner-white.png" width="512">
    <source media="(prefers-color-scheme: light)" srcset="resources/images/focust-banner-black.png" width="512">
    <img alt="Shows the banner for Focust, using white instead of black in dark mode to make the text in the image viewable." src="resources/images/focust-banner-black.png" width="512">
</picture>

# About

**Focust** (pormanteau of ***focused*** and ***locust***) is an open-source, issue tracker web application.

Built using the [*Spring Framework*](https://spring.io/) (Java) and [*React*](https://react.dev/) (TypeScript), it is my personal flagship project that I made to showcase my full-stack development skills.

# Technologies

* [**Spring Framework**](https://spring.io/) - for the back-end server

* [**React**](https://react.dev/) - for the front-end server (governs the graphical UI). Used the *TypeScript* version for this application.

* [**Tailwind CSS**](https://tailwindcss.com/) - CSS utility classes primarily used on markup (TSX)

* [**Hibernate**](https://hibernate.org/) - object-relational mapping (ORM) tool of choice to ease the creation of the database.

* [**MySQL**](https://www.mysql.com/) - database used.

* [**Docker**](https://www.docker.com/) - used to create images and containers for the needed servers.

* [**Jenkins**](https://www.jenkins.io/) - used to automate the entire build process.

# Building from Source
There are multiple ways to build this application from source code:

* Using [**Jenkins**](https://www.jenkins.io/) to automate the build (**recommended**) - see [`./resources/docs/BUILD_USING_JENKINS.md`](resources/docs/BUILD_USING_JENKINS.md) for more information.

* Using [**Docker**](https://www.docker.com/) & *Docker Compose* to build the application - see [`./resources/docs/BUILD_USING_DOCKER.md`](resources/docs/BUILD_USING_DOCKER.md) for more information.

# License

Focust is licensed under the **[GNU General Public License v3.0](LICENSE)**. To learn more, go to *https://www.gnu.org/licenses/*