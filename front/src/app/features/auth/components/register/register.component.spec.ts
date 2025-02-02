import { HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { AuthService } from '../../services/auth.service';
import { RegisterComponent } from './register.component';
import { SessionApiService } from 'src/app/features/sessions/services/session-api.service';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  const mockRouter = { navigate: jest.fn() };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        HttpClientTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        RouterTestingModule.withRoutes([
          { path: 'login', component: RegisterComponent }
        ])
      ],
      providers: [
        AuthService,
        { provide: Router, useValue: mockRouter }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  /************** Tests on Submit *********************/
  it('should call register and navigate us to "login" when submit is called', () => {
    let httpTestingController: HttpTestingController;
    httpTestingController = TestBed.inject(HttpTestingController);
    // Given
    const mockSessionInfo = {
      lastName: 'Tribbiani',
      firstName: 'Joe',
      email: 'joe@friends.com',
      password: 'password'
    };
    const navigateSpy = jest.spyOn(mockRouter, 'navigate');
    const submitSpy = jest.spyOn(component, "submit");
    // When
    component.form.setValue(mockSessionInfo);
    component.submit();
    // Then
    expect(submitSpy).toBeCalled();
    const req = httpTestingController.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    req.flush(null);
    expect(navigateSpy).toHaveBeenCalledWith(['/login']);
    httpTestingController.verify();
  })

  it('should set onError at true when AuthService return an error', () => {
    let httpTestingController: HttpTestingController;
    httpTestingController = TestBed.inject(HttpTestingController);
    // Given
    const mockSessionInfo = {
      lastName: 'Tribbiani',
      firstName: 'Joe',
      email: 'joe@friends.com',
      password: 'password'
    };
    const submitSpy = jest.spyOn(component, "submit");
    // When
    component.form.setValue(mockSessionInfo);
    component.submit();
    const req = httpTestingController.expectOne('api/auth/register');
    expect(req.request.method).toBe('POST');
    req.flush('Error', { status: 500, statusText: 'Internal Server Error' });
    // Then
    expect(submitSpy).toBeCalled();
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
    component.form.get('firstName')?.setValue("Monica");
    component.form.get('lastName')?.setValue("Geller");
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

  /*******************
   * Test on Inputs
  *******************/

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

  it('should invalid password input if it has a too short length', () => {
    // Given
    const ctrl = component.form.get('password');
    // When
    ctrl?.setValue("p");
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.invalid).toBeTruthy();
  })

  it('should invalid password input if it has a too long length', () => {
    // Given
    const ctrl = component.form.get('password');
    // When
    ctrl?.setValue("paaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaassword");
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

  /************** Tests on FirstName input *********************/
  it('should invalid firstName input if it has no value', () => {
    // Given
    const ctrl = component.form.get('firstName');
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.invalid).toBeTruthy();
  })

  it('should invalid firstName input if it has a too short length', () => {
    // Given
    const ctrl = component.form.get('firstName');
    // When
    ctrl?.setValue("f");
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.invalid).toBeTruthy();
  })

  it('should invalid firstName input if it has a too long length', () => {
    // Given
    const ctrl = component.form.get('firstName');
    // When
    ctrl?.setValue("fiiiiiiiiiiiiiiiiiiiiiirstName");
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.invalid).toBeTruthy();
  })

  it('should valid firstName input if it has a correct firstName', () => {
    // Given
    const ctrl = component.form.get('firstName');
    // When
    ctrl?.setValue("firstName");
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.valid).toBeTruthy();
  })

  /************** Tests on LastName input *********************/
  it('should invalid lastName input if it has no value', () => {
    // Given
    const ctrl = component.form.get('lastName');
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.invalid).toBeTruthy();
  })

  it('should invalid lastName input if it has a too short length', () => {
    // Given
    const ctrl = component.form.get('lastName');
    // When
    ctrl?.setValue("f");
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.invalid).toBeTruthy();
  })

  it('should invalid lastName input if it has a too long length', () => {
    // Given
    const ctrl = component.form.get('lastName');
    // When
    ctrl?.setValue("laaaaaaaaaaaaaaaaaaaaastName");
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.invalid).toBeTruthy();
  })

  it('should valid lastName input if it has a correct lastName', () => {
    // Given
    const ctrl = component.form.get('lastName');
    // When
    ctrl?.setValue("lastName");
    // Then
    expect(ctrl).toBeTruthy();
    expect(ctrl?.valid).toBeTruthy();
  })
});
