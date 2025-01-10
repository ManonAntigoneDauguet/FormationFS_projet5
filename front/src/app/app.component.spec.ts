import { HttpClientModule } from '@angular/common/http';
import { TestBed, ComponentFixture } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';
import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';
import { Router } from '@angular/router';


describe('AppComponent', () => {
  let app: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let mockSessionService: jest.Mocked<SessionService>;
  let router: Router;

  beforeEach(async () => {
    mockSessionService = {
      $isLogged: jest.fn(),
      logOut: jest.fn()
    } as any;

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([]),
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [AppComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService }
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    app = fixture.componentInstance;
    router = TestBed.inject(Router);
  });

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('should create the app', () => {
    expect(app).toBeTruthy();
  });


  it('should return the session state', (done) => {
    // Given
    mockSessionService.$isLogged.mockReturnValue(of(true));
    // Then
    app.$isLogged().subscribe((isLogged) => {
      expect(isLogged).toBe(true);
      done();
    });
  });

  it('should display session, account and logout when $isLogged() at true ', () => {
    // Given
    mockSessionService.$isLogged.mockReturnValue(of(true));
    // When
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    const sessionSpan = compiled.querySelector('span[routerLink="sessions"]');
    const accountSpan = compiled.querySelector('span[routerLink="me"]');
    const logoutLink = compiled.querySelector('span.link:nth-child(3)');
    // Then
    expect(sessionSpan.textContent).toContain('Sessions');
    expect(accountSpan.textContent).toContain('Account');
    expect(logoutLink.textContent).toContain('Logout');
  })

  it('should display login and register when $isLogged() at false ', () => {
    // Given
    mockSessionService.$isLogged.mockReturnValue(of(false));
    // When
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    const loginSpan = compiled.querySelector('span[routerLink="login"]');
    const registerSpan = compiled.querySelector('span[routerLink="register"]');
    // Then
    expect(loginSpan.textContent).toContain('Login');
    expect(registerSpan.textContent).toContain('Register');
  })

  it('should navigate us to root when logOut is called', () => {
    const navigateSpy = jest.spyOn(router, 'navigate');
    // When
    app.logout();
    // Then
    expect(mockSessionService.logOut).toHaveBeenCalled();
    expect(navigateSpy).toHaveBeenCalledWith(['']);
  })

  it('should call the logout method when Logout button is clicked', () => {
    // Given
    const logoutSpy = jest.spyOn(app, 'logout');
    mockSessionService.$isLogged.mockReturnValue(of(true));
    // When
    fixture.detectChanges();
    const compiled = fixture.nativeElement;
    const logoutLink = compiled.querySelector('span.link:nth-child(3)');
    logoutLink.click();
    // Then
    expect(logoutSpy).toBeCalled();
  })
});
