/// <reference types="cypress" />
describe('Account spec', () => {
    const mockSession1 = {
        id: 1,
        name: 'session1',
        description: 'description1',
        date: new Date('2025-02-01T14:45:30'),
        teacher_id: 1,
        users: [2, 3],
        createdAt: new Date('2025-01-23T14:45:30'),
        updatedAt: new Date('2025-01-24T14:45:30')
    };
    const mockSession2 = {
        id: 2,
        name: "session2",
        description: "description2",
        date: new Date('2025-03-01T14:45:30'),
        teacher_id: 1,
        users: [1, 2],
        createdAt: new Date('2025-01-22T14:45:30'),
        updatedAt: new Date('2025-01-23T14:45:30')
    }
    const mockTeacher1 = {
        id: 1,
        lastName: "Buffet",
        firstName: "Phoebe",
        createdAt: new Date('2025-01-23T14:45:30'),
        updatedAt: new Date('2025-01-24T14:45:30')
    }
    const mockTeacher2 = {
        id: 2,
        lastName: "Tribbiani",
        firstName: "Joe",
        createdAt: new Date('2025-01-23T14:45:30'),
        updatedAt: new Date('2025-01-24T14:45:30')
    }

    beforeEach(() => {
        cy.visit('/login');
        cy.intercept('POST', '/api/auth/login', {
            statusCode: 200,
            body: {
                id: 1,
                username: 'userName',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: false
            },
        }).as('login');
        cy.intercept('GET', '/api/session', {
            statusCode: 200,
            body: [mockSession1, mockSession2]
        }).as('sessions');
        cy.get('input[formControlName=email]').type("yoga@studio.com");
        cy.get('input[formControlName=password]').type("test!1234");
        cy.get('button[type="submit"]').should("not.be.disabled");
        cy.get('button[type="submit"]').click();
    })

    it('Display sessions information', () => {
        cy.contains('Rentals available').should('be.visible');

        cy.contains('session1').should('be.visible');
        cy.contains('February 1, 2025').should('be.visible');
        cy.contains('description1').should('be.visible');

        cy.contains('session2').should('be.visible');
        cy.contains('March 1, 2025').should('be.visible');
        cy.contains('description2').should('be.visible');
    })

    it('Display details of one session', () => {
        cy.intercept('GET', '/api/session/*', {
            statusCode: 200,
            body: mockSession1
        }).as('session');
        cy.intercept('GET', '/api/teacher/*', {
            statusCode: 200,
            body: mockTeacher1
        }).as('teacher');

        cy.get('.items .item').eq(0).within(() => {
            cy.contains('button', 'Detail').click();
        });

        cy.contains('Session1').should('be.visible');
        cy.contains('February 1, 2025').should('be.visible');
        cy.contains('description1').should('be.visible');
        cy.contains('Create at: January 23, 2025').should('be.visible');
        cy.contains('Last update: January 24, 2025').should('be.visible');
        cy.contains('2 attendees').should('be.visible');
        cy.contains('Phoebe BUFFET').should('be.visible');
    })

    it('Authorize participate at a session', () => {
        cy.intercept('GET', '/api/session/*', {
            statusCode: 200,
            body: mockSession1
        }).as('session');
        cy.intercept('GET', '/api/teacher/*', {
            statusCode: 200,
            body: mockTeacher1
        }).as('teacher');
        cy.intercept('POST', '/api/session/1/participate/1', {
            statusCode: 200
        }).as('participate');

        cy.get('.items .item').eq(0).within(() => {
            cy.contains('button', 'Detail').click();
        });

        cy.get('[data-cy="participate"]').contains('Participate').should('be.visible').click();
        cy.wait('@participate').its('request.method').should('eq', 'POST');
    })

    it('Authorize remove a participation from a session', () => {
        cy.intercept('GET', '/api/session/*', {
            statusCode: 200,
            body: mockSession2
        }).as('session');
        cy.intercept('GET', '/api/teacher/*', {
            statusCode: 200,
            body: mockTeacher1
        }).as('teacher');
        cy.intercept('DELETE', '/api/session/2/participate/1', {
            statusCode: 200
        }).as('participate');

        cy.get('.items .item').eq(1).within(() => {
            cy.contains('button', 'Detail').click();
        });

        cy.get('[data-cy="unParticipate"]').contains('Do not participate').should('be.visible').click();
        cy.wait('@participate').its('request.method').should('eq', 'DELETE');
    })

    it('Not authorize delete a session', () => {
        cy.intercept('GET', '/api/session/*', {
            statusCode: 200,
            body: mockSession1
        });

        cy.contains('button', 'Delete').should('not.exist');
    })

    it('Not authorize edit a session', () => {
        cy.intercept('GET', '/api/session/*', {
            statusCode: 200,
            body: mockSession1
        }).as('session');

        cy.contains('button', 'Edit').should('not.exist');
    })

    it('Not authorize create a session', () => {
        cy.contains('button[routerLink="create"').should('not.exist');
    })
})