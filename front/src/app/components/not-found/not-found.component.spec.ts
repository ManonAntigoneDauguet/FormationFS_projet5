import { ComponentFixture, TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { NotFoundComponent } from './not-found.component';

describe('NotFoundComponent', () => {
  let component: NotFoundComponent;
  let fixture: ComponentFixture<NotFoundComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NotFoundComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NotFoundComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should dipslay "Page not found !" in the center of the page', () => {
    const compiled = fixture.nativeElement;
    const h1 = compiled.querySelector('h1');
    expect(h1.textContent).toContain('Page not found !');
    const div = compiled.querySelector('div');
    expect(div.classList).toContain('flex');
    expect(div.classList).toContain('justify-center');
  })
});
