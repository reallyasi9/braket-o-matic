import { ComponentFixture, TestBed } from '@angular/core/testing';

import { TeamAdderComponent } from './team-adder.component';

describe('TeamAdderComponent', () => {
  let component: TeamAdderComponent;
  let fixture: ComponentFixture<TeamAdderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ TeamAdderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(TeamAdderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
