import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PickemTeamComponent } from './pickem-team.component';

describe('PickemTeamComponent', () => {
  let component: PickemTeamComponent;
  let fixture: ComponentFixture<PickemTeamComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PickemTeamComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PickemTeamComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
