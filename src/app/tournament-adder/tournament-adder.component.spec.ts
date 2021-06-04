import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TournamentAdderComponent } from './tournament-adder.component';

describe('TournamentAdderComponent', () => {
  let component: TournamentAdderComponent;
  let fixture: ComponentFixture<TournamentAdderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TournamentAdderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TournamentAdderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
