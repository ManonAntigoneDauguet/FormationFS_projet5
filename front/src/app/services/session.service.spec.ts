import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { SessionService } from './session.service';

describe('SessionService', () => {
  let service: SessionService;

  const mockSessionInfo = {
    type: "type",
    id: 1,
    token: 'abc123',
    username: "Drake",
    lastName: 'Tribbiani',
    firstName: 'Joe',
    admin: false,
  };

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [SessionService]
    });
    service = TestBed.inject(SessionService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should contain and return user information on a successful login', () => {
    // When
    service.logIn(mockSessionInfo);
    // Then
    expect(service.isLogged).toBe(true);
    expect(service.sessionInformation).toEqual(mockSessionInfo);
    service.$isLogged().subscribe(
      isLogged => expect(isLogged).toBe(true)
    )
  })

  it('should not contain user information on a successful logout', () => {
    // Given
    service.logIn(mockSessionInfo);
    // When
    service.logOut();
    // Then
    expect(service.isLogged).toBe(false);
    expect(service.sessionInformation).toEqual(undefined);
    service.$isLogged().subscribe(
      isLogged => expect(isLogged).toBe(true)
    )
  }) 
});
