# Lab 33: Another Day, Another Model

## Overview

Today, your app will add a second resource on your backend, consumed by your frontend.

## Setup

Continue working in your `taskmaster` repository.

### Resources
[One-to-many and many-to-one connections using GraphQL in AWS Amplify](https://docs.amplify.aws/cli/graphql/data-modeling/#has-many-relationship)

## Feature Tasks

### Tasks Are Owned By Teams
Create a second entity for a team, which has a name and a list of tasks. Update your tasks to be owned by a team.

Manually create three teams by running a mutation exactly three times in your code. (You do NOT need to allow the user to create new teams.)

### Add Task Form
Modify your Add Task form to include either a Spinner or Radio Buttons for which team that task belongs to.

### Settings Page
In addition to a username, allow the user to choose their team on the Settings page. Use that Team to display only that team’s tasks on the homepage.

#### Documentation
Update your daily change log with today’s changes.

#### Testing
Add to your Espresso tests some functionality about the new Team entity.

### Screenshots

<img src="../screenshots/lab33/alphaGroupSS.png" alt="Alpha Team Tasks" width="200"/> 
<img src="../screenshots/lab33/bravoGroupSS.png" alt="Bravo Team Tasks" width="200"/> 
<img src="../screenshots/lab33/deltaGroupSS.png" alt="Delta Team Tasks" width="200"/> 
<img src="../screenshots/lab33/awsTaskTableSS.png" alt="AWS Tasks" width="800"/> 
<img src="../screenshots/lab33/awsTeamTableSS.png" alt="AWS Teams" width="800"/> 


#### Instructions
Updating Schema and using Amplify CLI
1. Update schema in amplify/backend/api/taskmaster/schema.graphql
2. Run `amplify codegen models` to update models
3. Run `amplify update api` to ensure conflict detection is off
4. Run `amplify push` to update backend


#### Submission Instructions
* Continue working in your `taskmaster` repo.
* Work on a non-master branch and make commits appropriately.
* Update your README with your changes for today and screenshot of your work.
* Create a pull request to your master branch with your work for this lab.
* Submit the link to that pull request on Canvas. Add a comment with the amount of time you spent on this assignment.

#### Grading Rubric
* 2 pts Team entity added with a reasonable structure
* 2 pts Can add teams and can associate teams with tasks
* 1 pt Show only the selected team’s tasks on homepage
* 1 pt README with description, screenshots, and daily change log