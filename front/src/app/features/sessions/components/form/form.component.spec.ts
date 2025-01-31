import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';

import { Router } from '@angular/router';
import { of } from 'rxjs';
import { FormComponent } from './form.component';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let sessionApiService: SessionApiService;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  }

  const mockSession = {
    id: 1,
    name: 'session1',
    description: 'description1',
    date: new Date('2025-02-01T14:45:30'),
    teacher_id: 1,
    users: [1, 2],
    createdAt: new Date('2025-01-23T14:45:30'),
    updatedAt: new Date('2025-01-24T14:45:30')
  };

  const mockObservableSession = of(mockSession);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FormComponent],
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        SessionApiService
      ],
    }).compileComponents();

    router = TestBed.inject(Router);
    sessionApiService = TestBed.inject(SessionApiService);
    fixture = TestBed.createComponent(FormComponent);
  });

  it('should create', () => {
    component = fixture.componentInstance;
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should navigate us to "session" if the connected user is not an admin', () => {
    // Given
    const navigateSpy = jest.spyOn(router, 'navigate');
    mockSessionService.sessionInformation.admin = false;
    // When
    component = fixture.componentInstance;
    fixture.detectChanges();
    // Then
    expect(navigateSpy).toHaveBeenCalledWith(['/sessions']);
  })

  it('should display session information if url contains "update"', () => {
    // Given
    jest.spyOn(mockObservableSession, 'subscribe');
    jest.spyOn(sessionApiService, 'detail').mockReturnValue(mockObservableSession);
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions/update');
    // When
    component = fixture.componentInstance;
    fixture.detectChanges();
    // Then
    expect(component.onUpdate).toBe(true);
    expect(sessionApiService.detail).toBeCalled();
    expect(mockObservableSession.subscribe).toBeCalled();
    const compiled = fixture.nativeElement;
    const h1 = compiled.querySelector('h1');
    expect(h1.textContent).toContain('Update session');
  })

  it('should open a new form if url not contains "update"', () => {
    // Given
    jest.spyOn(router, 'url', 'get').mockReturnValue('/sessions');
    // When
    component = fixture.componentInstance;
    fixture.detectChanges();
    // Then
    const compiled = fixture.nativeElement;
    const h1 = compiled.querySelector('h1');
    expect(h1.textContent).toContain('Create session');
    const form = compiled.querySelector('form');
    expect(form).toBeTruthy();
  })

  it('should create a session when "submit" is called with "onUpdate" at true', () => {
    // Given
    const createSpy = jest.spyOn(sessionApiService, 'create');
    component = fixture.componentInstance;
    component.onUpdate = false;
    fixture.detectChanges();
    // When
    component.submit();
    // Then
    expect(createSpy).toBeCalled();
  })

  it('should update a session when "submit" is called with "onUpdate" at false', () => {
    // Given
    const updateSpy = jest.spyOn(sessionApiService, "update");
    component = fixture.componentInstance;
    component.onUpdate = true;
    fixture.detectChanges();
    // When
    component.submit();
    // Then
    expect(updateSpy).toBeCalled();
  })
});
