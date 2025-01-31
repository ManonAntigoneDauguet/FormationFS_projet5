import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBarModule } from '@angular/material/snack-bar';
import { RouterTestingModule, } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from '../../../../services/session.service';

import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { of } from 'rxjs';
import { TeacherService } from 'src/app/services/teacher.service';
import { SessionApiService } from '../../services/session-api.service';
import { DetailComponent } from './detail.component';


describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionService: SessionService;

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

  const mockTeacher = {
    id: 1,
    lastName: "Buffet",
    firstName: "Phoebe",
    createdAt: new Date('2025-01-23T14:45:30'),
    updatedAt: new Date('2025-01-24T14:45:30')
  }

  const mockSessionApiService = {
    detail: jest.fn().mockReturnValue(of(mockSession)),
    participate: jest.fn().mockReturnValue(of({})),
    unParticipate: jest.fn().mockReturnValue(of({})),
    delete: jest.fn().mockReturnValue(of({}))
  };

  const mockTeacherService = {
    detail: jest.fn().mockReturnValue(of(mockTeacher))
  };

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    }
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        MatIconModule,
        MatCardModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: TeacherService, useValue: mockTeacherService }
      ],
    })
      .compileComponents();
    sessionService = TestBed.inject(SessionService);
    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display session and teacher', () => {
    expect(component.session).toEqual(mockSession);
    expect(component.teacher).toEqual(mockTeacher);
  });

  it('should participate', () => {
    const participateSpy = jest.spyOn(mockSessionApiService, "participate");
    // When
    component.participate();
    // Then
    expect(participateSpy).toBeCalledWith(component.sessionId, component.userId);
  })

  it('should remove a participation', () => {
    const unParticipateSpy = jest.spyOn(mockSessionApiService, "unParticipate");
    // When
    component.unParticipate();
    // Then
    expect(unParticipateSpy).toBeCalledWith(component.sessionId, component.userId);
  })

  it('should remove a session', () => {
    const deleteSpy = jest.spyOn(mockSessionApiService, "delete");
    // When
    component.delete();
    // Then
    expect(deleteSpy).toBeCalledWith(component.sessionId);
  })
});

