import { HttpClient, HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { Session } from '../interfaces/session.interface';
import { SessionApiService } from './session-api.service';

describe('SessionsService', () => {
  let service: SessionApiService;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  const mockSession1 = {
    id: 1,
    description: "description",
    teacher_id: 1,
    date: new Date(),
    name: "session1",
    users: [1, 2],
    createdAt: new Date(),
    updatedAt: new Date()
  }

  const mockSession2 = {
    id: 2,
    description: "description",
    teacher_id: 2,
    date: new Date(),
    name: "session2",
    users: [2],
    createdAt: new Date(),
    updatedAt: new Date()
  }

  const sessions = [mockSession1, mockSession2]

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        HttpClientModule
      ],
      providers: [SessionApiService]
    });
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(SessionApiService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return a list of sessions when "all" is called', (done) => {
    // When
    service.all().subscribe((response) => {
      expect(response).toEqual(sessions),
      done()
    });
    // Then
    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toBe('GET');
    req.flush(sessions);
    httpTestingController.verify();
  })

  it('should return a specific session when "detail" is called', (done) => {
    // When
    service.detail("1").subscribe((response) => {
      expect(response).toEqual(mockSession1), 
      done()
    });
    // Then
    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockSession1);
    httpTestingController.verify(); 
  })

  it('should ask the correct endpoint when "delete" is called', () => {
    // When
    service.delete("1").subscribe();
    // Then
    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
    httpTestingController.verify(); 
  })

  it('should ask the correct endpoint when "create" is called', (done) => {
    // When
    service.create(mockSession1).subscribe((response) => {
      expect(response).toEqual(mockSession1), 
      done()
    });    
    // Then
    const req = httpTestingController.expectOne('api/session');
    expect(req.request.method).toBe('POST');
    req.flush(mockSession1);
    httpTestingController.verify(); 
  })

  it('should ask the correct endpoint when "update" is called', (done) => {
    // When
    service.update("1", mockSession2).subscribe((response) => {
      expect(response).toEqual(mockSession2), 
      done()
    });    
    // Then
    const req = httpTestingController.expectOne('api/session/1');
    expect(req.request.method).toBe('PUT');
    req.flush(mockSession2);
    httpTestingController.verify(); 
  })

  it('should ask the correct endpoint when "participate" is called', () => {
    // When
    service.participate("1", "2").subscribe();
    // Then
    const req = httpTestingController.expectOne('api/session/1/participate/2');
    expect(req.request.method).toBe('POST');
    req.flush(null);
    httpTestingController.verify(); 
  })

  it('should ask the correct endpoint when "unParticipate" is called', () => {
    // When
    service.unParticipate("1", "2").subscribe();
    // Then
    const req = httpTestingController.expectOne('api/session/1/participate/2');
    expect(req.request.method).toBe('DELETE');
    req.flush(null);
    httpTestingController.verify(); 
  })
});
