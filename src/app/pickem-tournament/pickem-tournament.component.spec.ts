import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PickemTournamentComponent } from './pickem-tournament.component';

describe('PickemTournamentComponent', () => {
  let component: PickemTournamentComponent;
  let fixture: ComponentFixture<PickemTournamentComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PickemTournamentComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PickemTournamentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
