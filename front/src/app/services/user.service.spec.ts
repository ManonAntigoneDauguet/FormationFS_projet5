import { HttpClient, HttpClientModule, HttpErrorResponse } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService } from './user.service';

describe('UserService', () => {
  let service: UserService;
  let httpClient: HttpClient;
  let httpTestingController: HttpTestingController;

  let mockUser = {
    id: 1,
    email: 'friends@test.fr',
    lastName: 'Tribbiani',
    firstName: 'Joe',
    admin: false,
    password: 'password',
    createdAt: new Date(),
    updatedAt: new Date()
  }

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        HttpClientTestingModule,
        HttpClientModule
      ],
      providers: [UserService]
    });
    httpClient = TestBed.inject(HttpClient);
    httpTestingController = TestBed.inject(HttpTestingController);
    service = TestBed.inject(UserService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should return a specific user when getById is called', (done) => {
    // Given
    service.getById("1").subscribe((response) => {
      expect(response).toEqual(mockUser),
        done()
    });
    // Then
    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');
    req.flush(mockUser);
    httpTestingController.verify();
  })

  it('should return an error when getById is called on inexistant user', (done) => {
    // Given
    service.getById("1").subscribe(
      response => {fail()},
      error => {
        expect(error.status).toBe(404);
        expect(error.statusText).toBe('Not Found');
        done();
      }
    );
    // Then
    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toBe('GET');
    req.flush('Error', { status: 404, statusText: 'Not Found' });
    httpTestingController.verify();
  })

  it('should return a specific user when getById is called', (done) => {
    // Given
    service.delete("1").subscribe((response) => {
      expect(response).toEqual(mockUser),
        done()
    });
    // Then
    const req = httpTestingController.expectOne('api/user/1');
    expect(req.request.method).toBe('DELETE');
    req.flush(mockUser);
    httpTestingController.verify();
  })
});
