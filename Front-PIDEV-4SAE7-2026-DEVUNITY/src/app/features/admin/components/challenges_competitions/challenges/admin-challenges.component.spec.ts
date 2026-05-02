/// <reference types="jasmine" />
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { of } from 'rxjs';

import { ReactiveFormsModule } from '@angular/forms';
import { ChallengeService } from '../../../../../core/services/activity/challenge.service';
import { AdminChallengesComponent } from './admin-challenges.component';

// simple stub for service
class DummyChallengeService {
  getAll() {
    return of([]);
  }

  delete() {
    return of(null);
  }
}

describe('AdminChallengesComponent', () => {
  let component: AdminChallengesComponent;
  let fixture: ComponentFixture<AdminChallengesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [AdminChallengesComponent],
      providers: [{ provide: ChallengeService, useClass: DummyChallengeService }],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminChallengesComponent);
    component = fixture.componentInstance;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
