import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import { StudentChallengesComponent } from './student-challenges.component';

describe('StudentChallengesComponent', () => {
  let component: StudentChallengesComponent;
  let fixture: ComponentFixture<StudentChallengesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [StudentChallengesComponent],
      imports: [HttpClientTestingModule],
      schemas: [NO_ERRORS_SCHEMA]
    })
    .compileComponents();

    fixture = TestBed.createComponent(StudentChallengesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
