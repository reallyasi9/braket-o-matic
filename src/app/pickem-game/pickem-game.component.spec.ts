import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PickemGameComponent } from './pickem-game.component';

describe('PickemGameComponent', () => {
  let component: PickemGameComponent;
  let fixture: ComponentFixture<PickemGameComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ PickemGameComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(PickemGameComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
