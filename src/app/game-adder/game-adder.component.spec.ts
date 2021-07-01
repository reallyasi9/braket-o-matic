import { ComponentFixture, TestBed } from '@angular/core/testing';

import { GameAdderComponent } from './game-adder.component';

describe('GameAdderComponent', () => {
  let component: GameAdderComponent;
  let fixture: ComponentFixture<GameAdderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ GameAdderComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(GameAdderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
