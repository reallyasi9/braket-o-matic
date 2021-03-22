import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { generateTeam } from '../mock-teams';
import { Team } from '../team';
import { TeamService } from '../team.service';
import { v4 as uuidv4 } from 'uuid';

@Component({
  selector: 'app-team-editor',
  templateUrl: './team-editor.component.html',
  styleUrls: ['./team-editor.component.css'],
})
export class TeamEditorComponent implements OnInit {
  teams: Team[] = [];

  constructor(private ts: TeamService) {}

  ngOnInit(): void {
    this.ts.getTeams(environment.tournamentId).subscribe(
      (teams) => (this.teams = teams),
      (error) => console.log(error)
    );
  }

  addTeam(): void {
    const newTeam = {
      id: "",
      name: "",
      primaryColor: "#000000",
      accentColor: "#FFFFFF",
      active: true,
    }
    this.teams.push(newTeam);
  }

  deleteTeam(team: Team): void {
    this.teams = this.teams.filter(t => t != team);
  }
}
