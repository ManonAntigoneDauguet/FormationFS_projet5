/// <reference types="cypress" />
describe('Account spec', () => {
    const mockSession1 = {
        id: 1,
        name: 'session1',
        description: 'description1',
        date: new Date('2025-02-01T14:45:30'),
        teacher_id: 1,
        users: [1, 2],
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
                admin: true
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
        });
        cy.intercept('GET', '/api/teacher/*', {
            statusCode: 200,
            body: mockTeacher1
        });

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

    it('Authorize delete a session', () => {
        cy.intercept('GET', '/api/session/*', {
            statusCode: 200,
            body: mockSession1
        });
        cy.intercept('DELETE', '/api/session/*', {
            statusCode: 200,
        }).as('delete');

        cy.get('.items .item').eq(0).within(() => {
            cy.contains('button', 'Detail').click();
        });
        
        cy.contains('button', 'Delete').click();
        cy.wait('@delete');
        cy.contains('Session deleted !').should('be.visible');
        cy.location('pathname').should('eq', '/sessions');
    })

    it('Authorize edit a session', () => {
        cy.intercept('GET', '/api/session/*', {
            statusCode: 200,
            body: mockSession1
        }).as('session');
        cy.intercept('GET', '/api/teacher', {
            statusCode: 200,
            body: [mockTeacher1, mockTeacher2]
        }).as('teachers');
        cy.intercept('PUT', '/api/session/*', {
            statusCode: 200,
            body: mockSession1
        }).as('update');

        cy.contains('button', 'Edit').click();
        cy.wait('@teachers');

        // Should display a form with actual session information
        cy.get('input[formControlName=name]').should('have.value', 'session1')
        cy.get('input[formControlName=date]').should('have.value', '2025-02-01');
        cy.get('mat-select[formControlName=teacher_id]').should('contain.text', 'Phoebe Buffet')
        cy.get('textarea[formControlName=description]').should('have.value', 'description1');

        // Should update session information
        cy.get('input[formControlName=name]').clear().type('new name');
        cy.get('input[formControlName=date]').clear().type('2025-01-22');
        cy.get('textarea[formControlName=description]').clear().type('new description');

        // Should display the available teachers
        cy.get('mat-select[formControlName=teacher_id]').click();
        cy.get('mat-option').eq(0).should('contain.text', 'Phoebe Buffet');
        cy.get('mat-option').eq(1).should('contain.text', 'Joe Tribbiani');
        cy.get('mat-option').eq(1).click();

        // Should submit updated information
        cy.get('button[type="submit"]').should("not.be.disabled");
        cy.get('button[type="submit"]').click();
        cy.contains('Session updated !');
        cy.location('pathname').should('eq', '/sessions');
    })

    it('Authorize create a session', () => {
        cy.intercept('GET', '/api/teacher', {
            statusCode: 200,
            body: [mockTeacher1, mockTeacher2]
        }).as('teachers');
        cy.intercept('POST', '/api/session', {
            statusCode: 200,
            body: mockSession2
        }).as('create');

        cy.get('button[routerLink="create"').click();

        // Should update session information
        cy.get('input[formControlName=name]').clear().type('name');
        cy.get('input[formControlName=date]').clear().type('2025-01-22');
        cy.get('textarea[formControlName=description]').clear().type('description');

        // Should display the available teachers
        cy.get('mat-select[formControlName=teacher_id]').click();
        cy.get('mat-option').eq(0).should('contain.text', 'Phoebe Buffet');
        cy.get('mat-option').eq(1).should('contain.text', 'Joe Tribbiani');
        cy.get('mat-option').eq(1).click();

        // Should submit new session
        cy.get('button[type="submit"]').should("not.be.disabled");
        cy.get('button[type="submit"]').click();
        cy.contains('Session created !');
        cy.location('pathname').should('eq', '/sessions');
    })

    it('Create session empty field error', () => {
        cy.intercept('GET', '/api/teacher', {
            statusCode: 200,
            body: [mockTeacher1, mockTeacher2]
        }).as('teachers');
        cy.intercept('POST', '/api/session/*', {
            statusCode: 200,
            body: mockSession2
        }).as('create');

        cy.get('button[routerLink="create"').click();

        cy.get('button[type="submit"]').should("be.disabled");
    })
})