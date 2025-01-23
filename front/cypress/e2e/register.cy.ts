/// <reference types="cypress" />
describe('Register spec', () => {
    it('Register server error', () => {
        cy.visit('/register');

        cy.intercept('POST', '/api/auth/register', Error);

        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type("test!1234");
        cy.get('input[formControlName=firstName]').type("firstName");
        cy.get('input[formControlName=lastName]').type("lastName");

        cy.get('button[type="submit"]').should("not.be.disabled");
        cy.get('button[type="submit"]').click();

        cy.url().should('include', '/register');
        cy.contains('An error occurred');
    })

    it('Register successfull', () => {
        cy.visit('/register');

        cy.intercept('POST', '/api/auth/register', {
            body: {
                email: 'email',
                firstName: 'firstName',
                lastName: 'lastName',
                password: 'password'
            },
        });

        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type("test!1234");
        cy.get('input[formControlName=firstName]').type("firstName");
        cy.get('input[formControlName=lastName]').type("lastName");

        cy.get('button[type="submit"]').should("not.be.disabled");
        cy.get('button[type="submit"]').click();

        cy.url().should('include', '/login');
    })


    it('Register empty field error', () => {
        cy.visit('/register');

        cy.get('button[type="submit"]').should('be.disabled');
    })


    it('Register email field error', () => {
        cy.visit('/register');

        cy.get('input[formControlName=email]').type("yoga")
        cy.get('input[formControlName=password]').type("test!1234");
        cy.get('input[formControlName=firstName]').type("firstName");
        cy.get('input[formControlName=lastName]').type("lastName");

        cy.get('button[type="submit"]').should('be.disabled');
    })


    it('Register password field error', () => {
        cy.visit('/register');

        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type("te");
        cy.get('input[formControlName=firstName]').type("firstName");
        cy.get('input[formControlName=lastName]').type("lastName");

        cy.get('button[type="submit"]').should('be.disabled');
    })


    it('Register firstName field error', () => {
        cy.visit('/register');

        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type("test!1234");
        cy.get('input[formControlName=firstName]').type("fi");
        cy.get('input[formControlName=lastName]').type("lastName");

        cy.get('button[type="submit"]').should('be.disabled');
    })


    it('Register lastName field error', () => {
        cy.visit('/register');

        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type("test!1234");
        cy.get('input[formControlName=firstName]').type("firstName");
        cy.get('input[formControlName=lastName]').type("la");

        cy.get('button[type="submit"]').should('be.disabled');
    })
});