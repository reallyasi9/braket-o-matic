import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TournamentStepperComponent } from './tournament-stepper.component';

describe('TournamentStepperComponent', () => {
  let component: TournamentStepperComponent;
  let fixture: ComponentFixture<TournamentStepperComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TournamentStepperComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TournamentStepperComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
