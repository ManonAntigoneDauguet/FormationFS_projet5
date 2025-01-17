import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';
import { MeComponent } from './me.component';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  const mockRouter = { navigate: jest.fn() };
  let sessionService: SessionService;

  const mockMatSnackBar = { open: jest.fn() };

  const mockUserService = {
    getById: jest.fn().mockReturnValue(of({
      id: 1,
      email: 'joe@friends.com',
      firstName: 'Joe',
      lastName: 'Tribbiani',
      admin: false,
      createdAt: new Date(),
      updatedAt: new Date()
    })),
    delete: jest.fn().mockReturnValue(of(null))
  };

  beforeEach(async () => {
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
        SessionService,
        { provide: UserService, useValue: mockUserService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar }],
    })
      .compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    sessionService.logIn({
      type: "type",
      id: 1,
      token: 'abc123',
      username: "Drake",
      lastName: 'Tribbiani',
      firstName: 'Joe',
      admin: false,
    })
    fixture.detectChanges(); 
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call delete method and navigate after successful deletion', (async () => {
    // Given
    component.user = {
      id: 1,
      email: 'friends@test.fr',
      lastName: 'Tribbiani',
      firstName: 'Joe',
      admin: false,
      password: 'password',
      createdAt: new Date(),
      updatedAt: new Date()
    };
    fixture.detectChanges();
    const deleteSpy = jest.spyOn(component, 'delete');
    const logOutSpy = jest.spyOn(sessionService, 'logOut');
    const navigateSpy = jest.spyOn(mockRouter, 'navigate');
    const snackBarSpy = jest.spyOn(mockMatSnackBar, 'open');
    const compiled = fixture.nativeElement;
    const raiseButton = compiled.querySelector('button[mat-raised-button]');
    // When
    raiseButton.click();
    await fixture.whenStable();
    // Then
    expect(deleteSpy).toBeCalled();
    expect(logOutSpy).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['/']);
    expect(snackBarSpy).toHaveBeenCalledWith('Your account has been deleted !', 'Close', { duration: 3000 });
  }));

  it('should call the back method when arrow_back button is clicked', () => {
    // Given
    const backSpy = jest.spyOn(component, 'back');
    // When
    const compiled = fixture.nativeElement;
    const backArrow = compiled.querySelector('button[mat-icon-button]');
    backArrow.click();
    // Then
    expect(backSpy).toBeCalled();
  })

  it('should display user information when there are available', () => {
    // Given
    component.user = {
      id: 1,
      email: 'friends@test.fr',
      lastName: 'Tribbiani',
      firstName: 'Joe',
      admin: false,
      password: 'password',
      createdAt: new Date(),
      updatedAt: new Date()
    };
    // When
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    const nameP = compiled.querySelector('mat-card-content p:nth-child(1)');
    const emailP = compiled.querySelector('mat-card-content p:nth-child(2)');
    // Then
    expect(nameP.textContent).toContain('Name: Joe TRIBBIANI');
    expect(emailP.textContent).toContain('Email: friends@test.fr');
  })

  it('should display "You are admin" if the user is an admin', () => {
    // Given
    component.user = {
      id: 2,
      email: 'phoebe@test.fr',
      lastName: 'Buffet',
      firstName: 'Phoebe',
      admin: true,
      password: 'password',
      createdAt: new Date(),
      updatedAt: new Date()
    };
    // When
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    // Then
    const adminP = compiled.querySelector('mat-card-content p:nth-child(3)');
    expect(adminP.textContent).toContain('You are admin');
  })
});
