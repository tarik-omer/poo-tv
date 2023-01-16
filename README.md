<div align="center"><img src="https://ocw.cs.pub.ro/courses/lib/exe/fetch.php?hash=2597cd&media=https%3A%2F%2Fi.imgur.com%2FS1ROjQr.gif" width="300px" alt="POO TV"></div>

<b><font size=6 ><center>POO-TV</center></div></font></b>

Name: **Omer Tarik Ilhan**

Group: **324CA**

## Description

This project represents an implementation of the backend of a
movie streaming web application, similar to Netflix, Disney Plus or
HBO GO. There are several pages the user can switch between, most of which
require a logged-in user to access.

## Usage

First, in order to be able to watch movies, a user must be logged in.
Either log into previous account or create a new account by registering.
Now you have access to the movies that are currently not banned in
you country. You may have either a standard account, or buy a premium
account, having 15 free-premium movies to watch, without spending any
tokens (except the 10 tokens price of the premium membership). A standard
account or an account whose freemium movies were spent, may also buy
movies for just 2 tokens. In order to watch a movie, a user must first
purchase it. After watching, the user may also rate and / or like it.

### Pages

The pages accessible to a logged-out user are the following
(none of which contain any movies):
- Unauthenticated Homepage
- Login
- Register

The only functionalities are login (on Login page) and register
(on Register page).

After logging in, the following pages become available:
- Movies - a page with all the movies; the user may search for a specific movie or
  filter / sort available movies
    - See Details - switched to the details of a specific movie, allowing the user
      to purchase / watch / like / rate it
- Upgrades - the user may buy tokens / a premium account
- Logout - the user logs out of the current account

The user can switch in between these pages in multiple way. For a better
understanding, refer to the following image:

<img src="images/pages.png" width="1000" alt="Page Structure">

### Commands

Possible commands, grouped by the pages they can be accessed on:

- Login Page
    - Login
- Register Page
    - Register
- Upgrades Page
    - Buy Tokens
    - Buy Premium Account
- Movies Page
    - Filter
    - Search
- See Details Page
    - Purchase
    - Watch
    - Rate
    - Like

## Implementation

The implementation consists of a for loop, iterating through the given commands in the input,
in the context provided by the input.

The pages are Singleton classes, which are then provided by a 'factory-like' method that
returns the instance of the required class.

Commands are grouped inside a class, as methods for the said class, being called upon within a
switch case.

Movies are stored inside a database (ArrayList), along with all the registered users (HashTable) as
<Credentials, User> pairs.

When logged in, an instance of the class CurrentSession stores available movies based on the
current  user's country, the current user and current movies visible (movies can be visible
only on Movies Page and on See Details Page).

# Updates
Version 1.1 is now available! New features added!

The added features are:

- 'back' / previous page button - it is possible to return to the last page easier
- new movies - the possibility to add / delete movies from the movie database (database - add and
database - delete commands)
- 'subscribe' feature - it is now possible for a user to subscribe to a specified genre;
he will be then notified when a movie containing said genre is added or removed from the
movie database
- premium user recommendations - premium users will now receive at the end of the session
a movie recommendation based on the liked genres and most popular movies; available only for
the last user that uses the app

## Possible Improvements

- A more general approach using other Design Patters, such as Command or Strategy pattern.
- Better memory efficiency

## Comments

- A well-rounded and balanced project
- Implementation requirements were not very well specified in the task, had to implement based
  on given tests

## Conclusion

A fun project to work on, giving me the opportunity to develop my Design Pattern usage skills,
while also enabling me to work in a more organised manner.

Copyright 2022 - 2023 Omer Tarik Ilhan 324CA
