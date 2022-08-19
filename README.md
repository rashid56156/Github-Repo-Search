# Github-Repo-Search
Android Application to demonstrate the implementation of Github search API

## Table of Contents

- [App](#app)
- [TechStack](#techstack)
- [Screenshots](#screenshots)
- [Api](#api)
- [Architecture](#architecture)
- [Libraries](#libraries)

## App
GitHub Repo Search allows you to search for different repositories.

Features:

Search for GitHub repositories and returns a list of 10 matching repositories.
Scroll to the bottom to fetch another 10 repositories and so on.

## TechStack
The project uses Kotlin language. For network requests, it uses Retrofit for network calls.
Dagger2 has been used for Dependency injection.
LiveData and Kotlin Coroutines are also used in the project.

## Screenshots
![Screenshot](screenshot_a.png)
![Screenshot](screenshot_b.png)

## Api
App uses [Github Search API](https://docs.github.com/en/rest/search#search-repositories) to search repositories.

## Architecture
The project is built using MVVM pattern. MVVM allows for the separation of concern which also makes testing easier.

## Libraries
Libraries used in the whole application are:

- [Dagger2](https://dagger.dev/dev-guide/) - Used for Dependency injection
- [Retrofit](https://square.github.io/retrofit/) - Turns your HTTP API into a Java interface.



