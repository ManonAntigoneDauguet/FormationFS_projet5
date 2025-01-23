/// <reference types="cypress" />
describe('Login spec', () => {
  it('Login server error', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', Error);

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`);

    cy.url().should('include', '/login');
    cy.contains('An error occurred');
  })


  it('Login successfull', () => {
    cy.visit('/login');

    cy.intercept('POST', '/api/auth/login', {
      statusCode: 200,
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    });

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session');

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type("test!1234");
    cy.get('button[type="submit"]').should("not.be.disabled");
    cy.get('button[type="submit"]').click();

    cy.url().should('include', '/sessions');
  })


  it('Register empty field error', () => {
    cy.visit('/login');

    cy.get('button[type="submit"]').should('be.disabled');
  })


  it('Login email field error', () => {
    cy.visit('/login');

    cy.get('input[formControlName=email]').type("yoga");
    cy.get('input[formControlName=password]').type("test!1234");
    cy.get('button[type="submit"]').should('be.disabled');
  })


  it('Login password field error', () => {
    cy.visit('/login');

    cy.get('input[formControlName=email]').type("yoga@studio.com");
    cy.get('input[formControlName=password]').type("te");
    cy.get('button[type="submit"]').should('be.disabled');
  })
});