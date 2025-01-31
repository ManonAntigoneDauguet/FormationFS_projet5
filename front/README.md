# Yoga App !

This project was generated with [Angular CLI](https://github.com/angular/angular-cli) version 14.1.0.

This projet is the front part of a global project.   
To see the global information about this projet, go [there](../README.md) !

## How to install and run the project

Go to the "front" folder.

`npm install` allows you to install dependencies.  
`npm run start` allows you to launch the front-end.   

You can now go to the web site [Yoga-app](http://localhost:4200/) !  

By default the admin account is:
- login: yoga@studio.com
- password: test!1234

## How to run the test

### Unitary tests

Launching test:

`npm run test` allows you to run all the unitary tests.  
`npm run test:watch` allows you to follow change.  
`node_modules/.bin/jest --coverage` allows you to display the coverage report.

### E2E test

`npm run e2e` allows you to launchthe e2e test.   

When the Cypress util is open, choose a browser for the tests and `Start E2E Testing in`.   
Run the "index" spec, into `index.cy.ts` if you want to run all the tests and have the entire coverage.

After launching e2e test, `npm run e2e:coverage` allows you to generate coverage report.
Report is available in [coverage/lcov-report/index.html](./coverage/lcov-report/index.html). Lauch the html page to display the visual report.