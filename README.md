<picture>
    <source media="(prefers-color-scheme: dark)" srcset="resources/images/focust-banner-white.png" width="512">
    <source media="(prefers-color-scheme: light)" srcset="resources/images/focust-banner-black.png" width="512">
    <img alt="Shows the banner for Focust, using white instead of black in dark mode to make the text in the image viewable." src="resources/images/focust-banner-black.png" width="512">
</picture>

# About

**Focust** (pormanteau of ***focused*** and ***locust***) is an open-source, issue tracker web application.

Built using the [*Spring Framework*](https://spring.io/) (Java) and [*React*](https://react.dev/) (TypeScript), it is my personal flagship project that I made to showcase my full-stack development skills.

# Overview of Technologies
The following table contains more general technologies that apply to the application as a whole rather than being more related to either back-end or front-end servers:

| Technology | Description |
|--- |--- |
| [**Docker**](https://www.docker.com/) | used to create images and containers for the needed servers. |
| [**Jenkins**](https://www.jenkins.io/) | used to automate the entire build process. |

Here is a brief list of technologies used for the [back-end server](./spring). If you want more details regarding the back-end server, please read the [`README.md`](./spring/README.md) file in the `./spring` directory.
| Back-end Technology | Description |
|--- |--- |
| [**Spring Framework**](https://spring.io/) | Main back-end framework of choice. Chosen to primarily demonstrate my existing skills in [Java](https://www.java.com/en/). |
| [**MySQL**](https://www.mysql.com/) | Database of choice. Could've easily chosen [PostgreSQL](https://www.postgresql.org/) |
| [**Hibernate**](https://hibernate.org/) | object-relational mapping (ORM) tool of choice to ease the creation of the database, as well as ensuring the mocked database has the same structure as the main database. |
| [**Testcontainers**](https://testcontainers.com/) | Used to mock the MySQL database for testing purposes. |
| [**Mockito**](https://site.mockito.org/) | Used to mock specific services that application uses |

Here is a brief list of technologies used for the [front-end server](./react). If you want more details regarding the front-end server, please read the [`README.md`](./react/README.md) file in the `./react` directory.

| Front-end Technology | Description |
|--- |--- |
| [**React**](https://react.dev/) | Used for the front-end server. [TypeScript](https://www.typescriptlang.org/) is used to here. |
| [**Tailwind CSS**](https://tailwindcss.com/) | contains common CSS utility classes that are agnostic to the overall structure of the application. |
| [**Vite**](https://vite.dev/) | dev server that JavaScript/TypeScript files together as well as have hot module replacement to view changes to the front-end in real time. |

# Building from Source
There are multiple ways to build this application from source code:

* Using [**Jenkins**](https://www.jenkins.io/) to automate the build (**recommended**) - see [`./resources/docs/BUILD_USING_JENKINS.md`](resources/docs/BUILD_USING_JENKINS.md) for more information.

* Using [**Docker**](https://www.docker.com/) to build the application - see [`./resources/docs/BUILD_USING_DOCKER.md`](resources/docs/BUILD_USING_DOCKER.md) for more information.


# License
Focust is licensed under the **[GNU General Public License v3.0](LICENSE)**. To learn more, go to *https://www.gnu.org/licenses/*