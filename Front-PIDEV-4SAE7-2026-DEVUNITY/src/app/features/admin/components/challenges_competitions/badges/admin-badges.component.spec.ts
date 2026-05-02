import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { AdminBadgesComponent } from './admin-badges.component';

describe('BadgesComponent', () => {
  let component: AdminBadgesComponent;
  let fixture: ComponentFixture<AdminBadgesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminBadgesComponent],
      imports: [HttpClientTestingModule],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminBadgesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
