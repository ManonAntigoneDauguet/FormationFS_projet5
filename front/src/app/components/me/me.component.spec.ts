import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';
import { expect } from '@jest/globals';
import { MeComponent } from './me.component';
import { User } from 'src/app/interfaces/user.interface';
import { Router } from '@angular/router';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let router: Router;
  let mockSessionService = jest.mocked<SessionService>;

  const mockUser = {
    id: 1,
    email: 'friends@test.fr',
    lastName: 'Tribbiani',
    firstName: 'Joe',
    admin: false,
    password: 'password',
    createdAt: new Date('1994-09-22T00:00:00'),
    updatedAt: new Date('1994-09-22T00:00:00')
  };
  const mockAdminUser = {
    id: 2,
    email: 'phoebe@test.fr',
    lastName: 'Buffet',
    firstName: 'Phoebe',
    admin: true,
    password: 'password',
    createdAt: new Date('1994-09-22T00:00:00'),
    updatedAt: new Date('1994-09-22T00:00:00')
  };

  beforeEach(async () => {
    mockSessionService = {
      sessionInformation: {
        admin: true,
        id: 1
      },
      logOut: jest.fn()
    } as any;

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService }
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    router = TestBed.inject(Router);
  });

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('click on arrow_back button should call the back method', () => {
    // Given
    const backSpy = jest.spyOn(component, 'back');
    // When
    const compiled = fixture.nativeElement;
    const backArrow = compiled.querySelector('button[mat-icon-button]');
    backArrow.click();
    // Then
    expect(backSpy).toBeCalled();
  })

  it('should display user information', () => {
    // Given
    component.user = mockUser;
    // When
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    const nameP = compiled.querySelector('mat-card-content p:nth-child(1)');
    const emailP = compiled.querySelector('mat-card-content p:nth-child(2)');
    // Then
    expect(nameP.textContent).toContain('Name: Joe TRIBBIANI');
    expect(emailP.textContent).toContain('Email: friends@test.fr');
  })

  it('should display "You are admin" with a admin user', () => {
    // Given
    component.user = mockAdminUser;
    // When
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    // Then
    const adminP = compiled.querySelector('mat-card-content p:nth-child(3)');
    expect(adminP.textContent).toContain('You are admin');
  })

  it('click on raise button should call delete method', () => {
    // Given
    component.user = mockUser;
    fixture.detectChanges();
    const deleteSpy = jest.spyOn(component, 'delete');
    const compiled = fixture.nativeElement;
    const raiseButton = compiled.querySelector('button[mat-raised-button]');
    // When
    raiseButton.click();
    // Then
    expect(deleteSpy).toBeCalled();
  })
});
