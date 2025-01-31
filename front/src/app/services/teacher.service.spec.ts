import { HttpClient, HttpClientModule } from '@angular/common/http';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';
import { TeacherService } from './teacher.service';


describe('TeacherService', () => {
  let service: TeacherService;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  const mockTeacher1 = {
    id: 1,
    lastName: "Tribbiani",
    firstName: 'Joe',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const mockTeacher2 = {
    id: 2,
    lastName: 'Buffet',
    firstName: 'Phoebe',
    createdAt: new Date(),
    updatedAt: new Date()
  };

  const teachers = [mockTeacher1, mockTeacher2];

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        HttpClientModule
      ],
      providers: [TeacherService]
    });
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(TeacherService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return a specific teacher when "detail" method is called', (done) => {
    // When
    service.detail("1").subscribe((response) => {
      expect(response).toEqual(mockTeacher1);
      done();
    }
    );
    // Then
    const req = httpTestingController.expectOne('api/teacher/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockTeacher1);
    httpTestingController.verify();
  });

  it('should return an error when detail is called on inexistant teacher', (done) => {
    // Given
    service.detail("1").subscribe(
      response => { fail() },
      error => {
        expect(error.status).toBe(404);
        expect(error.statusText).toBe('Not Found');
        done();
      }
    );
    // Then
    const req = httpTestingController.expectOne('api/teacher/1');
    expect(req.request.method).toBe('GET');
    req.flush('Error', { status: 404, statusText: 'Not Found' });
    httpTestingController.verify();
  })

  it('should return a list of teacher when "all" method is called', (done) => {
    // When
    service.all().subscribe((response) => {
      expect(response).toEqual(teachers);
      done();
    }
    );
    // Then
    const req = httpTestingController.expectOne('api/teacher');
    expect(req.request.method).toBe('GET');
    req.flush(teachers);
    httpTestingController.verify();
  });
});
