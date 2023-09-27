# Lab: Class 26 - Beginning TaskMaster

## Overview

Today, you’ll start building an Android app that will be a main focus of the second half of the 
course: TaskMaster. While you’ll start small today, over time this will grow to be a fully-featured 
application.

## Setup

To start, create a new directory and repo to hold this app. Name it `taskmaster`. Within that 
directory, use Android Studio to set up a new app, as discussed in class. Create a README file 
that includes, at minimum, a description of your app and a daily change log.

## Resources

[Android Buttons](https://developer.android.com/guide/topics/ui/controls/button.html)

[Android UI Events](https://developer.android.com/guide/topics/ui/ui-events.html)

## Feature Tasks

### Homepage

The main page should be built out to match the wireframe. In particular, it should have a heading 
at the top of the page, an image to mock the “my tasks” view, and buttons at the bottom of the page 
to allow going to the “add tasks” and “all tasks” page.

![Homepage](wireframes/lab26/taskmaster_homepage.png)

### Add a Task

On the “Add a Task” page, allow users to type in details about a new task, specifically a title and 
a body. When users click the “submit” button, show a “submitted!” label on the page.

![Add a Task](wireframes/lab26/taskmaster_add_task.png)

### All Tasks

The all tasks page should just be an image with a back button; it needs no functionality.

![All Tasks](wireframes/lab26/taskmaster_all_tasks.png)

## Documentation

Create a directory called screenshots in the root of your project. Take a screenshot of the homepage 
you’ve created. Use markdown to render the screenshot in your README.

For reference, here’s the markdown syntax to render an image in a page:

`![image description](screenshots/path_to_screenshot_file.png)`

## Testing

In a future lecture, we’ll talk about how to test Android UI using Espresso. For now, ensure that 
you’re writing good unit tests for anything unit-testable in your code.

## Stretch Goals

Consider the styling for this app. Decide on a color scheme and font families to use. (You’ll 
thank yourself later for doing this work now!)
