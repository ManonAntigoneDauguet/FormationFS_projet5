/// <reference types="cypress" />
describe('Account spec', () => {
    beforeEach(() => {
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
        }).as('create');
        cy.intercept('GET', '/api/session', {
            statusCode: 200,
            body: []
        }).as('session');
        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type("test!1234");
        cy.get('button[type="submit"]').should("not.be.disabled");
        cy.get('button[type="submit"]').click();
    })

    it('Display admin user information', () => {
        cy.intercept('GET', '/api/user/*', {
            statusCode: 200,
            body: {
                email: 'phoebe@test.fr',
                lastName: 'Buffet',
                firstName: 'Phoebe',
                password: 'password',
                admin: true,
                createdAt: '2025-01-23T14:45:30',
                updatedAt: '2025-01-24T14:45:30'
            },
        }).as('getUserById');

        cy.get('span[routerLink="me"]').click();
        cy.wait('@getUserById');

        // Should display
        cy.contains('Phoebe BUFFET').should('be.visible');
        cy.contains('phoebe@test.fr').should('be.visible');
        cy.contains('Create at: January 23, 2025').should('be.visible');
        cy.contains('Last update: January 24, 2025').should('be.visible');
        cy.contains('You are admin').should('be.visible');

        // Should not display
        cy.contains('Delete my account').should('not.exist');
        cy.get('button span mat-icon').contains('delete').should('not.exist');
    })

    it('Display not admin user information', () => {
        cy.intercept('GET', '/api/user/*', {
            statusCode: 200,
            body: {
                lastName: 'Tribbiani',
                firstName: 'Joe',
                email: 'joe@friends.com',
                password: 'password',
                admin: false,
                createdAt: '2025-01-23T14:45:30',
                updatedAt: '2025-01-24T14:45:30'
            },
        }).as('getUserById')

        cy.get('span[routerLink="me"]').click();
        cy.wait('@getUserById');

        // Should display
        cy.contains('Joe TRIBBIANI').should('be.visible');
        cy.contains('joe@friends.com').should('be.visible');
        cy.contains('Create at: January 23, 2025').should('be.visible');
        cy.contains('Last update: January 24, 2025').should('be.visible');
        cy.contains('Delete my account').should('be.visible');
        cy.get('[data-cy="delete-button"]').contains('delete').should('be.visible');

        // Should not display
        cy.contains('You are admin').should('not.exist');
    })

    it('Authorize delete a not admin user', () => {
        cy.intercept('GET', '/api/user/*', {
            statusCode: 200,
            body: {
                lastName: 'Tribbiani',
                firstName: 'Joe',
                email: 'joe@friends.com',
                password: 'password',
                admin: false,
                createdAt: '2025-01-23T14:45:30',
                updatedAt: '2025-01-24T14:45:30'
            },
        }).as('getUserById');
        cy.intercept('DELETE', '/api/user/*', {
            statusCode: 200,
        }).as('delete');

        cy.get('span[routerLink="me"]').click();
        cy.wait('@getUserById');

        cy.get('[data-cy="delete-button"]').contains('delete').should('be.visible');
        cy.get('[data-cy="delete-button"]').click();
        cy.wait('@delete');
        cy.contains('Your account has been deleted !');
        cy.location('pathname').should('eq', '/');
    })

    it('Authorize logout', () => {
        cy.get('[data-cy="logout-button"]').click();
        cy.location('pathname').should('eq', '/');
    })
})

