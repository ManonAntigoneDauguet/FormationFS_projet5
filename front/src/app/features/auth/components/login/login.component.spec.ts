import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';
import { Router } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let mockSessionService: jest.Mocked<SessionService>;
  let mockAuthService: jest.Mocked<AuthService>;
  let router: Router;

  beforeEach(async () => {
    mockAuthService = { login: jest.fn() } as any;

    mockSessionService = {
      $isLogged: jest.fn(),
      logOut: jest.fn(),
      logIn: jest.fn()
    } as any;

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ],
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: SessionService, useValue: mockSessionService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  /************** Tests on Submit *********************/
  it('should call login and navigate us to "session" when submit is called', () => {
    // Given
    const mockLoginRequest = { email: 'joe@friends.com', password: 'password' };
    const mockSessionInfo = {
      type: "type",
      id: 1,
      token: 'abc123',
      username: "Drake",
      lastName: 'Tribbiani',
      firstName: 'Joe',
      admin: false,
    };
    const loginAuthServiceSpy = jest
      .spyOn(mockAuthService, 'login')
      .mockReturnValue(of(mockSessionInfo));
    const logInSessionServiceSpy = jest.spyOn(mockSessionService, 'logIn');
    const navigateSpy = jest.spyOn(router, 'navigate');
    const submitSpy = jest.spyOn(component, "submit");
    // When
    component.form.setValue(mockLoginRequest);
    component.submit();
    // Then
    expect(submitSpy).toBeCalled();
    expect(loginAuthServiceSpy).toHaveBeenCalledWith(mockLoginRequest);
    expect(logInSessionServiceSpy).toHaveBeenCalledWith(mockSessionInfo);
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  })

  it('should set onError at true when AuthService return an error', () => {
    // Given
    const mockLoginRequest = { email: 'joe@friends.com', password: 'password' };
    const loginAuthServiceSpy = jest
      .spyOn(mockAuthService, 'login')
      .mockReturnValue(throwError(() => new Error));
    const submitSpy = jest.spyOn(component, "submit");
    // When
    component.form.setValue(mockLoginRequest);
    component.submit();
    // Then
    expect(submitSpy).toBeCalled();
    expect(loginAuthServiceSpy).toHaveBeenCalledWith(mockLoginRequest);
    expect(component.onError).toBe(true);
  })

  it('should say invalid form if all fields are not filled', () => {
    // Given
    const compiled = fixture.nativeElement;
    const submitButton = compiled.querySelector('button[type="submit"]');
    // When
    component.form.get('email')?.setValue("");
    component.form.get('password')?.setValue("password");
    fixture.detectChanges();
    // Then
    expect(component.form.invalid).toBeTruthy();
    expect(submitButton.disabled).toBeTruthy();
  })

  it('should say valid form if all fields are correctly filled', () => {
    // Given
    const compiled = fixture.nativeElement;
    const submitButton = compiled.querySelector('button[type="submit"]');
    // When
    component.form.get('email')?.setValue("monica@test.fr");
    component.form.get('password')?.setValue("password");
    fixture.detectChanges();
    // Then
    expect(component.form.valid).toBeTruthy();
    expect(submitButton.disabled).toBeFalsy();
  })

  it('should call the submit method when the form is submited', () => {
    // Given
    const compiled = fixture.nativeElement;
    const submitSpy = jest.spyOn(component, "submit");
    const form = compiled.querySelector('form');
    // When
    component.form.get('email')?.setValue("monica@test.fr");
    component.form.get('password')?.setValue("password");
    fixture.detectChanges();
    form.submit();
    // Then
    expect(submitSpy).toBeCalled();
  })

  /************** Tests on Error message *********************/
  it('should display error message if onError is true', () => {
    component.onError = true;
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    const errorP = compiled.querySelector('.error');
    expect(errorP).toBeTruthy();
  })

  it('should not display error message if onError is false', () => {
    component.onError = false;
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    const errorMessage = compiled.querySelector('.error');
    expect(errorMessage).toBeNull();
  });

  /************** Tests on Email input *********************/
  it('should invalid email input if it has no value', () => {
    // Given
    const ctrl = component.form.get('email');
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.invalid).toBeTruthy();
  })

  it('should invalid email input if the value is not an email', () => {
    // Given
    const ctrl = component.form.get('email');
    // When
    ctrl?.setValue("monica");
    fixture.detectChanges();
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.invalid).toBeTruthy();
  })

  it('should valid email input if it has a correct email', () => {
    // Given
    const ctrl = component.form.get('email');
    // When
    ctrl?.setValue("monica@test.fr");
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.valid).toBeTruthy();
  })

  /************** Tests on Password input *********************/
  it('should invalid password input if it has no value', () => {
    // Given
    const ctrl = component.form.get('password');
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.invalid).toBeTruthy();
  })

  it('should invalid password input if it has a incorrect length', () => {
    // Given
    const ctrl = component.form.get('password');
    // When
    ctrl?.setValue("p");
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.invalid).toBeTruthy();
  })

  it('should valid password input if it has a correct password', () => {
    // Given
    const ctrl = component.form.get('password');
    // When
    ctrl?.setValue("password");
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.valid).toBeTruthy();
  })

});
